package stablemarriages;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Represents a girl in the stable marriage problem.
 */

public class Girl implements Runnable {
    private Integer             id;
    private List<Integer>       preferences;
    private ProposalInbox       inbox;
    private AppendOnlyMailbox[] mailboxesForBoys;
    private StateOfDay          state;
    private Integer             currentSuitor = null;
    private TreeSet<Integer>    rankedProposals = new TreeSet<>();

    // Representation Invariant:
    // - this.mailboxesForBoys.length == preferences.size()
    // - this.inbox is not null

    /**
     * Create an instance of a girl
     * @param id unique identifier for this girl
     * @param preferences is not null and contains a permutation of [0, 1, ..., preferences.size() - 1]
     * @param inbox is not null
     * @param mailboxesForBoys does not contain nulls and mailboxesForBoys.length == preferences.size()
     * @param state shared state of the day, is not null
     */
    public Girl(Integer id, List<Integer> preferences, ProposalInbox inbox, AppendOnlyMailbox[] mailboxesForBoys, StateOfDay state) {
        this.id               = id;
        this.preferences      = new ArrayList<>(preferences);
        this.inbox            = inbox;
        this.mailboxesForBoys = mailboxesForBoys;
        this.state            = state;
    }

    /**
     * Obtain the id of this girl
     * @return the id of this girl
     */
    public int getID() {
        return id;
    }

    /**
     * Obtain the current suitor
     *
     * @return the current suitor
     * (could be null if the girl has not received a proposal yet)
     */
    public Integer getCurrentSuitor() {
        return currentSuitor;
    }

    /**
     * The traditional marriage algorithm
     */
    public void run() {
        // TODO: Implement this method
        int day = 0;

        while(!state.isMatchingDone()){
            day = state.getDay();

            while(!state.isMorningDone()){ }


            rankedProposals.addAll(inbox.getProposals());

            if(!rankedProposals.isEmpty()){
                currentSuitor = rankedProposals.first();

                for(Integer integer : rankedProposals){
                    if(preferences.indexOf(integer) < preferences.indexOf(currentSuitor)){
                        currentSuitor = integer;
                    }
                }

                for(Integer integer: inbox.getProposals()){
                    mailboxesForBoys[integer].post(currentSuitor);
                }
            }

            rankedProposals.clear();
            inbox.getProposals().clear();;
            state.setAfternoonDone(id);
            while (day == state.getDay()){}
        }

    }
}
