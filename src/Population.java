/**
 * @author Bingjun
 * 5/16/11 12:32 AM
 */

import java.util.ArrayList;
import java.util.Random;

public class Population {

    // TODO: read parameters from properties file
    private static final int N_FITNESS_LOCI = 1000;
    private static final int N_MUTATOR_LOCI = 1;
    private static final int N_RECOMBINATION_LOCI = 1;
    private static final int GENOME_SIZE = N_MUTATOR_LOCI + N_FITNESS_LOCI + N_RECOMBINATION_LOCI;
    private static final int MUTATOR_STRENGTH_MAX = 1000;
    private static final float MUTATOR_RATIO = 0.5f;

    private ArrayList<Individual> individuals;
    private LociPattern lociPattern;
    private Random random = new Random(System.nanoTime());

    public Population(int nIndividuals) {
        // Founder population
        lociPattern = new LociPattern(N_FITNESS_LOCI, N_MUTATOR_LOCI, N_RECOMBINATION_LOCI);
        individuals = new ArrayList<Individual>();

        for (int i = 0; i < nIndividuals; i++) {
            Individual individual = new Individual(GENOME_SIZE);
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

    private int getRandomMutatorStrength() {
        int strength = 1;
        if (random.nextFloat() < MUTATOR_RATIO) {
            strength = random.nextInt(MUTATOR_STRENGTH_MAX - 1) + 2;
        }
        return strength;
    }

    private float getRandomRecombinationStrength() {
        return random.nextFloat();
    }

    public Population(Population parent) {
        // Create next generation
        lociPattern = parent.lociPattern;
        individuals = new ArrayList<Individual>();

        while (getSize() < parent.getSize()) {
            IndividualPair offspringPair = reproduceOffspringPair();
            offspringPair.mutate();
            addIndividualPairs(offspringPair);
            // TODO: What if the population only needs one individual instead of two?
        }
    }

    public IndividualPair reproduceOffspringPair() {
        IndividualPair parentPair = getRandomIndividualPair();
        IndividualPair offspringPair = parentPair.reproduce();
        return offspringPair;
    }

    private void addIndividualPairs(IndividualPair offspringPair) {
        addIndividual(offspringPair.getIndividualA());
        addIndividual(offspringPair.getIndividualB());
    }

    private void addIndividual(Individual individual) {
        individuals.add(individual);
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
