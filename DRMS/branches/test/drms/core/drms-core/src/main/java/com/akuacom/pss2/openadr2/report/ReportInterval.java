package com.akuacom.pss2.openadr2.report;

import java.util.Date;
import java.util.List;


public class ReportInterval {
	private String uId;
	private Date start;
	private Long duration;
	private List<Payload> payloadList;
	
	
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public List<Payload> getPayloadList() {
		return payloadList;
	}
	public void setPayloadList(List<Payload> payloadList) {
		this.payloadList = payloadList;
	}
	
	
}
