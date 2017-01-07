package com.teamProject.regression;

import com.teamProject.Record2File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.*;

import weka.clusterers.SimpleKMeans;
import weka.classifiers.functions.RBFNetwork;

import com.teamProject.cluster.Cluster;
import com.teamProject.cluster.KMeans;
import com.teamProject.data.Point;
import com.teamProject.data.Points;
import com.teamProject.data.dataContainer;
import com.teamProject.regression.RegressionInterface;
import java.io.BufferedWriter;
import java.io.FileWriter;
import weka.core.converters.ConverterUtils.DataSource;

public class RBF implements RegressionInterface {
//private Cluster[] clusters;
    private double t0,t1;
    private Points points;
    private int clusterNum;
    private String methodName="Radial Basis Function";
    private String initData;
    private Cluster[] clusters;
    private RBFNetwork[] rbfModel;
    private double timeCost;
    private long memoryUsed;
    private int attributeNum ;
    private double yMean;
    private DataSource trainingDataSource [];
    private Instances tDtSet [];
    private double nrmse [];
    private double rms [];
    private int k;
    private int tInstance;
    private KMeans kMeans;
    private int kkk=5;
   
    
	
public RBF(Points pts){
     points=pts;
     attributeNum = pts.getDimension();
     tInstance = pts.getPointNum();
     clusterNum = kkk;
     k=5;
     clusters = new Cluster[clusterNum];
     rbfModel = new RBFNetwork [clusterNum];
     tDtSet = new Instances[clusterNum];
     kMeans = new KMeans(points,clusterNum);
     kMeans.run();
     clusters = kMeans.getClusters();
     
}
   

	@Override
	public long memory() {
		// TODO Auto-generated method stub
		return memoryUsed;
	}

	@Override
	public String getMethodName() {
		// TODO Auto-generated method stub
		return "RBF network";
	}

	@Override
	public void run() {
            try {
          buildRBF(clusterNum);
          NRMSE();
          RMSE();
          } catch (Exception ex) {
                Logger.getLogger(RBF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

	@Override
public double fit(double[] x, int clusterIDNum) {
        double fitV = 0.0;  
       
        try{
            Instances testDataSet = new Instances("testData",new FastVector(x.length),1);
            Instance inst = new DenseInstance(x.length + 1);
            int i;
            for(int j = 0;j<x.length;j++){
                StringBuilder sb = new StringBuilder();
                sb.append("x");
                sb.append(j);
                String s = sb.toString();
                Attribute attribut = new Attribute(s);
                testDataSet.insertAttributeAt(attribut, j);
            }
            Attribute attribut = new Attribute("y");
            testDataSet.insertAttributeAt(attribut, x.length);
            for(i = 0;i<x.length;i++){
                inst.setValue(i, x[i]);
            }
            inst.setValue(x.length,0);
            testDataSet.add(inst);
            
            testDataSet.setClassIndex(testDataSet.numAttributes()-1);

                    
                             
           fitV = rbfModel[clusterIDNum].classifyInstance(testDataSet.get(0));  
           System.out.println(testDataSet.get(0));
        } catch (Exception ex) {
            Logger.getLogger(RBF.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //System.out.println(fitV);
        return fitV;
    }

	@Override
	public Cluster[] getClusters() {
		// TODO Auto-generated method stub
                System.out.println(clusters);
		return clusters;
                
                
	}

	

	@Override
	public double timeCost() {
		return (double)timeCost/1000;


	
        }
    


    
    @Override
    public double RMSE() {
         
        double rmse = 0.0;
        double errors [] = new double[clusterNum];
        int cAmt [] = new int [clusterNum];
        for (int i = 0; i < clusterNum; i++) {
            int iNum = tDtSet[i].size();
            cAmt[i] = iNum;
            errors[i] = 0.0;
            
            if (iNum > 1) {
                for (int j = 0; j < iNum ; j++ ) {
                    try {
                        double realValue = tDtSet[i].get(j).classValue();
                        double pdValue = rbfModel[i].classifyInstance(tDtSet[i].get(j));
                        double tpDiff = pdValue - realValue;
                        tpDiff = tpDiff * tpDiff;
                        errors[i] = errors[i] + tpDiff;
                    } catch (Exception ex) {
                        Logger.getLogger(RBF.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                errors[i] = (double) errors[i] / iNum;
                errors[i] = Math.sqrt(errors[i]);
                
            }
        }
        
        for (int j = 0; j < clusterNum;j++) {
            rmse = rmse + ((errors[j]*errors[j])*cAmt[j]);
        }
        
        rmse = (double) rmse / tInstance;
        rmse = Math.sqrt(rmse);
        System.out.println("RMSE "+ rmse );
        return rmse;
    }
    
    
    

    @Override
    public double NRMSE() {
       double nrmse = 0.0;
        double errors [] = new double[clusterNum];
        int cAmt [] = new int [clusterNum];
        for (int i = 0; i < clusterNum; i++) {
            yMean = getPredMean();
            int iNum = tDtSet[i].size();
            cAmt[i] = iNum;
            //System.out.println("cluster Amount"+i + " " + cAmt[i] );
            if (iNum > 1) {
                // System.out.println("iNum "+i + " " + iNum );
                for (int j = 0; j < iNum ; j++ ) {
                    try {
                        
                        double rVal = tDtSet[i].get(j).classValue();
                        double predVal = rbfModel[i].classifyInstance(tDtSet[i].get(j));
                        double tpDiff = predVal - rVal;
                        tpDiff = tpDiff * tpDiff;
                        // System.out.println("Mean "+i + " " + yMean );
                        double tpDiff2 = yMean - rVal;
                        tpDiff2 = tpDiff2 * tpDiff2;
                        
                        tpDiff = (double)tpDiff / tpDiff2;
                        
                        errors[i] = errors[i] + tpDiff;
                    } catch (Exception ex) {
                        Logger.getLogger(RBF.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                errors[i] = (double) errors[i] / iNum;
                errors[i] = Math.sqrt(errors[i]);
                //System.out.println("NRMSE"+i + " " +errors[i]);
                
            }
        }    
     for (int k = 1; k < clusterNum;k++) {
            nrmse = nrmse + ((errors[k]*errors[k])*cAmt[k]);
        }
        
        nrmse = (double) nrmse / tInstance;
        nrmse = Math.sqrt(nrmse);
        System.out.println("NRMSE " + nrmse);
        return nrmse;
        
    }
  
  
  private void buildRBF(int clusterNum) throws Exception {
          
            double t0 = System.currentTimeMillis();
            for (int i = 0; i < clusterNum ; i++) {
                FastVector fv;    
                fv = new FastVector(attributeNum-1);
                double[][] trainingDataArr = clusters[i].toArray();
               
                   tDtSet[i] =  new Instances("trainingData",fv,trainingDataArr.length);
                for(int j = 0; j < attributeNum-1; j++){
                    String temp = "X";
                    temp = temp+j;
                    Attribute attribute = new Attribute(temp);
                    tDtSet[i].insertAttributeAt(attribute, j);
                }
                Attribute attribute = new Attribute("Y");
                tDtSet[i].insertAttributeAt(attribute, attributeNum-1);
                for(int k = 0; k < trainingDataArr.length; k++){
                    Instance instance = new DenseInstance(trainingDataArr[k].length);
                    for(int l = 0; l < trainingDataArr[k].length; l++){
                        instance.setValue(l, trainingDataArr[k][l]);
                    }
                    tDtSet[i].add(instance);
                }

                tDtSet[i].setClassIndex(tDtSet[i].numAttributes() - 1);
                    rbfModel[i] = new RBFNetwork();
                    rbfModel[i].setNumClusters(k);
                    rbfModel[i].buildClassifier(tDtSet[i]);
                    
                  System.out.println("EQ "+i+ " "+ rbfModel[i]);   
               
                for (int s=0; s< tDtSet[i].numInstances(); s++) {
                    double pred = rbfModel[i].classifyInstance(tDtSet[i].instance(s));
                    
                   // System.out.println("Cluster"+i+ " pred"+s+" "+pred);
                
                
            }      
      }    
        timeCost = System.currentTimeMillis() - t0;
              System.out.println("Total time "+timeCost /1000 + "s");
              
            memoryUsed =  (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1024*1024);
           System.out.println("Memory "+memoryUsed);
  }
@Override
    public String equation() {
        for (int i = 0; i < clusterNum ; i++) {
            try {
                rbfModel[i].buildClassifier(tDtSet[i]);
            } catch (Exception ex) {
                Logger.getLogger(RBF.class.getName()).log(Level.SEVERE, null, ex);
            }
  }              
return this.rbfModel.toString();
   
  }
    /*
public static void main(String[] args){
    dataContainer dc=new dataContainer();
        Record2File.deleteFile();
        dc.readCSV("OnlineNewsPopularity.csv",",");
            
            //the 2,3,4 column of the csv file
        int[] index={2,3,4};
        dc.filterData(index);
      RBF rbf ;
      rbf=new RBF(dc.getRowsBetween(1800, 2400));
        try {
            rbf.run();
            
         //System.out.println(rbf.equation());
          
        } catch (Exception ex) {
            Logger.getLogger(RBF.class.getName()).log(Level.SEVERE, null, ex);
        }
}
 */

    
 public double getPredMean(){
        double yMean = 0.0;
        double ySum = 0.0;
        
        for (int i = 0 ; i < clusterNum ; i++) {
            int localClusterSize = tDtSet[i].size();
            tInstance = tInstance + localClusterSize;
            for (int j = 0; j < localClusterSize; j++) {
                ySum = ySum + tDtSet[i].get(j).classValue();
            }
        }
        yMean = (double) ySum / tInstance;
        
        return yMean;
    }

    @Override
    public void changeK(int i) {
        kkk=i;
    }


}



    /**
     *
     * @return
     */
   


