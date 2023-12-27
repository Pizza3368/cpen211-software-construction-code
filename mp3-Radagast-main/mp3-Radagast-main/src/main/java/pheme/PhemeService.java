package pheme;

import com.google.gson.Gson;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import timedelayqueue.MessageType;
import timedelayqueue.PubSubMessage;
import twitter.TwitterListener;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// TODO: write a description for this class
// TODO: complete all methods, irrespective of whether there is an explicit TODO or not
// TODO: write clear specs
// TODO: State the rep invariant and abstraction function
// TODO: what is the thread safety argument?
public class PhemeService {

    public static final int DELAY = 1000; // 1 second or 1000 milliseconds
    private File twitterCredentialsFile;
    private Vector<User> UserList;
    private ConcurrentHashMap<String, ConcurrentHashMap<String, String>> userSubscription;



    public PhemeService(File twitterCredentialsFile) {
        this.twitterCredentialsFile = twitterCredentialsFile;
        UserList = new Vector<>();
        userSubscription = new ConcurrentHashMap<>();
    }

    public void saveState(String configDirName) {

    }

    /**
     *Adding user to sever
     * @param userID,userName,hashPassword are not null
     * @return true if addUser successfully
     */
    public synchronized boolean addUser(UUID userID, String userName, String hashPassword) {
        User newUser = new User(userID, userName, hashPassword, DELAY);
        boolean duplicate = false;
        for (User x : UserList) {
            if (x.userName.equals(userName)) {
                duplicate = true;
            }
        }
        if (!duplicate) {
            UserList.add(newUser);
            userSubscription.put(userName, new ConcurrentHashMap<>());
            return true;
        }
        return false;
    }
    /**
     *Removing user to sever
     * @param userName,hashPassword are not null
     * @return true if removeUser successfully
     */
    public synchronized boolean removeUser(String userName, String hashPassword) {
        for (User x : UserList) {
            if (x.userName == userName) {
                UserList.remove(x);
                userSubscription.remove(userName);
                return true;
            }
        }
        return false;
    }

    /**
     *cancel user in sever user's subscription without pattern
     * @param userName,hashPassword,twitterUserName are not null
     * @return true if cancelSubscription successfully
     */
    public synchronized boolean cancelSubscription(String userName,
                                      String hashPassword,
                                      String twitterUserName) {
        int index = -1;
        ConcurrentHashMap<String, String> subscriptionMap = userSubscription.get(userName);

        User[] userData = new User[UserList.size()];
        UserList.copyInto(userData);
        for (int i = 0; i < userData.length && index == -1; i++) {
            if (userData[i].userName.equals(userName)) {
                index = i;
            }
        }
        if (index == -1) {
            return false;
        }
        boolean compare = hashPassword == userData[index].hashPassword;
        if (!compare) {
            return false;
        }


        if (!subscriptionMap.keySet().contains(twitterUserName)) {
            return false;
        } else {
            subscriptionMap.remove(twitterUserName);
        }
        userSubscription.put(userName, subscriptionMap);
        return true;
    }

    /**
     *cancel user in sever user's subscription with pattern
     * @param userName,hashPassword,twitterUserName,pattern are not null
     * @return true if cancelSubscription successfully
     */
    public synchronized boolean cancelSubscription(String userName,
                                      String hashPassword,
                                      String twitterUserName,
                                      String pattern) {
        int index = -1;
        ConcurrentHashMap<String, String> subscriptionMap = userSubscription.get(userName);


        User[] userData = new User[UserList.size()];
        UserList.copyInto(userData);
        for (int i = 0; i < userData.length && index == -1; i++) {
            if (userData[i].userName.equals(userName)) {
                index = i;
            }
        }
        if (index == -1) {
            return false;
        }
        boolean compare = hashPassword == userData[index].hashPassword;
        if (!compare) {
            return false;
        }


        if (!subscriptionMap.keySet().contains(twitterUserName) && !subscriptionMap.get(twitterUserName).equals(pattern)) {
            return false;
        } else {
            subscriptionMap.remove(twitterUserName);
        }
        userSubscription.put(userName, subscriptionMap);
        return true;
    }

    /**
     *Add user in sever user's subscription without pattern
     * @param userName,hashPassword,twitterUserName are not null
     * @return true if addSubscription successfully
     */
    public synchronized boolean addSubscription(String userName, String hashPassword,
                                   String twitterUserName) {
        int index = -1;
        ConcurrentHashMap<String, String> subscriptionMap = userSubscription.get(userName);

        User[] userData = new User[UserList.size()];
        UserList.copyInto(userData);
        for (int i = 0; i < userData.length && index == -1; i++) {
            if (userData[i].userName.equals(userName)) {
                index = i;
            }
        }
        if (index == -1) {
            return false;
        }
        boolean compare = hashPassword == userData[index].hashPassword;
        if (!compare) {
            return false;
        }

        if (subscriptionMap.keySet().contains(twitterUserName)) {
            return false;
        } else {
            subscriptionMap.put(twitterUserName, "");
        }

        userSubscription.put(userName, subscriptionMap);
        return true;
    }

    /**
     *Add user in sever user's subscription with pattern
     * @param userName,hashPassword,twitterUserName,pattern are not null
     * @return true if addSubscription successfully
     */
    public synchronized boolean addSubscription(String userName, String hashPassword,
                                   String twitterUserName,
                                   String pattern) {
        int index = -1;
        ConcurrentHashMap<String, String> subscriptionMap = userSubscription.get(userName);

        User[] userData = new User[UserList.size()];
        UserList.copyInto(userData);
        for (int i = 0; i < userData.length && index == -1; i++) {
            if (userData[i].userName.equals(userName)) {
                index = i;
            }
        }
        if (index == -1) {
            return false;
        }
        boolean compare = hashPassword == userData[index].hashPassword;
        if (!compare) {
            return false;
        }


        if (subscriptionMap.keySet().contains(twitterUserName) && subscriptionMap.get(twitterUserName).equals(pattern)) {
            return false;
        } else {
            subscriptionMap.put(twitterUserName, pattern);
        }

        userSubscription.put(userName, subscriptionMap);
        return true;
    }
    /**
     *Send message from a server user to a list of user
     * @param userName,hashPassword,msg are not null
     * @return true if sendMessage successfully
     */
    public synchronized boolean sendMessage(String userName,
                               String hashPassword,
                               PubSubMessage msg) {
        boolean validSender = true;
        boolean allValidReceiver = true;
        int count = 0;
        UUID sender = msg.getSender();
        List<UUID> receiver = msg.getReceiver();
        for (UUID y : receiver) {
            for (User x : UserList) {
                if (x.userID.equals(y)) {
                    x.UserInbox.add(msg);
                    count++;
                }
            }
        }
        if (count != receiver.size()) {
            allValidReceiver = false;
        }
        for (User x : UserList) {
            if (x.userID.equals(sender)) {
                if (x.hashPassword == hashPassword) {
                    validSender = true;
                }
            }
        }
        if (validSender && allValidReceiver) {
            return true;
        } else return false;
    }

    /**
     *check if the msg is seen by other receivers
     * @param msgID,userList are not null, msg should have been sent successfully
     * @return true if the msg is seen by other user
     */
    public synchronized List<Boolean> isDelivered(UUID msgID, List<UUID> userList) {
        List<Boolean> isDelivered = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            UUID y = userList.get(i);
            isDelivered.add(true);
            for (User x : UserList) {
                if (x.userID == y) {
                    List<PubSubMessage> allMsg = new ArrayList<>(x.UserInbox.allMsg());
                    for (PubSubMessage z : allMsg) {
                        if (z.getId() == msgID) {
                            isDelivered.remove(i);
                            isDelivered.add(false);
                        }
                    }
                }
            }
        }
            return isDelivered;
    }

    /**
     *check if the msg is seen by one receiver
     * @param msgID,userList are not null, msg should have been sent successfully
     * @return true if the msg is seen by other user
     */
    public synchronized boolean isDelivered(UUID msgID, UUID user) {
        for (User x : UserList) {
            if (x.userID == user) {
                List<PubSubMessage> allMsg = new ArrayList<>(x.UserInbox.allMsg());
                for (PubSubMessage z : allMsg) {
                    if (z.getId() == msgID) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     *check if a user is the user in server
     * @param userName is not null
     * @return true if the useName isUser
     */
    public synchronized boolean isUser(String userName) {
        if (UserList.stream().filter(o -> o.userName.equals(userName)).findFirst().isPresent()) {
            return true;
        }
        return false;
    }

    /**
     *check if a user is the user in server
     * @param userName is not null
     * @return true if the useName isUser
     */
    public synchronized PubSubMessage getNext(String userName, String hashPassword) {
        PubSubMessage nextMsg = PubSubMessage.NO_MSG;
        for (User x : UserList) {
            boolean compare = hashPassword == x.hashPassword;
            if (x.userName.equals(userName) && compare) {
                x.UserInbox.sortQueueByTime();
                nextMsg = x.UserInbox.getNext();
            }
        }
        return nextMsg;
    }

    /**
     *getAllMessage for a user including other user's message and subscribed tweets update since last fetched
     * @param userName,hashPassword is not null
     * @return a List of PubSubMessage in time order
     */
    public synchronized List<PubSubMessage> getAllRecent(String userName, String hashPassword) {
        boolean validUser = false;
        List<PubSubMessage> listOfPSMsg = new ArrayList<>();
        for (User x : UserList) {
            if (x.userName.equals(userName)) {
                if (hashPassword == x.hashPassword) {
                    validUser = true;
                } else {
                    validUser = false;
                }

                if (validUser) {
                    LocalDateTime lastFetchedTime = x.lastFetchedTime;
                    TwitterListener tw = new TwitterListener(twitterCredentialsFile, lastFetchedTime);
                    for (String y : userSubscription.get(userName).keySet()) {
                        tw.addSubscription(y, userSubscription.get(userName).get(y));
                    }
                    List<TweetV2.TweetData> tweets = tw.getRecentTweets();
                    for (TweetV2.TweetData z : tweets) {

                        UUID msgID = UUID.randomUUID();
                        UUID fromTwitter = UUID.randomUUID();
                        ZonedDateTime zdt = ZonedDateTime.of(z.getCreatedAt(), ZoneId.systemDefault());
                        Timestamp tp = new Timestamp(zdt.toInstant().toEpochMilli());
                        MessageType msgtp = new MessageType() {
                            @Override
                            public String getDescription() {
                                return "TweetData";
                            }
                        };
                        Gson gson = new Gson();
                        String twitterText = gson.toJson(z);
                        PubSubMessage pbm = new PubSubMessage(msgID, tp, fromTwitter, x.userID, twitterText, msgtp);
                        x.UserInbox.add(pbm);
                    }

                    x.UserInbox.sortQueueByTime();

                        PubSubMessage nextMsg = x.UserInbox.getNext();
                        while (!nextMsg.equals(PubSubMessage.NO_MSG)) {
                            listOfPSMsg.add(nextMsg);
                            nextMsg = x.UserInbox.getNext();
                        }
                    LocalDateTime newTime = LocalDateTime.now();
                        x.updateLastFetchedTime(newTime);
                }
            }
        }
        return listOfPSMsg;
    }
}
