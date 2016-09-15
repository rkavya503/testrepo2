package com.honeywell.dras.ssm.api.request.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import scala.actors.threadpool.Arrays;

public class CustomerEnrollmentData  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String customerName;
	private String participantName;
	private String clientName;
	private String clientPassword;
	private String password;
	private String[] programs;
	private String accountNumber;
	private String ccName1;
	private String ccName2;
	private String ccEmail1;
	private String ccEmail2;
	private String ccPhone1;
	private String ccPhone2;
	private String comments;
	private String deviceType;
	private String requestId;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String[] getPrograms() {
		return programs;
	}
	public void setPrograms(String[] programs) {
		this.programs = programs;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public List<String> getProgramList(){
		List<String> programList = new ArrayList<String>();
		if(null == programs || 0 == programs.length){
			return programList;
		}
		return Arrays.asList(programs);
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getCcName1() {
		return ccName1;
	}
	public void setCcName1(String ccName1) {
		this.ccName1 = ccName1;
	}
	public String getCcName2() {
		return ccName2;
	}
	public void setCcName2(String ccName2) {
		this.ccName2 = ccName2;
	}
	public String getCcEmail1() {
		return ccEmail1;
	}
	public void setCcEmail1(String ccEmail1) {
		this.ccEmail1 = ccEmail1;
	}
	public String getCcEmail2() {
		return ccEmail2;
	}
	public void setCcEmail2(String ccEmail2) {
		this.ccEmail2 = ccEmail2;
	}
	public String getCcPhone1() {
		return ccPhone1;
	}
	public void setCcPhone1(String ccPhone1) {
		this.ccPhone1 = ccPhone1;
	}
	public String getCcPhone2() {
		return ccPhone2;
	}
	public void setCcPhone2(String ccPhone2) {
		this.ccPhone2 = ccPhone2;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getClientPassword() {
		return clientPassword;
	}
	public void setClientPassword(String clientPassword) {
		this.clientPassword = clientPassword;
	}
}
