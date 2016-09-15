package com.honeywell.dras.adapter;

import java.util.ArrayList;
import java.util.List;

import org.openadr.dras.eventstate.EventInfoInstance;
import org.openadr.dras.eventstate.EventInfoTypeID;
import org.openadr.dras.eventstate.EventInfoValue;
import org.openadr.dras.eventstate.EventState;
import org.openadr.dras.eventstate.OperationState;
import org.openadr.dras.eventstate.SimpleClientEventData.OperationModeSchedule;

import com.akuacom.pss2.openadr2.event.util.DateUtils;
import com.akuacom.pss2.program.ProgramPerf;
import com.honeywell.dras.vtn.api.EventStatus;
import com.honeywell.dras.vtn.api.Interval;
import com.honeywell.dras.vtn.api.IntervalDurationResolution;
import com.honeywell.dras.vtn.api.Oadr2Event;
import com.honeywell.dras.vtn.api.Oadr2Signal;
import com.honeywell.dras.vtn.api.ResponseRequired;
import com.honeywell.dras.vtn.api.SignalType;
import com.honeywell.dras.vtn.api.event.IntervalImpl;
import com.honeywell.dras.vtn.api.event.Oadr2EventImpl;
import com.honeywell.dras.vtn.api.event.Oadr2SignalImpl;
import com.honeywell.dras.vtn.api.event.TargetImpl;

public class Converter {
	
	public  static long roundableToMin(long minsec){
		//to fix time trimming problem in PayloadGenertor -e.g. 59:59sec will be trimmed to 59M
		return Math.round((minsec*1.00000)/(60000.000))*(60000);
	}
	
	public static Oadr2Event convertEventState2Oadr2Event(EventState evtSts,ProgramPerf program){
		Oadr2EventImpl oadr2evt = new Oadr2EventImpl();
		
		
		
		oadr2evt.setEventID(evtSts.getEventIdentifier());
		
		oadr2evt.setModificationNumber(evtSts.getEventModNumber());
		oadr2evt.setMarketContext(program.getProgramName());
		oadr2evt.setStartTime(DateUtils.asDate(evtSts.getDrEventData().getStartTime()));
		
		long duration = 0;
		if(evtSts.getDrEventData().getEndTime()!=null){
			duration = DateUtils.asDate(evtSts.getDrEventData().getEndTime()).getTime()
					-oadr2evt.getStartTime().getTime();
			oadr2evt.setDuration(roundableToMin(duration));
		}else{
			oadr2evt.setDuration(roundableToMin(duration)); // end time null set duration to 0
		}
		
		
		oadr2evt.setNotification(oadr2evt.getStartTime().getTime()
					-DateUtils.asDate(evtSts.getDrEventData().getNotificationTime()).getTime());
		
		
		oadr2evt.setTest(evtSts.isTestEvent());
		oadr2evt.setEventStatus(convertString2Oadr2EventStatus(evtSts.getSimpleDRModeData().getEventStatus()));
		oadr2evt.setPriority(new Long(program.getPriority()));
		oadr2evt.setResponseRequired(ResponseRequired.ALWAYS);
		//event.setVtnComment("");
		if(evtSts.getDrEventData().getNearTime() != null) {
			oadr2evt.setRampUp(oadr2evt.getStartTime().getTime()
					-DateUtils.asDate(evtSts.getDrEventData().getNearTime()).getTime());
		} else {
			oadr2evt.setRampUp(0L);
		}
		oadr2evt.setRecovery(0);
		oadr2evt.setToleranceStartAfter(0);
		oadr2evt.setToleranceStartBefore(0);
		
		TargetImpl target = new TargetImpl();
		List<String> res = new ArrayList<String>();
		res.add(evtSts.getDrasClientID());
		target.setResourceIdList(res);
		List<String> partys = new ArrayList<String>();
		target.setPartyIdList(partys);
		List<String> groups = new ArrayList<String>();		
		target.setGroupIdList(groups);
		List<String> venIds = new ArrayList<String>();
		venIds.add(evtSts.getDrasClientID());
		target.setVenIdList(venIds);
		
		oadr2evt.setTarget(target);
		
		List <Oadr2Signal> signals = new ArrayList<Oadr2Signal>();
		int signalIndex = 0;
		
		//mode signals - always delivered in 7 
		signals.add(convertModeSchedule2Oadr2Signal(
					evtSts,evtSts.getSimpleDRModeData().getOperationModeSchedule(),signalIndex++));
		
		//commented out because -Other signals were not allowed in opanadr2.0 profile a
		//may required in openadr2.0 profile b
		//for(EventInfoInstance evtInfoIns:evtSts.getDrEventData().getEventInfoInstances()){
		//	signals.add(convertEventInfoInstance2Oadr2Signal(evtSts,evtInfoIns,signalIndex++));
		//}
		oadr2evt.setSignals(signals);
		return oadr2evt;
	}
	
	
	public static Oadr2Signal convertModeSchedule2Oadr2Signal(EventState evtSts,OperationModeSchedule schedule,
				int signalIndex){
		Oadr2SignalImpl oadr2Signal = new Oadr2SignalImpl();
	
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
		
		List <Interval> intervals = new ArrayList<Interval>();
		List<OperationState> states =schedule.getModeSlot();
		int size = states.size();
		int k =0;
		for(OperationState state:states){
			int i =states.indexOf(state);
			IntervalImpl interval = new IntervalImpl();
			interval.setResolution(IntervalDurationResolution.MINUTE);
			//duration in millisecond
			interval.setOffsetId(k+"");
			//offset in seconds in openadr1.0
			long nextOffset = i==size-1? evtdurationinsec: states.get(i+1).getModeTimeSlot().longValue();
			long duration = roundableToMin((nextOffset-state.getModeTimeSlot().longValue())*1000 );
			//int k is to make the uid flow in sequence as for midnight 23.59 for 1 min we skip interval
			if(duration>0){
				interval.setDurationInResolution(roundableToMin((nextOffset-state.getModeTimeSlot().longValue())*1000 ));
				interval.setValue(operationalMode2Int(state.getOperationModeValue()));
				intervals.add(interval);
				k++;
			}
		}
		oadr2Signal.setIntervals(intervals);
		return oadr2Signal;
	}
	
	public static Oadr2Signal convertEventInfoInstance2Oadr2Signal(EventState evtSts,EventInfoInstance evtInstance,
				int signalIndex){
		Oadr2SignalImpl oadr2Signal = new Oadr2SignalImpl();
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
		
		//TODO
		List <Interval> intervals = new ArrayList<Interval>();
		List<EventInfoValue> values = evtInstance.getEventInfoValues();
		
		long evtdurationmillsec=0;
		if(evtSts.getDrEventData().getEndTime()!=null){
			evtdurationmillsec =DateUtils.asDate(evtSts.getDrEventData().getEndTime()).getTime()
							-DateUtils.asDate(evtSts.getDrEventData().getStartTime()).getTime();
		}else{
			evtdurationmillsec=0;
		}
		
		//offset in millisecond;
		long currentOffset = ( System.currentTimeMillis()
				-DateUtils.asDate(evtSts.getDrEventData().getStartTime()).getTime());
		
		long currentSlotStart=0;
		for(EventInfoValue evtInfoval:values){
			Interval interval = convertEventInfoValue2Interval(evtdurationmillsec,values,evtInfoval);
			if(interval.getDurationInResolution()==0){
				continue;
			}
			long currentduration=interval.getDurationInResolution();
			if(currentOffset>=currentSlotStart && currentOffset<currentduration+interval.getDurationInResolution()){
				oadr2Signal.setCurrentValue(interval.getValue());
			}
			currentSlotStart += interval.getDurationInResolution();
			//calculate current value
			intervals.add(interval);
		}
		oadr2Signal.setIntervals(intervals);
		return oadr2Signal;
	}
	
	public static Interval convertEventInfoValue2Interval(long evtduration, List<EventInfoValue> values,EventInfoValue evtInfoValue){
		IntervalImpl interval = new IntervalImpl();
		interval.setResolution(IntervalDurationResolution.MINUTE);
		
		int size = values.size();
		int index = values.indexOf(evtInfoValue);
		
		//second 
		evtduration = evtduration/1000;
		//offset in seconds in openadr1.0
		long nextOffset = index==size-1? evtduration: values.get(index+1).getTimeOffset();
		interval.setDurationInResolution(roundableToMin((nextOffset-evtInfoValue.getTimeOffset())*1000));
		
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
			throw new IllegalArgumentException(" Unable to convert "+typeId.getValue() +" to openADR2.0 signal");
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
	
}
