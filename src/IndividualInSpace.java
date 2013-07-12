/**
 * @author Bingjun Zhang
 */
public class IndividualInSpace {
    private Individual individual;
    private float x;
    private float y;

    public IndividualInSpace(Individual individual, int x, int y) {
        this.individual = individual;
        this.x = x;
        this.y = y;
    }

    public Individual getIndividual() {
        return individual;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
