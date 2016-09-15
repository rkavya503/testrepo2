package com.akuacom.pss2.history.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class ReportSummaryVo implements Serializable {
	
	private static final long serialVersionUID = 9150337181444127236L;
	
	private String catalog;
	private double baseAvg;
    private double baseTotal;
    private double actualAvg;
    private double actualTotal;
    private double shedAvg;
    private double shedTotal;
    private Map<String,ReportSummaryDetailVo> itemMap= new HashMap<String,ReportSummaryDetailVo>();

	public double getBaseTotal() {
		return baseTotal;
	}
	public double getActualTotal() {
		return actualTotal;
	}
	public double getShedTotal() {
		return shedTotal;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public Map<String, ReportSummaryDetailVo> getItemMap() {
		return itemMap;
	}
	public void setBaseAvg(double baseAvg) {
		this.baseAvg = baseAvg;
	}
	public void setBaseTotal(double baseTotal) {
		this.baseTotal = baseTotal;
	}
	public void setActualAvg(double actualAvg) {
		this.actualAvg = actualAvg;
	}
	public void setActualTotal(double actualTotal) {
		this.actualTotal = actualTotal;
	}
	public void setShedAvg(double shedAvg) {
		this.shedAvg = shedAvg;
	}
	public void setShedTotal(double shedTotal) {
		this.shedTotal = shedTotal;
	}
	
	public double getBaseAvg() {
            return baseAvg;
    }
    public double getActualAvg() {
            return actualAvg;
    }
    public double getShedAvg() {
            return shedAvg;
    }
    
}