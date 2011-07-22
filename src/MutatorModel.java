import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * @author Bingjun
 */

public class MutatorModel {

    public static final String VERSION = "0.1";

    private static final int N_GENERATIONS = 100;
    private static final int POPULATION_SIZE = 10;
    private static final String OUTPUT_FILE_NAME = "Mutator.txt";

    public static void main(String[] args) {

        String output = "FitnessMean\tFitnessSD\tMutatorStrengthMean\tMutatorStrengthSD\n";

        // Founder population
        Population population = new Population(POPULATION_SIZE);
        output += outputPopulationStat(population);

        for (int i = 0; i < N_GENERATIONS; i++ ) {
            population = new Population(population);
            output += outputPopulationStat(population);
        }

        writeFile(OUTPUT_FILE_NAME, output);
    }

    private static String outputPopulationStat(Population population) {
        float[] fitnessArray = population.getFitnessArray();
        int[] mutatorStrengthArray = population.getMutatorStrengthArray();
        return Util.mean(fitnessArray) + "\t" + Util.standardDeviation(fitnessArray) + "\t"
                + Util.mean(mutatorStrengthArray) + "\t" + Util.standardDeviation(mutatorStrengthArray) + "\n";
    }

    public static void writeFile(String outputFileName, String output) {
        try {
            FileWriter fileWriter = new FileWriter(outputFileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(output);
            bufferedWriter.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
