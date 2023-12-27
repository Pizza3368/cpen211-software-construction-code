package datacenter;

import org.junit.Test;

import static org.junit.Assert.*;

public class Task2Tests {

    private Job j1 = new Job(10, 10);
    private Job j2 = new Job(10, 20);
    private Job j3 = new Job(15, 30);

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

        assertEquals(40, placement.getCost());
    }

    @Test
    public void test2() {
        Placement placement = new Placement();

        Processor p1 = new Processor(25);
        Processor p2 = new Processor(25);

        p1.addJob(j1);
        p1.addJob(j2);

        p2.addJob(j3);

        placement.addProcessor(p1);
        placement.addProcessor(p2);

        assertEquals(50, placement.getCost());
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

        assertEquals(20, placement.getMakeSpan());
    }

    @Test
    public void test4() {
        Placement placement = new Placement();

        Processor p1 = new Processor(25);
        Processor p2 = new Processor(25);

        p1.addJob(j1);

        p2.addJob(j2);
        p2.addJob(j3);

        placement.addProcessor(p1);
        placement.addProcessor(p2);

        assertEquals(25, placement.getMakeSpan());
    }

    @Test
    public void test5() {
        Placement placement1 = new Placement();
        Placement placement2 = new Placement();

        Processor p1 = new Processor(25);
        Processor p2 = new Processor(25);

        p1.addJob(j1);

        p2.addJob(j2);
        p2.addJob(j3);

        placement1.addProcessor(p1);
        placement1.addProcessor(p2);

        placement2.addProcessor(p1);
        placement2.addProcessor(p2);

        assertTrue(placement1.equals(placement2));
    }

    @Test
    public void test6() {
        Placement placement1 = new Placement();
        Placement placement2 = new Placement();

        Processor p1 = new Processor(25);
        Processor p2 = new Processor(25);

        p1.addJob(j1);

        p2.addJob(j2);
        p2.addJob(j3);

        placement1.addProcessor(p1);
        placement1.addProcessor(p2);

        placement2.addProcessor(p2);
        placement2.addProcessor(p1);

        assertFalse(placement1.equals(placement2));
    }

}
