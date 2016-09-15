package com.akuacom.pss2.ge;

import java.io.Serializable;
import java.util.Date;

public class GeVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date timeStamp;
	private String accountNo;
	//ADRSite
	//Average MWH of load over the previous 5 minutes. Load
	private String load;
	//ProgramOptStatus
	private String progOptStatus;
	//EventOptStatus
	private String evtOptStatus;
	//LastPoll
	private Date lastPoll;
	//CallOff
    private String callOff;
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getLoad() {
		return load;
	}
	public void setLoad(String load) {
		this.load = load;
	}
	public String getProgOptStatus() {
		return progOptStatus;
	}
	public void setProgOptStatus(String progOptStatus) {
		this.progOptStatus = progOptStatus;
	}
	public String getEvtOptStatus() {
		return evtOptStatus;
	}
	public void setEvtOptStatus(String evtOptStatus) {
		this.evtOptStatus = evtOptStatus;
	}
	public Date getLastPoll() {
		return lastPoll;
	}
	public void setLastPoll(Date lastPoll) {
		this.lastPoll = lastPoll;
	}
	public String getCallOff() {
		return callOff;
	}
	public void setCallOff(String callOff) {
		this.callOff = callOff;
	}
}
