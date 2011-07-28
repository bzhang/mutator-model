/**
 * @author Bingjun
 * 7/7/11 11:52 AM
 */

public class MutatorLocus implements Locus {

    private int strength;

    public MutatorLocus(int strength) {
        this.strength = strength;
    }

    public MutatorLocus clone() {
        return new MutatorLocus(getStrength());
    }

    public int getStrength() {
        return strength;
    }
}
