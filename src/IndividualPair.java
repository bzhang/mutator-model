import java.util.Random;
import static java.lang.System.arraycopy;

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

    public void mutate() {}

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
        int position = random.nextInt(individualA.getGenomeSize());
        int swapLength = individualA.getGenomeSize() - position + 1;

        Locus[] temp = new Locus[swapLength];
        System.arraycopy(individualA, position, temp, 0, swapLength);
        System.arraycopy(individualB, position, individualA, position, swapLength);
        System.arraycopy(temp, 0, individualB, position, swapLength);
    }

}
