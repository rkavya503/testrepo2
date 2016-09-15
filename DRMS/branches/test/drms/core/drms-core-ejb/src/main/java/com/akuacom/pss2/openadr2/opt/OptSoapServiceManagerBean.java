package com.akuacom.pss2.openadr2.opt;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.akuacom.pss2.openadr2.opt.eao.OptRequestManager;
import com.akuacom.pss2.openadr2.party.PartyRequestProcessor;
import com.honeywell.dras.vtn.api.OptType;
import com.honeywell.dras.vtn.api.common.Response;
import com.honeywell.dras.vtn.api.opt.CancelOpt;
import com.honeywell.dras.vtn.api.opt.CanceledOpt;
import com.honeywell.dras.vtn.api.opt.CreateOpt;
import com.honeywell.dras.vtn.api.opt.CreatedOpt;
import com.honeywell.dras.vtn.dras.service.VtnDrasServiceException;

@Stateless 
public class OptSoapServiceManagerBean implements OptSoapServiceManager.L, OptSoapServiceManager.R{
	
	private String optRequestError;
	private String responseCode;
	
	@EJB
	private OptRequestProcessor.L optRequestProcessor;
	
	
	@EJB
	private OptRequestManager.L optRequestManager;
	
	@EJB
	private PartyRequestProcessor.L partyRequestProcessor;
	
	private Logger log = Logger.getLogger(OptSoapServiceManagerBean.class);

	@Override
	public CreatedOpt createOpt(CreateOpt createOpt) throws VtnDrasServiceException  {
		boolean isOptRequestValid = isCreateOptRequestValid(createOpt);
		if(!isOptRequestValid){
			return createOptResponse(createOpt, this.responseCode, this.optRequestError);
		}
		boolean isOptCreated = false;
		try {
			OptRequest optRequest = optRequestProcessor.processCreateOpt(createOpt);
			isOptCreated = optRequestManager.createOptRequest(optRequest);
		} catch(Exception e) {
			log.error("Error in processing create opt request"+e.getStackTrace());
			throw new VtnDrasServiceException("Error in processing create opt request");
		}
		
		return createOptResponse(createOpt,isOptCreated);
	}

	@Override
	public CanceledOpt cancelOpt(CancelOpt cancelOpt) throws VtnDrasServiceException {
		boolean isRequestValid = isCancelOptRequestValid(cancelOpt);
		if(!isRequestValid){
			return cancelOptResponse(cancelOpt,this.responseCode,this.optRequestError);
		}
		CanceledOpt response = null;
		try {
			boolean status = optRequestManager.cancelOptRequest(cancelOpt.getOptId());
			response =  cancelOptResponse(cancelOpt,status);
		} catch (Exception e) {
			log.error("Error in processing create opt request"+e.getStackTrace());
			throw new VtnDrasServiceException("Error in processing cancel opt request");
		}
		return response;
	}
	
	public boolean canCreateEvent(String resourceId,String marketContext,Date startTime , Long duration){
		boolean createEvent = false;
			List<OptRequest> optReqList = optRequestManager.findOptRequestByResourceEntityId(resourceId);
			if(optReqList.size() ==0){
				createEvent = true;
			}
			Collections.sort(optReqList); // Need to check with latest req
			for(OptRequest optReq :optReqList ){
				if(!isOptAvailabilityOverlappingWithEvent(optReq.getOptAvailability(), startTime, duration)){
					continue;
				}
				if(isOptRequestOnlyForMarketContext(optReq)){
					if(!isMarketContextSame(optReq.getOptTarget(),marketContext)){
						continue;
					}
				}
				if(optReq.getOptType().equalsIgnoreCase(OptType.OPT_IN.toString())){
					createEvent=true;
					break;
				}
				else if(optReq.getOptType().equalsIgnoreCase(OptType.OPT_OUT.toString())){
					createEvent = false;
					break;
				}
			}
		return createEvent;
	}
	private boolean isOptAvailabilityOverlappingWithEvent(List<OptAvailability> optAvailList , Date eventStartTime , long eventDuration){
		boolean isOverlapFound = false;
		for(OptAvailability optAvail : optAvailList){
			Date optStartTime = optAvail.getStartTime();
			Date optEndTime = new Date (optAvail.getStartTime().getTime() + optAvail.getDuration());
			Date eventEndTime = new Date (eventStartTime.getTime() + eventDuration);
			
			isOverlapFound = optStartTime.before(eventEndTime) && eventStartTime.before(optEndTime);
			if(isOverlapFound){
				break;
			}
		}
		return isOverlapFound;
	}
	private boolean isOptRequestOnlyForMarketContext(OptRequest optReq){
		List<OptTarget> optTargetList = optReq.getOptTarget();
		if(null == optTargetList){
			return false;
		}
		boolean isOptTypeMarketContest = false;
		int count = 0;
		for(OptTarget optTarget : optTargetList){
			count++;
			isOptTypeMarketContest = optTarget.getTargetType().equalsIgnoreCase(OptTargetType.MARKETCONTEXT.toString());
			if(!isOptTypeMarketContest || 1<count){
				break;
			}
		}
		return isOptTypeMarketContest;
	}
	private boolean isMarketContextSame(List<OptTarget> optTargetList, String eventMarketContext){
		boolean isMarketContextSame = false;
		for(OptTarget optTarget : optTargetList){
			if(!optTarget.getTargetType().equalsIgnoreCase(OptTargetType.MARKETCONTEXT.toString())){
				continue;
			}
			isMarketContextSame = optTarget.getTargetValue().contains(eventMarketContext);
		}
		return isMarketContextSame;
	}
	private boolean isCreateOptRequestValid(CreateOpt createOpt){
		boolean isRequestValid = true;
		String optId = createOpt.getOptId();
		if((null == optId) || optId.isEmpty()){
			this.optRequestError = "Invalid Opt Id";
			this.responseCode ="400";
			return false;
		}
		else if((createOpt.getRequestId() == null) || createOpt.getRequestId().isEmpty()){
			this.optRequestError = "Invalid Request Id";
			this.responseCode ="400";
			return false;
		}
		else if(!partyRequestProcessor.isVenValid(createOpt.getVenId(), createOpt.getCertCommonName())){
			this.optRequestError = "Invalid VEN Id";
			this.responseCode ="400";
			return false;
		}
		/*else if(!((createOpt.getOptType().equals(OptType.OPT_IN)) || createOpt.getOptType().equals(OptType.OPT_OUT))){
			this.optRequestError = "Invalid Opt Type";
			this.responseCode ="400";
			return false;
		}*/
		//blocking optional field check
		/*else if(null ==createOpt.getAvailibilityList() || (createOpt.getAvailibilityList().size()==0) ){
			this.optRequestError = "Request should have atleast one availability";
			this.responseCode ="400";
			return false;
		}*/
		return isRequestValid;
	}
	private boolean isCancelOptRequestValid(CancelOpt cancelOpt){
		/*boolean isRequestValid = true;
		String optId = cancelOpt.getOptId();
		if((null == optId) || optId.equals("")){
			this.optRequestError = "Invalid Opt Id";
			this.responseCode ="400";
			isRequestValid = false;
		}*/
		return isOptIdValid(cancelOpt.getOptId());
	}
	private boolean isOptIdValid(String optId ){
		if((null == optId) || optId.equals("")){
			setInvalidOptIdError();
		}
		List<OptRequest> requests = optRequestManager.findOptRequestByOptId(optId);
		if(null == requests || 0 == requests.size()){
			setInvalidOptIdError();
			return false;
		}
		boolean isOptIdFound = false;
		for(OptRequest optRequest : requests){
			if(optRequest.getOptID().equalsIgnoreCase(optId)){
				isOptIdFound = true;
				break;
			}
		}
		if(!isOptIdFound){
			setInvalidOptIdError();
		}
		return isOptIdFound;
	}
	private void setInvalidOptIdError(){
		this.optRequestError = "Invalid Opt Id";
		this.responseCode ="452";
	}
		
	private CreatedOpt createOptResponse(CreateOpt createOpt, boolean optCreatedStatus){
		String description = "";
		String resCode = "";
		if(optCreatedStatus){
			description = "Honeywell Akuacom ";
			resCode = "200";
		}
		else{
			description = "Error in creating opt request";
			resCode = "400";
		}
		return createOptResponse(createOpt,resCode , description);
	}
	private CreatedOpt createOptResponse(CreateOpt createOpt,String resCode , String description){
		CreatedOpt createdOpt = new CreatedOpt();
		createdOpt.setCertCommonName(createOpt.getCertCommonName());
		createdOpt.setFingerprint(createOpt.getFingerprint());
		createdOpt.setOptId(createOpt.getOptId());
		createdOpt.setRequestId(createOpt.getRequestId());
		
		Response response = new Response();
		response.setRequestId(createOpt.getRequestId());
		response.setResponseCode(resCode);
		response.setResponseDescription(description);
		createdOpt.setResponse(response);
		
		createdOpt.setSchemaVersion(createOpt.getSchemaVersion());
		createdOpt.setVenId(createOpt.getVenId());
		
		
		return createdOpt;
		
	}
	private CanceledOpt cancelOptResponse(CancelOpt cancelOpt , boolean cancelOptStatus){
		String description = "";
		String resCode = "";
		if(cancelOptStatus){
			description = "Honeywell Akuacom ";
			resCode = "200";
		}
		else{
			description = "Error in cancelling opt request";
			resCode = "400";
		}
	 return cancelOptResponse(cancelOpt,resCode,description);
	}
	private CanceledOpt cancelOptResponse(CancelOpt cancelOpt , String resCode , String description){
		CanceledOpt canOptResponse = new CanceledOpt();
		canOptResponse.setCertCommonName(cancelOpt.getCertCommonName());
		canOptResponse.setFingerprint(cancelOpt.getFingerprint());
		canOptResponse.setOptId(cancelOpt.getOptId());
		canOptResponse.setRequestId(cancelOpt.getRequestId());
		canOptResponse.setSchemaVersion(cancelOpt.getSchemaVersion());
		canOptResponse.setVenId(cancelOpt.getVenId());
		
		Response res = new Response();
		res.setRequestId(cancelOpt.getRequestId());
		res.setResponseCode(resCode);
		res.setResponseDescription(description);
		canOptResponse.setResponse(res)	;	
		
		return canOptResponse;
	}
}
