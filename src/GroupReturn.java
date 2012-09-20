/**
 * @author Bingjun Zhang
 */
public class GroupReturn {
    private FitnessLocus fitnessLocus;
    private int position;
    private int nDeleteriousMutations;
    private int nBeneficialMutations;
    private int[] nDeleMutArray;
    private int[] nBeneMutArray;
    private double fitness;
    private double meanDeleFitnessEffect;
    private double meanBeneFitnessEffect;
    private double[] fitnessArray;
    private double[] meanDeleFitnessEffectArray;
    private double[] meanBeneFitnessEffectArray;


    public GroupReturn(FitnessLocus fitnessLocus, int position) {
        this.fitnessLocus = fitnessLocus;
        this.position = position;
    }

    public GroupReturn(int nDeleteriousMutations, int nBeneficialMutations) {
        this.nDeleteriousMutations = nDeleteriousMutations;
        this.nBeneficialMutations = nBeneficialMutations;
    }

    public GroupReturn(int[] nDeleMutArray, int[] nBeneMutArray) {
        this.nDeleMutArray = nDeleMutArray;
        this.nBeneMutArray = nBeneMutArray;
    }

    public GroupReturn(double[] deleFitnessEffectArray, double[] beneFitnessEffectArray) {
    }

    public GroupReturn(double fitness, double meanDeleFitnessEffect, double meanBeneFitnessEffect, int nDeleteriousMutations, int nBeneficialMutations) {
        this.fitness = fitness;
        this.meanDeleFitnessEffect = meanDeleFitnessEffect;
        this.meanBeneFitnessEffect = meanBeneFitnessEffect;
        this.nDeleteriousMutations = nDeleteriousMutations;
        this.nBeneficialMutations = nBeneficialMutations;
    }

    public GroupReturn(double[] fitnessArray, double[] meanDeleFitnessEffectArray, double[] meanBeneFitnessEffectArray, int[] nDeleMutArray, int[] nBeneMutArray) {
        this.fitnessArray = fitnessArray;
        this.meanDeleFitnessEffectArray = meanDeleFitnessEffectArray;
        this.meanBeneFitnessEffectArray = meanBeneFitnessEffectArray;
        this.nDeleMutArray = nDeleMutArray;
        this.nBeneMutArray = nBeneMutArray;
    }

    public Locus getFitnessLocus() {
        return fitnessLocus;
    }

    public int getPosition() {
        return position;
    }

    public int getNDeleteriousMutations() {
        return nDeleteriousMutations;
    }

    public int getNBeneficialMutations() {
        return nBeneficialMutations;
    }

    public int[] getNDeleMutArray() {
        return nDeleMutArray;
    }

    public int[] getnBeneMutArray() {
        return nBeneMutArray;
    }

    public double getMeanBeneFitnessEffect() {
        return meanBeneFitnessEffect;
    }

    public double getMeanDeleFitnessEffect() {
        return meanDeleFitnessEffect;
    }

    public double getFitness() {
        return fitness;
    }

    public double[] getFitnessArray() {
        return fitnessArray;
    }

    public double[] getMeanDeleFitnessEffectArray() {
        return meanDeleFitnessEffectArray;
    }

    public double[] getMeanBeneFitnessEffectArray() {
        return meanBeneFitnessEffectArray;
    }
}
