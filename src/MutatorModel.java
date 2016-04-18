/**
 * @author Bingjun Zhang
 */

import java.util.HashMap;
import java.util.Map;

public class MutatorModel {

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();

        String propertiesFileName = args.length > 0 ? args[0] : "MutatorModel.properties";
        ModelParameters.setPropertiesFileName(propertiesFileName);
        System.out.println(propertiesFileName);

        for (int nExperiment = 0; nExperiment < ModelParameters.getInt("N_EXPERIMENT"); nExperiment++) {
            String resultFileNamePrefix = Util.prepareOutputDirectory();
            String popFilename = resultFileNamePrefix + "_Pop.txt";
            String mutMapFilename = resultFileNamePrefix + "_MutMap.txt";
            String mutStructureFilename = resultFileNamePrefix + "_MutStructure.txt";
            String mutStatFilename = resultFileNamePrefix + "_MutStat.txt";
            String popFileOutput = "Generation\tFitnessMean\tMeanU\tnDeleMutMean\tnBeneMutMean\t" +
                                   "RecombinationRateMean\t" +
                                   "MeanDeleFitnessEffect\tMeanBeneFitnessEffect\t" +
                                   "MeanTransformedValue\tvariance\tstd\tskewness\t" +
                                   "corFitnessLnU\tmeanLnU\tvarLnU\t" +
                                   "meanLnNeutral\tvarLnNeutral\t" +
                                   "meanCompleteNeutral\tvarCompleteNeutral\tmeanLnCompleteNeutral\tvarLnCompleteNeutral\t" +
                                   "FitnessSD\tNeutralMean\tNeutralSD\t" +
                                   "USD\tRecombinationRateSD\tnDeleMutSD\tnBeneMutSD\t" +
                                   "deleFitnessEffectSD\tbeneFitnessEffectSD\n";
            String mutMapFileOutput = "MutationID\tFitnessEffect\tMutatorStrength\tGeneration\tLocus\tParentFitnessZScore\tCorrelationBtwFitnessAndMutatorStrength\n";
            String mutStructureFileOutput = "Generation\tMutationID\tNIndividual\n";
            String mutStatFileOutput = "Generation\tlnU\n";

            // Write file header for mutStatFile
            Util.writeFile(mutStatFilename, mutStatFileOutput);

            // Write file headers for mutMapFile
            Util.writeFile(mutMapFilename, mutMapFileOutput);

            // Founder population
//            System.out.println("Output file: " + popFilename + "\nFounder population creating...");
            Population population = new Population(ModelParameters.getInt("POPULATION_SIZE"));
            popFileOutput += Util.outputPopulationStat(1, population, mutStatFilename);
            Util.writeFile(popFilename, popFileOutput);
            Util.writeFile(mutStructureFilename, mutStructureFileOutput);

//            System.out.println("Founder population created.");
//
//            int reminderFounder = (int) ((System.currentTimeMillis() - start) % (24L * 3600 * 1000));
//            Float secondsElapsedFounder = (float) reminderFounder / 1000;
//            System.out.println("Seconds elapsed for founder pop = " + secondsElapsedFounder);

            for (int i = 2; i <= ModelParameters.getInt("N_GENERATIONS"); i++) {
                // Create the next generation
//                Long genStart = System.currentTimeMillis();
                population = new Population(population, i, mutMapFilename);
                if (i % ModelParameters.getInt("POP_OUTPUT_PERIOD") == 0) {
                    popFileOutput = Util.outputPopulationStat(i, population, mutStatFilename);
                    Util.writeFile(popFilename, popFileOutput);
                }
                if (i % ModelParameters.getInt("MUT_STRUCTURE_OUTPUT_PERIOD") == 0) {
                    mutStructureFileOutput = outputMutStructure(i, population);
                    Util.writeFile(mutStructureFilename, mutStructureFileOutput);
                }
                System.out.println("Generation " + i);

//                int reminderGen = (int) ((System.currentTimeMillis() - genStart) % (24L * 3600 * 1000));
//                Float secondsElapsedGen = (float) reminderGen / 1000;
//                System.out.println("Seconds elapsed = " + secondsElapsedGen);

//                System.out.println("# of Recombinations = " + ModelParameters.getNRecombination());
            }

        }

        int reminder = (int) ((System.currentTimeMillis() - start) % (24L * 3600 * 1000));
        Float secondsElapsed = (float) reminder / 1000;
        System.out.println("Seconds elapsed = " + secondsElapsed);

    }

    private static String outputMutStructure(int i, Population population) {
        String output = i + "\t";
        Individual individual;
        long[] mutationIDsArray;
        Map<Long, Integer> counterMap = new HashMap<Long, Integer>();

        for (int j = 0; j < population.getSize(); j++) {
            individual = population.getIndividual(j);
            for (int k = 0; k < individual.getGenomeSize(); k++) {
                LociPattern lociPattern = individual.getLociPattern();
                if (lociPattern.getLocusType(k) == LociPattern.LocusType.Fitness) {
                    FitnessLocus locus = (FitnessLocus) individual.getLocus(k);
                    mutationIDsArray = locus.getMutationIDsArray();
                    for (long mutationID : mutationIDsArray) {
                        if (mutationID == 0) {
                            continue;
                        }
                        if (counterMap.containsKey(mutationID)) {
                            counterMap.put(mutationID, counterMap.get(mutationID) + 1);
                        } else {
                            counterMap.put(mutationID, 1);
                        }
                    }
                }

            }

        }

        for (Map.Entry<Long, Integer> longIntegerEntry : counterMap.entrySet()) {
            output += longIntegerEntry.getKey() + "\t" + longIntegerEntry.getValue() +"\t";
        }
        output += "\n";
        return output;
    }

}
