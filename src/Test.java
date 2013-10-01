import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author Bingjun Zhang
 */
public class Test{

    public static void main(String[] args) {

        String propertiesFileName = args.length > 0 ? args[0] : "MutatorModel.properties";
        ModelParameters.setPropertiesFileName(propertiesFileName);
//
//        int nIndividual = 614666;
//        int nDeleMutation = 15;
//        int nBeneMutation = 10;
//        int nMutatorMutation = 8;
//        int nAntiMutMutation = 5;
//        float baseMutationRate = 0.1f;
//        LociPattern lociPattern = new LociPattern(100, 1, 1);
//
//        for (int i = 0; i < nIndividual; i++) {
//            double fitnessEffect;
//            double mutationRate;
//            Individual individual = new Individual(lociPattern);
//
//            for (int location = 0; location < ModelParameters.getGenomeSize(); location++) {
//                if (lociPattern.getLocusType(location) == LociPattern.LocusType.Fitness) {
//                    individual.setFitnessLocus(location);
//                }
//                else if (lociPattern.getLocusType(location) == LociPattern.LocusType.Mutator) {
//                    individual.setMutatorLocus(location, 1);
//                }
//                else {
//                    individual.setRecombinationLocus(location, 0);
//                }
//            }
//
//            fitnessEffect = individual.mutate(nDeleMutation, nBeneMutation);
//            individual.mutateMutationRate(nMutatorMutation, nAntiMutMutation);
//            mutationRate = individual.getMutatorStrength() * baseMutationRate;
//            Util.writeFile("fitness.txt", fitnessEffect + "\n");
//            Util.writeFile("mutationRate.txt", mutationRate + "\n");

//        }
//        double[] data1 = {0.9696176324044004, 0.9922179334446213, 0.9922179334446213, 0.9922179334446213, 1.0};
//        double[] data2 = {1.0, 1.0, 1.0, 1.0, 1.0};
//        System.out.println(Arrays.toString(data1));
//
//        System.out.println(Arrays.toString(data2));
//        System.out.println(data1.length + " " + data2.length);
//        double cor = Util.pearsonCorrelation(data1, data2);
//        System.out.println(cor);
//        final String title = "Demo";
//        XYSeriesCollection dataset = new XYSeriesCollection();
//        XYSeries data = new XYSeries("plotData");
//        for (int i = 0; i < 5; i++) {
//            data.add(i, i);
//        }
//        dataset.addSeries(data);
//        JFreeChart jfreechart = ChartFactory.createScatterPlot(title, "X", "Y", dataset, PlotOrientation.VERTICAL, true, true, false);
//        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
//        xyPlot.setDomainCrosshairVisible(true);
//        xyPlot.setRangeCrosshairVisible(true);
//        XYItemRenderer renderer = new xyPlot.getRenderer();
//        renderer.setSeriesPaint(0, Color.blue);
        for (int i = 0; i < 10000; i++) {
            float disperseDistance = ModelParameters.getFloat("DISPERSE_DISTANCE");
            double angle = Math.toRadians(Math.random() * 360);
            double distance = -disperseDistance * Math.log(Rand.getDouble());
            float newX = (float) (distance * Math.cos(angle));
            float newY = (float) (distance * Math.sin(angle));
            Util.writeFile("Demo_Disperse.txt", newX + "\t" + newY + "\n");
        }
    }
}
