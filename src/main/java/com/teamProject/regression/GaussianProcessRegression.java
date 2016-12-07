/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.regression;

import com.teamProject.cluster.Cluster;
import com.teamProject.cluster.KMeans;
import com.teamProject.data.Points;
import weka.classifiers.functions.GaussianProcesses;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author wangxi
 */
public class GaussianProcessRegression implements RegressionInterface{
    
    private long memoryCost;
    private double timeCost;
    public Points points;
    private KMeans km;
    private int clusterNum = 5;
    private GaussianProcesses[] gp = new GaussianProcesses[clusterNum];
    private Cluster[] clusters;
    
    public GaussianProcessRegression(Points pts){
        points = pts;
    }
    @Override
    public String equation() {
        return null;
    }
    
    @Override
    public long memory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getMethodName() {
        return "GaussianProcessRegression";
    }
    
    @Override
    public void run() {
        Runtime runtime = Runtime.getRuntime();
        long start = System.currentTimeMillis();
        km = new KMeans(points,clusterNum);
        km.run();
        clusters = km.getClusters();
        try{
            for (int i = 0;i<clusterNum;i++) {
                double[][] trainData = clusters[i].toArray();
                //              for(int k = 0;k<trainData.length;k++){
                //                  for(int x = 0;x<trainData[k].length;x++){
                //                        System.out.print(" " + trainData[k][x]);
                //                  }
                //                  System.out.println();
                //              }
                //System.out.println(trainData[0].length);
                FastVector fv = new FastVector(trainData[0].length-1);
                Instances data = new Instances("trainData",fv,trainData.length);
                for(int k = 0;k<trainData[0].length-1;k++){
                    StringBuilder sb = new StringBuilder();
                    sb.append("x");
                    sb.append(k);
                    String s = sb.toString();
                    Attribute attr = new Attribute(s);
                    data.insertAttributeAt(attr, k);
                }
                Attribute attr = new Attribute("y");
                data.insertAttributeAt(attr, trainData[0].length-1);
                for(int k = 0;k<trainData.length;k++){
                    Instance inst = new DenseInstance(trainData[k].length);
                    for(int x = 0;x<trainData[k].length;x++){
                        inst.setValue(x, trainData[k][x]);
                    }
                    data.add(inst);
                }
                //for(int t = 0;t<data.numAttributes();t++)
                //	System.out.println("Attributes:"+ data.attribute(t));
                //                System.out.println(data.size());
                for(int x = 0;x<data.size();x++){
                    for(int y = 0;y<data.get(x).numValues();y++){
                        //////////////////System.out.println(data.get(x).value(y)+" ");
                    }
                    ///////////////////System.out.println();
                }
                //System.out.println("DATAEMPTY:" + data.isEmpty());
                //System.out.println(data.toString());
                //System.out.println(data);
                data.setClassIndex(data.numAttributes() - 1);
                gp[i] = new GaussianProcesses();
                gp[i].buildClassifier(data);
                //System.out.println(data.toString());
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        timeCost = end - start;
        memoryCost = runtime.totalMemory() - runtime.freeMemory();
    }
    
    @Override
    public double fit(double[] x, int clusterIDNum) {
        
        try{
            Instances test = new Instances("testData",new FastVector(x.length),1);
            Instance inst = new DenseInstance(x.length + 1);
            int i;
            for(int k = 0;k<x.length;k++){
                StringBuilder sb = new StringBuilder();
                sb.append("x");
                sb.append(k);
                String s = sb.toString();
                Attribute attr = new Attribute(s);
                test.insertAttributeAt(attr, k);
            }
            Attribute attr = new Attribute("y");
            test.insertAttributeAt(attr, x.length);
            for(i = 0;i<x.length;i++){
                inst.setValue(i, x[i]);
            }
            inst.setValue(x.length,0);
            test.add(inst);
            double[][] result = new double[1][2];
            //for(i = 0;i<x.length;i++){
            //    System.out.println(x[i]);
            //}
            test.setClassIndex(test.numAttributes()-1);
            result = gp[i].predictIntervals(test.get(0), 0.05);
            //System.out.println("result[0]: " + result[0][0] + " result[1]: "+ result[0][1]);
            //System.out.println("result"+(result[0][0]+result[0][1])/2);
            return (result[0][0]+result[0][1])/2;
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0.0;
    }
    
    @Override
    public Cluster[] getClusters() {
        return clusters;
    }
    
    @Override
    public double NRMSE() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public double RMSE() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public double timeCost() {
        return timeCost;
    }
    
}
