/**
 * Created by bingjun on 2/17/16.
 */
public class NeutralLocus extends Locus {

    private float strength;

    public NeutralLocus(float strength){
        this.strength = strength;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public void increaseStrength() {
        this.strength *= Math.pow(Rand.getDouble(), -ModelParameters.getFloat("NEUTRAL_MODIFIER_EFFECT"));
    }

    public void decreaseStrength() {
        this.strength *= Math.pow(Rand.getDouble(), ModelParameters.getFloat("ANTINEUTRAL_MODIFIER_EFFECT"));
    }

}
