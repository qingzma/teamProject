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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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


        //root.add(btnRead,0,5);
        root.add(dimensionBox(),0,0);

        //root.add(wekaPane(),0,7);
        //root.add(orsonChart(),0,2);
        
/*        
        
        
        
 */
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
                km=new KMeans(dc.getRowsBefore(140),5);
                
                km.runClusters();
                
                
                plt.showFittingLine(true);
                plt.showR2(false);
                plt.autoPlot(km.getClusters());
           
                Fitting ft=new Fitting(km.getClusters());
                ft.showValidateInformation(false);
                ft.validate(dc.getRowsAfter(140));
                System.out.println("NRMSE is : "+ft.getValidateNRMSE());
                System.out.println("RMSE  is : "+ft.getValidateRMSE());
                //System.out.println(ft.getValidateTMSE());


            }
            catch (NumberFormatException e){
                System.err.println("data Width inserted is not correct!");
            }
        });
        HBox hbDimension=new HBox();
        hbDimension.getChildren().addAll(lblDimension,tfWidth,btnDimension);
        return hbDimension;
    }

    
}
