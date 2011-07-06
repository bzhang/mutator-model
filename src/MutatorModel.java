/**
 * @author Bingjun
 */

public class MutatorModel {

    public static final String VERSION = "0.1";

    public static void main(String[] args) {

        Population population;
        int nGenerations = 1;
        int populationSize = 2;

        population = new Population(populationSize);

        for (int i = 0; i < nGenerations; i++ ) {
            Population nextGeneration = population.createNextGeneration();
            population = nextGeneration;
        }

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
