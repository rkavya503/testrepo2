package com.akuacom.pss2.report.entities;

import java.io.Serializable;
import java.util.Date;


public class CommunicationEmail implements Serializable{

	private static final long serialVersionUID = -5777978730832250208L;
	
	public CommunicationEmail(){
		super();
	}
	private Date creationTime;	
	private String contactName;
	private String emailAddress;
	private String subject;
	private String participantName;
	private String clientName;
	private int status;//NEW-0/FAIL-3/SENT-1
	private String statusExpress;
	private Date sendTime;

	/**
	 * @return the creationTime
	 */
	public Date getCreationTime() {
		return creationTime;
	}
	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	/**
	 * @return the contactName
	 */
	public String getContactName() {
		return contactName;
	}
	/**
	 * @param contactName the contactName to set
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the participantName
	 */
	public String getParticipantName() {
		return participantName;
	}
	/**
	 * @param participantName the participantName to set
	 */
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}
	/**
	 * @param clientName the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	/**
	 * @return the status
	 * <c:if test="${email.status == 0 }">New</c:if>
            	<c:if test="${email.status == 1 }">Sent</c:if>
            	<c:if test="${email.status == 2 }">Suspended</c:if>
            	<c:if test="${email.status == 3 }">Failed</c:if>
            	<c:if test="${email.status == 4 }">Ignored</c:if>
	 */
	public String getStatusExpress() {
		if(status==0){
			return "New";
		}else if(status==1){
			return "Sent";
		}else if(status==2){
			return "Suspended";
		}else if(status==3){
			return "Failed";
		}else if(status==4){
			return "Ignored";
		}
		return statusExpress;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatusStatusExpress(String status) {
		this.statusExpress = status;
	}
	/**
	 * @return the sendTime
	 */
	public Date getSendTime() {
		return sendTime;
	}
	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @param statusExpress the statusExpress to set
	 */
	public void setStatusExpress(String statusExpress) {
		this.statusExpress = statusExpress;
	}
	

}
