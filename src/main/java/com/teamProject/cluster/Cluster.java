/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.cluster;

import com.teamProject.data.Point;
import com.teamProject.data.Points;
import com.teamProject.regression.LR;

/**
 *
 * @author qingzhi
 */
public class Cluster {
    private Points cluster;
    private Point centroid;
    private int id;
    private Point[] range;
    final private boolean m_bPrintClusterInformation=false;
    double enlargeFactor=1.00;     // to give freedom to range, multiply this factor
    //public RegressionModel rm;
    //public LR lr;
    //public RegressionInterface ri;
    String regressionMethod;
    
    public Cluster(int id){
        this.id=id;
        this.cluster=new Points();
        this.centroid=new Point();
    }
    
    public Points getCluster(){
        return cluster;
    }
    
    public void addPoint(Point p){
        cluster.add(p.getPoint());
    }
    
    //public void setCluster(Points points){
    //    
    //}
    
    public Point getCentroid(){
        return centroid;
    }

    public double[][] toArray(){
        return cluster.toArray();
    }
    
  
    public int getID(){
        return id;
    }
    
    public void clear(){
        cluster.clear();
    }
    
    public void pltCluster(){
        System.out.println("[Cluster: " + id+"]");
        System.out.println("[Centroid: " + centroid + "]");
	System.out.println("[Points: \n");
        for(int i=0;i<cluster.getPointNum();i++){
            System.out.println(cluster.get(i).getPoint());
        }
    }
    
      public void setCentroid(Point cent){
        this.centroid.set(cent.clone());
    }
      
      public Point[] getRange(){
          //System.out.println("Clusters /n, dimension is");
          //System.out.println("Clusters /n, dimension is"+cluster.getDimension());
          range=new Point[cluster.getDimension()];
          double min;
          double max;
          Point pt;
          
          
          for(int i=0;i<cluster.getDimension();i++){
              
              double[] arrayRange=new double[2];
              arrayRange[0]=cluster.get(0).get(i);
              arrayRange[1]=cluster.get(0).get(i);
              //Point ptMin=new Point(cluster.get(0));
              for(int j=1;j<cluster.getPointNum();j++){
                  if(arrayRange[0]>cluster.get(j).get(i))
                      arrayRange[0]=cluster.get(j).get(i);
                  if(arrayRange[1]<cluster.get(j).get(i))
                      arrayRange[1]=cluster.get(j).get(i);
              }
              
              double center=getCentroid().get(i);
              //System.out.println("range before:"+arrayRange[0]+":"+center+":"+arrayRange[1]);
              arrayRange[0]=center-(center-arrayRange[0])*enlargeFactor;
              arrayRange[1]=center+(arrayRange[1]-center)*enlargeFactor;
              //System.out.println("range after:"+arrayRange[0]+":"+center+":"+arrayRange[1]);
              pt=new Point(arrayRange);
              range[i]=pt.clone();
              
          }
        
        if(m_bPrintClusterInformation){
            for(int i=0;i<cluster.getDimension();i++){
                  System.out.println("cluster "+getID()+"'range is "+
                          range[i].get(0)+":"+range[i].get(1));
            }
            System.out.println();
        }
          
        
        return range;
      }
      
    
      public double radius(){
          //return the radius of each cluster
          double result=0;
          for (int i=0;i<cluster.getPointNum();i++){
              if(result<centroid.distanceTo(cluster.get(i)))
                  result=centroid.distanceTo(cluster.get(i));
          }
          return result;
      }
     


}
