/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.regression;

import com.teamProject.cluster.Cluster;
import com.teamProject.data.Point;
import com.teamProject.data.Points;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.lazy.IBk;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

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
    private DataSource trainingDataSource [];
    private Instances trainingDataSet [];
    private double nrmse [];

     public KNNRegression(Points pts){
        points=pts;
        attributeNumber = 3;
        clusterNum = 5;
        k=3;
        clusters = new Cluster[clusterNum];
        knnRegModel = new IBk [clusterNum];
        trainingDataSource = new DataSource[clusterNum];
        trainingDataSet = new Instances[clusterNum];
        nrmse = new double[clusterNum];
        
        initialData = createArffFile(attributeNumber,points,"InitialData");
        writeToFile(initialData,"LocalClusterKNNRegression.arff");
        try {
            LocalCluster("LocalClusterKNNRegression.arff",clusterNum);
        } catch (Exception ex) {
            Logger.getLogger(KNNRegression.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public String equantion() {
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
            
        String arffContent = "";
        String relationName = "testing";
        arffContent = "@RELATION "+relationName+"\n";
        for (int i = 0; i < attributeNumber;i++) {
            arffContent = arffContent + "@ATTRIBUTE x"+i+" REAL"+"\n";
        }
        arffContent = arffContent + "@DATA"+"\n";
            
        double [] newX = new double [x.length+1];
        
        for (int i = 0; i < x.length;i++) {
            newX[i] = x[i];
        }
        
        newX[x.length-1] = 0.0;
        
        for (int j = 0; j < newX.length;j++) {
            if (j == (newX.length-1)) {
                arffContent = arffContent+newX[j]+"\n";
            } else {
                arffContent = arffContent+newX[j]+",";
            }
        }
            
            writeToFile(arffContent,"knnRegressionTesting.arff");
        try {
            
            
          DataSource testingDataSource = new DataSource(System.getProperty("user.dir")+"\\"+"knnRegressionTesting.arff");
          Instances testingDataSet = testingDataSource.getDataSet();
          testingDataSet.setClassIndex(testingDataSet.numAttributes()-1);
          
          double startPredictTime = System.currentTimeMillis();
          
          fittedValue = knnRegModel[clusterIDNum].classifyInstance(testingDataSet.get(0));
          
          timeCost = System.currentTimeMillis() - startPredictTime;
          memoryUsed = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1024*1024) ;
            
        } catch (Exception ex) {
            Logger.getLogger(KNNRegression.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fittedValue;
    }

    @Override
    public Cluster[] getClusters() {
        return clusters;
    }

    @Override
    public double[] getNRMSE() {
        double errors [] = new double[clusterNum];
        for (int i = 0; i < clusterNum; i++) {
            int instanceNum = trainingDataSet[i].size();
            double meanY = 0.0;
            if (instanceNum > 1) {
                for (int j = 0; j < instanceNum ; j++ ) {
                    try {
                        double realValue = trainingDataSet[i].get(j).classValue();
                        meanY = meanY + realValue;
                        double predictedValue = knnRegModel[i].classifyInstance(trainingDataSet[i].get(j));
                        double tempDiffValue = predictedValue - realValue;
                        tempDiffValue = tempDiffValue * tempDiffValue;
                        errors[i] = errors[i] + tempDiffValue;
                    } catch (Exception ex) {
                        Logger.getLogger(KNNRegression.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                meanY = (double) meanY / instanceNum;
                errors[i] = (double) errors[i] / instanceNum;
                errors[i] = Math.sqrt(errors[i]);
                if (meanY != 0.0) {
                   nrmse[i] = (double) errors[i] / meanY; 
                }
                
            }
        }
        
        return nrmse;
    }

    @Override
    public double timeCost() {
        return (double)timeCost/1000;
    }
    
    private String createArffFile (int numAttributes, Points points,String relationName) {
        String arffContent="";
        
        arffContent = "@RELATION "+relationName+"\n";
        for (int i = 0; i < numAttributes;i++) {
           arffContent = arffContent + "@ATTRIBUTE x"+i+" REAL"+"\n";
        }
        
        arffContent = arffContent + "@DATA"+"\n";
        
        for (int i = 0; i < points.getPointNum();i++) {
            for (int j = 0; j < points.get(i).getSize();j++) {
                if (j == (points.get(i).getSize()-1)) {
                    arffContent = arffContent+points.get(i).get(j)+"\n";
                } else {
                    arffContent = arffContent+points.get(i).get(j)+",";
                }
                
            }
        }
        
        return arffContent;
    }
    
    private void LocalCluster(String fileName,int clusterNum) throws Exception {
        
        DataSource dataSource = new DataSource(System.getProperty("user.dir")+"\\"+fileName);
        Instances data = dataSource.getDataSet();
        SimpleKMeans kMeans = new SimpleKMeans();
        kMeans.setPreserveInstancesOrder(true);
        kMeans.setNumClusters(clusterNum);
        kMeans.buildClusterer(data);
	Instances centroids = kMeans.getClusterCentroids();	
	int [] results = kMeans.getAssignments();
        String [] arffContent = new String[clusterNum];
        String relationName = "cluster";
        
        for (int i = 0; i < clusterNum; i++) {
             arffContent[i] = "@RELATION "+relationName+i+"\n";
             
             for (int j = 0; j < attributeNumber;j++) {
                arffContent[i] = arffContent[i] + "@ATTRIBUTE x"+j+" REAL"+"\n";
             }        
             arffContent[i] = arffContent[i] + "@DATA"+"\n";
        }
        
        for (int i = 0; i < clusterNum;i++) {
            clusters[i] = new Cluster(i);
            double temp [] = centroids.get(i).toDoubleArray();
            clusters[i].setCentroid(new Point(temp));
        }
        
        for (int i = 0; i < clusterNum ; i++) {
           
            for (int j = 0; j < data.size() ; j++) {
                if (results[j] == i) {
                   double temp [] = data.get(j).toDoubleArray();
                   clusters[i].addPoint(new Point(temp));
                   for (int k = 0; k < temp.length; k++) {
                        if (k == (temp.length-1)) {
                            arffContent[i] = arffContent[i]+temp[k]+"\n";
                        } else {
                            arffContent[i] = arffContent[i]+temp[k]+",";
                        }
                    } 
                }
            }
        }
        
        for (int i = 0; i < clusterNum;i++) {
            String localClusterName = "LocalClusterKnnRegression-";
            writeToFile(arffContent[i],localClusterName+i+".arff");
        }
        
    }
       
    private void buildModel (int clusterNum) throws Exception {
        
        String localClusterName = "LocalClusterKnnRegression-";
        
        for (int i = 0; i < clusterNum ; i++) {
            trainingDataSource[i] = new DataSource(System.getProperty("user.dir")+"\\"+localClusterName+i+".arff");
            trainingDataSet[i] =  trainingDataSource[i].getDataSet();
            trainingDataSet[i].setClassIndex(trainingDataSet[i].numAttributes()-1);
            knnRegModel[i] = new IBk();
            knnRegModel[i].setKNN(k);
            knnRegModel[i].buildClassifier(trainingDataSet[i]);
        }
        
        /*DataSource testingDataSource = new DataSource(System.getProperty("user.dir")+"\\"+"testing.arff");
	Instances testingDataSet = testingDataSource.getDataSet();
	testingDataSet.setClassIndex(testingDataSet.numAttributes()-1);
        
        double predictedValue = knnRegModel[1].classifyInstance(testingDataSet.get(0));
        
        System.out.println(predictedValue);*/
    }
    
    
    private void writeToFile(String content, String fileName) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"\\"+fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			writer.write(content);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
