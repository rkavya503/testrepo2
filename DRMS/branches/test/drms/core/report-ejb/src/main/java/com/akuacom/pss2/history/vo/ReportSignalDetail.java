/**
 * 
 */
package com.akuacom.pss2.history.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * the class ReportSignalDetail
 *
 */
public class ReportSignalDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	
	Date signalTime;
	String signalName;
	String signalValue;
	
	public String getSignalName() {
		return signalName;
	}
	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}
	public String getSignalValue() {
		return signalValue;
	}
	public void setSignalValue(String signalValue) {
		this.signalValue = signalValue;
	}
	public Date getSignalTime() {
		return signalTime;
	}
	public void setSignalTime(Date signalTime) {
		this.signalTime = signalTime;
	}
}
