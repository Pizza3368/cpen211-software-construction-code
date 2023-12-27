package timedelayqueue;

import java.sql.Timestamp;
import java.util.*;

/**
 * Creates a TimeDelayQueue with a set delay time for messages with timestamps
 * <p>
 * Rep invariant:
 * <p>
 *    totalMsgCount >= 0
 *    delay >= 0
 *    load is not null and consists of key-value pairs >= 0
 *    messageQueue is not null and has size >= 0
 *    all instances of PubSubMessage in the queue are not null and unique (identified by UUID)
 *    totalMsgCount = messageQueue.size()
 * </p>
 *
 * Abstract Function
 *     AF(messageQueue) = a collection of unique PubSubMessages sorted from the earliest to the latest timestamps
 *                        that are retrievable in FIFO manner only if such message's timestamp >= delay
 *
 * <p>
 * Thread safety argument:
 *     Synchronization: the 2 write operations: add() and getNext() are synchronized, as well as peakLoad().
 *     All operations performed over the lifetime of TimeDelayQueue are stored in a thread-safe data structure.
 *     Any iteration done over the structure are wrapped in synchronized block to eliminate data races.
 * </p>
 */
public class TimeDelayQueue {
    private long totalMsgCount;
    private final int delay;
    private final Map<Long, Long> load;
    private final PriorityQueue<PubSubMessage> messageQueue;
    private final Set<UUID> haveSeen;

    /**
     * Create a new TimeDelayQueue with a specified delay from when messages are
     * first added to the queue to when they are available for access
     *
     * @param delay the delay, in milliseconds, that the queue can tolerate, >= 0
     */
    public TimeDelayQueue(int delay) {
        totalMsgCount = 0L;

        this.delay = delay;
        messageQueue = new PriorityQueue<>(new PubSubMessageComparator());
        load = Collections.synchronizedMap(new HashMap<>());
        haveSeen = new HashSet<>();
    }

    /**
     * Adds a new message to the TimeDelayQueue, supports both
     * transient and permanent messages
     *
     * @param msg the message to be added to the queue, is not null
     * @return true if message with the same ID as msg is not already in the queue,
     *         and that the msg is added to the queue, false otherwise
     */
    public synchronized boolean add(PubSubMessage msg) {
        Timestamp start = new Timestamp(System.currentTimeMillis());

        if (messageQueue.contains(msg)) {
            Timestamp end = new Timestamp(System.currentTimeMillis());
            load.put(start.getTime(), end.getTime());

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return false;
        }

        boolean success = messageQueue.add(msg);
        totalMsgCount++;

        Timestamp end = new Timestamp(System.currentTimeMillis());
        load.put(start.getTime(), end.getTime());

        // delay 1 millis
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return success;
    }

    /**
     * Get the count of the total number of messages processed
     * by this TimeDelayQueue
     *
     * @return total number of messages (transient and permanent) over the lifetime of the
     *         timeDelayQueue
     */
    public long getTotalMsgCount() {
        Timestamp start = new Timestamp(System.currentTimeMillis());

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        load.put(start.getTime(), start.getTime());
        return this.totalMsgCount;
    }

    /**
     * Get the next accessible message from TimeDelayQueue
     *
     * @return return the next message or PubSubMessage.NO_MSG
     *         if there is no suitable message at this time point
     */
    public synchronized PubSubMessage getNext() {
        Timestamp start = new Timestamp(System.currentTimeMillis());
        long startTime = start.getTime();

        // remove expired transient messages
        // keep running until msg at top of tdq is a not an expired transient msg
        while (!messageQueue.isEmpty() && startTime - messageQueue.peek().getTimestamp().getTime() >= delay
                && messageQueue.peek() instanceof TransientPubSubMessage) {
            long creationTime = messageQueue.peek().getTimestamp().getTime();
            long allowedSurvivalTime = creationTime + ((TransientPubSubMessage) messageQueue.peek()).getLifetime();
            long currentTime = new Timestamp(System.currentTimeMillis()).getTime();

            if (currentTime > allowedSurvivalTime) {
                messageQueue.poll();
            } else {
                break;
            }
        }

        // get next accessible message
        if (!messageQueue.isEmpty() && startTime - messageQueue.peek().getTimestamp().getTime() >= delay) {
            PubSubMessage msg = messageQueue.poll();
            Timestamp end = new Timestamp(System.currentTimeMillis());
            load.put(startTime, end.getTime());

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            haveSeen.add(msg.getId());
            return msg;
        }

        Timestamp end = new Timestamp(System.currentTimeMillis());
        load.put(startTime, end.getTime());

        // delay 1 millis
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return PubSubMessage.NO_MSG;
    }

    public Set<UUID> getHaveSeen() {
        return haveSeen;
    }

    /**
     *
     * @param timeWindow a non-negative number denoting the range in milliseconds
     *                   cannot be greater than the lifetime of the timeDelayQueue
     * @return the greatest number of operations that timeDelayQueue performed over timeWindow
     */
    public synchronized int getPeakLoad(int timeWindow) {
        if (timeWindow == 0) {
            return 0;
        }

        int peakLoad = 0;
        // iterates over all possible windows over the lifetime of the timeDelayQueue
        for (Map.Entry<Long, Long> entry : load.entrySet()) {
            // specify a new window to obtain the number of operations performed
            long startWindow = entry.getKey();
            long endWindow = startWindow + timeWindow;

            // stream and filter operations whose start and end times are not within the window
            int currLoad = (int) load.entrySet().stream().filter(k -> (k.getKey() >= startWindow
                    && k.getKey() <= endWindow) || (k.getValue() >= startWindow && k.getValue() <= endWindow)
                    || (k.getKey() <= startWindow && k.getValue() >= endWindow)).count();
            // update peakLoad if a higher load is found
            if (currLoad > peakLoad) {
                peakLoad = currLoad;
            }
        }
        return peakLoad;
    }

    /**
     *  Comparator to sort messages based on the timestamps,
     *  from earliest (smallest) to latest (largest)
     */
    private static class PubSubMessageComparator implements Comparator<PubSubMessage> {
        public int compare(PubSubMessage msg1, PubSubMessage msg2) {
            return msg1.getTimestamp().compareTo(msg2.getTimestamp());
        }
    }
}
