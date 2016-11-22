/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.data;

import com.teamProject.Record2File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author qingzhi
 */
public class Point {
    private int m_size;
    private ArrayList<Double> point;
    private int clusterNum=0;
    
    public Point(){
        point=new ArrayList<>();
        m_size=0;
    }
    
    public Point(Point p){
        point=(ArrayList<Double>) p.getPoint().clone();
        m_size=p.getSize();
    }
    

    
    public Point(ArrayList<Double> al){
        this();
        for(int i=0;i<al.size();i++){
            point.add(al.get(i));
        }
        m_size=al.size();
    }
    
    
    public Point(double[] doubleArray){
        this();
        m_size=doubleArray.length;
        
        for(int i=0;i<m_size;i++){
            point.add(doubleArray[i]);
        }
    }
    
    
    //public Point(double d){
    //    point.add(d);
    //    m_size++;
    //}
    
    
    public int getSize(){
        return m_size;
    }
    
    public void setSize(int n){
        m_size=n;
    }
    
    public ArrayList<Double> getPoint(){
        return point;
        
    }
    
    public double get(int i){
        return point.get(i);
    }
    
    public void add(double d){
        point.add(d);
        m_size++;
    }
    
    @Override
    public Point clone(){
        
        return new Point((ArrayList<Double>)point.clone());
    }
    
    public void clear(){
        point.clear();
    }
    
    public void set(Point p){
        m_size=p.getSize();
        //this.point=(ArrayList<Double>) p.getPoint().clone();
        this.point=p.getPoint();
    }
    
    public void setClusterNum(int n){
        this.clusterNum=n;
    }
    
    
    
    public void RemoveAt(int n){
        point.remove(n);
        m_size--;
    }
    
    public double[] toArray(){
        double[] array=new double[point.size()];
        for(int i=0;i<point.size();i++){
            array[i]=point.get(i);
        }
        
        return array;
    }
    
    public boolean in(Point[] range){
        
        
        int rangeDimension=getSize();
        if(rangeDimension==0){
            System.err.println("error occur in class Point, Method in().");
            System.exit(1);
        }
        boolean[] isIn= new boolean[rangeDimension];
        
        for(int i=0;i<rangeDimension;i++){
            //boolean hs=point.get(i)>=range[i].get(0);
            //boolean hh=point.get(i)<=range[i].get(i);
            //boolean a= hs&&hh;
            isIn[i]=false;
            if(point.get(i)>=range[i].get(0) &&
                    point.get(i)<=range[i].get(1))
                isIn[i]=true;
        }
        
        
        boolean isIns=true;
        for(int i=0;i<rangeDimension;i++){
            isIns=isIns&&isIn[i];
        }
        
       // if(isIns)
       //     System.out.println("true, point"+point.get(0)+" in "+range[0].get(0)+"~"+range[0].get(1));
        return isIns;
    }
    
    public Point getXPoint(){
        //return the first n-1 column of point, which is point X
        Point pt=clone();
        pt.RemoveAt(pt.getSize()-1);
        return pt;
    }
    
    public double getYValue(){
        //return the last column of point, which is point Y
        return point.get(point.size()-1);
        
    }
    
    
    public double distanceTo(Point x2){
        double result=0;
        int size=getSize();
        for(int i=0;i<size;i++){
            result=result+(point.get(i)-x2.get(i))*(point.get(i)-x2.get(i));
            
        }
        result=Math.sqrt(result);
        
        if(point.size()!=x2.getSize()){
            Record2File.error("Point: distanceTo():  Point dimension mismatch!");
            System.exit(-1);
        }
            
        return result;
    }
    
    
    public double sum(){
        
        return Arrays.stream(toArray()).sum();
    }
    
    @Override
    public String toString(){
        String str="[";
        for(int i=0;i<point.size()-1;i++){
            str+=Record2File.double2str(point.get(i));
            str+=",";
        }
        str+=Record2File.double2str(point.get(point.size()-1));
        str+="]";
        
        return str;
    }
    
    public void plotPoint(){
        //System.out.println("Point: ");
        //for(int i=0;i<point.size();i++){
        //    System.out.print(point.get(i));
        //}
        //System.out.println("");
        Record2File.out(point.toString());
    }
}
