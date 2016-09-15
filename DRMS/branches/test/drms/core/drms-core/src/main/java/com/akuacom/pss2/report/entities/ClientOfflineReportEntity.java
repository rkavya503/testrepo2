package com.akuacom.pss2.report.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.ejb.VersionedEntity;

@Entity
@Table(name = "report_client_offline_entity")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@NamedQueries({       
    @NamedQuery(
            name  = "ClientOfflineReportEntity.findByDate",
            	    query = "select m from ClientOfflineReportEntity m where generateTime >= :start and m.generateTime <= :end order by m.generateTime",
                    hints={@QueryHint(name="org.hibernate.cacheable", value="false")})
})
public class ClientOfflineReportEntity extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1196474488204480688L;

	private String clientName;
	private String participantName;
	private String accountNumber;
	private Date lastContact;
	private String offline;//how long has been offline: ? Day(s), ? Hours, ? Minues
	private Date generateTime;
	private Date startTime;
	private Date endTime;
    @ManyToOne(targetEntity = ClientOfflineReport.class)
    @JoinColumn(name = "report_uuid")
    private ClientOfflineReport report;
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
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	/**
	 * @return the lastContact
	 */
	public Date getLastContact() {
		return lastContact;
	}
	/**
	 * @param lastContact the lastContact to set
	 */
	public void setLastContact(Date lastContact) {
		this.lastContact = lastContact;
	}
	/**
	 * @return the offline
	 */
	public String getOffline() {
		return offline;
	}
	/**
	 * @param offline the offline to set
	 */
	public void setOffline(String offline) {
		this.offline = offline;
	}
	/**
	 * @return the report
	 */
	public ClientOfflineReport getReport() {
		return report;
	}
	/**
	 * @param report the report to set
	 */
	public void setReport(ClientOfflineReport report) {
		this.report = report;
	}
	/**
	 * @return the generateTime
	 */
	public Date getGenerateTime() {
		return generateTime;
	}
	/**
	 * @param generateTime the generateTime to set
	 */
	public void setGenerateTime(Date generateTime) {
		this.generateTime = generateTime;
	}
	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Transient
	private String clientUUID;
	@Transient
	private int duration;
	@Transient
	private boolean optOutClientOfflineNotification;
	@Transient
	private boolean clientOfflineNotificationEnable;
	@Transient
	private int thresholdsSummer;
	@Transient
	private int thresholdsUnSummer;
	@Transient
	private boolean reachThreshold;
	@Transient
	private String address;
	@Transient
	private String subject;
	@Transient
	private String content;
	@Transient
	private String generateTimeContent;
	@Transient
	private String childParticipant;
	@Transient
	private boolean aggEnable;
	
	@Transient
	private String offlineDays;
	@Transient
	private String offlineHours;
	@Transient
	private String offlineMins;
	@Transient
	private String offlineTotalMins;
	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	/**
	 * @return the optOutClientOfflineNotification
	 */
	public boolean isOptOutClientOfflineNotification() {
		return optOutClientOfflineNotification;
	}
	/**
	 * @param optOutClientOfflineNotification the optOutClientOfflineNotification to set
	 */
	public void setOptOutClientOfflineNotification(
			boolean optOutClientOfflineNotification) {
		this.optOutClientOfflineNotification = optOutClientOfflineNotification;
	}
	/**
	 * @return the clientOfflineNotificationEnable
	 */
	public boolean isClientOfflineNotificationEnable() {
		return clientOfflineNotificationEnable;
	}
	/**
	 * @param clientOfflineNotificationEnable the clientOfflineNotificationEnable to set
	 */
	public void setClientOfflineNotificationEnable(
			boolean clientOfflineNotificationEnable) {
		this.clientOfflineNotificationEnable = clientOfflineNotificationEnable;
	}
	/**
	 * @return the thresholdsSummer
	 */
	public int getThresholdsSummer() {
		return thresholdsSummer;
	}
	/**
	 * @param thresholdsSummer the thresholdsSummer to set
	 */
	public void setThresholdsSummer(int thresholdsSummer) {
		this.thresholdsSummer = thresholdsSummer;
	}
	/**
	 * @return the thresholdsUnSummer
	 */
	public int getThresholdsUnSummer() {
		return thresholdsUnSummer;
	}
	/**
	 * @param thresholdsUnSummer the thresholdsUnSummer to set
	 */
	public void setThresholdsUnSummer(int thresholdsUnSummer) {
		this.thresholdsUnSummer = thresholdsUnSummer;
	}
	/**
	 * @return the reachThreshold
	 */
	public boolean isReachThreshold() {
		return reachThreshold;
	}
	/**
	 * @param reachThreshold the reachThreshold to set
	 */
	public void setReachThreshold(boolean reachThreshold) {
		this.reachThreshold = reachThreshold;
	}
	/**
	 * @return the clientUUID
	 */
	public String getClientUUID() {
		return clientUUID;
	}
	/**
	 * @param clientUUID the clientUUID to set
	 */
	public void setClientUUID(String clientUUID) {
		this.clientUUID = clientUUID;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	

	/**
	 * @return the childParticipant
	 */
	public String getChildParticipant() {
		return childParticipant;
	}
	/**
	 * @param childParticipant the childParticipant to set
	 */
	public void setChildParticipant(String childParticipant) {
		this.childParticipant = childParticipant;
	}
	/**
	 * @return the aggEnable
	 */
	public boolean isAggEnable() {
		return aggEnable;
	}
	/**
	 * @param aggEnable the aggEnable to set
	 */
	public void setAggEnable(boolean aggEnable) {
		this.aggEnable = aggEnable;
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
	 * @return the generateTimeContent
	 */
	public String getGenerateTimeContent() {
		return generateTimeContent;
	}
	/**
	 * @param generateTimeContent the generateTimeContent to set
	 */
	public void setGenerateTimeContent(String generateTimeContent) {
		this.generateTimeContent = generateTimeContent;
	}
	/**
	 * @return the offlineDays
	 */
	public String getOfflineDays() {
		return offlineDays;
	}
	/**
	 * @param offlineDays the offlineDays to set
	 */
	public void setOfflineDays(String offlineDays) {
		this.offlineDays = offlineDays;
	}
	/**
	 * @return the offlineHours
	 */
	public String getOfflineHours() {
		return offlineHours;
	}
	/**
	 * @param offlineHours the offlineHours to set
	 */
	public void setOfflineHours(String offlineHours) {
		this.offlineHours = offlineHours;
	}
	/**
	 * @return the offlineMins
	 */
	public String getOfflineMins() {
		return offlineMins;
	}
	/**
	 * @param offlineMins the offlineMins to set
	 */
	public void setOfflineMins(String offlineMins) {
		this.offlineMins = offlineMins;
	}
	/**
	 * @return the offlineTotalMins
	 */
	public String getOfflineTotalMins() {
		return offlineTotalMins;
	}
	/**
	 * @param offlineTotalMins the offlineTotalMins to set
	 */
	public void setOfflineTotalMins(String offlineTotalMins) {
		this.offlineTotalMins = offlineTotalMins;
	}
	
	
}
