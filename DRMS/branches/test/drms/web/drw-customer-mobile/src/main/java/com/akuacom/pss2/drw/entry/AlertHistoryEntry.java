package com.akuacom.pss2.drw.entry;

import java.text.SimpleDateFormat;
import javax.xml.bind.annotation.XmlRootElement;

import com.akuacom.pss2.drw.util.MDRConstants;
import com.akuacom.pss2.drw.value.AlertValue;


@XmlRootElement
public class AlertHistoryEntry {
	
	public static final String SPD ="SPD";
	public static final String SAI ="SAI";
	public static final String SDP ="SDP";
	public static final String BIP_30 ="30";
	public static final String BIP_15 ="15";
	public static final String DBP ="DBP";
	public static final String CBP = "CBP";
	public static final String API ="API";
	public static final String RTP ="RTP";
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getAlertDate() {
		return alertDate;
	}

	public void setAlertDate(String alertDate) {
		this.alertDate = alertDate;
	}

	public String getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	private String message;
	private String product;
	private String alertDate;
	private String alertTime;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;
	private String programName;
	private String program;
	
	public void setAlertHistory(AlertValue ev){
		if(ev!=null){
			this.product = ev.getProduct();
			this.message = ev.getMessage();
			this.programName = ev.getProgramName(); 
			
			if(ev.getProgramName().equalsIgnoreCase(DBP)){
				this.program = MDRConstants.DBP;
				
			}else if(ev.getProgramName().equalsIgnoreCase(CBP)){
				this.program = MDRConstants.CBP;
				
			}else if(ev.getProgramName().equalsIgnoreCase(RTP)){
				this.program = MDRConstants.RTP;
				
			}else if(ev.getProduct().contains(BIP_15)){
				this.program = MDRConstants.BIP_15;
				
			}else if(ev.getProduct().contains(BIP_30)){
				this.program = MDRConstants.BIP_30;
				
			}else if(ev.getProgramName().equalsIgnoreCase(SDP)){
				this.program = MDRConstants.SDP;
				
			}else if(ev.getProgramName().equalsIgnoreCase(SPD)){
				this.program = MDRConstants.SPD;
				
			}else if(ev.getProgramName().equalsIgnoreCase(SAI)){
				this.program = MDRConstants.SAI;
				
			}else if(ev.getProgramName().equalsIgnoreCase(API)){
				this.program = MDRConstants.API;
				
			}
			
			
			
			
			if(ev.getAlertTime() !=null){
				this.alertDate = (new SimpleDateFormat("MM/dd/yyyy")).format(ev.getAlertTime());
				this.alertTime = (new SimpleDateFormat("hh:mma")).format(ev.getAlertTime());				
			}
			
			if(ev.getStartTime()!=null){
				this.startDate = (new SimpleDateFormat("MM/dd/yyyy")).format(ev.getStartTime());
				this.startTime = (new SimpleDateFormat("hh:mma")).format(ev.getStartTime());
			}
			if(ev.getEndTime()!=null){
				if(!"TBD".equalsIgnoreCase(endDate)){
					this.endDate = (new SimpleDateFormat("MM/dd/yyyy")).format(ev.getEndTime());	
				}
				if(!"TBD".equalsIgnoreCase(endTime)){
					this.endTime = (new SimpleDateFormat("hh:mma")).format(ev.getEndTime());	
				}
			}else{
				this.endDate="TBD";
				this.endTime="TBD";
			}
		}
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}
	
}
