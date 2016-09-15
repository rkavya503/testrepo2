package com.akuacom.pss2.openadr2.opt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;

import org.jboss.logging.Logger;
import org.openadr.dras.eventstate.EventState;

import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantEAO;
import com.akuacom.pss2.event.participant.EventParticipantManager;
import com.akuacom.pss2.openadr2.endpoint.EndpointManager;
import com.akuacom.pss2.openadr2.endpoint.EndpointMapping;
import com.akuacom.pss2.openadr2.opt.eao.OptRequestManager;
import com.akuacom.pss2.participant.Participant;
import com.honeywell.dras.vtn.api.OptType;
import com.honeywell.dras.vtn.api.common.Target;
import com.honeywell.dras.vtn.api.opt.Availibility;
import com.honeywell.dras.vtn.api.opt.CreateOpt;

@Stateless 
public class OptRequestProcessorBean implements OptRequestProcessor.L {
	private Logger log = Logger.getLogger(OptRequestProcessorBean.class);
	
	@EJB
	OptRequestManager.L optRequestManager;
	
	@EJB
	EventParticipantManager.L eventParticipantManager;
	
	@EJB
	com.akuacom.pss2.event.EventManager.L eventManager;
	@EJB
	private EndpointManager.L endpointManager;
	@EJB
	private EventParticipantEAO.L eventParticipationEao;
	@EJB
	private ClientManager.L clientManager;
	
	/*@EJB
	private CamelRoutingManager camelBootStrap;*/
	

	
	public OptRequest processCreateOpt(CreateOpt createOpt){
		processCreateOptForEvent(createOpt.getEventId(),createOpt.getOptType());
		if(!createOpt.getOptType().toString().equalsIgnoreCase(OptType.OPT_IN.toString())){
			handleOptForResource(createOpt.getVenId());
		}
		return  createOptRequestEntity(createOpt);
	}
	
	private void processCreateOptForEvent(String eventName,OptType optType){
		if((null == eventName) || ("".equals(eventName))){
			return;
		}
		Event event = null;
		event = eventManager.getEventOnly(eventName);
		if(null == event){
			log.error("Could not create opt request for : " + eventName+ " since event object is null");
			return;
		}
		List<String> eveResourceList = eventParticipantManager.getParticipantsForEvent(eventName, true);
		if(null == eveResourceList){
			return;
		}
		List<EventParticipant> participantsList = new ArrayList<EventParticipant>();
		for(String party:eveResourceList) {
			EventParticipant eventParticipant = eventParticipantManager.getEventParticipant(eventName, party, true);
			if(eventParticipant != null){
				participantsList.add(eventParticipant);
			}
		}
		
		//EventServiceBusData data = new EventServiceBusData();
		for(EventParticipant eveResource : participantsList){
			Participant client = eveResource.getParticipant();
			
			if(optType.toString().equalsIgnoreCase(OptType.OPT_IN.toString())) {
				//data.configureOptIn(event, eveResource);				
				
			} else if(optType.toString().equalsIgnoreCase(OptType.OPT_OUT.toString())) {
				//data.configureOptOut(eveResource, eveResource.getEntityId());
				eventManager.removeParticipantFromEvent(event.getEventName(), client.getParent());				
				
			}
			/*try {
				camelBootStrap.sendEnterpriseMessage(new EventServiceRoute(), data);
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		}
	}
	private OptRequest createOptRequestEntity(CreateOpt createOpt){
		
		OptRequest optRequestEntity = new OptRequest();
		optRequestEntity.setOptID(createOpt.getOptId());
		optRequestEntity.setRequestId(createOpt.getRequestId());
		optRequestEntity.setOptType(createOpt.getOptType().toString());
		optRequestEntity.setOptReason(createOpt.getOptReason());
		optRequestEntity.setVenId(createOpt.getVenId());
		
		optRequestEntity.setOptTarget(createOptTargetEntity(createOpt));
		optRequestEntity.setOptResource(createOptResourceEntityForAllTargets(createOpt));
		if(null != createOpt.getAvailibilityList() && createOpt.getAvailibilityList().size()>0){
			optRequestEntity.setOptAvailability(createOptAvailabilityEntity(createOpt.getAvailibilityList()));
		}
		return optRequestEntity;
		
	}
	private List<OptTarget> createOptTargetEntity(CreateOpt createOpt){
		List<OptTarget> optTargetEntityList = new ArrayList<OptTarget>();
	    List<String> marketContextList = new ArrayList<String>();
	    marketContextList.add(createOpt.getMarketContext());
		createOptTargetEntityForMarketContext(marketContextList, optTargetEntityList);
		List<String> eventIdList = new ArrayList<String>();
		createOptTargetEntityForEvent(eventIdList, optTargetEntityList);
		createOptTargetEntity(createOpt.getTarget(),optTargetEntityList);
		
		return optTargetEntityList;
	}
	private void createOptTargetEntity(Target optReuestTarget,List<OptTarget> optTargetEntityList){
		if(null == optReuestTarget){
			return;
		}
		createOptTargetEntityForParty(optReuestTarget.getPartyIdList(), optTargetEntityList);
		createOptTargetEntityForGroup(optReuestTarget.getGroupIdList(), optTargetEntityList);
		createOptTargetEntityForVen(optReuestTarget.getVenIdList(), optTargetEntityList);
		createOptTargetEntityForResource(optReuestTarget.getResourceIdList(), optTargetEntityList);
	}
	private void createOptTargetEntityForParty(List<String> partyIdList, List<OptTarget> optTargetEntityList){
		
		createOptTargetEntity(partyIdList,OptTargetType.PARTY,optTargetEntityList);
	}
	private void createOptTargetEntityForGroup(List<String> groupIdList, List<OptTarget> optTargetEntityList){
		createOptTargetEntity(groupIdList,OptTargetType.GROUP,optTargetEntityList);
	}
	private void createOptTargetEntityForResource(List<String> resourceIdList, List<OptTarget> optTargetEntityList){
		createOptTargetEntity(resourceIdList,OptTargetType.RESOURCE,optTargetEntityList);
	}
	private void createOptTargetEntityForVen(List<String> venIdList, List<OptTarget> optTargetEntityList){
		createOptTargetEntity(venIdList,OptTargetType.VEN,optTargetEntityList);
		
	}
	private void createOptTargetEntityForMarketContext(List<String> marketContextList,List<OptTarget> optTargetEntityList){
		createOptTargetEntity(marketContextList,OptTargetType.MARKETCONTEXT,optTargetEntityList);
	}
	private void createOptTargetEntityForEvent(List<String> eventIdList,List<OptTarget> optTargetEntityList){
		createOptTargetEntity(eventIdList,OptTargetType.EVENT,optTargetEntityList);
	}
	
	private void createOptTargetEntity(List<String> targetValue, OptTargetType optType,List<OptTarget> optTargetEntityList){
		if((null == targetValue) ||(0==targetValue.size())){
			return;
		}
		OptTarget optTargetEntity = new OptTarget();
		optTargetEntity.setTargetType(optType.toString());
		optTargetEntity.setTargetValue(targetValue);
		
		optTargetEntityList.add(optTargetEntity);
	}
	private List<OptAvailability> createOptAvailabilityEntity(List<Availibility> availabilityList){
		List<OptAvailability>  optAvailabilityList  = new ArrayList<OptAvailability>();
		
		for(Availibility availability : availabilityList){
			OptAvailability optAvailabilityEntity = new OptAvailability();
			optAvailabilityEntity.setStartTime(availability.getStartTime());
			optAvailabilityEntity.setDuration(availability.getDuration());
			
			optAvailabilityList.add(optAvailabilityEntity);
		}
		
		return optAvailabilityList;
	}
	
	private List<OptResource> createOptResourceEntityForAllTargets(CreateOpt createOpt){
		List<OptResource> optResourceEntityList = new ArrayList<OptResource>();
		createOptResourceEntityForMarketContext(createOpt.getMarketContext(), optResourceEntityList, createOpt.getVenId());
		createOptResourceEntityForEvent(createOpt.getEventId(),optResourceEntityList);
		createOptRequestEntityForTarget(createOpt.getTarget(), optResourceEntityList);
		
		return optResourceEntityList;
	}
	private void createOptResourceEntityForMarketContext(String marketContext, 
			List<OptResource> optResourceEntityList, String venId){
		
		if((null == marketContext) ||("".equals(marketContext))){
			return;
		}
		List<String> mcList = new ArrayList<String>();
		mcList.add(marketContext);
		List<Participant> resourceList = optRequestManager.findResourcesByProgramName(mcList, venId);
		createOptResourceEntity(resourceList,optResourceEntityList);
	}
	private void createOptResourceEntityForEvent(String eventName,List<OptResource> optResourceEntityList){
		if((null == eventName) || ("".equals(eventName))){
			return;
		}
		Event event = null;
		try {
			event = eventManager.getEventOnly(eventName);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		if(null == event){
			return;
		}
		List<String> eveResourceList = eventParticipantManager.getParticipantsForEvent(eventName, true);
		
		List<Participant> resourceList = new ArrayList<Participant>();
		for(String party:eveResourceList) {
			EventParticipant eventParticipant = eventParticipantManager.getEventParticipant(eventName, party, true);
			if(eventParticipant != null){
				resourceList.add(eventParticipant.getParticipant());
			}
		}		
		createOptResourceEntity(resourceList,optResourceEntityList);
	}
	
	private void createOptRequestEntityForTarget(Target target , List<OptResource> optResourceEntityList){
		if(null == target){
			return;
		}
		createOptResourceEntityForParty(target.getPartyIdList(), optResourceEntityList);
		//createOptResourceEntityForGroup(target.getGroupIdList(), optResourceEntityList);
		createOptResourceEntityForVen(target.getVenIdList(), optResourceEntityList);
		
		List<String> resourcesIdInOptRequest = target.getResourceIdList();
		if(null == resourcesIdInOptRequest){
			return;
		}
		for(String resourceId :resourcesIdInOptRequest ){
			createOptResourceEntity(resourceId,optResourceEntityList);
		}
	}
	
	private void createOptResourceEntityForParty(List<String> partyNameList,List<OptResource> optResourceEntityList ){
		List<Participant> resourceList = optRequestManager.findResourcesByPartyName(partyNameList);
		createOptResourceEntity(resourceList,optResourceEntityList);
	}
	
	private void createOptResourceEntityForVen(List<String> venIdList,List<OptResource> optResourceEntityList ){
		List<Participant> resourceList = optRequestManager.findResourcesByVenId(venIdList);
		createOptResourceEntity(resourceList,optResourceEntityList);
		
	}
	
	private void createOptResourceEntity(List<Participant> resourceList, List<OptResource> optResourceEntityList){
		if(null == resourceList){
			return;
		}
		for(Participant resource : resourceList){
			OptResource optResource = new OptResource();
			optResource.setResourceID(resource.getUUID());			
			optResourceEntityList.add(optResource);
		}
	}
	
	private void createOptResourceEntity(String resourceId, List<OptResource> optResourceEntityList){
		OptResource optResource = new OptResource();
		optResource.setResourceID(resourceId);
		
		optResourceEntityList.add(optResource);
	}
	private void handleOptForResource(String venId){
		if(null == venId || venId.isEmpty()){
			return;
		}
		List<EndpointMapping> erLinks = endpointManager.findEndpointParticipantLinksByEndpointVenId(venId);
		if(null == erLinks || erLinks.isEmpty()){
			return;
		}
		Set<String> clientIdList = new TreeSet<String>();
		for(EndpointMapping er : erLinks){
			clientIdList.add(er.getParticipantName());
		}
		for(String clientId : clientIdList){
			List<EventState> evtStates =clientManager.getAllDrasEventStateForClient(clientId);	
			if(null == evtStates){
				continue;
			}
			for(EventState es : evtStates){
				Participant client = clientManager.getClientOnly(clientId);
				if(null == client){
					continue;
				}
				eventManager.removeParticipantFromEvent(es.getEventIdentifier(), client.getParent());
			}
		}
	}
}
