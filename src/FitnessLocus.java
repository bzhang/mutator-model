import java.util.ArrayList;
import java.util.Map;

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


    public float getFitnessEffect(Map mutationMap) {
        float effect = 1;
        float currentEffect;
        Map mutationProperties;

        for (long mutationID : mutationIDs) {
            mutationProperties = (Map) mutationMap.get(mutationID);
            currentEffect = (Float) mutationProperties.get("FitnessEffect");
            effect *= currentEffect;
        }
        return effect;
    }

    public ArrayList<Long> getMutationIDsArray() {
        return mutationIDs;
    }
}
