/**
 * @author Bingjun Zhang
 */

public class MutatorLocus extends Locus {

    private double strength;

    public MutatorLocus(double strength) {
        this.strength = strength;
    }

    public double getStrength() {
        return strength;
    }

//    public double getMutatorEffect() {
////        return ((-ModelParameters.getFloat("MUTATOR_MUTATION_EFFECT")) * Math.log(1 - Rand.getFloat()));
//        return ((-ModelParameters.getFloat("MUTATOR_MUTATION_EFFECT")) * Rand.getFloat());
//    }

    public void increaseStrength() {
        this.strength *= Math.pow(Rand.getDouble(), -ModelParameters.getFloat("MUTATOR_MUTATION_EFFECT"));
    }

    public void decreaseStrength() {
        this.strength *= Math.pow(Rand.getDouble(), ModelParameters.getFloat("ANTIMUTATOR_MUTATION_EFFECT"));
    }
}

