import java.util.Random;

/**
 * @author Bingjun
 */

public class MutatorModel {

    public static final String VERSION = "0.1";

    private static final int N_GENERATIONS = 10;
    private static final int POPULATION_SIZE = 10;
    private static final int N_FITNESS_LOCI = 1000;
    private static final int N_MUTATOR_LOCI = 1;
    private static final int N_RECOMBINATION_LOCI = 1;
    private static final int GENOME_SIZE = N_FITNESS_LOCI + N_MUTATOR_LOCI + N_RECOMBINATION_LOCI;
    private static final int MUTATOR_STRENGTH_MAX = 1000;
    private static final float MUTATOR_RATIO = 0.5f;

    private static Random random = new Random(System.nanoTime());

    public static void main(String[] args) {

        Population population = new Population(POPULATION_SIZE);
        initFounderPopulation(population);

        for (int i = 0; i < N_GENERATIONS; i++ ) {
            Population nextGeneration = population.createNextGeneration();
            population = nextGeneration;
        }

    }

    private static void initFounderPopulation(Population population) {
        // TODO: modify the mutator initiation
        // !! 50/50 mutator(mutatorLocusStrength = 2-1000)/nonmutator(mutatorLocusStrength = 1)
        int mutatorLocusStrength = 1;
        int mutatorLocusPosition = random.nextInt(GENOME_SIZE);

        if (random.nextFloat() > 0.5f) {
            mutatorLocusStrength = random.nextInt(MUTATOR_STRENGTH_MAX) + 1;
            population.addMutatorLoci(mutatorLocusPosition, mutatorLocusStrength);
        }
    }

    private boolean
}
