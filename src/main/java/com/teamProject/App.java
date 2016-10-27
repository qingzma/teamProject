/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject;




import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

//import net.sf.javaml.core.Dataset;


/**
 *
 * @author qingzhi
 */
public class App extends Application {

    dataContainer dc=new dataContainer();
    Plot plt=new Plot();
    KMeans km;
    RegressionModel rm;

    //@Override;
    public void start(Stage primaryStage){

        GridPane root = new GridPane();
        Scene scene = new Scene(root, 700, 450);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);

        //Button  btnRead=new Button("Read File");
        //btnRead.setOnAction((ActionEvent e)->{
        
        
        
        /*
        Cluster[] c=km.getClusters();
        for(int i=0;i<km.getClusters().length;i++){
            rm=new RegressionModel(c[i].getCluster().getX().toArray(),
                                   c[i].getCluster().getY().toVector());
            if(c[i].getCluster().getX().getDimension()==1)
                rm.polynominal1D(4);
            else if(c[i].getCluster().getX().getDimension()>=1)
                rm.polynominalMD2P();
            else{
                System.out.println("error detected!");
                System.exit(1);
            }
            
            System.out.println("R2 of cluster "+i+" is "+rm.getR2());
            //System.out.println("Beta "+i+" is "+rm.getBeta());
        }
        
        c[0].getRange();
           
        */
            /*
            double[][] X=dc.getX().toArray();
            double[] Y=dc.getY().toVector();
            rm=new RegressionModel(X, Y);
            rm.polynominal1D(5);
            */
            
            /*
            Point p1=new Point();
            p1.add(1);
            p1.add(3);
            Point p2=new Point();
            p2.add(2);
            p2.add(6);
            Point p3=new Point();
            p3.add(3);
            p3.add(9);
            Points pts=new Points();
            pts.add(p1);
            pts.add(p2);
            pts.add(p3);
            
            rm=new RegressionModel(pts.getX().toArray(), pts.getY().toVector());
            rm.polynominal1D(0);
            */
            
            
            
            
             /*
            Point p1=new Point();
            p1.add(1);
            p1.add(0.5);
            p1.add(0.25);
            p1.add(3);
            Point p2=new Point();
            p2.add(1);
            p2.add(1);
            p2.add(1);
            p2.add(7);
            Point p3=new Point();
            p3.add(1);
            p3.add(1);
            p3.add(2);
            p3.add(11);
            Point p4=new Point();
            p4.add(2);
            p4.add(1);
            p4.add(1);
            p4.add(8);
            Points pts=new Points();
            pts.add(p1);
            pts.add(p2);
            pts.add(p3);
            pts.add(p4);
            
            rm=new RegressionModel(pts.getX().toArray(), pts.getY().toVector());
            rm.polynominalMD1P();
            System.out.println("R2 is "+rm.getR2());
            System.out.println("Beta "+" is "+rm.getBeta());
            */
           
       // });
        
        
        
        

        //root.add(btnRead,0,5);
        root.add(dimensionBox(),0,4);
        //root.add(wekaPane(),0,7);
        //root.add(orsonChart(),0,2);
        
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
   
    
    private HBox dimensionBox(){
        Label lblDimension=new Label("please type in the number of columns to read: ");
        TextField tfWidth=new TextField("3");
        tfWidth.setPrefSize(60, 12);
        Button btnDimension=new Button("OK");
        btnDimension.setOnAction((ActionEvent)->{
            try{
                dc.setDataWidth(Integer.parseInt(tfWidth.getText()));
                dc.readFile(",");
                //dc.filterData();
                km=new KMeans(dc.getRowsBefore(140),6);
                
                km.runClusters();
                
                plt.showFittingLine(true);
                plt.showR2(false);
                plt.autoPlot(km.getClusters());
           
                Fitting tf=new Fitting(km.getClusters());
                tf.showValidateInformation(false);
                tf.validate(dc.getRowsAfter(140));
                System.out.println(tf.getValidateNRMSE());
                System.out.println(tf.getValidateRMSE());
                System.out.println(tf.getValidateTMSE());
                //runFX(new Stage());
                //runFX();

            }
            catch (NumberFormatException e){
                System.err.println("data Width inserted is not correct!");
            }
        });
        HBox hbDimension=new HBox();
        hbDimension.getChildren().addAll(lblDimension,tfWidth,btnDimension);
        return hbDimension;
    }
/*
     public void runFX() {
         Stage stage=new Stage();
        stage.setTitle(DemoJzy3dFX.class.getSimpleName());
        
        // Jzy3d
        JavaFXChartFactory factory = new JavaFXChartFactory();
        AWTChart chart  = getDemoChart(factory, "offscreen");
        ImageView imageView = factory.bindImageView(chart);

        // JavaFX
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        pane.getChildren().add(imageView);

        factory.addSceneSizeChangedListener(chart, scene);
        
        stage.setWidth(500);
        stage.setHeight(500);
    }

    private AWTChart getDemoChart(JavaFXChartFactory factory, String toolkit) {
        // -------------------------------
        // Define a function to plot
        Mapper mapper = new Mapper() {
            @Override
            public double f(double x, double y) {
                return x * Math.sin(x * y);
            }
        };

        // Define range and precision for the function to plot
        Range range = new Range(-3, 3);
        int steps = 80;

        // Create the object to represent the function over the given range.
        final Shape surface = Builder.buildOrthonormal(mapper, range, steps);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);

        // -------------------------------
        // Create a chart
        Quality quality = Quality.Advanced;
        //quality.setSmoothPolygon(true);
        //quality.setAnimated(true);
        
        // let factory bind mouse and keyboard controllers to JavaFX node
        AWTChart chart = (AWTChart) factory.newChart(quality, toolkit);
        chart.getScene().getGraph().add(surface);
        return chart;
    }
  */
}
