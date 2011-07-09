/**
 * @author Bingjun
 * Created by bingjun at 5/16/11 9:42 AM
*/

public class Individual {

    // TODO: read parameters from properties file

    private int genomeSize;
    private float fitness;

    private Locus[] loci;

    public Individual() {
        fitness = 1.5f;
        genomeSize = NUM_OF_MUTATOR_LOCI + NUM_OF_FITNESS_LOCI + NUM_OF_RECOMBINATION_LOCI;
        loci = new Locus[genomeSize];
    }

    public void addMutatorLocus(int position, int strength) {
        MutatorLocus mutator = new MutatorLocus(strength);
    }

    public float getFitness() {
        return fitness;
    }

}
