/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.regression;

import com.teamProject.Record2File;
import com.teamProject.cluster.Cluster;
import org.jblas.DoubleMatrix;
import org.jblas.Solve;



/**
 *this is the linear regression model
 * @author qingzhi
 */
public class LR{
    private DoubleMatrix X;         //the X matrix when run linear regression
    private DoubleMatrix x;         //restore the x value of points
    private DoubleMatrix Y;         //the Y Vector when run linear regression
    private DoubleMatrix beta;
    private double RSS;
    private double TSS;
    private double R2;
    private boolean m_bShowBeta=false;
    int n;
    int p;
    Cluster cluster;
    Cluster[] clusters;
    //RegressionModel regressionModels[];
    LR regressionMethods[];
    private double t0;
    private double t1;
    
    
    
    public LR(double[][] x,double[] y){
        this.x=new DoubleMatrix(x);
        Y=new DoubleMatrix(y);
    }
    
    public LR(Cluster cluster){
        this.x=new DoubleMatrix(cluster.getCluster().getX().toArray());
        Y=new DoubleMatrix(cluster.getCluster().getY().toVector());
        this.cluster=cluster;
    }
    
    public LR(Cluster[] clusters){
        this.clusters=clusters;
        regressionMethods=new LR[clusters.length];
        for(int i=0;i<clusters.length;i++){
            regressionMethods[i]=new LR(clusters[i]);
        }
    }
    

    public String getMethodName() {
        return "Linear Regression"; 
    }
    
    public void printTimeCost(){
        Record2File.out("Time for "+getMethodName()+" is "+
                    Record2File.double2str( timeCost()) +"s."  );
    }

    public void run() {
        //t0=System.currentTimeMillis()/1000.0d;
        //Record2File.out("Linear regression starting...");
        
        if(clusters==null){
            //do regression when there is no clusters[] input
            if(x.columns==1){
                //when x has only one attribute.
                polynominal1D(1);
            }
            else if(x.columns>=2){
                //when x has more than one attributes.
                polynominalMD1P();
            }
            else{
                System.out.println("error in class Regression Model.");
            }
            
            if(m_bShowBeta){
                System.out.println("Beta is: "+getBeta());
            }
        }
        else{   //do regression when there is clusters[] input
            t0=System.currentTimeMillis();
            for(int i=0;i<clusters.length;i++){
                regressionMethods[i].run();  
            }
            t1=System.currentTimeMillis();
            Record2File.out("Linear regression ends.");
            printTimeCost();
            Record2File.out("\n");
        }
        
        //set the fitFunction is each cluster
   //     if(cluster!=null)
    //        setClusterFitFunction();
        
        //t1=System.currentTimeMillis()/1000.0d;
        //Record2File.out("Linear regression ends.");
        //printTimeCost();
        //Record2File.out("\n");
    }

    public double fitFunction(double[] xx) {
        double result=999999;
        if(x.columns==1){
            result= functionPolynominal1D(xx[0]);
        }else
        if(x.columns>=2){
            //result= functionPolynominalMD2P(xx);
            result= functionPolynominalMD1P(xx);
        }else{
            System.err.println("error in class Regression Model.");
        }
        return result;
    }

    public double getNRMSE() {
        R2=1-RSS/TSS;
        return R2;
    }

    public double timeCost() {
        return (t1-t0)/1000.0d;
    }
    /*
    public void setClusterFitFunction() {
        if(cluster.getRegressionMethod()==null
                || !cluster.getRegressionMethodName().equals(getMethodName())){
            cluster.setRegressionMethodName(getMethodName());
            cluster.runRegression();
        }
    }
    */
    
    public void polynominal1D(int powerMax){
        n=Y.length;
        p=powerMax+1;
        //initialize matrix X
        double[][] arrayX=new double[n][p];
        for(int i=0;i<n;i++){
            for(int j=0;j<p;j++){
                arrayX[i][j]=Math.pow(x.get(i), j);
            }
        }
        X=new DoubleMatrix(arrayX);
        
        //X.print();
        //System.out.println("column "+X.columns);
        //System.out.println("row "+X.rows);
        beta=Solve.pinv(X.transpose().mmul(X)).mmul(X.transpose()).mmul(Y);
        RSS=Y.transpose().mmul(Y).sub(Y.transpose().mmul(X).mmul(beta)).get(0);
        
        double yMean=Y.mean();
        DoubleMatrix YMean=new DoubleMatrix(n);
        for(int i=0;i<n;i++){
            YMean.put(i, yMean);
        }
        
        TSS=(Y.sub(YMean)).transpose().mmul(Y.sub(YMean)).get(0);
        
    }
    
    public double functionPolynominal1D(double x_point){
        double[] arrayX=new double[p];
        for(int j=0;j<p;j++){
                arrayX[j]=Math.pow(x_point, j);
            }
        DoubleMatrix xVector=new DoubleMatrix(1, p, arrayX);
        double result=xVector.mmul(beta).get(0);
        return result;
    }
    
    public void polynominalMD1P(){
    /**
    * y=b0+b1*x1+b2*x2+b3*x3+...
    * 
    */
        n=Y.length;
        p=x.columns+1;
        //initialize matrix X
        double[][] arrayX=new double[n][p];
        for(int i=0;i<n;i++){
            arrayX[i][0]=1;
            for(int j=1;j<p;j++){
                arrayX[i][j]=x.get(i, j-1);
            }
        }
        X=new DoubleMatrix(arrayX);
        
        //X.print();
        //System.out.println("column "+X.columns);
        //System.out.println("row "+X.rows);
        beta=Solve.pinv(X.transpose().mmul(X)).mmul(X.transpose()).mmul(Y);
        RSS=Y.transpose().mmul(Y).sub(Y.transpose().mmul(X).mmul(beta)).get(0);
        
        double yMean=Y.mean();
        DoubleMatrix YMean=new DoubleMatrix(n);
        for(int i=0;i<n;i++){
            YMean.put(i, yMean);
        }
        
        TSS=(Y.sub(YMean)).transpose().mmul(Y.sub(YMean)).get(0);
    
    }
    
    public double functionPolynominalMD1P(double[] x_point){
        double[] arrayX=new double[p];
        
        arrayX[0]=1;
        for(int j=1;j<p;j++){
            arrayX[j]=x_point[j-1];
        }
        
        DoubleMatrix xVector=new DoubleMatrix(1, p, arrayX);
        double result=xVector.mmul(beta).get(0);
        return result;
    }
    
    public DoubleMatrix getBeta(){
        return beta;
    }

    
    
}
