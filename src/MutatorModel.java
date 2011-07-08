import java.util.Random;

/**
 * @author Bingjun
 */

public class MutatorModel {

    public static final String VERSION = "0.1";

    private static int nGenerations = 10;

    public static void main(String[] args) {

        int populationSize = 10;
        Population population = new Population(populationSize);

        // TODO: mutatorLocusStrentgh ranges from 1 to 1000
        int mutatorLocusStrength = getRandomInt(1000);
        // populationSize - 1?
        int mutatorLocusPosition = getRandomInt(populationSize);
        population.addMutatorLocus(mutatorLocusPosition, mutatorLocusStrength);

        for (int i = 0; i < nGenerations; i++ ) {
            Population nextGeneration = population.createNextGeneration();
            population = nextGeneration;
        }

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
