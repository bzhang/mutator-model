/**
 * @author Bingjun Zhang
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
        this.strength *= ModelParameters.getFloat("MUTATOR_MUTATION_EFFECT");
    }

    public void decreaseStrength() {
        this.strength /= ModelParameters.getFloat("MUTATOR_MUTATION_EFFECT");
    }
}

