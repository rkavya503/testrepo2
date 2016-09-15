package com.honeywell.dras.ge;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.ge.GeConfiguration;
import com.akuacom.pss2.ge.GeInterfaceManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.sceftp.MessageUtil;
import com.honeywell.dras.helpers.SseConstants;
@WebService(targetNamespace = "ENMAC/ADR", name = "ADRSoap")
public class EventSOAPWS4GEImpl implements ADRSoap {
	
	private ThreadLocal<WebServiceContext> threadSafeContext = new ThreadLocal<WebServiceContext>();
    private static final Logger log = Logger.getLogger(EventSOAPWS4GEImpl.class);
    private EventManager eventManager;
    private ProgramManager programManager;
    private ParticipantManager participantManager;
    private GeInterfaceManager geManager;
    private String errorLog="";
    @Resource
    public void setContext(WebServiceContext context){
    	threadSafeContext.set(context);
    }

	@Override
	@WebResult(name = "AdrEventResponseStc", targetNamespace = "ENMAC/ADR")
	@RequestWrapper(localName = "SubmitADREvent", targetNamespace = "ENMAC/ADR", className = "com.honeywell.dras.ge.SubmitADREvent")
	@WebMethod(operationName = "SubmitADREvent", action = "ENMAC/ADR/SubmitADREvent")
	@ResponseWrapper(localName = "SubmitADREventResponse", targetNamespace = "ENMAC/ADR", className = "com.honeywell.dras.ge.SubmitADREventResponse")
	public AdrEventResponseStc submitADREvent(
			@WebParam(name = "AdrEventStc", targetNamespace = "ENMAC/ADR") AdrEventStc adrEventStc) {		
		errorLog="";
		AdrEventResponseStc response = new AdrEventResponseStc();		
		//try catch
		try{
			EventManager eventManager = getEventManager();
			if(eventManager!=null){
				Event event = buildEvent(adrEventStc);
				if(errorLog!=null&&(!errorLog.equalsIgnoreCase(""))) {
					response.setStatus(1);
					response.setErrorMessage(errorLog);
					return response;										
				}
				event = buildEventParticipant(event,adrEventStc);
				String programName = event.getProgramName();
				if(programName!=null&&(!"".equalsIgnoreCase(programName))&&(event!=null)){
					eventManager.createEvent(programName,event);	
				}
			}
			response.setStatus(0);
		}catch(Exception e){
			errorLog = MessageUtil.getErrorMessage(e);
			log.debug(errorLog);
		}
		if(errorLog!=null&&(!errorLog.equalsIgnoreCase(""))){
			response.setStatus(1);		
			response.setErrorMessage(errorLog);
		}			
		return response;
	}
	
	private EventManager getEventManager(){
		if(eventManager==null){
			eventManager = EJBFactory.getBean(EventManager.class);
		}
		return eventManager;
	}
	public ProgramManager getProgramManager(){
		if(programManager==null){
			programManager=EJBFactory.getBean(ProgramManager.class);
		}
		return programManager;
	}
	private ParticipantManager getParticipantManager(){
		if(participantManager==null){
			participantManager = EJBFactory.getBean(ParticipantManager.class);
		}
		return participantManager;
	}
	private String getProgramName(){
		if(geManager==null){
			geManager = EJB3Factory.getLocalBean(GeInterfaceManager.class);
		}
		
		GeConfiguration conf = geManager.getGeConfiguration();
		String programName = conf.getProgramName();
		return programName;
	}
	private Event buildEvent(AdrEventStc adrEventStc) {
		Event event= null; 
		SimpleDateFormat dateFormat = new SimpleDateFormat(SseConstants.DATE_FORMAT);
		isEventExist(adrEventStc.getEventName());
		GregorianCalendar calender = adrEventStc.getStartTime().toGregorianCalendar();
		Calendar current = Calendar.getInstance();
		String currentDf = dateFormat.format(current.getTime());
		Date startTime = calender.getTime();
		String startTimeDf = dateFormat.format(startTime);
		Date endTime = adrEventStc.getEndTime().toGregorianCalendar().getTime();
		
		if (endTime.after(startTime)) {
			if( currentDf.equals(startTimeDf) ) {
				event = buildDOEvent(adrEventStc, current.getTime(), startTime);
			} else if (startTime.after(current.getTime())){
				event = buildDayAheadEvent(adrEventStc, current.getTime(), startTime);
			} else {
				errorLog = "Event start time should be current or future date.";
				log.error("Error creating buildEvent ## " + errorLog);
			}
		} else {
			errorLog = "Event end time should be after start time.";
			log.error("Error creating buildEvent ## " + errorLog);
		}
		return event;
	}
	
	private Event buildDOEvent(AdrEventStc adrEventStc, Date current, Date startTime) {
		if ( startTime.after(current) ) {
			current = startTime;
		}
		Program program = null;
		String programName = null;
		List<Program> programs = getProgramManager().getAllPrograms();
		Date endTime = adrEventStc.getEndTime().toGregorianCalendar().getTime();
		isEventExist(adrEventStc.getEventName());
		Iterator<Program> iter = programs.iterator();
		while(iter.hasNext()) {
			program = iter.next();
			String tempProgramName = program.getProgramName();
			if ( tempProgramName.contains(SseConstants.SSE_DO_PROG_NAME) ) {
				programName = tempProgramName;
			}
		}
		ProgramEJB programEJB = getProgramManager().lookupProgramBean(programName);
		Event event=programEJB.newProgramEvent();
		event.setStartTime(current);
		event.setIssuedTime(current);
		event.setReceivedTime(current);
		event.setEndTime(endTime);
		event.setEventName(adrEventStc.getEventName());
		event.setManual(true);
		event.setShedAmount(adrEventStc.getShedAmount());
		event.setProgramName(programName);
		return event;
	}
	
	private Event buildDayAheadEvent(AdrEventStc adrEventStc, Date current, Date startTime) {
		ProgramEJB programEJB = getProgramManager().lookupProgramBean(getProgramName());
		Event event=programEJB.newProgramEvent();
		isEventExist(adrEventStc.getEventName());
		Date endTime = adrEventStc.getEndTime().toGregorianCalendar().getTime();	
		event.setStartTime(startTime);
		event.setIssuedTime(current);
		event.setReceivedTime(current);
		event.setEndTime(endTime);
		event.setEventName(adrEventStc.getEventName());
		event.setManual(true);
		event.setProgramName(getProgramName());
		event.setShedAmount(adrEventStc.getShedAmount());
		return event;
	}
	
	private Event buildEventParticipant(Event event,AdrEventStc adrEventStc){
		List<String> accountNumberList = adrEventStc.getADRSite();
		List<Participant> participantList=getParticipantManager().findParticipantsWithEventParticipantsByAccounts(accountNumberList);
		for(Participant participant:participantList){
			if(participant!=null){
				EventParticipant eventParticipant = new EventParticipant();
				eventParticipant.setEvent(event);
				event.getEventParticipants().add(eventParticipant);
				eventParticipant.setParticipant(participant);
				participant.getEventParticipants().add(eventParticipant);
			}else{
				//log.info("Webservice[create GE event]:account number["+accountNumber+"]not exist in the DRAS.");
			}
		}
		
		return event;
	}
	/*private Date parseDate(String input){
		Date date = new Date();
		try{
			String part1 = input.substring(0,input.length()-6);
			String part2 = input.substring(input.length()-6);
			part2=part2.replaceAll(":", "");
			input=part1+part2;
			date = DateUtil.parse(input, "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");
		}catch(Exception e){
			errorLog = "Time input is not a valid value.";
		}
		return date;
	}*/
	/*private double parseShedAmount(String f){
		try{
			//change to double
			double shedAmount = Double.parseDouble(f);
			return shedAmount;
		}catch(Exception e){
			errorLog = "Shed amount is not a valid value.";
		}
		return 0;
	}*/
	
	private boolean isEventExist(String eventName){
		Event event = getEventManager().getEventOnly(eventName);
		if(event==null){
			return false;
		}else{
			errorLog = "Event name must be unique and cannot already exist in the DRAS.";
			return true;
		}
	}	
   
}
