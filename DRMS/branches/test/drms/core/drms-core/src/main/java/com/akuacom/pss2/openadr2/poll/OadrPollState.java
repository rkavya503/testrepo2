package com.akuacom.pss2.openadr2.poll;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.ejb.VersionedEntity;

@Entity
@Table(name = "oadrpollstate")
@NamedQueries({
	@NamedQuery(name="OadrPollState.findAll", query="SELECT pr FROM OadrPollState pr"),
	@NamedQuery(name="OadrPollState.findByVenID", query="SELECT pr FROM OadrPollState pr WHERE pr.venId=:id"),
	@NamedQuery(name="OadrPollState.deleteByVenID", query="DELETE FROM OadrPollState pr WHERE pr.venId=:id"),
})

public class OadrPollState extends VersionedEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String venId;
	private boolean sendReregisterVen;
	private boolean reregisterVenSent;
	private boolean sendCancelVenRegistration;
	private boolean cancelVenRegistrationSent;
	private boolean sendRegisterReport;
	private boolean registerReportSent;
	private boolean sendCreateReport;
	private boolean createReportSent;
	private boolean sendCancelReport;
	private boolean cancelReportSent;
	private boolean sendDistributeEvent;
	private boolean distributeEventSent;
	private boolean sendResponse;
	private boolean responseSent;
	
	
	public String getVenId() {
		return venId;
	}
	public void setVenId(String venId) {
		this.venId = venId;
	}
	public boolean isSendReregisterVen() {
		return sendReregisterVen;
	}
	public void setSendReregisterVen(boolean sendReregisterVen) {
		this.sendReregisterVen = sendReregisterVen;
	}
	public boolean isReregisterVenSent() {
		return reregisterVenSent;
	}
	public void setReregisterVenSent(boolean reregisterVenSent) {
		this.reregisterVenSent = reregisterVenSent;
	}
	public boolean isSendCancelVenRegistration() {
		return sendCancelVenRegistration;
	}
	public void setSendCancelVenRegistration(boolean sendCancelVenRegistration) {
		this.sendCancelVenRegistration = sendCancelVenRegistration;
	}
	public boolean isCancelVenRegistrationSent() {
		return cancelVenRegistrationSent;
	}
	public void setCancelVenRegistrationSent(boolean cancelVenRegistrationSent) {
		this.cancelVenRegistrationSent = cancelVenRegistrationSent;
	}
	public boolean isSendRegisterReport() {
		return sendRegisterReport;
	}
	public void setSendRegisterReport(boolean sendRegisterReport) {
		this.sendRegisterReport = sendRegisterReport;
	}
	public boolean isRegisterReportSent() {
		return registerReportSent;
	}
	public void setRegisterReportSent(boolean registerReportSent) {
		this.registerReportSent = registerReportSent;
	}
	public boolean isSendCreateReport() {
		return sendCreateReport;
	}
	public void setSendCreateReport(boolean sendCreateReport) {
		this.sendCreateReport = sendCreateReport;
	}
	public boolean isCreateReportSent() {
		return createReportSent;
	}
	public void setCreateReportSent(boolean createReportSent) {
		this.createReportSent = createReportSent;
	}
	public boolean isSendCancelReport() {
		return sendCancelReport;
	}
	public void setSendCancelReport(boolean sendCancelReport) {
		this.sendCancelReport = sendCancelReport;
	}
	public boolean isCancelReportSent() {
		return cancelReportSent;
	}
	public void setCancelReportSent(boolean cancelReportSent) {
		this.cancelReportSent = cancelReportSent;
	}
	public boolean isSendDistributeEvent() {
		return sendDistributeEvent;
	}
	public void setSendDistributeEvent(boolean sendDistributeEvent) {
		this.sendDistributeEvent = sendDistributeEvent;
	}
	public boolean isDistributeEventSent() {
		return distributeEventSent;
	}
	public void setDistributeEventSent(boolean distributeEventSent) {
		this.distributeEventSent = distributeEventSent;
	}
	public boolean isSendResponse() {
		return sendResponse;
	}
	public void setSendResponse(boolean sendResponse) {
		this.sendResponse = sendResponse;
	}
	public boolean isResponseSent() {
		return responseSent;
	}
	public void setResponseSent(boolean responseSent) {
		this.responseSent = responseSent;
	}
}
