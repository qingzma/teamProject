/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.regression;

import com.teamProject.cluster.Cluster;
import com.teamProject.cluster.KMeans;
import com.teamProject.data.Point;
import com.teamProject.data.Points;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Timotius Nico
 */
public class KNNRegression implements RegressionInterface {
    
    private Points points;
    private int clusterNum;
    private String methodName = "KNN Regression";
    private String initialData;
    private int attributeNumber;
    private Cluster[] clusters;
    private IBk[] knnRegModel;
    private double timeCost;
    private long memoryUsed;
    private int k;
    private Instances trainingDataSet [];
    private int totalInstance;
    private double meanY;
    private KMeans kMeans; 

     public KNNRegression(Points pts){
        points=pts;
        attributeNumber = pts.getDimension();
        totalInstance = pts.getPointNum();
        clusterNum = 5;
        k=1;
        clusters = new Cluster[clusterNum];
        knnRegModel = new IBk [clusterNum];
        trainingDataSet = new Instances[clusterNum];
        kMeans = new KMeans(points,clusterNum);
        kMeans.run();
        clusters = kMeans.getClusters();
    }
     
    @Override
    public String equation() {
        return "KNN Regression with k= "+k;
    }

    @Override
    public long memory() {
        return memoryUsed;
    }

    @Override
    public String getMethodName() {
       return methodName;
    }

    @Override
    public void run() {
        try {
            buildModel(clusterNum);
        } catch (Exception ex) {
            Logger.getLogger(KNNRegression.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public double fit(double[] x, int clusterIDNum){
        double fittedValue = 0.0;
        double [] newX = new double [x.length+1];
        for (int i = 0; i < x.length; i++) {
            newX[i] = x[i];
        }
        newX[x.length-1] = 0.0;
        try{ 
            Instance instance = new DenseInstance(newX.length);
            for(int i = 0; i < newX.length; i++){
                instance.setValue(0, newX[i]);
            }
            
            Instances testingDataSet = new Instances("testData",new FastVector(),1);
            for(int j = 0; j < attributeNumber-1; j++){
                String temp = "X";
                temp = temp+j;
                Attribute attribute = new Attribute(temp);
                testingDataSet.insertAttributeAt(attribute, j);
           }
           Attribute attribute = new Attribute("Y");
           testingDataSet.insertAttributeAt(attribute, attributeNumber-1);
           testingDataSet.add(instance);
           testingDataSet.setClassIndex(testingDataSet.numAttributes()-1);
           fittedValue = knnRegModel[clusterIDNum].classifyInstance(testingDataSet.get(0));
           
        }catch(Exception ex){
             Logger.getLogger(KNNRegression.class.getName()).log(Level.SEVERE, null, ex);
        }          
        return fittedValue;
    }

    @Override
    public Cluster[] getClusters() {
        return clusters;
    }

    @Override
    public double timeCost() {
        return (double)timeCost/1000;
    }
       
    private void buildModel (int clusterNum) throws Exception {
        double startTrainingTime = System.currentTimeMillis();
        try{    
                for (int i = 0; i < clusterNum; i++) {
                FastVector fv = new FastVector(attributeNumber-1);    
                double[][] trainingDataArr = clusters[i].toArray();
                trainingDataSet[i] =  new Instances("trainingData",fv,trainingDataArr.length);
                for(int j = 0; j < attributeNumber-1; j++){
                    String temp = "X";
                    temp = temp+j;
                    Attribute attribute = new Attribute(temp);
                    trainingDataSet[i].insertAttributeAt(attribute, j);
                }
                Attribute attribute = new Attribute("Y");
                trainingDataSet[i].insertAttributeAt(attribute, attributeNumber-1);
                for(int k = 0; k < trainingDataArr.length; k++){
                    Instance instance = new DenseInstance(trainingDataArr[k].length);
                    for(int l = 0; l < trainingDataArr[k].length; l++){
                        instance.setValue(l, trainingDataArr[k][l]);
                    }
                    trainingDataSet[i].add(instance);
                }

                trainingDataSet[i].setClassIndex(trainingDataSet[i].numAttributes() - 1);
                knnRegModel[i] = new IBk();
                knnRegModel[i].setKNN(k);
                knnRegModel[i].buildClassifier(trainingDataSet[i]);           
            }
        }catch(Exception ex){
                Logger.getLogger(KNNRegression.class.getName()).log(Level.SEVERE, null, ex);
        }
        timeCost = System.currentTimeMillis() - startTrainingTime;
        meanY = getMeanY();
        memoryUsed =  (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1024*1024);
    }
   
    @Override
    public double NRMSE() {
        double nrmse = 0.0;
        double errors [] = new double[clusterNum];
        int clusterAmount [] = new int [clusterNum];
        for (int i = 0; i < clusterNum; i++) {
            int instanceNum = trainingDataSet[i].size();
            clusterAmount[i] = instanceNum;
            if (instanceNum > 1) {
                for (int j = 0; j < instanceNum ; j++ ) {
                    try {
                        double realValue = trainingDataSet[i].get(j).classValue();
                        double predictedValue = knnRegModel[i].classifyInstance(trainingDataSet[i].get(j));
                        double tempDiffValue = predictedValue - realValue;
                        tempDiffValue = tempDiffValue * tempDiffValue;
                        
                        double tempDiffValue2 = meanY - realValue;
                        tempDiffValue2 = tempDiffValue2 * tempDiffValue2;
                        
                        tempDiffValue = (double)tempDiffValue / tempDiffValue2;
                        
                        errors[i] = errors[i] + tempDiffValue;
                    } catch (Exception ex) {
                        Logger.getLogger(KNNRegression.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                errors[i] = (double) errors[i] / instanceNum;
                errors[i] = Math.sqrt(errors[i]);
                
            }
        }
        
        for (int k = 0; k < clusterNum;k++) {
            nrmse = nrmse + ((errors[k]*errors[k])*clusterAmount[k]);
        }
        
        nrmse = (double) nrmse / totalInstance;
        nrmse = Math.sqrt(nrmse);
        
        return nrmse;
    }

    @Override
    public double RMSE() {
        double rmse = 0.0;
        double errors [] = new double[clusterNum];
        int clusterAmount [] = new int [clusterNum];
        for (int i = 0; i < clusterNum; i++) {
            int instanceNum = trainingDataSet[i].size();
            clusterAmount[i] = instanceNum;
            errors[i] = 0.0;
            //System.out.println("cn"+i+"-"+instanceNum);
            if (instanceNum > 1) {
                for (int j = 0; j < instanceNum ; j++ ) {
                    try {
                        double realValue = trainingDataSet[i].get(j).classValue();
                        double predictedValue = knnRegModel[i].classifyInstance(trainingDataSet[i].get(j));
                        double tempDiffValue = predictedValue - realValue;
                        tempDiffValue = tempDiffValue * tempDiffValue;
                        errors[i] = errors[i] + tempDiffValue;
                    } catch (Exception ex) {
                        Logger.getLogger(KNNRegression.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                errors[i] = (double) errors[i] / instanceNum;
                errors[i] = Math.sqrt(errors[i]);
                
            }
        }
        
        for (int k = 0; k < clusterNum;k++) {
            rmse = rmse + ((errors[k]*errors[k])*clusterAmount[k]);
        }
        
        rmse = (double) rmse / totalInstance;
        rmse = Math.sqrt(rmse);
        
        return rmse;
    }
    
    public double getMeanY(){
        double meanY = 0.0;
        double sumY = 0.0;
        
        for (int i = 0 ; i < clusterNum ; i++) {
            int localClusterSize = trainingDataSet[i].size();
            totalInstance = totalInstance + localClusterSize;
            for (int j = 0; j < localClusterSize; j++) {
                sumY = sumY + trainingDataSet[i].get(j).classValue();
            }
        }
        meanY = (double) sumY / totalInstance;
        /*
        System.out.println(trainingDataSet[0].size());
        for (int i = 0; i < trainingDataSet[0].size() ; i++) {
            System.out.println("train0"+i+" "+trainingDataSet[0].get(i).classValue());
            System.out.println("train0"+i+" "+trainingDataSet[0].attribute("x0"));
        }
        System.out.println(trainingDataSet[1].size());
        System.out.println(trainingDataSet[2].size());
        System.out.println(trainingDataSet[3].size());
        System.out.println(trainingDataSet[4].size());*/
        
        return meanY;
    }


}
