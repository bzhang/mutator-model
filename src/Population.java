import java.util.ArrayList;

/**
 * Created by Bingjun at 5/16/11 12:32 AM
 */

public class Population {

    private ArrayList<Individual> individuals;

    public Population(int size) {
        individuals = new ArrayList<Individual>();

        for (int i = 0; i < size; i++) {
            individuals.add(new Individual());
        }
    }

    public Population() {
        this(0);
    }

    public ArrayList<Individual> getIndividuals() {
        return individuals;
    }

    public Population createNextGeneration() {
        Population nextGeneration = new Population();

        while(nextGeneration.getSize() < getSize()) {
            IndividualPair offspringPair = reproduce();
            // TODO: add mutation
            nextGeneration.addIndividualPairs(offspringPair);
        }

        return nextGeneration;
    }

    private void addIndividualPairs(IndividualPair offspringPair) {
        addIndividual(offspringPair.getIndividualA());
        addIndividual(offspringPair.getIndividualB());
    }

    private void addIndividual(Individual individual) {
        individuals.add(individual);
    }

    public IndividualPair reproduce() {

        IndividualPair parentPair = getTwoRandomIndividuals();
        Individual parentA = parentPair.getIndividualA();
        Individual parentB = parentPair.getIndividualB();

        // TODO: add sex reproduction

        Individual offspringA = parentA;
        Individual offspringB = parentB;
        IndividualPair offspringPair = new IndividualPair(offspringA, offspringB);
        return offspringPair;
    }

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

    private float[] getAllFitness() {
        float[] allFitness = new float[getSize()];
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
