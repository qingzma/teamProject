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
    
    

    public LinearRegression(Points pts){
        points=pts;
        
    }

    @Override
    public void run() {
        clusterNum=5;
        km=new KMeans(points, clusterNum);
        km.run();
        clusters=km.getClusters();
        
        lr=new LR[clusterNum];
        for(int i=0;i<clusterNum;i++){
            lr[i]=new LR(clusters[i]);
            lr[i].run();
        }
        
        
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double fit(double[] x) {
        return 1.0;
    }

    @Override
    public double[] getNRMSE() {
        double[] results=new double[clusterNum];
        for(int i=0;i<clusterNum;i++){
            results[i]=lr[i].getNRMSE();
        }
        return results;
    }

    @Override
    public double timeCost() {
        return 1.0;
    }

    
    

    @Override
    public Cluster[] getClusters() {
        return clusters;
    }

    @Override
    public String equantion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long memory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
}
