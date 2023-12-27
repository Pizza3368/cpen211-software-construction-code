
# Simple Job Scheduling in a Data Center

You want to automate aspects of computational job scheduling. A computational job has an execution time requirement (how long does the job execute?) and a memory requirement (how much memory is needed to execute the job?).

## Processors

A data center contains many processors, and we can rent processors **but** any processor can compute for at most `T` time units (`T` is a parameter). The memory capacity of each processor is *elastic*: it can grow to accommodate the job with the highest memory requirement that is allocated to it **but** the processor cost is directly proportional to the peak memory utilization.

To illustrate these constraints, consider two jobs J<sub>1</sub>, with execution time requirement of 5 time units and memory requirement of 10 units, and J<sub>2</sub>, with execution time requirement of 4 time units and memory requirement of 12 units. Let P be a processor with maximum computation time `T`. Suppose `T` = 10 time units. Then, these two jobs can be scheduled on processor P because their total computation time is 9 time units. The peak memory utilization of P is 12 units (from J<sub>2</sub>) and, therefore, the cost of allocating these two jobs to P is 12 units. From this example, it should be clear that: 
- A job can fit on a processor, if adding the job does not cause the total execution time of the processor to exceed its time limit.
- The peak memory usage of a processor is equal to the maximum memory usage of any job assigned to it.
- The cost of a processor is equal to its peak memory usage. 

## Placements (or Schedules)

You would like to schedule **n** jobs, J<sub>1</sub> ... J<sub>n</sub>, and you can use multiple processors in the data center. The total scheduling cost is the sum of the costs incurred per processor. 

Your job scheduling decisions are, however, further constrained by the following **no-job-shuffling** rule. For all pairs of jobs J<sub>x</sub> and J<sub>y</sub> with y > x, we must have one of the following two situations:

1. Jobs J<sub>x</sub>, J<sub>x+1</sub>, ..., J<sub>y</sub> are all assigned to the same processor and are executed in this order, or
2. Jobs J<sub>x</sub> and J<sub>y</sub> are assigned to different processors.

What this rule implies is that if jobs J<sub>5</sub> and J<sub>8</sub> are assigned to one processor then so are jobs J<sub>6</sub> and J<sub>7</sub>, and the jobs are executed in the order J<sub>5</sub>, J<sub>6</sub>, J<sub>7</sub> and J<sub>8</sub> (and possibly some jobs before and after these).

Your goal, given a list of jobs J<sub>1</sub> ... J<sub>n</sub> and a limit `T` on the maximum computation time per processor, is to reason about different job placements and to compute an optimal placement.

To achieve this goal, you will work through a sequence of four tasks.

## Task 1

You should implement the following methods in `datacenter.Processor`:

```
public boolean canFitJob(Job job)
public boolean addJob(Job job)
public int     getPeakMemoryUsage()
public int     getTotalComputationTime()
public Job[]   getJobs()
public boolean equals(Processor other)	
```

## Task 2

You should implement the following methods in `datacenter.Placement`:

```
public int     getCost()
public boolean equals(Placement other)
public int     getMakeSpan()
```

The **makespan** of a placement or schedule is the latest time at which a processor becomes idle. If we have three processors, and one has 8 time units of work, the second has 13 time units of work, and the third has 12 time units of work, then the makespan is 13 time units because the second processor will become idle after 13 time units. In this example, we assume that `T` is at least 13 time units.

## Task 3

You should implement the following methods in `datacenter.Placement` to compute other useful metrics:

```
public double  getMeanFlowTime()
public double  getMedianFlowTime()
```


The **flow time** of a job is the total time a job spends in the system before it is completed. We assume that all jobs are ready to start execution at time t = 0. As an example, if jobs J<sub>1</sub> and J<sub>2</sub>, with execution times 5 and 4, respectively, are the only two jobs assigned to a processor then J<sub>1</sub> will finish at time 5 and J<sub>2</sub> will finish at time 9. The flow times are 5 and 9, respectively.


## Task 4

The final task is to compute an optimal placement or assignment of jobs to processors. An optimal placement is one that achieves the minimum total cost, and the cost is based on the peak memory utilization per processor.

For this task, you should implement the following method in the class `PlacementOptimizer`:

```
public static Placement getOptimalPlacement(List<Job> jobs, int maxComputeTimePerProcessor)
```

## Logistics

### Duration and Grading

**Duration**

You have 75 minutes to complete all four tasks.

**Grading**

| Work Accomplished                                      | Grade |
| ------------------------------------------------------ | ----- |
| Task 1 does not pass all hidden tests                  | F     |
| Task 1 passes all hidden tests                         | C     |
| Tasks 1, 2 pass all hidden tests                       | B     |
| Tasks 1, 2 and 3 pass all hidden tests | A     |
| Tasks 1 through 4 pass all hidden tests                | A+    |

### Submission Instructions

+ Submit your work to the Github classroom repository that was created for you.
+ **Do not alter the directory/folder structure. You should retain the structure as in this repository.**
+ Do not wait until the last minute to push your work to Github. It is a good idea to push your work at intermediate points as well. _We would recommend that you get your Git and Github workflow set up at the start._

### What Should You Implement / Guidelines

+ You should implement all the methods that are indicated with `TODO`.
+ Passing the provided tests is the minimum requirement. Use the tests to identify cases that need to be handled. Passing the provided tests is *not sufficient* to infer that your implementation is complete and that you will get full credit. Additional tests will be used to evaluate your work. The provided tests are to guide you.
+ You can implement additional helper methods if you need to but you should keep these methods `private` to the appropriate classes.
+ You do not need to implement new classes.
+ You can use additional **standard** Java libraries by importing them.
+ Do not throw new exceptions unless the specification for the method permits exceptions.


## Honour Code

By submitting your work to Github you agree to the following:

+ You did not consult with any other person for the purpose of completing this activity.
+ You did not aid any other person in the class in completing their activity.
+ If you consulted any external sources, such as resources available on the World Wide Web, in completing the examination then you have cited the source. (You do not need to cite class notes or Sun/Oracle Java documentation.)
+ You are not aware of any infractions of the honour code for this examination.

## Answers to FAQs

* **Can I consult Java documentation and other Internet-based sources?**

   Yes, you can. The point of this test is not to demonstrate mastery over syntax but that you can solve a problem in a    reasonable amount of time with reasonable resources.

   *If you find useful information online outside the official Java documentation and the course material, you must cite the source. You should do so by adding comments in your source code.*

   Naturally you are expected to adhere to all of the course and UBC policies on academic integrity.

* **Isn't 75 minutes insufficient time to produce a working implementation?**

   The questions are straightforward, and these are not very different from what one might sometimes encounter on a job interview (for example). The difference is that you get less time during an interview (10-15 minutes) with no access to additional resources. So the time allotted is reasonable in that regard and I am expecting that everyone will be able to clear this bar. The goal is that it is possible to say, at a minimal level, what everyone who completes this course can achieve.

* **Why am I not guaranteed full credit if my implementation passes all the provided tests?**

   It is easy to develop an implementation that passes the provided tests and not much else. A good-faith implementation that passes all the provided tests is very likely to pass other tests too.
