/**
 * @author Bingjun
 * Created by bingjun at 5/16/11 9:42 AM
*/
public class Individual {
    //TODO: read parameters from properties file
//    static final int NUM_OF_MUATOR_LOCI = 1;
//    static final int NUM_OF_FITNESS_LOCI = 1000;
//    static final int NUM_OF_RECOMBINATION_LOCI = 1;
//    int genomeSize = NUM_OF_MUATOR_LOCI + NUM_OF_FITNESS_LOCI + NUM_OF_RECOMBINATION_LOCI;

    // TODO: initiate individual.
    private float fitness;
    private float mutatorStrength;
    private int mutatorPosition;

    MutationRateModifier mutator = new MutationRateModifier();


    public Individual() {
        fitness = 1.5f;
        mutatorStrength = mutator.getMutatorStrength();

    }


    public float getFitness() {
        return fitness;
    }

}
