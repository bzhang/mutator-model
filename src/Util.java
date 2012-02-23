import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

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

    private static String outputMutMap(ArrayList mutationProperties) {
        Iterator iterator = mutationProperties.iterator();
        String output = "";

        while (iterator.hasNext()) {
            output += longMapEntry.getKey() + "\t";
            mutationProperties = longMapEntry.getValue();
//            String mutMapFileOutput = "MutationID\tGeneration\tFitnessEffect\tMutatorStrength\tLocus\n";
            output += mutationProperties.get("Generation") + "\t"
                    + mutationProperties.get("FitnessEffect") + "\t"
                    + mutationProperties.get("MutatorStrength") + "\t"
                    + mutationProperties.get("Locus") + "\n";

        }
        return output;
    }


}
