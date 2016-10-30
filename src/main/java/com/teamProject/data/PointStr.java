/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.data;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author qingzhi
 */
public class PointStr {
    private int m_size;
    private ArrayList<String> pointStr;
    private String[] header;
    private int clusterNum=0;
    
    public PointStr(){
        pointStr=new ArrayList<>();
        m_size=0;
    }
    
    public PointStr(PointStr p){
        //System.out.println("in PointStr,PointStr");
        this.pointStr=(ArrayList<String>) p.getPointStr().clone();
        m_size=p.getSize();
    }
    

    
    public PointStr(ArrayList<String> al){
        this();
        for(int i=0;i<al.size();i++){
            pointStr.add(al.get(i));
        }
        m_size=al.size();
    }
    
    
    public PointStr(String[] str){
        this();
        pointStr.addAll(Arrays.asList(str));
        m_size=pointStr.size();
        
    }
    
    
    public PointStr(double[] doubleArray){
        m_size=doubleArray.length;
        
        for(int i=0;i<m_size;i++){
            pointStr.add(Double.toString(doubleArray[i]));
        }
    }
    
    
    public PointStr(String str){
        //pointStr.add(Double.toString(d));
        //m_size++;
        this();
        pointStr.add(str);
    }
    
    
    public int getSize(){
        return m_size;
    }
    
    public void setSize(int n){
        m_size=n;
    }
    
    public ArrayList<String> getPointStr(){
        //System.out.println("in PointStr,getPointStr");
        return pointStr;
        
    }
    
    public String get(int i){
        return pointStr.get(i);
    }
    
    public void add(double d){
        pointStr.add(Double.toString(d));
        m_size++;
    }
    
    public void add(String str){
        pointStr.add(str);
        m_size++;
    }
    
    
    
    public PointStr clone(){
        return new PointStr((ArrayList<String>)pointStr.clone());
    }
    
    public void clear(){
        pointStr.clear();
    }
    
    public void set(PointStr p){
        m_size=p.getSize();
        //this.point=(ArrayList<Double>) p.getPoint().clone();
        this.pointStr=p.getPointStr();
    }
    
    public void setClusterNum(int n){
        this.clusterNum=n;
    }
    
    public void printPointStr(){
        System.out.print("Point: [");
        for(int i=0;i<pointStr.size();i++){
            System.out.print(pointStr.get(i)+" , ");
        }
        System.out.println("]");
    }
    
    public void RemoveAt(int n){
        pointStr.remove(n);
        m_size--;
    }
    
    public double[] toArray(){
        double[] array=new double[pointStr.size()];
        try{
            
            for(int i=0;i<pointStr.size();i++){
                array[i]=Double.parseDouble(pointStr.get(i));
            }
        }catch(NumberFormatException e){
            System.err.println("PointsStr: toArray(): can't convert str to double");
            System.exit(1);
        }
        
        return array;
    }
    
    public int indexOf(String str){
        /**
        *return the index of first str in PointStr
        * -1 when no result
        */
        return pointStr.indexOf(str);
        
    }
    
/*    public boolean in(PointStr[] range){
        
        
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
            if(pointStr.get(i)>=range[i].get(0) &&
                    pointStr.get(i)<=range[i].get(1))
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
        return result;
    }
    
    
    public double sum(){
        
        return Arrays.stream(toArray()).sum();
    }
*/
}
