/**
 * @author Bingjun
 * 7/7/11 11:52 AM
 */

public class MutatorLocus implements Locus {

    private static final int DEFAULT_MUTATOR_STRENGTH = 10;

    private float mutatorStrength;

    public MutatorLocus(){
        this(DEFAULT_MUTATOR_STRENGTH);
    }

    public MutatorLocus(float strength) {
        mutatorStrength = strength;
    }

    public float getMutatorStrength() {
        return mutatorStrength;
    }
}
