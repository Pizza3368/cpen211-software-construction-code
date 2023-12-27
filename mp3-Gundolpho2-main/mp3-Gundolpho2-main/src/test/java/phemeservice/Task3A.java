package phemeservice;

import io.github.redouane59.twitter.dto.tweet.TweetV2;
import org.junit.jupiter.api.Test;
import twitter.TwitterListener;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Task3A {

    @Test
    public void testFetchRecentTweets() {
        TwitterListener tl = new TwitterListener(new File("secret/credentials.json"));
        tl.addSubscription("UBC");
        List<TweetV2.TweetData> tweets = tl.getRecentTweets();
        assertTrue(tweets.size() > 0);
    }

    @Test
    public void testDoubleFetchRecentTweets() {
        TwitterListener tl = new TwitterListener(new File("secret/credentials.json"));
        tl.addSubscription("UBC");
        List<TweetV2.TweetData> tweets = tl.getRecentTweets();
        assertTrue(tweets.size() > 0);
        tweets = tl.getRecentTweets();
        assertTrue(tweets.size() == 0); // second time around, in quick succession, no tweet
    }

    @Test
    public void testUser() {
        TwitterListener tl = new TwitterListener(new File("secret/credentials.json"));
        assertFalse(tl.addSubscription("rexsywang5566789"));
        assertFalse(tl.addSubscription("niubiplusleitasdfing", "abc"));
        assertTrue(tl.addSubscription("SensaHealth"));
    }

    @Test
    public void testUpperandLowerPattern() {
        TwitterListener tl = new TwitterListener(new File("secret/credentials.json"));
        assertTrue(tl.addSubscription("UBC", "STuDY"));

        tl.getRecentTweets().forEach(x -> System.out.println("***\n" + x.getText() + "\n---"));
    }

    @Test
    public void testMultiplePattern() {
        TwitterListener tl = new TwitterListener(new File("secret/credentials.json"));
        assertTrue(tl.addSubscription("UBC", "STuDY"));
        assertTrue(tl.addSubscription("UBC", "weather"));

        tl.getRecentTweets().forEach(x -> System.out.println("***\n" + x.getText() + "\n---"));
    }

    @Test
    public void testMultiplePatternDifferentSource() {
        TwitterListener tl = new TwitterListener(new File("secret/credentials.json"));
        assertTrue(tl.addSubscription("UBC", "STuDY"));
        assertTrue(tl.addSubscription("CityofVancouver", "weather"));

        tl.getRecentTweets().forEach(x -> System.out.println("***\n" + x.getText() + "\n---"));
    }


    @Test
    public void testSubscribeToSameOver() {
        TwitterListener tl = new TwitterListener(new File("secret/credentials.json"));
        assertTrue(tl.addSubscription("UBC"));
        assertFalse(tl.addSubscription("UBC", "weather"));

        TwitterListener tl2 = new TwitterListener(new File("secret/credentials.json"));
        assertTrue(tl2.addSubscription("UBC"));

        assertEquals(tl.getRecentTweets().size(), tl2.getRecentTweets().size());
    }

    @Test
    public void testEmpty() {
        TwitterListener tl = new TwitterListener(new File("secret/credentials.json"));
        assertTrue(tl.getRecentTweets().isEmpty());
        assertTrue(tl.getRecentTweets().isEmpty());
    }

    @Test
    public void testCancelSubscription() {
        TwitterListener tl = new TwitterListener(new File("secret/credentials.json"));
        assertTrue(tl.addSubscription("UBC"));
        assertTrue(tl.cancelSubscription("UBC"));
        assertTrue(tl.getRecentTweets().isEmpty());

        assertTrue(tl.addSubscription("UBC", "weather"));
        assertTrue(tl.addSubscription("UBC", "did you know?"));
        assertTrue(tl.cancelSubscription("UBC"));
        assertTrue(tl.getRecentTweets().isEmpty());

        assertTrue(tl.addSubscription("UBC", "weather"));
        assertTrue(tl.addSubscription("CityofVancouver", "traffic alert"));
        assertTrue(tl.cancelSubscription("UBC"));
        assertFalse(tl.getRecentTweets().isEmpty());
    }

}
