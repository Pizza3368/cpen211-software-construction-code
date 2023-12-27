package pheme;

import timedelayqueue.TimeDelayQueue;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    UUID userID;
    String userName;
    String hashPassword;
    TimeDelayQueue UserInbox;
    LocalDateTime lastFetchedTime;
    static final LocalDateTime OCT_1_2022 = LocalDateTime.parse("2022-10-01T00:00:00");
    public User(UUID userID, String userName, String hashPassword, int DELAY){
        this.userID = userID;
        this.userName = userName;
        this.hashPassword = hashPassword;
        UserInbox = new TimeDelayQueue(DELAY);
        lastFetchedTime = OCT_1_2022;
    }

    public void updateLastFetchedTime(LocalDateTime lastFetchedTimeNew){
        lastFetchedTime = lastFetchedTimeNew;
    }
}
