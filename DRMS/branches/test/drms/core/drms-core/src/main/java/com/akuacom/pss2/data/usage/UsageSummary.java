package com.akuacom.pss2.data.usage;

import java.io.Serializable;
import java.text.DecimalFormat;


public class UsageSummary implements Serializable {
    private double average;
    private double total;
    private String name;
    private int treeSize = 1;

    public UsageSummary(double average, double total, String name) {
        this.average = average;
        this.total = total;
        this.name = name;
    }

    public UsageSummary() {
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    
    public int getTreeSize() {
        return treeSize;
    }

    public void setTreeSize(int treeSize) {
        this.treeSize = treeSize;
    }
    
}
