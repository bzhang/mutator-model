import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Bingjun
 */

public class MutatorModel {

    public static void main(String[] args) {
        if (args.length > 0) {
            String PROPERTIES_FILE_NAME = args[0];
            ModelParameters.setPropertiesFileName(PROPERTIES_FILE_NAME);
        }

        String resultDir = createDirectory();
        final String resultFileName = getFilename(resultDir);

        File srcFile  = new File(ModelParameters.PROPERTIES_FILE_NAME);
        File destDir  = new File(ModelParameters.DIRECTORY_NAME);
        File destFile = new File(ModelParameters.DIRECTORY_NAME + "/" + ModelParameters.PROPERTIES_FILE_NAME);

//        public String PROPERTIES_FILE_NAME = args[0];

        if (!destFile.exists()) {
            try {
                FileUtils.copyFileToDirectory(srcFile, destDir, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int nExperiment = 0; nExperiment < ModelParameters.N_EXPERIMENT; nExperiment++) {

            String output = "Generation\tFitnessMean\tFitnessSD\tMutatorStrengthMean\tMutatorStrengthSD\n";

            // Founder population
            System.out.println("Output file: " + resultFileName + "\nFounder population creating...");
            Population population = new Population(ModelParameters.POPULATION_SIZE);
            output += outputPopulationStat(1, population);

            System.out.println("Founder population created.");

            for (int i = 2; i <= ModelParameters.N_GENERATIONS; i++) {
                // Create the next generation
                population = new Population(population);
                output += outputPopulationStat(i, population);
                System.out.println("Generation " + i);
            }

            writeFile(resultFileName, output);
        }
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

    private static String getFilename(String dir) {
        return dir + "/" + System.nanoTime() + ".txt";
    }

    private static String createDirectory() {
        String dir = ModelParameters.DIRECTORY_NAME;
        File outputDir = new File(dir);

        if (!outputDir.exists()) {
            if (outputDir.mkdir()) {
                System.out.println("Directory: " + dir + " created.");
            }
        }
        return dir;
    }
}
