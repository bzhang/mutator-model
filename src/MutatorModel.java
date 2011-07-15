/**
 * @author Bingjun
 */

public class MutatorModel {

    public static final String VERSION = "0.1";

    private static final int N_GENERATIONS = 10;
    private static final int POPULATION_SIZE = 10;

    public static void main(String[] args) {
        // Founder population
        Population population = new Population(POPULATION_SIZE);

        for (int i = 0; i < N_GENERATIONS; i++ ) {
            population = new Population(population);
        }

    }

}
