/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject;




import com.teamProject.plot.Plot;
import com.teamProject.data.dataContainer;
import com.teamProject.cluster.KMeans;
import com.teamProject.regression.Fitting;
import com.teamProject.regression.LR;
import com.teamProject.regression.RegressionModel;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//import net.sf.javaml.core.Dataset;


/**
 *
 * @author qingzhi
 */
public class App extends Application {

    dataContainer dc=new dataContainer();
    Plot plt;
    KMeans km;
    RegressionModel rm;

    //@Override;
    public void start(Stage primaryStage){

        GridPane root = new GridPane();
        Scene scene = new Scene(root, 700, 450);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        //root.add(dimensionBox(),0,0);
        
       
        Button btnCSV=new Button("read CSV");
        btnCSV.setOnAction((ActionEvent e)->{
            Record2File.deleteFile();
            dc.readCSV("OnlineNewsPopularity.csv",",");
            
            //the 2,3,4 column of the csv file
            int[] index={2,3,4};
            dc.filterData(index);
            
            
            km=new KMeans(dc.getRowsBefore(25000),30); 
            km.runClusters();
            
            
            

            LR lr=new LR(km.getClusters());
            //lr.run();
            Thread threadLR=new Thread(lr);
            threadLR.run();
            
            
            plt=new Plot();
            plt.showFittingLine(true);
            plt.showR2(false);
            plt.plot(km.getClusters());
            
            
            Fitting ft=new Fitting(km.getClusters());
            ft.showValidateInformation(false);
            ft.validate(dc.getRowsBetween(2500,3500));
            Record2File.out("NRMSE is "+ft.getValidateNRMSE());
            Record2File.out("RMSE is "+ft.getValidateRMSE());
            
        });
        
        
        root.add(btnCSV,0,1);
        
        root.add(evaludateK(),0,2);
        

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
                dc.readFile("iris1.data",",");
                //dc.filterData();
                km=new KMeans(dc.getRowsBefore(140),5); 
                km.runClusters();
                
               
                rm=new RegressionModel(km.getClusters());
                rm.showBeta(true);
                rm.run();
                
                plt=new Plot();
                plt.showFittingLine(false);
                plt.showR2(false);
                plt.plot(km.getClusters());
           
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
    
    
    private HBox evaludateK(){
        HBox hb=new HBox();
        TextField txtKNum=new TextField("2");
        TextField txtLineNum=new TextField("2500");
        
        Button btn=new Button("evaluate K");
        btn.setOnAction((ActionEvent)->{
            Record2File.deleteFile();
            dc.readCSV("OnlineNewsPopularity.csv",",");
            
            //the 2,3,4 column of the csv file
            int[] index={2,3};
            dc.filterData(index);
            
            int grpNum=Integer.parseInt(txtKNum.getText());
            int lineNum=Integer.parseInt(txtLineNum.getText());
            km=new KMeans(dc.getRowsBefore(lineNum),grpNum); 
            km.runClusters();
            km.printCentroids();
            
            plt=new Plot();
            plt.showFittingLine(false);
            plt.plot(km.getClusters());
            
        });
        txtKNum.setPrefWidth(70);
        txtLineNum.setPrefWidth(100);
        Label lblKNum=new Label("Cluster number: ");
        Label lblLineNum=new Label("Rows number: ");
        
        
        
        
        hb.getChildren().addAll(lblKNum,txtKNum,lblLineNum,txtLineNum,btn);
        return hb;
        
    }


    
}
