import java.lang.reflect.Array;

/**
 * Created by Bingjun at 7/21/11 1:29 AM
 */

public class Util {

    public static float sum(float[] array) {
        float sum = 0;
        for (float element : array) {
            sum += element;
        }
        return sum;
    }

    public static float mean(float[] array) {
        return sum(array) / array.length;
    }

    public static float standardDeviation(float[] array) {
        float sumOfDiff = 0;
        for (float element : array) {
            sumOfDiff += Math.pow(element - mean(array), 2);
        }
        return (float) Math.sqrt(sumOfDiff / (array.length - 1));
    }

}
