import java.util.ArrayList;

/**
 * Created by bingjun at 7/8/11 11:43 AM
 */

public class FitnessLocus extends Locus {

    private ArrayList<Float> fitnessEffects = new ArrayList<Float>();
    private ArrayList<Long> mutationIDs = new ArrayList<Long>();

    public FitnessLocus() {}

    public void addFitnessEffect(float fitnessEffect) {
        fitnessEffects.add(fitnessEffect);
    }

    public void addMutationID(Long mutationID) {
        mutationIDs.add(mutationID);
    }

    public Object clone() throws CloneNotSupportedException {
        FitnessLocus cloned = null;
        try {
            cloned = (FitnessLocus) super.clone();
            cloned.initFitnessEffects();
            for (Float fitnessEffect : fitnessEffects) {
                cloned.addFitnessEffect(fitnessEffect);
            }
            cloned.initMutationIDs();
            for (Long mutationID : mutationIDs) {
                cloned.addMutationID(mutationID);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cloned;
    }

    private void initMutationIDs() {
        mutationIDs = new ArrayList<Long>();
    }

    protected void initFitnessEffects() {
        fitnessEffects = new ArrayList<Float>();
    }


    public float getFitnessEffect() {
        float effect = 1;

        for (float fitnessEffect : fitnessEffects) {
            effect *= fitnessEffect;
        }
        return effect;
    }

    public ArrayList<Float> getFitnessEffectsArray() {
        return fitnessEffects;
    }

    public ArrayList<Long> getMutationIDsArray() {
        return mutationIDs;
    }
}
