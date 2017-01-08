/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject;




import com.teamProject.cluster.Cluster;
import com.teamProject.plot.Plot;
import com.teamProject.data.dataContainer;
import com.teamProject.cluster.KMeans;
import com.teamProject.data.Point;
import com.teamProject.data.PointInt;
import com.teamProject.data.Points;
import com.teamProject.fit.SimpleValidate;
import com.teamProject.regression.Fitting;
import com.teamProject.regression.GaussianProcessRegression;
import com.teamProject.regression.KNNRegression;
import com.teamProject.regression.LR;
import com.teamProject.regression.RegressionModel;
import com.teamProject.regression.LinearRegression;
import com.teamProject.regression.PolynomialRegression;
import com.teamProject.regression.RegressionInterface;
import com.teamProject.regression.RBF;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    Cluster[] dataDivision;
    private RegressionInterface lr;
    private RegressionInterface nl;
    private RegressionInterface knn;
    private RegressionInterface gps;
    private RegressionInterface rbf;
    private Points evaluations;
    //@Override;
    public void start(Stage primaryStage){

        GridPane root = new GridPane();
        Scene scene = new Scene(root, 900, 800);
        
        primaryStage.setTitle("Team project interface");
        primaryStage.setScene(scene);
        //root.add(dimensionBox(),0,0);
        
       
        
        Record2File.deleteFile();
        dc.readCSV("OnlineNewsPopularity.csv",",");
            
            //the 2,3,4 column of the csv file
        int[] index={2,3,4};
        dc.filterData(index);
        //km=new KMeans(dc.getData(), 5);
        //km=new KMeans(dc.getRowsBefore(25000), 5);
        //km.run();
        
        //dataDivision=km.getClusters();
        /*
        lr=new LinearRegression(dc.getRowsBefore(1600));
        gps=new GaussianProcessRegression(dc.getRowsBetween(660, 1200));
        nl=new PolynomialRegression(dc.getRowsBetween(1200, 1800));
        rbf=new LinearRegression(dc.getRowsBetween(1800, 2400));
        knn=new KNNRegression(dc.getRowsBetween(2400, 3000));
        evaluations=dc.getRowsBefore(33);
        */
        ///*
        lr=new LinearRegression(dc.getRowsBefore(6607));
        gps=new GaussianProcessRegression(dc.getRowsBetween(6607, 13214));
        nl=new PolynomialRegression(dc.getRowsBetween(13214, 19821));
        rbf=new RBF(dc.getRowsBetween(19821, 26428));
        knn=new KNNRegression(dc.getRowsBetween(26428, 33035));
        evaluations=dc.getRowsAfter(33035);
        //*/
        /*
        lr=new KNNRegression(dc.getRowsBefore(6607));
        gps=new KNNRegression(dc.getRowsBetween(6607, 13214));
        nl=new KNNRegression(dc.getRowsBetween(13214, 19821));
        rbf=new KNNRegression(dc.getRowsBetween(19821, 26428));
        knn=new KNNRegression(dc.getRowsBetween(26428, 33035));
        */
        evaluations=dc.getRowsAfter(33035);
        
        
        
        root.add(LRDemo(),0,6);
        root.add(GPS(),0,7);
        root.add(KNN(),0,8);
        root.add(NL(),0,9);
        root.add(RBF(),0,10);
        root.add(validate(),0,11);

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
                km.run();
                
               
                rm=new RegressionModel(km.getClusters());
                rm.showBeta(true);
                rm.run();
                
                plt=new Plot();
                plt.showFittingLine(false);
                
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
            km.run();
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
            km.run();
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
            lr.run();
            //Thread threadLR=new Thread(lr);
            //threadLR.start();
            
            
            plt=new Plot();
            plt.showFittingLine(true);
            
            plt.plot(km.getClusters());
        });
        
        vb.getChildren().addAll(lblLR,btnLR);
        return vb;
    }
    
    
    private HBox LRDemo(){
        Button btn=new Button("Linear regression Demo");
        btn.setOnAction(((ActionEvent) -> {
            
            
            
            
            
            
            lr.changeK(5); 
            lr.run();
            
          System.out.println("RMSE: " + lr.RMSE());
            System.out.println("NRMSE: " + lr.NRMSE());
            System.out.println("TimeCost: " + lr.timeCost());
           System.out.println("Memory: " + lr.memory());
            
            
            
           plt=new Plot();
           plt.showFittingLine(true);
           plt.plot(lr.getClusters(),lr);
            
        }));
        
        HBox hb=new HBox();
        hb.getChildren().add(btn);
        return hb;
    }
    
    
    private HBox GPS(){
        Button btn=new Button("Gaussian Process");
        btn.setOnAction(((ActionEvent) -> {
            
            
            gps.changeK(5);
            gps.run();
            
            System.out.println("RMSE: " + gps.RMSE());
            System.out.println("NRMSE: " + gps.NRMSE());
            System.out.println("TimeCost: " + gps.timeCost());
            System.out.println("NRMSE: " + gps.NRMSE());
            
            
            
           plt=new Plot();
           plt.showFittingLine(true);
           plt.plot(gps.getClusters(),gps);
        }));
        
        HBox hb=new HBox();
        hb.getChildren().add(btn);
        return hb;
    }
    
    private HBox NL(){
        Button btn=new Button("Non-Linear regression Demo");
        btn.setOnAction(((ActionEvent) -> {
            
            
            nl.changeK(5);
            nl.run();
            System.out.println("RMSE: " + nl.RMSE());
            System.out.println("NRMSE: " + nl.NRMSE());
            System.out.println("TimeCost: " + nl.timeCost());
            System.out.println("Memory: " + nl.memory());
          
          
           plt=new Plot();
           plt.showFittingLine(true);
           plt.plot(nl.getClusters(),nl);
            
        }));
        
        HBox hb=new HBox();
        hb.getChildren().add(btn);
        return hb;
    }
    
    private HBox RBF(){
        Button btn=new Button("RBF Demo");
        btn.setOnAction(((ActionEvent) -> {
            
            
            rbf.changeK(5);
            rbf.run();
            System.out.println("RMSE: " + rbf.RMSE());
            System.out.println("NRMSE: " + rbf.NRMSE());
            System.out.println("TimeCost: " + rbf.timeCost());
            System.out.println("Memory: " + rbf.memory());
          
           
           plt=new Plot();
           plt.showFittingLine(true);
           plt.plot(rbf.getClusters(),rbf);
        }));
        
        HBox hb=new HBox();
        hb.getChildren().add(btn);
        return hb;
    }
    
    private HBox KNN(){
        Button btn=new Button("KNN Demo");
        btn.setOnAction(((ActionEvent) -> {
            
            
            knn.changeK(5);
            knn.run();
            System.out.println("RMSE: " + knn.RMSE());
            System.out.println("NRMSE: " + knn.NRMSE());
            System.out.println("TimeCost: " + knn.timeCost());
            System.out.println("Memory: " + knn.memory());
          
            
            
            
           plt=new Plot();
           plt.showFittingLine(true);
           plt.plot(knn.getClusters(),knn);
        }));
        
        HBox hb=new HBox();
        hb.getChildren().add(btn);
        return hb;
    }
    
    private HBox validate(){
        Button btn=new Button("validate");
        btn.setOnAction(((ActionEvent) -> {
            RegressionInterface ris[]=new RegressionInterface[5];
            
            
            ris[0]=lr;
            ris[1]=gps;
            ris[2]=nl;
            ris[3]=rbf;
            ris[4]=knn;
            
            Thread threads[]=new Thread[ris.length];
            for(int i=0;i<ris.length;i++){
                threads[i]=new Thread(ris[i]);
                try {
                    threads[i].start();
                    threads[i].join();
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            }
            
            
            
            Point pt=dc.getRowsBefore(10).get(4).getXPoint();
            
            SimpleValidate validate=new SimpleValidate(ris);
            validate.showFittedInformation(false);
            
            validate.runValidate(evaluations);
            validate.NRMSE();
            validate.RMSE();
            
            
            plt=new Plot();
            //plt.plot(validate.collectCentroids());
            Points[] haha=validate.pairPoints();
            plt.plot(haha);
            
            System.out.println(validate.fit(pt));
            System.out.println("finished!");
                        
            /*
            Cluster[] hah=knn.getClusters();
            System.out.println(hah[0].getCentroid());
            System.out.println(hah[1].getCentroid());
            System.out.println(hah[2].getCentroid());
            System.out.println(hah[3].getCentroid());
            System.out.println(hah[4].getCentroid());
            System.out.println(knn.NRMSE());
            */
            
            
           
        }));
        
        HBox hb=new HBox();
        hb.getChildren().add(btn);
        return hb;
    }

    
    
}
