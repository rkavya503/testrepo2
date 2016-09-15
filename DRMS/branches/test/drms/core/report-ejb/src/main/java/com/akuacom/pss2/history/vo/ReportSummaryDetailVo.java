package com.akuacom.pss2.history.vo;

import java.io.Serializable;

public class ReportSummaryDetailVo implements Serializable {
	private static final long serialVersionUID = -887922729085370112L;
	private double average;
    private double total;
    private String catalog;
    private String name;
    private int treeSize = 1;

    public ReportSummaryDetailVo(double average, double total, String name) {
        this.average = average;
        this.total = total;
        this.name = name;
    }

    public ReportSummaryDetailVo() {
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

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
    
}
