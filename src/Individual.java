/**
 * @author Bingjun
 * Created by bingjun at 5/16/11 9:42 AM
*/

public class Individual {

    private Locus[] loci;

    public Individual(int genomeSize) {
        loci = new Locus[genomeSize];
    }

    public void setFitnessLocus(int position, float fitnessEffect) {
        FitnessLocus fitnessLocus = new FitnessLocus(fitnessEffect);
        loci[position] = fitnessLocus;
    }
    public void setMutatorLocus(int position, int strength) {
        MutatorLocus mutatorLocus = new MutatorLocus(strength);
        loci[position] = mutatorLocus;
    }
    public void setRecombinationLocus(int position, int strength) {
        RecombinationLocus recombinationLocus = new RecombinationLocus(strength);
        loci[position] = recombinationLocus;
    }

    public float getFitness() {
        return 1.5f;
    }

    public int getGenomeSize() {
        return loci.length;
    }

}
