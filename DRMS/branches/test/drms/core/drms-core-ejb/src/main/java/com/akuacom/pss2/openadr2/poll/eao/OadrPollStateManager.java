package com.akuacom.pss2.openadr2.poll.eao;

import javax.ejb.Local;
import javax.ejb.Remote;


public interface OadrPollStateManager {
	@Remote
	public interface R extends OadrPollStateManager {}
	@Local
	public interface L extends OadrPollStateManager {}
	
	public void setSendReregisterVen(String venId, boolean sendReregisterVen) ;
	public void setReregisterVenSent(String venId, boolean reregisterVenSent);
	public void setSendCancelVenRegistration(String venId,boolean sendCancelVenRegistration);
	public void setSendCancelReport(String venId,boolean sendCancelReport);
	public void setSendCreateReport(String venId,boolean sendCreateReport);
	public void setSendRegisterReport(String venId,boolean sendRegisterReport);
	public void setSendDistributeEvent(String venId,boolean sendDistributeEvent);
	public void setSendResponse(String venId, boolean responseSent);
	public void setCancelVenRegistrationSent(String venId,boolean distributeEventSent);
	public void setCancelReportSent(String venId,boolean cancelReportSent);
	public void setReportRegisterSent(String venId,boolean reportRegisterSent);
	public void setCreateReportSent(String venId,boolean createReportSent);
	public void setDistributeEventSent(String venId,boolean distributeEventSent);
	public void setResponseSent(String venId, boolean responseSent);
	public boolean isSendReregisterVen(String venId);
	public boolean isSendRegisterReport(String venId);
	public boolean isSendCancelVenRegistration(String venId);
	public boolean isSendCreateReport(String venId);
	public boolean isSendCancelReport(String venId) ;
	public boolean isSendDistributeEvent(String venId) ;
	public boolean isSendResponse(String venId) ;
}
