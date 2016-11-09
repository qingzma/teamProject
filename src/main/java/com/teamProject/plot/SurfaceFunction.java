/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.plot;

import com.teamProject.cluster.Cluster;
import org.jzy3d.plot3d.builder.Mapper;
import com.teamProject.data.Point;
import com.teamProject.regression.RegressionInterface;

/**
 *
 * @author qingzhi
 */
public class SurfaceFunction extends Mapper {
    Cluster cluster;
    RegressionInterface ri;
    int clusterIDNum;
    public SurfaceFunction(Cluster cls){
        cluster=cls;
    }
    
    public SurfaceFunction(int clusterID,RegressionInterface ri){
        clusterIDNum=clusterID;
        this.ri=ri;
    }

    @Override
    public double f(double x, double y) {
        if(cluster!=null){
            Point pt=new Point();
            pt.add(x);
            pt.add(y);
        
            //return cluster.fit(pt);
            return 1;
        }
        else{
            double xx[]=new double[2];
            xx[0]=x;
            xx[1]=y;
            return ri.fit(xx,clusterIDNum);
        }
        
        
    }
    
}
