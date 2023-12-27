package pheme;

import timedelayqueue.TimeDelayQueue;
import twitter.TwitterListener;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Custom class for phemeService
 * <p>
 * Rep invariant:
 *         hashPassword is not null, computed using blowfish cipher
 *         userName, userID, listener, tdq are not null
 *         messageHasSeen.size() = number of ALL messages that this user has successfully sent over the
 *         lifetime since its instantiation
 * <p>
 * AF(User) = a user accessing Twitter and can subscribe/unsubscribe to certain tweets, and can
 *                interact with other Users as well as read/send messages
 * <p>
 * Thread safety argument:
 *         There is no sharing of resources, and no mutator methods.
 *         Most parameters are private and final, and the only mutation occurs in timeDelayQueue
 *         and TwitterListener, which are proven to be thread-safe (see thread-safe arguments in their
 *         respective classes).
 * </p>
 */
public class User {
    public static final int DELAY = 1000; // 1 second or 1000 milliseconds
    private final String hashPassword;
    private final String userName;
    private final UUID userID;
    private final TwitterListener listener;
    private final TimeDelayQueue tdq;
    private final Set<UUID> messageHasSeen;

    public User(UUID userID, String userName, String hashPassword) {
        this.userName = userName;
        this.userID = userID;
        this.hashPassword = hashPassword;
        listener = new TwitterListener(new File("secret/credentials.json"));
        tdq = new TimeDelayQueue(DELAY);
        messageHasSeen = new HashSet<>();
    }

    /**
     * Getter for hashPassword
     * @return hashed password
     */
    public String getHashPassword() {
        return hashPassword;
    }

    /**
     * Getter method for username
     * @return name of user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Getter for userID
     * @return id of user
     */
    public UUID getUserID() {
        // immutable, so passing this directly is fine
        return userID;
    }

    /**
     * getter for Listener
     * @return user's TwitterListener
     */
    public TwitterListener getListener() {
        return listener;
    }

    /**
     * Getter for tdq
     * @return user's timeDelayQueue
     */
    public TimeDelayQueue getTdq() {
        return tdq;
    }

    /**
     * Getter for messageHasSeen
     * @return set of booleans that correspond to whether a received message
     *         has been seen
     */
    public Set<UUID> getMessageHasSeen() {
        return messageHasSeen;
    }
}
