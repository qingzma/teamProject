/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject;

import java.util.Random;

/**
 *
 * @author qingzhi
 */
public class KMeans {
    public Points X=new Points();
    public Points Y=new Points();
    public Points data;
    public int NUM_CLUSTER =2;
    private final double ITERATION_CRITERIA=0.001;
    private final int ITERATION_STEP=100;
    //private boolean bFinish=false;
    //private Point centroids[];
    private Cluster  clusters[];
    private final boolean m_bPrintCycleInformation=false;
    
    public KMeans(dataContainer dc,int group){
        //this.X=dc.getX();
        //this.Y=dc.getY();
        data=dc.getData();
        NUM_CLUSTER=group;
        
        //ArrayList<ArrayList<Double>> clusters[];
        
        
    }
    
    public KMeans(Points pts,int group){
        //this.X=dc.getX();
        //this.Y=dc.getY();
        data=pts.clone();
        NUM_CLUSTER=group;
        
        //ArrayList<ArrayList<Double>> clusters[];
        
        
    }
    
    
    public Cluster[] runClusters(){
        //create and initialize clusters and centroids
        //centroids=new Point[NUM_CLUSTER];
        do{
        clusters=new Cluster[NUM_CLUSTER];
        //Point pt;
        //for(int i=0;i<NUM_CLUSTER;i++){
            //clusters[i]=new Cluster();
            //centroids[i]=new Point();
        //}
        
        //set random centroids
        Random rand=new Random();
        int randNum;
        for(int i=0;i<NUM_CLUSTER;i++){
            randNum=rand.nextInt(data.getPointNum());
            //System.out.println("KMeans, Random number is: "+randNum);
            Point cent=data.get(randNum);
            clusters[i]=new Cluster(i);
            clusters[i].setCentroid(cent);
        }
        
        
        //implement K-Means
        assignCluster();
        
        //clusters[0].pltCluster();
        //clusters[1].pltCluster();
        //clusters[2].pltCluster();
        //clusters[3].pltCluster();
        
        int iteration=0;
        boolean bFinish=false;
        while(!bFinish){
            
            
            clearClusters();
            
            Point[] lastCentroids=getCentroid();
            //System.out.println("after clear");
            //lastCentroids[0].plotPoint();
            
            
            // to assign data points to nearest clusters
            assignCluster();
            
            
            //Point[] currentCentroids=getCentroid();
            //System.out.println("after assign");
            //currentCentroids[0].plotPoint();
            
            // calculate centroids
            calculateCentroids();
            
            iteration++;
            
            Point[] currentCentroids=getCentroid();
                    //System.out.println("after calculate");
            //currentCentroids[0].plotPoint();
            //calculates total distance between new and old centroids
            double distan=0.0;
            for(int i=0;i<NUM_CLUSTER;i++){
                //lastCentroids[i].plotPoint();
                //currentCentroids[i].plotPoint();
                distan=distan+distance(lastCentroids[i], currentCentroids[i]);
                //System.out.println(distan);
            }
            
            if(m_bPrintCycleInformation){
                System.out.println("#################");
                System.out.println("Iteration: " + iteration);
                System.out.println("Centroid distances: " + distan);
            }
            
            
            if(distan< ITERATION_CRITERIA) break;
            if(iteration>ITERATION_STEP) break;
        }
        
        
        //check if each group hold several data points
        }while(isSomeClusterEmpty());
        
        //check if each group hold several data points
        for(int i=0;i<NUM_CLUSTER;i++){
            if(clusters[i].getCluster().isEmpty()){
                System.err.println("In Kmeans, Still, cluster is empty in  "+i);
               
            }
        }
        
        
        return clusters;
    }
    
    
    
    /*
    public Points kMeans(){
        Points result=new Points();
        
        return result;
    }
    */
    
    public double distance(Point x1,Point x2){
        double result=0;
        int size=x1.getSize();
        for(int i=0;i<size;i++){
            result=result+(x1.get(i)-x2.get(i))*(x1.get(i)-x2.get(i));
            
        }
        result=Math.sqrt(result);
        return result;
    }
    
    public void setClusterGroupNum(int n){
        NUM_CLUSTER=n;
    }
    
    public void clearClusters(){
        for(int i=0;i<NUM_CLUSTER;i++){
            clusters[i].getCluster().clear();
        }
    }
    
    
    //
    private void assignCluster(){
        

        
        double distan=0.0;
        
        for(int i=0;i<data.getPointNum();i++){
            double max=Double.MAX_VALUE;
            double min=max;
            //double distanceMax=0.0;
            int clusterIndex=0;
            for(int j=0;j<NUM_CLUSTER;j++){
                Cluster c=clusters[j];
                
                distan=distance(c.getCentroid(),data.get(i));
                
                //System.out.println("distance for grp "+i+" is: "+distan);
                if(distan<=min){
                    min=distan;
                    clusterIndex=j;
                    //System.out.println("index changed to "+j);
                }
            }
            data.get(i).setClusterNum(clusterIndex);
            clusters[clusterIndex].getCluster().add(data.get(i).clone());
        }
        
    }
    
    
    private void calculateCentroids(){
        
        for (int i=0;i<NUM_CLUSTER;i++){
            //generate a zero point as centroid
            double[] zeroArray=new double[data.getDimension()];
            for(int j=0;j<data.getDimension();j++){
                zeroArray[j]=0;
            }
            //Point sumCentroid= new Point(zeroArray);
            
            for(int k=0;k<clusters[i].getCluster().getPointNum();k++){
                for (int m=0;m<data.getDimension();m++){
                    zeroArray[m]+=clusters[i].getCluster().get(k).get(m);
                    //System.out.println("m: "+m+"i: "+i+"k: "+k);
                }
            }
            
            for(int j=0;j<data.getDimension();j++){
                zeroArray[j]=zeroArray[j]/clusters[i].getCluster().getPointNum();
            }
            Point centro=new Point(zeroArray);
            clusters[i].setCentroid(centro);
            //clusters[i].getCentroid().plotPoint();
        }
        
    }

    public Point[] getCentroid(){
        Point[] centroidResult=new Point[NUM_CLUSTER];
        for(int i=0;i<NUM_CLUSTER;i++){
            centroidResult[i]=clusters[i].getCentroid().clone();
        }
        
        return centroidResult;
    }
    
    public Cluster[] getClusters(){
        return clusters;
    }
    
    private boolean isSomeClusterEmpty(){
        boolean isEmpty=false;
        for(int i=0;i<NUM_CLUSTER;i++){
            if(clusters[i].getCluster().isEmpty()){
                //System.err.println("In Kmeans, cluster is empty in "+i);
                isEmpty=true;
            }
        }
        return isEmpty;
    }
}
