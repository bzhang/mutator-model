/**
 * @author Bingjun
 * 7/7/11 11:52 AM
 */
public class MutatorLocus implements Locus {
    private float mutatorStrength;
    private int mutatorPosition;

    public MutatorLocus(){
        mutatorStrength = 10;
        mutatorPosition = 1;
    }

    public float getMutatorStrength() {
        return mutatorStrength;
    }

    public int getMutatorPosition() {
        return mutatorPosition;
    }
}
