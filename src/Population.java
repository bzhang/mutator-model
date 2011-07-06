import java.util.ArrayList;

/**
 * @author Bingjun
 * 5/16/11 12:32 AM
 */

public class Population {
    // Initialization. Create a new population which is a ArrayList, the elements are individuals    
    private ArrayList<Individual> individuals;

    // Add new individuals into the population (individuals) until reach the size limit (size)
    public Population(int size) {
        individuals = new ArrayList<Individual>();

        for (int i = 0; i < size; i++) {
            individuals.add(new Individual());
        }
    }

    //
    public Population() {
        this(0);
    }

    public ArrayList<Individual> getIndividuals() {
        return individuals;
    }

    // Create next generation by reproducing and mutating
    public Population createNextGeneration() {
        // Initialize next generation with the 0 pop size as current one
        Population nextGeneration = new Population();

        // Loop until next generation reach the pop size limit
        while(nextGeneration.getSize() < getSize()) {
            // Reproduce and return two offspring
            IndividualPair offspringPair = reproduce();
            IndividualPair mutatedOffspringPair = mutate(offspringPair);
            // Add new offspring into next generation
            nextGeneration.addIndividualPairs(mutatedOffspringPair);
        }

        return nextGeneration;
    }

    private IndividualPair mutate(IndividualPair individualPair) {
        return individualPair;  //To change body of created methods use File | Settings | File Templates.
    }

    // Add two individuals into the individuals array
    private void addIndividualPairs(IndividualPair offspringPair) {
        addIndividual(offspringPair.getIndividualA());
        addIndividual(offspringPair.getIndividualB());
    }

    private void addIndividual(Individual individual) {
        individuals.add(individual);
    }

    public IndividualPair reproduce() {
        // Get two parent individuals randomly
        IndividualPair parentPair = getTwoRandomIndividuals();
        Individual parentA = parentPair.getIndividualA();
        Individual parentB = parentPair.getIndividualB();

        // TODO: add sex reproduction

        // Asexual reproduction: copy the parent genome to offspring
        Individual offspringA = parentA;
        Individual offspringB = parentB;
        IndividualPair offspringPair = new IndividualPair(offspringA, offspringB);
        return offspringPair;
    }

    // Get two random individuals to reproduce according to their fitness
    private IndividualPair getTwoRandomIndividuals() {
        WeightedRandomGenerator wrg = new WeightedRandomGenerator(getAllFitness());
        int indexA = wrg.nextInt();
        int indexB = wrg.nextInt();
        while (indexA == indexB) {
            indexB = wrg.nextInt();
        }
        Individual individualA = getIndividuals().get(indexA);
        Individual individualB = getIndividuals().get(indexB);
        return new IndividualPair(individualA, individualB);
    }

    private double[] getAllFitness() {
        double[] allFitness = new double[getSize()];
        int i = 0;
        for (Individual individual : getIndividuals()) {
            allFitness[i++] = individual.getFitness();
        }
        return allFitness;
    }

    public int getSize() {
        return individuals.size();
    }
}
