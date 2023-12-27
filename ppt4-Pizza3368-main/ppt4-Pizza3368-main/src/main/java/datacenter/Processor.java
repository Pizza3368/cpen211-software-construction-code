package datacenter;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Processor {

    /*
        Abstraction Function:
            this represents a processor with time capacity this.timeLimit
            and holds the jobs in this.jobs
        Representation Invariant:
            jobs != null && jobs does not contain nulls
            && timeLimit > 0
            && (there should be something more that you should think about)
     */

    private List<Job> jobs;
    private int timeLimit;

    /**
     * Create a new empty processor
     * @param timeLimit the limit on compute time on this processor, > 0
     */
    public Processor(int timeLimit) {
        this.timeLimit = timeLimit;
        this.jobs = new ArrayList<Job>();
    }

    /**
     * Check if a given job can fit in this processor
     *
     * @return true if adding the job does not exceed the time limit on this processor, and false otherwise.
     */
    public boolean canFitJob(Job job) {
        // TODO: Implement this
        int totaltime = 0;
        for(Job j: this.jobs){
            totaltime = totaltime+j.getExecutionTime();
        }

        if(totaltime+job.getExecutionTime() <= this.timeLimit){
            return true;
        }
        return false;
    }

    /** Inserts a job to the processor, at the end of its schedule
     *
     * @param job not null
     * @return true if the job can fit on this processor and was assigned, and false otherwise
     */
    public boolean addJob(Job job) {
        // TODO: Implement this

        if(canFitJob(job)){
            this.jobs.add(job);
            return true;
        }
        return false;
    }

    /** Get the peak memory usage of this processor
     *
     * @return the peak memory usage of the jobs ossigned to this processor
     * */
    public int getPeakMemoryUsage() {
        // TODO: Implement this
        int peak =0;
        for(Job j: this.jobs){
            if(j.getMemoryUsage()>= peak){
                peak = j.getMemoryUsage();
            }
        }
        return peak;
    }

    /** Get the total computation (execution) time of this processor
     *
     * @return the total computation (execution) time of jobs assigned
     * to this processor
     */
    public int getTotalComputationTime() {
        // TODO: Implement this
        int totaltime = 0;
        for(Job j: this.jobs){
            totaltime = totaltime+j.getExecutionTime();
        }
        return totaltime;
    }

    /** Check if this processor is equal to a given processor
     *
     * @return true if both processors have exactly the same jobs,
     * in the same order, and they have the same time limit
     */
    public boolean equals(Processor that) {
        // TODO: Implement this
        boolean time = false;
        boolean order = true;

        if(this.timeLimit == that.timeLimit){
            time = true;
        }

        Job[] jobone = this.getJobs();
        Job[] jobtwo = that.getJobs();

        if(jobone.length != jobtwo.length){
            return false;
        }

        else {
            for (int i = 0; i < jobone.length; i++) {
                if (jobone[i] != jobtwo[i]) {
                    order = false;
                }
            }

        }


        if(time == true && order == true){
            return true;
        }
        return false;
    }

    /** Get the time limit of this processor
     *
     * @return the time limit on this processor
     */
    public int getTimeLimit() {
        return this.timeLimit;
    }

    /**
     * Obtain the jobs scheduled on this processor, in scheduled order
     * @return the jobs scheduled on this processor, in scheduled order
     */
    public Job[] getJobs() {
        // TODO: Implement this method
//        Job[] array = new Job[this.jobs.size()];
//        this.jobs.toArray(Job);
        Job[] arr = this.jobs.toArray(new Job[0]);
        //return new Job[0];
        return arr;
    }
}