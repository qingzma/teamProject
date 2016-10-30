/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamProject;

import java.io.PrintWriter;

/**
 *
 * @author qingzhi
 */
public class Record2File {
    public static void normal(String str){
        try{
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
            writer.append(str);
            writer.close();
        } catch (Exception e) {
            System.err.println("error to write to output.txt");
        }
    }
    
    public static void error(String str){
        try{
            PrintWriter writer = new PrintWriter("error.txt", "UTF-8");
            writer.append(str);
            writer.close();
            normal(str);
        } catch (Exception e) {
            System.err.println("error to write to error.txt");
        }
    }
    
}
