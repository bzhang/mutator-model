/**
 * @author Bingjun
 * Created by bingjun at 5/16/11 9:42 AM
*/

public class Individual {

    // TODO: read parameters from properties file
    static final int NUM_OF_MUTATOR_LOCI = 1;
    static final int NUM_OF_FITNESS_LOCI = 1000;
    static final int NUM_OF_RECOMBINATION_LOCI = 1;

    private int genomeSize;
    private float fitness;

    private Locus[] loci;

    public Individual() {
        fitness = 1.5f;
        genomeSize = NUM_OF_MUTATOR_LOCI + NUM_OF_FITNESS_LOCI + NUM_OF_RECOMBINATION_LOCI;
        loci = new Locus[genomeSize];

        // TODO: initiate individual
        addMutatorLocus(0, 10);
    }

    public void addMutatorLocus(int position, float strength) {
        MutatorLocus mutator = new MutatorLocus(strength);
    }

    public float getFitness() {
        return fitness;
    }

}
