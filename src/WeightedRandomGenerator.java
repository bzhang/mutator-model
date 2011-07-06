import java.util.Arrays;
import java.util.Random;

/**
 * @author Bingjun
 * Generated weighted random numbers according to the weights array.
 */

public class WeightedRandomGenerator {

    private Random random = new Random(System.nanoTime());
    private double[] totals;

    public WeightedRandomGenerator(double[] weights) {
        totals = new double[weights.length];
        initTotals(weights);
    }

    public int nextInt() {
        double sumOfWeights = totals[totals.length - 1];
        double randomNumber = random.nextFloat() * sumOfWeights;
        int index = Arrays.binarySearch(totals, randomNumber);
        index = (index >= 0) ? index : - index - 1;
        return index;
    }

    private void initTotals(double[] weights) {
        float runningTotal = 0;
        int i = 0;
        for (double weight: weights) {
            runningTotal += weight;
            totals[i++] = runningTotal;
        }
    }
}
