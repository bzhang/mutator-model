import java.util.ArrayList;

/**
 * Created by bingjun at 7/8/11 11:43 AM
 */

public class FitnessLocus extends Locus {

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

    public Object clone() throws CloneNotSupportedException {
        FitnessLocus cloned = null;
        try {
            cloned = (FitnessLocus) super.clone();
            cloned.initFitnessEffects();
            for (float fitnessEffect : fitnessEffects) {
                cloned.addFitnessEffect(fitnessEffect);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cloned;
    }

    protected void initFitnessEffects() {
        fitnessEffects = new ArrayList<Float>();
    }

}
