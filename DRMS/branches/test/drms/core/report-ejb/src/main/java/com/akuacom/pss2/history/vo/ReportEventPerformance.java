/**
 * 
 */
package com.akuacom.pss2.history.vo;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;


/**
 *
 */
public class ReportEventPerformance implements Serializable {
	private static final long serialVersionUID = 1L;
	
	String participantName;
	String programName;
	String eventName;
	Date startTime;
	Date endTime;
	Double avgShed;
	Double totalShed;
	boolean client;
	
	public ReportEventPerformance(){
		
	}
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Double getAvgShed() {
		if(avgShed==null){
			return avgShed;
		}
		DecimalFormat df = new DecimalFormat( "##.00 "); 
		return Double.valueOf(df.format(avgShed));
	}
	public void setAvgShed(Double avgShed) {
		this.avgShed = avgShed;
	}
	public Double getTotalShed() {
		if(totalShed==null){
			return totalShed;
		}
		DecimalFormat df = new DecimalFormat( "##.00 "); 
		return Double.valueOf(df.format(totalShed));
	}
	public void setTotalShed(Double totalShed) {
		this.totalShed = totalShed;
	}
	public boolean isClient() {
		return client;
	}
	public void setClient(boolean client) {
		this.client = client;
	}
	
	public static void main(String args[]){
		Double d = new Double(0.0);
		DecimalFormat df = new DecimalFormat( "##.00 "); 
		Double dd = Double.valueOf(df.format(d));
		System.out.println(dd);
	}
}
