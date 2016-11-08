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
public class PointInt {
    private int m_size=0;
    private ArrayList<Integer> pointInt;
    public PointInt(){
        m_size=0;
        pointInt=new ArrayList<>();
    }
    
    public PointInt(ArrayList<Integer> al){
        this();
        m_size=al.size();
        pointInt=(ArrayList<Integer>) al.clone();
    }
    
    
    public int[] toArray(){
        int[] result=new int[m_size];
        for(int i=0;i<m_size;i++){
            result[i]=pointInt.get(i);
        }
        
        return result;
    }
    

}
