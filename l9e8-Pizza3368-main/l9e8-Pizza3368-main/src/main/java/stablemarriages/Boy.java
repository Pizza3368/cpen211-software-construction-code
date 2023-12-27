package stablemarriages;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a boy in the stable marriage problem.
 */

public class Boy implements Runnable {
    private Integer             id;
    private List<Integer>       preferences;
    private AppendOnlyMailbox[] mailboxesForGirls;
    private ProposalInbox       inbox;
    private StateOfDay          state;
    private Integer             currentSuitor = null;

    // Representation Invariant:
    // - this.mailboxesForGirls.length == preferences.size()
    // - this.inbox is not null

    /**
     * Create an instance of a boy
     * @param id unique identifier for this boy
     * @param preferences is not null and contains a permutation of [0, 1, ..., preferences.size() - 1]
     * @param inbox is not null
     * @param mailboxesForGirls does not contain nulls and mailboxesForGirls.length == preferences.size()
     * @param state shared state of the day, is not null
     */
    public Boy(Integer id, List<Integer> preferences,
               ProposalInbox inbox, AppendOnlyMailbox[] mailboxesForGirls,
               StateOfDay state) {
        this.id                = id;
        this.preferences       = new ArrayList<>(preferences);
        this.inbox             = inbox;
        this.mailboxesForGirls = mailboxesForGirls;
        this.state             = state;
    }

    /**
     * Obtain the current suitor
     *
     * @return the current suitor
     * (could be null if the boy was rejected in the most recent day)
     */
    public Integer getCurrentSuitor() {
        return currentSuitor;
    }

    /**
     * Obtain the id of this boy
     * @return the id of this boy
     */
    public Integer getID() {
        return id;
    }

    /**
     * The traditional marriage algorithm
     */
    public void run() {
        int day = 0;

        while (!state.isMatchingDone()) {
            day = state.getDay();

            Integer tempSuitor = preferences.get(0);
            mailboxesForGirls[tempSuitor].post(id);
            state.setMorningDone(id);

            while (!state.isAfternoonDone()) { }

            Integer response = inbox.getProposals().get(0);
            inbox.getProposals().clear();
            if (response == id) {
                currentSuitor = tempSuitor;
                state.setEveningDone(id, true);
            }
            else {
                currentSuitor = null;
                preferences.remove(0);
                state.setEveningDone(id, false);
            }

            while (day == state.getDay()) { }
        }
    }
}
