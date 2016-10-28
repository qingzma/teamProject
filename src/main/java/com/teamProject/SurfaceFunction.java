/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject;

import org.jzy3d.plot3d.builder.Mapper;

/**
 *
 * @author qingzhi
 */
public class SurfaceFunction extends Mapper {
    Cluster cluster;
    public SurfaceFunction(Cluster cls){
        cluster=cls;
    }

    @Override
    public double f(double x, double y) {
        Point pt=new Point();
        pt.add(x);
        pt.add(y);
        
        return cluster.fit(pt);
    }
    
}
