import org.jfree.data.xy.XYZDataset;

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
            MetaPopulation metaPopulation = new MetaPopulation();
            popFileOutput += Util.outputMetaPopulationStat(1, metaPopulation);
            Util.writeFile(popFilename, popFileOutput);
            XYZDataset xyzDatasetFirstGen = metaPopulation.xyzDataset;
            Util.scatterPlot(xyzDatasetFirstGen, 1, resultFileNamePrefix);

            System.out.println("Founder population created.");

            for (int i = 2; i <= ModelParameters.getInt("N_GENERATIONS"); i++) {
                // Create the next generation
//                Long genStart = System.currentTimeMillis();
                metaPopulation = new MetaPopulation(metaPopulation, i);
                if (i % ModelParameters.getInt("POP_OUTPUT_PERIOD") == 0) {
                    popFileOutput = Util.outputMetaPopulationStat(i, metaPopulation);
                    Util.writeFile(popFilename, popFileOutput);
                    XYZDataset xyzDataset = metaPopulation.xyzDataset;
                    Util.scatterPlot(xyzDataset, i, resultFileNamePrefix);
                }
//                if (i % ModelParameters.getInt("MUT_STRUCTURE_OUTPUT_PERIOD") == 0) {
//                    mutStructureFileOutput = outputMutStructure(i, population);
//                    Util.writeFile(mutStructureFilename, mutStructureFileOutput);
//                }
                System.out.println("Generation " + i);

//                int reminderGen = (int) ((System.currentTimeMillis() - genStart) % (24L * 3600 * 1000));
//                Float secondsElapsedGen = (float) reminderGen / 1000;
//                System.out.println("Seconds elapsed = " + secondsElapsedGen);

                System.out.println("# of Recombinations = " + ModelParameters.getNRecombination());
            }

            int reminder = (int) ((System.currentTimeMillis() - start) % (24L * 3600 * 1000));
            Float secondsElapsed = (float) reminder / 1000;
            System.out.println("Seconds elapsed = " + secondsElapsed);
        }
    }
}
