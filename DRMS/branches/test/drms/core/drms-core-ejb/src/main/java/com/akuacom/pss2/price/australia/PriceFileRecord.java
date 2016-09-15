package com.akuacom.pss2.price.australia;

import java.io.Serializable;

public class PriceFileRecord implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7253844211639753793L;
	private String dateStr;
	private String fileLocation;
	
	public PriceFileRecord(String dateStr, String fileLocation) {
		this.dateStr = dateStr;
		this.fileLocation = fileLocation;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	
}
