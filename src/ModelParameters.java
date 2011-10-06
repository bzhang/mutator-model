/**
 * Created by Bingjun at 7/20/11 11:00 PM
 */

import java.io.FileInputStream;
import java.util.Properties;

public class ModelParameters {

    private static String propertiesFilename = "MutatorModel.properties";
    private static Properties properties = new Properties();
    private static boolean initialized = false;

    public static void setPropertiesFileName(String filename) {
        propertiesFilename = filename;
        initialized = loadPropertiesFile();
    }

    public static String getPropertiesFilename() {
        return propertiesFilename;
    }

    private static boolean loadPropertiesFile() {
        try {
            properties.load(new FileInputStream(propertiesFilename));
            return true;
        } catch (Exception e) {
            System.err.println("Warning: " + e.getMessage());
            return false;
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }

    private static String getProperty(String propertyName) throws RuntimeException {
        if (initialized) {
            return properties.getProperty(propertyName);
        }
        System.err.println("ERROR: Model parameters not initialized.");
        throw new RuntimeException();
    }

    public static int getInt(String propertyName) {
        return Integer.parseInt(getProperty(propertyName));
    }

    public static float getFloat(String propertyName) {
        return Float.parseFloat(getProperty(propertyName));
    }

    public static double getDouble(String propertyName) {
        return Double.parseDouble(getProperty(propertyName));
    }

    public static int getGenomeSize() {
        return getInt("N_FITNESS_LOCI") + getInt("N_MUTATOR_LOCI") + getInt("N_RECOMBINATION_LOCI");
    }

    public static String getDirectoryName() {
        return "out/MutCount"
                + "_M" + getFloat("MUTATOR_RATIO")
                + "_R" + getFloat("RECOMBINATION_RATIO")
                + "_G" + getInt("N_GENERATIONS")
                + "_N" + getInt("POPULATION_SIZE")
                + "_BeneMR" + getDouble("BASE_BENEFICIAL_MUTATION_RATE")
                + "_DeleMR" + getDouble("BASE_DELETERIOUS_MUTATION_RATE")
                + "_BeneE"  + getFloat("DEFAULT_BENEFICIAL_EFFECT")
                + "_DeleE"  + getFloat("DEFAULT_DELETERIOUS_EFFECT")
                + "_MutStr" + getInt("MUTATOR_STRENGTH_MAX")
                + "_InitMutaMR" + getDouble("INITIAL_MUTATOR_MUTATION_RATE")
                + "_EvolMutaMR" + getDouble("EVOLVING_MUTATOR_MUTATION_RATE")
                + "_StartEvol"  + getInt("START_EVOLVING_GENERATION")
                + "_Prob2M" + getDouble("PROBABILITY_TO_MUTATOR")
                + "_MutaE"  + getInt("MUTATOR_MUTATION_EFFECT");
    }

}
