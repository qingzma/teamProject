/*
*please call Record2File.out() or Record2File.error() to output result to file
 */
package com.teamProject.regression;

import com.teamProject.cluster.Cluster;
import com.teamProject.data.Points;


/**
 *
 * @author qingzhi
 */
public interface RegressionInterface extends Runnable{
    /*the implemented class should have the following constructors:
    RegressionInterface( Points pts)
    */
    
    public String equation();      //return the equation or parameters.
    
    public long memory();    ///return the mmeory to sotre data points
    
    public String getMethodName();      //return the name of your regression algorithm
    
    @Override
    public void run();              //implement the regression 
    
    public double fit(double[]x,int clusterIDNum);  
    //this is the fit function, given x value, it should return the fitted value
    
    public Cluster[] getClusters();   //return the K-Means clusters.
    
    public double NRMSE();        
    // return the normal root mean square error of each cluster based on the training dataset
    
    public double RMSE();
    
    public double timeCost();
        // return the time cost (in seconds) during calculation
    
    //public Points evaluationData();
    // return 1/3 of the whole data set for evaluation.
    // note, you are suggested not to use this part of data in training.
    
    
    
    


}
