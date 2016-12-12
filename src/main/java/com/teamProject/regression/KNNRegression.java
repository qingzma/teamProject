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
    
    /** variable representing allocated points for KNN Regression Training **/
    private Points points;
    /** to define how many local cluster created **/
    private int clusterNum;
    /** variable for method name **/
    private String methodName = "KNN Regression";
    /** to define how many attribute used**/
    private int attributeNumber;
    /** cluster object **/
    private Cluster[] clusters;
    /** KNN regression  model object **/
    private IBk[] knnRegModel;
    /**  time cost for training the models **/
    private double timeCost;
    /**  memory used for training **/
    private long memoryUsed;
    /** number of nearest neighbors used **/
    private int k;
    /**  object for training instances **/
    private Instances trainingDataSet [];
    /** variable for total number of instance **/
    private int totalInstance;
    /** variable for meanY of all of the training dataset **/
    private double meanY;
    /** object for doing KMeans clustering **/
    private KMeans kMeans; 

    /**
     * This is a constructor method for this class
     * The method will initialize variables needed in the class
     * 
     * @param pts 
     */
    public KNNRegression(Points pts){
        points=pts;
        attributeNumber = pts.getDimension();
        totalInstance = pts.getPointNum();
        clusterNum = 5;
        k=3;
        clusters = new Cluster[clusterNum];
        knnRegModel = new IBk [clusterNum];
        trainingDataSet = new Instances[clusterNum];
        kMeans = new KMeans(points,clusterNum);
        kMeans.run();
        clusters = kMeans.getClusters();
    }
     
    @Override
    /**
     * Method used for getting the name of regression algorithm
     * 
     */
    public String equation() {
        return "KNN Regression with k= "+k;
    }

    @Override
    /**
     * Method used for getting total memory used in training phase
     * 
     */
    public long memory() {
        return memoryUsed;
    }

    @Override
    /**
     * Method used for getting regression algorithm name 
     * 
     */
    public String getMethodName() {
       return methodName;
    }

    @Override
    /**
     * Method used for building KNNRegression Models
     * 
     */
    public void run() {
        try {
            buildModel(clusterNum);
        } catch (Exception ex) {
            Logger.getLogger(KNNRegression.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    /**
     * Method used for predicting Y value of inputted X
     * 
     */
    public double fit(double[] x, int clusterIDNum){
        /** return value **/
        double fittedValue = 0.0;
        try{
            /**  create new instances for testing dataset **/
            Instances testingDataSet = new Instances("testingDataSet",new FastVector(x.length),1);
            /**  create and add X attributes for testing dataset **/
            for(int i = 0; i<x.length; i++){
                String temp = "X";
                temp = temp+i;
                Attribute attribute = new Attribute(temp);
                testingDataSet.insertAttributeAt(attribute, i);
            }
            /**  create and add Y attributes for testing dataset **/
            Attribute attribute = new Attribute("Y");
            testingDataSet.insertAttributeAt(attribute, x.length);
            /** create testing instance to be added in testing dataset **/
            Instance testingInstance = new DenseInstance(x.length + 1);
            /** set the value for testing instance**/
            for(int j = 0; j <x.length; j++){
                testingInstance.setValue(j, x[j]);
            }
            /** set value for attribute Y in testing dataset **/
            testingInstance.setValue(x.length,0);
            /** add testing instance into testing dataset**/
            testingDataSet.add(testingInstance);
            /** set the class index **/
            testingDataSet.setClassIndex(testingDataSet.numAttributes()-1);
            /** predict the Y value using KNN regression model based on supplied clusterIDNum **/
            fittedValue = knnRegModel[clusterIDNum].classifyInstance(testingDataSet.get(0));
            
        }catch(Exception ex){
           Logger.getLogger(KNNRegression.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fittedValue;
    }

    @Override
    /**
     * Method for getting clusters (cluster points and centroids)
     */
    public Cluster[] getClusters() {
        return clusters;
    }

    @Override
    /**
     * Method for getting timecost for training (in seconds)
     * 
     */
    public double timeCost() {
        return (double)timeCost/1000;
    }
    
    /**
     * Method used for training KNN Regression model
     * @param clusterNum
     * @throws Exception 
     */
    private void buildModel (int clusterNum) throws Exception {
        System.out.println("KNN training start");
        /** log starting time **/
        double startTrainingTime = System.currentTimeMillis();
        try{/** iteration for creating KNN Regression Model **/    
            for (int i = 0; i < clusterNum; i++) {
                /** create vector for attribute info**/
                FastVector vector = new FastVector(attributeNumber-1); 
                /** points in a cluster **/
                double[][] trainingDataArr = clusters[i].toArray();
                /** instantiate training dataset **/
                trainingDataSet[i] =  new Instances("trainingData",vector,trainingDataArr.length);
                /** create and add X attributes **/
                for(int j = 0; j < attributeNumber-1; j++){
                    String temp = "X";
                    temp = temp+j;
                    Attribute attribute = new Attribute(temp);
                    trainingDataSet[i].insertAttributeAt(attribute, j);
                }
                /** create and add Y attribute **/
                Attribute attribute = new Attribute("Y");
                trainingDataSet[i].insertAttributeAt(attribute, attributeNumber-1);
                /** create and add instance into training dataset **/
                for(int k = 0; k < trainingDataArr.length; k++){
                    Instance instance = new DenseInstance(trainingDataArr[k].length);
                    for(int l = 0; l < trainingDataArr[k].length; l++){
                        instance.setValue(l, trainingDataArr[k][l]);
                    }
                    trainingDataSet[i].add(instance);
                }
                /** set class index **/    
                trainingDataSet[i].setClassIndex(trainingDataSet[i].numAttributes() - 1);
                /** create new KNN Regression object **/
                knnRegModel[i] = new IBk();
                /** set number of K in KNN **/
                knnRegModel[i].setKNN(k);
                /** training the models based on training dataset **/
                knnRegModel[i].buildClassifier(trainingDataSet[i]);
                //System.out.println("neares"+knnRegModel[i].getNearestNeighbourSearchAlgorithm());
                //System.out.println("dis"+knnRegModel[i].getOptions());
                
                //String test [] = knnRegModel[i].getOptions();
                
                //for (int k = 0; k < test.length ; k++) {
                //    System.out.println("aaa"+test[k]);
                //}
            }
        }catch(Exception ex){
                Logger.getLogger(KNNRegression.class.getName()).log(Level.SEVERE, null, ex);
        }
        /** calculate timecost for training models **/
        timeCost = System.currentTimeMillis() - startTrainingTime;
        System.out.println("KNN training end");
        /** calculate value for meanY **/
        meanY = getMeanY();
        /** calculate memory used for training models in MB**/
        memoryUsed =  (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1024*1024);
    }
   
    @Override
    /**
     * Method for getting NRMSE
     * 
     */
    public double NRMSE() {
        /** return value of the method **/
        double nrmse = 0.0;
        /**  keeping errors for each cluster **/
        double errors [] = new double[clusterNum];
        /** keeping number of instances in each cluster **/
        int clusterAmount [] = new int [clusterNum];
        int clusterAmountUsed [] = new int [clusterNum];
        /** iteration for counting error in each cluster **/
        for (int i = 0; i < clusterNum; i++) {
            int instanceNum = trainingDataSet[i].size();
            clusterAmount[i] = instanceNum;
            if (instanceNum > 1) {
                for (int j = 0; j < instanceNum ; j++ ) {
                    try {
                        double realValue = trainingDataSet[i].get(j).classValue();
                        double predictedValue = knnRegModel[i].classifyInstance(trainingDataSet[i].get(j));
                        double tempDiffValue = predictedValue - realValue;
                        double tempDiffValue2 = meanY - realValue;
                        if (Math.abs(tempDiffValue2) > 0.008) {
                            tempDiffValue = tempDiffValue * tempDiffValue;
                            tempDiffValue2 = tempDiffValue2 * tempDiffValue2;
                            tempDiffValue = (double)tempDiffValue / tempDiffValue2;
                            errors[i] = errors[i] + tempDiffValue;
                            clusterAmountUsed[i]++;
                        }
                       
                    } catch (Exception ex) {
                        Logger.getLogger(KNNRegression.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                /** getting NRMSE for each cluster**/
                errors[i] = (double) errors[i] / clusterAmountUsed[i];
                errors[i] = Math.sqrt(errors[i]);
                
            }
        }
        
        for (int k = 0; k < clusterNum;k++) {
            nrmse = nrmse + ((errors[k]*errors[k])*clusterAmountUsed[k]);
        }
        int totalInstanceUsed = 0;
        for (int l = 0; l < clusterNum; l++) {
            totalInstanceUsed=totalInstanceUsed+clusterAmountUsed[l];
        }
         /** getting NRMSE for all of the cluster **/
        nrmse = (double) nrmse / totalInstanceUsed;
        nrmse = Math.sqrt(nrmse);
        //nrmse = (double)RMSE()/meanY;
        return nrmse;
    }

    @Override
    /**
     * Method for getting RMSE 
     */
    public double RMSE() {
        /** returned value **/
        double rmse = 0.0;
        /** getting errors from each clusters **/
        double errors [] = new double[clusterNum];
        /** keeping number of instances from clusters **/
        int clusterAmount [] = new int [clusterNum];
        /** calculating error for each clusters**/
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
                 /** getting RMSE for each cluster**/
                errors[i] = (double) errors[i] / instanceNum;
                errors[i] = Math.sqrt(errors[i]);
                
            }
        }
        
        for (int k = 0; k < clusterNum;k++) {
            rmse = rmse + ((errors[k]*errors[k])*clusterAmount[k]);
        }
        /** getting RMSE for all of the cluster **/
        rmse = (double) rmse / totalInstance;
        rmse = Math.sqrt(rmse);
        
        return rmse;
    }
    
    /**
     * Method for calculating meanY of all the training dataset
     * 
     */
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
