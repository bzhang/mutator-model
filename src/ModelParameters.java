/**
 * Created by Bingjun at 7/20/11 11:00 PM
 */

public class ModelParameters {

    // TODO: read parameters from properties file
    public static final int nFitnessLoci = 100;
    public static final int nMutatorLoci = 1;
    public static final int nRecombinationLoci = 1;
    public static final int genomeSize = nFitnessLoci + nMutatorLoci + nRecombinationLoci;

    public static final int mutatorStrengthMax = 1000;
    public static final float mutatorRatio = 0.5f;
    public static final float recombinationRatio = 0.5f;

    public static final double baseLethalMutationRate = 1e-5;
    public static final double baseDeleteriousMutationRate = 1e-4;
    public static final double baseBeneficialMutationRate = 1e-8;

    public static final float baseFitnessEffect = 1f;
    public static final float defaultDeleteriousEffect = 0.99f;
    public static final float defaultBeneficialEffect = 1.01f;

}
