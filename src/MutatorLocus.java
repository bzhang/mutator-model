/**
 * @author Bingjun
 * 7/7/11 11:52 AM
 */

public class MutatorLocus extends Locus {

    private int strength;

    public MutatorLocus(int strength) {
        this.strength = strength;
    }

    public int getStrength() {
        return strength;
    }

    public void increaseStrength() {
        this.strength *= ModelParameters.MUTATOR_MUTATION_EFFECT;
    }

    public void decreaseStrength() {
        this.strength *= ModelParameters.MUTATOR_MUTATION_EFFECT;
    }
}

