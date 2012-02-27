import java.util.ArrayList;
import java.util.HashMap;
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


    public float getFitnessEffect(Map mutationMap) {
        float effect = 1;
        float currentEffect;

        for (long mutationID : mutationIDs) {
            currentEffect = (Float) mutationMap.get(mutationID);
            effect *= currentEffect;
        }
        return effect;
    }

    public ArrayList<Long> getMutationIDsArray() {
        return mutationIDs;
    }
}
