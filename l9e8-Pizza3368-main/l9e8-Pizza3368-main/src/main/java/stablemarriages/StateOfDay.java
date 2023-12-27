package stablemarriages;

public class StateOfDay {
    private boolean[] morningDone;
    private boolean[] afternoonDone;
    private boolean[] eveningDone;
    private boolean[] done;
    private int       day;

    public StateOfDay(int num) {
        morningDone   = new boolean[num];
        afternoonDone = new boolean[num];
        eveningDone   = new boolean[num];
        done          = new boolean[num];
        day           = 1;
    }

    synchronized public int getDay() {
        return day;
    }

    synchronized public void setMorningDone(int n) {
        morningDone[n] = true;
    }

    synchronized public void setAfternoonDone(int n) {
        afternoonDone[n] = true;
    }

    synchronized public void setEveningDone(int n, boolean done) {
        eveningDone[n] = true;
        this.done[n]   = done;
        if (this.isEveningDone()) {
            this.nextDay();
        }
    }

    synchronized public boolean isMorningDone() {
        for (boolean b: morningDone) {
            if (!b) return false;
        }
        return true;
    }

    synchronized public boolean isAfternoonDone() {
        for (boolean b: afternoonDone) {
            if (!b) return false;
        }
        return true;
    }

    synchronized public boolean isEveningDone() {
        for (boolean b: eveningDone) {
            if (!b) return false;
        }
        return true;
    }

    synchronized public boolean isMatchingDone() {
        for (boolean b: done) {
            if (!b) return false;
        }
        return true;
    }

    synchronized private void nextDay() {
        day++;
        if (this.isMatchingDone()) {
            return;
        }

        for (int i = 0; i < morningDone.length; i++) {
            morningDone[i]   = false;
            afternoonDone[i] = false;
            eveningDone[i]   = false;
            done[i]          = false;
        }
    }
}
