/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.fit;

import com.teamProject.data.Point;
import com.teamProject.data.PointInt;
import com.teamProject.data.Points;
import com.teamProject.regression.RegressionInterface;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author qingzhi
 */
public class SimpleValidate {
    private RegressionInterface[] ris;
    //private Points pts;
    //private Point pt;
    private double RMSE;   //the sum of (y_i -y_mean)^2
    private double NRMSE;   //the sum of (y_i -y_hat)^2
    //private Points centroids;
    //private PointInt centroidIndex;
    public SimpleValidate(RegressionInterface[] ris){
        RMSE=0;
        NRMSE=0;
        this.ris=ris;
    }
    

    
    
    //get all the centroids and its corresponding regression model
/*    public void collectCentroids(){
        int numRegressionModels=ris.length;
        centroids=new Points();
        centroidIndex=new PointInt();
        for(int i=0;i<numRegressionModels;i++){
            for(int j=0;i<ris[i].getClusters().length;j++){
                centroids.add((ris[i].getClusters())[j].getCentroid());
                centroidIndex.add(i);
            }
                
        }
    }
    */
    public PointInt locateRegressionModel(Point pt){
        PointInt ptInt;
        int rmIndex=0;
        int clusterIndex=0;
        int numRegressionModels=ris.length;
        double distance =Double.MAX_VALUE;
        for(int i=0;i<numRegressionModels;i++){
            for(int j=0;j<ris[i].getClusters().length;j++){
                if(distance>ris[i].getClusters()[j].getCentroid().getXPoint().distanceTo(pt)){
                    distance=ris[i].getClusters()[j].getCentroid().getXPoint().distanceTo(pt);
                    rmIndex=i;
                    clusterIndex=j;
                }
            }
                
        }
        ArrayList<Integer> al=new ArrayList<>();
        al.add(rmIndex);
        al.add(clusterIndex);
        ptInt=new PointInt(al);
        return ptInt;
    }
    
    
    public double fit(Point pt){
        double result;
        PointInt index=locateRegressionModel(pt);
        result = ris[index.get(0)].fit(pt.toArray(), index.get(1));
        return result;
    }
    
    public double[] fit(Points pts){
        double results[]=new double[pts.getPointNum()];
        for(int i=0;i<results.length;i++){
            results[i]=fit(pts.get(i));
        }
        
        return results;
    }
    
    public void runValidate(Points pts){
        double y_hat[]=fit(pts.getX());
        double y[]=pts.getY().toVector();
        double y_mean=Arrays.stream(y).sum()/y.length;
        RMSE=0;
        NRMSE=0;
        for(int i=0;i<y.length;i++){
            RMSE+=(y[i]-y_hat[i])*(y[i]-y_hat[i]);
            double tmp=(y[i]-y_hat[i])*(y[i]-y_hat[i]);
            tmp=tmp/(y[i]-y_mean)/(y[i]-y_mean);
                    
            if(tmp<=1){
                NRMSE+=tmp;
            }
        }
        RMSE=Math.sqrt(RMSE/y.length);
        NRMSE=Math.sqrt(NRMSE/y.length);
    }
    
    public double RMSE(){
        return RMSE;
    }
    
    public double NRMSE(){
        return NRMSE;
    }
    
}
