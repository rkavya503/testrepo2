
package com.akuacom.pss2.openadr2.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.openadr.dras.eventstate.EventState;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.client.ClientEAO;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.ClientConversationState;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantEAO;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.openadr2.Constants;
import com.akuacom.pss2.openadr2.endpoint.Endpoint;
import com.akuacom.pss2.openadr2.endpoint.EndpointManager;
import com.akuacom.pss2.openadr2.endpoint.EndpointMapping;
import com.akuacom.pss2.openadr2.event.eao.EventRequestManager;
import com.akuacom.pss2.openadr2.event.util.EventConverter;
import com.akuacom.pss2.openadr2.event.validator.EventRequestValidatorBean;
import com.akuacom.pss2.openadr2.opt.OptSoapServiceManager;
import com.akuacom.pss2.openadr2.poll.eao.OadrPollStateManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.ProgramPerf;
import com.akuacom.pss2.program.ProgramPerfGenEAO;
import com.akuacom.pss2.program.aggregator.ProgramAggregator;
import com.akuacom.pss2.program.aggregator.eao.ProgramAggregatorEAO;
import com.akuacom.pss2.program.aggregator.eao.ProgramAggregatorEAOManager;
import com.akuacom.pss2.program.apx.aggregator.eao.ApxAggregatorEAOManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManager;
import com.akuacom.pss2.query.ParticipantClientListFor20Ob;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.MemorySequence;
import com.akuacom.pss2.util.OperationModeValue;
import com.honeywell.dras.vtn.api.OptType;
import com.honeywell.dras.vtn.api.common.EiTarget;
import com.honeywell.dras.vtn.api.common.Response;
import com.honeywell.dras.vtn.api.event.CreatedEvent;
import com.honeywell.dras.vtn.api.event.DistributeEvent;
import com.honeywell.dras.vtn.api.event.Event;
import com.honeywell.dras.vtn.api.event.RequestEvent;
import com.honeywell.dras.vtn.api.event.Signal;
import com.honeywell.dras.vtn.api.opt.ServiceDeliveryPoint;
import com.honeywell.dras.vtn.dras.service.VtnDrasServiceException;

@Stateless
public class EventRequestProcessorBean implements EventRequestProcessor.L,EventRequestProcessor.R {
	
	private Logger log = Logger.getLogger(EventRequestProcessorBean.class);
	
	@EJB
	private EndpointManager.L endpointManager;
	@EJB
	private EventManager.L eventManager;
	@EJB
	private ClientManager.L clientManager;
	@EJB
	private ProgramPerfGenEAO.L programEJB ;
	@EJB
	private ClientEAO.L clientEAO;
	@EJB
	private EventRequestManager.L eventRequestManager;
	@EJB
	private EventRequestValidatorBean.L eventRequestValidator ;
	@EJB
	private OadrPollStateManager.L oadrPollStateManager;
	@EJB
	private EventEAO.L eventEao;
	@EJB
	private OptSoapServiceManager.L optService;
	@EJB
	private ApxAggregatorEAOManager.L apxAggEaoManager;	
	@EJB
	private ProgramAggregatorEAOManager.L prgAggEaoManager;	
	@EJB
	private ParticipantManager.L particiapntManager;	
	@EJB
	private ProgramParticipantAggregationManager.L ppManager;	
	@EJB
    protected CorePropertyEAO.L coreProperty;	
	private static boolean isPGEInstnace;
	@EJB
    protected EventParticipantEAO.L eventParticipantEAO;
	@EJB
	protected ProgramAggregatorEAO.L programAggregatorEAO;
	@Override
	public DistributeEvent requestEvent(RequestEvent requestEvent)
			throws VtnDrasServiceException {
		log.info("requestEvent called");
		DistributeEvent distEvent = this.createDistributeEventObj();
		if(!eventRequestValidator.isRequestEventValid(requestEvent)){
			distEvent.setResponse(eventRequestValidator.getErrorResponse());
			return distEvent;
		}
		try {
			Endpoint endpoint = endpointManager.findByVenId(requestEvent.getVenId());
			if (endpoint.getReportOnly() ) {
				distEvent.getResponse().setResponseCode("410");
				distEvent.getResponse().setResponseDescription("VEN configured for report only");
				return distEvent;
			}
			
		} catch (EntityNotFoundException e) {
			throw new VtnDrasServiceException("VEN is not valid");
		}
		log.info("requestEvent called for ven ID " + requestEvent.getVenId());
		String requestId = Constants.REQUEST_ID_PREFIX + MemorySequence.getNextSequenceId();
		distEvent.getResponse().setRequestId(requestId);
		distEvent.setRequestId(requestId);
		distEvent.setCertCommonName(requestEvent.getCertCommonName());
		distEvent.setSchemaVersion(requestEvent.getSchemaVersion());
		distEvent.setVenId(requestEvent.getVenId());
		distEvent.getResponse().setVenId(requestEvent.getVenId());
		boolean isPGE = getInstance();
		
		String venId = requestEvent.getVenId();		
		List<EndpointMapping> erLinks = endpointManager.findEndpointParticipantLinksByEndpointVenId(requestEvent.getVenId());
		HashMap<String,com.honeywell.dras.vtn.api.event.Event> oadrEventIdAndOadrEventMap = new HashMap<String,com.honeywell.dras.vtn.api.event.Event>();
		HashMap<String, Participant> aggregatorChilds = null;
		for (EndpointMapping erl : erLinks) {			
			Participant participant = erl.getParticipant();			
			String clientId = erl.getParticipantName();
			String eventName = null;
			String parentName = participant.getParent();
			boolean mainParticipantCalling = true;
			HashMap<String, Event> createOadrEvent = createOadrEvent(requestId, venId, clientId,eventName, isPGE,parentName, mainParticipantCalling);
			if(createOadrEvent != null && !createOadrEvent.isEmpty()) {
				Set<String> keySet = createOadrEvent.keySet();
				for(String keyEvent: keySet) {
					Event event = createOadrEvent.get(keyEvent);
					if(event != null) {
						eventName = event.getEventID();
						oadrEventIdAndOadrEventMap.put(event.getEventID(), event);
					}
				}
			}
			Set<String> eventIds = new HashSet<String>();
			eventIds.addAll(oadrEventIdAndOadrEventMap.keySet());
			eventIds.addAll(programAggregatorEAO.findEventIdsByClientName(participant.getParent()));
			Map<String,String> evtProgMap =new HashMap<String, String>();
			Set<String> accounts =new TreeSet<String>();
		    Map<String,List<Participant>> eventIdAndAggregatorChildMap = new HashMap <String,List<Participant>>();
		    List<Participant> partList = new ArrayList<Participant>();
		    List<String> parentList = new ArrayList<String>();
		    parentList.add(erl.getParticipant().getParticipantName());
		    Map<String,List<EventParticipant>> mapOfEventAndEventParticipantForClients = new HashMap<String,List<EventParticipant>>();
		    Map<String,List<EventParticipant>>mapOfEventAndEventParticipantForParent =  new HashMap<String,List<EventParticipant>>();
		    Map<String,ParticipantClientListFor20Ob>  clientParentMap = new HashMap<String,ParticipantClientListFor20Ob>();
			if(eventIds.size()>0){
				mapOfEventAndEventParticipantForParent = eventParticipantEAO.findEventParticipantWithSignalsByAllEventNameAndClientUUIDForEvent(parentList,eventIds);
				evtProgMap =  eventEao.findEventIdAndProgramName(eventIds);
				accounts =new TreeSet<String>(prgAggEaoManager.getAggregatorResources(parentName, eventIds));
				if(accounts.size()>0){
					eventIdAndAggregatorChildMap = eventParticipantEAO.findEventParticipantWithParticipantByAccont(accounts);
				}
				for(Entry<String,List<Participant>> entry: eventIdAndAggregatorChildMap.entrySet()) {
					partList.addAll(entry.getValue());
				}
				List<String> clientListNew = new ArrayList<String>();				
				for(Participant par: partList) {
					clientListNew.add(par.getParticipantName());
				}
				if(clientListNew.size()>0){
					mapOfEventAndEventParticipantForClients = eventParticipantEAO.findEventParticipantWithSignalsByAllEventNameAndClientUUIDForEvent(clientListNew,eventIds);
				}
				clientParentMap.putAll((particiapntManager.getParticipantCLientListFor20Ob(new ArrayList<String>(parentList))));
	        	clientParentMap.putAll((particiapntManager.getParticipantCLientListFor20Ob(new ArrayList<String>(clientListNew))));
			}
				if(eventIds!=null){
				for(String oadrEvent : eventIds){
					eventName = oadrEvent;
					List<Participant> aggregatorClients = new ArrayList<Participant>();
					if(!isPGE){
						aggregatorClients = eventIdAndAggregatorChildMap.get(eventName);	
					}else{
						aggregatorChilds = getAggregatorChilds(participant,evtProgMap.get(oadrEvent));	
						if(aggregatorChilds != null && !aggregatorChilds.isEmpty()){
							aggregatorClients = getAggregatorParticipantClients(aggregatorChilds, eventName , clientId);	
						}
					}
						HashMap<String, Event> aggregateSignals = new HashMap<String, Event>();
							
							List<String> aggregaotorID = new ArrayList<String>();
							aggregaotorID.add(erl.getParticipant().getUUID());
							
							if(aggregatorClients != null && !aggregatorClients.isEmpty()) {
							HashMap<Participant, ArrayList<EventParticipantSignalEntry>> aggChlidrenSignlaList = getAggChlidrenSignlaList(mapOfEventAndEventParticipantForParent.get(eventName));
							List<String> clientList = new ArrayList<String>();				
							for(Participant par: aggregatorClients) {
								clientList.add(par.getUUID());
							}
							HashMap<Participant, ArrayList<EventParticipantSignalEntry>> listOfSigEntry = getAggChlidrenSignlaList(mapOfEventAndEventParticipantForClients.get(eventName));
							Map<String, ArrayList<String>> groupList = checkSignalsforAggregatorChildParticipats(aggChlidrenSignlaList, listOfSigEntry);
							if(!oadrEventIdAndOadrEventMap.isEmpty()){
								Event aggEvent = oadrEventIdAndOadrEventMap.get(eventName);
								aggregateSignals = aggregateSignals(requestId, venId, aggEvent, groupList,eventName, isPGE,clientParentMap);	//calling again createoadrevent	
							}else{
								aggregateSignals = aggregateSignals(requestId, venId, null, groupList,eventName, isPGE,clientParentMap);	//calling again createoadrevent	
							}
							for(Entry<String, Event> groupSetEvent : aggregateSignals.entrySet()){
								oadrEventIdAndOadrEventMap.put(groupSetEvent.getKey(), groupSetEvent.getValue());
							}
						  }
				}
			}
		}
		distEvent.getEventList().addAll(oadrEventIdAndOadrEventMap.values());
		
		try {
			oadrPollStateManager.setDistributeEventSent(requestEvent.getVenId(), true);
		} catch(Exception e) {
			log.error("Exception in setting RegisterReport flag in PollState !!! "+e);
		}
		sortEvent(distEvent.getEventList());
		return distEvent;
	}
	
	/**
	 * group the event messages which is having same signal 
	 * @param requestId
	 * @param venId
	 * @param aggEvent
	 * @param groupList
	 * @return
	 */
	private HashMap<String, Event> aggregateSignals(String requestId, String venId, Event aggEvent,	Map<String, ArrayList<String>> groupList,String eventName,
			boolean isPGE, Map<String, ParticipantClientListFor20Ob> clientParentMap) {
		HashMap<String,com.honeywell.dras.vtn.api.event.Event> oadrEventMap = new HashMap<String,com.honeywell.dras.vtn.api.event.Event>();		
		int iEvent=0;
			List<String> resourceIdList = null;
			for(Entry<String, ArrayList<String>> groupSet : groupList.entrySet()) {	
				String groupKey = groupSet.getKey();
				ArrayList<String> groupValue = groupSet.getValue();
				Set<ParticipantClientListFor20Ob> participantClientList = new HashSet<ParticipantClientListFor20Ob>();
				if(groupKey.equalsIgnoreCase("AggGroup") && aggEvent !=null) {
					EiTarget eventTarget = aggEvent.getTarget();
					resourceIdList = eventTarget.getResourceIdList();
					if(clientParentMap.size()>0){
						for(String grpVal : groupValue)
						{
							participantClientList.add(clientParentMap.get(grpVal));
						}
					}
					for(ParticipantClientListFor20Ob participantClient: participantClientList) {
						if(resourceIdList != null) {
							if(participantClient.getParentApplicationID() != null) {													
								resourceIdList.add(participantClient.getParentApplicationID());												
							}else if(!isPGE){
								resourceIdList.add(participantClient.getParentAccount());	
							}
						}
					}
					if(resourceIdList != null && !resourceIdList.isEmpty()) {
						eventTarget.setResourceIdList(resourceIdList);
						aggEvent.setTarget(eventTarget);
						List<Signal> signals = aggEvent.getSignals();
						Signal bidSignal = null;
						for(Signal sig: signals) {
							String name = sig.getName();
							if(name.equalsIgnoreCase("bid")) {
								bidSignal = sig;
								break;
							}
						}
						signals.remove(bidSignal);
						aggEvent.setSignals(signals);						
						oadrEventMap.put(aggEvent.getEventID(), aggEvent);
					}					
				} else {	
					int count =0;
					EiTarget eTarget = null;
					Event aggChildevent = null;
					String eventId = null;
					Set<ParticipantClientListFor20Ob> participantClientListForchild = new HashSet<ParticipantClientListFor20Ob>();
					for(String groupRes: groupValue) {
						if(count == 0 && clientParentMap.get(groupRes) != null) {
							ParticipantClientListFor20Ob participantClientListFor20Ob = clientParentMap.get(groupRes);
							String parentName = participantClientListFor20Ob.getParentName();
							HashMap<String, Event> createOadrEventforGroup = createOadrEvent(requestId, venId,participantClientListFor20Ob.getClientName(),eventName, isPGE,null,false);						
							for(Entry<String, Event> entrySet: createOadrEventforGroup.entrySet()) {
								aggChildevent = entrySet.getValue();
								if(aggChildevent != null){
									eventId = aggChildevent.getEventID();
									eventId = eventId + iEvent++;
									eTarget = aggChildevent.getTarget();
									resourceIdList = eTarget.getResourceIdList();	
									
									for(String resourceId:resourceIdList){
										participantClientListForchild.add(clientParentMap.get(resourceId));
									}
									List<String> resAppIDList = new ArrayList<String>();
									for(ParticipantClientListFor20Ob resId: participantClientListForchild) {
										if(resId!=null){
										if(!isPGE){
											resAppIDList.add(resId.getParentAccount());
										}else if(resId.getParentApplicationID()!=null){
											resAppIDList.add(resId.getParentApplicationID());
										}
										}
									}
									if(resourceIdList != null && !resAppIDList.isEmpty()){
										resourceIdList.clear();
										for(String res:resAppIDList) {
											resourceIdList.add(res);
										}									
									}
								}
								count = count +1;
							}
						} else {
							if(clientParentMap.get(groupRes) != null && resourceIdList != null) {
								ParticipantClientListFor20Ob participantClientListFor20Ob = clientParentMap.get(groupRes);
								String parent = participantClientListFor20Ob.getParentName();
								if(parent != null) {
										if(!isPGE){
											resourceIdList.add(participantClientListFor20Ob.getParentAccount());
										}else if(participantClientListFor20Ob.getParentApplicationID()!=null){
											resourceIdList.add(participantClientListFor20Ob.getParentApplicationID());
										}
								}
							}
						}						
					}					
					if(aggChildevent != null && resourceIdList != null && !resourceIdList.isEmpty()) {
						eTarget.setResourceIdList(resourceIdList);
						aggChildevent.setTarget(eTarget);
						List<Signal> signals = aggChildevent.getSignals();
						Signal bidSignal = null;
						for(Signal sig: signals) {
							String name = sig.getName();
							if(name.equalsIgnoreCase("bid")) {
								bidSignal = sig;
								break;
							}
						}
						signals.remove(bidSignal);
						aggChildevent.setSignals(signals);	
						if(eventId != null) {
							oadrEventMap.put(eventId, aggChildevent);
						}
					}	
				}						
			}
		return oadrEventMap;
	}
	
	/**
	 * List the aggregator children signals
	 * @param clientList
	 * @param eventName 
	 * @return listOfSigEntry
	 */
	private HashMap<Participant, ArrayList<EventParticipantSignalEntry>> getAggChlidrenSignlaList(
			List<String> clientList, String eventName) {
		List<EventParticipant> findEventParticipantWithSignalsByAllClientUUID = eventParticipantEAO.findEventParticipantWithSignalsByAllClientUUIDAndEventUUID(clientList, eventName);
		HashMap<Participant, ArrayList<EventParticipantSignalEntry>> listOfSigEntry = new HashMap<Participant, ArrayList<EventParticipantSignalEntry>>();
		if(findEventParticipantWithSignalsByAllClientUUID!=null){
		for(EventParticipant ep: findEventParticipantWithSignalsByAllClientUUID){
			ArrayList<EventParticipantSignalEntry> sigEntriesforParticipant = new ArrayList<EventParticipantSignalEntry>();
			 for(EventParticipantSignalEntry sigEn:ep.getSignalEntries()){
				 sigEntriesforParticipant.add(sigEn);
			 }
			 listOfSigEntry.put(ep.getParticipant(), sigEntriesforParticipant);
			}
		}
		return listOfSigEntry;
	}
	private HashMap<Participant, ArrayList<EventParticipantSignalEntry>> getAggChlidrenSignlaList(List<EventParticipant> evPart) {
		HashMap<Participant, ArrayList<EventParticipantSignalEntry>> listOfSigEntry = new HashMap<Participant, ArrayList<EventParticipantSignalEntry>>();
		if(evPart!=null){
		for(EventParticipant ep: evPart){
			ArrayList<EventParticipantSignalEntry> sigEntriesforParticipant = new ArrayList<EventParticipantSignalEntry>();
			 for(EventParticipantSignalEntry sigEn:ep.getSignalEntries()){
				 sigEntriesforParticipant.add(sigEn);
			 }
			 listOfSigEntry.put(ep.getParticipant(), sigEntriesforParticipant);
			}
		}
		return listOfSigEntry;
	}
	
	/***
	 * group the resources based on the signals
	 * @param aggChlidrenSignlaList
	 * @param listOfSigEntry
	 * @return groupList
	 */
	private Map<String, ArrayList<String>> checkSignalsforAggregatorChildParticipats(HashMap<Participant, ArrayList<EventParticipantSignalEntry>> aggChlidrenSignlaList, 
			HashMap<Participant, ArrayList<EventParticipantSignalEntry>> listOfSigEntry){			
		Map<Date, String> aggSignals = new HashMap<Date, String>();
		//Add aggregator Signals
		for(Entry<Participant, ArrayList<EventParticipantSignalEntry>> aggEntrySet: aggChlidrenSignlaList.entrySet()){
			ArrayList<EventParticipantSignalEntry> aggEntryValue = aggEntrySet.getValue();			
			for(EventParticipantSignalEntry aggvalue: aggEntryValue){
				String levelValue = aggvalue.getLevelValue();
				if(levelValue.equalsIgnoreCase("on")){
					continue;
				}
				Date time = aggvalue.getTime();	
				aggSignals.put(time, levelValue);
			}
		}
		//Add aggregator childrens Signals
		Map<Participant, HashMap<Date,String>> aggAllchildSignals = new HashMap<Participant, HashMap<Date,String>>();
		for(Entry<Participant, ArrayList<EventParticipantSignalEntry>> entrySet: listOfSigEntry.entrySet()){
			Participant key = entrySet.getKey();
			ArrayList<EventParticipantSignalEntry> value = entrySet.getValue();
			HashMap<Date, String> aggchildSignals = new HashMap<Date, String>();
			for(EventParticipantSignalEntry cSignalValue: value){
				String levelValue = cSignalValue.getLevelValue();
				if(levelValue.equalsIgnoreCase("on")){
					continue;
				}
				Date time = cSignalValue.getTime();
				aggchildSignals.put(time, levelValue);
			}	
			aggAllchildSignals.put(key, aggchildSignals);
		}
		
		// Group them Based on Signals
		ArrayList<String> participantHaveSameSignalsAsAgg= new ArrayList<String>();
		HashMap<Participant, HashMap<Date, String>> childChilds = new HashMap<Participant, HashMap<Date,String>>();	
		Map<String, ArrayList<String>>  childGroupList = new HashMap<String, ArrayList<String>>();
		int i=1;		
		for(Entry<Participant, HashMap<Date, String>> entrySet: aggAllchildSignals.entrySet()) {
			Participant key = entrySet.getKey();
			HashMap<Date, String> value = entrySet.getValue();
			if(aggSignals.equals(value)) {
				participantHaveSameSignalsAsAgg.add(key.getParticipantName());
			} else {				
				boolean isexist = false;
				if(!childChilds.isEmpty()) {
					for(Entry<Participant, HashMap<Date, String>> entrySet2: childChilds.entrySet()) {
						 Participant key2 = entrySet2.getKey();
						HashMap<Date, String> value2 = entrySet2.getValue();
						if(value.equals(value2)){	
							for(Entry<String, ArrayList<String>> childEntryList : childGroupList.entrySet()) {
								if(childEntryList.getValue().contains(key2.getParticipantName())) {
									isexist= true;
								}
							}
							if(isexist) {
								for(Entry<String, ArrayList<String>> entrySet3 :childGroupList.entrySet()){
									if(entrySet3.getValue().contains(key2.getParticipantName())) {
										ArrayList<String> arrayList = childGroupList.get(entrySet3.getKey());
										arrayList.add(key.getParticipantName());
										childGroupList.put(entrySet3.getKey(), arrayList);													
									}
								}
							}
						}	
					}
				} 
				if(!isexist) {
					childChilds.put(key, value);
					ArrayList<String> participant = new ArrayList<String>();
					participant.add(key.getParticipantName());
					childGroupList.put("group"+i++, participant);
				}
				
			}
		}		
		childGroupList.put("AggGroup", participantHaveSameSignalsAsAgg);		
		return childGroupList;
	}
	
	/**
	 * convert the 1.0 event to 2.0b oadr event event
	 * @param requestId
	 * @param venId
	 * @param clientId
	 * @return oadrEvent
	 */
	private HashMap<String, com.honeywell.dras.vtn.api.event.Event> createOadrEvent(
			String requestId,
			String venId,			
			String clientId,String eventId, boolean isPGE,String parentName, boolean mainParticipantCalling) {
		String eventStatus = EventStatus.NONE.toString();
		String operationModeValue = OperationModeValue.UNKNOWN.toString();
		HashMap<String, com.honeywell.dras.vtn.api.event.Event> oadrEventIdAndOadrEventMap = new HashMap<String, com.honeywell.dras.vtn.api.event.Event>();
		List<EventState> evtStates =clientManager.getAllDrasEventStateForClient(clientId);	
		List<String> eventNames = new ArrayList<String>();
		Map<String, com.akuacom.pss2.event.Event> eventIdEventObjectMap = new HashMap<String, com.akuacom.pss2.event.Event>();
		if(evtStates.size()>0){
			for (EventState eventState : evtStates) {
					eventNames.add(eventState.getEventIdentifier());
			}
			if(eventNames.size()>0){
				eventIdEventObjectMap =eventManager.findEventIdEventObjectMap(eventNames);
			}
		}
		for (EventState eventState : evtStates) {
			if(oadrEventIdAndOadrEventMap.containsKey(eventState.getEventIdentifier())){
				updateOadrEventResourceListWithExtraResource(eventState.getEventIdentifier(), clientId, oadrEventIdAndOadrEventMap.get(eventState.getEventIdentifier()).getTarget().getResourceIdList(), isPGE,parentName);
				 //clientManager.updateParticipantCommunications(clientId, new Date(), true, eventStatus, operationModeValue);
				continue;
			}				
			eventStatus = eventState.getSimpleDRModeData().getEventStatus();
			operationModeValue = eventState.getSimpleDRModeData().getOperationModeValue();
		    if(eventState.getDrEventData()!=null 
		    		&& eventState.getDrEventData().getStartTime()!=null 
		    		&& eventState.getSimpleDRModeData()!=null 
		    		|| //at least one mode signal 
		    		eventState.getSimpleDRModeData().getOperationModeSchedule()!=null 
		    		&& eventState.getSimpleDRModeData().getOperationModeSchedule().getModeSlot()!=null
		    		&& !eventState.getSimpleDRModeData().getOperationModeSchedule().getModeSlot().isEmpty()){
		    		
		    	ProgramPerf program =programEJB.findByName(eventState.getProgramName()).get(0);
		    	com.honeywell.dras.vtn.api.event.Event oadrEvent = EventConverter.convertEventState2Oadr2Event(eventState,program,venId);          
		    	updateOadrEventResourceListWithExtraResource(oadrEvent.getEventID(), clientId, oadrEvent.getTarget().getResourceIdList(), isPGE,parentName);
		    	fillLocationInfoAndCreatedTime(oadrEvent , eventIdEventObjectMap.get(eventState.getEventIdentifier()));
		    	if (!optService.canCreateEvent(clientId, 
		    			eventState.getProgramName(), oadrEvent.getStartTime(), oadrEvent.getDuration())) {
		    		log.info("Resource exclude because of opt: " + clientId);
		    		continue;
		    	}	
		    	if(eventId!=null){
		    		if(oadrEvent.getEventID().equalsIgnoreCase(eventId)){
		    			oadrEventIdAndOadrEventMap.put(oadrEvent.getEventID(), oadrEvent);
		    		}
		    	}else{
		    		oadrEventIdAndOadrEventMap.put(oadrEvent.getEventID(), oadrEvent);
		    	}
		    }
		    //update comms status
		    //clientManager.updateParticipantCommunications(clientId, new Date(), true, eventStatus, operationModeValue);
		    createClientConversationState(eventState);
		    EventRequest er = new EventRequest();
			er.setEventId(eventState.getEventIdentifier());
			er.setEventModificationNumber(eventState.getEventModNumber());
			er.setRequestId(requestId);
			er.setClientId(clientId);
			er.setEventStatus(eventStatus);
			er.setClientOperationModeValue(operationModeValue);
			er.setVenId(venId);
			eventRequestManager.createEventRequest(er);
		}
		if(evtStates != null && !evtStates.isEmpty()){
			if(mainParticipantCalling){
				clientManager.updateParticipantCommunications(clientId, new Date(), true, eventStatus, operationModeValue);
			}
		}else{
        	String pendingSignalValue = "";
			String modeSignalValue = "";
			Participant client = clientEAO.getClient(clientId);
			if (client.isManualControl()) {
				pendingSignalValue = clientManager.getClientManualSignalValueAsString(client, "pending");
				modeSignalValue = clientManager.getClientManualSignalValueAsString(client, "mode");
			} else {
				pendingSignalValue = "none";
				modeSignalValue = "normal";
			}

            if (pendingSignalValue.equals("active")) {
            	eventStatus =EventStatus.ACTIVE.toString();
            } else if (pendingSignalValue.equals("near")) {
            	eventStatus=EventStatus.NEAR.toString();
            } else if (pendingSignalValue.equals("far")) {
            	eventStatus=EventStatus.FAR.toString();
            } else {
            	eventStatus=EventStatus.NONE.toString();
            }
            operationModeValue = modeSignalValue.toUpperCase();
        	if(mainParticipantCalling){
        		clientManager.updateParticipantCommunications(clientId, new Date(), true, eventStatus, operationModeValue);
        	}
        
		}
		return oadrEventIdAndOadrEventMap;
	}
	
	
	private List<Participant> getAggregatorParticipantClients(HashMap<String, Participant> aggregatorChilds, String eventName, String clientId) {			
		List<EventParticipant> eventParticipantsForEvent = eventManager.getEventParticipantsForEvent(eventName);
		List<Participant> eventclinets = new ArrayList<Participant>();
		
		for(Entry<String, Participant> aggChild :aggregatorChilds.entrySet()){
			Participant aggChildParty = aggChild.getValue();
			for(EventParticipant ep: eventParticipantsForEvent) {
				if(ep.getParticipant().isClient() && ep.getParticipant().getParent().equalsIgnoreCase(aggChildParty.getParticipantName())) {
					eventclinets.add(ep.getParticipant());
				}				
			}
			
		}
       	return 	eventclinets;       			
   	}       		
   		
	/**
	 * optout the aggregator childrens if aggregator opted out from the event
	 * @param aggregatorChilds
	 * @param erl
	 */
	private void optoutAggregatorChilds(HashMap<String, Participant> aggregatorChilds, EndpointMapping erl) {
		if(aggregatorChilds != null && !aggregatorChilds.isEmpty()) {
			 List<EventInfo> eventsForParticipant = particiapntManager.getEventsForParticipant(erl.getParticipantName(), true);
			 for(EventInfo info: eventsForParticipant){
				 List<EventParticipant> eventParticipantsForEvent = eventManager.getEventParticipantsForEvent(info.getEventName());
				 for(EventParticipant ep: eventParticipantsForEvent) {
					 if(!ep.getParticipant().isClient()){
						 if(aggregatorChilds.containsKey(ep.getParticipant().getAccountNumber())) {
							eventManager.removeParticipantFromEvent(info.getEventName(), ep.getParticipant().getParticipantName());									
						 }
					 }
				 }	
			 }					
		}
	}
	
	/**
	 * List the aggregator children and return 
	 * else if participants doesn't have descendents return null
	 * 
	 */
	private HashMap<String, Participant> getAggregatorChilds(Participant participant,String programName) {
			String parent2 = participant.getParent();	
			if(parent2 != null) {
			
				Participant aggregator = particiapntManager.getParticipantAndProgramParticipantsOnly(parent2,false);
				if(aggregator != null) {
					HashMap<String, Participant> aParticipants = new HashMap<String, Participant>();
					Set<ProgramParticipant> programParticipants = aggregator.getProgramParticipants();						
					for(ProgramParticipant p: programParticipants){	
					 if(p.getProgramName()!=null && p.getProgramName().equalsIgnoreCase(programName)){
					 Set<ProgramParticipant> descendants = ppManager.getDescendantsForSpecifiedProgram(p, programName);
					 for(ProgramParticipant pp: descendants) {
						 Participant party = pp.getParticipant();
						 aParticipants.put(party.getAccountNumber(), party);
					  }	
					}
				}
					return aParticipants;
				}
			}
		return null;
	}


	@Override
	public Response createdEvent(CreatedEvent createdEvent)
			throws VtnDrasServiceException {
		log.info("createdEvent called");		
		
		if(!this.eventRequestValidator.isCreatedEventValid(createdEvent)){
			Response errorResponse = eventRequestValidator.getErrorResponse();
			errorResponse.setRequestId(Constants.REQUEST_ID_PREFIX + MemorySequence.getNextSequenceId());
			return errorResponse;
		}
		
		
		Response response = createResponseObject();
		String venId = createdEvent.getVenId();
		for (com.honeywell.dras.vtn.api.event.EventResponse er : createdEvent.getEventResponseList()) {
			List<EventRequest> eventRequests = getEventRequest(er.getRequestID() , venId, er.getEventID());
			if(0 == eventRequests.size()){
				log.error("No request ID found for ven id");
				response.setResponseCode("414");
				response.setResponseDescription("No request ID found for ven id");
				response.setRequestId(er.getRequestID());
				response.setSchemaVersion(createdEvent.getSchemaVersion());				
				return response;
			}
			for(EventRequest eventRequest : eventRequests) {
				String clientId = eventRequest.getClientId();
				String eventId = eventRequest.getEventId();
				List<String> events=clientManager.getClientEventNames(clientId);
				if(events==null || !events.contains(eventId)){
					log.error("No event"+ eventId +" found for ven id");
					response.setResponseCode("400");
					response.setResponseDescription("No event "+eventId+"  found for ven id "+venId);
					response.setRequestId(er.getRequestID());
					return response;
				}
				if(eventRequest.getEventModificationNumber() != er.getModificationNumber()){
					response.setResponseCode("400");
					response.setResponseDescription("event modification number does not match for event id "+eventId);
					response.setRequestId(er.getRequestID());
					response.setSchemaVersion(createdEvent.getSchemaVersion());
					return response;
				}
				clientManager.updateParticipantCommunications(clientId, new Date(), true, eventRequest.getEventStatus(), eventRequest.getClientOperationModeValue());
			    if(er.getOptType()==OptType.OPT_OUT){
			    	response = handleOptOut(clientId, eventId);
			    }
			}
			
		}
		String requestId = createdEvent.getRequestId();
		if(null == requestId || requestId.isEmpty()){
			requestId = Constants.REQUEST_ID_PREFIX + MemorySequence.getNextSequenceId();
		}
		response.setRequestId(requestId);
		response.setCertCommonName(createdEvent.getCertCommonName());
		response.setFingerprint(createdEvent.getFingerprint());
		response.setSchemaVersion(createdEvent.getSchemaVersion());
		response.setVenId(venId);
		
		return response;
	}
	
	
	private Response handleOptOut(String clientId,String eventId){
		Response response = createResponseObject();
		SystemManager systemManager = EJBFactory.getBean(SystemManager.class);
		PSS2Features features = systemManager.getPss2Features();
		if (!features.isClientOptOut()) {
			response.setResponseCode("400");
			response.setResponseDescription("Event opt out by client is not allowed");
			return response;
		}
		
		Participant client = clientManager.getClientOnly(clientId);
		if (!client.getClientAllowedToOptOut()) {
			response.setResponseCode("400");
			response.setResponseDescription("Event opt out by client is not allowed");
			return response;
		}
		eventManager.removeParticipantFromEvent(eventId, client.getParent());
		return response;
	}
	private void createClientConversationState(EventState eventState){
		 ClientConversationState conversadr1 = new ClientConversationState();
         conversadr1.setDrasClientId(eventState.getDrasClientID());
         conversadr1.setEventIdentifier(eventState.getEventIdentifier());
         conversadr1.setEventModNumber((int)eventState.getEventModNumber());
         conversadr1.setConversationStateId((int) eventState.getEventStateID());
         conversadr1.setProgramName(eventState.getProgramName());
         conversadr1.setCommTime(new Date());
         String eventStatus = eventState.getSimpleDRModeData().getEventStatus();
         conversadr1.setEventStatus(Enum.valueOf(EventStatus.class,  eventStatus));
         String operationModeValue = eventState.getSimpleDRModeData().getOperationModeValue();
         conversadr1.setOperationModeValue(Enum.valueOf(OperationModeValue.class, operationModeValue));
         clientManager.putClientConversationState(conversadr1);
		
	}
	private List<EventRequest> getEventRequest(String requestId , String venId, String eventId){
		List<EventRequest> eventRequests = new ArrayList<EventRequest>();
		if(null == eventId || eventId.isEmpty()){
			eventRequests  = eventRequestManager.getEventRequestByRequestIdAndVenId(requestId, venId);
		}
		else{
			eventRequests  = eventRequestManager.getEventRequestByRequestIdAndVenIdAndEventId(requestId, venId, eventId);
		}
		return eventRequests;
	}
	private DistributeEvent createDistributeEventObj(){
		DistributeEvent distEvent = new DistributeEvent();
		distEvent.setResponse(createResponseObject());
		return distEvent;
	}
	private Response createResponseObject(){
		Response response = new Response();
		response.setResponseCode("200");
		response.setResponseDescription("Success");
		return response;
	}
	private void sortEvent(List<Event> oadrEvents){
		
		Comparator<Event> comparator = new Comparator<Event>(){

			@Override
			public int compare(Event o1, Event o2) {
				// TODO Auto-generated method stub
				return o1.getStartTime().compareTo(o2.getStartTime()) ;
			}
		};
		Collections.sort(oadrEvents,comparator);
	}
	private void fillLocationInfoAndCreatedTime(Event oadrEvent , com.akuacom.pss2.event.Event event){
		Date createdTime = new Date (System.currentTimeMillis());
		List<String> locations = new ArrayList<String>();
			createdTime = event.getCreationTime();
			locations = event.getLocations();
    		oadrEvent.setCreatedTime(createdTime);
		if(null ==locations){
			return;
		}
		List<String> nodes = new ArrayList<String>();
		for(String location : locations){
			if(nodes.contains(location)){
				continue;
			}
			nodes.add(location);
		}
		ServiceDeliveryPoint serviceDeliveryPoint = new ServiceDeliveryPoint();
		serviceDeliveryPoint.setNodeList(nodes);
		EiTarget target = oadrEvent.getTarget();
		if(null == target){
			oadrEvent.setTarget(new EiTarget());
		}
		List<ServiceDeliveryPoint> serviceDeliveryPointList = target.getServiceDeliveryPointList();
		if(null == serviceDeliveryPointList){
			serviceDeliveryPointList = new ArrayList<ServiceDeliveryPoint>();
			target.setServiceDeliveryPointList(serviceDeliveryPointList);
		}
		serviceDeliveryPointList.add(serviceDeliveryPoint);
	}
	
	private void updateOadrEventResourceListWithExtraResource(String eventId, String clientId,List<String> resourceList , boolean isPGE,String parent){
		String uuid = "";
		Set<String> resources = new HashSet<String>();
		if(!isPGE){
				if(null != parent){
				ProgramAggregator progAgg = programAggregatorEAO.findAccountNumberOfAggregator(parent, eventId);
				if (null != progAgg) {
					String accountNumber = progAgg.getAggregatorAccountNumber();
					if (accountNumber != null) {
						resources.add(accountNumber);
					}
				}
			
				}
			}else{
		     resources = apxAggEaoManager.getAggregatorResources(eventId,clientId);
		}
		//if(resources.isEmpty()){
			resourceList.add(clientId);
			//return;
		//}
		resourceList.addAll(resources);
	}
	/***
	 * this method added to check the instance of DRAS for DBP Enhancement requirement.
	 * DRMS-8593 parent
	 * @return isPGEInstnace 
	 */
	private boolean getInstance() {		
			if(!isPGEInstnace) {
				EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
				String coreProps =  cache.getUtilityName("utilityName");
				if(coreProps != null && coreProps.equalsIgnoreCase("PGE")){
					isPGEInstnace = true;
				}
			}
		return isPGEInstnace;
	}
}
