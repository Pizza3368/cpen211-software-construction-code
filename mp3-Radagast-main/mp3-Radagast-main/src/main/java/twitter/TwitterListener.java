package twitter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.endpoints.AdditionalParameters;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.dto.tweet.TweetList;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import io.github.redouane59.twitter.dto.user.User;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

// TODO: write a description for this class
// TODO: complete all methods, irrespective of whether there is an explicit TODO or not
// TODO: write clear specs
// TODO: State the rep invariant and abstraction function
// TODO: what is the thread safety argument?
public class TwitterListener {

    TwitterClient twitter;
    private static final LocalDateTime OCT_1_2022 = LocalDateTime.parse("2022-10-01T00:00:00");
    private Map<String, String> subscription;
    private LocalDateTime lastFetchedTime = OCT_1_2022;

    // create a new instance of TwitterListener
    // the credentialsFile is a JSON file that
    // contains the API access keys
    // consider placing this file in the
    // 'secret' directory but the constructor
    // should work with any path
    public TwitterListener(File credentialsFile) {
        twitter = new TwitterClient(TwitterClient.getAuthentication(credentialsFile));
        subscription = new HashMap<>();
        // ... add other elements ...
    }

    public TwitterListener(File credentialsFile, LocalDateTime lastFetchedTime) {
        twitter = new TwitterClient(TwitterClient.getAuthentication(credentialsFile));
        subscription = new HashMap<>();
        this.lastFetchedTime = lastFetchedTime;
        // ... add other elements ...
    }

    // add a subscription for all tweets made by a specific
    // Twitter user
    /**
     *Adding user to subscription
     * @return false if user is already subscribed
     * true of user is successfully subscribed
     */

    public synchronized boolean addSubscription(String twitterUserName) {
        if (subscription.keySet().contains(twitterUserName)) {
            if (!subscription.get(twitterUserName).equals("")){
                subscription.put(twitterUserName, "");
                return true;
            }
            return false;
        }else {
            subscription.put(twitterUserName, "");
            return true;
        }
    }
    /**
     *Adding user to subscription
     * @param twitterUserName is not null
     * @return false if user not valid
     * true of user is valid
     */
    private boolean isValidUser(String twitterUserName) {
        if (twitter.getUserFromUserName(twitterUserName).equals(null)) {
            return false;
        }
        return true;
    }


    // add a subscription for all tweets made by a specific
    // Twitter user that also match a given pattern
    // for simplicity, a match is an exact match of strings but
    // ignoring case
    /**
     *Adding user to subscription with pattern
     * @param twitterUserName, pattern not null, twitterUserName is valid
     * @return false if user is already subscribed
     * true of user is successfully subscribed
     */

    public synchronized boolean addSubscription(String twitterUserName, String pattern) {
        if (!subscription.keySet().contains(twitterUserName)) {
            subscription.put(twitterUserName, pattern);
            return true;
        }
        return false;
    }

    // cancel a previous subscription
    // will also cancel subscriptions to specific patterns
    // from the twitter user
    /**
     * Cancel a previous subscription
     * @param twitterUserName not null and valid
     * @return false if user not subscribe
     * true of user is cancel the previous subscription
     */
    public synchronized boolean cancelSubscription(String twitterUserName) {
        if (subscription.keySet().contains(twitterUserName)){
            subscription.remove(twitterUserName);
            return true;
        }else {
            return false;
        }
    }

    // cancel a specific user-pattern subscription
    /**
     * Cancel a previous subscription
     * @param twitterUserName, pattern not null, twitterUserName valid
     * @return false if user not subscribe
     * true of user is cancel the previous subscription
     */
    public synchronized boolean cancelSubscription(String twitterUserName, String pattern) {
        if (subscription.containsKey(twitterUserName) && subscription.get(twitterUserName) == pattern){
            subscription.remove(twitterUserName);
            return true;
        }
        return false;
    }

    // get all subscribed tweets since the last tweet or
    // set of tweets was obtained
    /**
     *  get all subscribed tweets since the last tweet or
     *  set of tweets was obtained

     */
    public synchronized List<TweetV2.TweetData> getRecentTweets() {
        LocalDateTime fetchingTime = LocalDateTime.now();
        List<TweetV2.TweetData> recentTweets = new ArrayList<>();

        //subscription with no pattern
        Map<String, String> subscriptionWithNoPattern = new HashMap<>();
        for (String x : subscription.keySet()){
            if (subscription.get(x).equals("")){
                subscriptionWithNoPattern.put(x, "");
            }
        }
        for (String x : subscriptionWithNoPattern.keySet()) {
            User twUser = twitter.getUserFromUserName(x);
            if (twUser == null) {
                throw new IllegalArgumentException();
            }
            TweetList twList = twitter.getUserTimeline(twUser.getId(), AdditionalParameters.builder().startTime(lastFetchedTime).endTime(fetchingTime).build());
            for (TweetV2.TweetData y : twList.getData())
            recentTweets.add(y);
        }

        //subscription with pattern
        Map<String, String> subscriptionWithPattern = new HashMap<>();
        for (String x : subscription.keySet()){
            if (!subscription.get(x).equals("")){
                subscriptionWithPattern.put(x, subscription.get(x));
            }
        }
        for (String x : subscriptionWithPattern.keySet()) {
            User twUser = twitter.getUserFromUserName(x);
            if (twUser == null) {
                throw new IllegalArgumentException();
            }
            TweetList twList = twitter.getUserTimeline(twUser.getId(), AdditionalParameters.builder().startTime(lastFetchedTime).endTime(fetchingTime).build());
            for (TweetV2.TweetData y : twList.getData()) {
                String tweetText = y.getText();
                String pattern = subscriptionWithPattern.get(x);
                if (Pattern.compile(Pattern.quote(pattern), Pattern.CASE_INSENSITIVE).matcher(tweetText).find())
                    recentTweets.add(y);
            }
        }

        lastFetchedTime = fetchingTime;
        return recentTweets;
    }

    // get all the tweets made by a user
    // within a time range.
    // method has been implemented to help you.
    // get all subscribed tweets since the last tweet or
    // set of tweets was obtained
    /**
     *  get all subscribed tweets between start time and end time
     * @param twitterUserName not null and valid, endTime later than startTime
     * @return all tweets from User
     */
    public synchronized List<TweetV2.TweetData> getTweetsByUser(String twitterUserName,
                                                   LocalDateTime startTime,
                                                   LocalDateTime endTime) {
        User twUser = twitter.getUserFromUserName(twitterUserName);
        if (twUser == null) {
            throw new IllegalArgumentException();
        }
        TweetList twList = twitter.getUserTimeline(twUser.getId(), AdditionalParameters.builder().startTime(startTime).endTime(endTime).build());
        return twList.getData();
    }

}
