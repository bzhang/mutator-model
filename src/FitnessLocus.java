import java.util.ArrayList;

/**
 * Created by bingjun at 7/8/11 11:43 AM
 */

public class FitnessLocus extends Locus {

    private ArrayList<Long> mutationIDs = new ArrayList<Long>();

    public FitnessLocus() {}

    public void addMutationIDs(long mutationID) {
        mutationIDs.add(mutationID);
    }

    public Object clone() throws CloneNotSupportedException {
        FitnessLocus cloned = null;
        try {
            cloned = (FitnessLocus) super.clone();
            cloned.initMutationIDs();
            for (long mutationID : mutationIDs) {
                cloned.addMutationIDs(mutationID);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cloned;
    }

    protected void initMutationIDs() {
        mutationIDs = new ArrayList<Long>();
    }

    //TODO: add new method to retrieve fitness effect from hashmap for each mutation.

    //TODO: change the way to calculate fitness


//    public float getFitnessEffect() {
//        float effect = 1;
//        for (float e : fitnessEffects) {
//            effect *= e;
//        }
//        return effect;
//    }
//
//    public ArrayList<Float> getFitnessEffectsArray() {
//        return fitnessEffects;
//    }

}
