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

    public void mutate() {}

    public IndividualPair reproduce() {
        IndividualPair offspringPair;
        //if (isAsexualHomozygote());
        return new IndividualPair(individualA, individualB);
    }

    private boolean isAsexualHomozygote() {
        return true;
    }
}
