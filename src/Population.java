import java.util.ArrayList;

/**
 * Created by Bingjun at 5/16/11 12:32 AM
 */

public class Population {

    private ArrayList<Individual> individuals = new ArrayList<Individual>();

    public Population(int size) {
        for (int i = 0; i < size; i++) {
            individuals.add(new Individual());
        }
    }

    public ArrayList<Individual> getIndividuals() {
        return individuals;
    }

    public Population createNextGeneration() {
        Population nextGeneration = new Population(getSize());

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
        // TODO: add randomization
        return new IndividualPair(getIndividuals().get(0), getIndividuals().get(1));
    }

    public int getSize() {
        return individuals.size();
    }
}
