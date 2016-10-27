/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject;




import javafx.application.Application;
import javafx.event.ActionEvent;
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
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.IChartComponentFactory;
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

        
        Button btn=new Button("FX");
        btn.setOnAction((ActionEvent)->{
            showFX();
    });

        //root.add(btnRead,0,5);
        root.add(dimensionBox(),0,0);
        root.add(btn, 0, 1);
        //root.add(wekaPane(),0,7);
        //root.add(orsonChart(),0,2);
        
        
        
        // Jzy3d
        JavaFXChartFactory factory = new JavaFXChartFactory();

        AWTChart chart = (AWTChart) factory.newChart(Quality.Advanced, IChartComponentFactory.Toolkit.offscreen);
        
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
        
        chart.add(surface);
        ImageView imageView=factory.bindImageView(chart);
        
        root.add(imageView, 0, 5);
        factory.addSceneSizeChangedListener(chart, scene);
        
        
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

    private void showFX(){
        Stage newStage=new Stage();
        newStage.setTitle("FX Demo");
        newStage.setHeight(600);
        newStage.setWidth(1000);
        
         // Jzy3d
        JavaFXChartFactory factory = new JavaFXChartFactory();
        AWTChart chart  = getDemoChart(factory, "offscreen");
        ImageView imageView = factory.bindImageView(chart);
        
        GridPane pane=new GridPane();
        Scene scene=new Scene(pane);
        pane.add(imageView, 0, 0);
        
        newStage.setScene(scene);
        newStage.show();
    }

    private AWTChart getDemoChart(JavaFXChartFactory factory, String toolkit) {
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
        AWTChart chart =  (AWTChart) factory.newChart(quality, toolkit);
        chart.getScene().getGraph().add(surface);
        return chart;

      
  }
}
