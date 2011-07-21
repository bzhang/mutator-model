import java.net.SocketOptions;

/**
 * @author Bingjun
 */

public class MutatorModel {

    public static final String VERSION = "0.1";

    private static final int N_GENERATIONS = 100;
    private static final int POPULATION_SIZE = 10;

    public static void main(String[] args) {
        // Founder population
        Population population = new Population(POPULATION_SIZE);
        printPopulationStat(population);

        for (int i = 0; i < N_GENERATIONS; i++ ) {
            population = new Population(population);
            printPopulationStat(population);
        }
    }

    private static void printPopulationStat(Population population) {
        float[] fitnessArray = population.getFitnessArray();
        System.out.println(Util.mean(fitnessArray));
        System.out.println(Util.standardDeviation(fitnessArray));

        for (float f : fitnessArray) {
            System.out.print(f);
            System.out.print(", ");
        }
        System.out.println("");
        System.out.println("------------------------");
    }

}
