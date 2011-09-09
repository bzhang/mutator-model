import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by Bingjun at 7/20/11 11:00 PM
 */

public class ModelParameters {
    public static String PROPERTIES_FILE_NAME = "MutatorModel.properties";
    private static final Properties properties = new Properties();

    public static final int N_EXPERIMENT, N_GENERATIONS, POPULATION_SIZE;
    public static final int N_FITNESS_LOCI, N_MUTATOR_LOCI, N_RECOMBINATION_LOCI, GENOME_SIZE;
    public static final int MUTATOR_STRENGTH_MAX;
    public static final float MUTATOR_RATIO, RECOMBINATION_RATIO;
    public static final double BASE_LETHAL_MUTATION_RATE, BASE_DELETERIOUS_MUTATION_RATE, BASE_BENEFICIAL_MUTATION_RATE;

    public static final double MUTATOR_MUTATION_RATE;
    public static final double PROBABILITY_TO_MUTATOR;
    public static final int MUTATOR_MUTATION_EFFECT;

    public static final float BASE_FITNESS_EFFECT, DEFAULT_DELETERIOUS_EFFECT, DEFAULT_BENEFICIAL_EFFECT;
    public static final String DIRECTORY_NAME;

    static {
        try {
            properties.load(new FileInputStream(PROPERTIES_FILE_NAME));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        N_EXPERIMENT     = getInt("N_EXPERIMENT");
        N_GENERATIONS    = getInt("N_GENERATIONS");
        POPULATION_SIZE  = getInt("POPULATION_SIZE");

        N_FITNESS_LOCI       = getInt("N_FITNESS_LOCI");
        N_MUTATOR_LOCI       = getInt("N_MUTATOR_LOCI");
        N_RECOMBINATION_LOCI = getInt("N_RECOMBINATION_LOCI");
        GENOME_SIZE          = N_FITNESS_LOCI + N_MUTATOR_LOCI + N_RECOMBINATION_LOCI;

        MUTATOR_STRENGTH_MAX = getInt("MUTATOR_STRENGTH_MAX");
        MUTATOR_RATIO        = getFloat("MUTATOR_RATIO");
        RECOMBINATION_RATIO  = getFloat("RECOMBINATION_RATIO");

        BASE_LETHAL_MUTATION_RATE      = getDouble("BASE_LETHAL_MUTATION_RATE");
        BASE_DELETERIOUS_MUTATION_RATE = getDouble("BASE_DELETERIOUS_MUTATION_RATE");
        BASE_BENEFICIAL_MUTATION_RATE  = getDouble("BASE_BENEFICIAL_MUTATION_RATE");

        MUTATOR_MUTATION_RATE   = getDouble("MUTATOR_MUTATION_RATE");
        PROBABILITY_TO_MUTATOR  = getDouble("PROBABILITY_TO_MUTATOR");
        MUTATOR_MUTATION_EFFECT = getInt("MUTATOR_MUTATION_EFFECT");

        BASE_FITNESS_EFFECT        = getFloat("BASE_FITNESS_EFFECT");
        DEFAULT_DELETERIOUS_EFFECT = getFloat("DEFAULT_DELETERIOUS_EFFECT");
        DEFAULT_BENEFICIAL_EFFECT  = getFloat("DEFAULT_BENEFICIAL_EFFECT");

        DIRECTORY_NAME = "out/MutCount"
                + "_M" + MUTATOR_RATIO
                + "_R" + RECOMBINATION_RATIO
                + "_G" + N_GENERATIONS
                + "_N" + POPULATION_SIZE
                + "_BeneMR" + BASE_BENEFICIAL_MUTATION_RATE
                + "_DeleMR" + BASE_DELETERIOUS_MUTATION_RATE
                + "_BeneE"  + DEFAULT_BENEFICIAL_EFFECT
                + "_DeleE"  + DEFAULT_DELETERIOUS_EFFECT
                + "_MutStr" + MUTATOR_STRENGTH_MAX
                + "_MutaMR" + MUTATOR_MUTATION_RATE
                + "_Prob2M" + PROBABILITY_TO_MUTATOR
                + "_MutaE"  + MUTATOR_MUTATION_EFFECT
                ;
    }

    private static int getInt(String propertyName) {
        return Integer.parseInt(properties.getProperty(propertyName));
    }

    private static float getFloat(String propertyName) {
        return Float.parseFloat(properties.getProperty(propertyName));
    }

    private static double getDouble(String propertyName) {
        return Double.parseDouble(properties.getProperty(propertyName));
    }

}
