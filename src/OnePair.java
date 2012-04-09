/**
 * @author Bingjun Zhang
 */
public class OnePair {
    private FitnessLocus fitnessLocus;
    private int position;
    private int nDeleteriousMutations;
    private int nBeneficialMutations;
    private int[] nDeleMutArray;
    private int[] nBeneMutArray;

    public OnePair(FitnessLocus fitnessLocus, int position) {
        this.fitnessLocus = fitnessLocus;
        this.position = position;
    }

    public OnePair(int nDeleteriousMutations, int nBeneficialMutations) {
        this.nDeleteriousMutations = nDeleteriousMutations;
        this.nBeneficialMutations = nBeneficialMutations;
    }

    public OnePair(int[] nDeleMutArray, int[] nBeneMutArray) {
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

}
