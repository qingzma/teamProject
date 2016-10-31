/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject.data;

import com.opencsv.CSVReader;
import com.teamProject.Record2File;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author qingzhi
 */
public class dataContainer {
    //private Points X; //=new Points();
    //private Points Y; //=new Points();
    private PointsStr fileData; 
    private Points data;        //to store  X,Y data points,(point1, x1,x2,..y)
    //private PointStr fileHeader;
    private PointStr header;
    //private int  iXDimension=2;
    private int iDataWidth;
    private int fileWidth;
    private int fileRowNum;
    private double t1,t0;
    
    public void readFile(String file,String splitStr){
        t0=System.currentTimeMillis();
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
        t1=System.currentTimeMillis();
    }
    
    
    public void readCSV(String file, String splitStr){
        Record2File.out("Reading file starting...");
        t0=System.currentTimeMillis();
        fileData=new PointsStr();
        PointStr pt;
        
        try{
            //open csv file
            CSVReader csvReader = new CSVReader(new FileReader("src/main/resources/"+file));
            String [] nextLine;
            
            //read fileHeader
            if((nextLine=csvReader.readNext())!=null){
                fileWidth=nextLine.length;
                fileData.setHeader(nextLine);
                //fileHeader=new PointStr(nextLine);
            }
            
            // read file to fileData
            while((nextLine=csvReader.readNext())!=null){
                pt=new PointStr(nextLine);
                fileData.add(pt);
            }
            fileRowNum=fileData.getPointNum();
        }
        catch (IOException e){
            System.err.println("data Container:readCSV(): exception");
        }

        //
        //column(2).plotPoints();
        //fileData.column(" n_tokens_content").plotPoints();
        
        t1=System.currentTimeMillis();
        Record2File.out("Reading file ends.");
        printTimeCost();
        Record2File.out("\n");
        
        
    }
    
    
    public void filterData(int[] index){
        
        data=fileData.generateTargetPoints(index);
        iDataWidth=data.get(0).getSize();
        
        for(int i=0;i<data.getPointNum();i++){
            if(data.get(i).get(2)>600){
                System.out.println("abnormal data point detected and deleted: "+i);
                data.delete(i);
            }
        }
        
        t1=System.currentTimeMillis();
        
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
        
        return data.getRowsBefore(cutIndex);
    }
    
    public Points getRowsAfter(int cutIndex){
        
        
        return data.getRowsAfter(cutIndex);
    }
    
    public Points getRowsBetween(int cutIndex1,int cutIndex2){
        return data.getRowsBetween(cutIndex1, cutIndex2);
    }
    
    public void printFileHeader(){
        if(fileData.header()!=null){
            System.out.println("fileHeader:");
            for(int i=0;i<fileData.header().getSize();i++){
                System.out.print(fileData.header().get(i)+",");
            }
            System.out.println();
        }
        
    }
    
    public void printFileData(){
        if(fileData!=null){
            System.out.println("fileData:");
            for(int i=0;i<fileData.getPointNum();i++){
                System.out.println("i: "+i);
                PointStr pt1=fileData.get(i);
                //System.out.print(fileData.get(i)+",");
                fileData.get(i).printPointStr();
            }
        }
        else{
            System.err.println("dataContainer:printFileData(): "
                    + "fileData not exist!");
        }
    }
    
    
    public double timeCost(){
        return (t1-t0)/1000.0d;
    }
    

    public void printTimeCost(){
        Record2File.out("Time for file read and treatment is "+
                    Record2File.double2str(  timeCost() )+"s." );
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
