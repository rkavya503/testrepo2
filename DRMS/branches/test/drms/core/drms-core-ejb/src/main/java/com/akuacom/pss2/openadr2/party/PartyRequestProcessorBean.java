package com.akuacom.pss2.openadr2.party;

import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.openadr2.Constants;
import com.akuacom.pss2.openadr2.endpoint.Endpoint;
import com.akuacom.pss2.openadr2.endpoint.EndpointEAO;
import com.akuacom.pss2.openadr2.endpoint.EndpointManager;
import com.akuacom.pss2.openadr2.poll.eao.OadrPollStateManager;
//import com.akuacom.pss2.openadr2.report.ReportRequestManager;
import com.honeywell.dras.vtn.api.common.FingerPrint;
import com.honeywell.dras.vtn.api.common.Response;
import com.honeywell.dras.vtn.api.registration.CancelPartyRegistration;
import com.honeywell.dras.vtn.api.registration.CanceledPartyRegistration;
import com.honeywell.dras.vtn.api.registration.CreatePartyRegistration;
import com.honeywell.dras.vtn.api.registration.CreatedPartyRegistration;
import com.honeywell.dras.vtn.api.registration.Profile;
import com.honeywell.dras.vtn.api.registration.ProfileName;
import com.honeywell.dras.vtn.api.registration.QueryRegistration;
import com.honeywell.dras.vtn.api.registration.Result;
import com.honeywell.dras.vtn.api.registration.TransportName;
import com.honeywell.dras.vtn.dras.service.VtnDrasServiceException;

@Stateless 
public class PartyRequestProcessorBean implements PartyRequestProcessor.L, PartyRequestProcessor.R {
	
	private Logger log = Logger.getLogger(PartyRequestProcessorBean.class);	
	private static final String IS_CERT_COMMON_NAME_VALIDATION_ENABLED = "com.honeywell.dras.soap.isCertCommonNameValidationEnabled";
	@EJB
	private EndpointManager.L endpointManager;

	@EJB
	private OadrPollStateManager.L oadrPollStateManager;
	
//	@EJB
//	private ReportRequestManager.L reportRequestManager;

	/* (non-Javadoc)
	 * @see com.honeywell.dras.services.soap.vtn.party.PartyRequestProcessor#isVenValid(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isVenValid(String venId, String certCommonName) {
		boolean res = false;
		try {
			Endpoint ep = endpointManager.findByVenId(venId);
			if(ep != null) {
				// ven id is case sensitive as per harness tool
				if(!ep.getVenId().equals(venId)){
					return false;
				}
				
				if ("2.0b".equalsIgnoreCase(ep.getSchemaVersion())) {
					String certificationEnableFlag = System.getProperty(IS_CERT_COMMON_NAME_VALIDATION_ENABLED).trim();
					if(certificationEnableFlag.trim().equalsIgnoreCase("TRUE")){
						  if(ep.getCertCommonName() != null && ep.getCertCommonName().equals(certCommonName)) {
							res = true;  //ep.getCertCommonName() != null && ep.getCertCommonName().equals(certCommonName);
						  }
						}else{
							res = true;
						}
					//res = true;  //ep.getCertCommonName() != null && ep.getCertCommonName().equals(certCommonName);
				} else {
					res = true;
				}
			}
		} catch (EntityNotFoundException e) {
			res = false;
		}
		
		return res;
	}
	
	/* (non-Javadoc)
	 * @see com.honeywell.dras.services.soap.vtn.party.PartyRequestProcessor#queryRegistration(com.honeywell.dras.vtn.api.registration.QueryRegistration)
	 */
	@Override
	public CreatedPartyRegistration queryRegistration(
			QueryRegistration queryRegistration) throws VtnDrasServiceException {
		
		CreatedPartyRegistration result=null;
		String requestId = queryRegistration.getRequestId();
		String venId = queryRegistration.getVenId();		
		result = generateCreatedPartyReg(requestId,venId);
		
		result.setCertCommonName(queryRegistration.getCertCommonName());
		result.setFingerprint(queryRegistration.getFingerprint());
		result.setSchemaVersion(queryRegistration.getSchemaVersion());
		
		return result;
	}

	/* (non-Javadoc)
	 * @see com.honeywell.dras.services.soap.vtn.party.PartyRequestProcessor#createPartyRegistration(com.honeywell.dras.vtn.api.registration.CreatePartyRegistration)
	 */
	@Override
	public CreatedPartyRegistration createPartyRegistration(
			CreatePartyRegistration createPartyRegistration)
			throws VtnDrasServiceException {
		
		
		
		if (createPartyRegistration == null 
				|| createPartyRegistration.getProfileName() == null
				|| ((createPartyRegistration.getHttpPullMode() == false) && (createPartyRegistration.getTransportAddress() == null))
				|| createPartyRegistration.getTransports() == null
				|| createPartyRegistration.getTransports().isEmpty()
				//|| createPartyRegistration.getVenName() == null
				) {
			log.error("Invalid request parameters");
			throw new VtnDrasServiceException("Invalid request parameters");
		}
		
		boolean reReg = false;
		
		String venId = Constants.VEN_ID_PREFIX + System.currentTimeMillis();
		if (createPartyRegistration.getVenId() != null&&(!"".equalsIgnoreCase(createPartyRegistration.getVenId().trim()))) {
			venId = createPartyRegistration.getVenId();
		}
		String regId = Constants.REQUEST_ID_PREFIX + UUID.randomUUID().toString();
		if (createPartyRegistration.getRegistrationId() != null) {
			reReg = true;
			regId = createPartyRegistration.getRegistrationId();
		}
		
		Endpoint ep = null;
		try {
			ep = endpointManager.findByVenId(venId);
			
			if(ep != null)
			{
				if (reReg && !regId.equals(ep.getRegistrationId())) {
					log.error("Unable to create Party registration - invalid registration id for reReg");
					CreatedPartyRegistration cpReg = generateCreatedPartyReg(createPartyRegistration.getRequestId(), venId);
					cpReg.getResponse().setResponseCode("452");
					cpReg.getResponse().setResponseDescription("Incorrect registration ID for VEN ID");
					cpReg.setCertCommonName(createPartyRegistration.getCertCommonName());
					cpReg.setFingerprint(createPartyRegistration.getFingerprint());
					cpReg.setRegistrationId(regId);
					cpReg.setSchemaVersion(createPartyRegistration.getSchemaVersion());

					return cpReg;
				}
			}
			if (ep != null) {
				ep.setRegistrationId(regId);
				ep.setVenId(venId);
				ep.setProfileName(createPartyRegistration.getProfileName().value());
				ep.setTransport(createPartyRegistration.getTransports().get(0).value());
				ep.setHttpPullMode(createPartyRegistration.getHttpPullMode());
				ep.setReportOnly(createPartyRegistration.getReportOnly());
				ep.setTransportAddress(createPartyRegistration.getTransportAddress());
				ep.setVenName(createPartyRegistration.getVenName());
				ep.setXmlSignature(createPartyRegistration.getXmlSignature());	
				ep.setCanceled(false);
				endpointManager.updateEndpoint(ep);
			}
		} catch (EntityNotFoundException e1) {
			ep = null;
		}
		
		if (ep == null) {
			if (reReg) {
				CreatedPartyRegistration cpReg = generateCreatedPartyReg(createPartyRegistration.getRequestId(), venId);
				log.error("Unable to cancel Party - ven id not found and reReg");
				cpReg.getResponse().setResponseCode("452");
				cpReg.getResponse().setResponseDescription("Incorrect registration ID (2)");
				cpReg.setCertCommonName(createPartyRegistration.getCertCommonName());
				cpReg.setFingerprint(createPartyRegistration.getFingerprint());
				cpReg.setRegistrationId(regId);
				cpReg.setSchemaVersion(createPartyRegistration.getSchemaVersion());
				return cpReg;
			}
			try {
				ep = endpointManager.getEndPointInstance(createPartyRegistration, venId, regId);
				endpointManager.createEndpoint(ep);
			} catch (DuplicateKeyException e) {
				log.error("Create endpoint failed", e);
				throw new VtnDrasServiceException("Create endpoint failed");
			}
			
			log.info("Endpoint created: " + ep);
		}
		CreatedPartyRegistration cpReg = generateCreatedPartyReg(createPartyRegistration.getRequestId(), venId);
	
		cpReg.setCertCommonName(createPartyRegistration.getCertCommonName());
		cpReg.setFingerprint(createPartyRegistration.getFingerprint());
		cpReg.setRegistrationId(regId);
		cpReg.setSchemaVersion(createPartyRegistration.getSchemaVersion());		
		oadrPollStateManager.setSendRegisterReport(venId, true);
		
		/*if((createPartyRegistration.getHttpPullMode() == false) && (createPartyRegistration.getTransportAddress() != null))
		{
			sendOpenadrPushMessage(createPartyRegistration);
			OpenADRPushMDB oadrPushMDB = EJBFactory.getBean(OpenADRPushMDBManagerBean.class);
			oadrPushMDB.processMessage(registerReportRequest);
		}*/
		
		return cpReg;
	}

	/* (non-Javadoc)
	 * @see com.honeywell.dras.services.soap.vtn.party.PartyRequestProcessor#cancelPartyRegistration(com.honeywell.dras.vtn.api.registration.CancelPartyRegistration)
	 */
	@Override
	public CanceledPartyRegistration cancelPartyRegistration(
			CancelPartyRegistration cancelPartyRegistration)
			throws VtnDrasServiceException {
		if (cancelPartyRegistration == null 
				|| cancelPartyRegistration.getRegistrationId() == null
				|| cancelPartyRegistration.getVenId() == null) {
			log.error("Invalid request parameters");
			throw new VtnDrasServiceException("Invalid request parameters");
		}
		
		CanceledPartyRegistration res = new CanceledPartyRegistration();
		res.setCertCommonName(cancelPartyRegistration.getCertCommonName());
		res.setRequestId(cancelPartyRegistration.getRequestId());
		res.setVenId(cancelPartyRegistration.getVenId());
		res.setSchemaVersion(cancelPartyRegistration.getSchemaVersion());
		res.setRegistrationId(cancelPartyRegistration.getRegistrationId());
		Response resp = new Response();
		resp.setResponseCode("200");
		resp.setResponseDescription("Success");
		resp.setRequestId(cancelPartyRegistration.getRequestId());
		resp.setVenId(res.getVenId());
		res.setResponse(resp);
		
		try {
			Endpoint ep = endpointManager.findByVenId(cancelPartyRegistration.getVenId());
			if (ep==null||!ep.getRegistrationId().equals(cancelPartyRegistration.getRegistrationId())) {
				log.error("Unable to cancel Party registration - reg id does not match");
				res.getResponse().setResponseCode("452");
				res.getResponse().setResponseDescription("Invalid Registration ID for cancel");
				return res;
			}
			
			ep.setCanceled(Boolean.TRUE);
			endpointManager.updateEndpoint(ep);
			//endpointManager.deleteEndpoint(ep);
		} catch (EntityNotFoundException e) {
			log.error("Unable to cancel Party registration - ven id not found");
			res.getResponse().setResponseCode("452");
			res.getResponse().setResponseDescription("Invalid VEN ID for cancel");
			return res;
		}
		
		try{
			oadrPollStateManager.setSendCancelVenRegistration(cancelPartyRegistration.getVenId(), true);
		}catch(Exception e){
			log.error("Exception in setSendCancelVenRegistration flag in PollState !!! "+e);
		}
		
		return res;
		
	}

	/* (non-Javadoc)
	 * @see com.honeywell.dras.services.soap.vtn.party.PartyRequestProcessor#canceledPartyRegistration(com.honeywell.dras.vtn.api.registration.CanceledPartyRegistration)
	 */
	@Override
	public Response canceledPartyRegistration(
			CanceledPartyRegistration canceledPartyRegistration)
			throws VtnDrasServiceException {
		if (canceledPartyRegistration == null 
				|| canceledPartyRegistration.getRegistrationId() == null
				|| canceledPartyRegistration.getVenId() == null) {
			log.error("Invalid request parameters");
			throw new VtnDrasServiceException("Invalid request parameters");
		}
		
		Response resp = new Response();
		
		try {
			Endpoint ep = endpointManager.findByVenId(canceledPartyRegistration.getVenId());
			//After getting cancelled acknowledgement from the ven we set the cancelled flag to true 
			ep.setCanceled(Boolean.TRUE);
			endpointManager.updateEndpoint(ep);
			
			if (ep.getCanceled().booleanValue()) {
				resp.setResponseCode("200");
				resp.setResponseDescription("Success");
			} else {
				resp.setResponseCode("400");
				resp.setResponseDescription("Not Canceled. Error in cancelPartyRegistration");
			}
		} catch (EntityNotFoundException e) {
			log.error("Unable to cancel Party", e);
			throw new VtnDrasServiceException("Invalid vendID");
		}
		
		resp.setCertCommonName(canceledPartyRegistration.getCertCommonName());
		resp.setRequestId(canceledPartyRegistration.getResponse().getRequestId());
		resp.setSchemaVersion(canceledPartyRegistration.getSchemaVersion());
		
		return resp;
	}

	/* (non-Javadoc)
	 * @see com.honeywell.dras.services.soap.vtn.party.PartyRequestProcessor#registerFingerPrint(com.honeywell.dras.vtn.api.common.FingerPrint)
	 */
	@Override
	public Result registerFingerPrint(FingerPrint fingerPrint)
			throws VtnDrasServiceException {
		if (fingerPrint == null || fingerPrint.getVenId() == null || fingerPrint.getVenId().isEmpty()) {
			return Result.FAILURE;
		}
		
		try {
			Endpoint endpoint = endpointManager.findByVenId(fingerPrint.getVenId());
			endpoint.setFingerprint(fingerPrint.getFingerprint());
			endpointManager.updateEndpoint(endpoint);
		} catch (EntityNotFoundException e) {
			log.error("Error retrieving endpoint for updating fingerprint", e);
			return Result.FAILURE;
		}
		
		return Result.SUCCESS;
		
	}
	
	private CreatedPartyRegistration generateCreatedPartyReg(String requestId, String venId) {
		CreatedPartyRegistration cpReg = new CreatedPartyRegistration();
		String vtnId = System.getProperty(Constants.VTN_ID_PROPERTY); //Constants.VTNID;
		if (vtnId == null || vtnId.isEmpty()) {
			log.error("DRAS VTN ID is not set");
			Response resp = new Response();
			resp.setResponseCode("400");
			resp.setResponseDescription("Server Error - vtnId");
			resp.setRequestId(requestId);
			resp.setVenId(venId);
			cpReg.setRequestId(requestId);
			cpReg.setResponse(resp);
			return cpReg;
		}
		
		// set response
		Response resp = new Response();
		resp.setResponseCode("200");
		resp.setResponseDescription("Success");
		resp.setRequestId(requestId);
		resp.setVenId(venId);
		cpReg.setRequestId(requestId);
		cpReg.setResponse(resp);
		
		// set cpReg
		cpReg.setVtnId(vtnId);
		String pollFrequency = System.getProperty(Constants.POLL_FREQ);
		//cpReg.setPollFrequency(new Long(Constants.POLL_FREQ));
		if(pollFrequency == null || pollFrequency.isEmpty())
		{
			log.error("Poll Frequency is Null");
			cpReg.setPollFrequency(new Long(Constants.DEFAULT_POLL_FREQ));
		}
		
		else
		{
				
			try
			{
				cpReg.setPollFrequency(Long.parseLong(pollFrequency));
			}
			catch(NumberFormatException n)
			{
				cpReg.setPollFrequency(new Long(Constants.DEFAULT_POLL_FREQ));
			}
		}
		cpReg.setVenId(venId);
		
		Profile prof = null;
		
		if (Constants.SUPPORTS_OPENADR_2B) {
			prof = new Profile();
			prof.setProfileName(ProfileName.PROFILE2B);
			prof.getTransports().add(TransportName.simpleHttp);
			prof.getTransports().add(TransportName.xmpp);
			cpReg.getProfiles().add(prof);
		}
		
		if (Constants.SUPPORTS_OPENADR_2A) {
			prof = new Profile();
			prof.setProfileName(ProfileName.PROFILE2A);
			prof.getTransports().add(TransportName.simpleHttp);
			cpReg.getProfiles().add(prof);
		}		
		return cpReg;	
	}
	
	
	/*private void sendOpenadrPushMessage(CreatePartyRegistration createPartyRegistration)
	{
		RegisterReport registerReport = reportRequestManager.getRegisterReportForVenId(createPartyRegistration.getVenId(), createPartyRegistration.getSchemaVersion());
		if(null == registerReport){
			log.info("The report already registered so not requesting for another register report.");
			return;
		}
		
		RegisterReportRequest registerReportRequest = new RegisterReportRequest();
		registerReportRequest.setRegisterReport(registerReport);
		PushProfile pushProfile = new PushProfile();
		pushProfile.setProfileName(createPartyRegistration.getProfileName());
		pushProfile.setPushUrl(createPartyRegistration.getTransportAddress());
		//Taking the first transport type to send the message
		TransportName transportName = null;
		for(TransportName name: createPartyRegistration.getTransports()){
			transportName = name;
			break;
		}
		pushProfile.setTransport(transportName);
		registerReportRequest.setPushProfile(pushProfile);
		
		//Publish the Push messages to QUEUE  
		
		OpenADRPushMDB oadrPushMDB = EJBFactory.getBean(OpenADRPushMDBManagerBean.class);
		oadrPushMDB.processMessage(registerReportRequest);
		
	}*/
}
