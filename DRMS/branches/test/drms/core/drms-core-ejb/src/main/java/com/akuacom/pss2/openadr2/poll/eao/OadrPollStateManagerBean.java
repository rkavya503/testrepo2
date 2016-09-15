package com.akuacom.pss2.openadr2.poll.eao;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.logging.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.openadr2.poll.OadrPollState;


@Stateless
public class OadrPollStateManagerBean implements OadrPollStateManager.L,OadrPollStateManager.R {

	private Logger log = Logger.getLogger(OadrPollStateManagerBean.class);
	
	
	@EJB
	OadrPollStateEAO.L pollStateEAO;

	@Override
	public void setSendReregisterVen(String venId, boolean sendReregisterVen) {
		if(isNullAndEmpty(venId)){
			return;
		}
		OadrPollState pollState = getPollState(venId);
		pollState.setSendReregisterVen(sendReregisterVen);
		updatePollState(pollState);
	}

	@Override
	public void setSendCancelVenRegistration(String venId,
			boolean sendCancelVenRegistration) {
		if(isNullAndEmpty(venId)){
			return;
		}
		OadrPollState pollState = getPollState(venId);
		pollState.setSendCancelVenRegistration(sendCancelVenRegistration);
		updatePollState(pollState);
	}

	@Override
	public void setSendCancelReport(String venId, boolean sendCancelReport) {
		if(isNullAndEmpty(venId)){
			return;
		}
		OadrPollState pollState = getPollState(venId);
		pollState.setSendCancelReport(sendCancelReport);
		updatePollState(pollState);
	}

	@Override
	public void setSendCreateReport(String venId, boolean sendCreateReport) {
		if(isNullAndEmpty(venId)){
			return;
		}
		OadrPollState pollState = getPollState(venId);
		pollState.setSendCreateReport(sendCreateReport);
		updatePollState(pollState);
	}

	@Override
	public void setSendRegisterReport(String venId,
			boolean sendRegisterReport) {
		if(isNullAndEmpty(venId)){
			return;
		}
		OadrPollState pollState = getPollState(venId);
		pollState.setSendRegisterReport(sendRegisterReport);
		updatePollState(pollState);
	}
	
	@Override
	public void setReregisterVenSent(String venId, boolean reregisterVenSent) {
		if(isNullAndEmpty(venId)){
			return;
		}
		OadrPollState pollState = getPollState(venId);
		pollState.setReregisterVenSent(reregisterVenSent);
		updatePollState(pollState);
	}

	@Override
	public void setSendDistributeEvent(String venId, boolean sendDistributeEvent) {
		if(isNullAndEmpty(venId)){
			return;
		}
		OadrPollState pollState = getPollState(venId);
		pollState.setSendDistributeEvent(sendDistributeEvent);
		updatePollState(pollState);
	}

	@Override
	public void setSendResponse(String venId, boolean responseSent) {
		if(isNullAndEmpty(venId)){
			return;
		}
		OadrPollState pollState = getPollState(venId);
		pollState.setSendResponse(responseSent);
		updatePollState(pollState);
		
	}

	@Override
	public void setCancelVenRegistrationSent(String venId,
			boolean distributeEventSent) {
		if(isNullAndEmpty(venId)){
			return;
		}
		OadrPollState pollState = getPollState(venId);
		pollState.setCancelVenRegistrationSent(distributeEventSent);
		updatePollState(pollState);
	}

	@Override
	public void setCancelReportSent(String venId, boolean cancelReportSent) {
		if(isNullAndEmpty(venId)){
			return;
		}
		OadrPollState pollState = getPollState(venId);
		pollState.setCancelReportSent(cancelReportSent);
		updatePollState(pollState);
		
	}

	@Override
	public void setReportRegisterSent(String venId, boolean reportRegisterSent) {
		if(isNullAndEmpty(venId)){
			return;
		}
		OadrPollState pollState = getPollState(venId);
		pollState.setRegisterReportSent(reportRegisterSent);
		updatePollState(pollState);
	}

	@Override
	public void setCreateReportSent(String venId, boolean createReportSent) {
		if(isNullAndEmpty(venId)){
			return;
		}
		OadrPollState pollState = getPollState(venId);
		pollState.setCreateReportSent(createReportSent);
		updatePollState(pollState);
	}

	@Override
	public void setDistributeEventSent(String venId, boolean distributeEventSent) {
		if(isNullAndEmpty(venId)){
			return;
		}
		OadrPollState pollState = getPollState(venId);
		pollState.setDistributeEventSent(distributeEventSent);
		updatePollState(pollState);
	}

	@Override
	public void setResponseSent(String venId, boolean responseSent) {
		if(isNullAndEmpty(venId)){
			return;
		}
		OadrPollState pollState = getPollState(venId);
		pollState.setResponseSent(responseSent);
		updatePollState(pollState);
	}

	@Override
	public boolean isSendReregisterVen(String venId) {
		if(isNullAndEmpty(venId)){
			return false;
		}
		OadrPollState pollState = getPollState(venId);
		return pollState.isSendReregisterVen();
	}

	@Override
	public boolean isSendRegisterReport(String venId) {
		if(isNullAndEmpty(venId)){
			return false;
		}
		OadrPollState pollState = getPollState(venId);
		return pollState.isSendRegisterReport();
	}

	@Override
	public boolean isSendCancelVenRegistration(String venId) {
		if(isNullAndEmpty(venId)){
			return false;
		}
		OadrPollState pollState = getPollState(venId);
		return pollState.isSendCancelVenRegistration();
	}

	@Override
	public boolean isSendCreateReport(String venId) {
		if(isNullAndEmpty(venId)){
			return false;
		}
		OadrPollState pollState = getPollState(venId);
		return pollState.isSendCreateReport();
	}

	@Override
	public boolean isSendCancelReport(String venId) {
		if(isNullAndEmpty(venId)){
			return false;
		}
		OadrPollState pollState = getPollState(venId);
		return pollState.isSendCancelReport();
	}

	@Override
	public boolean isSendDistributeEvent(String venId) {
		if(isNullAndEmpty(venId)){
			return false;
		}
		OadrPollState pollState = getPollState(venId);
		return pollState.isSendDistributeEvent();
	}

	@Override
	public boolean isSendResponse(String venId) {
		if(isNullAndEmpty(venId)){
			return false;
		}
		OadrPollState pollState = getPollState(venId);
		return pollState.isSendResponse();
	}
	private boolean updatePollState(OadrPollState pollState){
		boolean updateStatus = true;
		try {
			pollStateEAO.update(pollState);
		} catch (EntityNotFoundException e) {
			log.error("Error in updating record ");
			updateStatus = false;
		}
		return updateStatus;
	}
	private OadrPollState getPollState(String venId){
		if((null == venId)||(venId.equals(""))){
			return null;
		}
		OadrPollState pollState =pollStateEAO.findByVenId(venId);
		if(null == pollState){
			createPollState(venId);
			pollState =pollStateEAO.findByVenId(venId);
		}
		
		if(null == pollState){
			log.error("The PollState is NULL !!!!!!!! ");
		}
		
		return pollState;
	}
	
	private void createPollState(String venId){
		OadrPollState pollState = new OadrPollState();
		pollState.setVenId(venId);
		pollStateEAO.create(pollState);
	}
	private boolean isNullAndEmpty(String velue){
		boolean isNullAndEmpty = false;
		if((null == velue)||(velue.equals(""))){
			isNullAndEmpty = true;
		}
		return isNullAndEmpty;
	}
	
}
