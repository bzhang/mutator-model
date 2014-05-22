/**
 * @author Bingjun Zhang
 */
public class GroupReturn {
    private int index;
    private IndividualInSpace individualInSpace;
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
    private int row;
    private int column;
    private Individual individual;

    public GroupReturn(Individual individual, int row, int column) {
        this.individual = individual;
        this.row = row;
        this.column = column;
    }

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

    public GroupReturn(double meanDeleFitnessEffect, double meanBeneFitnessEffect, int nDeleteriousMutations, int nBeneficialMutations) {
//        this.fitness = fitness;
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

    public GroupReturn(IndividualInSpace individualInSpace, int index) {
        this.individualInSpace = individualInSpace;
        this.index = index;

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

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Individual getIndividual() {
        return individual;
    }

    public int getIndex() {
        return index;
    }

    public IndividualInSpace getIndividualInSpace() {
        return individualInSpace;
    }
}
