package datacenter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Task3Tests {

    private Job j1 = new Job(10, 10);
    private Job j2 = new Job(10, 20);
    private Job j3 = new Job(15, 30);

    private static final double delta = 0.01;

    @Test
    public void test1() {
        Placement placement = new Placement();

        Processor p1 = new Processor(25);
        Processor p2 = new Processor(25);

        p1.addJob(j1);

        p2.addJob(j2);
        p2.addJob(j3);

        placement.addProcessor(p1);
        placement.addProcessor(p2);

        assertEquals(15, placement.getMeanFlowTime(), delta);
    }

    @Test
    public void test2() {
        Placement placement = new Placement();

        Processor p1 = new Processor(25);
        Processor p2 = new Processor(25);

        p1.addJob(j1);

        p2.addJob(j2);
        p2.addJob(j3);

        placement.addProcessor(p1);
        placement.addProcessor(p2);

        assertEquals(10, placement.getMedianFlowTime(), delta);
    }

    @Test
    public void test3() {
        Placement placement = new Placement();

        Processor p1 = new Processor(25);
        Processor p2 = new Processor(25);

        p1.addJob(j1);
        p1.addJob(j2);

        p2.addJob(j3);

        placement.addProcessor(p1);
        placement.addProcessor(p2);

        assertEquals(15, placement.getMeanFlowTime(), delta);
        assertEquals(15, placement.getMedianFlowTime(), delta);
    }

}
