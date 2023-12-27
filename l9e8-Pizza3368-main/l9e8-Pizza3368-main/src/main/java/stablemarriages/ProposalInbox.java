package stablemarriages;

import java.util.ArrayList;
import java.util.List;

public class ProposalInbox implements AppendOnlyMailbox<Integer> {
    private List<Integer> proposals = new ArrayList<>();

    synchronized public boolean post(Integer n) {
        if (!proposals.contains(n)) {
            proposals.add(n);
            return true;
        }
        return false;
    }

    synchronized public List<Integer> getProposals() {
        return proposals;
    }
}
