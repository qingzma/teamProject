/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject;

import com.orsoncharts.Chart3D;
import com.orsoncharts.Chart3DFactory;
import com.orsoncharts.axis.AbstractValueAxis3D;
import com.orsoncharts.axis.NumberAxis3D;
import com.orsoncharts.axis.ValueAxis3D;
import com.orsoncharts.data.Dataset3D;
import com.orsoncharts.data.function.Function3D;
import com.orsoncharts.data.xyz.XYZDataset;
import com.orsoncharts.data.xyz.XYZSeries;
import com.orsoncharts.data.xyz.XYZSeriesCollection;
import com.orsoncharts.fx.Chart3DCanvas;
import com.orsoncharts.fx.Chart3DViewer;
import com.orsoncharts.plot.XYZPlot;
import com.orsoncharts.renderer.xyz.ScatterXYZRenderer;
import com.orsoncharts.renderer.xyz.SurfaceRenderer;
import com.orsoncharts.renderer.xyz.XYZRenderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import static java.lang.Double.NaN;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jblas.DoubleMatrix;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
//import org.jfree.chart.axis.NumberAxis;
//import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.fx.FXGraphics2D;


/**
 *
 * @author qingzhi
 */
public class Plot {
    private String windowTitle="Plot window";
    private String title="title";
    private String xAxis="xAxis";
    private String yAxis="yAxis";
    private String zAxis="zAxis";
    private boolean m_bShowFitting=true;
    private final int m_iPlotPointNum=40;
    private RegressionModel rm;
    private double[] clusterRange=new double[4];
    private boolean m_bPrintR2=true;
    //private Cluster[] cluster;
    public void scatter2DWindowFX(Points X, Points Y){
    
        Stage stage = new Stage();
        stage.setTitle(windowTitle);
        final NumberAxis xAxis = new NumberAxis(4, 8, 1);
        final NumberAxis yAxis = new NumberAxis(0, 8, 1);
        final ScatterChart<Number,Number> sc = new
            ScatterChart<>(xAxis,yAxis);
        xAxis.setLabel(this.xAxis);                
        yAxis.setLabel(this.yAxis);
        sc.setTitle(title);
        
        //XYChart.Series[] series = new XYChart.Series[1];
        XYChart.Series series = new XYChart.Series();
        
        
        //for(int i=0;i<clusters.length;i++){
            
            //series[i] = new XYChart.Series();
            //series[i].setName("Group "+i);
            
            for(int j=0;j<Y.getPointNum();j++){
                series.getData().add(new XYChart.Data(X.get(j).get(0),Y.get(j).get(0)));
            }
            sc.getData().add(series);
        //}
        
        //GridPane grid=new GridPane();
        stage.setScene(new Scene(sc, 750, 750));
        stage.show();
    }
    
    public void scatter3DWindow(Points X,Points Y){
        Stage stage = new Stage();
        stage.setTitle(title);
        XYZSeriesCollection xyzCollection=new XYZSeriesCollection();
        XYZSeries xyzSeries=new XYZSeries(title);
        int numSize=Y.getPointNum();
        for(int i=0;i<numSize;i++){
            xyzSeries.add(X.get(i).get(0), X.get(i).get(1), Y.get(i).get(0));
        }
        xyzCollection.add(xyzSeries);
        
        Chart3D chart=Chart3DFactory.createScatterChart(title, 
                title, xyzCollection, xAxis, yAxis, zAxis);
        Chart3DViewer viewer=new Chart3DViewer(chart);
        
        stage.setScene(new Scene(viewer, 750, 750));
        stage.show();
    }
    
    
    public void scatter3DWindow(Cluster[] cluster){
        Stage stage = new Stage();
        stage.setTitle(title);
        
        ValueAxis3D xAxis3D=new NumberAxis3D(xAxis);
        ValueAxis3D yAxis3D=new NumberAxis3D(yAxis);
        ValueAxis3D zAxis3D=new NumberAxis3D(zAxis);
        
        
        //XYPlot plt=new XYPlot();

        /* SETUP Scatter */
       // Create the scatter data, renderer, and axis
        //XYItemRenderer renderer1= new XYLineAndShapeRenderer(false, true);
        //ValueAxis domain1=new org.jfree.chart.axis.NumberAxis("x");
        //ValueAxis range1=new org.jfree.chart.axis.NumberAxis("y");
        
        // Set the scatter data, renderer, and axis into plot
        //plt.setDataset(0, collection1);
        //plt.setRenderer(0, renderer1);
        //plt.setDomainAxis(0, domain1);
        //plt.setRangeAxis(0, range1);
        
        
        

        
  
        
        //JFreeChart chart=ChartFactory.createScatterPlot(title, xAxis, yAxis, 
        //        xyCollection, PlotOrientation.HORIZONTAL, true, true, true);
        
        
       
        
      
        
        
        Chart3D chart=Chart3DFactory.createScatterChart(title, 
                null, get3DScatterPlotData(cluster), xAxis, yAxis, zAxis);
        XYZPlot xyzplt=(XYZPlot ) chart.getPlot();
        
        //XYZRenderer xyzrenderer=new ScatterXYZRenderer();
        //xyzplt.setRenderer(get3DsurfaceRender(cluster));
        //xyzplt.setRenderer(xyzrenderer);
        
               // new XYZPlot(get3DScatterPlotData(cluster), 
               // get3DsurfaceRender(cluster), xAxis3D, yAxis3D, zAxis3D);
        Chart3DViewer viewer=new Chart3DViewer(chart,true);
        
        stage.setScene(new Scene(viewer, 750, 750));
        stage.show();
    }
    public void scatter3DWindow_copy(Cluster[] cluster){
        Stage stage = new Stage();
        stage.setTitle(title);
        
        ValueAxis3D xAxis3D=new NumberAxis3D(xAxis);
        ValueAxis3D yAxis3D=new NumberAxis3D(yAxis);
        ValueAxis3D zAxis3D=new NumberAxis3D(zAxis);
        
        
        //XYPlot plt=new XYPlot();

        /* SETUP Scatter */
       // Create the scatter data, renderer, and axis
        //XYItemRenderer renderer1= new XYLineAndShapeRenderer(false, true);
        //ValueAxis domain1=new org.jfree.chart.axis.NumberAxis("x");
        //ValueAxis range1=new org.jfree.chart.axis.NumberAxis("y");
        
        // Set the scatter data, renderer, and axis into plot
        //plt.setDataset(0, collection1);
        //plt.setRenderer(0, renderer1);
        //plt.setDomainAxis(0, domain1);
        //plt.setRangeAxis(0, range1);
        
        
        

        
  
        
        //JFreeChart chart=ChartFactory.createScatterPlot(title, xAxis, yAxis, 
        //        xyCollection, PlotOrientation.HORIZONTAL, true, true, true);
        
        
       
        
      
        
        
        Chart3D chart=Chart3DFactory.createScatterChart(title, 
                null, get3DScatterPlotData(cluster), xAxis, yAxis, zAxis);
        XYZPlot xyzplt=(XYZPlot ) chart.getPlot();
        xyzplt.setRenderer(get3DsurfaceRender(cluster));
        
               // new XYZPlot(get3DScatterPlotData(cluster), 
               // get3DsurfaceRender(cluster), xAxis3D, yAxis3D, zAxis3D);
        Chart3DViewer viewer=new Chart3DViewer(chart,true);
        
        stage.setScene(new Scene(viewer, 750, 750));
        stage.show();
    }
    
    public void scatter2DWindow(Points X,Points Y){
        Stage stage = new Stage();
        stage.setTitle(title);
        XYSeriesCollection xyCollection=new XYSeriesCollection();
        XYSeries xySeries=new XYSeries(title);
        int numSize=Y.getPointNum();
        for(int i=0;i<numSize;i++){
            xySeries.add(X.get(i).get(0), Y.get(i).get(0));
        }
        xyCollection.addSeries(xySeries);
        
        
        JFreeChart chart=ChartFactory.createScatterPlot(title, xAxis, yAxis, 
                xyCollection, PlotOrientation.HORIZONTAL, true, true, true);

        ChartCanvas canvas = new ChartCanvas(chart);

        StackPane gp=new StackPane();
        gp.getChildren().add(canvas);
        canvas.widthProperty().bind( gp.widthProperty()); 
        canvas.heightProperty().bind( gp.heightProperty());

        stage.setScene(new Scene(gp, 750, 750));
        stage.show();
    }
    
    
    public void scatter2DWindow(Cluster[] cluster){
        Stage stage = new Stage();
        stage.setTitle(title);
        
        XYPlot plt=new XYPlot();

        /* SETUP Scatter */
       // Create the scatter data, renderer, and axis
        XYDataset collection1=get2DScatterPlotData(cluster);
        XYItemRenderer renderer1= new XYLineAndShapeRenderer(false, true);
        ValueAxis domain1=new org.jfree.chart.axis.NumberAxis("x");
        ValueAxis range1=new org.jfree.chart.axis.NumberAxis("y");
        //range1.setInverted(false);
        // Set the scatter data, renderer, and axis into plot
        plt.setDataset(0, collection1);
        plt.setRenderer(0, renderer1);
        plt.setDomainAxis(0, domain1);
        plt.setRangeAxis(0, range1);
        
        // Map the scatter to the first Domain and first Range
        //plt.mapDatasetToDomainAxis(0, 0);
        //plt.mapDatasetToRangeAxis(0, 0);
        
        
        if(m_bShowFitting){
            /* SETUP LINE */
            // Create the line data, renderer, and axis
            XYDataset collection2 = get2DLinePlotData(cluster);
            XYItemRenderer renderer2 = new XYLineAndShapeRenderer(true, false);   // Lines only
            //ValueAxis domain2 = new org.jfree.chart.axis.NumberAxis("Domain2");
            //ValueAxis range2 = new org.jfree.chart.axis.NumberAxis("Range2");
            // Set the line data, renderer, and axis into plot
            plt.setDataset(1, collection2);
            plt.setRenderer(1, renderer2);
            //plt.setDomainAxis(1, domain2);
            //plt.setRangeAxis(1, range2);
            // Map the line to the second Domain and second Range
            //plt.mapDatasetToDomainAxis(1, 1);
            //plt.mapDatasetToRangeAxis(1, 1);
            
            
            //range1.setFixedAutoRange(1);
            //domain1.setFixedAutoRange(20);
            //range1.setAutoRange(m_bShowFitting);
            //domain1.setAutoRange(m_bShowFitting);
            // Map the line to the FIRST Domain and second Range
            plt.mapDatasetToDomainAxis(1, 0);
            plt.mapDatasetToRangeAxis(1, 0);
            
            //range1.setAutoRange(m_bShowFitting);
            //domain1.setAutoRange(m_bShowFitting);
        }
        

        
  
        JFreeChart chart = new JFreeChart("Multi Dataset Chart", 
                JFreeChart.DEFAULT_TITLE_FONT, plt, true);
        
        
        //JFreeChart chart=ChartFactory.createScatterPlot(title, xAxis, yAxis, 
        //        xyCollection, PlotOrientation.HORIZONTAL, true, true, true);
        
        
        XYPlot plot=chart.getXYPlot();
        plot.getRangeAxis().setInverted(false);
        
        Range rangeX=get2DScatterPlotData(cluster).getDomainBounds(true);
        plot.getDomainAxis().setRange(rangeX);
        Range rangeY=get2DScatterPlotData(cluster).getRangeBounds(true);
        plot.getRangeAxis().setRange(rangeY);
        
        
        ChartCanvas canvas = new ChartCanvas(chart);
        
        StackPane gp=new StackPane();
        gp.getChildren().add(canvas);
        canvas.widthProperty().bind( gp.widthProperty()); 
        canvas.heightProperty().bind( gp.heightProperty());

        
        
        stage.setScene(new Scene(gp, 750, 750));
        stage.show();
    }
    
    
    public void autoPlot(Points data){
        Points X=data.getX(); 
        Points Y=data.getY();
        //X.plotPoints();
        //Y.plotPoints();
        int dimension=X.getDimension();
        //System.out.println(haha);
        if(dimension==1)
            scatter2DWindow(X,Y);
        else if(dimension==2)
            scatter3DWindow(X,Y);
        else{
            System.err.println("not 2D or 3D, so can not plot!");
            
        }
    }
    
    public void autoPlot(Cluster[] cluster){
        
        
        
        int xDimension=cluster[0].getCluster().getDimension()-1;
        
        if(xDimension==1)
            scatter2DWindow(cluster);
        else if(xDimension==2)
            
            scatter3DWindow(cluster);
        else{
            System.err.println("not 2D or 3D, so can not plot!");
            
        }
    }
    
    static class ChartCanvas extends Canvas { 
        
        JFreeChart chart;
        
        private FXGraphics2D g2;
        
        public ChartCanvas(JFreeChart chart) {
            this.chart = chart;
            this.g2 = new FXGraphics2D(getGraphicsContext2D());
            // Redraw canvas when size changes.
            widthProperty().addListener(e -> draw()); 
            heightProperty().addListener(e -> draw()); 
        }  
        
        private void draw() { 
            double width = getWidth(); 
            double height = getHeight();
            getGraphicsContext2D().clearRect(0, 0, width, height);
            this.chart.draw(this.g2, new Rectangle2D.Double(0, 0, width, 
                    height));
        } 
        
        @Override 
        public boolean isResizable() { 
            return true;
        }  
        
        @Override 
        public double prefWidth(double height) { return getWidth(); }  
        
        @Override 
        public double prefHeight(double width) { return getHeight(); } 
    }
    
    public XYSeriesCollection get2DScatterPlotData(Cluster[] cluster){
        int numCluster=cluster.length;
        // Initialize Scatter plot data    
        XYSeriesCollection xyCollection=new XYSeriesCollection();
        XYSeries[] xySeries=new XYSeries[numCluster];
        for(int j=0;j<numCluster;j++){
            xySeries[j]=new XYSeries("group "+j);
            for(int i=0;i<cluster[j].getCluster().getPointNum();i++){
                xySeries[j].add(cluster[j].getCluster().getX().get(i).get(0), 
                                cluster[j].getCluster().getY().get(i).get(0));
                
            }
             xyCollection.addSeries(xySeries[j]);
             
        }
        
        XYSeries centroidGroup=new XYSeries("Centroids");
        for(int i=0;i<cluster.length;i++){
            centroidGroup.add(cluster[i].getCentroid().get(0),
                              cluster[i].getCentroid().get(1));
        }
        
        xyCollection.addSeries(centroidGroup);
        
        return xyCollection;
    }
    
    public XYSeriesCollection get2DLinePlotData(Cluster[] cluster){
        int numCluster=cluster.length;
        // Initialize Scatter plot data    
        XYSeriesCollection xyCollection=new XYSeriesCollection();
        XYSeries[] xySeries=new XYSeries[numCluster];
        for(int j=0;j<numCluster;j++){
            xySeries[j]=new XYSeries("group "+j);
            
            Point[] range=cluster[j].getRange();
            double min=range[0].get(0);
            double max=range[0].get(1);
            double inteval=(max-min)/m_iPlotPointNum;
            //RegressionModel rm=new RegressionModel(cluster[j].getCluster().getX().toArray(), 
            //                                    cluster[j].getCluster().getY().toVector() );
            //rm.run();
            cluster[j].runRegression();
            for(int i=0;i<m_iPlotPointNum;i++){
                //double [] xFitData=new double[1];
                double xFitData=min+i*inteval;;
                //xFitData[0]=min+i*inteval;
                Point pt=new Point();
                pt.add(xFitData);
                xySeries[j].add(min+i*inteval, cluster[j].fit(pt));
                //                rm.fitFunction(xFitData));
                
            }
            if(m_bPrintR2)
                System.out.println("R2 of Cluster "+j+" is "+cluster[j].getR2());
                //System.out.println("R2 of Cluster "+j+" is "+rm.getR2());
             xyCollection.addSeries(xySeries[j]);
        }
        
                
        return xyCollection;
    }
    
    public XYZSeriesCollection get3DScatterPlotData(Cluster[] cluster){
        int numCluster=cluster.length;
        XYZSeriesCollection xyzCollection=new XYZSeriesCollection();
        XYZSeries[] xyzSeries=new XYZSeries[numCluster];
        
        for(int i=0;i<numCluster;i++){
            xyzSeries[i]=new XYZSeries("Group "+cluster[i].getID());
            
            for(int j=0;j<cluster[i].getCluster().getPointNum();j++){
                xyzSeries[i].add(cluster[i].getCluster().getX().get(j).get(0),
                                 cluster[i].getCluster().getX().get(j).get(1), 
                                 cluster[i].getCluster().getY().get(j).get(0));
            }
            xyzCollection.add(xyzSeries[i]);
        }
        
        
        XYZSeries centroidGroup=new XYZSeries("Centroids");
        for(int i=0;i<cluster.length;i++){
            centroidGroup.add(cluster[i].getCentroid().get(0),
                              cluster[i].getCentroid().get(1),
                              cluster[i].getCentroid().get(2)
            );
            
            if(m_bPrintR2){
                cluster[i].runRegression();
                System.out.println("R2 of Cluster "+i+" is "+cluster[i].getR2());
                //System.out.println("R2 of Cluster "+j+" is "+rm.getR2());
            }
                
                
             
        }
        
        xyzCollection.add(centroidGroup);
        
        return xyzCollection;
    }
    
    public SurfaceRenderer get3DsurfaceRender(Cluster[] cluster){
        
        rm=new RegressionModel(cluster[0].getCluster().getX().toArray(),
                                            cluster[0].getCluster().getY().toVector());
        //clusterRange[0]=(cluster[0].getRange())[0].get(0);
        //clusterRange[1]=(cluster[0].getRange())[0].get(1);
        //clusterRange[2]=(cluster[0].getRange())[1].get(0);
        //clusterRange[3]=(cluster[0].getRange())[1].get(1);
        rm.run();
        DoubleMatrix beta=rm.getBeta();
        Function3D tf=new FittingFunctionFor3DPlot(beta);
        SurfaceRenderer sr = new SurfaceRenderer(tf);
        return sr;
    }
    
    /*
    public double fittingFunction(double x,double z){
        double[] xz=new double[2];
        double result=NaN;
        xz[0]=x;
        xz[1]=z;
        if(x>=clusterRange[0] && x<=clusterRange[1] &&
               z>=clusterRange[2] && z<=clusterRange[3] )
            result=rm.functionPolynominalMD2P(xz);
        return result;
    }
    */
    public void showFittingLine(boolean b){
        m_bShowFitting=b;
    }
    
    public void showR2(boolean b){
        m_bPrintR2=b;
    }
    
}
