import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Bingjun Zhang
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

    public static float mean(int[] array) {
        return mean(convertIntToFloat(array));

    }

    public static float standardDeviation(float[] array) {
        float sumOfDiff = 0;
        for (float element : array) {
            sumOfDiff += Math.pow(element - mean(array), 2);
        }
        return (float) Math.sqrt(sumOfDiff / (array.length - 1));
    }

    public static float standardDeviation(int[] array) {
        return standardDeviation(convertIntToFloat(array));
    }

    private static float[] convertIntToFloat(int[] intArray) {
        float[] floatArray = new float[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            floatArray[i] = intArray[i];
        }
        return floatArray;
    }

    public static void writeFile(String outputFileName, String output) {
        try {
            FileWriter fileWriter = new FileWriter(outputFileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(output);
            bufferedWriter.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static String outputMutMap(ArrayList mutationProperties) {
        Iterator iterator = mutationProperties.iterator();
        String output = "";

        if (mutationProperties.size() % 5 == 0) {
            for (int i = 0; i < mutationProperties.size(); i += 5) {
                for (int j = 0; j < 4; j++) {
                    output += iterator.next() + "\t";
                }
                output += iterator.next() + "\n";
            }
        } else {
            System.err.println("The number of elements in mutationProperties" +
                                " can not be completely divided by 5!");
        }
        return output;
    }


}
