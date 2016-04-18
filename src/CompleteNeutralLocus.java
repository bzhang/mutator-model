public class CompleteNeutralLocus extends Locus {

    private float strength;

    public CompleteNeutralLocus(float strength){
        this.strength = strength;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public void mutateStrength() {
        this.strength += Rand.getGaussian();
    }

}
