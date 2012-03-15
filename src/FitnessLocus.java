import java.util.ArrayList;

/**
 * Created by bingjun at 7/8/11 11:43 AM
 */

public class FitnessLocus extends Locus {

    public static final int FITNESS_EFFECTS_INITIAL_SIZE = 3;

    //    private ArrayList<Float> fitnessEffects = new ArrayList<Float>();
//    private ArrayList<Long> mutationIDs = new ArrayList<Long>();
    
    private float[] fitnessEffects = new float[FITNESS_EFFECTS_INITIAL_SIZE];
    private int[] mutationIDs = new int[FITNESS_EFFECTS_INITIAL_SIZE];
    private float currentFitness = 1f;
    private int nMutations = 0;
    private int nDeleteriousMutations = 0;
    private int nBeneficialMutations = 0;

    public FitnessLocus() {}

    public void addFitnessEffect(float fitnessEffect) {
        fitnessEffects[nMutations] = fitnessEffect;
        currentFitness *= fitnessEffect;
        nMutations++;
        if (fitnessEffect > 1) {
            nBeneficialMutations++;
        } else if (fitnessEffect < 1) {
            nDeleteriousMutations++;
        }
        if (nMutations == fitnessEffects.length) {
            System.out.println("FitnessLocus: resizing mutations array");
            float[] newFitnessEffects = new float[fitnessEffects.length + FITNESS_EFFECTS_INITIAL_SIZE];
            System.arraycopy(fitnessEffects, 0, newFitnessEffects, 0, fitnessEffects.length);
            fitnessEffects = newFitnessEffects;
        }
    }

    public void addMutationID(int mutationID) {
        mutationIDs[nMutations] = mutationID;
    }

    public Object clone() throws CloneNotSupportedException {
        FitnessLocus cloned = null;
        try {
            cloned = (FitnessLocus) super.clone();
            cloned.initFitnessEffects();
            cloned.fitnessEffects = this.fitnessEffects.clone();
            cloned.initMutationIDs();
            for (Long mutationID : mutationIDs) {
                cloned.addMutationID(mutationID);
            }
            this.nDeleteriousMutations = cloned.nDeleteriousMutations;
            this.nBeneficialMutations = cloned.nBeneficialMutations;
            this.currentFitness = cloned.currentFitness;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cloned;
    }

    private void initMutationIDs() {
        mutationIDs = new ArrayList<Long>();
    }

    protected void initFitnessEffects() {
        fitnessEffects = new float[FITNESS_EFFECTS_INITIAL_SIZE];
        currentFitness = 1f;
        nMutations = 0;
        nDeleteriousMutations = 0;
        nBeneficialMutations = 0;
    }


    public float getFitnessEffect() {
        return currentFitness;
/*
        float effect = 1;

        if (nMutations == 0) {
            return effect;
        }

        for (int mutation = 0; mutation < nMutations; mutation++) {
            effect *= fitnessEffects[mutation];
        }
        return effect;
*/
    }

    public int getNDeleteriousMutations() {
        return nDeleteriousMutations;
    }

    public int getNBeneficialMutations() {
        return nBeneficialMutations;
    }

    public ArrayList<Long> getMutationIDsArray() {
        return mutationIDs;
    }
}
