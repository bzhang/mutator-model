/**
 * @author Bingjun
 * 5/16/11 12:32 AM
 */

import java.util.ArrayList;
import java.util.Random;

public class Population {

    // TODO: read parameters from properties file
    private static final int N_FITNESS_LOCI = 100;
    private static final int N_MUTATOR_LOCI = 1;
    private static final int N_RECOMBINATION_LOCI = 1;
    private static final int GENOME_SIZE = N_MUTATOR_LOCI + N_FITNESS_LOCI + N_RECOMBINATION_LOCI;
    private static final int MUTATOR_STRENGTH_MAX = 1;
    private static final float MUTATOR_RATIO = 0.0f;
    private static final float RECOMBINATION_RATIO = 0.0f;

    private ArrayList<Individual> individuals;
    private LociPattern lociPattern;
    private Random random = new Random(System.nanoTime());

    public Population(int nIndividuals) {
        // Create the founder population
        lociPattern = new LociPattern(N_FITNESS_LOCI, N_MUTATOR_LOCI, N_RECOMBINATION_LOCI);
        individuals = new ArrayList<Individual>();

        for (int i = 0; i < nIndividuals; i++) {
            Individual individual = new Individual(lociPattern);
            individuals.add(individual);

            for (int location = 0; location < GENOME_SIZE; location++) {
                if (lociPattern.getLocusType(location) == LociPattern.LocusType.Fitness) {
                    individual.setFitnessLocus(location);
                }
                else if (lociPattern.getLocusType(location) == LociPattern.LocusType.Mutator) {
                    individual.setMutatorLocus(location, getRandomMutatorStrength());
                }
                else {
                    individual.setRecombinationLocus(location, getRandomRecombinationStrength());
                }
            }
        }
    }

    public Population(Population parent) {
        // Create the next generation
        lociPattern = parent.lociPattern;
        individuals = new ArrayList<Individual>();

        while (getSize() < parent.getSize()) {
            IndividualPair offspringPair = reproduceOffspringPair();
            offspringPair.mutate();
            addIndividualPair(offspringPair, parent.getSize());
        }
    }

    private void addIndividualPair(IndividualPair offspringPair, int parentSize) {
        addIndividual(offspringPair.getIndividualA(), parentSize);
        addIndividual(offspringPair.getIndividualB(), parentSize);
    }

    public IndividualPair reproduceOffspringPair() {
        IndividualPair parentPair = getRandomIndividualPair();
        return parentPair.reproduce();
    }

    private void addIndividual(Individual individual, int parentSize) {
        if (getSize() < parentSize && individual.isAlive()) {
            individuals.add(individual);
        }
    }

    private IndividualPair getRandomIndividualPair() {
        float[] randomWeights = getFitnessArray();
        WeightedRandomGenerator wrg = new WeightedRandomGenerator(randomWeights);
        int indexA = wrg.nextInt();
        int indexB = wrg.nextInt();
        while (indexA == indexB) {
            indexB = wrg.nextInt();
        }
        Individual individualA = getIndividualArray().get(indexA);
        Individual individualB = getIndividualArray().get(indexB);
        return new IndividualPair(individualA, individualB);
    }

    private int getRandomMutatorStrength() {
        int strength = 1;
        // Generate mutator locus, strength ranging from [2, MUTATOR_STRENGTH_MAX]
        if (random.nextFloat() < MUTATOR_RATIO) {
            strength = random.nextInt(MUTATOR_STRENGTH_MAX - 1) + 2;
        }
        return strength;
    }

    private float getRandomRecombinationStrength() {
        float strength = 0;
        // Generate recombination locus (sexual), strength ranging from (0.0, 1.0]
        if (random.nextFloat() < RECOMBINATION_RATIO) {
            strength = random.nextFloat();
        }
        return strength;
    }

    public ArrayList<Individual> getIndividualArray() {
        return individuals;
    }

    public int getSize() {
        return individuals.size();
    }

    private float[] getFitnessArray() {
        float[] fitnessArray = new float[getSize()];
        int i = 0;
        for (Individual individual : getIndividualArray()) {
            fitnessArray[i++] = individual.getFitness();
        }
        return fitnessArray;
    }
}
