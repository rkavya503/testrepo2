package com.honeywell.drms.logger;

import com.kanaeki.firelog.util.FireLogEntry;

public class SuppressibleLogEntry extends FireLogEntry {

	private static final long serialVersionUID = -4787443727007577176L;
	
	private String suppressId;
	
	private int suppressDuration = 60*60; // sec, default one hour 
	
	public SuppressibleLogEntry(String supressId, int supressDuration) {
		super();
		this.suppressId = supressId;
		this.suppressDuration = supressDuration;
	}

	public String getSupressId() {
		return suppressId;
	}

	public void setSupressId(String supressId) {
		this.suppressId = supressId;
	}

	public int getSuppressDuration() {
		return suppressDuration;
	}

	public void setSuppressDuration(int supressDuration) {
		this.suppressDuration = supressDuration;
	}
	
}
