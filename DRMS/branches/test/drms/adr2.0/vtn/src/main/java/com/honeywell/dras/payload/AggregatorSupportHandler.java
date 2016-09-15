package com.honeywell.dras.payload;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantEAO;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.aggregator.ProgramAggregator;
import com.akuacom.pss2.program.aggregator.eao.ProgramAggregatorEAO;
import com.akuacom.pss2.program.apx.aggregator.eao.ApxAggregatorEAOManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManager;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.honeywell.dras.vtn.api.Target;
import com.honeywell.dras.vtn.api.event.Oadr2EventImpl;
import com.honeywell.dras.vtn.api.event.TargetImpl;

public class AggregatorSupportHandler {
	
	static Logger log = Logger.getLogger(AggregatorSupportHandler.class);	
	private EventEAO eventEao = EJBFactory.getBean(EventEAO.class);	
	private ApxAggregatorEAOManager apxAggEaoManager = EJBFactory.getBean(ApxAggregatorEAOManager.class);	
	private ParticipantManager partManager = EJBFactory.getBean(ParticipantManager.class);	
	private ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);	
	private ProgramParticipantAggregationManager ppManager =EJBFactory.getBean(ProgramParticipantAggregationManager.class);	
	private EventManager eventManager = EJBFactory.getBean(EventManager.class);
	private EventParticipantEAO eventParticipantEAO = EJBFactory.getBean(EventParticipantEAO.class);
	private static boolean isPGEInstnace;
    protected CorePropertyEAO coreProperty = EJBFactory.getBean(CorePropertyEAO.class);	
    private ProgramAggregatorEAO programagg = EJBFactory.getBean(ProgramAggregatorEAO.class);
	/**
	 * List the aggregator children and return 
	 * else if participants doesn't have descendents return null
	 * 
	 */
	public HashMap<String, Participant> getAggregatorChilds(Participant participant,String programName) {
		if(participant.isClient()) {
			String parent2 = participant.getParent();	
			if(parent2 != null) {
				Participant aggregator = participantManager.getParticipantAndProgramParticipantsOnly(parent2,false);
				if(aggregator != null) {
					HashMap<String, Participant> aParticipants = new HashMap<String, Participant>();
					Set<ProgramParticipant> programParticipants = aggregator.getProgramParticipants();						
					for(ProgramParticipant p: programParticipants){	
					if(p.getProgramName()!=null && p.getProgramName().equalsIgnoreCase(programName)){
					 Set<ProgramParticipant> descendants = ppManager.getDescendantsForSpecifiedProgram(p,programName);
					 for(ProgramParticipant pp: descendants) {
						 Participant party = pp.getParticipant();
						 aParticipants.put(party.getAccountNumber(), party);
					 }
					}
				 }
					return aParticipants;
				}
			}
		}
		return null;
	}
	
	public List<String> updateOadrEventResourceListWithExtraResource(String eventId, String clientId, List<String> resourceList,String parent,boolean isPGE){
		String uuid = "";
		Set<String> resources = new HashSet<String>();
		if(isPGE){
			 resources = apxAggEaoManager.getAggregatorResources(eventId,clientId);
		}else if(null!=parent){
				ProgramAggregator progAgg = programagg.findAccountNumberOfAggregator(parent,eventId);
			if (null != progAgg) {
				String accountNumber = progAgg.getAggregatorAccountNumber();
				if (accountNumber != null) {
					resourceList.clear();
					resources.add(accountNumber);
				}
			}
		}
		for(String str: resources){
			resourceList.add(str);
		}
		return resourceList;
	}
	
	public void fillLocationInfoAndCreatedTime(Oadr2EventImpl oadrEvent ,Event event){
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
		
		Target target = oadrEvent.getTarget();		
		if(null == target){
			oadrEvent.setTarget(new TargetImpl());
		}
		List<String> groupIdList = target.getGroupIdList();
		if(groupIdList == null){
			groupIdList = new ArrayList<String>();
		} 
		groupIdList.addAll(nodes);		
	}
	
	public Participant getAggregator(String clientId, boolean isClinet) {
		if(clientId != null) {
			if(isClinet) {
				return participantManager.getParticipantOnly(clientId, isClinet);
			} else {
				Participant participant = participantManager.getParticipantOnly(clientId, true);
				String parent = participant.getParent();
				return  participantManager.getParticipantOnly(parent, isClinet);
			}
		}
		return null;
	}

	public HashMap<String, Participant> getAggregatorInfo(String clientId,String programName,Participant aggregator) {
		if(clientId != null && aggregator != null) {
				return getAggregatorChilds(aggregator,programName);
		}
		return null;
	}
	
	public List<Participant> getAggregatorParticipantClients(HashMap<String, Participant> aggregatorChilds, String eventName) {			
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
	 * List the aggregator children signals
	 * @param clientList
	 * @return listOfSigEntry
	 */
	public HashMap<Participant, ArrayList<EventParticipantSignalEntry>> getAggChlidrenSignlaList(
			List<String> clientList, String eventId) {
		List<EventParticipant> findEventParticipantWithSignalsByAllClientUUID = eventParticipantEAO.findEventParticipantWithSignalsByAllClientUUIDforEvent(clientList, eventId);
		HashMap<Participant, ArrayList<EventParticipantSignalEntry>> listOfSigEntry = new HashMap<Participant, ArrayList<EventParticipantSignalEntry>>();
		for(EventParticipant ep: findEventParticipantWithSignalsByAllClientUUID){
			ArrayList<EventParticipantSignalEntry> sigEntriesforParticipant = new ArrayList<EventParticipantSignalEntry>();
			 for(EventParticipantSignalEntry sigEn:ep.getSignalEntries()){
				 sigEntriesforParticipant.add(sigEn);
			 }
			 listOfSigEntry.put(ep.getParticipant(), sigEntriesforParticipant);
		}
		return listOfSigEntry;
	}
	
	public HashMap<Participant, ArrayList<EventParticipantSignalEntry>> getAggChlidrenSignlaList(List<EventParticipant> evtPart) {
		HashMap<Participant, ArrayList<EventParticipantSignalEntry>> listOfSigEntry = new HashMap<Participant, ArrayList<EventParticipantSignalEntry>>();
		if(null!= evtPart){
			for(EventParticipant ep: evtPart){
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
	public Map<String, ArrayList<String>> checkSignalsforAggregatorChildParticipats(HashMap<Participant, ArrayList<EventParticipantSignalEntry>> aggChlidrenSignlaList, 
			HashMap<Participant, ArrayList<EventParticipantSignalEntry>> listOfSigEntry){			
		Map<Date, String> aggSignals = new HashMap<Date, String>();
		//Add Aggregator Signals
		for(Entry<Participant, ArrayList<EventParticipantSignalEntry>> aggEntrySet: aggChlidrenSignlaList.entrySet()){
			ArrayList<EventParticipantSignalEntry> aggEntryValue = aggEntrySet.getValue();			
			for(EventParticipantSignalEntry aggvalue: aggEntryValue){
				String levelValue = aggvalue.getLevelValue();
				if(levelValue != null && !levelValue.equalsIgnoreCase("on")) {
					Date time = aggvalue.getTime();	
					aggSignals.put(time, levelValue);
				}
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
				if(levelValue != null && !levelValue.equalsIgnoreCase("on")) {
					Date time = cSignalValue.getTime();
					aggchildSignals.put(time, levelValue);
				}
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
	
	
	public Event getEvent(String eventName) {
		if(eventName != null) {
			try {
				return eventEao.getByEventName(eventName);
			} catch (EntityNotFoundException e) {
				log.error(e);
			}
		}
		return null;
	}
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
