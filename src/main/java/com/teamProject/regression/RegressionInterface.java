/*
*please call Record2File.out() or Record2File.error() to output result to file
 */
package com.teamProject.regression;

import com.teamProject.cluster.Cluster;


/**
 *
 * @author qingzhi
 */
public interface RegressionInterface extends Runnable{
    /*the implemented class should have the following constructors:
    RegressionInterface( Points pts)
    */
    
    public String equantion();      //return the equation or parameters.
    
    public long memory();    ///return the mmeory to sotre data points
    
    public String getMethodName();      //return the name of your regression algorithm
    
    public void run();              //implement the regression 
    
    public double fit(double[]x,int clusterIDNum);  
    //this is the fit function, given x value, it should return the fitted value
    
    public Cluster[] getClusters();   //return the K-Means clusters.
    
    public double[] getNRMSE();        
    // return the normal root mean square error of each cluster based on the training dataset
    
    public double timeCost();
        // return the time cost (in seconds) during calculation
    
    //public void printTimeCost();
        //print the time cost in a well defined structure
        //Simply copy the following codes
    /*
    {
        Record2File.out("Time for "+getMethodName()+" is "+
                    Record2File.double2str( timeCost()) +"s."  );
    }
    */
    


}
