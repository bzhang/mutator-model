/**
 * Created by bingjun at 7/8/11 11:43 AM
 */

public class FitnessLocus implements Locus {

    private float fitnessEffect;

    public FitnessLocus(float fitnessEffect) {
        this.fitnessEffect = fitnessEffect;
    }

    public float getFitnessEffect() {
        return fitnessEffect;
    }
}
