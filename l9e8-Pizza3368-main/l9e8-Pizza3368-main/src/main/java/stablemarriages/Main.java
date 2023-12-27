package stablemarriages;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {
    private static final int NUM_PAIRS = 8;

    public static void main(String[] args) {

        List<List<Integer>> preferencesOfGirls = new ArrayList<>();
        List<List<Integer>> preferencesOfBoys  = new ArrayList<>();

        for (int i = 0; i < NUM_PAIRS; i++) {
            List<Integer> prefs = new ArrayList<>();
            setPreferences(prefs, NUM_PAIRS);
            preferencesOfGirls.add(prefs);
        }

        for (int i = 0; i < NUM_PAIRS; i++) {
            List<Integer> prefs = new ArrayList<>();
            setPreferences(prefs, NUM_PAIRS);
            preferencesOfBoys.add(prefs);
        }

        Integer[] matching = Main.doMatching(
            preferencesOfGirls, preferencesOfBoys);

        for (int i = 0; i < NUM_PAIRS; i++) {
            System.out.printf("Girl %3d --- Boy %3d\n",
                matching[2 * i], matching[2 * i + 1]);
        }

        System.out.println(Utils.isStable(matching, preferencesOfGirls, preferencesOfBoys));

//        GsonBuilder builder = new GsonBuilder();
//        builder.serializeNulls();
//        Gson gson = builder.create();
//
//        Utils.ProblemInstance p = new Utils.ProblemInstance();
//        p.preferencesOfBoys     = preferencesOfBoys;
//        p.preferencesOfGirls    = preferencesOfGirls;
//        p.solution              = matching;
//
//        File file = new File(UUID.randomUUID().toString()+".json");
//        try {
//            FileWriter fw = new FileWriter(file);
//            fw.write(gson.toJson(p));
//            fw.close();
//        }
//        catch (IOException ioe) {
//            System.out.println("Unable to write to file!");
//        }
    }

    /**
     * Create a stable set of marriages
     *
     * @param preferencesOfGirls does not contain nulls and represents a list per girl of their rankings of the boys,
     *                           preferencesForGirls.size() == preferencesForBoys.size() &&
     *                           for all (i, j),
     *                           preferencesForGirls.get(i).size() == preferencesForGirls.get(j).size() &&
     *                           preferencesForGirls.size() == preferencesForGirls.get(i).size()
     * @param preferencesOfBoys does not contain nulls and represents a list per boy of their rankings of the girls,
     *                          preferencesForBoys.size() == preferencesForGirls.size() &&
     *                          for all (i, j),
     *                          preferencesForBoys.get(i).size() == preferencesForBoys.get(j).size() &&
     *                          preferencesForBoys.size() == preferencesForBoys.get(i).size()
     * @return a stable matching of girls to boys,
     *         where index (2 * i) represents a girl
     *         and index (2 * i + 1) represents the boy the girl is matched to
     */
    public static Integer[] doMatching(List<List<Integer>> preferencesOfGirls,
                                       List<List<Integer>> preferencesOfBoys) {

        final int NUM_PAIRS = preferencesOfGirls.size();

        Thread[] threads = new Thread[2 * NUM_PAIRS];
        int threadId = 0;

        ProposalInbox[] mailboxesForGirls = new ProposalInbox[NUM_PAIRS];
        ProposalInbox[] mailboxesForBoys  = new ProposalInbox[NUM_PAIRS];
        for (int i = 0; i < NUM_PAIRS; i++) {
            mailboxesForBoys[i]  = new ProposalInbox();
            mailboxesForGirls[i] = new ProposalInbox();
        }

        StateOfDay state = new StateOfDay(NUM_PAIRS);

        // set up the list of girls
        List<Girl> girls = new ArrayList<>();

        for (int i = 0; i < NUM_PAIRS; i++) {
            Girl g = new Girl(i, preferencesOfGirls.get(i), mailboxesForGirls[i], mailboxesForBoys, state);
            girls.add(g);
            threads[threadId] = new Thread(g);
            threadId++;
        }

        // set up the list of boys
        List<Boy> boys = new ArrayList<>();

        for (int i = 0; i < NUM_PAIRS; i++) {
            Boy b = new Boy(i, preferencesOfBoys.get(i), mailboxesForBoys[i], mailboxesForGirls, state);
            boys.add(b);
            threads[threadId] = new Thread(b);
            threadId++;
        }

        // start the threads
        for (Thread t: threads) {
            t.start();
        }

        for (Thread t: threads) {
            try {
                t.join();
            }
            catch (InterruptedException ie) {
                // ...?
            }
        }

        Integer[] matching = new Integer[2 * NUM_PAIRS];

        int i = 0;
        for (Girl g: girls) {
            matching[2 * i] = i;
            matching[2 * i + 1] = g.getCurrentSuitor();
            i++;
        }

        return matching;
    }

    /**
     * Create a random permutation for preferences
     * @param preferences is not null, and will contain a permutation of [0, 1, ..., n-1]
     * @param n the number of preferences in the permutation
     */
    private static void setPreferences(List<Integer> preferences, int n) {
        preferences.clear();
        for (int i = 0; i < n; i++) {
            preferences.add(i);
        }
        Collections.shuffle(preferences);
    }

}

