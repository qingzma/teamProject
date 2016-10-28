/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author qingzhi
 */
public class dataContainer {
    private Points X; //=new Points();
    private Points Y; //=new Points();
    private Points data;     //=new Points();
    //private int  iXDimension=2;
    private int iDataWidth=3;
    
    public void readFile(String file,String splitStr){
        //X=new Points();
        //Y=new Points();
        data=new Points();
        Point pt;
        //Point pointX;
        //Point pointY;
        try{
            
            //ArrayRow.clear();
            Scanner lineScanner;
            Scanner s=new Scanner(new FileInputStream(new File("src/main/resources/"+file)));
            while(s.hasNext()){
                pt=new Point();
                String lineStr=s.nextLine();
                String str=lineStr.replaceAll(splitStr  ,  " ");
                lineScanner=new Scanner(str);
            
                for(int i=0;i<iDataWidth;i++){
                   pt.add(lineScanner.nextDouble());
                }
                
                data.add(pt);
                
            }
        } catch (IOException e){
            System.out.println("input file not found!");
        }
        
        iDataWidth=data.get(0).getSize();
    }
    
    public void filterData(){
        data.removeAt(1);
        iDataWidth--;
        //iXDimension--;
    }
    
    public void showData(){
        for(int i=0;i<getPointNum();i++){
            System.out.println(data.get(i).getPoint());
        }
    }
    
    //public void setXDimension(int n){
    //    iXDimension=n;
    //}
    
    public void setDataWidth(int n){
        iDataWidth=n;
    }
    
    public int getPointNum(){
        return data.getPointNum();
    }
    
   
    public Points getData(){
        return data.clone();
    }
    
    public Points getRowsBefore(int cutIndex){
        if(cutIndex>data.getPointNum()){
            System.out.println("in dataContainer: cutIndex beyond bounds, "
                    + "so adjust it automatically ");
            cutIndex=data.getPointNum();
        }
        Points newPt=new Points();
        for(int i=0;i<cutIndex;i++){
            newPt.add(data.get(i));
        }
        
        return newPt;
    }
    
    public Points getRowsAfter(int cutIndex){
        if(cutIndex>data.getPointNum()){
            System.out.println("in dataContainer: cutIndex beyond bounds, "
                    + "so adjust it automatically ");
            cutIndex=data.getPointNum();
        }
        Points newPt=new Points();
        for(int i=cutIndex;i<data.getPointNum();i++){
            newPt.add(data.get(i));
        }
        
        return newPt;
    }
    
    /*
    public void generateXY(){
        X=new Points();
        Y=new Points();
        Point pointX;
        Point pointY;
        
        for(int i=0;i<getPointNum();i++){
            pointX=new Point();
            pointY=new Point();
            for(int j=0;j<iDataWidth-1;j++){
                   pointX.add(data.get(i).get(j));
            }
            pointY.add(data.get(i).get(iDataWidth-1));
            
            
            X.add(pointX);
            Y.add(pointY);
        
        }
        
    }
    
    
    
    public void showXY(){
        generateXY();
        for(int i=0;i<X.getPointNum();i++){
            System.out.print(X.get(i).getPoint());
            System.out.println(Y.get(i).getPoint());
        }
    }
    */
    
    /*
    public Points getX(){
        generateXY();
        return X;
    }
    
    public Points getY(){
        generateXY();
        return Y;
    }
    */
    
}
