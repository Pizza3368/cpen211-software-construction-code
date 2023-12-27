package datacenter;

public class Job {

    /*
        Abstraction Function:
            this represents a job with execution time this.executionTime
            and memory usage this.memoryUsage
        Representation Invariant:
            this.executionTime > 0 && this.memoryUsage > 0
     */

    private int executionTime;
    private int memoryUsage;

    /**
     *
     * @param memoryUsage the execution time of the job, > 0
     * @param executionTime the memory usage of the job, > 0
     */
    public Job(int executionTime, int memoryUsage) {
        this.memoryUsage = memoryUsage;
        this.executionTime = executionTime;
    }

    public int getExecutionTime() {
        return this.executionTime;
    }
    public int getMemoryUsage() {
        return this.memoryUsage;
    }
}