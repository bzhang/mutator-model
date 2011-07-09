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


    private static int mutatorLocusStrength;
    private static int mutatorLocusPosition;

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
        // !! In founder population,
        // !! 50/50 mutator(mutatorLocusStrength = 2-1000)/nonmutator(mutatorLocusStrength = 1)
        mutatorLocusStrength = getRandomInt(MUTATOR_STRENGTH_MAX) + 1;
        mutatorLocusPosition = getRandomInt(GENOME_SIZE);
        population.addMutatorLocus(mutatorLocusPosition, mutatorLocusStrength);
    }

    private static int getRandomInt(int length) {
        Random random = new Random(System.nanoTime());
        return random.nextInt(length);
    }


//    Individual[] individuals = population.getIndividuals();
//
//    Individual indA = individuals[0];
//
//    float fitness = indA.getFitness();
//
//    Individual indB = individuals[1];
//
//    Population nextGeneration = population.createNextGeneration();
//
//    float r1 = indA.recombinationModifier;
//
//    float r2 = indB.recombinationModifier;
//
//    Individual offA = population.nextGeneration.individuals[0];
//
//    Individual offB = population.nextGeneration.individuals[0];
//
//    int m1 = offA.mutationModifier;
//
//    int m2 = offB.mutationModifier;
//
//    float mr1 = offA.getMutationRate();
//    float mr2 = offB.getMutationRate();
//
//    offA.mutate();
//    offB.mutate();
//
//    population.size;




}
