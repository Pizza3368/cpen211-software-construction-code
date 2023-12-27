package phemeserver;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pheme.PhemeClient;
import pheme.PhemeServer;
import pheme.Utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Task4 {
    private static PhemeServer srv;
    private static PhemeClient client;
    private static final int PORT = 4949;
    private static UUID userID1;
    private static String username1;
    private static String password1;
    private static Utils.RequestInstance.Parameters parameters1User1;
    private static UUID userID2;
    private static String username2;
    private static String password2;
    private static Utils.RequestInstance.Parameters parameters1User2;

    @BeforeAll
    public static void setup() throws IOException {
        srv = new PhemeServer(PORT, new File("secret/credentials.json"));
        client = new PhemeClient("127.0.0.1", PhemeServer.PORT);

        userID1 = UUID.randomUUID();
        username1 = "CPEN221";
        password1 = "WhyIsCPEN221SoHardLol";
        parameters1User1 = new Utils.RequestInstance.Parameters(userID1,
                username1, password1);

        userID2 = UUID.randomUUID();
        username2 = "theGoatestGoat";
        password2 = "IamThEGoaToFAllGoAts";
        parameters1User2 = new Utils.RequestInstance.Parameters(userID2,
                username2, password2);
    }

    // could not write tests for task 4 :(

}
