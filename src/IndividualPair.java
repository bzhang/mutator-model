import java.util.Map;

/**
 * @author Bingjun
 * A pair of individuals
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

    public void mutate(int currentGeneration, Map mutationMap) {
        getIndividualA().mutate(currentGeneration, mutationMap);
        getIndividualB().mutate(currentGeneration, mutationMap);
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
