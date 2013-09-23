import java.awt.*;
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
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ScatterAdd extends JFrame {

    private static final int N = 5;
    private static final String title = "Scatter Add Demo";
//    private static final Random rand = new Random();
//    private XYSeries added = new XYSeries("Added");

    public ScatterAdd(String s) {
        super(s);
        final ChartPanel chartPanel = createDemoPanel();
        this.add(chartPanel, BorderLayout.CENTER);
//        JPanel control = new JPanel();
//        control.add(new JButton(new AbstractAction("Add") {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                for (int i = 0; i < N; i++) {
//                    added.add(rand.nextGaussian(), rand.nextGaussian());
//                }
//            }
//        }));
//        this.add(control, BorderLayout.SOUTH);
    }

    private ChartPanel createDemoPanel() {
        JFreeChart jfreechart = ChartFactory.createScatterPlot(
                title, "X", "Y", createSampleData(),
                PlotOrientation.VERTICAL, true, true, false);
        final XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
//        xyPlot.setDomainCrosshairVisible(false);
//        xyPlot.setRangeCrosshairVisible(false);
//        XYItemRenderer renderer = xyPlot.getRenderer();

        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
        domain.setVerticalTickLabels(true);
        return new ChartPanel(jfreechart);
    }

    private XYDataset createSampleData() {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        XYSeries series = new XYSeries("Random");
        for (int i = 0; i < N * N; i++) {
//            double x = rand.nextGaussian();
//            double y = rand.nextGaussian();
            series.add(i, i);
        }
        xySeriesCollection.addSeries(series);
//        xySeriesCollection.addSeries(added);
        return xySeriesCollection;
    }

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                ScatterAdd demo = new ScatterAdd(title);
                demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                demo.pack();
                demo.setLocationRelativeTo(null);
                demo.setVisible(true);
            }
        });
    }
}