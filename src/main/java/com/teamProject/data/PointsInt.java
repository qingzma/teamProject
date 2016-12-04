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
public class PointsInt {
    private ArrayList<ArrayList<Integer>> pointsInt;
    private int length;
    public PointsInt(){
        pointsInt=new ArrayList<>();
        length=0;
    }
    
    public void add(PointInt ptInt){
        pointsInt.add(ptInt.getPointInt());
        length++;
    }
    
    public int length(){
        return length;
    }
    public PointInt get(int i){
        return new PointInt(pointsInt.get(i));
    }
}
