/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author qingzhi
 */
public class Record2File {
    private final static String fileName="src/main/java/com/teamProject/output.txt";
    public static void out(String str){
        System.out.println(str);
        try(PrintWriter writer = new PrintWriter(new FileWriter(
                fileName,true))){
            //writer.append(str);
            writer.println(str);
            writer.close();
        } catch (Exception e) {
            System.err.println("error to write to output.txt");
        }
    }
    
    public static void warning(String str){
        System.out.println("[Warning: "+str+"]");
        try(PrintWriter writer = new PrintWriter(new FileWriter(
                fileName,true))){
            //writer.append(str);
            writer.println("[warning:] "+str+"]");
            writer.close();
        } catch (Exception e) {
            System.err.println("error to write to output.txt");
        }
    }
    
    
    
    
    public static void error(String str){
        System.err.println("[Error: "+str+"]");
        try(PrintWriter writer = new PrintWriter(new FileWriter(
                fileName,true))){
            //writer.append(str);
            writer.println("[Error:] "+str+"]");
            writer.close();
            out(str);
        } catch (Exception e) {
            System.err.println("error to write to error.txt");
        }
    }
    
    public static void deleteFile(){
        try{
            File file=new File(fileName);
            file.delete();
        }catch(Exception e){}
        
    }
    
    public static String double2str(double d){
        DecimalFormat df = new DecimalFormat("####.000"); 
        return df.format(d);
    }
}
