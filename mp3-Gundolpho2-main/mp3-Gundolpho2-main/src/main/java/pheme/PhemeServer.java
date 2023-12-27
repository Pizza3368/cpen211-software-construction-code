package pheme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import security.AESCipher;
import security.BlowfishCipher;
import timedelayqueue.PubSubMessage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * A server that can allow multiple users to message each other and interact with the Twitter API
 * <p>
 * Rep invariant:
 *      users have unique userName, is not null and consists of key-value pairs >= 0
 *      twitterCredentialsFile is not null
 *      Inherits the rep invariants of TimeDelayQueue and TwitterListener
 *
 * <p>
 * AF(PhemeServer) = a collection of twitter-related, authenticated (on server end)
 *                    operations for a group of Users who each has their own
 *                    twitterListener, timeDelay queues.
 *
 * <p>
 * Thread safety argument:
 *     Each server has a local instance of phemeService which establishes resource confinement and prevents
 *     data resources.
 *     Handling multiple clients at one time will cause interleaving, but because the interwoven operations answer to
 *     individual user's database (TwitterListener, TimeDelayQueue), the operations are independent and do not cause
 *     data races.
 *     In the case that multiple requests are sent from the same user, the methods to perform are synchronized in
 *     TwitterListener class, so no data race occurs.
 * </p>
 */
public class PhemeServer {
    private static final String KEY = "Many years later, as he faced the firing squad, "
        + "Colonel Aureliano Buendía was to remember that distant afternoon when "
        + "his father took him to discover ice.";
    public static final int PORT = 4949;
    private static final Map<String, String> userToHashedPassword = Collections.synchronizedMap(new HashMap<>());
    private ServerSocket serverSocket;
    private final int portNumber;
    private final File twitterCredentials;
    private final PhemeService phemeService;

    /**
     * Initiates a server that communicates between multiple clients and fulfills specified
     * twitter operations
     *
     * @param portNumber port number, requires 0 <= port <= 65535
     * @param twitterCredentials
     */
    public PhemeServer(int portNumber, File twitterCredentials) {
        this.portNumber = portNumber;
        this.twitterCredentials = twitterCredentials;
        phemeService = new PhemeService(twitterCredentials);
    }

    /**
     * Starts the server
     *
     * @return true if the server was successfully started or is already running
     *         false otherwise
     */
    public boolean startServer() {
        if (this.portNumber != PORT) {
            return false;
        }

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Run the server, listening for connections and handling them.
     * Code courtesy of
     * <a href="https://github.com/CPEN-221/FibonacciServer/blob/master/src/main/java/fibonacci/
     * FibonacciServerMulti.java">FibonacciServer</a>
     *
     * @throws IOException if the main server socket is broken
     */
    public void serve() throws IOException {
        while (true) {
            // block until a client connects
            final Socket socket = serverSocket.accept();
            // create a new thread to handle that client
            Thread handler = new Thread(new Runnable() {
                public void run() {
                    try {
                        try {
                            handle(socket);
                        } finally {
                            socket.close();
                        }
                    } catch (IOException ioe) {
                        // this exception wouldn't terminate serve(),
                        // since we're now on a different thread, but
                        // we still need to handle it
                        ioe.printStackTrace();
                    }
                }
            });
            // start the thread
            handler.start();
        }
    }

    /**
     * Handles the client request. Run the server, listening for connections and handling them.
     * Precondition: server is already started and running
     *
     * @param socket connection from client side, is not null.
     *
     * @throws IOException if connection encounters an error
     *
     */
    public void handle(Socket socket) throws IOException {
        System.out.println("Client connected!");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(
            socket.getInputStream())); PrintWriter out = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream()), true)) {
            // each request is a single line containing a number
            for (String encryptedRequest = in.readLine(); encryptedRequest != null; encryptedRequest = in.readLine()) {
                // decrypt using AES cipher
                AESCipher aes = new AESCipher(KEY);
                String decryptedRequest = aes.decrypt(encryptedRequest);

                // extract plaintext passwords and other relevant info from JSON file
                Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
                Utils.RequestInstance request = gson.fromJson(decryptedRequest, Utils.RequestInstance.class);
                System.out.println("Request: " + gson.toJson(request));

                // process different operations
                int requestId = request.requestId;
                boolean success = false;
                Object response = null;

                try {
                    if (request.operation.equals("addUser")) {
                        String hashedPW = BlowfishCipher.hashPassword(request.parameters.password,
                            BlowfishCipher.gensalt());
                        UUID userID = request.parameters.userID;
                        String username = request.parameters.userName;
                        boolean result = phemeService.addUser(userID, username, hashedPW);
                        if (result) {
                            // add user to server for future authentication purposes
                            userToHashedPassword.put(username, hashedPW);
                            response = "User added to the server!";
                        } else {
                            response = "User cannot be added to server";
                        }
                        success = true;
                    } else {
                        if (authenticate(request.parameters.userName, request.parameters.password)) {
                            String username = request.parameters.userName;
                            String hashedPassword = userToHashedPassword.get(username);
                            switch (request.operation) {
                                case "removeUser" -> response = phemeService.removeUser(username, hashedPassword);
                                case "addSubscription", "cancelSubscription" -> { // overloaded method
                                    String twitterUser = request.parameters.twitterUserName;
                                    if (request.parameters.pattern == null) {
                                        if (request.operation.equals("addSubscription")) {
                                            response = phemeService.addSubscription(username,
                                                hashedPassword, twitterUser);
                                        } else {
                                            response = phemeService.cancelSubscription(username,
                                                hashedPassword, twitterUser);
                                        }
                                    } else {
                                        String pattern = request.parameters.pattern;
                                        if (request.operation.equals("addSubscription")) {
                                            response = phemeService.addSubscription(username,
                                                hashedPassword, twitterUser, pattern);
                                        } else {
                                            response = phemeService.cancelSubscription(username,
                                                hashedPassword, twitterUser, pattern);
                                        }
                                    }
                                }
                                case "sendMessage" -> {
                                    PubSubMessage message = request.parameters.msg;
                                    response = phemeService.sendMessage(username, hashedPassword, message);
                                }
                                case "isDelivered" -> {
                                    if (request.parameters.userList != null) {
                                        response = phemeService.isDelivered(request.parameters.msgID,
                                            request.parameters.userList);

                                    } else {
                                        response = phemeService.isDelivered(request.parameters.msgID,
                                            request.parameters.user);
                                    }
                                }
                                case "isUser" -> response = phemeService.isUser(request.parameters.userName);
                                case "getNext" -> response = phemeService.getNext(username, hashedPassword);
                                case "getAllRecent" -> response = phemeService.getAllRecent(username, hashedPassword);
                                default -> { } // do nothing
                            }
                            success = true;
                        } else {
                            response = "Authentication failed";
                            // success already set to false when first initializing it
                        }
                    }
                    // compose reply in JSON format and send back to client
                    Utils.ReplyInstance reply = new Utils.ReplyInstance(requestId, success, response);
                    String replyInJson = gson.toJson(reply);
                    String encryptedReply = aes.encrypt(replyInJson);
                    System.out.println("Reply: " + encryptedReply);
                    out.println(encryptedReply);
                } catch (Exception e) {
                    // mal-formed requests will automatically result in operation failure
                    System.err.println("Reply: Error");
                    out.print("Error\n");
                }
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                    System.err.println("Sleep not working");
                }
            }
        }
    }

    /**
     * Performs authentication for user-specific operations.
     *
     * @param userName name of user to authenticate, is not null
     * @param candidatePassword plaintext password provided by the request from client-side, is not null
     * @return true if password matches the stored hashPassword in server, false otherwise
     */
    public static boolean authenticate(String userName, String candidatePassword) {
        String storedHash = userToHashedPassword.get(userName);
        if (storedHash == null) {
            return false;
        }
        return (BlowfishCipher.verifyPassword(candidatePassword, storedHash));
    }

    /**
     * Start a PhemeServer running on the default port.
     */
    public static void main(String[] args) {
        try {
            PhemeServer server = new PhemeServer(PORT, new File("secret/credentials.json"));
            if (server.startServer()) {
                server.serve();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
