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
    private double[] deleFitnessEffectArray;
    private double[] beneFitnessEffectArray;

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
        this.deleFitnessEffectArray = deleFitnessEffectArray;
        this.beneFitnessEffectArray = beneFitnessEffectArray;
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

    public double[] getBeneFitnessEffectArray() {
        return beneFitnessEffectArray;
    }

    public double[] getDeleFitnessEffectArray() {
        return deleFitnessEffectArray;
    }
}
