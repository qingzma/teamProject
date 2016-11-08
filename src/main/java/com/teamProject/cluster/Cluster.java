/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.cluster;

import com.teamProject.Record2File;
import com.teamProject.data.Point;
import com.teamProject.data.Points;
import com.teamProject.regression.GPS;
import com.teamProject.regression.KNN;
import com.teamProject.regression.LR;
import com.teamProject.regression.NL;
import com.teamProject.regression.NN;
import com.teamProject.regression.RBF;
import com.teamProject.regression.RegressionInterface;
import com.teamProject.regression.RegressionModel;

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
    double enlargeFactor=1.01;     // to give freedom to range, multiply this factor
    //public RegressionModel rm;
    public LR lr;
    public RegressionInterface ri;
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
      
      public double fit(Point pt){
          if(ri==null)
              System.err.println("Please call runRegression() Method "
                      + "in Cluster class before calling fit()");
          return ri.fitFunction(pt.toArray());
      }
     
      public void runRegression(){
          if(regressionMethod.equals("Linear Regression")){
              ri=new LR(cluster.getX().toArray(),cluster.getY().toVector());
              ri.run();
          }else if(regressionMethod.equals("Non-linear Regression")){
              ri=new NL(cluster.getX().toArray(),cluster.getY().toVector());
              ri.run();
          }else if(regressionMethod.equals("RBF")){
              ri=new RBF(cluster.getX().toArray(),cluster.getY().toVector());
              ri.run();
          }else if(regressionMethod.equals("KNN")){
              ri=new KNN(cluster.getX().toArray(),cluster.getY().toVector());
              ri.run();
          }else if(regressionMethod.equals("GPS")){
              ri=new GPS(cluster.getX().toArray(),cluster.getY().toVector());
              ri.run();
          }else if(regressionMethod.equals("NN")){
              ri=new NN(cluster.getX().toArray(),cluster.getY().toVector());
              ri.run();
          }else{
              Record2File.error("Regression method not found! Please check.");
              System.exit(-1);
          }
                  
            
      }
      
      public double getR2(){
          double result=9999999;
          if (ri!=null){
              //result= ri.getR2();
          }
          else{
              System.err.println("Please call fit method before getR2 in Class Cluster");
          }
            //}
            return result;
      }
      
      public RegressionInterface getRegressionMethod(){
          return ri;
      }
      
      
      public void setRegressionMethodName(String str){
          regressionMethod=str;
      }
      
      public String getRegressionMethodName(){
          return regressionMethod;
      }


}