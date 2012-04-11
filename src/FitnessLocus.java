/**
 * @author Bingjun Zhang
 */

public class FitnessLocus extends Locus {

    public static final int FITNESS_EFFECTS_INITIAL_SIZE = ModelParameters.getInt("FITNESS_EFFECTS_INITIAL_SIZE");

//    private float[] fitnessEffects = new float[FITNESS_EFFECTS_INITIAL_SIZE];
    private long[]   mutationIDs = new long[FITNESS_EFFECTS_INITIAL_SIZE];
    private float currentFitness = 1f;
    private int   nMutations     = 0;
    private int   nDeleteriousMutations = 0;
    private int   nBeneficialMutations  = 0;

    public FitnessLocus() {}

    public void updateFitnessEffect(double fitnessEffect) {
        currentFitness *= fitnessEffect;
        updateNMutations(fitnessEffect);

    }

    private void updateNMutations(double fitnessEffect) {
        if (fitnessEffect > 1) {
            nBeneficialMutations++;
        } else if (fitnessEffect < 1) {
            nDeleteriousMutations++;
        }
    }

    //  use this method to calculate fitnessEffect for each locus if need to store fitnessEffects array
//    public void addFitnessEffect(float fitnessEffect) {
//        fitnessEffects[nMutations] = fitnessEffect;
//        currentFitness *= fitnessEffect;
//        updateNMutations(fitnessEffect);
//        if (nMutations == fitnessEffects.length) {
//            resizeArray(fitnessEffects);
//        }
//    }

    public void addMutationID(long mutationID) {
        mutationIDs[nMutations] = mutationID;
        nMutations++;
        if (nMutations == mutationIDs.length) {
            mutationIDs = resizeArray(mutationIDs);
            System.out.println("mutationIDs: " + mutationIDs.length);
        }
    }

//    private void resizeArray(float[] floatArray) {
//            System.out.println("FitnessLocus: resizing mutations array");
//            float[] newFloatArray = new float[floatArray.length + FITNESS_EFFECTS_INITIAL_SIZE];
//            System.arraycopy(floatArray, 0, newFloatArray, 0, floatArray.length);
//            floatArray = newFloatArray;
//    }

    private long[] resizeArray(long[] longArray) {
        System.out.println("FitnessLocus: resizing mutations array");
        long[] newLongArray = new long[longArray.length + FITNESS_EFFECTS_INITIAL_SIZE];
        System.arraycopy(longArray, 0, newLongArray, 0, longArray.length);
        longArray = newLongArray;
        return longArray;
    }

    public Object clone() throws CloneNotSupportedException {
        FitnessLocus cloned = null;
        try {
            cloned = (FitnessLocus) super.clone();
//            cloned.initFitnessEffects();
//            cloned.fitnessEffects = this.fitnessEffects.clone();
            cloned.initMutationIDs();
            cloned.mutationIDs = this.mutationIDs.clone();
            cloned.nDeleteriousMutations = this.nDeleteriousMutations;
            cloned.nBeneficialMutations = this.nBeneficialMutations;
            cloned.currentFitness = this.currentFitness;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cloned;
    }

    private void initMutationIDs() {
        mutationIDs = new long[FITNESS_EFFECTS_INITIAL_SIZE];
    }

//    protected void initFitnessEffects() {
//        fitnessEffects = new float[FITNESS_EFFECTS_INITIAL_SIZE];
//        currentFitness = 1f;
//        nMutations = 0;
//        nDeleteriousMutations = 0;
//        nBeneficialMutations = 0;
//    }


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

    public long[] getMutationIDsArray() {
        return mutationIDs;
    }
}
