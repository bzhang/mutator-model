import java.util.ArrayList;

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
    private float mutatorStrength;
    private int mutatorPosition;

    private ArrayList<MutatorLocus> mutatorLoci;

    // TODO: initiate individual
    MutatorLocus mutator = new MutatorLocus();

    public Individual() {
        fitness = 1.5f;
        mutatorStrength = mutator.getMutatorStrength();
        genomeSize = NUM_OF_MUTATOR_LOCI + NUM_OF_FITNESS_LOCI + NUM_OF_RECOMBINATION_LOCI;
    }

    public float getFitness() {
        return fitness;
    }

}
