package timedelayqueue;


import java.sql.Timestamp;
import java.util.*;

// TODO: write a description for this class
// TODO: complete all methods, irrespective of whether there is an explicit TODO or not
// TODO: write clear specs
// TODO: State the rep invariant and abstraction function
// TODO: what is the thread safety argument?
public class TimeDelayQueue {

    private int delay;
    private Queue<PubSubMessage> queue;
    private int totalMsgCount;

    private  Vector<PubSubMessage> totalMsg;
    private Vector<Timestamp> operation;

    /**
     * Create a new TimeDelayQueue
     *
     * @param delay the delay, in milliseconds, that the queue can tolerate, >= 0
     */
    public TimeDelayQueue(int delay) {
        this.delay = delay;
        queue = new LinkedList<>();
        operation = new Vector<>();
        totalMsg = new Vector<>();
        totalMsgCount = 0;
    }

    // add a message to the TimeDelayQueue
    // if a message with the same id exists then
    // return false
    /**
     * Add a message to time delay queue
     * @param msg,tdq not null
     * @return if add success return true, if is already exist return false
     */
    public synchronized boolean add(PubSubMessage msg) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        operation.add(ts);
        if (!queue.contains(msg)) {
            queue.add(msg);
            totalMsgCount++;
            totalMsg.add(msg);
            return true;
        }else return false;
    }

    /**
     * Get the count of the total number of messages processed
     * by this TimeDelayQueue
     * not null tdq
     * @return synchronized number of Total Msg
     */
    public synchronized long getTotalMsgCount() {
        return totalMsg.size();
    }

    // return the next message and PubSubMessage.NO_MSG
    // if there is ni suitable message
    /**
     * Get the next msg in TimeDelayQueue
     * not null tdq
     * @return next msg
     */
    public synchronized PubSubMessage getNext() {
        sortQueueByTime();
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        operation.add(ts);
        if (queue.size() == 0){
            return PubSubMessage.NO_MSG;
        }
        PubSubMessage nextMsg = queue.peek();

        if (nextMsg.isTransient()) {
            TransientPubSubMessage msg = (TransientPubSubMessage) queue.poll();
            int msgLifetime = msg.getLifetime();
            long timePasted = ts.getTime() - nextMsg.getTimestamp().getTime();
            if (timePasted <= msgLifetime) {
                if (timePasted < delay){
                    return PubSubMessage.NO_MSG;
                }else {
                    return msg;
                }
            }
        }

        if(ts.getTime() - nextMsg.getTimestamp().getTime() >= delay){
            if(queue.size()==0){return PubSubMessage.NO_MSG;}
            PubSubMessage copy = queue.poll();
            return copy;
        }else{return PubSubMessage.NO_MSG;}
    }

    // return the maximum number of operations
    // performed on this TimeDelayQueue over
    // any window of length timeWindow
    // the operations of interest are add and getNext
    /**
     * Get the count of the total number of messages processed
     * by this TimeDelayQueue
     * @param timeWindow not null and >=0
     * @return synchronized number of Total Msg
     */
    public synchronized int getPeakLoad(int timeWindow) {
        int maxPeakLoad = 0;
        int count;
        if(operation.size() == 0){
            return 0;}
        if(timeWindow == 0){return 0;}
        for(int i = 0; i < operation.size(); i++){
            count = 1;
            for (int j = i+1; j < operation.size(); j++){
                if(operation.get(j).getTime() - operation.get(i).getTime() <= timeWindow){
                    count++;
                }
            }
            if(count > maxPeakLoad){
                maxPeakLoad = count;
            }
        }
        return maxPeakLoad;
    }

    /**
     * Helper method. Sort the time delay queue
     */
    public synchronized void sortQueueByTime(){
        queue.stream().sorted();
    }

    /**
     * Helper method. find all msg
     */
    public synchronized List<PubSubMessage> allMsg(){
        List<PubSubMessage> allMsg = new ArrayList<>(queue);
        return allMsg;
    }

    // a comparator to sort messages
    private class PubSubMessageComparator implements Comparator<PubSubMessage> {
        public int compare(PubSubMessage msg1, PubSubMessage msg2) {
            return msg1.getTimestamp().compareTo(msg2.getTimestamp());
        }
    }

}
