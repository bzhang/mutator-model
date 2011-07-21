import java.util.Random;

/**
 * @author Bingjun
 * A pair of individuals
 */

public class IndividualPair {

    private Individual individualA, individualB;
    private Random random = new Random(System.nanoTime());

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

    public void mutate() {
        individualA.mutate();
        individualB.mutate();
    }

    public IndividualPair reproduce() {

        IndividualPair offspringPair = new IndividualPair(individualA, individualB);

        float recombinationStrengthA = individualA.getRecombinationStrength();
        float recombinationStrengthB = individualB.getRecombinationStrength();
        float recombinationProbability = (recombinationStrengthA + recombinationStrengthB) / 2;

        if (random.nextFloat() < recombinationProbability) {
            offspringPair.recombine();
        }

        return offspringPair;
    }

    private void recombine() {
        int genomeSize = individualA.getGenomeSize();
        int position = random.nextInt(genomeSize);

        for (int i = position; i < genomeSize; i++) {
            Locus temp = individualA.getLocus(i);
            individualA.setLocus(i, individualB.getLocus(i));
            individualB.setLocus(i, temp);
        }
    }

}
