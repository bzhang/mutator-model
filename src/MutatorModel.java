/**
 * @author Bingjun
 */

import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MutatorModel {

    public static void main(String[] args) {

        String propertiesFileName = args.length > 0 ? args[0] : "MutatorModel.properties";
        ModelParameters.setPropertiesFileName(propertiesFileName);

        String resultFilename = prepareOutputDirectory();

        for (int nExperiment = 0; nExperiment < ModelParameters.getInt("N_EXPERIMENT"); nExperiment++) {

            String output = "Generation\tFitnessMean\tFitnessSD\tMutatorStrengthMean\tMutatorStrengthSD" +
                            "\tnDeleMutMean\tnDeleMutSD\tnBeneMutMean\tnBeneMutSD\n";



            // Founder population
            System.out.println("Output file: " + resultFilename + "\nFounder population creating...");
            Population population = new Population(ModelParameters.getInt("POPULATION_SIZE"));
            output += outputPopulationStat(1, population);
            writeFile(resultFilename, output);

            System.out.println("Founder population created.");

            for (int i = 2; i <= ModelParameters.getInt("N_GENERATIONS"); i++) {
                // Create the next generation
                population = new Population(population, i);
                output = outputPopulationStat(i, population);
                writeFile(resultFilename, output);
                System.out.println("Generation " + i);
            }

        }
    }

    private static String outputPopulationStat(int i, Population population) {
        float[] fitnessArray = population.getFitnessArray();
        int[] mutatorStrengthArray = population.getMutatorStrengthArray();
        int[] nDeleMutArray = population.getNDeleMutArray();
        int[] nBeneMutArray = population.getBeneMutArray();


        return i + "\t" + Util.mean(fitnessArray)
                 + "\t" + Util.standardDeviation(fitnessArray)
                 + "\t" + Util.mean(mutatorStrengthArray)
                 + "\t" + Util.standardDeviation(mutatorStrengthArray)
                 + "\t" + Util.mean(nDeleMutArray)
                 + "\t" + Util.standardDeviation(nDeleMutArray)
                 + "\t" + Util.mean(nBeneMutArray)
                 + "\t" + Util.standardDeviation(nBeneMutArray)
                 + "\n";
    }

    public static void writeFile(String outputFileName, String output) {
        try {
            FileWriter fileWriter = new FileWriter(outputFileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(output);
            bufferedWriter.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static String prepareOutputDirectory() {
        String directoryName = ModelParameters.getDirectoryName();
        File outputDir = new File(directoryName);
        if (!outputDir.exists() && outputDir.mkdir()) {
            System.out.println("Directory: " + directoryName + " created.");
        }

        File propertiesFile  = new File(ModelParameters.getPropertiesFilename());
        File destinationFile = new File(directoryName + "/" + ModelParameters.getPropertiesFilename());
        if (!destinationFile.exists()) {
            try {
                FileUtils.copyFileToDirectory(propertiesFile, outputDir, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return directoryName + "/" + System.nanoTime() + ".txt";
    }
}
