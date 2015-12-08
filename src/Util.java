import org.apache.commons.io.FileUtils;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.ShapeUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
        double arraySize = 0;
        for (double element : array) {
//            System.out.println("element = " + element);
            if (!isNaN(element)) {
                sum += element;
                arraySize += 1;
            }
        }
//        System.out.println("sum / arraySize = " + sum / arraySize);
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

    public static double skewness(double[] array) {
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
                sumOfDiff += Math.pow(element - mean, 3);
            }
        }
        return (sumOfDiff / arraySize) / Math.pow(variance(array), 1.5);
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
        int mutationPropertiesSize = 7;
        if (mutationProperties.size() % mutationPropertiesSize == 0) {
            for (int i = 0; i < mutationProperties.size(); i += mutationPropertiesSize) {
                for (int j = 0; j < mutationPropertiesSize - 1; j++) {
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
        long fileID = System.nanoTime();
        String directoryName = ModelParameters.getDirectoryName();
        File outputDir = new File(directoryName);
        File pngDir = new File(directoryName + "/" + fileID + "/");
        if (!outputDir.exists() && outputDir.mkdir()) {
            System.out.println("Directory: " + directoryName + " created.");
        }
        if (!pngDir.exists() && pngDir.mkdir()) {
            System.out.println("Directory: " + directoryName + "/" + fileID + "/" + " created.");
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

        return directoryName + "/" + fileID;
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

    public static GroupReturn getVarianceStdSkewnessOfU(int gen, double[] mutatorStrengthArray, String mutStatFilename){
        double[] statArray = new double[mutatorStrengthArray.length];
        String mutStatFileOutput = "";
//        System.out.println(mutatorStrengthArray.length);
        int j = 0;
        for (int i=0; i < mutatorStrengthArray.length; i++) {
            double U = mutatorStrengthArray[i] * ModelParameters.getDouble("BASE_DELETERIOUS_MUTATION_RATE") * 2;
            double lnU = Math.log(U);
//            ln(U_opt) = 0.6591, tau = 0.8152
            double stat = (lnU - ModelParameters.getDouble("LN_U_OPT")) / ModelParameters.getDouble("TAU");
            statArray[i] = stat;
//            if ((gen >= 50000) && ((gen % 10000) == 0)) {
            if ((gen >= 60000) && ((gen % 1000) == 0)) {
//                System.out.println(j);
                j++;
                mutStatFileOutput += gen + "\t" + U + "\n";
            }
        }
        Util.writeFile(mutStatFilename, mutStatFileOutput);
        double meanTransformedValue = Util.mean(statArray);
        double variance = Util.variance(statArray);
        double std = Math.sqrt(variance);
        double skewness = Util.skewness(statArray);
        return new GroupReturn(meanTransformedValue, variance, std, skewness);
    }

    public static String outputPopulationStat(int i, Population population, String mutStatFilename) {
        GroupReturn fitnessPropertiesArray = population.getFitnessPropertiesArray();
        double[] fitnessArray = fitnessPropertiesArray.getFitnessArray();
        int[] nDeleMutArray = fitnessPropertiesArray.getNDeleMutArray();
        int[] nBeneMutArray = fitnessPropertiesArray.getnBeneMutArray();
        double[] meanDeleFitnessEffectArray = fitnessPropertiesArray.getMeanDeleFitnessEffectArray();
        double[] meanBeneFitnessEffectArray = fitnessPropertiesArray.getMeanBeneFitnessEffectArray();
        GroupReturn mutatorAndRecombinationStrengthArray = population.getMutatorAndRecombinationStrengthArray();
        double[] mutatorStrengthArray = mutatorAndRecombinationStrengthArray.getMutatorStrengthArray();
        double[] recombinationStrengthArray = mutatorAndRecombinationStrengthArray.getRecombinationStrengthArray();
        double fitness = Util.mean(fitnessArray);
        GroupReturn varianceStdSkewnessArray = Util.getVarianceStdSkewnessOfU(i, mutatorStrengthArray, mutStatFilename);
        double meanTransformedValue = varianceStdSkewnessArray.getMeanTransformedValue();
        double variance = varianceStdSkewnessArray.getVariance();
        double std = varianceStdSkewnessArray.getStd();
        double skewness = varianceStdSkewnessArray.getSkewness();
//        System.out.println("Fitness = " + fitness);

        return i + "\t" + fitness
                + "\t" + Util.mean(mutatorStrengthArray)
                + "\t" + Util.mean(nDeleMutArray)
                + "\t" + Util.mean(nBeneMutArray)
                + "\t" + Util.mean(recombinationStrengthArray)
                + "\t" + Util.mean(meanDeleFitnessEffectArray)
                + "\t" + Util.mean(meanBeneFitnessEffectArray)
                + "\t" + meanTransformedValue
                + "\t" + variance
                + "\t" + std
                + "\t" + skewness
                + "\t" + Util.standardDeviation(fitnessArray)
                + "\t" + Util.standardDeviation(mutatorStrengthArray)
                + "\t" + Util.standardDeviation(recombinationStrengthArray)
                + "\t" + Util.standardDeviation(nDeleMutArray)
                + "\t" + Util.standardDeviation(nBeneMutArray)
                + "\t" + Util.standardDeviation(meanDeleFitnessEffectArray)
                + "\t" + Util.standardDeviation(meanBeneFitnessEffectArray)
                + "\n";
    }

    public static double[] initTotals(double[] weights) {
        double[] totals = new double[weights.length];
        double runningTotal = 0;
        int i = 0;
        for (double weight : weights) {
            runningTotal += weight;
            totals[i++] = runningTotal;
        }
        return totals;
    }

//    public static List<List<Integer>> getDirections() {
//        int matingDistance = ModelParameters.getInt("MATING_DISTANCE");
//        List<List<Integer>> directions = new ArrayList<List<Integer>>();
//        for (int i = -matingDistance; i < matingDistance + 1; i++) {
//            for (int j = -matingDistance; j < matingDistance + 1; j++) {
//                ArrayList<Integer> direction = new ArrayList<Integer>(2);
//                direction.add(i);
//                direction.add(j);
//                directions.add(direction);
//            }
//        }
//        return directions;
//    }
//
//    public static String outputMetaPopulationStat(int i, MetaPopulation metaPopulation) {
//        GroupReturn fitnessPropertiesArray = metaPopulation.getFitnessPropertiesArray();
//        double[] fitnessArray = fitnessPropertiesArray.getFitnessArray();
//        int[] nDeleMutArray = fitnessPropertiesArray.getNDeleMutArray();
//        int[] nBeneMutArray = fitnessPropertiesArray.getnBeneMutArray();
//        double[] meanDeleFitnessEffectArray = fitnessPropertiesArray.getMeanDeleFitnessEffectArray();
//        double[] meanBeneFitnessEffectArray = fitnessPropertiesArray.getMeanBeneFitnessEffectArray();
//        double[] mutatorStrengthArray = metaPopulation.getMutatorStrengthArray();
//
//        return i + "\t" + Util.mean(fitnessArray)
//                + "\t" + Util.mean(mutatorStrengthArray)
//                + "\t" + Util.mean(nDeleMutArray)
//                + "\t" + Util.mean(nBeneMutArray)
//                + "\t" + Util.mean(meanDeleFitnessEffectArray)
//                + "\t" + Util.mean(meanBeneFitnessEffectArray)
//                + "\t" + Util.standardDeviation(fitnessArray)
//                + "\t" + Util.standardDeviation(mutatorStrengthArray)
//                + "\t" + Util.standardDeviation(nDeleMutArray)
//                + "\t" + Util.standardDeviation(nBeneMutArray)
//                + "\t" + Util.standardDeviation(meanDeleFitnessEffectArray)
//                + "\t" + Util.standardDeviation(meanBeneFitnessEffectArray)
//                + "\n";
//    }

    public static void scatterPlot(XYZDataset xyzDataset, int currentGeneration, String resultFileNamePrefix) {
        XYItemRenderer r = new XYZColorRenderer();
        NumberAxis xAxis = new NumberAxis("x");
        NumberAxis yAxis = new NumberAxis("y");
//        xAxis.setRange(-1 * ModelParameters.getDouble("IMAGE_RANGE"), ModelParameters.getDouble("IMAGE_RANGE"));
//        yAxis.setRange(-1 * ModelParameters.getDouble("IMAGE_RANGE"), ModelParameters.getDouble("IMAGE_RANGE"));
        XYPlot plot = new XYPlot(xyzDataset, xAxis, yAxis, r);
        JFreeChart chart = new JFreeChart("Mutator in Space", new Font("Helvetica",0,18), plot, false);
        double zMax = Double.NEGATIVE_INFINITY;
        double zMin = Double.POSITIVE_INFINITY;
//        System.out.println(xyzDataset.getSeriesCount());
        for (int i = 0; i < xyzDataset.getItemCount(0); i++) {
            double z = xyzDataset.getZValue(0, i);
            zMin = Math.min(zMin, z);
            zMax = Math.max(zMax, z);
        }
        TextTitle legendText = new TextTitle("Current Generation: " + currentGeneration + "\n" + "Mutator Max = " + zMax + "\n" + "Mutator Min = " + zMin);
        legendText.setPosition(RectangleEdge.BOTTOM);
//        xyzDataset.getZValue(1, )
        chart.addSubtitle(legendText);
        try{
            ChartUtilities.saveChartAsPNG(new File(resultFileNamePrefix + "/" + currentGeneration + ".png"), chart, 700, 700);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class XYZColorRenderer extends AbstractXYItemRenderer {
        public void drawItem(Graphics2D g2, XYItemRendererState state,
                             Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot,
                             ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset xyzDataset,
                             int series, int item, CrosshairState crosshairState, int pass) {
            double x = xyzDataset.getXValue(series, item);
            double y = xyzDataset.getYValue(series, item);
            if (Double.isNaN(x) || Double.isNaN(y)) {
                // can't draw anything
                return;
            }

            double transX = domainAxis.valueToJava2D(x, dataArea,
                    plot.getDomainAxisEdge());
            double transY = rangeAxis.valueToJava2D(y, dataArea,
                    plot.getRangeAxisEdge());

            Shape shape = null;
            Color color = null;
            if(xyzDataset instanceof XYZDataset){
                XYZDataset xyz = (XYZDataset)xyzDataset;
                double z = xyz.getZValue(series, item);
                int red = 0;
                if (z > 1) {
                    red = (int) Math.floor(Math.atan((z-1) * ModelParameters.getFloat("COLOR_SCALE")) * 2 / Math.PI * 256);
//                    System.out.println("z = " + z);
//                    System.out.println("red = " + red);
                }
                color = new Color(255, 255-red, 255-red);
            }
            shape = new Ellipse2D.Double(-3, -3, 6, 6);
            shape = ShapeUtilities.createTranslatedShape(shape, transX,
                    transY);
            if (shape.intersects(dataArea)) {
                g2.setPaint(color);
                g2.fill(shape);
                g2.setPaint(getItemOutlinePaint(series, item));
                g2.setStroke(getItemOutlineStroke(series, item));
                g2.draw(shape);
            }
        }
    }
}
