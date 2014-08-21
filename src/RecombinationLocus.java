/**
 * @author Bingjun
 *         7/8/11 9:50 AM
 */

public class RecombinationLocus extends Locus {

    private float strength;

    public RecombinationLocus(float strength){
        this.strength = strength;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public void increaseStrength() {
        this.strength *= Math.pow(Rand.getDouble(), -ModelParameters.getFloat("RECOMBINATION_MODIFIER_EFFECT"));
    }

    public void decreaseStrength() {
        this.strength *= Math.pow(Rand.getDouble(), ModelParameters.getFloat("ANTIRECOMBINATION_MODIFIER_EFFECT"));
    }
}
