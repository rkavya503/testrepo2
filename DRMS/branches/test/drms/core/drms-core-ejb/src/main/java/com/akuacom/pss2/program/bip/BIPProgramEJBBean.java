package com.akuacom.pss2.program.bip;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.Timer;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.event.BIPEventUtil;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntryEAO;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.cpp.CPPProgramEJBBean;
import com.akuacom.pss2.program.sceftp.progAutoDisp.InterruptibleProgramManager;
import com.akuacom.pss2.query.EventEnrollingGroup;
import com.akuacom.pss2.query.EvtParticipantCandidate;
import com.akuacom.pss2.query.NativeQueryManager;
import com.akuacom.pss2.query.ParticipantUtils;
import com.akuacom.pss2.query.SCELocationUtils;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.DateUtil;

@Stateless
public class BIPProgramEJBBean extends CPPProgramEJBBean implements
		BIPProgramEJB.L, BIPProgramEJB.R {

	/** The Constant log. */
    private static final Logger log = Logger.getLogger(BIPProgramEJBBean.class);
    
	@EJB 
	private EventParticipantSignalEntryEAO.L signalEntryEAO;
	
	@EJB
	private NativeQueryManager.L nativeQuery;
	
	@EJB
	private EventEAO.L eventEAO;
	
	@EJB
	EventManager.L eventManager;
	
	
	@EJB
	private InterruptibleProgramManager.L irrProgramManager;
	
	@Override
	public void processTimeout(Timer timer) {
		super.processTimeout(timer);
	}
	
	@Override
	public Collection<String> createEvent(String programName, Event event,
			UtilityDREvent utilityDREvent) {
		//check if the event object contains event participants
		if(event.getLocations()!=null && !event.getLocations().isEmpty() &&
				(event.getEventParticipants()==null || event.getEventParticipants().isEmpty())){
			//try to load event participants in these locations
			try {
				List<EventEnrollingGroup> locations=nativeQuery.
							getEventParticipantsByLocation(programName, 
									SCELocationUtils.getLocationType(programManager.getProgramOnly(programName).getProgramClass()));
				
				for(EventEnrollingGroup group:locations){
					if(event.getLocations().indexOf(group.getGroupId())>=0){
						for(EvtParticipantCandidate candidate:group.getLegibleEventParticipants()){
							ParticipantUtils.createEventParticipantForEvent(event, candidate);
						}
					}
				}
			} catch (SQLException e) {
				log.error("Error to load participants by locations");
			}
		}
		//then follow basic process 
		return super.createEvent(programName, event, utilityDREvent);
	}
	
	 protected void setEventStatus(Event event){
		  long ms = System.currentTimeMillis();
		  if(event.getStartTime().getTime()<ms)
			  event.setEventStatus(EventStatus.ACTIVE);
		  else if(event.getIssuedTime().getTime() < ms)
	    	  event.setEventStatus(EventStatus.FAR);
		  else
			  event.setEventStatus(EventStatus.RECEIVED);
	 }
	 
	/**
	 * this method is called by timer service. if it's mid night, 
	 * new signals entries of the coming day will be appended to the event signal list
	 */
	protected void processEvent(Event event, long ms) {
		//end time may not be specified. Null end time means event will last 
		//User can specify end time after event created. 
		//This will only happen at midnight to catch up new signals for the coming day
		if (event.getEndTime()==null || event.getEndTime().getTime() > ms) {
			//check the latest signal entries
			Date latestTime=signalEntryEAO.getLatestSignalStartTimeInEvent(event.getEventName());
			if(latestTime!=null && latestTime.getTime()<=ms){
				//now need to append signals of the coming day since BIP event can cross days
				event = eventEAO.findEventWithParticipantsAndSignals(event.getEventName());
				//now 
				try {
					//work around - set event date as today so that new signals can be added 
					Date startTime = event.getStartTime();
					event.setStartTime(DateUtil.stripTime(new Date()));
					
					Program program = programManager.getProgramWithParticipantsAndPRules(event.getProgramName());
					processRulesAndSignals(program, event, null, new Date());
					// Persist the modified event
					
					//don't change start time- revert event startTime
					event.setStartTime(startTime);
					
					eventEAO.update(event);
				} catch (Exception e) {
					 log.error(LogUtils.createLogEntry(event.getProgramName(),
			                    LogUtils.CATAGORY_EVENT, "Error updating event signal", null));
			          throw new EJBException(e);
				}
			}
		}
		
		if(!BIPEventUtil.isSameEndTimeForAllLocations(event)){
			Map<String, Date> locationEndTimes = BIPEventUtil.getAllEventLocationAndEndTime(event);
			for(String loc:locationEndTimes.keySet()){
				Date locEndTime = locationEndTimes.get(loc);
				Date now = new Date();
				event = eventEAO.findEventWithParticipantsByEventName(event.getEventName());
				Set<EventParticipant> eplist = event.getEventParticipants();
				//make a copy to avoid ConcurrentModificationException 
				List<EventParticipant> eplistCopy = new ArrayList<EventParticipant>(eplist);
				
				if(locEndTime!=null && locEndTime.before(now)){
					//end locations
					for(Iterator<EventParticipant> it=eplistCopy.iterator();it.hasNext();){
						EventParticipant ep = it.next();
						if(isParticipantsInLocation(ep.getParticipant(),loc)){
							//remove participants from event
							if(ep.getEventOptOut()==0){
								eventManager.removeParticipantFromEvent(event.getEventName(),
									ep.getParticipant().getParticipantName(),false);
							}
						}
					}
				}
			}
		}
		super.processEvent(event, ms);
	}
	
	protected boolean isParticipantsInLocation(Participant p,String location){
		return	location.equals(p.getABank());
	}
	
	protected void publicToDRwebsite(Program program, Event event) {
		irrProgramManager.createEvent("TOU-BIP", "TOU-BIP", "ABANK", BIPEventUtil.getAllEventLocations(event), 
					event.getIssuedTime(), event.getStartTime(), event.getEndTime());
	}
	
	protected void publicCancelToDRwebsite(Program program, Event event) {
		//delete event
		irrProgramManager.deleteEvent("TOU-BIP", "TOU-BIP", "ABANK", BIPEventUtil.getAllEventLocations(event), 
				event.getStartTime());
	}
	 
	@Override
	public void endEvent(String programName, String eventName, Date endTime){
		Event event = eventEAO.findEventWithParticipantsAndSignalsAndContacts(eventName);
		if(endTime==null){
			completeEventImmediately(event);
		}else{
			completeEvent(event,endTime);
		}
	}
	
	public void completeEventImmediately(Event event) {
		Date endTime = new Date();
		List<String> locations = new ArrayList<String>();
		try {
			locations=BIPEventUtil.getAllActiveEventLocations(event);
			//BIPEventUtil.updateEventLocationStr(event, locations, endTime);
			event.setEndTime(new Date());
			eventEAO.update(event);
		} catch (EntityNotFoundException e) {
			log.error("Error to update end time for event "+event,e);
		}
		super.completeEvent(event);
		irrProgramManager.endEvent("TOU-BIP", "TOU-BIP", "ABANK", locations,event.getStartTime(),new Date());
	}
	
	public void completeEvent(Event event, Date endTime) {
		List<String> locations = new ArrayList<String>();
		try {
			locations =BIPEventUtil.getAllActiveEventLocations(event);
			//BIPEventUtil.updateEventLocationStr(event, locations, endTime);
			event.setEndTime(endTime);
			eventEAO.update(event);
			irrProgramManager.endEvent("TOU-BIP", "TOU-BIP", "ABANK",locations,event.getStartTime(), endTime);
		} catch (EntityNotFoundException e) {
			log.error("Error to update end time for event "+event,e);
		}
	}
	

}
