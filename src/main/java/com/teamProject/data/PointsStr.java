/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.data;

import com.teamProject.Record2File;
import java.util.ArrayList;

/**
 *
 * @author qingzhi
 */
public class PointsStr {
    private ArrayList<ArrayList<String>> pointsStr=new ArrayList<>();
    private PointStr header;
    public PointsStr(){
        
    }
    
    
    public void add(PointStr p){
        pointsStr.add(p.getPointStr());
    }
    
    public void add(double[] d){
        PointStr p=new PointStr(d);
        pointsStr.add(p.getPointStr());
    }
    
    public void add(ArrayList<String> al){
        PointStr p=new PointStr(al);
        pointsStr.add(p.getPointStr());
    }
    
    
    public PointStr get(int i){
        //System.out.println("in PointsStr: get()");
        //System.out.println(pointsStr.get(i));
        //pointsStr.get(i)
        //PointStr pt1=new PointStr(pointsStr.get(i));
        return new PointStr(pointsStr.get(i));
        //return points.
    }
    
    public ArrayList<String> getTarget(int i){
        return pointsStr.get(i);
    }
    
    public int getPointNum(){
        //int x=points.size()/points.get(0).size();
        //System.out.println("size is: "+x);
        return pointsStr.size();
    }
    
    public void clear(){
        pointsStr.clear();
    }
    
    public int getDimension(){
        return pointsStr.get(0).size();
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
    
    public PointsStr clone(){
        PointsStr cln=new PointsStr();
        
        PointStr pt;

        
        for(int i=0;i<getPointNum();i++){
            pt=new PointStr();

            for(int j=0;j<getDimension();j++){
                   pt.add(pointsStr.get(i).get(j));
            }
            cln.add(pt);
        }
        return cln;
    }
    
    
/*    public Points getX(){
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
*/   
    public void plotPoints(){
        for(int i=0;i<getPointNum();i++){
            System.out.println(pointsStr.get(i));
        }
    }
    
    
    public double[][] toArray(){
        int xIndex=getPointNum();
        int yIndex=getDimension();
        
        
        double[][] array=new double[xIndex][yIndex];
        try{
        for(int i=0;i<xIndex;i++){
            for(int j=0;j<yIndex;j++){
                array[i][j]=Double.parseDouble( pointsStr.get(i).get(j));
            }
        }
        } catch(NumberFormatException e){
            System.err.println("PointsStr: toArray(): cant convert str to number.");
            System.exit(1);
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
        try{
        for(int i=0;i<xIndex;i++){
            array[i]=Double.parseDouble( pointsStr.get(i).get(0)); 
        }
        }
        catch(NumberFormatException e){
            
        }
        return array;   
    }
    
    public boolean isEmpty(){
        return pointsStr.isEmpty();
    }
    
    
    public PointsStr column(int num){
        PointsStr columnStr=new PointsStr();
        PointStr pt;
        if(num>=getDimension()){
            System.err.println("dataContainer:getColumn(): error, column inedex"
                    + "out of range!");
            Record2File.normal("dataContainer:getColumn(): error, column inedex"
                    + "out of range!");
            Record2File.error("dataContainer:getColumn(): error, column inedex"
                    + "out of range!");
            System.exit(-1);
        }
        else{
            for(int i=0;i<getPointNum();i++){
                pt=new PointStr(pointsStr.get(i).get(num));
                columnStr.add(pt);
            }
        }
        
        //set the header of the column
        String[] columnHeader=new String[1];
        columnHeader[0]=header.get(num);
        columnStr.setHeader(columnHeader);
        return columnStr;
    }
    
    public PointsStr column(String str){
        int num=header.indexOf(str);
        if(num==-1){
            Record2File.error("PointsStr:column(): column does not exist!");
            System.exit(-1);
        }
        return column(num);
    }
    
    public void addColumn(PointsStr pts){
        
        int num=getPointNum();
        //System.out.println(num);
        for(int i=0;i<num;i++){
            getTarget(i).add(pts.get(i).get(0));
        }
        header.add(pts.header().get(0));
    }
    
    
    
    public void setHeader(String[] str){
        header=new PointStr();
        for (String str1 : str) {
            header.add(str1);
        }
    }
    
    public PointStr header(){
        return header;
    }
    
    public Points generateTargetPoints(int[] index){
        //Points pts=new Points();
        PointsStr ptsStr=column(index[0]);
        for(int i=1;i<index.length;i++){
            //System.out.println("ptsStr row number "+ptsStr.getPointNum());
            ptsStr.addColumn(column(index[i]));
        }
        
        return ptsStr.toPoints();
    }
    
    public Points toPoints(){
        Points pts=new Points();
        Point pt;
        double value;
        try{
            for(int i=0;i<getPointNum();i++){
                pt=new Point();
                for(int j=0;j<getDimension();j++)
                    pt.add(Double.parseDouble(pointsStr.get(i).get(j)));
                
                pts.add(pt);

            }
        } catch(NumberFormatException e){
            Record2File.error("PointsStr->toPoints: string cant convert to double.");
        }
        return pts;
    }

}
