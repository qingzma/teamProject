/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.data;

import java.util.ArrayList;

/**
 *
 * @author qingzhi
 */
public class Points {
    public ArrayList<ArrayList<Double>> points=new ArrayList<>();
    public Points(){
        
    }
    
    public void add(Point p){
        points.add(p.getPoint());
    }
    
    public void add(double[] d){
        Point p=new Point(d);
        points.add(p.getPoint());
    }
    
    public void add(ArrayList<Double> al){
        Point p=new Point(al);
        points.add(p.getPoint());
    }
    
    
    public Point get(int i){
        return new Point(points.get(i));
        //return points.
    }
    
    public ArrayList<Double> getTarget(int i){
        return points.get(i);
    }
    
    public int getPointNum(){
        //int x=points.size()/points.get(0).size();
        //System.out.println("size is: "+x);
        return points.size();
    }
    
    public void clear(){
        points.clear();
    }
    
    public int getDimension(){
        return points.get(0).size();
    }
    //public Points clone(){
     //   return (Points)points.clone();
    //}
    
    public void removeAt(int n){
        for(int i=0;i<getPointNum();i++){
            getTarget(i).remove(n);
        }
        
    }
    
    //public Points clone(){

    //}
    
    public Points clone(){
        Points cln=new Points();
        
        Point pt;

        
        for(int i=0;i<getPointNum();i++){
            pt=new Point();

            for(int j=0;j<getDimension();j++){
                   pt.add(points.get(i).get(j));
            }
            cln.add(pt);
        }
        return cln;
    }
    
    
    public Points getX(){
        Points x=clone();
        x.removeAt(getDimension()-1);
        return x;
    }
    
    public Points getY(){
        Points y=clone();
        for(int i=getDimension()-2;i>=0;i--){
            y.removeAt(i);
        }
        
        return y;
    }
    
    public void plotPoints(){
        for(int i=0;i<getPointNum();i++){
            System.out.println(points.get(i));
        }
    }
    
    
    public double[][] toArray(){
        int xIndex=getPointNum();
        int yIndex=getDimension();
        
        
        double[][] array=new double[xIndex][yIndex];
        for(int i=0;i<xIndex;i++){
            for(int j=0;j<yIndex;j++){
                array[i][j]=points.get(i).get(j);
            }
        }
        
        return array;   
    }
    
    public double[] toVector(){
        int xIndex=getPointNum();
        int yIndex=getDimension();
        if(yIndex!=1){
            System.err.println(" array size mismatch!cant convert to vector."
                    + " check class: Points->toVector() ");
            System.exit(-1);
        }
        
        double[] array=new double[xIndex];
        for(int i=0;i<xIndex;i++){
            array[i]=points.get(i).get(0); 
        }
        
        return array;   
    }
    
    public boolean isEmpty(){
        return points.isEmpty();
    }
    
}
