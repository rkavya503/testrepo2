package com.akuacom.pss2.history.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class ClientOfflineStatisticsData implements Serializable{
	private static final long serialVersionUID = 1L;
	private String participantName;
	private Long times;
	private BigDecimal totalTime;
	private BigDecimal timePercent;
	private BigDecimal duringEvent;
	private Long rowCount;
	
	public ClientOfflineStatisticsData(){
		
	}
	
	public ClientOfflineStatisticsData(String participantName, Long times,
			BigDecimal totalTime, BigDecimal timePercent,
			BigDecimal duringEvent, Long rowCount) {
		super();
		this.participantName = participantName;
		this.times = times;
		this.totalTime = totalTime;
		this.timePercent = timePercent;
		this.duringEvent = duringEvent;
		this.rowCount = rowCount;
	}
	
	public Long getRowCount() {
		return rowCount;
	}
	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public Long getTimes() {
		return times;
	}
	public void setTimes(Long times) {
		this.times = times;
	}
	public BigDecimal getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(BigDecimal totalTime) {
		this.totalTime = totalTime;
	}
	public BigDecimal getTimePercent() {
		return timePercent;
	}
	public void setTimePercent(BigDecimal timePercent) {
		this.timePercent = timePercent;
	}
	public BigDecimal getDuringEvent() {
		return duringEvent;
	}
	public void setDuringEvent(BigDecimal duringEvent) {
		this.duringEvent = duringEvent;
	}	
}
