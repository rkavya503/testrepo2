package com.akuacom.pss2.openadr2.event.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.openadr.dras.eventstate.EventInfoInstance;
import org.openadr.dras.eventstate.EventInfoTypeID;
import org.openadr.dras.eventstate.EventInfoValue;
import org.openadr.dras.eventstate.EventState;
import org.openadr.dras.eventstate.OperationState;
import org.openadr.dras.eventstate.SimpleClientEventData.OperationModeSchedule;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.ProgramPerf;
import com.honeywell.dras.vtn.api.EventStatus;
import com.honeywell.dras.vtn.api.ResponseRequired;
import com.honeywell.dras.vtn.api.SignalType;
import com.honeywell.dras.vtn.api.common.EiTarget;
import com.honeywell.dras.vtn.api.event.Event;
import com.honeywell.dras.vtn.api.event.EventInterval;
import com.honeywell.dras.vtn.api.event.Signal;
import com.honeywell.dras.vtn.api.opt.FeatureCollection;
import com.honeywell.dras.vtn.api.opt.ServiceLocation;


public class EventConverter {
	public  static long roundableToMin(long minsec){
		//to fix time trimming problem in PayloadGenertor -e.g. 59:59sec will be trimmed to 59M
		return Math.round((minsec*1.00000)/(60000.000))*(60000);
	}
	
	public static Event convertEventState2Oadr2Event(EventState evtSts,ProgramPerf program,String venId){
		Event oadr2evt = new Event();
		
		oadr2evt.setEventID(evtSts.getEventIdentifier());

		oadr2evt.setModificationNumber(evtSts.getEventModNumber());
		oadr2evt.setMarketContext(program.getProgramName());
		oadr2evt.setStartTime(DateUtils.asDate(evtSts.getDrEventData().getStartTime())); 
			
		long duration = 0;
		if(evtSts.getDrEventData().getEndTime()!=null){
			duration = DateUtils.asDate(evtSts.getDrEventData().getEndTime()).getTime()
						-oadr2evt.getStartTime().getTime();
		}else{
			duration = 0; // this is allowed as per 2.0b spec, if no defined end time send 0 as duration
		}
		oadr2evt.setDuration(roundableToMin(duration));
		
		oadr2evt.setNotification(oadr2evt.getStartTime().getTime()
					-DateUtils.asDate(evtSts.getDrEventData().getNotificationTime()).getTime());
		
		oadr2evt.setTest(evtSts.isTestEvent());
		oadr2evt.setEventStatus(convertString2Oadr2EventStatus(evtSts.getSimpleDRModeData().getEventStatus()));
		
		oadr2evt.setPriority(new Long(program.getPriority()));
		oadr2evt.setResponseRequired(ResponseRequired.ALWAYS);
		
		oadr2evt.setVtnComment("");
		if(evtSts.getDrEventData().getNearTime() != null) {
			oadr2evt.setRampUp(oadr2evt.getStartTime().getTime()
				-DateUtils.asDate(evtSts.getDrEventData().getNearTime()).getTime());
		} else {
			oadr2evt.setRampUp(0L);
		}
		oadr2evt.setRecovery(0L);
		oadr2evt.setToleranceStartAfter(0L);
		//oadr2evt.setToleranceStartBefore(0L);
		
		EiTarget target = new EiTarget();
		
		//target.setResourceIdList(Arrays.asList(evtSts.getDrasClientID()));
		List<String> resourceIdList = new ArrayList<String>();
		target.setResourceIdList(resourceIdList);
		List<String> empty = Collections.emptyList();
		target.setPartyIdList(empty);
		target.setGroupIdList(empty);
		List<String> venIdList = new ArrayList<String>();
		venIdList.add(venId);
		target.setVenIdList(venIdList);
		
		oadr2evt.setTarget(target);
		
		List <Signal> signals = new ArrayList<Signal>();
		int signalIndex = 0;

		//mode signals - always delivered in 7 
			signals.add(convertModeSchedule2Oadr2Signal(
						evtSts,evtSts.getSimpleDRModeData().getOperationModeSchedule(),signalIndex++));
				
			for(EventInfoInstance evtInfoIns:evtSts.getDrEventData().getEventInfoInstances()){
				signals.add(convertEventInfoInstance2Oadr2Signal(evtSts,evtInfoIns,signalIndex++));
			}
			
		oadr2evt.setSignals(signals);
		return oadr2evt;
	}
	
	public static List<Signal> addEventSignalsforAggregatorChilderns(EventState evtSts, String venId, List<Participant> eventparticipant){	
		List <Signal> signals = new ArrayList<Signal>();
		int signalIndex = 0;		
				//mode signals - always delivered in 7 
				Signal convertModeSchedule2Oadr2Signal = convertModeSchedule2Oadr2Signal(evtSts,evtSts.getSimpleDRModeData().getOperationModeSchedule(),signalIndex++);				
				EiTarget signalTargets = addSignalTargets(eventparticipant, venId);
				if(signalTargets != null) {
					convertModeSchedule2Oadr2Signal.setTarget(signalTargets);
				}
				signals.add(convertModeSchedule2Oadr2Signal);
				
				/*signals.add(convertModeSchedule2Oadr2Signal(
							evtSts,evtSts.getSimpleDRModeData().getOperationModeSchedule(),signalIndex++, eventparticipant, venId));*/
				
				//commented out because -Other signals were not allowed in opanadr2.0 profile a
				//may required in openadr2.0 profile b
				for(EventInfoInstance evtInfoIns:evtSts.getDrEventData().getEventInfoInstances()){
					Signal convertEventInfoInstance2Oadr2Signal = convertEventInfoInstance2Oadr2Signal(evtSts,evtInfoIns,signalIndex++);
					
					EiTarget bidTargets = addSignalTargets(eventparticipant, venId);
					if(bidTargets != null) {
						convertEventInfoInstance2Oadr2Signal.setTarget(bidTargets);
					}
					signals.add(convertEventInfoInstance2Oadr2Signal);
				}
		return signals;
	}
	
	public static EiTarget addSignalTargets(List<Participant> p, String venId){
			if(p != null) {
				EiTarget target = new EiTarget();
				List<String> empty = Collections.emptyList();
				target.setPartyIdList(empty);
				target.setGroupIdList(empty);
				List<String> list = new ArrayList<String>();
				for(Participant par: p) {
					if(par.getApplicationId() != null) {
						list.add(par.getApplicationId());
					} else {
						list.add(par.getAccountNumber());
					}
				}
				target.setResourceIdList(list);
				List<String> venIdList = new ArrayList<String>();
				venIdList.add(venId);
				target.setVenIdList(venIdList);			
				return target;
			}
			return null;
	}
	
	public static Signal convertModeSchedule2Oadr2Signal(EventState evtSts,OperationModeSchedule schedule,
				int signalIndex){
			
		Signal oadr2Signal = new Signal();
	
		oadr2Signal.setName("simple");
		oadr2Signal.setType(SignalType.LEVEL);
		oadr2Signal.setId(oadr2Signal.getType().value().toUpperCase() +":Signal"+signalIndex);
		oadr2Signal.setCurrentValue(operationalMode2Int(
			evtSts.getSimpleDRModeData().getOperationModeValue()));
		
		long evtdurationinsec = 0;
		if(evtSts.getDrEventData().getEndTime()!=null){
			evtdurationinsec =(DateUtils.asDate(evtSts.getDrEventData().getEndTime()).getTime()
			-DateUtils.asDate(evtSts.getDrEventData().getStartTime()).getTime())/1000;
		}else{
			evtdurationinsec = 0;
		}
		
		List <EventInterval> intervals = new ArrayList<EventInterval>();
		List<OperationState> states =schedule.getModeSlot();
		int size = states.size();
		long totalDuration = 0;

		for(OperationState paststate:states){
			
			int j =states.indexOf(paststate);
			
			long nextOffsetP = j==size-1? evtdurationinsec: states.get(j+1).getModeTimeSlot().longValue();
			long durationP = roundableToMin((nextOffsetP-paststate.getModeTimeSlot().longValue())*1000 );
			if(durationP>0){
				totalDuration = totalDuration+durationP;
			}
		}
		
		totalDuration = totalDuration/1000;
		
		long durDiff = 0;
		if(evtdurationinsec>0){
			durDiff = evtdurationinsec-totalDuration;
		}else if(evtdurationinsec==0){
			durDiff = (DateUtils.asDate(evtSts.getDrEventData().getNotificationTime()).getTime()-DateUtils.asDate(evtSts.getDrEventData().getStartTime()).getTime())/1000;
		}
		
		
		boolean pastd = false;
		// [SGSDI # 221 ] added dummy interval(signals) for event issued ahead of time (past time) this interval is created to match the event duration with event duration and should not be referred in devices 
		if(durDiff>0 && !pastd){
			EventInterval interval = new EventInterval();
			interval.setOffsetId(0+"");
			interval.setDuration(roundableToMin(durDiff*1000));
			interval.setValue(0.0f);
			intervals.add(interval);
			pastd = true;
		}
		int k =0;
		for(OperationState state:states){
			int i =states.indexOf(state);
			EventInterval interval = new EventInterval();
			
			//interval.setResolution(IntervalDurationResolution.MINUTE);
			//duration in millisecond
			if(k==0 && pastd){
				k = i+1;
			}else if(k==0){				
				k = i;
			}
			interval.setOffsetId(k+"");
			//offset in seconds in openadr1.0
			long nextOffset = i==size-1? evtdurationinsec: states.get(i+1).getModeTimeSlot().longValue();
			long duration = roundableToMin((nextOffset-state.getModeTimeSlot().longValue())*1000 );
			//int k is to make the uid flow in sequence as for midnight 23.59 for 1 min we skip interval 
			if(duration>0){
				interval.setDuration(roundableToMin((nextOffset-state.getModeTimeSlot().longValue())*1000 ));
				interval.setValue(operationalMode2Int(state.getOperationModeValue()));
				intervals.add(interval);
				k++;
			}
		}
		
		// [SGSDI # 67 ] An intervals(signals) for event with unlimited time (no end time) this interval is created with 0 duration to match the event duration with event duration and should not be referred in devices
		if(evtdurationinsec==0){
			EventInterval interval = new EventInterval();
			interval.setOffsetId((k)+"");
			interval.setDuration(0L);
			interval.setValue(0.0f);
			intervals.add(interval);
		}
		
		oadr2Signal.setIntervals(intervals);		
		return oadr2Signal;
	}
	
	public static Signal convertEventInfoInstance2Oadr2Signal(EventState evtSts,EventInfoInstance evtInstance,
				int signalIndex){
		Signal oadr2Signal = new Signal();
		oadr2Signal.setCurrentValue(0f);
		oadr2Signal.setType(
					convertEventInfoType2SignalType(evtInstance.getEventInfoTypeID()));
		oadr2Signal.setName(evtInstance.getEventInfoName());
		oadr2Signal.setId(oadr2Signal.getType().value().toUpperCase() +":Signal"+signalIndex);
		
		if(oadr2Signal.getType()==SignalType.LEVEL){
			oadr2Signal.setName("simple");
			oadr2Signal.setCurrentValue(operationalMode2Int(
					evtSts.getSimpleDRModeData().getOperationModeValue()));
		}
		
		List <EventInterval> intervals = new ArrayList<EventInterval>();
		List<EventInfoValue> values = evtInstance.getEventInfoValues();
		
		long evtdurationmillsec = 0;
		if(evtSts.getDrEventData().getEndTime()!=null){
			evtdurationmillsec =DateUtils.asDate(evtSts.getDrEventData().getEndTime()).getTime()
							-DateUtils.asDate(evtSts.getDrEventData().getStartTime()).getTime();
		}else{
			evtdurationmillsec = 0;
		}
		
		//offset in millisecond;
		long currentOffset = ( System.currentTimeMillis()
				-DateUtils.asDate(evtSts.getDrEventData().getStartTime()).getTime());
		
		long currentSlotStart=0;
		for(EventInfoValue evtInfoval:values){
			EventInterval interval = convertEventInfoValue2Interval(evtdurationmillsec,values,evtInfoval);
			if(interval.getDuration()==0){
				continue;
			}
			long currentduration=interval.getDuration();
			if(currentOffset>=currentSlotStart && currentOffset<currentduration+interval.getDuration()){
				oadr2Signal.setCurrentValue(interval.getValue());
			}
			currentSlotStart += interval.getDuration();
			//calculate current value
			intervals.add(interval);
		}			
		oadr2Signal.setIntervals(intervals);
		return oadr2Signal;
	}
	
	public static EventInterval convertEventInfoValue2Interval(long evtduration, List<EventInfoValue> values,EventInfoValue evtInfoValue){
		EventInterval interval = new EventInterval();
		
		//interval.setResolution(IntervalDurationResolution.MINUTE);
		
		int size = values.size();
		int index = values.indexOf(evtInfoValue);
		
		//second 
		evtduration = evtduration/1000;
		//offset in seconds in openadr1.0
		long nextOffset = index==size-1? evtduration: values.get(index+1).getTimeOffset();
		long intervalValueInSeconds = Math.round(nextOffset-evtInfoValue.getTimeOffset());
		interval.setDuration(intervalValueInSeconds*1000);
		
		//interval.setDurationInResolution(roundableToMin((nextOffset-evtInfoValue.getTimeOffset())*1000));
		
		//TODO
		interval.setOffsetId(index+"");
		//TODO
		interval.setValue(evtInfoValue.getValue().floatValue());
		return interval;
	}
	
	public static EventStatus convertString2Oadr2EventStatus(String evtsts){
		if(evtsts.equalsIgnoreCase("NONE")){
			return EventStatus.NONE;
		}else if(evtsts.equalsIgnoreCase("FAR")){
			return EventStatus.FAR;
		}else if(evtsts.equalsIgnoreCase("NEAR")){
			return EventStatus.NEAR;
		}else if(evtsts.equalsIgnoreCase("ACTIVE")){
			return EventStatus.ACTIVE;
		}else if(evtsts.equalsIgnoreCase("CANCELED") 
					|| evtsts.equalsIgnoreCase("CANCELLED") 
					||  evtsts.equalsIgnoreCase("OPT_OUT") ){
			return EventStatus.CANCELLED;
		}
		return EventStatus.NONE;
	}
	
	public static SignalType convertEventInfoType2SignalType(EventInfoTypeID typeId){
		String value = typeId.getValue();
		if(value.equalsIgnoreCase("LOAD_LEVEL")){
			return SignalType.LEVEL;
		}else if(value.equalsIgnoreCase("PRICE_ABSOLUTE")){
			return SignalType.PRICE;
		}else if(value.equalsIgnoreCase("PRICE_RELATIVE")){
			return SignalType.PRICE_RELATIVE;
		}else if(value.equalsIgnoreCase("PRICE_MULTIPLE")){
				return SignalType.PRICE_MULTIPLIER;
		}else if(value.equalsIgnoreCase("LOAD_AMOUNT")){
			return SignalType.DELTA;
		}
		else	
			throw new IllegalArgumentException("Unable to convert "+typeId.getValue() +" to openADR2.0 signal");
	}
	
	public  static float operationalMode2Int(String operationMode){
		if(operationMode.equalsIgnoreCase("normal"))
				return 0;
		if(operationMode.equalsIgnoreCase("moderate"))
			return 1;
		if(operationMode.equalsIgnoreCase("high"))
			return 2;
		if(operationMode.equalsIgnoreCase("special"))
			return 3;
		return 0;
	}
	private void fillLocationInfo(Event event , List<String> locationList){
		
		List<FeatureCollection> featureCollectionList = new ArrayList<FeatureCollection>();
		Integer locationId = 1;
		for(String location : locationList){
			FeatureCollection fc = new FeatureCollection();
			fc.setId(locationId.toString());
			fc.setLocation(location);
			featureCollectionList.add(fc);
			locationId++;
		}
		ServiceLocation serviceLocation = new ServiceLocation();
		serviceLocation.setFeatureCollectionList(featureCollectionList);
		EiTarget target = event.getTarget();
		if(null == target){
			event.setTarget(new EiTarget());
		}
		List<ServiceLocation> serviceLocationList =  target.getServiceLocationList();
		if(null == serviceLocationList){
			target.setServiceLocationList(new ArrayList<ServiceLocation>());
		}
		target.getServiceLocationList().add(serviceLocation);
	}

}
