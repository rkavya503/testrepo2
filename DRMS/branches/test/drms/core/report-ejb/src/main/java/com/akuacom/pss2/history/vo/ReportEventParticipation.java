/**
 * 
 */
package com.akuacom.pss2.history.vo;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

import com.akuacom.pss2.history.ClientParticipationStatus;

/**
 * the report entity ReportEventParticipation
 *
 */
public class ReportEventParticipation implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	String clientName;
	String programName;
	String eventName;
	Date eventDate;
	int participation;
	double percentage;
	String status;
	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
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
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	public int getParticipation() {
		return participation;
	}
	public void setParticipation(int participation) {
		this.participation = participation;
	}
	public double getPercentage() {
		return percentage;
	}
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	public String getStatus() {
		if (percentage == 0.0) {
			status = ClientParticipationStatus.get(participation).getDescription();
		} else {
			DecimalFormat format = new DecimalFormat("##0.00%"); 
			status = format.format(percentage);
		}
		return status;
	}
}
