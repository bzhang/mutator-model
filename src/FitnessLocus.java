import java.util.ArrayList;

/**
 * Created by bingjun at 7/8/11 11:43 AM
 */

public class FitnessLocus extends Locus {

    private ArrayList<Integer> mutationIndexes;

    public FitnessLocus(int mutationIndex) {
        mutationIndexes = new ArrayList<Integer>();
        mutationIndexes.add(mutationIndex);
    }

    public void addMutationIndexes(int mutationIndex) {
        mutationIndexes.add(mutationIndex);
    }

    public Object clone() throws CloneNotSupportedException {
        FitnessLocus cloned = null;
        try {
            cloned = (FitnessLocus) super.clone();
            cloned.initMutationIndexes();
            for (int mutationIndex : mutationIndexes) {
                cloned.addMutationIndexes(mutationIndex);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cloned;
    }

    protected void initMutationIndexes() {
        mutationIndexes = new ArrayList<Integer>();
    }

    //TODO: add new method to retrieve fitness effect from hashmap for each mutation.


    //TODO: change the way to calculate fitness


    public float getFitnessEffect() {
        float effect = 1;
        for (float e : fitnessEffects) {
            effect *= e;
        }
        return effect;
    }

    public ArrayList<Float> getFitnessEffectsArray() {
        return fitnessEffects;
    }

}
