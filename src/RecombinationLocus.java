/**
 * @author Bingjun
 *         7/8/11 9:50 AM
 */
public class RecombinationLocus implements Locus {

    private float recombinationLocusStrength;
    private int recombinationLocusPosition;

    public RecombinationLocus(){
        recombinationLocusStrength = 10;
        recombinationLocusPosition = 3;
    }

// A setter necessary?
//    public void setRecombinationModifierPosition(int recombinationLocusPosition) {
//        this.recombinationLocusPosition = recombinationLocusPosition;
//    }

    public float getRecombinationLocusStrength() {
        return recombinationLocusStrength;
    }

    public int getRecombinationLocusPosition() {
        return recombinationLocusPosition;
    }
}
