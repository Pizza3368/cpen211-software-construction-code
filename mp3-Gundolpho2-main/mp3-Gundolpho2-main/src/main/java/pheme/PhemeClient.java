package pheme;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import security.AESCipher;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

/**
 * PhemeClient is a client that sends requests to the PhemeServer
 * and interprets its replies.
 * <p>
 * A new PhemeClient  is "open" until the close() method is called,
 * at which point it is "closed" and may not be used further.
 */

public class PhemeClient {
    private static final String key = "Many years later, as he faced the firing squad, "
            + "Colonel Aureliano Buendía was to remember that distant afternoon when "
            + "his father took him to discover ice.";
    private Socket socket;
    private BufferedReader in;
    // Rep invariant: socket, in, out != null
    private PrintWriter out;

    /**
     * Make a PhemeClient and connect it to a server running on
     * hostname at the specified port.
     *
     * @throws IOException if can't connect
     */
    public PhemeClient(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    /**
     * Uses a PhemeServer instance to send some requests
     */
    public static void main(String[] args) {
        try {
            PhemeClient client = new PhemeClient("127.0.0.1",
                    PhemeServer.PORT);

            // send a request to the server
            UUID userID = UUID.randomUUID();
            String username = "CPEN221";
            String password = "WhyIsCPEN221SoHardLol";
            Utils.RequestInstance.Parameters parameters = new Utils.RequestInstance.Parameters(userID,
                    username, password);

            Utils.RequestInstance request = new Utils.RequestInstance(1, "addUser", parameters);
            Gson gson = new GsonBuilder().create();
            String jsonRequest = gson.toJson(request);

            AESCipher aes = new AESCipher(key);
            String encryptedRequest = aes.encrypt(jsonRequest);

            client.sendRequest(encryptedRequest);
            System.out.println("Original request sent: " + jsonRequest);
            System.out.println("Encrypted request sent: " + encryptedRequest);

            // collect the reply
            String reply = client.getReply();
            String decryptedReply = aes.decrypt(reply);
            System.out.println("Original reply from server: " + reply);
            System.out.println("Decrypted reply from server: " + decryptedReply);

            client.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Send a request to the server. Requires this is "open".
     *
     * @param jsonRequest to send to server
     * @throws IOException if network or server failure
     */
    public void sendRequest(String jsonRequest) throws IOException {
        out.print(jsonRequest + "\n");
        out.flush(); // important! make sure the request actually gets sent
    }

    /**
     * Get a reply from the next request that was submitted.
     * Requires this is "open".
     *
     * @return the requested operation number
     * @throws IOException if network or server failure
     */
    public String getReply() throws IOException {
        String reply = in.readLine();
        if (reply == null) {
            throw new IOException("Connection terminated unexpectedly");
        }

        try {
            return reply;
        } catch (Exception e) {
            throw new IOException("Mis-formatted reply: " + reply);
        }
    }

    /**
     * Closes the client's connection to the server.
     * This client is now "closed". Requires this is "open".
     *
     * @throws IOException if close fails
     */
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
