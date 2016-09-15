/**
 * 
 */
package com.akuacom.pss2.history;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.jdbc.SQLBuilderException;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.client.ClientEAO;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantShedEntry;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramEAO;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.LogUtils;

/**
 * the class CustomerReportManagerBean
 *
 */
@Stateless
public class CustomerReportManagerBean implements CustomerReportManager.L, CustomerReportManager.R {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(CustomerReportManagerBean.class);

	@EJB
	ClientStatusGenEAO.L clientStatusEAO;
	@EJB
	HistoryEventParticipantGenEAO.L historyEventParticipantGenEAO;
	@EJB
	ClientEAO.L clientEAO;
	@EJB
	ProgramEAO.L programEAO;
	@EJB
	ParticipantEAO.L participantEAO;
	@EJB
	HistoryEventGenEAO.L historyEventEAO;
	@EJB
	Pss2SQLExecutor.L sqlExecutor;
	@EJB
	EventEAO.L eventEAO;

	@Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void reportOfflineStatus(String client_uuid, Date lastContact, String clientName){
		try {
			doReportStatus(client_uuid,clientName, lastContact, false );
		}catch(Exception e) {
			log.error("Failed to log offline status");
			log.debug(e);
		}
	}
	
	@Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void reportOnlineStatus(String client_uuid, Date time, Date lastContact, String clientName){
		try {
			doReportStatus(client_uuid, clientName,time,true);
		}catch(Exception e) {
			log.error("Failed to log online status");
			log.error(e);
		}
	}
	
	protected void doReportStatus(String client_uuid, String clientName,
					Date changeTime, /* status change time */
					boolean statusTobe /*online or off line*/) 	throws Exception{
		ClientStatus clientStatus=new ClientStatus();
		clientStatus.setClient(client_uuid);
		clientStatus.setClientName(clientName);
		clientStatus.setStatus(statusTobe);
		clientStatus.setStartTime(changeTime);
		List<ClientStatus> previous=clientStatusEAO.findOpenRecords(client_uuid);
		log.info("Inside online and offline report update method "+clientName);
		if(previous== null || previous.size()==0){
		//no open record, for example, legacy client, just open a new status record, no need to close previous one
			clientStatusEAO.create(clientStatus);
		
		}else if (previous.size()==1){
		//have one but only one open online record, e.g. end time is null
			ClientStatus toclose = previous.get(0);
			//make sure the open record status is on the contrary  
			log.info("open record status is"+toclose.isStatus()+"For client"+clientName);
			log.info("future record status is"+statusTobe+"For client"+clientName);
			if(toclose.isStatus() == !statusTobe){
				//close last  record
				toclose.setEndTime(changeTime);
				clientStatusEAO.merge(toclose);
				log.info("log client offline time to history table for client"+clientName);
				//open a new  record
				clientStatusEAO.create(clientStatus);
			}else{
				//the open record is the same status with status to be, 
				//it's not correct, but just do nothing for fault tolerance
			}
		}else{
			//more than one open records, not correct
			log.error("There are multi status record which are not closed. Client name: " + clientName);
			ClientStatus toclose = null;
			
			for(ClientStatus st:previous){
				//records are sorted by start time ascendantly 
				//the earliest record on the contrary should the one to be closed 
				if(st.isStatus()!=statusTobe && toclose==null)  toclose = st;
			}
			
			//found record to close
			if(toclose!=null){
				//close it
				toclose.setEndTime(changeTime);
				clientStatusEAO.merge(toclose);
				//open a new  record with status to be
				clientStatusEAO.create(clientStatus);
				//delete other open records
				for(ClientStatus contraty:previous){
					if(contraty!=toclose){
						clientStatusEAO.delete(contraty);
					}
				}
			//can not find open record
			}else{
				//keep the earliest record which status is status-to-be 
				for(int i = 1; i<previous.size();i++){
					clientStatusEAO.delete(previous.get(i));
				}
			}
		}
	}
	
	
	@Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void reportClientNonParticipation(String eventName, Map<String, 
			ClientParticipationStatus> partNonParticipation,
			Program program){
		try{
			doReportClientNonParticipation(eventName,partNonParticipation,program);
		}catch(Exception e){
        	log.error("failed to log the event participants which donot participate the event " + eventName);
        	log.debug(LogUtils.createExceptionLogEntry(null, null, e));
		}
	}
	
	protected void doReportClientNonParticipation(String eventName, Map<String, 
			ClientParticipationStatus> partNonParticipation,
			Program program){
		
		Collection<HistoryEventParticipant> entities = new HashSet<HistoryEventParticipant>();

        Iterator<String> partNames = partNonParticipation.keySet().iterator();
        try {
			//Event event = eventEAO.getByEventName(eventName);
		
	        while(partNames.hasNext()){
	        	String partName=partNames.next();
	        	int status=partNonParticipation.get(partName).getValue();
	        	
	        	Participant participant=participantEAO.getParticipant(partName);
	    		HistoryEventParticipant parentEventParticipant=new HistoryEventParticipant();
	    		parentEventParticipant.setEventName(eventName);
	    		parentEventParticipant.setParticipant(participant.getUUID());
	    		parentEventParticipant.setParticipantName(participant.getParticipantName());
	    		parentEventParticipant.setClient(participant.isClient());
	    		parentEventParticipant.setParticipation(status);
	    		entities.add(parentEventParticipant);
	        	
				List<Participant> clients=clientEAO.findClientsByParticipant(partName);
				for (Participant client: clients) {
		    		HistoryEventParticipant eventParticipant=new HistoryEventParticipant();
		    		eventParticipant.setEventName(eventName);
		    		eventParticipant.setParticipant(client.getUUID());
		    		eventParticipant.setParticipantName(client.getParticipantName());
		    		eventParticipant.setClient(client.isClient());
		    		eventParticipant.setParticipation(status);
		    		
		    		//calculateEstimatedShed();
		    		eventParticipant.setRegisteredShed(client.getShedPerHourKW());
					if (client.isClient()) {
						eventParticipant.setParent(client.getParent());
					}
		    		
		    		entities.add(eventParticipant);
				}
	        }
			historyEventParticipantGenEAO.create(entities);
        } catch (Exception e) {
			// TODO Auto-generated catch block
        	log.error("failed to log the event participants which donot participate the event " + eventName);
		}
	}

	@Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void reportEventHistory(Event event, boolean cancelled) {
		try {
			Program program = programEAO.getProgramWithParticipants(event.getProgramName());
			doReportEventHistory(event, cancelled, program);
		}catch(Exception e) {
			log.error("Failed to log event history");
			log.debug(e);
		}
		
	}
	
	@Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void reportEventHistory(Event event, boolean cancelled, Program program){
		try {
			doReportEventHistory(event, cancelled, program);
		}catch(Exception e) {
			log.error("Failed to log event history");
			log.debug(e);
		}
		
	}
	
	private void doRemoveTestEvent(Event event){
		List<HistoryEventParticipant> eps=historyEventParticipantGenEAO.findAllByProgramName("%"+TestProgram.PROGRAM_NAME+"%");
		try {
			if (eps!=null && eps.size()!=0)
				historyEventParticipantGenEAO.delete(eps);
		} catch (EntityNotFoundException e) {
		}
	}
	
	protected void doReportEventHistory(Event event, boolean cancelled, Program program) 
			throws AppServiceException, EntityNotFoundException, DuplicateKeyException {
		
			if (event.getProgramName().equals(TestProgram.PROGRAM_NAME)) {
				doRemoveTestEvent(event);
				return;
			}
				
			HistoryEvent historyEvent=new HistoryEvent();
			String eventName=event.getEventName();
			historyEvent.setEventName(eventName);
			
			historyEvent.setProgram(program.getUUID());
			
			historyEvent.setProgramName(program.getProgramName());
			historyEvent.setIssueTime(event.getIssuedTime());
			historyEvent.setStartTime(event.getStartTime());
			historyEvent.setEndTime(event.getEndTime());
			historyEvent.setScheduledStartTime(event.getStartTime());
			historyEvent.setScheduledEndTime(event.getEndTime());
			historyEvent.setCancelled(cancelled);
			
			int usageCount = 0;
			double usageRegShed = 0;
			double regShed = 0;
			HashSet<String> set = new HashSet<String>();
			for (ProgramParticipant pp : program.getProgramParticipants()) {
				if (pp.getParticipant().isClient()) {
					if(pp.getParticipant().getParent()!=null) set.add(pp.getParticipant().getParent());
					continue;
				}
				
				if (pp.getOptStatus().intValue() == 1) {
					continue;
				}
				
				//regShed += pp.getParticipant().getShedPerHourKW(); 
				regShed += calculateEstimatedShed(pp.getParticipant(),event);
				if (pp.getParticipant().getDataEnabler() != null 
						&& pp.getParticipant().getDataEnabler().booleanValue()) {
					usageCount++;
					//usageRegShed += pp.getParticipant().getShedPerHourKW(); 
					usageRegShed += calculateEstimatedShed(pp.getParticipant(),event);
				}
			}
			
			historyEvent.setNumProgramParticipants(set.size());
			historyEvent.setRegisteredShed(new Double(regShed));
			historyEvent.setNumUsageEnabled(new Integer(usageCount));
			historyEvent.setRegisteredUsageShed(new Double(usageRegShed));
			
			int status=ClientParticipationStatus.EVENT_COMPLETED.getValue();
			if (cancelled) {
				if (event.getEventStatus() == EventStatus.ACTIVE) {
					historyEvent.setStartTime(event.getStartTime());
					historyEvent.setEndTime(new Date());
					status = ClientParticipationStatus.ACTIVE_EVENT_CANCELLED.getValue();
				} else {
					historyEvent.setStartTime(null);
					historyEvent.setEndTime(null);
					status = ClientParticipationStatus.INACTIVE_EVENT_CANCELLED.getValue();
				}
			}
			
			Set<EventParticipant> parts=event.getEventParticipants();
			Set<HistoryEventParticipant> historyParts=new HashSet<HistoryEventParticipant>();
			
			//add offline per duration event
			long eventDuration=-1;
			if (status!=ClientParticipationStatus.INACTIVE_EVENT_CANCELLED.getValue())
				eventDuration=event.getEndTime().getTime()-event.getStartTime().getTime();
			//end
			
			for(EventParticipant part:parts){
				HistoryEventParticipant historyPart=new HistoryEventParticipant();
				String participant_uuid=part.getParticipant().getUUID();
				String participantName=part.getParticipant().getParticipantName();
				
				historyPart.setEventName(eventName);
				historyPart.setParticipant(participant_uuid);
				historyPart.setParticipantName(participantName);
				historyPart.setClient(part.getParticipant().isClient());
				
				if (part.getEventOptOut()==0) {
					historyPart.setParticipation(status);
					historyPart.setStartTime(historyEvent.getStartTime());
					if (part.getOptInTime()!=null) {
						historyPart.setStartTime(part.getOptInTime());
					}
					historyPart.setEndTime(historyEvent.getEndTime());
				} else if (part.getEventOptOut()==ClientParticipationStatus.ACTIVE_EVENT_OPT_OUT.getValue()){ 
					historyPart.setParticipation(part.getEventOptOut());
					historyPart.setStartTime(historyEvent.getStartTime());
					historyPart.setEndTime(part.getOptOutTime());
				} else {
					//client opt inactive event out -> start time & end time = null
					historyPart.setParticipation(part.getEventOptOut());
					historyPart.setStartTime(null);
					historyPart.setEndTime(null);
				}
								
				double registedShed = calculateEstimatedShed(part.getParticipant(), event);
				historyPart.setRegisteredShed(registedShed);
				//historyPart.setRegisteredShed(part.getParticipant().getShedPerHourKW());
				if (part.getParticipant().isClient()) {
					historyPart.setParent(part.getParticipant().getParent());
				}

				if (part.getParticipant().isClient() && status != ClientParticipationStatus.INACTIVE_EVENT_CANCELLED.getValue() &&
						historyPart.getParticipation().intValue() !=ClientParticipationStatus.INACTIVE_EVENT_OPT_OUT.getValue()) {
					List<TimeRange> offlinePeriods=new ArrayList<TimeRange>();
					offlinePeriods= getClientTimePeriods(part.getParticipant().getUUID(), false,
							historyPart.getStartTime(), historyPart.getEndTime());
					
					long offlineTime=getClientOfflineTime(offlinePeriods);
					double percetage=Double.longBitsToDouble(historyPart.getEndTime().getTime()-historyPart.getStartTime().getTime()-offlineTime)/
							Double.longBitsToDouble(historyEvent.getEndTime().getTime()-historyEvent.getStartTime().getTime());
					historyPart.setPercentage(percetage);
					if (percetage==0.0)
						historyPart.setParticipation(ClientParticipationStatus.CLIENT_OFFLINE.getValue());
					
					//add offline per duration event
					long offlineDurationEvent=getClientOfflineTime(
							getClientTimePeriods(historyPart.getParticipant(), false,
									event.getStartTime(), event.getEndTime()));
					double offlinePerEvent=Double.longBitsToDouble(offlineDurationEvent)/Double.longBitsToDouble(eventDuration);
					historyPart.setOfflinePerEvent(offlinePerEvent);
					//end

					
					Set<HistoryEventParticipantSignal> historySignals=new HashSet<HistoryEventParticipantSignal>();
					historySignals.addAll(toHistoryEventSignals(part.getSignals(), eventName, participant_uuid, participantName, historyPart.getEndTime().getTime()));
					historySignals.addAll(toHistoryEventSignals(event.getEventSignals(), eventName, participant_uuid, participantName, historyPart.getEndTime().getTime()));
					
					for (HistoryEventParticipantSignal historySignal:historySignals){
						historySignal.setEventParticipant(historyPart);
					}
					historyPart.setSignals(historySignals);
				}
				
				historyParts.add(historyPart);
			}
			
			for (HistoryEventParticipant part:historyParts){
				part.setEvent(historyEvent);
			}
			historyEvent.setEventParticipants(historyParts);
			historyEvent = historyEventEAO.create(historyEvent);
			
			updateProgramOptoutParticipants(historyEvent);
	}
	
	private void updateProgramOptoutParticipants(HistoryEvent event) throws EntityNotFoundException{
		List<HistoryEventParticipant> historyParts=historyEventParticipantGenEAO.findProgramOptoutParticipant(event.getEventName());
		if (historyParts!=null && historyParts.size()>0) {
			for (HistoryEventParticipant historyPart : historyParts) {
				if (historyPart.getClient() && 
						historyPart.getParticipation() == ClientParticipationStatus.ACTIVE_EVENT_OPT_OUT.getValue() &&
						historyPart.getEndTime() !=null) {
					double percetage = 0;
					long offlineTime=0;
					
					List<TimeRange> periods = getClientTimePeriods(historyPart.getParticipant(), false,
							historyPart.getStartTime(), historyPart.getEndTime());
					if (periods.size() > 0) {
						 offlineTime = getClientOfflineTime(periods);
					}
					
					percetage = Double.longBitsToDouble(historyPart.getEndTime().getTime()-historyPart.getStartTime().getTime()-offlineTime) / 
						Double.longBitsToDouble(event.getEndTime().getTime()-event.getStartTime().getTime());
					historyPart.setPercentage(percetage);
					if (percetage==0.0)
						historyPart.setParticipation(ClientParticipationStatus.CLIENT_OFFLINE.getValue());
					
					//add offlinePerEvent column for client status report -> %DurationEvent
					long offlineDurationEvent=getClientOfflineTime(
							getClientTimePeriods(historyPart.getParticipant(), false,
									event.getStartTime(), event.getEndTime()));
					double offlinePerEvent=Double.longBitsToDouble(offlineDurationEvent)/
						Double.longBitsToDouble(event.getEndTime().getTime()-event.getStartTime().getTime());
					historyPart.setOfflinePerEvent(offlinePerEvent);
					//end
				}

				historyPart.setEvent(event);
			}
			Set<HistoryEventParticipant> partSet = new HashSet<HistoryEventParticipant>();
			partSet.addAll(historyParts);
			event.setEventParticipants(partSet);
			historyEventEAO.update(event);
		}
	}
	
	public HistoryEvent getHistoryEventByEventName(String eventName) {
		return historyEventEAO.findByEventName(eventName);
	}
	
	@Override
	public List<Event> getEventListByParticipantAndProgram(List<String> participantNames, String[] porgams, Date start, Date end) {
		
		StringBuffer sqltemplate = new StringBuffer(); 
		sqltemplate.append(" SELECT eventName,startTime, endTime FROM history_event WHERE UUID IN (        \n");
		sqltemplate.append(" 	SELECT history_event_uuid FROM history_event_participant WHERE             \n");
		sqltemplate.append("  	participantName IN ${participantNames}  					       ) 	   \n");
		sqltemplate.append("  	and startTime >= ${param_start_time}  					         		   \n");
		sqltemplate.append("  	and startTime < ${param_end_time}  					         			   \n");
		if(porgams!=null&&porgams.length>0){
			sqltemplate.append("  	and programName not in${porgams} 					         		   \n");
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("participantNames", participantNames);
		params.put("param_start_time", start);
		params.put("param_end_time", end);
		if(porgams!=null&&porgams.length>0){
			params.put("porgams", porgams);
		}
		
		String sql = null;
		try {
			sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
			List<Event> results = null;
			results = sqlExecutor.doNativeQuery(sql,params,
					new ListConverter<Event>(new ColumnAsFeatureFactory<Event>(Event.class)));
			return results;
		} catch (Exception e) {
			throw new EJBException(e);
		}
		
    }
	
	@Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void reportEventParticipant(List<EventParticipant> reportList, Event event) {
		try{
			doReportEventParticipant(reportList,event);
		}catch(Exception e){
			log.error("Failed to log event opt-out participants");
			log.debug(e);
		}
	}
		
	protected void doReportEventParticipant(List<EventParticipant> reportList, Event event){
		long now=System.currentTimeMillis();
		String eventName=event.getEventName();
		boolean isActive=event.getEventStatus() == EventStatus.ACTIVE?true:false;
		double percetage=0;
		int status=ClientParticipationStatus.INACTIVE_EVENT_OPT_OUT.getValue();
		if (isActive) {
			status=ClientParticipationStatus.ACTIVE_EVENT_OPT_OUT.getValue();
		}
		Set<HistoryEventParticipant> historyParts=new HashSet<HistoryEventParticipant>();
		for (EventParticipant ep: reportList) {
			HistoryEventParticipant part=new HistoryEventParticipant();
			part.setEventName(eventName);
			
			String participant_uuid=ep.getParticipant().getUUID();
			String participantName=ep.getParticipant().getParticipantName();
			part.setParticipant(participant_uuid);
			part.setParticipantName(participantName);
			part.setClient(ep.getParticipant().isClient());
			part.setParticipation(status);
			part.setPercentage(percetage);
			double registedShed = calculateEstimatedShed(ep.getParticipant(), event);
			part.setRegisteredShed(registedShed);
			//part.setRegisteredShed(ep.getParticipant().getShedPerHourKW());
			if (ep.getParticipant().isClient()) {
				part.setParent(ep.getParticipant().getParent());
			}
			
			if (isActive) {
				part.setStartTime(event.getStartTime());
				part.setEndTime(new Date(now));
			}else{
				part.setStartTime(null);
				part.setEndTime(null);
			}
			
			if (isActive && ep.getParticipant().isClient()) {
				Set<HistoryEventParticipantSignal> historySignals = new HashSet<HistoryEventParticipantSignal>();

				historySignals.addAll(toHistoryEventSignals(
						ep.getSignals(), eventName, participant_uuid,
						participantName, now));

				historySignals.addAll(toHistoryEventSignals(
						event.getEventSignals(), eventName,
						participant_uuid, participantName, now));

				for (HistoryEventParticipantSignal historySignal : historySignals) {
					historySignal.setEventParticipant(part);
				}
				part.setSignals(historySignals);
			}
			historyParts.add(part);
		}
		
		historyEventParticipantGenEAO.create(historyParts);
	}
	
	private Set<HistoryEventParticipantSignal> toHistoryEventSignals(
			Set<? extends Signal> signals, String eventName,
			String participant_uuid, String participantName, long now) {
		Set<HistoryEventParticipantSignal> historySignals = new HashSet<HistoryEventParticipantSignal>();
		
		for (Signal signal : signals) {
			String signalName = signal.getSignalDef().getSignalName();
			
			// sort the signal time to get the signal end time
			TreeMap<Date, String> signalMap=new TreeMap<Date, String>();
			for (SignalEntry entry : signal.getSignalEntries()) {
				if (entry.getTime().getTime() > now)
					continue;
				
				signalMap.put(entry.getTime(), entry.getValueAsString());
			}
			
			//get client online duration, save signal only when the client is online 
			if (signalMap.size()>0) {
				List<TimeRange> onlineRange=getClientTimePeriods(participant_uuid, true, signalMap.firstKey(),new Date(now));
				List<Date> signalTimeList=new ArrayList<Date>();
				for (Date key:signalMap.keySet()) {
					signalTimeList.add(key);
				}
				signalTimeList.add(new Date(now));
				
				for (int i=0;i<signalTimeList.size()-1;i++) {
					List<TimeRange> signalTimes=getValidSignalTime(signalTimeList.get(i), signalTimeList.get(i+1),onlineRange);
					for (TimeRange signalTime:signalTimes){
						
						HistoryEventParticipantSignal historySignal = new HistoryEventParticipantSignal();
						historySignal.setEventName(eventName);
						historySignal.setParticipant(participant_uuid);
						historySignal.setParticipantName(participantName);
						historySignal.setSignalName(signalName);
						historySignal.setSignalTime(signalTime.getStart());
						historySignal.setSignalEndTime(signalTime.getEnd());
						historySignal.setSignalValue(signalMap.get(signalTimeList.get(i)));
						historySignals.add(historySignal);
					}
					
				}
			}
		}

		return historySignals;
	}
	
	//get signal valid duration
	private List<TimeRange> getValidSignalTime(Date start, Date end, List<TimeRange> onlineRange){
		List<TimeRange> ranges=new ArrayList<TimeRange>();
		
		for (TimeRange range:onlineRange) {
			if (start.compareTo(range.getEnd())>0 || end.compareTo(range.getStart())<0)
				continue;
			
			TimeRange validSignalTime=new TimeRange();
			if (start.compareTo(range.getStart())>=0)
				validSignalTime.setStart(start);
			else
				validSignalTime.setStart(range.getStart());
			
			if (end.compareTo(range.getEnd())<=0)
				validSignalTime.setEnd(end);
			else
				validSignalTime.setEnd(range.getEnd());
			
			ranges.add(validSignalTime);				
		}
		
		return ranges;
	}
	
	private long getClientOfflineTime(List<TimeRange> periods){
		long total=0;
		for (TimeRange period:periods) {
			total=total+(period.getEnd().getTime() - period.getStart().getTime());
		}
		
		return total;
	}
	
	private List<TimeRange> getClientTimePeriods(String client_uuid, boolean isOnline, Date start, Date end){
		List<TimeRange> periods=new ArrayList<TimeRange>();
		if (start!=null && end!=null) {
			List<ClientStatus> clientStatusList= clientStatusEAO.findClientStatus(client_uuid, isOnline, start, end);
			
			for (ClientStatus clientStatus:clientStatusList) {
				long msStart=Math.max(clientStatus.getStartTime().getTime(), start.getTime());
				long msEnd=clientStatus.getEndTime()==null? end.getTime():Math.min(clientStatus.getEndTime().getTime(), end.getTime());
				
				if (msStart !=msEnd)
					periods.add(new TimeRange(new Date(msStart), new Date(msEnd)));
			}
		}
		return periods;
	}
	
	class TimeRange {
		Date start;
		Date end;
		
		public TimeRange(){}
		public TimeRange(Date start, Date end){
			this.start=start;
			this.end=end;
		}
		
		public Date getStart() {
			return start;
		}
		public void setStart(Date start) {
			this.start = start;
		}
		public Date getEnd() {
			return end;
		}
		public void setEnd(Date end) {
			this.end = end;
		}
	}

	
	public double calculateEstimatedShed(Participant participant,Event event){
		double result = 0;
		if(event==null||participant==null){
			//OR THROW EXCEPTION
			return 0;
		}
		if(participant!=null){
			if(participant.getShedType()!=null){
				if(participant.getShedType().equalsIgnoreCase("SIMPLE")){
					result = participant.getShedPerHourKW();
				}else if(participant.getShedType().equalsIgnoreCase("ADVANCED")){
					participant = clientEAO.findParticipantWithSheds(participant.getParticipantName());
					List<ParticipantShedEntry> shedEntries = new ArrayList<ParticipantShedEntry>(participant.getShedEntries());
					//------------------------ADVANCED SHED COMPUTING BEGIN-----------------------------
					Calendar calS=Calendar.getInstance();
					calS.setTime(event.getStartTime());
					
					Calendar calE=Calendar.getInstance();
					calE.setTime(event.getEndTime());
					double totalHours=0;
					double totalSheds=0;
					double hourStart = calS.get(Calendar.HOUR_OF_DAY);
					double hourEnd = calE.get(Calendar.HOUR_OF_DAY);
					double minStart = calS.get(Calendar.MINUTE);
					double minEnd = calE.get(Calendar.MINUTE);
					for(ParticipantShedEntry entry:shedEntries){
						int hourIndex = entry.getHourIndex();
						double value = entry.getValue();
						if(hourIndex>=hourStart&&hourIndex<=hourEnd){
							if(hourIndex==hourStart&&hourIndex==hourEnd){
								
								return value;
							}else if(hourIndex==hourStart){
								totalSheds+=(60-minStart)/60*value;
								totalHours+=(60-minStart)/60;
							}else if(hourIndex==hourEnd){
								totalSheds+=(minEnd/60)*value;
								totalHours+=minEnd/60;
							}else{
								totalSheds+=value;
								totalHours++;
							}
						}
					}
					if(totalHours!=0){
						result = totalSheds/totalHours;	
					}
					java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
					result = Double.parseDouble(df.format(result));
					//------------------------ADVANCED SHED COMPUTING END-----------------------------
				}
			}
		}
		return result;
	}
	
	
	public double calcAvailableSheds(Participant p,Event event,List<ProgramParticipant> ppList) {

		// 1 calculate shed 
		double result = 0;
		ArrayList<Participant> clientList = new ArrayList<Participant>();
		if(event==null||p==null||ppList==null){
			//OR THROW EXCEPTION
			return 0;
		}
		double estimatedShed = calculateEstimatedShed(p,event);
		// 2 calculate available shed
		int c = 0;
		for (ProgramParticipant client : ppList) {
			if (client.getParticipant().getParent() != null && client.getParticipant().getParent().equals(p.getParticipantName())) {
				clientList.add(client.getParticipant());
				if (client.getParticipant().getStatus() == 0) {
					c++;
				}
			}
		}
		if (clientList.size() != 0 && c !=  0) {
			//result = ((estimatedShed / clientList.size()) * c);
			result = calcAvailableSheds(estimatedShed,c,clientList.size());
		}
		
		
		
		return result;
	}
	public double calcAvailableSheds(double estimateShed,double validClients,double clients) {
		double result = 0;
		result = ((estimateShed / clients) * validClients);
		return result;
	}


	/**
	 * @return the eventEAO
	 */
	public EventEAO.L getEventEAO() {
		return eventEAO;
	}

	/**
	 * @param eventEAO the eventEAO to set
	 */
	public void setEventEAO(EventEAO.L eventEAO) {
		this.eventEAO = eventEAO;
	}
	
	
}
