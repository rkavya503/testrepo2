/**
 * 
 */
package com.akuacom.pss2.richsite.report;

import java.util.Date;

/**
 * the class RTPShedStrategyEntry
 */
public class RTPShedStrategyEntry {
	
	private String client;
	private String program;
	private String enroll;
	private String participant;
	private String account;
	private String parent;
	
	private String startTime;
	
	private String veryHostSummerWeekday;
	private String moderateSummerWeekday;
	private String mildSummerWeekday;
	private String lowCostWinterWeekday;
	private String lowCostWeekend;
	private String hotSummerWeekday;
	private String highCostWinterWeekday;
	private String extremelyHotSummerWeekday;
	private String highCostWeekend;
	
	
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public String getEnroll() {
		return enroll;
	}
	public void setEnroll(String enroll) {
		this.enroll = enroll;
	}
	public String getParticipant() {
		return participant;
	}
	public void setParticipant(String participant) {
		this.participant = participant;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getParent() {
		if(parent==null||parent.equalsIgnoreCase("null")){
			parent="";
		}

		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getVeryHostSummerWeekday() {
		return veryHostSummerWeekday;
	}
	public void setVeryHostSummerWeekday(String veryHostSummerWeekday) {
		this.veryHostSummerWeekday = veryHostSummerWeekday;
	}
	public String getModerateSummerWeekday() {
		return moderateSummerWeekday;
	}
	public void setModerateSummerWeekday(String moderateSummerWeekday) {
		this.moderateSummerWeekday = moderateSummerWeekday;
	}
	public String getMildSummerWeekday() {
		return mildSummerWeekday;
	}
	public void setMildSummerWeekday(String mildSummerWeekday) {
		this.mildSummerWeekday = mildSummerWeekday;
	}
	public String getLowCostWinterWeekday() {
		return lowCostWinterWeekday;
	}
	public void setLowCostWinterWeekday(String lowCostWinterWeekday) {
		this.lowCostWinterWeekday = lowCostWinterWeekday;
	}
	public String getLowCostWeekend() {
		return lowCostWeekend;
	}
	public void setLowCostWeekend(String lowCostWeekend) {
		this.lowCostWeekend = lowCostWeekend;
	}
	public String getHotSummerWeekday() {
		return hotSummerWeekday;
	}
	public void setHotSummerWeekday(String hotSummerWeekday) {
		this.hotSummerWeekday = hotSummerWeekday;
	}
	public String getHighCostWinterWeekday() {
		return highCostWinterWeekday;
	}
	public void setHighCostWinterWeekday(String highCostWinterWeekday) {
		this.highCostWinterWeekday = highCostWinterWeekday;
	}
	public String getExtremelyHotSummerWeekday() {
		return extremelyHotSummerWeekday;
	}
	public void setExtremelyHotSummerWeekday(String extremelyHotSummerWeekday) {
		this.extremelyHotSummerWeekday = extremelyHotSummerWeekday;
	}
	public String getHighCostWeekend() {
		return highCostWeekend;
	}
	public void setHighCostWeekend(String highCostWeekend) {
		this.highCostWeekend = highCostWeekend;
	}
	
	
}
