/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.regression;

import com.teamProject.cluster.Cluster;
import java.util.ArrayList;
import java.util.Arrays;
import com.teamProject.data.Points;
import com.teamProject.data.Point;

/**
 *
 * @author qingzhi
 */
public class Fitting {
    //private Points points;
    private Cluster[] clusters;
    private int numClusters;
    private ArrayList<Integer> clusterGroup;            //to store the index of each cluster that a point x belongs to
    private ArrayList<Double> clusterGroupWeight;
    private    double RSS=0;
    private    double TSS=0;
    private    double MSS=0;
    private    double SSRatio=0;
    private    double R2=999;
    private int validateDegree;
    private boolean m_bShowVaidateInformation=false;
    
    public Fitting(Cluster[] clusters){
        this.clusters=clusters;
        //this.points=pts;
        numClusters=clusters.length;
        //clusterGroup=new ArrayList<>();
        //clusterGroupWeight=new ArrayList<>();
    }
    
    public double fit(Point x){
        double result=0.0;
        clusterGroup=new ArrayList<>();
        clusterGroupWeight=new ArrayList<>();
        for(int i=0;i<numClusters;i++){
            if(x.in(clusters[i].getRange()))
                clusterGroup.add(i);
                //clusterGroupWeight.add(1.0);
            
        }
        
        clusterGroupWeight =assignWeight(x,clusterGroup);
        //if(clusterGroupWeight.size()>0)
            //System.out.println("weights: "+clusterGroupWeight);
        
        
        if(clusterGroup.isEmpty()){
            System.out.println("Point"+x+"does not belong to any clusters, "
                    + "so choose the nearest cluster to predict it.");
            double distance=Double.MAX_VALUE;
            int nearestGroupID=0;
            for(int i=0;i<numClusters;i++){
                if(x.distanceTo(clusters[i].getCentroid())<distance){
                    nearestGroupID=i;
                    distance=x.distanceTo(clusters[i].getCentroid());
                    
                }
                
            }
            System.out.println("use cluster "+nearestGroupID+ " to fit, distance is "+distance);
            clusters[nearestGroupID].runRegression();
            result=clusters[nearestGroupID].fit(x);
            
            
        }
        else{
            //int weight=0;
            for(int i=0;i<clusterGroup.size();i++){
                clusters[clusterGroup.get(i)].runRegression();
                result+=clusters[clusterGroup.get(i)].fit(x) * 
                        clusterGroupWeight.get(i) ;
            //   weight+=clusterGroupWeight.get(i);
            }
            //System.out.println("result is "+result);
            //result=result/weight;
            //System.out.println("result is "+result);
        }
        
        return result;
    }
    
 
    public Points fit(Points pts){
        Points newPt=pts.clone();
        
        //for(int i=0;i<numClusters;i++){
        //    clusters[i].runRegression();
        //}
        
        
        for(int j=0;j<pts.getPointNum();j++){
            newPt.get(j).add(fit(newPt.get(j)));
        }
        return newPt;
    }
    
    public Points validate(Points pts){
        SSRatio=0;
        TSS=0;
        RSS=0;
        MSS=0;
        validateDegree=pts.getPointNum();
        //Points pts include Y items to check the answer;
        Points newPts=pts.getX();
        double y_hat;
        double y_mean=Arrays.stream(pts.getY().toVector()).sum()/pts.getPointNum();
        double yi;
        
        for(int j=0;j<pts.getPointNum();j++){
            //update Y as y_hat
            y_hat=fit(newPts.get(j));
            newPts.get(j).add(y_hat);
            //get yi
            yi=pts.get(j).getYValue();
            
            TSS+=(yi-y_mean)*(yi-y_mean);
            RSS+=(yi-y_hat)*(yi-y_hat);
            SSRatio+=(yi-y_hat)*(yi-y_hat)/(yi-y_mean)/(yi-y_mean);
            if(m_bShowVaidateInformation)
                System.out.println("xi,yi,y_hat,y_mean:"+pts.get(j).get(0)+" "+yi+" "+y_hat+" "+y_mean);
        }
        return newPts;
        
    }
    
    public double getValidateNRMSE(){
        double a1=SSRatio/validateDegree;
        double a2=SSRatio;
        return Math.sqrt(SSRatio/validateDegree);
    }
    
    public double getValidateRMSE(){
        double a1=SSRatio/validateDegree;
        double a2=SSRatio;
        return Math.sqrt(RSS/validateDegree);
    }
    public double getValidateTMSE(){
        double a1=SSRatio/validateDegree;
        double a2=SSRatio;
        return Math.sqrt(TSS/validateDegree);
    }
    
    
    public ArrayList<Double> assignWeight(Point x,ArrayList<Integer> clusterGroup){
        Point pt;
        double[] weights=new double[clusterGroup.size()];
        double sum;
        ArrayList<Double> al=new ArrayList<>();
        for(int i=0;i<clusterGroup.size();i++){
            pt=clusters[clusterGroup.get(i)].getCentroid();
            weights[i]=x.distanceTo(pt);
            weights[i]=weights[i]*weights[i];
        }
        
        sum=Arrays.stream(weights).sum();
        
        for(int i=0;i<clusterGroup.size();i++){
            weights[i]=weights[i]/sum;
            al.add(weights[i]);
        }
        
        return al;
            //weights[i]=
    }
    
    
    public void showValidateInformation(boolean b){
        m_bShowVaidateInformation=b;
    }
}
