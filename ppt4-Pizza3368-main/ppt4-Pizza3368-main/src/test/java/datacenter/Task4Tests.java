package datacenter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class Task4Tests {

    @Test
    public void test1() {

        int timeLimit = 100;

        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job(1, 2));
        jobs.add(new Job(100, 320));

        Placement placement =
                PlacementOptimizer.arrange(jobs, timeLimit);

        /* Composing optimal placement */
        int numProcs = 2;

        Placement optimalPlacement = new Placement();

        for (int index = 0; index < numProcs; ++index) {
            Processor processor = new Processor(timeLimit);
            processor.addJob(jobs.get(index));
            optimalPlacement.addProcessor(processor);
        }

        assertEquals(322, optimalPlacement.getCost());
        assertTrue(optimalPlacement.equals(placement));

    }

    @Test
    public void test2() {

        int timeLimit = 20;

        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job(3, 7));
        jobs.add(new Job(6, 6));
        jobs.add(new Job(5, 8));
        jobs.add(new Job(6, 4));
        jobs.add(new Job(8, 9));
        jobs.add(new Job(12, 8));

        Placement placement =
                PlacementOptimizer.arrange(jobs, timeLimit);

        assertEquals(17, placement.getCost());

    }

    @Test
    public void test3() {

        int timeLimit = 150;

        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job(27, 9));
        jobs.add(new Job(35, 10));
        jobs.add(new Job(75, 11));
        jobs.add(new Job(57, 4));
        jobs.add(new Job(11, 15));

        Placement placement =
                PlacementOptimizer.arrange(jobs, timeLimit);

        Placement optimalPlacement = new Placement();

        Processor processor0 = new Processor(timeLimit);
        processor0.addJob(jobs.get(0));
        processor0.addJob(jobs.get(1));
        placement.addProcessor(processor0);

        Processor processor1 = new Processor(timeLimit);
        processor1.addJob(jobs.get(2));
        processor1.addJob(jobs.get(3));
        processor1.addJob(jobs.get(4));
        placement.addProcessor(processor1);

        assertTrue(optimalPlacement.equals(placement));

        assertEquals(25, placement.getCost());
    }

    @Test
    public void test4() {

        int timeLimit = 100;

        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job(105, 2));
        jobs.add(new Job(200, 1000));
        jobs.add(new Job(150, 70));

        Placement placement =
                PlacementOptimizer.arrange(jobs, timeLimit);

        assertEquals(0, placement.getCost());

    }

    @Test
    public void test5() {

        int timeLimit = 150;

        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job(27, 9));
        jobs.add(new Job(35, 10));
        jobs.add(new Job(75, 11));
        jobs.add(new Job(60, 4));
        jobs.add(new Job(11, 16));
        jobs.add(new Job(50, 15));
        jobs.add(new Job(50, 17));

        Placement placement =
                PlacementOptimizer.arrange(jobs, timeLimit);

        assertEquals(32, placement.getCost());

    }

    @Test
    public void test6() {

        int timeLimit = 100;

        List<Job> jobs = new ArrayList<>();
        int numJobs = 50;

        for (int index = 0; index < numJobs; ++index) {
            jobs.add(new Job(2, 3));
        }

        Placement placement =
                PlacementOptimizer.arrange(jobs, timeLimit);

        Placement myPlacement = new Placement();

        Processor processor = new Processor(timeLimit);
        for (int index = numJobs - 1; index >= 0; --index) {
            processor.addJob(jobs.get(index));
        }

        myPlacement.addProcessor(processor);

        assertFalse(myPlacement.equals(placement));
    }

}