/**
 * @author Bingjun Zhang
 */

public class MutatorLocus extends Locus {

    private double strength;
    private double mutatorEffect;

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
        double u = Rand.getDouble();
        if (u <= ModelParameters.getDouble("PROBABILITY_TO_MUTCLASS_1")) {
            mutatorEffect = ModelParameters.getDouble("MUTATOR_MUTATION_EFFECT_1");
            this.strength *= Math.pow(Rand.getDouble(), -mutatorEffect);
        } else {
            mutatorEffect = ModelParameters.getDouble("MUTATOR_MUTATION_EFFECT_2");
            double randEffect = Rand.getGaussian() * ModelParameters.getDouble("MUTATOR_MUTATION_EFFECT_2_SD") + mutatorEffect;
            if (randEffect < 0) {
                randEffect = 0;
            }
            this.strength *= 1 + randEffect;
        }

    }

    public void decreaseStrength() {
        this.strength *= Math.pow(Rand.getDouble(), ModelParameters.getFloat("ANTIMUTATOR_MUTATION_EFFECT"));
    }
}

