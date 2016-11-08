/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject;




import com.teamProject.plot.Plot;
import com.teamProject.data.dataContainer;
import com.teamProject.cluster.KMeans;
import com.teamProject.data.Point;
import com.teamProject.data.PointInt;
import com.teamProject.regression.Fitting;
import com.teamProject.regression.LR;
import com.teamProject.regression.RegressionModel;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private int[] index;        // store the columns that need to read from file.

    //@Override;
    public void start(Stage primaryStage){

        GridPane root = new GridPane();
        Scene scene = new Scene(root, 900, 800);
        
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
            
            
            km=new KMeans(dc.getRowsBefore(2500),1); 
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
        
        root.add(ContainerPane(),0,3);
        

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
        TextField txtDimension=new TextField("2");
        TextField txtTimeCost=new TextField("time cost");
        
        Button btn=new Button("evaluate K");
        btn.setOnAction((ActionEvent ActionEvent)->{
            Record2File.deleteFile();
            dc.readCSV("OnlineNewsPopularity.csv",",");
            
            //the 2,3,4 column of the csv file
            
            int dimensionNum=Integer.parseInt( txtDimension.getText() );
            int[] index=new int[dimensionNum];
            for(int i=0;i<dimensionNum;i++){
                index[i]=i+1;
            }
                        
            
            dc.filterData(index);
            
            int grpNum=Integer.parseInt(txtKNum.getText());
            int lineNum=Integer.parseInt(txtLineNum.getText());
            km=new KMeans(dc.getRowsBefore(lineNum),grpNum); 
            km.runClusters();
            km.printCentroids();
            txtTimeCost.setText("time cost is "+Record2File.double2str(  km.timeCost()   ));
            
            
            plt=new Plot();
            plt.showFittingLine(false);
            //plt.plot(km.getClusters());
            
        });
        txtKNum.setPrefWidth(70);
        txtLineNum.setPrefWidth(100);
        txtDimension.setPrefWidth(80);
        Label lblKNum=new Label("Cluster number: ");
        Label lblLineNum=new Label("Rows number: ");
        Label lblDimension=new Label("Dimension: ");
        
        
        
        
        hb.getChildren().addAll(lblKNum,txtKNum,lblLineNum,txtLineNum,
                lblDimension,txtDimension,btn,txtTimeCost);
        return hb;
        
    }

    
    private GridPane ContainerPane(){
        GridPane grid =new GridPane();
        grid.add(readFilePane(), 0, 0);
        grid.add(clusterPane(), 0, 1);
        grid.add(regressionPane(), 0, 2);
        return grid;
    }
    
    
    private VBox readFilePane(){
        VBox vb=new VBox();
        HBox hb=new HBox();
        //C, D, E, G, H, I, M, U, and V
        Label lblColumns=new Label("please type in the columns you"
                + " want to read: ");
        TextField tfColumns=new TextField("2 3 4");
        
        Button btnReadCSV=new Button("Read csv file");
        btnReadCSV.setOnAction((ActionEvent )->{
            
            Scanner s=new Scanner(tfColumns.getText());
            
            ArrayList<Integer> indexList=new ArrayList<>();
            while(s.hasNext()){
                indexList.add(Integer.parseInt(s.next()));
            }
            PointInt IndexPt=new PointInt(indexList);
            
            Record2File.deleteFile();
            dc.readCSV("OnlineNewsPopularity.csv",",");
            System.out.println(indexList.toArray());
            index=IndexPt.toArray();
            dc.filterData(index);  
        });
        
        hb.getChildren().addAll(lblColumns,tfColumns);
        vb.getChildren().addAll(hb,btnReadCSV);
        return vb;
    }

    private VBox clusterPane(){
        VBox vb=new VBox();
        HBox hb=new HBox();
        TextField txtKNum=new TextField("2");
        TextField txtLineNum=new TextField("2500");
        //TextField txtDimension=new TextField("2");
        TextField txtTimeCost=new TextField("time cost");
        
        Button btn=new Button("Cluster");
        btn.setOnAction((ActionEvent ActionEvent)->{
            Record2File.deleteFile();
            dc.readCSV("OnlineNewsPopularity.csv",",");
            
            //the 2,3,4 column of the csv file
            
         /*   int dimensionNum=Integer.parseInt( txtDimension.getText() );
            int[] index=new int[dimensionNum];
            for(int i=0;i<dimensionNum;i++){
                index[i]=i+1;
            }
          */              
            
            dc.filterData(index);
            
            int grpNum=Integer.parseInt(txtKNum.getText());
            int lineNum=Integer.parseInt(txtLineNum.getText());
            km=new KMeans(dc.getRowsBefore(lineNum),grpNum); 
            km.runClusters();
            km.printCentroids();
            txtTimeCost.setText("time cost is "+Record2File.double2str(  km.timeCost()   ));
            
            
            plt=new Plot();
            plt.showFittingLine(false);
            //plt.plot(km.getClusters());
            
        });
        txtKNum.setPrefWidth(70);
        txtLineNum.setPrefWidth(100);
        
        Label lblKNum=new Label("Cluster number: ");
        Label lblLineNum=new Label("Rows number: ");
        
        
        
        
        
        hb.getChildren().addAll(lblKNum,txtKNum,lblLineNum,txtLineNum);
        
        HBox hb1=new HBox();
        hb1.getChildren().addAll(btn,txtTimeCost);
        
        
        vb.getChildren().addAll(hb,hb1);
        return vb;
    }
    
    
    private VBox regressionPane(){
        VBox vb=new VBox();
        Label lblLR=new Label("Linear Regression");
        Button btnLR=new Button("Run Regression");
        btnLR.setOnAction((ActionEvent)->{
            LR lr=new LR(km.getClusters());
            //lr.run();
            Thread threadLR=new Thread(lr);
            threadLR.start();
            
            
            plt=new Plot();
            plt.showFittingLine(true);
            plt.showR2(false);
            plt.plot(km.getClusters());
        });
        
        vb.getChildren().addAll(lblLR,btnLR);
        return vb;
    }
}
