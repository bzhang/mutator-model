import org.apache.commons.io.FileUtils;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static java.lang.Double.*;

/**
 * @author Bingjun Zhang
 */

public class Util {
    public static double sum(double[] array) {
        double sum = 0;
        for (double element : array) {
                sum += element;
        }
        return sum;
    }

    public static double mean(double[] array) {
        double sum = 0;
        int arraySize = 0;
        for (double element : array) {
            if (!isNaN(element)) {
                sum += element;
                arraySize += 1;
            }
        }
        return sum / arraySize;
    }

    public static double mean(int[] array) {
        return mean(convertIntToDouble(array));

    }

    public static double standardDeviation(double[] array) {
        return Math.sqrt(variance(array));
    }

    public static double standardDeviation(int[] array) {
        return standardDeviation(convertIntToDouble(array));
    }

    private static double[] convertIntToDouble(int[] intArray) {
        double[] floatArray = new double[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            floatArray[i] = intArray[i];
        }
        return floatArray;
    }

    public static double variance(double[] array) {
        double sum = 0;
        int arraySize = 0;
        for (double element : array) {
            if (!isNaN(element)) {
                sum += element;
                arraySize += 1;
            }
        }
        double mean = sum / arraySize;
        float sumOfDiff = 0;
        for (double element : array) {
            if (!isNaN(element)) {
                sumOfDiff += Math.pow(element - mean, 2);
            }
        }
        return sumOfDiff / arraySize;
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

        if (mutationProperties.size() % 6 == 0) {
            for (int i = 0; i < mutationProperties.size(); i += 6) {
                for (int j = 0; j < 5; j++) {
                    output += iterator.next() + "\t";
                }
                output += iterator.next() + "\n";
            }
        } else {
            System.err.println("The number of elements in mutationProperties" +
                                " can not be completely divided by 6!");
        }
        return output;
    }

    public static String prepareOutputDirectory() {
        String directoryName = ModelParameters.getDirectoryName();
        File outputDir = new File(directoryName);
        if (!outputDir.exists() && outputDir.mkdir()) {
            System.out.println("Directory: " + directoryName + " created.");
        }

        File propertiesFile  = new File(ModelParameters.getPropertiesFilename());
        File destinationFile = new File(directoryName + "/" + ModelParameters.getPropertiesFilename());
        if (!destinationFile.exists()) {
            try {
                FileUtils.copyFileToDirectory(propertiesFile, outputDir, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return directoryName + "/" + System.nanoTime();
    }

    public static String getResultFileNamePrefix() {
        return prepareOutputDirectory();
    }

    public static int getPoisson(double lambda) {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do {
            k++;
            p *= Math.random();
        } while (p > L);

        return k - 1;
    }

    public static double pearsonCorrelation(double[] data1, double[] data2) {
        return new PearsonsCorrelation().correlation(data1, data2);
    }
}
