/**
 * @author Bingjun Zhang
 */
public class MetaPopulationEvolution {
    public static void main(String[] args) {
        Long start = System.currentTimeMillis();

        String propertiesFileName = args.length > 0 ? args[0] : "MutatorModel.properties";
        ModelParameters.setPropertiesFileName(propertiesFileName);

        for (int nExperiment = 0; nExperiment < ModelParameters.getInt("N_EXPERIMENT"); nExperiment++) {
            String resultFileNamePrefix = Util.prepareOutputDirectory();
            String popFilename = resultFileNamePrefix + "_Pop.txt";
            String popFileOutput = "Generation\tFitnessMean\tMutatorStrengthMean\tnDeleMutMean\tnBeneMutMean\t" +
                    "MeanDeleFitnessEffect\tMeanBeneFitnessEffect\t" +
                    "FitnessSD\tMutatorStrengthSD\tnDeleMutSD\tnBeneMutSD\t" +
                    "deleFitnessEffectSD\tbeneFitnessEffectSD\n";

            // Create founder population
            System.out.println("Output file: " + popFilename + "\nFounder population creating...");
            Population population = new Population(ModelParameters.getInt("POPULATION_SIZE"));
            popFileOutput += Util.outputPopulationStat(1, population);

        }
    }
}
