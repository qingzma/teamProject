/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.plot;

import com.orsoncharts.data.function.Function3D;
import org.jblas.DoubleMatrix;

/**
 *
 * @author qingzhi
 */
public class FittingFunctionFor3DPlot implements Function3D{
    int n;
    int p;
    DoubleMatrix beta;
    @Override
    public double getValue(double x, double z) {
        double[] xz=new double[2];
        xz[0]=x;
        xz[1]=z;
        return functionPolynominalMD2P(xz);
    }
    
    
    public FittingFunctionFor3DPlot(DoubleMatrix bt){
        n=1;
        p=6;
        beta=bt;
    }
    
    public double functionPolynominalMD2P(double[] x_point){

        double[] arrayX=new double[p];

        
        int d=2;

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
        double result=xVector.mmul(this.beta).get(0);
        return result;
    }
}
