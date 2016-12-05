/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.regression;

/**
 *
 * @author langsha
 */
import com.teamProject.Record2File;
import com.teamProject.cluster.Cluster;
import org.jblas.DoubleMatrix;
import org.jblas.Solve;

/**
 *
 * @author langsha
 */
public class PR {
    private DoubleMatrix X;         //the X matrix when run linear regression
    private DoubleMatrix x;         //restore the x value of points
    private DoubleMatrix Y;         //the Y Vector when run linear regression
    private DoubleMatrix beta;
    private DoubleMatrix predicte;
    private DoubleMatrix E;
    private DoubleMatrix YM;
    private DoubleMatrix Esq;
    private DoubleMatrix YMsq;
    private int ylength;
    private double MSE;
    private double NRSMEi;
    private double ein;
    private double NRSMEiNi;
    private double RSS;
    private double TSS;
    private double R2;
    private boolean m_bShowBeta=false;
    int n;
    int p;
    Cluster cluster;
    Cluster[] clusters;
    //RegressionModel regressionModels[];
    PR regressionMethods[];
    private double t0;
    private double t1;
    
    
    
    
    public PR(double[][] x,double[] y){
        this.x=new DoubleMatrix(x);
        Y=new DoubleMatrix(y);
    }
    
    public PR(Cluster cluster){
        this.x=new DoubleMatrix(cluster.getCluster().getX().toArray());
        Y=new DoubleMatrix(cluster.getCluster().getY().toVector());
        this.cluster=cluster;
    }
    
    public PR(Cluster[] clusters){
        this.clusters=clusters;
        regressionMethods=new PR[clusters.length];
        for(int i=0;i<clusters.length;i++){
            regressionMethods[i]=new PR(clusters[i]);
        }
    }
    

    public String getMethodName() {
        return "Polynominal Regression"; 
    }
    
    public void printTimeCost(){
        Record2File.out("Time for "+getMethodName()+" is "+
                    Record2File.double2str( timeCost()) +"s."  );
    }
    
    public void run(){
        t0=System.currentTimeMillis();
         if(clusters==null){
             if(x.columns==1){
                //when x has only one attribute.
                polynominal1D(1);
            }
             else if(x.columns>1){
                 polynominalMD();
             }else{
                System.out.println("error in class Regression Model.");
            }
              if(m_bShowBeta){
                  
                System.out.println("Beta for this cluster: "+getBeta());
              
                System.out.println("MSE for this cluster: "+getMSE());
            
              }
                
         }
         
         else{   //do regression when there is clusters[] input
           
            for(int i=0;i<clusters.length;i++){
                regressionMethods[i].run();  
            }
            t1=System.currentTimeMillis();
            Record2File.out("Polynomial regression ends.");
            printTimeCost();
            Record2File.out("\n");
        }
          t1=System.currentTimeMillis();
    
    }
    
    public double fitFunction(double[] xx){
           double result=999999;  
           if(x.columns==1){
            result=functionPolynominal1D(xx[0]);
            }else if(x.columns>1){
            result=functionPolynominalMD(xx);
            }
           else{
            System.err.println("error in class Regression Model.");
        }
        return result;
    }
    
    
    public double timeCost() {
        return (t1-t0)/1000.0d;
    }
    
    public double getMSE() {
        
        return MSE;
    }
    
    public int getTotalLength(){
        return ylength;
    }
    
    public double getEiN(){
        
        return ein;
    }
    
    public double getNRSMEiNi(){
      return NRSMEiNi;
    }
    
    public double getNRSMEi(){
       return NRSMEi;
    }
    
    public void polynominal1D(int powerMax){
         //y=a0+b1X1+b2X1^2
        n=Y.length;
        p=powerMax+2;
        //initialize matrix X
        double[][] arrayX=new double[n][p];
        for(int i=0;i<n;i++){
            for(int j=0;j<p;j++){
                arrayX[i][j]=Math.pow(x.get(i), j);
                //Design Matrix[1,x1,x1^2]
            }
        }
        X=new DoubleMatrix(arrayX);
        
        ylength=Y.length;
        beta=Solve.pinv(X.transpose().mmul(X)).mmul((X.transpose()).mmul(Y));
        predicte=X.mmul(beta);
        E=(predicte.sub(Y));
        Esq=E.transpose().mmul(E);
        double aveEsq=Esq.get(0)/E.length;
        MSE=Math.sqrt(aveEsq);
        ein=MSE*MSE*E.length;
        
        RSS=Y.transpose().mmul(Y).sub(Y.transpose().mmul(X).mmul(beta)).get(0);
        
        double yMean=Y.mean();
        
        DoubleMatrix YMean=new DoubleMatrix(n);
        for(int i=0;i<n;i++){
            YMean.put(i, yMean);
        }
        YM=predicte.sub(YMean);
        YMsq=YM.transpose().mmul(YM);
        double nrmseAve=(Esq.get(0)/YMsq.get(0))/Y.length;
        NRSMEi=Math.sqrt(nrmseAve);
        NRSMEiNi=NRSMEi*NRSMEi*Y.length;
        TSS=(Y.sub(YMean)).transpose().mmul(Y.sub(YMean)).get(0);
        
    }
       public double functionPolynominal1D(double x_point){
           
        double[] arrayX=new double[p];
        for(int j=0;j<p;j++){
                arrayX[j]=Math.pow(x_point, j);
            }
        DoubleMatrix xVector=new DoubleMatrix(1,p,arrayX);
        double result=xVector.mmul(beta).get(0);
        
        return result;
    }
    
        
         
         public void polynominalMD(){
         n=Y.length;
         p=1+2*x.columns+x.columns*(x.columns-1)/2;
         int d=x.columns;
         double[][] arrayX=new double[n][p];
         for(int i=0;i<n;i++){
            	arrayX[i][0]=1;//1st(a0) col of designX is 1
             for(int j=1;j<1+d;j++){
            	  arrayX[i][j]=x.get(i,j-1);
               }//2,3...d col of designX is x itself(b1X1,b2X2,...bdXd)
             
             for(int j=1+d;j<1+2*d;j++){
                arrayX[i][j]=x.get(i, j-1-d)*x.get(i, j-1-d);
            }  //b(d+1)X1^2,B(D+2)X2^2...b(2d)Xd^2
        
             
             int index=1+2*d;
                for(int j=1+2*d;j<p;j++){
                     if(d==2){
                   for(int n=j+1;n<p+1;n++){
                      arrayX[i][index]=x.get(i,j-2*d-1)*x.get(i, n-2*d-1);
                      }
                 }
                   else {
                        for(int n=j+1;n<p-3*(d-3);n++){  
    //n<p-(3 times dimension-3),which can make loop work even if a large dimension
                        
                            arrayX[i][index]=x.get(i,j-2*d-1)*x.get(i, n-2*d-1);
                            index++;
                       
                        }
                    }
                 }
                 
                 
            } 
          X=new DoubleMatrix(arrayX);
          ylength=Y.length;
          beta=Solve.pinv(X.transpose().mmul(X)).mmul((X.transpose()).mmul(Y));
          predicte=X.mmul(beta);
        E=(predicte.sub(Y));
        Esq=E.transpose().mmul(E);
        double aveEsq=Esq.get(0)/E.length;
        MSE=Math.sqrt(aveEsq);
        ein=MSE*MSE*E.length;
        RSS=Y.transpose().mmul(Y).sub(Y.transpose().mmul(X).mmul(beta)).get(0);
         double yMean=Y.mean();
         
        DoubleMatrix YMean=new DoubleMatrix(n);
        for(int i=0;i<n;i++){
            YMean.put(i, yMean);
        }
        YM=predicte.sub(YMean);
        YMsq=YM.transpose().mmul(YM);
        double nrmseAve=(Esq.get(0)/YMsq.get(0))/Y.length;
        NRSMEi=Math.sqrt(nrmseAve);
        NRSMEiNi=NRSMEi*NRSMEi*Y.length;
        TSS=(Y.sub(YMean)).transpose().mmul(Y.sub(YMean)).get(0);
         }
         
         
      public double functionPolynominalMD(double[] x_point){
        double[] arrayX=new double[p];

        n=Y.length;
        p=p=1+2*x.columns+x.columns*(x.columns-1)/2;
        int d=x.columns;

            arrayX[0]=1;
         
            for(int j=1;j<1+d;j++){
                arrayX[j]=x_point[j-1];
            }

            for(int j=1+d;j<1+2*d;j++){
                arrayX[j]=x_point[j-1-d]*x_point[j-1-d];
            }
            
           
           int index=1+2*d;
            for(int j=1+2*d;j<p;j++){
                if(d==2){
                    for(int n=j+1;n<p+1;n++){
                        
                        arrayX[index]=x_point[j-2*d-1]*x_point[ n-2*d-1];
                        index++;
                    }
                }
                else {
                
                    for(int n=j+1;n<p-3*(d-3);n++){
                        
                        arrayX[index]=x_point[j-2*d-1]*x_point[ n-2*d-1];
                            index++;
                    }
                }
          
            }
        
        
        DoubleMatrix xVector=new DoubleMatrix(1, p, arrayX);
        double result=xVector.mmul(beta).get(0);
               
        return result;
    }
      
      public DoubleMatrix getBeta(){
        return beta;
    }
      
}


