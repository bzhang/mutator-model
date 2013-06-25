/**
 @author Bingjun Zhang
 */
public class FourIndividuals {
    private IndividualPair individualPairA, individualPairB;
    private Individual individualA, individualB, individualC, individualD;

    public FourIndividuals(Individual individualA, Individual individualB, Individual individualC, Individual individualD) {
        this.individualA = individualA;
        this.individualB = individualB;
        this.individualC = individualC;
        this.individualD = individualD;
        individualPairA = new IndividualPair(individualA, individualB);
        individualPairB = new IndividualPair(individualC, individualD);
    }

    public Individual getIndividual(int i) {
        Individual newIndividual = null;
        switch (i) {
            case 1:
                newIndividual = individualA;
                break;
            case 2:
                newIndividual = individualB;
                break;
            case 3:
                newIndividual = individualC;
                break;
            case 4:
                newIndividual = individualD;
                break;
        }
        return newIndividual;
    }

    public IndividualPair getIndividualPair(int i) {
        IndividualPair individualPair = null;
        switch (i) {
            case 1:
                individualPair = individualPairA;
                break;
            case 2:
                individualPair = individualPairB;
                break;
        }
        return individualPair;
    }
}
