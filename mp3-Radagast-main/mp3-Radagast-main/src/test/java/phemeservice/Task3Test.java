package phemeservice;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pheme.PhemeService;
import security.BlowfishCipher;
import timedelayqueue.PubSubMessage;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


import java.io.File;
import java.util.UUID;

public class Task3Test {

    private static PhemeService srv;
    private static String userName1;
    private static UUID userID1;
    private static String userName2;
    private static UUID userID2;
    private static String hashPwd1;
    private static String hashPwd2;
    private static PubSubMessage msg1;

    @BeforeAll
    public static void setup() {
        srv = new PhemeService(new File("secret/credentials.json"));

        userName1 = "Test User 1";
        userID1 = UUID.randomUUID();
        hashPwd1 = BlowfishCipher.hashPassword("Test Password 1", BlowfishCipher.gensalt(12));

        userName2 = "Test User 2";
        userID2 = UUID.randomUUID();
        hashPwd2 = BlowfishCipher.hashPassword("Test Password 2", BlowfishCipher.gensalt(12));
        srv.addUser(userID1, userName1, hashPwd1);
        srv.addUser(userID2, userName2, hashPwd2);
        srv.removeUser(userName2, hashPwd2);
    }

    @Test
    public void cancelTest(){
        srv.addSubscription(userName1, hashPwd1, "UBC");
        assertTrue(srv.cancelSubscription(userName1, hashPwd1, "UBC"));
        assertFalse(srv.cancelSubscription(userName1, hashPwd1, "UBC"));
    }

    @Test
    public void patternTest(){
        assertTrue(srv.addSubscription(userName1, hashPwd1, "UBC", "Engineer"));
        assertTrue(srv.cancelSubscription(userName1, hashPwd1, "UBC"));
        assertFalse(srv.cancelSubscription(userName1, hashPwd1, "UBC"));
        assertTrue(srv.addSubscription(userName1, hashPwd1, "UBC", "Engineer"));
        assertTrue(srv.cancelSubscription(userName1, hashPwd1, "UBC", "Engineer"));
    }


}
