package com.akuacom.pss2.drw.value;

import java.io.Serializable;

public class SlapBlockRange implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String number;
	private int minValue;
	private int maxValue;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public int getMinValue() {
		return minValue;
	}
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}
	public int getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
	
	

}
