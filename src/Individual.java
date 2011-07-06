/**
 * @author Bingjun
 * Created by bingjun at 5/16/11 9:42 AM
*/
public class Individual {
    //TODO: read parameters from properties file
    static final int NUM_OF_MUATOR_LOCI = 1;
    static final int NUM_OF_FITNESS_LOCI = 1000;
    static final int NUM_OF_RECOMBINATION_LOCI = 1;
    int genomeSize = NUM_OF_MUATOR_LOCI + NUM_OF_FITNESS_LOCI + NUM_OF_RECOMBINATION_LOCI;

    // TODO: initiate individual.
    private double fitness;

    public Individual() {
        fitness = 0.5;
    }


    public double getFitness() {
        return fitness;
    }

}
