/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.regression;

import org.jblas.DoubleMatrix;
import org.jblas.Solve;
import com.teamProject.cluster.Cluster;

/**
 *
 * @author qingzhi
 */
public class RegressionModel {
    private DoubleMatrix X;
    private DoubleMatrix x;
    private DoubleMatrix Y;
    private DoubleMatrix beta;
    private double RSS;
    private double TSS;
    private double R2;
    private boolean m_bShowBeta=false;
    int n;
    int p;
    Cluster cluster;
    Cluster[] clusters;
    RegressionModel regressionModels[];
    
    public RegressionModel(double[][] x,double[] y){
        this.x=new DoubleMatrix(x);
        Y=new DoubleMatrix(y);  
    }
    
    public RegressionModel(Cluster cluster){
        this.x=new DoubleMatrix(cluster.getCluster().getX().toArray());
        Y=new DoubleMatrix(cluster.getCluster().getY().toVector());
        this.cluster=cluster;
    }
    
    public RegressionModel(Cluster[] clusters){
        this.clusters=clusters;
        regressionModels=new RegressionModel[clusters.length];
        for(int i=0;i<clusters.length;i++){
            regressionModels[i]=new RegressionModel(clusters[i]);
        }
    }
    
    
    public void run(){
        if(clusters==null){
            if(x.columns==1){
                polynominal1D(3);
            }
            else if(x.columns>=2){
                //polynominalMD2P();
                polynominalMD1P();
            }
            else{
                System.out.println("error in class Regression Model.");
            }
            
            if(m_bShowBeta){
                System.out.println("Beta is: "+getBeta());
            
            }
        }
        else{
            for(int i=0;i<clusters.length;i++){
                regressionModels[i].run();
                
            }
        }
        
      
        
        
        
        
    }
    
    public double fitFunction(double[]xx){
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
    
    
    public void polynominalMD2P(){
    /**
    * y=b0+b1*x1+b2*x2+b3*x3+...+b4*x1^2+b5*x2^2+b6*x3^2+...+b7*x1*x2+b8*x1*x3
    * 
    */
        n=Y.length;
        p=1+2*x.columns+x.columns*(x.columns-1)/2;
        int d=x.columns;
        //initialize matrix X
        double[][] arrayX=new double[n][p];
        for(int i=0;i<n;i++){
            // first column equals 1
            arrayX[i][0]=1;
            // column 2-x.column, assign Xi
            for(int j=1;j<1+d;j++){
                arrayX[i][j]=x.get(i, j-1);
            }
            // column x.column+1 ~2*column x.column+1 , assign Xi^2
            for(int j=1+d;j<1+2*d;j++){
                arrayX[i][j]=x.get(i, j-1-d)*x.get(i, j-1-d);
            }
            
            // column 2*column x.column+1 ~ 2*column x.column+1+(d,2), 
            // assign cross multiplication of Xi 
            int index=1+2*d;
            for(int j=1+2*d;j<p;j++){
                
                //for(int m=0;m<d-1;m++){
                    //for(int n=m+1;n<d;n++){
                    if(d==2){
                        for(int n=j+1;n<p+1;n++){
                        
                            arrayX[i][index]=x.get(i,j-2*d-1)*x.get(i, n-2*d-1);
                            index++;
                        }
                    }
                    else {
                        for(int n=j+1;n<p;n++){
                        
                            arrayX[i][index]=x.get(i,j-2*d-1)*x.get(i, n-2*d-1);
                            index++;
                        //if(i==0) System.out.println("j:"+j+", n: "+n+". value: "+arrayX[i][j]);
                        }
                    }
                //}
                
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
    
    public double functionPolynominalMD2P(double[] x_point){
        double[] arrayX=new double[p];

        n=Y.length;
        p=1+2*x.columns+x.columns*(x.columns-1)/2;
        int d=x.columns;

            // first column equals 1
            arrayX[0]=1;
            // column 2-x.column, assign Xi
            for(int j=1;j<1+d;j++){
                arrayX[j]=x_point[j-1];
            }
            // column x.column+1 ~2*column x.column+1 , assign Xi^2
            for(int j=1+d;j<1+2*d;j++){
                arrayX[j]=x_point[j-1-d]*x_point[j-1-d];
            }
            
            // column 2*column x.column+1 ~ 2*column x.column+1+(d,2), 
            // assign cross multiplication of Xi 
            int index=1+2*d;
            for(int j=1+2*d;j<p;j++){
                if(d==2){
                    for(int n=j+1;n<p+1;n++){
                        
                        arrayX[index]=x_point[j-2*d-1]*x_point[ n-2*d-1];
                        index++;
                    }
                }
                else {
                //for(int m=0;m<d-1;m++){
                    //for(int n=m+1;n<d;n++){
                    for(int n=j+1;n<p;n++){
                        
                        arrayX[index]=x_point[j-2*d-1]*x_point[ n-2*d-1];
                            index++;
                    }
                }
            }
        
        DoubleMatrix xVector=new DoubleMatrix(1, p, arrayX);
        double result=xVector.mmul(beta).get(0);
        return result;
    }
    
    
    
    public double getR2(){
        R2=1-RSS/TSS;
        return R2;
    }
    
    public DoubleMatrix getBeta(){
        return beta;
    }
    
    
 
    
    
    public void showBeta(boolean b){
        
        if(clusters!=null){
            for(int i=0;i<clusters.length;i++){
                regressionModels[i].showBeta(b);
                
            }
        }
    }
}
