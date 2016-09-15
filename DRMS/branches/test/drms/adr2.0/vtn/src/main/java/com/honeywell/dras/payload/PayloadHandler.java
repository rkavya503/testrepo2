package com.honeywell.dras.payload;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.log4j.Logger;
import org.openadr.dras.eventstate.EventState;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.client.ClientEAO;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.client.ClientManagerBean;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.ClientConversationState;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantEAO;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.ProgramPerf;
import com.akuacom.pss2.program.ProgramPerfGenEAO;
import com.akuacom.pss2.program.aggregator.eao.ProgramAggregatorEAO;
import com.akuacom.pss2.program.aggregator.eao.ProgramAggregatorEAOManager;
import com.akuacom.pss2.query.ParticipantClientListFor20Ob;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.MemorySequence;
import com.akuacom.pss2.util.OperationModeValue;
import com.honeywell.dras.adapter.Converter;
import com.honeywell.dras.openadr2.eipayloads.OadrCreatedEvent;
import com.honeywell.dras.openadr2.eipayloads.OadrDistributeEvent;
import com.honeywell.dras.openadr2.eipayloads.OadrRequestEvent;
import com.honeywell.dras.openadr2.eipayloads.OadrResponse;
import com.honeywell.dras.vtn.api.CommsStatusType;
import com.honeywell.dras.vtn.api.CreatedEvent;
import com.honeywell.dras.vtn.api.EventResponse;
import com.honeywell.dras.vtn.api.Oadr2Event;
import com.honeywell.dras.vtn.api.Oadr2Signal;
import com.honeywell.dras.vtn.api.OptType;
import com.honeywell.dras.vtn.api.Request;
import com.honeywell.dras.vtn.api.Response;
import com.honeywell.dras.vtn.api.Telemetry;
import com.honeywell.dras.vtn.api.TelemetryData;
import com.honeywell.dras.vtn.api.event.DistributeEventImpl;
import com.honeywell.dras.vtn.api.event.Oadr2EventImpl;
import com.honeywell.dras.vtn.api.event.ResponseImpl;
import com.honeywell.dras.vtn.api.event.TargetImpl;

/**
 * Handles VTN payloads and generates proper responses
 * @author sunil
 */

public class PayloadHandler {

	private static String SEPERATOR =";"; 
	static Logger log = Logger.getLogger(PayloadHandler.class);
	  
	public static final String VTN_ID = "HONEYWELL.AKUACOM.VTN.7.X";
	public static final String REQUEST_ID_PREFIX = "AKUACOM.7.X.REQ:";
	
    /** The program manager. */
    private EventManager eventManager = EJBFactory
            .getBean(EventManager.class);

    private ClientManager clientManager = EJBFactory
            .getBean(ClientManager.class);
    
    private ClientEAO clientEAO = EJBFactory
            .getBean(ClientEAO.class);
    
    private ProgramPerfGenEAO programEJB =  EJBFactory
    		.getBean(ProgramPerfGenEAO.class);
    
    private ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class); 
    private ProgramAggregatorEAOManager prgAggregatorEAOManager = EJBFactory.getBean(ProgramAggregatorEAOManager.class); 
    private AggregatorSupportHandler handler = new AggregatorSupportHandler();       
   
    protected CorePropertyEAO coreProperty = EJBFactory.getBean(CorePropertyEAO.class);	
	private static boolean isPGEInstnace;
	
	protected ProgramAggregatorEAO  programAggregatorEAO = EJBFactory.getBean(ProgramAggregatorEAO.class);
	protected EventParticipantEAO  eventParticipantEAO = EJBFactory.getBean(EventParticipantEAO.class);
    
	public OadrResponse getOadrResponse(OadrCreatedEvent oadrCreatedEvent) {
		PojoGenerator pg = new PojoGenerator();
		CreatedEvent createdEvent = pg.getCreatedEvent(oadrCreatedEvent);
		PayloadGenerator pay = new PayloadGenerator();
		
		ResponseImpl resp = new ResponseImpl();
		resp.setResponseCode("200");
		resp.setResponseDescription("Success");
		if(createdEvent != null && createdEvent.getResponse() != null) {
			Response response = createdEvent.getResponse();
			String requestId2 = response.getRequestId();
			resp.setRequestId(requestId2);
		}
		
		// initial error check
		if (createdEvent.getVenId() == null || createdEvent.getVenId().isEmpty()) {
			log.error("venID is required");
			resp.setResponseCode("400");
			resp.setResponseDescription("venID is required");
			return pay.getOadrResponse(resp);
		}
		
		if (createdEvent.getEventResponseList() == null || createdEvent.getEventResponseList().isEmpty()) {
			log.error("Event Response List is required");
			resp.setResponseCode("400");
			resp.setResponseDescription("Event Response List is required");
			return pay.getOadrResponse(resp);
		}
		
		for (EventResponse er : createdEvent.getEventResponseList()) {
			if (er.getRequestID() == null || er.getRequestID().isEmpty()) {
				log.error("Request ID is required for each Event Response");
				resp.setResponseCode("400");
				resp.setResponseDescription("Request ID is required for each Event Response");
				return pay.getOadrResponse(resp);
			}
		}
		
		if(createdEvent != null && createdEvent.getEventResponseList() != null && createdEvent.getEventResponseList().size() > 0){
			EventResponse er = createdEvent.getEventResponseList().get(0);
			String desc = er.getDescription();
			if(desc != null && (desc.equals(CommsStatusType.ONLINE.value())  || desc.equals(CommsStatusType.OFFLINE.value()) )){
				return  handleCommsOnly(createdEvent);
			}
			String requestId = er.getRequestID();
			if(requestId != null && requestId.equals(Telemetry.TELEMETRY_TAG)){
				TelemetryData td = new TelemetryData();
				List<Telemetry> telemetryDataList = td.fromString(desc);
				return handleTelemetryOnly(createdEvent,telemetryDataList);
			}
		}
		
		//handle confirmation and comms
		OadrResponse oadrResp=handleEventConfirmation(createdEvent);
		
		return oadrResp;
	}

	
	protected OadrResponse handleCommsOnly(CreatedEvent createdEvent){
    	ResponseImpl resp = new ResponseImpl();
		resp.setResponseCode("200");
		resp.setResponseDescription("Success");
		if(createdEvent != null && createdEvent.getResponse() != null) {
			Response response = createdEvent.getResponse();
			String requestId = response.getRequestId();		
			if(requestId != null) {
				resp.setRequestId(requestId);
			}
		}
		PayloadGenerator pay = new PayloadGenerator();
		if (createdEvent.getEventResponseList().size() != 1) {
			log.error("Event Response List can only have 1 entry for offline");
			resp.setResponseCode("400");
			resp.setResponseDescription("Event Response List can only have 1 entry for offline");
			return  pay.getOadrResponse(resp);
		}
		
		String venId = createdEvent.getVenId();
		EventResponse er = createdEvent.getEventResponseList().get(0);
		CommsStatusType commStatus;
		try {
			commStatus = CommsStatusType.fromValue(er.getDescription());
		} catch (IllegalArgumentException iaex) {
			log.error("CommsStatusType not set in description");
			resp.setResponseCode("400");
			resp.setResponseDescription("CommsStatusType not set in description");
			return  pay.getOadrResponse(resp);
		}
		
		if(clientManager.getClientOnly(venId)==null){
			log.error("venID is not registered");
			resp.setResponseCode("400");
			resp.setResponseDescription("venID is not registered");
			return  pay.getOadrResponse(resp);
		}
		
		boolean online = CommsStatusType.ONLINE.equals(commStatus);
		clientManager.updateParticipantCommunications(venId,new Date(),online,null);
		return  pay.getOadrResponse(resp);
    }
    
    protected OadrResponse handleTelemetryOnly(CreatedEvent createdEvent,List<Telemetry> telemetryDataList){
    	ResponseImpl resp = new ResponseImpl();
		resp.setResponseCode("200");
		resp.setResponseDescription("Success");
		if(createdEvent != null && createdEvent.getResponse() != null) {
			String requestId = createdEvent.getResponse().getRequestId();
			if(requestId != null) {
				resp.setRequestId(requestId);
			}
		}
		PayloadGenerator pay = new PayloadGenerator();
		
		//TODO handle telemetry- such as meter data
		log.error("Telemetry currently is not supported yet");
		resp.setResponseCode("400");
		resp.setResponseDescription("Telemetry currently is not supported yet");
		return  pay.getOadrResponse(resp);
    }
    
    protected OadrResponse handleEventConfirmation(CreatedEvent createdEvent){
    	ResponseImpl resp = new ResponseImpl();
		resp.setResponseCode("200");
		resp.setResponseDescription("Success");
		PayloadGenerator pay = new PayloadGenerator();
		Response response = createdEvent.getResponse();
		String requestId = response.getRequestId();
		if(requestId != null) {
			resp.setRequestId(requestId);
		}
		String venId = createdEvent.getVenId();
		for (EventResponse er : createdEvent.getEventResponseList()) {
			String reqId = er.getRequestID();
			String eventId = er.getEventID();
			
			int reqsequence =Integer.parseInt(reqId.substring(REQUEST_ID_PREFIX.length()));
			ClientConversationState conversadr2=clientManager.getClientConversationState(reqsequence);
			int index =conversadr2!=null?indexOf(eventId,conversadr2.getCompactEventIds().split(SEPERATOR)):-1;
			
			if(conversadr2==null){
				log.error("No request ID found for ven id");
				resp.setResponseCode("400");
				resp.setResponseDescription("No request ID found for ven id " +venId);
				return pay.getOadrResponse(resp);
			}
			
			if(index==-1){
				log.error("No Event ID found for ven id");
				resp.setResponseCode("400");
				resp.setResponseDescription("No Event "+eventId+" found for ven id " +venId);
				return pay.getOadrResponse(resp);
			}
			
			List<String> events=clientManager.getClientEventNames(venId);
			if(events==null || !events.contains(eventId)){
				log.error("No event"+ eventId +" found for ven id");
				resp.setResponseCode("400");
				resp.setResponseDescription("No event "+eventId+"  found for ven id "+venId);
				return pay.getOadrResponse(resp);
			}
		       
			//if(conversadr2!=null 
			String eventState = conversadr2.getCompactEventStatus().split(SEPERATOR)[index];
			String operationModeValue = conversadr2.getCompactOperationModes().split(SEPERATOR)[index];
			clientManager.updateParticipantCommunications(venId,
		               new Date(), true, eventState, operationModeValue);
				
		    // if client opt out the event 
		    if(er.getOptType()==OptType.OPT_OUT){
			    SystemManager systemManager = EJBFactory.getBean(SystemManager.class);
				PSS2Features features = systemManager.getPss2Features();
				if (features.isClientOptOut()) {
		             Participant client = clientManager.getClientOnly(venId);
		             if (client.getClientAllowedToOptOut()) {
		            	 log.debug("Client "+venId +" optOut Event "+eventId);
		            	 eventManager.removeParticipantFromEvent(
			            		eventId, client.getParent());
		             } else {
		            	resp.setResponseCode("400");
		 				resp.setResponseDescription("Event opt out by client is not allowed");
		             }
				}else{
					resp.setResponseCode("400");
	 				resp.setResponseDescription("Event opt out by client is not allowed");
				}
		    }
		}
		return pay.getOadrResponse(resp);
    }
    
    private int indexOf(String one,String[] strs){
    	if(strs==null) return -1;
    	for(int i=0;i<strs.length;i++){
    		if(strs[i].equals(one)) return i;
    	}
    	return -1;
    }

    public OadrDistributeEvent getOadrDistributeEvent(OadrRequestEvent oadrRequestEvent)
			throws DatatypeConfigurationException {
		
		OadrDistributeEvent oadrDistributeEvent = null;
		PojoGenerator pg = new PojoGenerator();
		PayloadGenerator pay = new PayloadGenerator();
		
		Request request = pg.getRequest(oadrRequestEvent);
		//TODO verify the client id is the one who is talking to the server 
		String clientId = request.getVenID();
		
		//response 
		ResponseImpl resp = new ResponseImpl();
		resp.setResponseCode("200");
		resp.setResponseDescription("Success");
		resp.setRequestId(request.getRequestId());
		
		DistributeEventImpl distributeEvent = new DistributeEventImpl();
		distributeEvent.setResponse(resp);
		List<Oadr2Event> oadr2EvtList = new ArrayList<Oadr2Event>();
		distributeEvent.setEventList(oadr2EvtList);
		
		// initial error check
		if (request.getRequestId() == null || request.getRequestId().isEmpty()) {
			log.error("requestID is required");
			resp.setResponseCode("400");
			resp.setResponseDescription("requestID is required");
			oadrDistributeEvent =  pay.getOadrDistributeEvent(distributeEvent);
			return oadrDistributeEvent;
		}
		
		if (request.getVenID() == null || request.getVenID().isEmpty()) {
			log.error("venID is required");
			resp.setResponseCode("400");
			resp.setResponseDescription("venID is required");
			oadrDistributeEvent =  pay.getOadrDistributeEvent(distributeEvent);
			return oadrDistributeEvent;
		}	
		
		int sequence =   MemorySequence.getNextSequenceId();
		String newRequestId = REQUEST_ID_PREFIX +sequence;
		distributeEvent.setRequestID(newRequestId);
		distributeEvent.setVtnID(VTN_ID);
		String venId= clientId;
		String parentName = participantManager.getParticipantByClient(clientId).getParent();
		boolean isPGE = getInstance();
		HashMap<String, Oadr2EventImpl> createOADR2AEventPayload = createOADR2AEventPayload(clientId,sequence, venId,null,parentName,isPGE,true);	
		Set<String> eventIds = new  HashSet<String>();
		eventIds.addAll(createOADR2AEventPayload.keySet());
		eventIds.addAll(programAggregatorEAO.findEventIdsByClientName(parentName));
		HashMap<String,com.honeywell.dras.vtn.api.event.Oadr2EventImpl> oadrEventIdAndOadrEventMap = new HashMap<String,com.honeywell.dras.vtn.api.event.Oadr2EventImpl>();
		Map<String,Event> eventIdEventObjectMap = new HashMap<String, Event>();
		List<String> eventNames = new ArrayList<String>();
		eventNames.addAll(eventIds);
        Participant aggregator = handler.getAggregator(clientId, true);
        Set<String> accounts =new TreeSet<String>();
        Map<String,List<Participant>> eventIdAndAggregatorChildMap = new HashMap <String,List<Participant>>();
        List<Participant> partList = new ArrayList<Participant>();
        Map<String,List<EventParticipant>> mapOfEventAndEventParticipantForClients = new HashMap<String,List<EventParticipant>>();
        List<String> parentList = new ArrayList<String>();
	    parentList.add(request.getVenID());
        Map<String,List<EventParticipant>>mapOfEventAndEventParticipantForParent = new HashMap<String, List<EventParticipant>>();
        Map<String,ParticipantClientListFor20Ob>  clientParentMap = new HashMap<String,ParticipantClientListFor20Ob>();
        if(eventIds.size()>0){
        	eventIdEventObjectMap =eventManager.findEventIdEventObjectMap(eventNames);
        	mapOfEventAndEventParticipantForParent = eventParticipantEAO.findEventParticipantWithSignalsByAllEventNameAndClientUUIDForEvent(parentList,eventIds);
        	accounts =new TreeSet<String>(prgAggregatorEAOManager.getAggregatorResources(parentName, eventIds));
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
        	clientParentMap.putAll((participantManager.getParticipantCLientListFor20Ob(new ArrayList<String>(parentList))));
        	clientParentMap.putAll((participantManager.getParticipantCLientListFor20Ob(new ArrayList<String>(clientListNew))));
        }
		for(String eventId: eventIds) {
			List<String> resourceList = new ArrayList<String>();
					HashMap<String, Participant> aggregatorChilds = new HashMap<String, Participant>();
					List<Participant> aggregatorClients = new ArrayList<Participant>();
					aggregatorClients = eventIdAndAggregatorChildMap.get(eventId);	
					List<String> aggregaotorID = new ArrayList<String>();
					Event event = eventIdEventObjectMap.get(eventId);
					if(aggregator != null && event != null && aggregatorClients != null && !aggregatorClients.isEmpty()) {
						HashMap<String, Oadr2EventImpl> aggregateSignals = new HashMap<String, Oadr2EventImpl>();
						aggregaotorID.add(aggregator.getUUID());						
						HashMap<Participant, ArrayList<EventParticipantSignalEntry>> aggChlidrenSignlaList = handler.getAggChlidrenSignlaList(mapOfEventAndEventParticipantForParent.get(eventId));
						List<String> clientList = new ArrayList<String>();				
						for(Participant par: aggregatorClients) {
							clientList.add(par.getUUID());
						}
						HashMap<Participant, ArrayList<EventParticipantSignalEntry>> listOfSigEntry = handler.getAggChlidrenSignlaList(mapOfEventAndEventParticipantForClients.get(eventId));
						Map<String, ArrayList<String>> groupList = handler.checkSignalsforAggregatorChildParticipats(aggChlidrenSignlaList, listOfSigEntry);	
						if(!createOADR2AEventPayload.isEmpty()){
							 Oadr2Event oadr2Event = createOADR2AEventPayload.get(eventId);
							 Oadr2EventImpl oadrEvent= (Oadr2EventImpl) oadr2Event;
						     aggregateSignals = aggregateSignals(newRequestId, request.getVenID(), oadrEvent, groupList, sequence,eventId,isPGE,clientParentMap);	
						}else{
							 aggregateSignals = aggregateSignals(newRequestId, request.getVenID(), null, groupList, sequence,eventId,isPGE,clientParentMap);	
						}
						
						for(Entry<String, Oadr2EventImpl> groupSetEvent : aggregateSignals.entrySet()){
							oadrEventIdAndOadrEventMap.put(groupSetEvent.getKey(), groupSetEvent.getValue());
						}
					}
					else if(!createOADR2AEventPayload.isEmpty()){
						oadrEventIdAndOadrEventMap.putAll(createOADR2AEventPayload);
					}
		}		
		
		if(!oadrEventIdAndOadrEventMap.isEmpty()) {
			oadr2EvtList.clear();
			for(Entry<String, Oadr2EventImpl> eventEntrySet : oadrEventIdAndOadrEventMap.entrySet()){
				oadr2EvtList.add(eventEntrySet.getValue());
			}
		}else if(!createOADR2AEventPayload.isEmpty()){
			for(Entry<String, Oadr2EventImpl> entrySet :createOADR2AEventPayload.entrySet()){
				oadr2EvtList.add(entrySet.getValue());
			}
		}
		oadrDistributeEvent = pay.getOadrDistributeEvent(distributeEvent);
		return oadrDistributeEvent;
	}


	private HashMap<String, com.honeywell.dras.vtn.api.event.Oadr2EventImpl> createOADR2AEventPayload(String clientId, int sequence, String venId,String eventName,String parentName,boolean isPGE,boolean mainParticipantCalling) {
		
		HashMap<String, com.honeywell.dras.vtn.api.event.Oadr2EventImpl> oadrEventIdAndOadrEventMap = new HashMap<String, com.honeywell.dras.vtn.api.event.Oadr2EventImpl>();
		List<EventState> evtStates =clientManager.getClientDrasEventStates(clientId);		
		
		//for openadr2.0 response
	    ClientConversationState conversadr2 = new ClientConversationState();
        conversadr2.setDrasClientId(clientId);
        conversadr2.setCommTime(new Date());
        conversadr2.setConversationStateId(sequence);
        StringBuilder eventIds= null;
        StringBuilder eventStatuses= null;
        StringBuilder modes= null;
        List<String> eventNames = new ArrayList<String>();
        Map<String,Event> eventIdEventObjectMap = new HashMap<String, Event>();
        String eventStatus = EventStatus.NONE.toString();
		String operationModeValue = OperationModeValue.UNKNOWN.toString();
		if(evtStates != null && !evtStates.isEmpty()){
			for(org.openadr.dras.eventstate.EventState evState:evtStates){
				eventNames.add(evState.getEventIdentifier());
			}
			if(eventNames.size()>0){
				eventIdEventObjectMap =eventManager.findEventIdEventObjectMap(eventNames);
			}
        	for(org.openadr.dras.eventstate.EventState eventState: evtStates){
        		if(eventIdEventObjectMap.get(eventState.getEventIdentifier())!=null){
        		//for dras 7 event state cache
                ClientConversationState conversadr1 = new ClientConversationState();
                conversadr1.setDrasClientId(eventState.getDrasClientID());
                conversadr1.setEventIdentifier(eventState.getEventIdentifier());
                conversadr1.setEventModNumber((int)eventState.getEventModNumber());
                conversadr1.setConversationStateId((int) eventState.getEventStateID());
                conversadr1.setProgramName(eventState.getProgramName());
                conversadr1.setCommTime(new Date());
                eventStatus = eventState.getSimpleDRModeData().getEventStatus();
                conversadr1.setEventStatus(Enum.valueOf(EventStatus.class,  eventStatus));
                operationModeValue = eventState.getSimpleDRModeData().getOperationModeValue();
                conversadr1.setOperationModeValue(Enum.valueOf(OperationModeValue.class, operationModeValue));
                clientManager.putClientConversationState(conversadr1);
                
                if(eventIds==null) {
                	eventIds = new StringBuilder();
                	eventIds.append(eventState.getEventIdentifier());
                } else {
                	eventIds.append(SEPERATOR+eventState.getEventIdentifier());
                }
        		
                if(eventStatuses==null) {
                	eventStatuses= new StringBuilder();
                	eventStatuses.append(eventStatus);
                } else {
                	eventStatuses.append(SEPERATOR+eventStatus);
                }
        		
        		if(modes==null) {
        			modes= new StringBuilder();
        			modes.append(operationModeValue);
        		} else {
        			modes.append(SEPERATOR+operationModeValue);
        		}
                
                //at least one event signal 
                if(		eventState.getDrEventData()!=null 
                		&& eventState.getDrEventData().getStartTime()!=null 
                		&& eventState.getSimpleDRModeData()!=null 
                		|| //at least one mode signal 
                		eventState.getSimpleDRModeData().getOperationModeSchedule()!=null 
                		&& eventState.getSimpleDRModeData().getOperationModeSchedule().getModeSlot()!=null
                		&& !eventState.getSimpleDRModeData().getOperationModeSchedule().getModeSlot().isEmpty()){
                		
                	ProgramPerf program =programEJB.findByName(eventState.getProgramName()).get(0);
                	Oadr2Event oadr2AEvent = Converter.convertEventState2Oadr2Event(eventState,program);            
                	Oadr2EventImpl oadrEvent = (Oadr2EventImpl) oadr2AEvent;
                	List<String> venIdList = oadr2AEvent.getTarget().getVenIdList();
                	if(venIdList != null && !venIdList.isEmpty() && venId != null){
                		venIdList.clear();
                		venIdList.add(venId);
                	}
                	
                	if(eventName!=null){
    		    		if(oadr2AEvent.getEventID().equalsIgnoreCase(eventName)){
    		    			oadrEventIdAndOadrEventMap.put(oadr2AEvent.getEventID(), oadrEvent);
    		    		}
    		    	}else{
    		    		oadrEventIdAndOadrEventMap.put(oadr2AEvent.getEventID(), oadrEvent);
    		    	}
                	
                	handler.updateOadrEventResourceListWithExtraResource(oadr2AEvent.getEventID(), clientId,  oadr2AEvent.getTarget().getResourceIdList(),parentName,isPGE);               	
                	handler.fillLocationInfoAndCreatedTime((Oadr2EventImpl) oadr2AEvent ,eventIdEventObjectMap.get(eventState.getEventIdentifier()));
                }
                //update comms status
             }
        	}
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
		if(eventIds!=null){
			conversadr2.setCompactEventIds(eventIds.toString());
			conversadr2.setCompactEventStatus(eventStatuses.toString());
			conversadr2.setCompactOperationModes(modes.toString());
			
			clientManager.putClientConversationState(conversadr2);
		}
		return oadrEventIdAndOadrEventMap;
	}
	
	/**
	 * group the event messages which is having same signal 
	 * @param requestId
	 * @param venId
	 * @param aggEvent
	 * @param groupList
	 * @return
	 */
	private HashMap<String, Oadr2EventImpl> aggregateSignals(String requestId, String venId, Oadr2EventImpl aggEvent, Map<String, ArrayList<String>> groupList, int sequence,
			String eventName,boolean isPGE, Map<String, ParticipantClientListFor20Ob> clientParentMap) {		
		HashMap<String,com.honeywell.dras.vtn.api.event.Oadr2EventImpl> oadrEventMap = new HashMap<String,com.honeywell.dras.vtn.api.event.Oadr2EventImpl>();		
		int iEvent=0;
			List<String> resourceIdList = null;
			for(Entry<String, ArrayList<String>> groupSet : groupList.entrySet()) {	
				String groupKey = groupSet.getKey();
				ArrayList<String> groupValue = groupSet.getValue();
				Set<ParticipantClientListFor20Ob> participantClientList = new HashSet<ParticipantClientListFor20Ob>();
				if(groupKey.equalsIgnoreCase("AggGroup") && aggEvent !=null) {
					TargetImpl eventTarget = (TargetImpl) aggEvent.getTarget();
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
					
							List<Oadr2Signal> signals = aggEvent.getSignals();
							Oadr2Signal bidSignal = null;
							for(Oadr2Signal sig: signals) {
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
					TargetImpl eTarget = null;
					Oadr2EventImpl aggChildevent = null;
					String eventId = null;
					Set<ParticipantClientListFor20Ob> participantClientListForchild = new HashSet<ParticipantClientListFor20Ob>();
					for(String groupRes: groupValue) {
						if(count == 0 && clientParentMap.get(groupRes) != null) {
							ParticipantClientListFor20Ob participantClientListFor20Ob = clientParentMap.get(groupRes);
							String parentName = participantClientListFor20Ob.getParentName();
							HashMap<String, Oadr2EventImpl> createOadrEventforGroup = createOADR2AEventPayload(participantClientListFor20Ob.getClientName(), sequence, venId,eventName,null,isPGE,false);						
							for(Entry<String, Oadr2EventImpl> entrySet: createOadrEventforGroup.entrySet()) {
								aggChildevent = entrySet.getValue();
								if(aggChildevent != null){
									eventId = aggChildevent.getEventID();
									eventId = eventId + iEvent++;
									eTarget = (TargetImpl) aggChildevent.getTarget();
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
							if(clientParentMap.get(groupRes) != null  && resourceIdList != null) {
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
					
							List<Oadr2Signal> signals = aggChildevent.getSignals();
							Oadr2Signal bidSignal = null;
							for(Oadr2Signal sig: signals) {
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
	
	/***
	 * this method added to check the instance of DRAS for DBP Enhancement requirement.
	 * DRMS-8593 parent
	 * @return isPGEInstnace 
	 */
	private boolean getInstance() {		
			if(!isPGEInstnace) {
				EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
				String coreProps = cache.getUtilityName("utilityName");
				if(coreProps != null && coreProps.equalsIgnoreCase("PGE")){
					isPGEInstnace = true;
				}
			}
		return isPGEInstnace;
	}
}
