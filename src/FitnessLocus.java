import java.util.ArrayList;

/**
 * Created by bingjun at 7/8/11 11:43 AM
 */

public class FitnessLocus implements Locus {

    private ArrayList<Float> fitnessEffects;

    public FitnessLocus(float effect) {
        fitnessEffects = new ArrayList<Float>();
        fitnessEffects.add(effect);
    }

    public float getFitnessEffect() {
        float effect = 1;
        for (float e : fitnessEffects) {
            effect *= e;
        }
        return effect;
    }

    public void addFitnessEffect(float effect) {
        fitnessEffects.add(effect);
    }

}
