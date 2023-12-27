package evolution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Evolution {

    /**
     * Compute the number of cells that survive the evolutionary process as
     * described in the problem statement assuming that a given cell dies during
     * the process.
     *
     * @param evolutionaryTree array that represents the normal evolution of the organism
     * @param deadCell         the index in evolutionaryTree of the cell that dies
     * @return the number of surviving cells in the mature organism
     * @requires - evolutionaryTree will contain exactly N elements, where N is
     * an odd integer between 1 and 50, inclusive.
     * - There will be exactly one "-1" element in evolutionaryTree.
     * - Every element of evolutionaryTree will be between -1 and N-1, inclusive.
     * - evolutionaryTree will form a binary tree.
     * - deadCell will be between 0 and N-1, inclusive.
     */
    public static int numSurvivingCells(int[] evolutionaryTree, int deadCell) {
        Set<Integer> survived = getSurvivingCells(evolutionaryTree, deadCell);
        return survived.size();


    }

    /**
     * Compute the number of cells that survive the evolutionary process as
     * described in the problem statement assuming that a given cell dies during
     * the process.
     *
     * @param evolutionaryTree array that represents the normal evolution of the organism
     * @param deadCell         the index in evolutionaryTree of the cell that dies
     * @return the set of surviving cells in the mature organism
     * @requires - evolutionaryTree will contain exactly N elements, where N is
     * an odd integer between 1 and 50, inclusive.
     * - There will be exactly one "-1" element in evolutionaryTree.
     * - Every element of evolutionaryTree will be between -1 and N-1, inclusive.
     * - evolutionaryTree will form a binary tree.
     * - deadCell will be between 0 and N-1, inclusive.
     */
    public static Set<Integer> getSurvivingCells(int[] evolutionaryTree,
                                                 int deadCell) {

        Set<Integer> survived = lowestLayer(evolutionaryTree);
        Set<Integer> temp = new HashSet<>();
        for(int i: survived){
            if(!ancestor(i, evolutionaryTree).contains(deadCell)){
                temp.add(i);
            }
        }
        temp.remove(deadCell);
        return temp; // TODO: Implement this method
    }

    private static  Set<Integer> ancestor(int target, int[] tree){
        Set<Integer> anc = new HashSet<>();

        if(tree[target] == -1) {
            return anc;
        }

        else{
            anc.addAll(ancestor(tree[target], tree));
            anc.add(tree[target]);
        }
        return anc;
    }

    private static Set<Integer> lowestLayer(int[] tree){
        Set<Integer> possible = new HashSet<>();


        for(int i=0; i<tree.length; i++){
            possible.add(i);
        }

        for(int i=0; i<tree.length; i++){
            possible.remove(tree[i]);
        }

        return possible;
    }
}
