/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.regression;

import com.teamProject.cluster.Cluster;
import com.teamProject.cluster.KMeans;
import com.teamProject.data.Point;
import com.teamProject.data.Points;
import com.teamProject.plot.Plot;
import com.teamProject.regression.LR;


/**
 *
 * @author qingzhi
 */
public class LinearRegression implements RegressionInterface {
    private Points points;
    private KMeans km;
    private LR[] lr;
    private Cluster[] clusters;
    private int clusterNum;
    private int k;
    private long start;
    private long end;
    

    public LinearRegression(Points pts){
        points=pts;
        k=5;
        
    }

    @Override
    public void run() {
        System.gc();
        start=Runtime.getRuntime().freeMemory();
        clusterNum=k;
        km=new KMeans(points, clusterNum);
        km.run();
        clusters=km.getClusters();
        
        lr=new LR[clusterNum];
        for(int i=0;i<clusterNum;i++){
            lr[i]=new LR(clusters[i]);
            lr[i].run();
        }
        
        System.gc();
        end = Runtime.getRuntime().totalMemory();
    }
    
    @Override
    public double fit(double[] x,int clusterNum){
        Point pt=new Point(x);
        
        //LR lr1=new LR(clusters[clusterNum]);
        //lr1.run();
        
        return lr[clusterNum].fitFunction(x);
    }

    @Override
    public String getMethodName() {
        return "linear regression"; //To change body of generated methods, choose Tools | Templates.
    }

    public double fit(double[] x) {
        return 1.0;
    }

     @Override
    public double RMSE() {
       
        double[] EIN=new double[clusterNum];
        double[] T=new double[clusterNum];
        for(int i=0;i<clusterNum;i++){
            EIN[i]=lr[i].getEiN();
            
        }
        for(int i=0;i<clusterNum;i++){
            T[i]=lr[i].getTotalLength();
            
        }
        double TNUM=0;
        for(int i=0; i<clusterNum;i++){
        TNUM=TNUM+T[i];
         }
        double TEIN=0;
         for(int i=0; i<clusterNum;i++){
        TEIN=TEIN+EIN[i];
         }
        
        double RMSE=Math.sqrt(TEIN/TNUM);
          //System.out.println("The RMSE is: "+RMSE);
        return RMSE;
    }

    @Override
    public double NRMSE() {
         double[] T=new double[clusterNum];
         for(int i=0;i<clusterNum;i++){
            T[i]=lr[i].getTotalLength();
            
        }
        double TNUM=0;
        for(int i=0; i<clusterNum;i++){
        TNUM=TNUM+T[i];
         }
        
        double[] NRMSEINI=new double[clusterNum];
        for(int i=0;i<clusterNum;i++){
            NRMSEINI[i]=lr[i].getNRSMEiNi();
         }
        double TNRM=0;
         for(int i=0; i<clusterNum;i++){
        TNRM=TNRM+NRMSEINI[i];
         }
          double NRMSE=Math.sqrt(TNRM/TNUM);
         //System.out.println("The NRMSE is: "+NRMSE);
        return NRMSE;
    }

     @Override
    public double timeCost() {
        double t=lr[0].timeCost();
        return t;
    }

    
    

    @Override
    public Cluster[] getClusters() {
        return clusters;
    }

    @Override
    public String equation() {
        for(int i=0;i<clusterNum;i++){
            System.out.println("Beta for cluster number "+i+" is: "+lr[i].getBeta());
        }
        return "";
    }

    @Override
    public long memory() {
        return (long) ((end-start)/1024.0/1024.0);
    }

    @Override
    public void changeK(int i) {
        k=i;
    }
    
    
    
    
}
