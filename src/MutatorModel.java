/**
 * @author Bingjun
 */

import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MutatorModel {

    public static void main(String[] args) {

        String propertiesFileName = args.length > 0 ? args[0] : "MutatorModel.properties";
        ModelParameters.setPropertiesFileName(propertiesFileName);

        String resultFileNamePrefix = prepareOutputDirectory();
        String popFilename = resultFileNamePrefix + "_Pop.txt";
        String mutMapFilename = resultFileNamePrefix + "_MutMap.txt";
        String mutStructureFilename = resultFileNamePrefix + "_MutStructure.txt";

        for (int nExperiment = 0; nExperiment < ModelParameters.getInt("N_EXPERIMENT"); nExperiment++) {

            String popFileOutput = "Generation\tFitnessMean\tFitnessSD\tMutatorStrengthMean\tMutatorStrengthSD" +
                            "\tnDeleMutMean\tnDeleMutSD\tnBeneMutMean\tnBeneMutSD\n";
            String mutMapFileOutput = "MutationID\tGeneration\tFitnessEffect\tMutatorStrength\tLocus\n";
            String mutStructureFileOutput = "Generation\tMutationID\tNIndividual\n";

            Map<Long, Map<String, Object>> mutationMap = new HashMap<Long, Map<String, Object>>();
            Map<String, Object> mutationProperties = new HashMap<String, Object>();

            // Founder population
            System.out.println("Output file: " + popFilename + "\nFounder population creating...");
            Population population = new Population(ModelParameters.getInt("POPULATION_SIZE"));
            popFileOutput += outputPopulationStat(1, population, mutationMap);
            writeFile(popFilename, popFileOutput);
            writeFile(mutStructureFilename, mutStructureFileOutput);

            System.out.println("Founder population created.");

            for (int i = 2; i <= ModelParameters.getInt("N_GENERATIONS"); i++) {
                // Create the next generation
                population = new Population(population, i, mutationMap, mutationProperties);
                popFileOutput = outputPopulationStat(i, population, mutationMap);
                writeFile(popFilename, popFileOutput);
                mutStructureFileOutput = outputMutStructure(i, population);
                writeFile(mutStructureFilename, mutStructureFileOutput);
                System.out.println("Generation " + i);
            }

            mutMapFileOutput += outputMutMap(mutationMap, mutMapFileOutput);
            writeFile(mutMapFilename, mutMapFileOutput);
        }
    }

    private static String outputMutStructure(int i, Population population) {
        String output = "";
        Map<Long, Integer> tempMap = new HashMap<Long, Integer>();
        Individual individual;
        ArrayList<Long> mutationIDsArray;

        for (int j = 0; j < population.getSize(); j++) {
            individual = population.getIndividual(j);
            for (int k = 0; k < individual.getGenomeSize(); k++) {
                LociPattern lociPattern = individual.getLociPattern();
                while (lociPattern.getLocusType(k) == LociPattern.LocusType.Fitness) {
                    FitnessLocus locus = (FitnessLocus) individual.getLocus(k);
                    mutationIDsArray = locus.getMutationIDsArray();
                    for (Long mutationID : mutationIDsArray) {
                        if (tempMap.containsKey(mutationID)) {
                            tempMap.put(mutationID, tempMap.get(mutationID) + 1);
                        } else {
                            tempMap.put(mutationID, 1);
                        }
                    }
                }

            }

        }

        for (Map.Entry<Long, Integer> longIntegerEntry : tempMap.entrySet()) {
            output += i + "\t" + longIntegerEntry.getKey() + "\t" + longIntegerEntry.getValue() +"\n";
        }
        return output;
    }

    private static String outputMutMap(Map<Long, Map<String, Object>> mutationMap, String output) {
        Map mutationProperties;
        for (Map.Entry<Long, Map<String, Object>> longMapEntry : mutationMap.entrySet()) {
            output += longMapEntry.getKey() + "\t";
            mutationProperties = longMapEntry.getValue();
//            String mutMapFileOutput = "MutationID\tGeneration\tFitnessEffect\tMutatorStrength\tLocus\n";
            output += mutationProperties.get("Generation") + "\t"
                    + mutationProperties.get("FitnessEffect") + "\t"
                    + mutationProperties.get("MutatorStrength") + "\t"
                    + mutationProperties.get("Locus") + "\n";
        }

        return output;
    }

    //TODO: output mutationMap.
    private static String outputPopulationStat(int i, Population population, Map mutationMap) {
        float[] fitnessArray = population.getFitnessArray(mutationMap);
        int[] mutatorStrengthArray = population.getMutatorStrengthArray();
        int[] nDeleMutArray = population.getNDeleMutArray(mutationMap);
        int[] nBeneMutArray = population.getNBeneMutArray(mutationMap);


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

        return directoryName + "/" + System.nanoTime();
    }
}
