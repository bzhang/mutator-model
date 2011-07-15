/**
 * @author Bingjun
 * Generate a pair of individuals to reproducePair
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

    public void mutate() {}

    public IndividualPair reproduce() {
        return new IndividualPair(individualA, individualB);
    }
}
