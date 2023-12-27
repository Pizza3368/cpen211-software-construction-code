package timedelayqueue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class timeDelayQueueTest {
    private static final int DELAY    = 40; // delay of 40 milliseconds
    private static final int NUM_MSGS = 10;

    private static final Gson gson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gson = gsonBuilder.create();
    }

    private class Sender implements Runnable {
        private int id;
        private TimeDelayQueue tdq;
        private List<PubSubMessage> msgList;

        public Sender(int id, TimeDelayQueue tdq, List<PubSubMessage> msgList) {
            this.id      = id;
            this.tdq     = tdq;
            this.msgList = msgList;
        }

        public void run() {
            int msgsAdded = 0;
            for (int i = 0; i < timeDelayQueueTest.NUM_MSGS; i++) {
//                System.out.printf("Thread %d : Message %d\n", id, i);
                UUID sndID        = UUID.randomUUID();
                UUID rcvID        = UUID.randomUUID();
                String text       = gson.toJson("loren ipsum");
                PubSubMessage msg = new PubSubMessage(sndID, rcvID, text);
                msgList.add(msg);
                if (tdq.add(msg)) {
                    msgsAdded++;
                }
                try {
                    Thread.sleep(1);
                }
                catch (InterruptedException ie) {
                    fail();
                }
            }
//            System.out.printf("Thread %d added %d messages\n", id, msgsAdded);
        }
    }

    private class Receiver implements Runnable {
        private TimeDelayQueue tdq;
        private List<PubSubMessage> msgList;

        public Receiver(TimeDelayQueue tdq, List<PubSubMessage> msgList) {
            this.tdq     = tdq;
            this.msgList = msgList;
        }

        public void run() {
            try {
                Thread.sleep(DELAY * timeDelayQueueTest.NUM_MSGS * 2);
            }
            catch (InterruptedException ie) {
                fail();
            }

            for (int i = 0; i < timeDelayQueueTest.NUM_MSGS; i++) {
                PubSubMessage msg = tdq.getNext();
                assertEquals(msgList.get(i), msg);
            }
        }
    }
    @Test
    public  void testPeakLoading(){
        TimeDelayQueue tdq = new TimeDelayQueue(DELAY);

        UUID sndID     = UUID.randomUUID();
        UUID rcvID     = UUID.randomUUID();
        String msgText = gson.toJson("test");
        long startTime = System.currentTimeMillis();
        PubSubMessage msg1 = new PubSubMessage(sndID, rcvID, msgText);
        tdq.add(msg1);
        try {
            Thread.sleep(2 * DELAY);
        }
        catch (InterruptedException ie) {
            // nothing to do but ...
            fail();
        }
        PubSubMessage msg2 = tdq.getNext();
        try {
            Thread.sleep(2 * DELAY);
        }
        catch (InterruptedException ie) {
            // nothing to do but ...
            fail();
        }
        tdq.add(msg2);
        try {
            Thread.sleep(2 * DELAY);
        }
        catch (InterruptedException ie) {
            // nothing to do but ...
            fail();
        }
        TransientPubSubMessage msg3 = new TransientPubSubMessage(msg1);
        tdq.add(msg3);
        try {
            Thread.sleep(2 * DELAY);
        }
        catch (InterruptedException ie) {
            // nothing to do but ...
            fail();
        }
        tdq.getNext();
        long FiveCommands = System.currentTimeMillis();
        try {
            Thread.sleep(2 * DELAY);
        }
        catch (InterruptedException ie) {
            // nothing to do but ...
            fail();
        }

        tdq.getNext();
        try {
            Thread.sleep(2 * DELAY);
        }
        catch (InterruptedException ie) {
            // nothing to do but ...
            fail();
        }
        tdq.getNext();
        try {
            Thread.sleep(2 * DELAY);
        }
        catch (InterruptedException ie) {
            // nothing to do but ...
            fail();
        }
        tdq.getNext();
        try {
            Thread.sleep(2 * DELAY);
        }
        catch (InterruptedException ie) {
            // nothing to do but ...
            fail();
        }
        tdq.getNext();
        try {
            Thread.sleep(2 * DELAY);
        }
        catch (InterruptedException ie) {
            // nothing to do but ...
            fail();
        }
        tdq.getNext();
        try {
            Thread.sleep(2 * DELAY);
        }
        catch (InterruptedException ie) {
            // nothing to do but ...
            fail();
        }
        tdq.getTotalMsgCount();
        try {
            Thread.sleep(2 * DELAY);
        }
        catch (InterruptedException ie) {
            // nothing to do but ...
            fail();
        }
        tdq.getTotalMsgCount();
        try {
            Thread.sleep(2 * DELAY);
        }
        catch (InterruptedException ie) {
            // nothing to do but ...
            fail();
        }
        long endTime = System.currentTimeMillis();
        int window = (int) ((endTime - startTime) % Integer.MAX_VALUE);
        tdq.getPeakLoad(window);
        int fiveWindow = (int) ((FiveCommands - startTime) % Integer.MAX_VALUE);
        assertEquals(10, tdq.getPeakLoad(window));
        assertEquals(5, tdq.getPeakLoad(fiveWindow));

    }

    @Test
    public void testEmptyTdq(){
        TimeDelayQueue tdq = new TimeDelayQueue(DELAY);

        UUID sndID     = UUID.randomUUID();
        UUID rcvID     = UUID.randomUUID();
        String msgText = gson.toJson("test");
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 1000L;
        int window = (int) ((endTime - startTime) % Integer.MAX_VALUE);
        assertEquals(0, tdq.getPeakLoad(window));
        assertEquals(0, tdq.getTotalMsgCount());
    }

    @Test
    public void testAddNullMsg(){
        TimeDelayQueue tdq = new TimeDelayQueue(DELAY);

        UUID sndID     = UUID.randomUUID();
        UUID rcvID     = UUID.randomUUID();
        String msgText = gson.toJson("test");
        PubSubMessage msg = null;
        tdq.add(msg);
        tdq.allMsg();

        //assertEquals(1, tdq.getTotalMsgCount());
        try {
            Thread.sleep(2 * DELAY);
        }
        catch (InterruptedException ie) {
            // nothing to do but ...
            fail();
        }
        //assertEquals(null, tdq.getNext());
    }

    @Test
    public void testAddAndGetMsg(){
        TimeDelayQueue tdq = new TimeDelayQueue(DELAY);

        UUID sndID     = UUID.randomUUID();
        UUID rcvID     = UUID.randomUUID();
        String msgText = gson.toJson("test");

        PubSubMessage msg = new PubSubMessage(sndID, rcvID, "Hello World");
        TransientPubSubMessage msg1 = new TransientPubSubMessage(sndID, rcvID, msgText, DELAY);

        assertTrue(tdq.add(msg));
        assertFalse(tdq.add(msg));
        tdq.add(msg1);
        assertEquals(PubSubMessage.NO_MSG,tdq.getNext());
        //assertEquals(1, tdq.getTotalMsgCount());
        try {
            Thread.sleep(2 * DELAY);
        }
        catch (InterruptedException ie) {
            // nothing to do but ...
            fail();
        }
        assertEquals(msg,tdq.getNext());
        assertEquals(PubSubMessage.NO_MSG,tdq.getNext());
        //assertEquals(null, tdq.getNext());
    }

    @Test
    public void testTransientMsg_InTime() {
        TimeDelayQueue tdq = new TimeDelayQueue(DELAY);

        UUID sndID     = UUID.randomUUID();
        UUID rcvID     = UUID.randomUUID();
        String msgText = gson.toJson("test");
        TransientPubSubMessage msg1 = new TransientPubSubMessage(sndID, rcvID, msgText, 80);
        PubSubMessage          msg2 = new PubSubMessage(sndID, rcvID, msgText);

        tdq.add(msg1);
        tdq.add(msg2);
        try {
            Thread.sleep(DELAY + 1);
        }
        catch (InterruptedException ie) {
            fail();
        }
        assertEquals(msg1, tdq.getNext());
        assertEquals(msg2, tdq.getNext());
    }
}
