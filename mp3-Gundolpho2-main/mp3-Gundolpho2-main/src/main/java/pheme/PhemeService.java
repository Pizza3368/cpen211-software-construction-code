package pheme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import timedelayqueue.BasicMessageType;
import timedelayqueue.PubSubMessage;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

/**
 * PhemeService interacts with PhemeServer to process client requests
 * <p>
 * Rep invariant:
 *      users have unique userName, is not null and consists of key-value pairs >= 0
 *      twitterCredentialsFile is not null
 *      Inherits the rep invariants of TimeDelayQueue and TwitterListener
 *
 * <p>
 * AF(PhemeService) = a collection of twitter-related, authenticated (on server end)
 *                    operations for a group of Users who each has their own
 *                    twitterListener, timeDelay queues.
 *
 * <p>
 * Thread safety argument:
 *     PhemeService is used by phemeServer. Each server has its own instance of PhemeService and is not shared
 *     across servers, establishes resource confinement and prevents data resources.
 *     Synchronization: addUser method is synchronized.
 *     Any iteration done over the collections structure are local in nature and does not incur data races.
 * </p>
 */
public class PhemeService {
    private final File twitterCredentialsFile;
    private final Map<String, User> users;

    /**
     * Maintains a group of Users and allows them users to subscribe/unsubscribe to
     * specific Twitter posts and to communicate among each other
     *
     * @param twitterCredentialsFile a JSON file containing the secret tokens and keys
     *                               needed to access the Twitter developer API
     */
    public PhemeService(File twitterCredentialsFile) {
        this.twitterCredentialsFile = twitterCredentialsFile;
        users = Collections.synchronizedMap(new HashMap<>());
    }

    public void saveState(String configDirName) { }

    /**
     * Adds user to PhemeService
     *
     * @param userID the unique ID of userName, is not null
     * @param userName name of user to be removed, is not null
     * @param hashPassword hashed version of the password for userName as a token
     *                     is not null
     * @return true if a new userName is successfully added from PhemeService, false otherwise
     */
    public synchronized boolean addUser(UUID userID, String userName, String hashPassword) {
        if (isUser(userName)) { // user already in this server
            return false;
        }

        User userObj = new User(userID, userName, hashPassword);
        users.put(userName, userObj);
        return true;
    }

    /**
     * Removes a user from PhemeService
     *
     * @param userName name of user to be removed, is not null
     * @param hashPassword hashed version of the password for userName as a token
     *                     is not null
     * @return true if userName is successfully removed from PhemeService, false otherwise
     */
    public synchronized boolean removeUser(String userName, String hashPassword) {
        if (!isUser(userName)) {
            return false;
        }

        users.remove(userName);
        return true;
    }

    /**
     * Cancels a previous user-specific subscription for a Twitter user
     *
     * @param userName name of user that cancels the subscription, is not null
     * @param hashPassword hashed version of the password for userName as a token
     *                     is not null
     * @param twitterUserName name of user to be unsubscribed from, is not null
     * @return true if subscription is successfully canceled for userName,
     *         false if otherwise
     */
    public boolean cancelSubscription(String userName,
                                      String hashPassword,
                                      String twitterUserName) {
        // remove subscription.
        User user = users.get(userName);
        return user.getListener().cancelSubscription(twitterUserName);
    }

    /**
     * Cancels a previous user-specific, pattern-specific subscription for a Twitter user.
     *
     * @param userName name of user that cancels the subscription
     * @param hashPassword hashed version of the password for userName as a token
     *                     is not null
     * @param twitterUserName name of user to be unsubscribed from, is not null
     * @param pattern tweets that match the pattern will be unsubscribed, is not null.
     *                A match is an exact match of strings but ignoring case
     * @return true if subscription is successfully canceled for userName, false if otherwise
     */
    public boolean cancelSubscription(String userName,
                                      String hashPassword,
                                      String twitterUserName,
                                      String pattern) {
        // remove subscription.
        User user = users.get(userName);
        return user.getListener().cancelSubscription(twitterUserName, pattern);
    }

    /**
     * Adds a previous user-specific subscription for a Twitter user.
     *
     * @param userName name of user that adds the subscription, is not null
     * @param hashPassword hashed version of the password as a token
     *                     is not null
     * @param twitterUserName name of user to be subscribed to, is not null
     * @return true if subscription is successfully added for userName, false if otherwise
     */
    public boolean addSubscription(String userName, String hashPassword,
                                   String twitterUserName) {
        // add subscription.
        User user = users.get(userName);
        return user.getListener().addSubscription(twitterUserName);
    }

    /**
     * Adds a previous user-specific subscription for a Twitter user.
     *
     * @param userName name of user that adds the subscription, is not null
     * @param hashPassword hashed version of the password for userName as a token
     *                     is not null
     * @param twitterUserName name of user to be subscribed to, is not null
     * @param pattern tweets that match the pattern will be subscribed, is not null.
     *                A match is an exact match of strings but ignoring case
     * @return true if subscription is successfully added for userName, false if otherwise
     */
    public boolean addSubscription(String userName, String hashPassword,
                                   String twitterUserName,
                                   String pattern) {
        // add subscription.
        User user = users.get(userName);
        return user.getListener().addSubscription(twitterUserName, pattern);
    }

    /**
     * Sends a message to all valid users specified
     *
     * @param userName name of user to send, is not null
     * @param hashPassword hashed version of the password for userName as a token,
     *                     is not null
     * @param msg the message to send, is not null
     * @return true if the message has been sent to every user in the list of specified
     *         recipients, false otherwise
     */
    public synchronized boolean sendMessage(String userName,
                               String hashPassword,
                               PubSubMessage msg) {
        if (!isUser(userName)) {
            return false;
        }

        boolean valid = true;

        List<UUID> recipients = msg.getReceiver();
        Set<String> existingUsers = users.keySet();
        Map<UUID, User> basket = new HashMap<>();

        for (String u: existingUsers) {
            basket.put(users.get(u).getUserID(), users.get(u));
        }
        // send msg to all valid recipients
        for (UUID id : recipients) {
            if (!basket.containsKey(id)) {
                valid = false;
            } else {
                basket.get(id).getTdq().add(msg);
                basket.get(id).getMessageHasSeen().add(id);
            }
        }
        return valid;
    }

    /**
     * Checks whether a message has been delivered to a list of users,
     *
     * @param msgID id of the pubSubMessage to be checked, is not null
     * @param userList id of users who are the message recipients, is not null
     * @return a list L where index i corresponds to the user at true userList[i]
     *         L[i] = true if the msg is delivered to userList[i], false otherwise
     */
    public synchronized List<Boolean> isDelivered(UUID msgID, List<UUID> userList) {
        List<Boolean> result = new ArrayList<>();
        for (UUID u: userList) {
            if (isDelivered(msgID, u)) {
                result.add(true);
            } else {
                result.add(false);
            }
        }
        return result;
    }

    /**
     * Checks whether a message has been delivered to user
     *
     * @param msgID id of the pubSubMessage to be checked, is not null
     * @param user id of user who is the message recipient, is not null
     * @return true if the msg is delivered to the user, false otherwise
     */
    public synchronized boolean isDelivered(UUID msgID, UUID user) {
        Set<String> existingUsers = users.keySet();
        Map<UUID, User> basket = new HashMap<>();

        for (String u: existingUsers) {
            basket.put(users.get(u).getUserID(), users.get(u));
        }

        List<UUID> readMsg = new ArrayList<>(basket.get(user).getTdq().getHaveSeen());
        return readMsg.contains(msgID);
    }

    /**
     * Checks if a user exists in this PhemeService
     *
     * @param userName name of user, is not null
     * @return true if userName exists in PhemeService, false otherwise
     */
    public boolean isUser(String userName) {
        // checks whether this user exists in PhemeService
        return users.containsKey(userName);
    }

    /**
     * Get the next message for a specified user, requires authentication
     *
     * @param userName  name of user that gets the next message, is not null
     * @param hashPassword hashed version of the password for userName as a token
     *                     is not null
     * @return next message for the user, return PubSubMessage.NO_MSG if authentication fails
     *         or there exists no such accessible message for the user at this time point.
     */
    public PubSubMessage getNext(String userName, String hashPassword) {
        updateTDQ(userName);
        return users.get(userName).getTdq().getNext();
    }

    /**
     * Update user's TimeDelayQueue by merging the
     * message stream and the tweet stream
     *
     * @param userName name of user to perform the operation
     *                 is not null
     */
    private synchronized void updateTDQ(String userName) {
        User user = users.get(userName);
        List<TweetV2.TweetData> tweets = user.getListener().getRecentTweets();
        // converts all tweets to psm
        for (TweetV2.TweetData t : tweets) {
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            String jsonTweet = gson.toJson(t);

            PubSubMessage psm = new PubSubMessage(new UUID(Long.parseLong(t.getId()), Long.parseLong(t.getId())),
                Timestamp.valueOf(t.getCreatedAt()),
                new UUID(Long.parseLong(t.getAuthorId()), Long.parseLong(t.getAuthorId())),
                user.getUserID(), jsonTweet, BasicMessageType.TWEET);

            user.getTdq().add(psm);
        }
    }

    /**
     * Updates all recent messages for a user since their last fetch from the Twitter API
     * and returns their stored recent messages
     *
     * @param userName name of user to get their most recent messages, is not null
     * @param hashPassword hashed version of the password for userName as a token
     *                     is not null
     * @return a collection of available messages since the last time this operation was performed,
     *         ordered from the earliest to the latest.
     */
    public synchronized List<PubSubMessage> getAllRecent(String userName, String hashPassword) {
        User user = users.get(userName);
        updateTDQ(user.getUserName());

        // next step: get messages from tdq since last fetch
        List<PubSubMessage> psm = new ArrayList<>();
        // while there are more accessible msg to fetch
        while (true) {
            PubSubMessage msg = user.getTdq().getNext();
            if (msg.equals(PubSubMessage.NO_MSG)) {
                break;
            } else {
                psm.add(msg);
            }
        }
        return psm;
    }
}
