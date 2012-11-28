import java.util.ArrayList;

/**
 * @author Bingjun Zhang
 */

public class IndividualPair {

    private Individual individualA, individualB;

    public IndividualPair(Individual individualA, Individual individualB) {
        this.individualA = individualA;
        this.individualB = individualB;
    }

    public Individual getIndividualA() {
        return individualA;
    }

    public Individual getIndividualB() {
        return individualB;
    }

    public void mutate(int currentGeneration, ArrayList mutationProperties, double parentFitnessMean, double parentFitnessSD, double corFitnessMutatorStrength) {
        getIndividualA().mutate(currentGeneration, mutationProperties, parentFitnessMean, parentFitnessSD, corFitnessMutatorStrength);
        getIndividualB().mutate(currentGeneration, mutationProperties, parentFitnessMean, parentFitnessSD, corFitnessMutatorStrength);
    }

    public void mutate(int currentGeneration) {
        getIndividualA().mutate(currentGeneration);
        getIndividualB().mutate(currentGeneration);
    }

    public IndividualPair reproduce() {

        Individual offspringA = new Individual(getIndividualA());
        Individual offspringB = new Individual(getIndividualB());
        IndividualPair offspringPair = new IndividualPair(offspringA, offspringB);

        float recombinationStrengthA = offspringA.getRecombinationStrength();
        float recombinationStrengthB = offspringB.getRecombinationStrength();
        float recombinationProbability = (recombinationStrengthA + recombinationStrengthB) / 2;

        if (Rand.getFloat() < recombinationProbability) {
            offspringPair.recombine();
        }

        return offspringPair;
    }

    private void recombine() {
        int genomeSize = getIndividualA().getGenomeSize();
        int position = Rand.getInt(genomeSize);

        for (int i = position; i < genomeSize; i++) {
            Locus locusA = getIndividualA().getLocus(i);
            Locus locusB = getIndividualB().getLocus(i);
            getIndividualA().setLocus(i, locusB);
            getIndividualB().setLocus(i, locusA);
        }
    }
}
