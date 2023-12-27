package stablemarriages;

import java.util.Arrays;
import java.util.List;

public class Utils {

    /**
     * Check if a matching is stable
     * @param matching is not null, and of length 2 * n,
     *                 where index 2 * i represents a girl
     *                 match with boy at index 2 * i + 1
     * @param preferencesOfGirls is a permutation of [0, 1, ..., n-1]
     * @param preferencesOfBoys is a permutation of [0, 1, ..., n-1]
     * @return true if matching stable and false otherwise
     *
     */
    public static boolean isStable(Integer[] matching,
                                   List<List<Integer>> preferencesOfGirls,
                                   List<List<Integer>> preferencesOfBoys) {
        // TODO: Implement this method


        //girls
        for(int i=0; i<matching.length -1; i = i +2 ){
            int tGirl = matching[i];
            int tBoy = matching[i+1];
            List<Integer> girlPreference = preferencesOfGirls.get(tGirl);


            int counter = 0;
            while(counter < girlPreference.indexOf(tBoy)){
                int tryBoy = girlPreference.get(counter);

                if( canDo(tryBoy, tGirl, matching, preferencesOfGirls, preferencesOfBoys)){
                    return false;
                }

                counter++;
            }
        }

        return true;
    }

    public static boolean canDo(int boy, int girl, Integer[] matching,
                                List<List<Integer>> preferencesOfGirls,
                                List<List<Integer>> preferencesOfBoys){


        int boyIndex = 0;
        for(int i=0; i<matching.length; i++){
            if(matching[i] == boy && i%2 ==1 ){
                boyIndex = i;
                break;
            }
        }
        int currentGirlIndex = boyIndex-1;
        int currentGirl = matching[currentGirlIndex];

        List<Integer> boyPreference = preferencesOfBoys.get(boy);

        int currentScore = boyPreference.indexOf(currentGirl);
        int possibleScore = boyPreference.indexOf(girl);

        if(possibleScore<currentScore){
            return true;
        }

        return false;
    }

    /**
     * Simple helper class for JSON formatting
     */
    static class ProblemInstance {
        public List<List<Integer>> preferencesOfGirls;
        public List<List<Integer>> preferencesOfBoys;
        public Integer[] solution;
    }
}
