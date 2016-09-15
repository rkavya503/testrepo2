package com.akuacom.pss2.email;

import java.io.Serializable;

import com.akuacom.pss2.email.MessageEntity;

public class JSFClientTestEmailReportEntity implements Serializable{

	private static final long serialVersionUID = 1196474488204480649L;
	
	private String participantName;
	private String clientName;
	private String programName;
	private String contactName;
	private String contactAddress;
	private String startTimeString;
	private String endTimeString;
	private String subjectList;
	
	private String eventName;
	private String content;
	private String extention;
	private String createTime;
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
	 * @return the programName
	 */
	public String getProgramName() {
		return programName;
	}
	/**
	 * @param programName the programName to set
	 */
	public void setProgramName(String programName) {
		this.programName = programName;
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
	 * @return the contactAddress
	 */
	public String getContactAddress() {
		return contactAddress;
	}
	/**
	 * @param contactAddress the contactAddress to set
	 */
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}
	/**
	 * @return the startTimeString
	 */
	public String getStartTimeString() {
		return startTimeString;
	}
	/**
	 * @param startTimeString the startTimeString to set
	 */
	public void setStartTimeString(String startTimeString) {
		this.startTimeString = startTimeString;
	}
	/**
	 * @return the endTimeString
	 */
	public String getEndTimeString() {
		return endTimeString;
	}
	/**
	 * @param endTimeString the endTimeString to set
	 */
	public void setEndTimeString(String endTimeString) {
		this.endTimeString = endTimeString;
	}
	/**
	 * @return the subjectList
	 */
	public String getSubjectList() {
		return subjectList;
	}
	/**
	 * @param subjectList the subjectList to set
	 */
	public void setSubjectList(String subjectList) {
		this.subjectList = subjectList;
	}
	/**
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}
	/**
	 * @param eventName the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the extention
	 */
	public String getExtention() {
		return extention;
	}
	/**
	 * @param extention the extention to set
	 */
	public void setExtention(String extention) {
		this.extention = extention;
	}
	
	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public static JSFClientTestEmailReportEntity transfer(MessageEntity message){
		JSFClientTestEmailReportEntity result = new JSFClientTestEmailReportEntity();
		result.setContent(message.getContent());
		result.setSubjectList(message.getSubject());
		result.setCreateTime(message.getCreationTime().toString());
		result.setParticipantName(message.getTo());
		result.setContactName(message.getTo());
		return result;
	}
}
