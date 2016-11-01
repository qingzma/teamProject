/*
*please call Record2File.out() or Record2File.error() to output result to file
 */
package com.teamProject.regression;


/**
 *
 * @author qingzhi
 */
public interface RegressionInterface extends Runnable{
    /*the implemented class should have at least three constructors:
    RegressionInterface(double[][] x,double[] y)
    RegressionInterface(Cluster cluster)
    RegressionInterface(Cluster[] clusters)
    and should be able to run regression for these three different inputs
    */
    
    
    public String getMethodName();      //return the name of your regression algorithm
    
    public void run();              //implement the regression 
    
    public double fitFunction(double[]x);  
    //this is the fit function, given x value, it should return the fitted value
    
    public double getNRMSE();        
    // return the normal root mean square error based on the training dataset
    
    public double timeCost();
        // return the time cost (in seconds) during calculation
    
    public void printTimeCost();
        //print the time cost in a well defined structure
        //Simply copy the following codes
    /*
    {
        Record2File.out("Time for "+getMethodName()+" is "+
                    Record2File.double2str( timeCost()) +"s."  );
    }
    */
    

    public void setClusterFitFunction();        //
        //set the fitFunction is each cluster, simply copy the following codes to
        //the implemented class.
        /* {
        if(cluster.getRegressionMethod()==null
                || !cluster.getRegressionMethodName().equals(getMethodName())){
            cluster.setRegressionMethodName(getMethodName());
            cluster.runRegression();
        }
        */
}
