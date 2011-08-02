/**
 * @author Bingjun
 * Created by bingjun at 5/16/11 9:42 AM
*/

public class Individual implements Cloneable{

    private Locus[] loci;
    private LociPattern lociPattern;
    private boolean alive;

    public Individual(LociPattern pattern) {
        lociPattern = pattern;
        loci = new Locus[lociPattern.getGenomeSize()];
        alive = true;
    }

    public Individual(Individual individual) {
        this(individual.getLociPattern());
        for (int i = 0; i < individual.getGenomeSize(); i++) {
            Object clonedLocus = null;
            try {
                clonedLocus = individual.getLocus(i).clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            if (getLociPattern().getLocusType(i) == LociPattern.LocusType.Fitness) {
                loci[i] = (FitnessLocus) clonedLocus;
            } else if (getLociPattern().getLocusType(i) == LociPattern.LocusType.Mutator) {
                loci[i] = (MutatorLocus) clonedLocus;
            } else if (getLociPattern().getLocusType(i) == LociPattern.LocusType.Recombination) {
                loci[i] = (RecombinationLocus) clonedLocus;
            }
        }
    }

    public void mutate() {
        lethalMutate();
        if (isAlive()) {
            deleteriousMutate();
            beneficialMutate();
        }
        if (getFitness() <= 0) {
            die();
        }
    }

    private void lethalMutate() {
        double mutationRate = ModelParameters.BASE_LETHAL_MUTATION_RATE * getMutatorStrength();
        if (Rand.getDouble() < mutationRate) {
            die();
        }
    }

    public void die() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    private void deleteriousMutate() {
        double mutationRate = ModelParameters.BASE_DELETERIOUS_MUTATION_RATE * getMutatorStrength();
        if (Rand.getDouble() < mutationRate) {
            FitnessLocus locus = getRandomFitnessLocus();
            locus.addFitnessEffect(ModelParameters.DEFAULT_DELETERIOUS_EFFECT);
        }
    }

    private void beneficialMutate() {
        double mutationRate = ModelParameters.BASE_BENEFICIAL_MUTATION_RATE * getMutatorStrength();
        if (Rand.getDouble() < mutationRate) {
            FitnessLocus locus = getRandomFitnessLocus();
            locus.addFitnessEffect(ModelParameters.DEFAULT_BENEFICIAL_EFFECT);
        }
    }

    private FitnessLocus getRandomFitnessLocus() {
        int position = lociPattern.getRandomFitnessPosition();
        return (FitnessLocus) getLocus(position);
    }

    public void setFitnessLocus(int position, float fitnessEffect) {
        FitnessLocus fitnessLocus = new FitnessLocus(fitnessEffect);
        loci[position] = fitnessLocus;
    }

    public void setFitnessLocus(int position) {
        setFitnessLocus(position, ModelParameters.BASE_FITNESS_EFFECT);
    }

    public void setMutatorLocus(int position, int strength) {
        MutatorLocus mutatorLocus = new MutatorLocus(strength);
        setLocus(position, mutatorLocus);
    }

    public void setRecombinationLocus(int position, float strength) {
        RecombinationLocus recombinationLocus = new RecombinationLocus(strength);
        setLocus(position, recombinationLocus);
    }

    public LociPattern getLociPattern() {
        return lociPattern;
    }

    public Locus getLocus(int position) {
        return loci[position];
    }

    public void setLocus(int position, Locus locus) {
        loci[position] = locus;
    }

    public int getGenomeSize() {
        return loci.length;
    }

    public float getFitness() {
        float fitness = 1;
        for (int i = 0; i < getGenomeSize(); i++) {
            if (lociPattern.getLocusType(i) == LociPattern.LocusType.Fitness) {
                FitnessLocus locus = (FitnessLocus)getLocus(i);
                fitness *= locus.getFitnessEffect();
            }
        }
        return fitness;
    }

    public int getMutatorStrength() {
        // TODO: multiple all mutator strength values
        int mutatorLocusPosition = lociPattern.getMutatorLociPositions()[0];
        return ((MutatorLocus) getLocus(mutatorLocusPosition)).getStrength(); // refactor
    }

    public float getRecombinationStrength() {
        // TODO: multiple all recombination strength values
        int recombinationLocusPosition = lociPattern.getRecombinationLociPositions()[0];
        return ((RecombinationLocus) getLocus(recombinationLocusPosition)).getStrength(); // refactor
    }

}
