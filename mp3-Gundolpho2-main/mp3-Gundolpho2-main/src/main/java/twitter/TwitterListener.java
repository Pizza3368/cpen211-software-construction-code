package twitter;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.endpoints.AdditionalParameters;
import io.github.redouane59.twitter.dto.tweet.TweetList;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import io.github.redouane59.twitter.dto.user.User;
import io.github.redouane59.twitter.dto.user.UserV2;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

/**
 * A class used to interact with the Twitter API and to keep track of user subscriptions
 * <p>
 * Rep Invariant: twitter client is not null
 *                subscriptions contains subscribed users mapped to a list of patterns.
 *                Inherits rep invariants of TimeDelayQueue
 * <p>
 * Abstract Function:
 *     AF(TwitterListener) = a collection of methods that connect between the Twitter API and
 *     remote user's messages
 * <p>
 * Thread safety argument:
 *     Thread-safe data structure such as Collections.synchronizedMap are used.
 *     Getter and mutator methods are synchronized when accessing the same resource (TimeDelayQueue, etc.).
 */
public class TwitterListener {
    TwitterClient twitter;
    private static final LocalDateTime OCT_1_2022 = LocalDateTime.parse("2022-10-01T00:00:00");
    private LocalDateTime lastTweetCall;
    private final Map<String, List<String>> subscriptions; // a map of subscribed users to a list of patterns (if any)
    private static final Cache cache = new Cache();

    /**
     * TwitterListener interacts with the public Twitter API to obtain posts made
     * by users and coordinates actions such as subscription.
     *
     * @param credentialsFile a JSON file containing the secret tokens and keys
     *                        needed to access the Twitter developer API
     */
    public TwitterListener(File credentialsFile) {
        twitter = new TwitterClient(TwitterClient.getAuthentication(credentialsFile));
        lastTweetCall = OCT_1_2022;
        subscriptions = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Adds a subscription for all tweets made by a specific Twitter user
     *
     * @param twitterUserName name of user to be subscribed to, is not null
     * @return true if successfully added a new subscription to all the tweets of the twitterUsername
     *         false if otherwise (ex. already subscribed to this Twitter user)
     */
    public synchronized boolean addSubscription(String twitterUserName) {
        if (!isValidUser(twitterUserName)) {
            return false;
        }

        // return false if already subscribed to this Twitter user
        if (subscriptions.containsKey(twitterUserName) && subscriptions.get(twitterUserName).isEmpty()) {
            return false;
        } else if (subscriptions.containsKey(twitterUserName) && subscriptions.get(twitterUserName).size() > 0) {
            subscriptions.replace(twitterUserName, new ArrayList<>());
        } else {
            subscriptions.put(twitterUserName, new ArrayList<>());
        }
        return true;
    }

    /**
     * Checks whether a Twitter username is valid (exists in database)
     *
     * @param twitterUserName name of Twitter user, is not null
     * @return true if such user exists in the Twitter API, false otherwise.
     */
    private synchronized boolean isValidUser(String twitterUserName) {
        UserV2 twitterUserFromUserName = twitter.getUserFromUserName(twitterUserName);
        return twitterUserFromUserName.getData() != null;
    }

    /**
     * Add a new subscription a specified Twitter user using a specified pattern for their
     * tweets to be subscribed to
     *
     * @param twitterUserName name of user to be subscribed to, is not null
     * @param pattern tweets of the specified user that matches the
     *                pattern will be subscribed, is not null but can be empty (subscribes to all tweets)
     *                A match is an exact match of strings but ignoring case
     * @return true if is successfully subscribed to the matched tweets of a valid twitterUserName
     *         that has not been previously subscribed to, false otherwise
     */
    public synchronized boolean addSubscription(String twitterUserName, String pattern) {
        if (!isValidUser(twitterUserName)) {
            return false;
        }

        // return false if already subscribed to this Twitter user with the same pattern, or if they
        // are trying to subscribe to the same user with no pattern without first unsubscribing
        if (subscriptions.containsKey(twitterUserName) && subscriptions.get(twitterUserName).isEmpty()) {
            return false;
        } else if (subscriptions.containsKey(twitterUserName)
            && subscriptions.get(twitterUserName).stream().anyMatch(pattern::equalsIgnoreCase)) {
            return false;
        } else if (subscriptions.containsKey(twitterUserName) && subscriptions.get(twitterUserName).size() > 0) {
            return subscriptions.get(twitterUserName).add(pattern);
        } else {
            List<String> patterns = new ArrayList<>();
            patterns.add(pattern);
            subscriptions.put(twitterUserName, patterns);
        }
        return true;
    }

    /**
     * Cancel a previous subscription from a Twitter user
     *
     * @param twitterUserName name of user to be unsubscribed from, is not null
     *                        unsubscription removes all tweets of that user
     *                        that matches all patterns
     * @return true if subscription is successfully canceled, false if otherwise
     */
    public synchronized boolean cancelSubscription(String twitterUserName) {
        return subscriptions.remove(twitterUserName, subscriptions.get(twitterUserName));
    }

    /**
     * Cancel a specific user-pattern subscription
     *
     * @param twitterUserName name of user to be unsubscribed from, is not null
     * @param pattern tweets that match the pattern will be unsubscribed, is not null.
     *                A match is an exact match of strings but ignoring case
     * @return true if subscription is successfully canceled, false if otherwise
     */
    public synchronized boolean cancelSubscription(String twitterUserName, String pattern) {
        if (subscriptions.containsKey(twitterUserName) && subscriptions.get(twitterUserName).size() == 1) {
            if (subscriptions.get(twitterUserName).get(0).equalsIgnoreCase(pattern)) {
                subscriptions.remove(twitterUserName);
            }
        } else if (subscriptions.containsKey(twitterUserName) && subscriptions.get(twitterUserName).size() > 1) {
            return subscriptions.get(twitterUserName).removeIf(value -> value.equalsIgnoreCase(pattern));
        }
        return false;
    }

    /**
     * Get all subscribed tweets since the last tweet or the set of tweets was obtained
     *
     * @return a collection of tweets sorted from the earliest to the latest timestamp of the tweets
     *         since the last tweet was obtained. If no such tweet exists, return all tweets
     */
    public synchronized List<TweetV2.TweetData> getRecentTweets() {
        if (subscriptions.isEmpty()) {
            return new ArrayList<>();
        }

        LocalDateTime currentDateAndTime = LocalDateTime.now();
        List<TweetV2.TweetData> recentTweets = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : subscriptions.entrySet()) {
            if (cache.contains(entry.getKey())) {
                Set<TweetV2.TweetData> tweets = new HashSet<>();
                LocalDateTime cacheStartTime = cache.getUserStartTime(entry.getKey());
                LocalDateTime cacheEndTime = cache.getUserEndTime(entry.getKey());

                if (cacheStartTime.isAfter(lastTweetCall)) {
                    tweets.addAll(getTweetsByUser(entry.getKey(), lastTweetCall, cacheStartTime));
                    cache.add(entry.getKey(), getTweetsByUser(entry.getKey(), lastTweetCall, cacheStartTime));
                    cache.setStartTime(entry.getKey(), lastTweetCall);

                    tweets.addAll(cache.getTweets(entry.getKey()));
                } else {
                    tweets.addAll(cache.getTweetsInRange(entry.getKey(), lastTweetCall, cacheEndTime));
                }

                tweets.addAll(getTweetsByUser(entry.getKey(), cacheEndTime, currentDateAndTime));
                cache.add(entry.getKey(), getTweetsByUser(entry.getKey(), cacheEndTime, currentDateAndTime));
                cache.setEndTime(entry.getKey(), currentDateAndTime);

                if (entry.getValue().isEmpty()) {
                    recentTweets.addAll(tweets);
                } else {
                    recentTweets.addAll(getTweetsWithPatterns(entry.getValue(), new ArrayList<>(tweets)));
                }
            } else {
                List<TweetV2.TweetData> tweets = getTweetsByUser(entry.getKey(), lastTweetCall,
                        currentDateAndTime);
                cache.add(entry.getKey(), getTweetsByUser(entry.getKey(), lastTweetCall, currentDateAndTime));
                cache.setStartTime(entry.getKey(), lastTweetCall);
                cache.setEndTime(entry.getKey(), currentDateAndTime);

                if (entry.getValue().isEmpty()) {
                    // return all tweets from user since the last call
                    recentTweets.addAll(tweets);
                } else {
                    // return all patterned tweets from user since last call
                    recentTweets.addAll(getTweetsWithPatterns(entry.getValue(), tweets));
                }
            }
        }

        lastTweetCall = currentDateAndTime;
        return recentTweets;
    }

    /**
     * Get tweets that match one or more patterns
     *
     * @param patterns a list of String patterns for tweet-matching, are not null
     *                 A match is an exact match of strings to the tweets but ignoring case
     * @param tweets a list of Tweets to be matched by patterns
     * @return a list of tweets that matches the given pattern(s)
     */
    private synchronized List<TweetV2.TweetData> getTweetsWithPatterns(List<String> patterns,
                                                                       List<TweetV2.TweetData> tweets) {
        List<TweetV2.TweetData> patternedTweets = new ArrayList<>();
        for (String pattern : patterns) {
            for (TweetV2.TweetData tweet : tweets) {
                if (tweet.getText().toLowerCase().contains(pattern.toLowerCase())) {
                    patternedTweets.add(tweet);
                }
            }
        }
        return patternedTweets;
    }

    /**
     * Get all the tweets made by a user within a time range
     *
     * @param twitterUserName name of user to get tweets from
     * @param startTime start of the time range, is non-negative
     * @param endTime end of the time range, is non-negative and >= startTime
     * @return a list of tweets with startTime <= timestamps <= endTime
     * @throws IllegalArgumentException if no such user exists in the Twitter API
     */
    public synchronized List<TweetV2.TweetData> getTweetsByUser(String twitterUserName,
                                                   LocalDateTime startTime,
                                                   LocalDateTime endTime) {
        User twUser = twitter.getUserFromUserName(twitterUserName);
        if (twUser == null) {
            throw new IllegalArgumentException();
        }
        TweetList twList = twitter.getUserTimeline(twUser.getId(),
            AdditionalParameters.builder().startTime(startTime).endTime(endTime).build());
        return twList.getData();
    }
}
