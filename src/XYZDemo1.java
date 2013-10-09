/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

//package com.jpmorgan.dqreport.util;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import javax.swing.JFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.DomainOrder;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.ShapeUtilities;

public class XYZDemo1 {
    public static void main(String[] args) {
        String propertiesFileName = args.length > 0 ? args[0] : "MutatorModel.properties";
        ModelParameters.setPropertiesFileName(propertiesFileName);
//        JFrame frame = new JFrame("XYZDemo");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int rows = 4;
        int columns = 2;
        String[] rowKeys = {"X1", "X2", "X3", "X4"};
        String[] columnKeys = {"Y1", "Y2"};
        double[] xValues = {1, 2, 3, 4, 5, 6, 7, 8};
        double[] yValues = {1, 2, 3, 4, 5, 6, 7, 8};
        double[] zValues = {0, 1, 1.5, 1.75, 2, 2.25, 3.5, 5.75};
        DefaultXYZDataset dataset = new DefaultXYZDataset();
        double[][] data = new double[3][];
        data[0] = xValues;
        data[1] = yValues;
        data[2] = zValues;
        dataset.addSeries("Series 1", data);
        XYItemRenderer r = new XYZColorRenderer();
        NumberAxis xAxis = new NumberAxis("x");
        xAxis.setRange(-100, 100);
        NumberAxis yAxis = new NumberAxis("y");
        yAxis.setRange(-100, 100);
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, r);
        JFreeChart chart = new JFreeChart("XYZ Demo", new Font("Helvetica",0,18), plot, false);
        double zMax = Double.NEGATIVE_INFINITY;
        double zMin = Double.POSITIVE_INFINITY;
        System.out.println(dataset.getSeriesCount());
        for (int i = 0; i < dataset.getItemCount(0); i++) {
            double z = dataset.getZValue(0, i);
            zMin = Math.min(zMin, z);
            zMax = Math.max(zMax, z);
        }

        System.out.println(zMax);
        System.out.println(zMin);
        TextTitle legendText = new TextTitle("Current Generation: 1; " + "Mutator Max = " + zMax + "; " + "Min = " + zMin);
        legendText.setPosition(RectangleEdge.BOTTOM);
        chart.addSubtitle(legendText);
        TextTitle legendMutatorText = new TextTitle("Mutator Max = " + zMax + " " + "Max = " + zMin);
        legendText.setPosition(RectangleEdge.BOTTOM);
        chart.addSubtitle(legendMutatorText);
//        GradientPaint colorMap = new GradientPaint(
//                50, 50, Color.white,
//                100, 100, Color.red);
//        float[] dist = {0.0f, 1.0f};
//        Color[] colors = {Color.WHITE, Color.RED};
//        LinearGradientPaint colorMap = new LinearGradientPaint(0, 0, 0, 10, dist, colors);
//        LookupPaintScale paintScale = new LookupPaintScale(0.0,100.0,colorMap);
//        NumberAxis scaleAxis = new NumberAxis("Mutator Strength");
//        scaleAxis.setUpperBound(100);
//        scaleAxis.setAxisLinePaint(Color.black);
//        scaleAxis.setTickMarkPaint(Color.black);
//        scaleAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 12));
//        PaintScaleLegend legend = new PaintScaleLegend(paintScale, scaleAxis);
////        PaintScaleLegend legend = new PaintScaleLegend(colorMap, scaleAxis);
//
//        legend.setPadding(new RectangleInsets(10, 10, 10, 10));
//        legend.setStripWidth(25);
//        legend.setPosition(RectangleEdge.RIGHT);
//        legend.setBackgroundPaint(Color.white);
//        legend.setAxisLocation(AxisLocation.TOP_OR_RIGHT);
//        chart.addSubtitle(legend);

//        frame.setContentPane(new ChartPanel(chart));
//        frame.pack();
//        frame.setVisible(true);
        try{
            ChartUtilities.saveChartAsPNG(new File("test1.png"), chart, 700, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static class XYZColorRenderer extends AbstractXYItemRenderer{
//        private Shape[] shapes = new Shape[]{new Rectangle2D.Double(-8, -8, 16, 16), new Ellipse2D.Double(-8, -8, 16, 16)};
//        private Color[] colors = new Color[]{ new Color(189, 252, 201),
//                new Color(0, 255, 0),
//                new Color(202, 255, 112)};
        public void drawItem(Graphics2D g2, XYItemRendererState state,
                             Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot,
                             ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset,
                             int series, int item, CrosshairState crosshairState, int pass) {

//            Shape hotspot = null;
//            EntityCollection entities = null;
//            if (info != null) {
//                entities = info.getOwner().getEntityCollection();
//            }

            double x = dataset.getXValue(series, item);
            double y = dataset.getYValue(series, item);
            if (Double.isNaN(x) || Double.isNaN(y)) {
                // can't draw anything
                return;
            }

            double transX = domainAxis.valueToJava2D(x, dataArea,
                    plot.getDomainAxisEdge());
            double transY = rangeAxis.valueToJava2D(y, dataArea,
                    plot.getRangeAxisEdge());

//            PlotOrientation orientation = plot.getOrientation();
//            boolean useDefaultShape = true;
            Shape shape = null;
            Color color = null;
            if(dataset instanceof XYZDataset){
                XYZDataset xyz = (XYZDataset)dataset;
                double z = xyz.getZValue(series, item);
                int red = 0;
                if (z > 1) {
                    red = (int) Math.floor(Math.atan((z-1) * ModelParameters.getFloat("COLOR_SCALE")) * 2 / Math.PI * 256);
                }
                color = new Color(255, 255-red, 255-red);

//                if(z == 0){
//                    shape = shapes[0];
//                    color = colors[0];
//                    useDefaultShape = false;
//                }
//                if(z == 1){
//                    shape = shapes[1];
//                    color = colors[1];
//                    useDefaultShape = false;
//                }
            }
//            if(useDefaultShape){
//            shape = getItemShape(series, item);
//            }
//            if (orientation == PlotOrientation.HORIZONTAL) {
//                shape = ShapeUtilities.createTranslatedShape(shape, transY,
//                        transX);
//            }
//            else if (orientation == PlotOrientation.VERTICAL) {
            shape = new Ellipse2D.Double(-4, -4, 8, 8);
            shape = ShapeUtilities.createTranslatedShape(shape, transX,
                        transY);
//            }
//            hotspot = shape;
            if (shape.intersects(dataArea)) {
                //if (getItemShapeFilled(series, item)) {
                g2.setPaint(color);
                g2.fill(shape);
                //}
                g2.setPaint(getItemOutlinePaint(series, item));
                g2.setStroke(getItemOutlineStroke(series, item));
                g2.draw(shape);
            }

//            // add an entity for the item...
//            if (entities != null) {
//                addEntity(entities, hotspot, dataset, series, item, transX,
//                        transY);
//            }
        }
    }
}