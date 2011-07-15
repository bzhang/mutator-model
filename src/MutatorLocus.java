/**
 * @author Bingjun
 * 7/7/11 11:52 AM
 */

public class MutatorLocus implements Locus {

    private float strength;

    public MutatorLocus(float strength) {
        this.strength = strength;
    }

    public float getStrength() {
        return strength;
    }
}
