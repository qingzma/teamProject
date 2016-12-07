/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.fit;

import com.teamProject.Record2File;
import com.teamProject.data.Point;
import com.teamProject.data.PointInt;
import com.teamProject.data.Points;
import com.teamProject.data.PointsInt;
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
    private Points[] centroids;   //the matrix to store all centroids
    private Points[] radius;    //the matrix to store all radius
    private double radiusReduceFactor=0.3;      //this facotr is used to restrict the distance to centroids when assign point X to it.
    double y_hat[];
    double y[];
    
    
    public SimpleValidate(RegressionInterface[] ris){
        RMSE=0;
        NRMSE=0;
        this.ris=ris;
        collectCentroids();
    }
    

    
    
    //get all the centroids and its corresponding regression model
    public Points[] collectCentroids(){
        int numRegressionModels=ris.length;
        centroids=new Points[numRegressionModels];
        radius=new Points[numRegressionModels];
        for(int i=0;i<numRegressionModels;i++){
            centroids[i]=new Points();
            radius[i]=new Points();
            for(int j=0;j<ris[i].getClusters().length;j++){
                centroids[i].add((ris[i].getClusters())[j].getCentroid());
                Point pt=new Point();
                pt.add(ris[i].getClusters()[j].radius());
                radius[i].add(pt);
            }
                
        }
        return  centroids;
    }
    
    
    public Points[] centroids(){
        return centroids;
    }
    
    public PointInt locateRegressionModel(Point pt){
        PointInt ptInt;         //0 is the index of cluster, 1 is the index of regression method
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
    
    public PointsInt locateRegressionModels(Point pt){
        PointsInt ptsInt=new PointsInt();
        int numRegressionModels=ris.length;
        for(int i=0;i<numRegressionModels;i++){
            for(int j=0;j<ris[i].getClusters().length;j++){
                //System.out.println("i,j"+i+" "+j);
                //System.out.println("the centroids are: "+ris[i].getClusters()[j].getCentroid());
                double distance=ris[i].getClusters()[j].getCentroid().getXPoint().distanceTo(pt);
                if(distance<radiusReduceFactor*radius[i].get(j).get(0)){
                    PointInt ptInt =new PointInt();
                    ptInt.add(i);
                    ptInt.add(j);
                    ptsInt.add(ptInt);
                    Record2File.out("           point "+pt+" close to centroid "+j+" in "+
                            ris[i].getMethodName()+": "+ris[i].getClusters()[j].getCentroid().getXPoint());
                    //System.out.println("distance is "+distance+"radius is "+
                    //        ris[i].getClusters()[j].radius()+". range is "+
                    //        ris[i].getClusters()[j].getRange()[0]);
                }
            }
                
        }
            
                
        return ptsInt;
    }
    
    public double fit(Point pt){
        double result;
        
        PointsInt indexs=locateRegressionModels(pt);
        if(indexs.length()==1){
            Record2File.out("Point "+pt+" is fitted by Cluster "+ indexs.get(0).get(1)+
                " in " + "regression method: "+ris[indexs.get(0).get(0)].getMethodName());
            result = ris[indexs.get(0).get(0)].fit(pt.toArray(), indexs.get(0).get(1));
        }
        else if (indexs.length()==0){
            PointInt index=locateRegressionModel(pt);
            Record2File.warning("Point "+pt+" does not belongs to any clusters, thus is fitted by Cluster "+ index.get(1)+
                " in " + "regression method: "+ris[index.get(0)].getMethodName());
            result = ris[index.get(0)].fit(pt.toArray(), index.get(1));
        }
        else{
            result=Double.MAX_VALUE;
            Record2File.out("Point "+pt+" is fitted by multiple models: ");
            Point distances=new Point();
            Point ys=new Point();
            for(int i=0;i<indexs.length();i++){
                distances.add(ris[indexs.get(i).get(0)].getClusters()[indexs.get(i).get(1)].getCentroid().getXPoint().distanceTo(pt));
                ys.add(ris[indexs.get(i).get(0)].fit(pt.toArray(), indexs.get(i).get(1)));
            }
            double totalDistance=Arrays.stream(distances.toArray()).sum();
            Point weights=new Point();
            
            double tmp;
            for(int i=0;i<indexs.length();i++){
                tmp=totalDistance-distances.get(i);
                weights.add(tmp);
            }
            double totalWeights=Arrays.stream(weights.toArray()).sum();
            Point finalWeights=new Point();
            for(int i=0;i<indexs.length();i++){
                finalWeights.add(weights.get(i)/totalWeights);
            }
            result=0;
            for(int i=0;i<indexs.length();i++){
                result+=finalWeights.get(i)*ys.get(i);
            }
            Record2File.outInLine("Fitted Y: "+ys+"; Weights: "+finalWeights+"; RMSE: [");
            
            /*
            for(int i=0;i<indexs.length();i++){
                Record2File.out(ris[indexs.get(i).get(0)].NRMSE()+" ");
            }
            */
            Record2File.out("].");
            
            
        }
        
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
        y_hat=fit(pts.getX());
        y=pts.getY().toVector();
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
        Record2File.out("The root mean squared error (RMSE) is: "+ Record2File.double2str(RMSE));
        return RMSE;
    }
    
    public double NRMSE(){
        Record2File.out("The normalised root mean squared error (NRMSE) is: "+ Record2File.double2str(NRMSE));
        return NRMSE;
    }
    
    public Points[] pairPoints(){
        Points pts[]=new Points[2];
        pts[0]=new Points();
        pts[1]=new Points();
        Point pt1;
        Point pt2;
        for(int i=0;i<y.length;i++){
            pt1=new Point();
            pt2=new Point();
            pt1.add((double)i);
            pt1.add(y[i]);
            pt2.add((double)i);
            pt2.add(y_hat[i]);
            
            pts[0].add(pt1);
            pts[1].add(pt2);
        }
        
        
        return pts;
    }
    
    
}
