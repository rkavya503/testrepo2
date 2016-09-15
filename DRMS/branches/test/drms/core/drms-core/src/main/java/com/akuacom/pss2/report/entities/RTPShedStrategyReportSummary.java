/**
 * 
 */
package com.akuacom.pss2.report.entities;

import java.io.Serializable;
import java.util.List;

import scala.actors.threadpool.Arrays;

/**
 * the class RTPShedStrategyReport
 */
public class RTPShedStrategyReportSummary implements Serializable {
	
	private static final long serialVersionUID = -8240737282975603215L;

	private String client;
	private String program;
	private String enroll;
	private String participant;
	private String account;
	private String parent;
	
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
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
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
	
	@SuppressWarnings("unchecked")
	public List<String> getVeryHostSummerWeekdayList() {
		return Arrays.asList(this.veryHostSummerWeekday.split(","));
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getModerateSummerWeekdayList() {
		return Arrays.asList(this.moderateSummerWeekday.split(","));
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getMildSummerWeekdayList() {
		return Arrays.asList(this.mildSummerWeekday.split(","));
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getLowCostWinterWeekdayList() {
		return Arrays.asList(this.lowCostWinterWeekday.split(","));
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getLowCostWeekendList() {
		return Arrays.asList(this.lowCostWeekend.split(","));
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getHotSummerWeekdayList() {
		return Arrays.asList(this.hotSummerWeekday.split(","));
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getHighCostWinterWeekdayList() {
		return Arrays.asList(this.highCostWinterWeekday.split(","));
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getExtremelyHotSummerWeekdayList() {
		return Arrays.asList(this.extremelyHotSummerWeekday.split(","));
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getHighCostWeekendList() {
		return Arrays.asList(this.highCostWeekend.split(","));
	}
	
}
