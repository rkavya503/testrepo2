package com.honeywell.dras.vtn.api.report;

import java.util.Date;
import java.util.List;

public class ReportInterval {

	private String uId;
	private Date start;
	private Long duration;
	private List<Payload> payloadList;
	/**
	 * @return the uId
	 */
	public String getuId() {
		return uId;
	}
	/**
	 * @param uId the uId to set
	 */
	public void setuId(String uId) {
		this.uId = uId;
	}
	/**
	 * @return the start
	 */
	public Date getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(Date start) {
		this.start = start;
	}
	/**
	 * @return the duration
	 */
	public Long getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	/**
	 * @return the payloadList
	 */
	public List<Payload> getPayloadList() {
		return payloadList;
	}
	/**
	 * @param payloadList the payloadList to set
	 */
	public void setPayloadList(List<Payload> payloadList) {
		this.payloadList = payloadList;
	}

}
