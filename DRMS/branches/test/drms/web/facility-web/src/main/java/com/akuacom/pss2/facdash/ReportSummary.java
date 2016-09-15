package com.akuacom.pss2.facdash;

import java.io.Serializable;


public class ReportSummary implements Serializable {
	
	private static final long serialVersionUID = 9150337181444127236L;
	
	private String name;
	private double baseAvg;
    private double baseTotal;
    private double actualAvg;
    private double actualTotal;
    private double shedAvg;
    private double shedTotal;
    
	public String getName() {
		return name;
	}
	public double getBaseAvg() {
		return baseAvg;
	}
	public double getBaseTotal() {
		return baseTotal;
	}
	public double getActualAvg() {
		return actualAvg;
	}
	public double getActualTotal() {
		return actualTotal;
	}
	public double getShedAvg() {
		return shedAvg;
	}
	public double getShedTotal() {
		return shedTotal;
	}
	public void setName(String name) {
		this.name = name;
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
    
}