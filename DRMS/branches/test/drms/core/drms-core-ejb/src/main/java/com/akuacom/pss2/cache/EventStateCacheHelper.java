package com.akuacom.pss2.cache;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openadr.dras.eventstate.OperationState;
import org.openadr.dras.eventstate.SimpleClientEventData;
import org.openadr.dras.eventstate.SmartClientDREventData;

import com.akuacom.pss2.contact.ConfirmationLevel;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.util.MemorySequence;

public class EventStateCacheHelper {

	private static final Map<String, EventStateWrapper> esCache = new ConcurrentHashMap<String, EventStateWrapper> ();
	
	
	private static final Map<String, ConfirmationWrapper> idCache = new ConcurrentHashMap<String, ConfirmationWrapper>();
	
	private static final int CACHE_SIZE = 10000;
	
	private static final int TRIM_SIZE = 2000;
	
	private static final EventStateCacheHelper instance = new EventStateCacheHelper();
	
	private static ConfirmationLevel confirmationLevel = null;
	
	private static final Map<String, String> esCacheForUtilityName = new ConcurrentHashMap<String, String> ();
	
	private static final Map<String, SignalDef> esCacheForSignalDef = new ConcurrentHashMap<String, SignalDef> ();
	
	private static final Map<String, List<Contact> > esCacheForOperatorContacts = new ConcurrentHashMap<String, List<Contact> > ();
	
	private static Boolean supressAllEmail = false;
	
	private static String esCacheForFromAddress = null;
	
	public static EventStateCacheHelper getInstance(){
		return instance;
	}
	
	
	
	public ConfirmationResult isConfirmationOk(String participantName, long eventStateID){
		
		boolean result = false;
		ConfirmationWrapper cw = idCache.get(participantName);
		if(cw != null && cw.containsEventID(eventStateID)){
			idCache.remove(participantName);
			result = true;
		}
		EventStateWrapper item = esCache.get(participantName);
		String eventState = null;
		String operationModeValue = null;
		if(item != null){
			eventState = item.getEventStateValue();
			operationModeValue = item.getOperationModeValue();
		}
		
		return new ConfirmationResult(result, eventState, operationModeValue);
		
	}
	
	
	public List<org.openadr.dras.eventstate.EventState> get(String participantName){
		EventStateWrapper item = esCache.get(participantName);
		
		List<org.openadr.dras.eventstate.EventState> result = null;
		if(item != null ){
			if(item.isTimeToDiscard()){
				delete(participantName);
			}else{
				result = item.getEventState();
				Map<String,Date> nearTimeMap = item.getNearTimeMap();
				updateEventStates(result, participantName, nearTimeMap);
			}
		}
		trim();
		return result;
	}
	
	public List<org.openadr.dras.eventstate.EventState> getEventState(String participantName){
		EventStateWrapper item = esCache.get(participantName);
		
		List<org.openadr.dras.eventstate.EventState> result = null;
		if(item != null ){
			if(item.isTimeToDiscard()){
				delete(participantName);
			}else{
				result = item.getEventState();
				Map<String,Date> nearTimeMap = item.getNearTimeMap();
				getUpdateEventStates(result, participantName, nearTimeMap);
			}
		}
		trim();
		return result;
	}
	
	
	private void getUpdateEventStates(List<org.openadr.dras.eventstate.EventState> es, String participantName, Map<String,Date> nearTimeMap){
		if(es != null){
			List<Long> eventStateIDList = new ArrayList<Long>();
			for(org.openadr.dras.eventstate.EventState eventState: es){
				if(eventState != null){
					int id = MemorySequence.getNextSequenceId();
					eventState.setEventStateID(id);
					eventStateIDList.add((long) id);
					String eventID = eventState.getEventIdentifier();
					SmartClientDREventData  smartClientDREventData = eventState.getDrEventData();
					if(smartClientDREventData != null){
						XMLGregorianCalendar startC = smartClientDREventData.getStartTime();
						if(startC != null){
							long start = startC.toGregorianCalendar().getTimeInMillis();
							long now = System.currentTimeMillis();
							long currentTime = now - start;
							SimpleClientEventData  sd = eventState.getSimpleDRModeData();
							
							if(sd != null){
								String status = sd.getEventStatus();
								sd.setCurrentTime(new BigDecimal(currentTime/1000));
								
								long nearTime = 0;
								if(nearTimeMap != null){
									Date nearDate = nearTimeMap.get(eventID);
									if(nearDate != null){
										nearTime = nearDate.getTime();
									}
								}
								
								
								
								if(nearTime != 0 && now > nearTime && status.equalsIgnoreCase("FAR")){
									sd.setEventStatus("NEAR");
								}
							}
						}
					}
				}
			}
			
			ConfirmationWrapper cw = new ConfirmationWrapper(participantName, eventStateIDList);
			idCache.put(participantName, cw);

		}
	}
	
	
	
	
	private void updateEventStates(List<org.openadr.dras.eventstate.EventState> es, String participantName, Map<String,Date> nearTimeMap){
		if(es != null){
			List<Long> eventStateIDList = new ArrayList<Long>();
			for(org.openadr.dras.eventstate.EventState eventState: es){
				if(eventState != null){
					int id = MemorySequence.getNextSequenceId();
					eventState.setEventStateID(id);
					eventStateIDList.add((long) id);
					String eventID = eventState.getEventIdentifier();
					SmartClientDREventData  smartClientDREventData = eventState.getDrEventData();
					if(smartClientDREventData != null){
						XMLGregorianCalendar startC = smartClientDREventData.getStartTime();
						if(startC != null){
							long start = startC.toGregorianCalendar().getTimeInMillis();
							long now = System.currentTimeMillis();
							long currentTime = now - start;
							SimpleClientEventData  sd = eventState.getSimpleDRModeData();
							
							if(sd != null){
								String status = sd.getEventStatus();
								sd.setCurrentTime(new BigDecimal(currentTime/1000));
								
								long nearTime = 0;
								if(nearTimeMap != null){
									Date nearDate = nearTimeMap.get(eventID);
									if(nearDate != null){
										nearTime = nearDate.getTime();
									}
								}
								
								
								
								if(nearTime != 0 &&  now > nearTime && status.equalsIgnoreCase("FAR")){
									sd.setEventStatus("NEAR");
								}
							}
						}
					}
				}
			}
			
			ConfirmationWrapper cw = new ConfirmationWrapper(participantName, eventStateIDList);
			idCache.put(participantName, cw);

		}
	}
	
	
	public void set(String participantName, List<org.openadr.dras.eventstate.EventState> es, Map<String,Date> nearTimeMap){
		long timeToDiscard = getTimeToDiscard(es);
		EventStateWrapper item = new EventStateWrapper(es, timeToDiscard, nearTimeMap);
		esCache.put(participantName, item);
	}
	
	public void set(String propertyName,String propertyValue){
		esCacheForUtilityName.put(propertyName, propertyValue);
	}
	public String getUtilityName(String propertyName){
		String item = esCacheForUtilityName.get(propertyName);
		if(item==null){
			SystemManager systemManager = EJBFactory.getBean(SystemManager.class);
			if(propertyName.equalsIgnoreCase("UtilityName")){
				item = systemManager.getPss2Properties().getUtilityName();
				esCacheForUtilityName.put("UtilityName", item);
			}
			if(propertyName.equalsIgnoreCase("utilityDisplayName")){
				item = systemManager.getPss2Properties().getUtilityDisplayName();
				esCacheForUtilityName.put("utilityDisplayName", item);
			}
		}
		return item;
	}
	
	public void setSignalDef(String key,SignalDef value){
		esCacheForSignalDef.put(key, value)	;
	}
	public SignalDef getSignalDef(String key){
		return esCacheForSignalDef.get(key);
	}
	public String getFromEmailAddress() {
		return esCacheForFromAddress;
	}
	public void setFromEmailAddress(String esCacheForFromAddress) {
		EventStateCacheHelper.esCacheForFromAddress = esCacheForFromAddress;
	}
	public List<Contact> getEscacheforoperatorcontacts() {
		return esCacheForOperatorContacts.get("OperatorContacts");
	}
	public void setEscacheforoperatorcontacts(String key,List<Contact> operatorContacts) {
		esCacheForOperatorContacts.put(key, operatorContacts);
	}
	private long getTimeToDiscard(List<org.openadr.dras.eventstate.EventState> es){
		long result = 0;
		long now = System.currentTimeMillis();
		if(es != null && !es.isEmpty()){
			for(org.openadr.dras.eventstate.EventState eventState: es){
				long temp;
				if(eventState != null){
					SmartClientDREventData  smartClientDREventData = eventState.getDrEventData();
					if(smartClientDREventData != null){
						XMLGregorianCalendar startC = smartClientDREventData.getStartTime();
						XMLGregorianCalendar endC = smartClientDREventData.getEndTime();
						if(startC != null){
							long start = startC.toGregorianCalendar().getTimeInMillis();
							long end = (endC == null) ? 0 : endC.toGregorianCalendar().getTimeInMillis();
							if(now < start){
								if(result == 0 || start < result){
									result = start;
								}
							}else if(now > start && (now < end || end==0)){
								SimpleClientEventData  simpleClientEventData  = eventState.getSimpleDRModeData();
								List<OperationState>  osList = simpleClientEventData.getOperationModeSchedule().getModeSlot();
								for(OperationState os: osList){
									BigInteger seconds = os.getModeTimeSlot();
									if(seconds.doubleValue() > 0){
										temp = (long) (start + seconds.doubleValue() * 1000);
										if(result == 0 || temp < result){
											result = temp;
										}
									}
								}
							}
							
						}
						
						
					}
				}
			}
		}
		return result;
	}
	
	public void delete(String participantName){
		esCache.remove(participantName);
	}
	
	public void trim(){
		if(esCache.size() > CACHE_SIZE){
			Iterator<EventStateWrapper> iterator = esCache.values().iterator();
			int removed = 0;
			while(iterator.hasNext() || removed == TRIM_SIZE){
				iterator.next();
				iterator.remove();
				removed++;
			}
			
		}
		if(idCache.size() > CACHE_SIZE){
			Iterator<ConfirmationWrapper> iterator = idCache.values().iterator();
			int removed = 0;
			while(iterator.hasNext() || removed == TRIM_SIZE){
				iterator.next();
				iterator.remove();
				removed++;
			}
			
		}
	}


	public void clearOldConfirmations() {
        for (String key : idCache.keySet()) {
            ConfirmationWrapper cw = idCache.get(key);
            if (cw.isOld()) {
                idCache.remove(key);
            }
        }


    }

	public ConfirmationLevel getConfirmationLevel() {
		return confirmationLevel;
	}



	public void setConfirmationLevel(ConfirmationLevel confirmationLevel) {
		EventStateCacheHelper.confirmationLevel = confirmationLevel;
	}
	
	public Boolean getSupressAllEmail() {
		return supressAllEmail;
	}

	public void setSupressAllEmail(Boolean supressAllEmail) {
		EventStateCacheHelper.supressAllEmail = supressAllEmail;
	}
}
