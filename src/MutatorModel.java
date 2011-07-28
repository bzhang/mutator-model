import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * @author Bingjun
 */

public class MutatorModel {

    public static final String VERSION = "0.1";

    public static void main(String[] args) {

        String output = "Generation\tFitnessMean\tFitnessSD\tMutatorStrengthMean\tMutatorStrengthSD\n";

        // Founder population
        System.out.println("Output file: " + getFilename() + "\nFounder population creating...");
        Population population = new Population(ModelParameters.POPULATION_SIZE);
        output += outputPopulationStat(1, population);

        System.out.println("Founder population created.");

        for (int i = 2; i <= ModelParameters.N_GENERATIONS; i++ ) {
            // Create the next generation
            population = new Population(population);
            output += outputPopulationStat(i, population);
            System.out.println("Generation " + i);
        }

        writeFile(getFilename(), output);
    }

    private static String outputPopulationStat(int i, Population population) {
        float[] fitnessArray = population.getFitnessArray();
        int[] mutatorStrengthArray = population.getMutatorStrengthArray();
        return i + "\t" + Util.mean(fitnessArray) + "\t" + Util.standardDeviation(fitnessArray) + "\t"
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

    private static String getFilename() {
        return "out/Mutator_M" + ModelParameters.MUTATOR_RATIO + "_R" + ModelParameters.RECOMBINATION_RATIO
                + "_G" + ModelParameters.N_GENERATIONS + "_N" + ModelParameters.POPULATION_SIZE + ".txt";
    }
}
