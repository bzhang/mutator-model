/**
 * Created by Bingjun at 7/20/11 11:00 PM
 */

public class ModelParameters {

    // TODO: read parameters from properties file

    public static final int N_GENERATIONS = 1000;
    public static final int POPULATION_SIZE = 100;

    public static final int N_FITNESS_LOCI = 100;
    public static final int N_MUTATOR_LOCI = 1;
    public static final int N_RECOMBINATION_LOCI = 1;
    public static final int GENOME_SIZE = N_FITNESS_LOCI + N_MUTATOR_LOCI + N_RECOMBINATION_LOCI;

    public static final int MUTATOR_STRENGTH_MAX = 1000;
    public static final float MUTATOR_RATIO = 0.0f;
    public static final float RECOMBINATION_RATIO = 0.0f;

    public static final double BASE_LETHAL_MUTATION_RATE = 1e-5 * 0;
    public static final double BASE_DELETERIOUS_MUTATION_RATE = 1e-4 * 0;
    public static final double BASE_BENEFICIAL_MUTATION_RATE = 1e-8 * 0;

    public static final float BASE_FITNESS_EFFECT = 1f;
    public static final float DEFAULT_DELETERIOUS_EFFECT = 0.98f;
    public static final float DEFAULT_BENEFICIAL_EFFECT = 1.02f;

}
