package twitter;

import io.github.redouane59.twitter.dto.tweet.TweetV2;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A Cache class used to cache Twitter posts retrieved from the Twitter API
 * <p>
 * Rep Invariant:
 *      userTweets that are cached are not null, and user(s) is/are not null
 *      the set of tweets per user does not exceed 512 counts
 *      startTimes is not null and consists of key-value pairs >= 0
 *      endTimes is not null and consists of key-value pairs >= 0
 *
 * <p>
 * Abstract Function:
 *     AF(Cache) = represents the collection of cached Twitter messages that is shared between multiple instances
 *                  of TwitterListeners
 * <p>
 * Thread safety argument:
 *      Thread-safe data structure such as Collections.synchronizedMap are used.
 *      Methods that mutate and/or iterate over a map have been synchronized.
 *      In the case that multiple requests are sent from the same user, the methods to perform are synchronized in
 *      TwitterListener class, so no data race occurs.
 * </p>
 */
public class Cache {
    private final int USER_CAPACITY = 512;
    private final Map<String, LocalDateTime> startTimes;
    private final Map<String, LocalDateTime> endTimes;
    private final Map<String, Set<TweetV2.TweetData>> userTweets;

    /**
     * Creates a Cache object to cache tweets retrieved from the Twitter API
     */
    public Cache() {
        startTimes = Collections.synchronizedMap(new HashMap<>());
        endTimes = Collections.synchronizedMap(new HashMap<>());
        userTweets = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Adds new tweets that are retrieved from the Twitter API
     * to the cache of a Twitter user
     *
     * @param user name of user whose tweets are to be cached, is not null
     * @param newTweets the new list of tweets to be added to the cache
     */
    public void add(String user, List<TweetV2.TweetData> newTweets) {
        if (user == null || newTweets.isEmpty()) {
            return;
        }

        if (!userTweets.containsKey(user)) {
            userTweets.put(user, new HashSet<>(newTweets));
            return;
        }

        if (cacheIsAtCapacity(user)) {
            int numberOfTweetsToRemove = (sizeOfUserCache(user) - USER_CAPACITY) + newTweets.size();

            List<TweetV2.TweetData> tweets = new ArrayList<>(userTweets.get(user));
            int oldSize = tweets.size();

            List<TweetV2.TweetData> newTweetList = tweets.subList(numberOfTweetsToRemove - 1, oldSize);
            startTimes.replace(user, newTweetList.get(0).getCreatedAt());
            userTweets.replace(user, new HashSet<>(newTweetList));
        }

        userTweets.get(user).addAll(newTweets);
        new ArrayList<>(userTweets.get(user)).sort(Comparator.comparing(TweetV2.TweetData::getCreatedAt));
    }

    /**
     * Returns the number of tweets currently contained within
     * a user's cache
     *
     * @param user name of user whose tweets are cached, is not null
     * @return the number of tweets saved in the cache of the user
     */
    private int sizeOfUserCache(String user) {
        return userTweets.get(user).size();
    }

    /**
     * Determines whether a user's cache is at capacity or not
     *
     * @param user name of user whose tweets are to be cached, is not null
     * @return true if the user's cache is at capacity, and false otherwise
     */
    private boolean cacheIsAtCapacity(String user) {
        int size = userTweets.get(user).size();
        return size >= USER_CAPACITY;
    }

    /**
     * Sets a new start date for the tweets that are being
     * cached for a given user
     *
     * @param user name of user whose tweets are being cached, is not null
     * @param startTime the new start date of the given user's tweet cache
     */
    public void setStartTime(String user, LocalDateTime startTime) {
        if (!startTimes.containsKey(user)) {
            startTimes.put(user, startTime);
            return;
        }

        startTimes.replace(user, startTimes.get(user), startTime);
    }

    /**
     * Sets a new end date for the tweets that are being
     * cached for a given user
     *
     * @param user name of user whose tweets are being cached, is not null
     * @param endTime the new end date of the given user's tweet cache
     */
    public void setEndTime(String user, LocalDateTime endTime) {
        if (!endTimes.containsKey(user)) {
            endTimes.put(user, endTime);
            return;
        }

        endTimes.replace(user, endTimes.get(user), endTime);
    }

    /**
     * Checks whether the cache contains a given Twitter user
     *
     * @param user name of user to check, is not null
     * @return true is the user exists in the cache, and false otherwise
     */
    public boolean contains(String user) {
        return userTweets.containsKey(user);
    }

    /**
     * Get all the cached tweets for a given user
     *
     * @param user name of user to get cached tweets from, is not null
     * @return a list of all the cached tweets thus far for a given user
     */
    public List<TweetV2.TweetData> getTweets(String user) {
        return userTweets.get(user).stream().toList();
    }

    /**
     * Get all the cached tweets within a certain date
     * range for a given user
     *
     * @param user name of user to get cached tweets from, is not null
     * @param startTime the start date to start retrieving tweets from the user's cache
     * @param endTime the end date to stop retrieving tweets from the user's cache
     * @return a list of all the cached tweets in a given range for a given user
     */
    public synchronized List<TweetV2.TweetData> getTweetsInRange(String user,
                                                                 LocalDateTime startTime, LocalDateTime endTime) {
        List<TweetV2.TweetData> newTweets = new ArrayList<>(userTweets.get(user));

        return newTweets.stream().filter(tweet -> !tweet.getCreatedAt().
                isBefore(startTime) && !tweet.getCreatedAt().isAfter(endTime)).collect(Collectors.toList());
    }

    /**
     * Gets the start date of cached messages for a given user
     *
     * @param user name of user to check, is not null
     * @return the start date of the cached messages for the given user
     */
    public LocalDateTime getUserStartTime(String user) {
        return startTimes.get(user);
    }

    /**
     * Gets the end date of cached messages for a given user
     *
     * @param user name of user to check, is not null
     * @return the end date of the cached messages for the given user
     */
    public LocalDateTime getUserEndTime(String user) {
        return endTimes.get(user);
    }
}
