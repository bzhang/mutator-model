import java.util.Arrays;
import java.util.Random;

/**
 * @author Bingjun Zhang
 */

//Generated weighted random numbers according to the weights array.
public class WeightedRandomGenerator {

//    private Random random = new Random(System.nanoTime());
    private float randomNumber;
    private float[] totals;

    public WeightedRandomGenerator(float[] weights) {
        totals = new float[weights.length];
        initTotals(weights);
    }

    public int nextInt() {
        float sumOfWeights = totals[totals.length - 1];
        randomNumber = Rand.getFloat() * sumOfWeights;
        int index = Arrays.binarySearch(totals, randomNumber);
        index = (index >= 0) ? index : - index - 1;
        return index;
    }

    private void initTotals(float[] weights) {
        float runningTotal = 0;
        int i = 0;
        for (float weight: weights) {
            runningTotal += weight;
            totals[i++] = runningTotal;
        }
    }
}
