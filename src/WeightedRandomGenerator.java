import java.util.Arrays;
import java.util.Random;

/**
 * @author Bingjun Zhang
 */

//Generated weighted random numbers according to the weights array.
public class WeightedRandomGenerator {

    public int nextInt(float[] totals) {
        float sumOfWeights = totals[totals.length - 1];
        float randomNumber = Rand.getFloat() * sumOfWeights;
        int index = Arrays.binarySearch(totals, randomNumber);
        index = (index >= 0) ? index : - index - 1;
        return index;
    }

}
