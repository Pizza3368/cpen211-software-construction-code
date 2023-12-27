package datacenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Placement {
    private List<Processor> processors;

    /** Create a new empty placement
     *
     */
    public Placement() {
        this.processors = new ArrayList<Processor>();
    }

    /** Add a processor to this placement
     *
     */
    public void addProcessor(Processor processor) {
        this.processors.add(processor);
    }

    /** Get the cost of this placement
     *
     *  @return cost
     */
    public int getCost() {
        // TODO: Implement this
        int cost =0;
        for(Processor p : this.processors){
            cost = cost + p.getPeakMemoryUsage();
        }

        return cost;
    }

    /**
     * Compute the makespan of this placement
     *
     * @return the makespan of the placement, and return 0, if there is no work
     * (no processors or no jobs on any processor)
     */
    public int getMakeSpan() {
        // TODO: Implement this
        int makespan =0;
        for(Processor p: this.processors){
            if(makespan<= p.getTotalComputationTime()){
                makespan = p.getTotalComputationTime();
            }
        }
        return makespan;
    }

    /** Check if this placement is equal to another given placement
     *
     * @param that is the other placement to check
     * @return true if (1) number of processors in "that" is
     * equal to the number of processors in "this", and (2) each processor
     * in "this" is equal to the corresponding processor in "that"
     * (order of processors does matter)
     * */
    public boolean equals(Placement that) {
        // TODO: Implement this
        if(this.processors.size() != that.processors.size()){
            return false;
        }

        for(int i =0; i<this.processors.size(); i++){
            if(!this.processors.get(i).equals(that.processors.get(i))){
                return false;
            }
        }
        return true;
    }

    /** Obtain the mean flow time for this placement
     *
     * @return the mean flow time, and return 0 if there is no work (no processors or no jobs on any processor)
     */
    public double getMeanFlowTime() {
        // TODO: Implement this method
        int temp =0;
        List<Integer> master = new ArrayList<>();

        if(this.processors.size() == 0){
            return temp;
        }

        else{
            master = allProcessor();
            int sum = 0;
            for (int i=0; i< master.size(); i++) {
                sum += master.get(i);
            }
            temp =  sum / master.size();

        }


        return temp;
    }

    //helper that find flow time of each job in a processor
    public List<Integer> oneProcessor(Processor p){
        List<Integer> time = new ArrayList<>();
        if(p.getJobs().length==0){
            return time;
        }
        Integer temp =0;
        for(int i=0; i< p.getJobs().length; i++){
            temp = 0;
            for(int j=0; j<= i; j++){

                temp = temp + p.getJobs()[j].getExecutionTime();

            }
            time.add(temp);
        }

        return time;
    }

    //helper that return a list contain all flow time of all processor
    public List<Integer> allProcessor(){
        List<Integer> masterTimeList = new ArrayList<>();
        for(Processor p: this.processors){
            masterTimeList.addAll(oneProcessor(p));
        }

        return masterTimeList;
    }


    /** Obtain the median flow time for this placement
     *
     * @return the median flow time, and return 0 if there is no work (no processors or no jobs on any processors)
     */
    public double getMedianFlowTime() {
        // TODO: Implement this method
        double temp =0.0;
        List<Integer> master = new ArrayList<>();

        if(this.processors.size() == 0){
            return temp;
        }

        else{
            master = allProcessor();
            Collections.sort(master);

            if (master.size()%2 == 0) {
                temp = (master.get(master.size()/2) + master.get(master.size()/2 -1))/2.0;
            } else {
                temp = master.get(master.size()/2);
            }
        }


        return temp;
    }

}