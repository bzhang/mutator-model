import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * @author Bingjun
 */

public class MutatorModel {

    public static final String VERSION = "0.1";

    private static final int N_GENERATIONS = 2000;
    private static final int POPULATION_SIZE = 500;
    private static final String OUTPUT_FILE_NAME = "Mutator_M0_R0_G2000_N500.txt";

    public static void main(String[] args) {

        String output = "FitnessMean\tFitnessSD\tMutatorStrengthMean\tMutatorStrengthSD\n";

        // Founder population
        System.out.println("Output file: " + OUTPUT_FILE_NAME + "\nFounder population creating...");
        Population population = new Population(POPULATION_SIZE);
        output += outputPopulationStat(population);

        System.out.println("Founder population created.");

        for (int i = 0; i < N_GENERATIONS; i++ ) {
            // Create the next generation
            population = new Population(population);
            output += outputPopulationStat(population);
            System.out.println("Generation " + i);
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
