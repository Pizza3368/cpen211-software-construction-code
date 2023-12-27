package datacenter;

import org.junit.Test;

import static org.junit.Assert.*;

public class Task1Tests {

    @Test
    public void test1() {
        Job j1 = new Job(10, 10);
        Job j2 = new Job(11, 11);
        Processor proc = new Processor(25);
        assertTrue(proc.canFitJob(j1));
        assertTrue(proc.canFitJob(j2));
        proc.addJob(j1);
        assertEquals(10, proc.getTotalComputationTime());
        assertTrue(proc.canFitJob(j2));
        proc.addJob(j2);
        assertEquals(21, proc.getTotalComputationTime());
    }

    @Test
    public void test2() {
        Job j1 = new Job(10, 10);
        Job j2 = new Job(11, 11);
        Processor proc = new Processor(25);
        proc.addJob(j1);
        assertEquals(10, proc.getPeakMemoryUsage());
        proc.addJob(j2);
        assertEquals(11, proc.getPeakMemoryUsage());
    }

    @Test
    public void test3() {
        Job j1 = new Job(10, 10);
        Job j2 = new Job(11, 11);
        Processor proc1 = new Processor(25);
        proc1.addJob(j1);
        proc1.addJob(j2);
        Processor proc2 = new Processor(25);
        proc2.addJob(j1);
        proc2.addJob(j2);
        assertTrue(proc1.equals(proc2));
    }

    @Test
    public void test4() {
        Job j1 = new Job(10, 10);
        Job j2 = new Job(11, 11);
        Processor proc1 = new Processor(11);
        proc1.addJob(j1);
        assertFalse(proc1.canFitJob(j2));
    }

    @Test
    public void test5() {
        Job j1 = new Job(10, 10);
        Job j2 = new Job(11, 11);

        Processor proc1 = new Processor(11);
        proc1.addJob(j1);

        Processor proc2 = new Processor(11);
        proc2.addJob(j1);

        assertTrue(proc1.equals(proc2));
    }

    @Test
    public void test6() {
        Job j1 = new Job(10, 10);
        Job j2 = new Job(11, 11);

        Processor proc1 = new Processor(11);
        proc1.addJob(j1);

        Processor proc2 = new Processor(11);
        proc2.addJob(j2);

        assertFalse(proc1.equals(proc2));
    }

    @Test
    public void test7() {
        Job j1 = new Job(10, 10);
        Job j2 = new Job(11, 11);

        Processor proc1 = new Processor(11);
        proc1.addJob(j1);
        proc1.addJob(j2);

        Processor proc2 = new Processor(11);
        proc2.addJob(j2);
        proc2.addJob(j1); // this is okay for the purposes of testing equals

        assertFalse(proc1.equals(proc2));
    }

    @Test
    public void test8() {
        Job[] jobs = new Job[]{
                new Job(10, 10),
                new Job(11, 11),
                new Job(12, 12)
        };

        Processor proc = new Processor(50);
        for (Job j: jobs) {
            proc.addJob(j);
        }

        assertArrayEquals(jobs, proc.getJobs());
    }

}
