/**
 * @author Bingjun Zhang
 */
public class IndividualInSpace {
    private Individual individual;
    private int x;
    private int y;

    public IndividualInSpace(Individual individual, int x, int y) {
        this.individual = individual;
        this.x = x;
        this.y = y;
    }

    public Individual getIndividual() {
        return individual;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
