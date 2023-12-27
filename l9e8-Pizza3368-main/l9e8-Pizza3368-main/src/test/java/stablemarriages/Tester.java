package stablemarriages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class Tester {
    private static Utils.ProblemInstance problemInstanceFromJSON(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        Gson gson = (new GsonBuilder()).create();

        String text = reader.readLine();
        return gson.fromJson(text, Utils.ProblemInstance.class);
    }

    /* Provides test cases */
    private static Stream<Arguments> testProvider() {
        return Stream.of(
            Arguments.of(new File("4a1d13c5-b198-4682-aa6c-24b85829e058.json")),
            Arguments.of(new File("7a9c4796-781d-4e85-b4ec-6423844a4669.json")),
            Arguments.of(new File("649e475c-be17-4c81-a02a-98dbf04eefbc.json")),
            Arguments.of(new File("665b59e1-a108-49dd-9bd8-633d6fb1f512.json")),
            Arguments.of(new File("3264ae55-fc56-4999-b088-b817a3a9d105.json"))
        );
    }

    @ParameterizedTest
    @MethodSource("testProvider")
    void testIsStableMarriage(File file) {
        try {
            Utils.ProblemInstance p = problemInstanceFromJSON(file);
            assertTrue(Utils.isStable(p.solution, p.preferencesOfGirls, p.preferencesOfBoys));
        }
        catch (IOException ioe) {
            fail();
        }
    }
}
