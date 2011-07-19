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
        IndividualPair offspringPair = new IndividualPair(individualA,individualB);
        float recombinationLocusStrengthA;
        float recombinationLocusStrengthB;
        float recombinationProbability;
        int recombinationPosition;
        // Calculate the probability of recombination, mean(RecombinationLocusStrengthA, B)
        recombinationLocusStrengthA = individualA.getRecombinationLocusStrength();
        recombinationLocusStrengthB = individualB.getRecombinationLocusStrength();
        recombinationProbability = (recombinationLocusStrengthA + recombinationLocusStrengthB) / 2;

        if(random.nextFloat() < recombinationProbability){
            return offspringPair;
        }   else {
            recombinationPosition = random.nextInt(individualA.getGenomeSize());
            offspringPair = recombine(individualA, individualB, recombinationPosition);
            return offspringPair;
        }
    }

    private IndividualPair recombine(Individual individualA, Individual individualB, int recombinationPosition) {
        IndividualPair recombinedOffspringPair;
        Individual recombinedIndividualA = individualA;
        Individual recombinedIndividualB = individualB;
        int length = individualA.getGenomeSize() - recombinationPosition + 1;

        arraycopy(individualB, recombinationPosition,
                recombinedIndividualA, recombinationPosition,
                length);
        arraycopy(individualA, recombinationPosition,
                recombinedIndividualB, recombinationPosition,
                length);
        return recombinedOffspringPair = new IndividualPair(recombinedIndividualA, recombinedIndividualB);
    }

}
