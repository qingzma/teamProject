/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.regression;

/**
 *
 * @author langsha
 */
import com.teamProject.cluster.Cluster;
import com.teamProject.cluster.KMeans;
import com.teamProject.data.Point;
import com.teamProject.data.Points;
import com.teamProject.plot.Plot;
import com.teamProject.regression.PR;
import org.jblas.DoubleMatrix;

/**
 *
 * @author langsha
 */
public class PolynomialRegression implements RegressionInterface{
    private Points points;
    private KMeans km;
    private PR[] pr;
 
    private Cluster[] clusters;
    private int clusterNum;
    private long start=0;
    private long end=0;
    
    private int k=5;
    
    public PolynomialRegression(Points pts){
          points=pts;
     }
    @Override
    public String equation() {
        for(int i=0;i<clusterNum;i++){
            
             System.out.println("Beta for "+i+"th"+" cluster: "+pr[i].getBeta());
        }
     
      return "";
       
    }

    @Override
    public long memory() {
        return (long) ((end-start)/1024.0/1024.0);
    }

    @Override
    public String getMethodName() {
        
        return pr[1].getMethodName();
    }

    @Override
    public void run() {
       
        System.gc();
        start=Runtime.getRuntime().freeMemory();
        clusterNum=k;
        km=new KMeans(points, clusterNum);
        km.run();
        clusters=km.getClusters();
        
        pr=new PR[clusterNum];
        for(int i=0;i<clusterNum;i++){
            pr[i]=new PR(clusters[i]);
            pr[i].run();
            
        }
        System.gc();
        end = Runtime.getRuntime().totalMemory();
    }

    @Override
    public double fit(double[] x, int clusterNum) {
         Point pt=new Point(x);
          return pr[clusterNum].fitFunction(x);
    }

    @Override
    public Cluster[] getClusters() {
       return clusters;
        
    }

   

    @Override
    public double timeCost() {
        double t=pr[0].timeCost();
        return t;
    }

    @Override
    public double RMSE() {
       
        double[] EIN=new double[clusterNum];
        double[] T=new double[clusterNum];
        for(int i=0;i<clusterNum;i++){
            EIN[i]=pr[i].getEiN();
            
        }
        for(int i=0;i<clusterNum;i++){
            T[i]=pr[i].getTotalLength();
            
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
            T[i]=pr[i].getTotalLength();
            
        }
        double TNUM=0;
        for(int i=0; i<clusterNum;i++){
        TNUM=TNUM+T[i];
         }
        
        double[] NRMSEINI=new double[clusterNum];
        for(int i=0;i<clusterNum;i++){
            NRMSEINI[i]=pr[i].getNRSMEiNi();
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
    public void changeK(int i) {
        k=i;
    }
       
    
}


