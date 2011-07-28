/**
 * @author Bingjun
 *         7/8/11 9:50 AM
 */

public class RecombinationLocus implements Locus {

    private float strength;

    public RecombinationLocus(float strength){
        this.strength = strength;
    }

    public RecombinationLocus clone() {
        return new RecombinationLocus(getStrength());
    }

    public float getStrength() {
        return strength;
    }
}
