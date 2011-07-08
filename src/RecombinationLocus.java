/**
 * @author Bingjun
 *         7/8/11 9:50 AM
 */
public class RecombinationLocus {

    private float recombinationModifierStrength;
    private int recombinationModifierPosition;

    public RecombinationLocus(){
        recombinationModifierStrength = 10;
        recombinationModifierPosition = 3;
    }

// A setter necessary?
//    public void setRecombinationModifierPosition(int recombinationModifierPosition) {
//        this.recombinationModifierPosition = recombinationModifierPosition;
//    }

    public float getRecombinationModifierStrength() {
        return recombinationModifierStrength;
    }

    public int getRecombinationModifierPosition() {
        return recombinationModifierPosition;
    }
}
