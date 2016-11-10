/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.plot;


import com.sun.javafx.scene.control.skin.CustomColorDialog;
import com.teamProject.Record2File;
import com.teamProject.cluster.Cluster;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
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
import org.jzy3d.chart.AWTChart;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import com.teamProject.data.Points;
import com.teamProject.data.Point;
import com.teamProject.data.dataContainer;
import com.teamProject.jzy3dFx.JavaFXChartFactory;
import static com.teamProject.jzy3dFx.JavaFXChartFactory.chart;
import com.teamProject.regression.RegressionInterface;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;



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
    private static boolean m_bShowFitting=false;
    private static boolean m_bShowPoints=true;
    private final int m_iPlotPointNum=20;
    //private RegressionModel rm;
    private double[] clusterRange=new double[4];
    //private Cluster[] cluster;
    private double t0,t1;
    JavaFXChartFactory factory;
    AWTChart chart;
    XYPlot plt;
    Shape[] surfaces;
    
    
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
    
    
    

    public void scatter3DWindowJzy3d(Cluster[] cluster){
        Stage stage = new Stage();
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setTitle(title);
        GridPane grid=new GridPane();
        Scene scene=new Scene(grid);
        

        
        // Jzy3d
        JavaFXChartFactory factory = new JavaFXChartFactory();

        AWTChart chart = (AWTChart) factory.newChart(Quality.Advanced, IChartComponentFactory.Toolkit.offscreen);
        
        
        
        //chart.add(surface);
        Scatter[] scatters=getJzy3dScatterPoints(cluster);
        for(int i=0;i<scatters.length;i++){
            scatters[i].setWidth(6);
            Random rand=new Random();
            Color color=new Color(rand.nextInt(255), rand.nextInt(255), 
                rand.nextInt(255), 200);
            scatters[i].setColor(color);
            chart.add(scatters[i]);
        }
        
        
        
        
        
        
        //JavaFXChartFactory factory = new JavaFXChartFactory();
        //AWTChart chart  = getDemoChart(factory, "offscreen");
        
        ImageView imageView = factory.bindImageView(chart);
        
        
        
        grid.add(imageView, 0, 0);
        //grid.add(showFitSurfaceButton(),1,1);
        
        stage.setScene(scene);
        stage.show();
        factory.addSceneSizeChangedListener(chart, scene);
        
        t1=System.currentTimeMillis();
        
    
 
        
    }
    
    public void scatter3DWindowJzy3d(Cluster[] cluster,RegressionInterface ri){
        Stage stage = new Stage();
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setTitle(title);
        GridPane grid=new GridPane();
        Scene scene=new Scene(grid);
        
        
        
        // Jzy3d
        factory = new JavaFXChartFactory();

        chart = (AWTChart) factory.newChart(Quality.Advanced, IChartComponentFactory.Toolkit.offscreen);
        
        
        
        
        
        //chart.add(scatters);
        Scatter[] scatters=getJzy3dScatterPoints(cluster);
        for(int i=0;i<scatters.length;i++){
            scatters[i].setWidth(6);
            Random rand=new Random();
            Color color=new Color(rand.nextInt(255), rand.nextInt(255), 
                rand.nextInt(255), 200);
            scatters[i].setColor(color);
            chart.add(scatters[i]);
        }
        
        
        
        
        if(m_bShowFitting){
            surfaces=getJzy3dSurface(cluster,ri);
                for(int i=0;i<surfaces.length;i++){
                    chart.getScene().getGraph().add(surfaces[i]);
                }
        }
        
        
        
        HBox hb=new HBox();
        hb.getChildren().addAll(showPointsButton(scatters),showFitSurfaceButton(cluster,ri));
        
        
        
        
        
        //JavaFXChartFactory factory = new JavaFXChartFactory();
        //AWTChart chart  = getDemoChart(factory, "offscreen");
        
        ImageView imageView = factory.bindImageView(chart);
        
        
        
        grid.add(imageView, 0, 0);
        grid.add(hb,0,1);
        
        stage.setScene(scene);
        stage.show();
        factory.addSceneSizeChangedListener(chart, scene);
        
        t1=System.currentTimeMillis();
        
    
 
        
    }
    
    public Button showFitSurfaceButton(Cluster[] clusters,RegressionInterface ri){
        Button btn=new Button();
        if(m_bShowFitting)
            btn.setText("hide fitting");
        else
            btn.setText("show fitting");
        
        
        
        if(surfaces==null){
           surfaces=getJzy3dSurface(clusters,ri); 
        }
        
        btn.setOnAction((ActionEvent) -> {
            
            if(!m_bShowFitting){
                btn.setText("hide fitting");
                m_bShowFitting=true;
                // Jzy3d
                factory = new JavaFXChartFactory();
                
                
                for(int i=0;i<surfaces.length;i++){
                    chart.getScene().getGraph().add(surfaces[i]);
                }
                
            }
            else{
                btn.setText("show fitting");
                m_bShowFitting=false;
                for(int i=0;i<surfaces.length;i++){
                    
                    chart.getScene().getGraph().remove(surfaces[i]);
                }
            }
        });
        //hb.getChildren().add(btn);
        return btn;
    }
    
    
    
    public Button showPointsButton(Scatter[] scatters){
        //HBox hb=new HBox();
        Button btn=new Button("show points");
        //Shape[] surfaces=getJzy3dSurface(clusters,ri);
        btn.setOnAction((ActionEvent) -> {
            
            if(!m_bShowPoints){
                btn.setText("hide points");
                m_bShowPoints=true;
                // Jzy3d
                factory = new JavaFXChartFactory();
                
                
                for(int i=0;i<scatters.length;i++){
                    scatters[i].setWidth(6);
                    Random rand=new Random();
                    Color color=new Color(rand.nextInt(255), rand.nextInt(255), 
                        rand.nextInt(255), 200);
                    scatters[i].setColor(color);
                    //chart.add(scatters[i]);
                    chart.getScene().getGraph().add(scatters[i]);
                }
                
            }
            else{
                m_bShowPoints=false;
                btn.setText("show points");
                for(int i=0;i<scatters.length;i++){
                /*    scatters[i].setWidth(6);
                    Random rand=new Random();
                    Color color=new Color(rand.nextInt(255), rand.nextInt(255), 
                        rand.nextInt(255), 200);
                    scatters[i].setColor(color);*/
                    //chart.add(scatters[i]);
                    chart.getScene().getGraph().remove(scatters[i]);
                }
            }
        });
        //hb.getChildren().add(btn);
        return btn;
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
        t1=System.currentTimeMillis();
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
        t1=System.currentTimeMillis();
    }
    
    public void scatter2DWindow(Cluster[] cluster,RegressionInterface ri){
        Stage stage = new Stage();
        stage.setTitle(title);
        
        plt=new XYPlot();

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
        

        if(m_bShowFitting){
            /* SETUP LINE */
            // Create the line data, renderer, and axis
            XYDataset collection2 = get2DLinePlotData(cluster,ri);                   ///////////
            XYItemRenderer renderer2 = new XYLineAndShapeRenderer(true, false);   // Lines only
            //ValueAxis domain2 = new org.jfree.chart.axis.NumberAxis("Domain2");
            //ValueAxis range2 = new org.jfree.chart.axis.NumberAxis("Range2");
            // Set the line data, renderer, and axis into plot
            plt.setDataset(1, collection2);                  //////////////
            plt.setRenderer(1, renderer2);
            //plt.setDomainAxis(1, domain2);

            plt.mapDatasetToDomainAxis(1, 0);
            plt.mapDatasetToRangeAxis(1, 0);
            
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
        
        StackPane sp=new StackPane();
        sp.getChildren().add(canvas);
        //sp.setPrefSize(600, 600);
        canvas.widthProperty().bind( sp.widthProperty()); 
        canvas.heightProperty().bind( sp.heightProperty());

        
        //sp.getChildren().add(sp);
        
        
        
        stage.setScene(new Scene(sp, 750, 750));
        stage.show();
        t1=System.currentTimeMillis();
    }

    
    public void plot(Cluster[] cluster){
        Record2File.out("Plot starting...");
        t0=System.currentTimeMillis();
        
        int xDimension=cluster[0].getCluster().getDimension()-1;
        
        if(xDimension==1)
            scatter2DWindow(cluster);
        else if(xDimension==2)
            scatter3DWindowJzy3d(cluster);
            //scatter3DWindow(cluster);
            
        else{
            System.err.println("not 2D or 3D, so can not plot!");
            
        }
        t1=System.currentTimeMillis();
        
        Record2File.out("Plot ends.");
        printTimeCost();
        Record2File.out("\n");
    }
    
    public void plot(Cluster[] cluster,RegressionInterface ri){
        Record2File.out("Plot starting...");
        t0=System.currentTimeMillis();
        
        int xDimension=cluster[0].getCluster().getDimension()-1;
        
        if(xDimension==1)
            scatter2DWindow(cluster,ri);
        else if(xDimension==2)
            scatter3DWindowJzy3d(cluster,ri);
            //scatter3DWindow(cluster);
            
        else{
            System.err.println("not 2D or 3D, so can not plot!");
            
        }
        t1=System.currentTimeMillis();
        
        Record2File.out("Plot ends.");
        printTimeCost();
        Record2File.out("\n");
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
    
    public XYSeriesCollection get2DLinePlotData(Cluster[] cluster,RegressionInterface ri){
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
            ///////////////////////////////////////////////////////////////////////
            //cluster[j].runRegression();
            for(int i=0;i<m_iPlotPointNum;i++){
                //double [] xFitData=new double[1];
                double xFitData=min+i*inteval;;
                //xFitData[0]=min+i*inteval;
                Point pt=new Point();
                pt.add(xFitData);
                xySeries[j].add(min+i*inteval, ri.fit(pt.toArray(),j));
                //                rm.fitFunction(xFitData));
                
            }
  
             xyCollection.addSeries(xySeries[j]);
        }
        
                
        return xyCollection;
    }
   /**/
    private Scatter[] getJzy3dScatterPoints(Cluster[] cluster){
        int numCluster=cluster.length;
        Scatter[] scatters=new Scatter[numCluster+1];
        Coord3d[] points;
        Color[]   colors;
        float a;
        //XYZSeriesCollection xyzCollection=new XYZSeriesCollection();
        //XYZSeries[] xyzSeries=new XYZSeries[numCluster];
        
        //Color clors[]=new Color[numCluster+1];
        
        Random rand=new Random();
        Color color=new Color(rand.nextInt(255), rand.nextInt(255), 
                rand.nextInt(255), 200);
        
        for(int i=0;i<numCluster;i++){
            //xyzSeries[i]=new XYZSeries("Group "+cluster[i].getID());
            int clusterILength=cluster[i].getCluster().getPointNum();
            points=new Coord3d[clusterILength];
            colors=new Color[clusterILength];
            for(int j=0;j<cluster[i].getCluster().getPointNum();j++){
                points[j]=new Coord3d(cluster[i].getCluster().getX().get(j).get(0),
                                 cluster[i].getCluster().getX().get(j).get(1), 
                                 cluster[i].getCluster().getY().get(j).get(0));
                a=0.25f;
               // colors[j]=new Color((int)(20+30*i),(int)(255-30*i),(int)(100+20*i),
                //                255);
            }
            //scatters[i]=new Scatter(points,colors);
            scatters[i]=new Scatter(points);
            
        }
        
        
        // get data for centroids
        
        
        points=new Coord3d[numCluster];
        colors=new Color[numCluster];
        
        for(int i=0;i<cluster.length;i++){
            points[i]=new Coord3d(cluster[i].getCentroid().get(0),
                              cluster[i].getCentroid().get(1),
                              cluster[i].getCentroid().get(2)
            );
            a=0.25f;
            colors[i]=new Color((float)cluster[i].getCentroid().get(0),
                              (float)cluster[i].getCentroid().get(1),
                              (float)cluster[i].getCentroid().get(2),a
            );
              
        }
        scatters[numCluster]=new Scatter(points);
        //scatters[numCluster]=new Scatter(points,colors);
        

        return scatters;
    }
    
    private Shape[] getJzy3dSurface(Cluster[] cluster){
        int numCluster=cluster.length;     
        Random rand=new Random();
        Mapper mappers[]=new Mapper[numCluster];
        Shape surfaces[]=new Shape[numCluster];
        int steps = 20;
        
        for(int i=0;i<numCluster;i++){
            //rm=new RegressionModel(cluster[i].getCluster().getX().toArray(),
             //                               cluster[i].getCluster().getY().toVector());
            //rm.run();
            
            mappers[i]=new SurfaceFunction(cluster[i]);
            
            
            float min=(float)(cluster[i].getRange()[0].get(0));
            float max=(float)(cluster[i].getRange()[0].get(1));
            org.jzy3d.maths.Range rangex = new org.jzy3d.maths.Range(min,max);
            min=(float)(cluster[i].getRange()[1].get(0));
            max=(float)(cluster[i].getRange()[1].get(1));
            org.jzy3d.maths.Range rangey = new org.jzy3d.maths.Range(min,max);      
            
            
            surfaces[i] = Builder.buildOrthonormal(new 
                OrthonormalGrid(rangex, steps, rangey, steps), mappers[i]);
            
            surfaces[i].setColorMapper(new ColorMapper(new ColorMapRainbow(), 
                    surfaces[i].getBounds().getZmin(), 
                    surfaces[i].getBounds().getZmax(), 
                    new Color(1, 1, 1, .5f)));
            surfaces[i].setFaceDisplayed(true);
            surfaces[i].setWireframeDisplayed(false);
            

            
        }

        

        return surfaces;
    }
    
    private Shape[] getJzy3dSurface(Cluster[] cluster,RegressionInterface ri){
        int numCluster=cluster.length;     
        Random rand=new Random();
        Mapper mappers[]=new Mapper[numCluster];
        Shape surfaces[]=new Shape[numCluster];
        int steps = 20;
        
        for(int i=0;i<numCluster;i++){
            //rm=new RegressionModel(cluster[i].getCluster().getX().toArray(),
             //                               cluster[i].getCluster().getY().toVector());
            //rm.run();
            
            mappers[i]=new SurfaceFunction(i,ri);
            
            
            float min=(float)(cluster[i].getRange()[0].get(0));
            float max=(float)(cluster[i].getRange()[0].get(1));
            org.jzy3d.maths.Range rangex = new org.jzy3d.maths.Range(min,max);
            min=(float)(cluster[i].getRange()[1].get(0));
            max=(float)(cluster[i].getRange()[1].get(1));
            org.jzy3d.maths.Range rangey = new org.jzy3d.maths.Range(min,max);      
            
            
            surfaces[i] = Builder.buildOrthonormal(new 
                OrthonormalGrid(rangex, steps, rangey, steps), mappers[i]);
            
            surfaces[i].setColorMapper(new ColorMapper(new ColorMapRainbow(), 
                    surfaces[i].getBounds().getZmin(), 
                    surfaces[i].getBounds().getZmax(), 
                    new Color(1, 1, 1, .5f)));
            surfaces[i].setFaceDisplayed(true);
            surfaces[i].setWireframeDisplayed(false);
            

            
        }

        

        return surfaces;
    }
    
    public void showFittingLine(boolean b){
        m_bShowFitting=b;
    }
    

    
    public double timeCost(){
        return (t1-t0)/1000.0d;
    }
    
    public void printTimeCost(){
        Record2File.out("Time for "+"plot"+" is "+
                    Record2File.double2str( timeCost()) +"s." );
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
    
/*    public void scatter3DWindow(Cluster[] cluster){
        Stage stage = new Stage();
        stage.setTitle(title);
        
        ValueAxis3D xAxis3D=new NumberAxis3D(xAxis);
        ValueAxis3D yAxis3D=new NumberAxis3D(yAxis);
        ValueAxis3D zAxis3D=new NumberAxis3D(zAxis);
        
        
        //XYPlot plt=new XYPlot();

        //SETUP Scatter 
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
    
*/
    
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
}
