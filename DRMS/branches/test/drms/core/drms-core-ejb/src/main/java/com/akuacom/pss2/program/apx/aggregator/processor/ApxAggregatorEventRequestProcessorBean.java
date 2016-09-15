package com.akuacom.pss2.program.apx.aggregator.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.apx.ApxAggregator;
import com.akuacom.pss2.program.apx.aggregator.eao.ApxAggregatorEAOManager;
import com.akuacom.pss2.program.apx.common.ApxManagerHelper;
import com.akuacom.pss2.program.apx.parser.APXXmlParser;
import com.akuacom.pss2.program.sceftp.MessageUtil;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;


/**
 * @author Ram Pandey
 */
@Stateless
public class ApxAggregatorEventRequestProcessorBean implements ApxAggregatorEventRequestProcessor.L,ApxAggregatorEventRequestProcessor.R {

	private final String CLIENTID_AGGREGATORID_SEPERATOR="|";
	private final String CBP_PROGRAM_CLASS = "CBP";
	public  final String LOG_CATEGORY="APX Event Creation";
	
	private Map<String,Set<String>> aggregatorAccNoParticipantAccNoListMap = new HashMap<String,Set<String>>();
	private List<ProgramValidationMessage> warnings = new ArrayList<ProgramValidationMessage>();
	
	@EJB 
	private ParticipantManager.L partManager;
	@EJB
	private SystemManager.L systemManager;
	@EJB
	private ApxManagerHelper.L helperBean;
	@EJB
	private ProgramManager.L programManager;
	@EJB
	private ApxAggregatorEAOManager.L apxAggregatorEaoManager;
	@EJB
	private EventEAO.L eventEao;
	
	@Override
	public void processApxQueueMessage(APXXmlParser apxXmlParser) {
		try {
			if(null ==apxXmlParser ){
				throw new Exception("Apx xml parser is null");
			}
			createEventForApx(apxXmlParser);
		} catch (Exception e) {
			e.printStackTrace();
			helperBean.sendExceptionNotifications(MessageUtil.getErrorMessage(e), null);
		}
		
	}
	private void createEventForApx(APXXmlParser xmlParser) throws Exception{
		clearWarnings();
		fillAggregatorIdClientIdListMap(xmlParser);
		Event event = createEventObj(xmlParser);
		List<EventParticipant> eventParticipants = createParticipants(event);
		event.setParticipants(eventParticipants);
		createEvent(event);   
		updateApxAggregatorTable(event);
		//helperBean.sendEventCreationNotifications(event,this.warnings);
	}
	private List<EventParticipant> createParticipants(Event event){
		List<EventParticipant> eventParticipantList = new ArrayList<EventParticipant>();
		List<String> clientAccNumberList = new ArrayList<String>();
		List<Participant> aggregatorList = new ArrayList<Participant>();
		String programName = event.getProgramName();
		List<String> aggAccNumberList = new ArrayList<String>(); 
		aggAccNumberList.addAll(aggregatorAccNoParticipantAccNoListMap.keySet());
		Participant aggregator = null;
		for(String aggAccNumber : aggAccNumberList){
			clientAccNumberList.addAll(aggregatorAccNoParticipantAccNoListMap.get(aggAccNumber));
			aggregator =getAggregator(aggAccNumber,programName);
			if(null != aggregator && isValidApxAggregator(aggregator)){
			aggregatorList.add(aggregator);
			}
			else{
				aggregatorAccNoParticipantAccNoListMap.remove(aggAccNumber);
			}
		}
		eventParticipantList.addAll(createEventParticipantForParticipant(clientAccNumberList,event));
		eventParticipantList.addAll(createEventParticipantForAggregator(aggregatorList , event));
		return eventParticipantList;
	}
	
	private List<EventParticipant> createEventParticipantForAggregator(List<Participant> aggregatorList , Event event){
		List<EventParticipant> eventParticipantList = new ArrayList<EventParticipant>();
		EventParticipant eventParticipant = null;
		for(Participant participant : aggregatorList){
			eventParticipant = createEventParticipant(participant,event);
			eventParticipant.setAggregator(true);
			eventParticipantList.add(eventParticipant);
		}
		return eventParticipantList;
		
	}
	private List<EventParticipant> createEventParticipantForParticipant(List<String> participantAccNumberList , Event event){
		List<EventParticipant> eventParticipantList = new ArrayList<EventParticipant>();
		List<Participant> participants = getPaticipants(participantAccNumberList,event.getProgramName());
		for(Participant participant : participants){
			eventParticipantList.add(createEventParticipant(participant,event));
		}
		return eventParticipantList;
	}
	private EventParticipant createEventParticipant(Participant participant , Event event){
		EventParticipant ep=new EventParticipant();
		ep.setParticipant(participant);
		ep.setEvent(event);
		return ep;
	}
	private List<Participant> getPaticipants(List<String> participantAccNumberList , String programName){
		if(isSecondaryAccountEnabled(programName)){
			return getParticipantBySecondaryAccNumber(participantAccNumberList,programName);
		}
		return getParticipantByAccNumber(participantAccNumberList,programName);
	}
	private List<Participant> getParticipantByAccNumber(List<String> participantAccNumberList , String programNumber){
		List<Participant> participants = new ArrayList<Participant>();
		Participant participant = null;
		for(String participantAccNumber : participantAccNumberList ){
			participant = getParticipantByAccNumber(participantAccNumber,programNumber);
			if(null == participant){
				setParticipantNotFoundWarning(participantAccNumber,programNumber);
				continue;
			}
			participants.add(participant);
		}
		return participants;
	}
	private List<Participant> getParticipantBySecondaryAccNumber(List<String> participantAccNumberList , String programNumber){
		List<Participant> participants = new ArrayList<Participant>();
		Participant participant = null;
		for(String clientAccNumber : participantAccNumberList ){
			participant = getParticipantBySecondaryAccNumber(clientAccNumber,programNumber);
			if(null == participant){
				setParticipantNotFoundWarning(clientAccNumber,programNumber);
				continue;
			}
			participants.add(participant);
		}
		return participants;
	}
	private Participant getParticipantByAccNumber(String participantAccNumber , String programName){
		List<String> accountIDs=new ArrayList<String>();
    	accountIDs.add(participantAccNumber);
    	List<Participant> participants = partManager.getParticipantsByAccounts(accountIDs);
    	return filterParticipantOnProgram(participants,programName);
	}
	private Participant getParticipantBySecondaryAccNumber(String clientAccNumber , String programName){
		List<String> accountIDs=new ArrayList<String>();
    	accountIDs.add(clientAccNumber);
    	List<Participant> participants = partManager.getParticipantsBySecondaryAccounts(accountIDs);
		return filterParticipantOnProgram(participants,programName);
	}
	private Participant filterParticipantOnProgram(List<Participant> participants , String programName){
		for (Participant part : participants) {
			List<String> programNames=partManager.getProgramsForParticipant(part.getParticipantName(), false);
			if (programNames.contains(programName)){
				return part;
			}
		}
		return null;
		
	}
	private Participant getAggregator(String aggregatorAccNumber , String programName){
		if(isSecondaryAccountEnabled(programName)){
			return getParticipantBySecondaryAccNumber(aggregatorAccNumber, programName);
		}
		return getParticipantByAccNumber(aggregatorAccNumber, programName);
	}
	private boolean isValidApxAggregator(Participant part){
		if(null == part){
			return false;
		}
		boolean status = true;
		String participantName = part.getParticipantName();
		List<String> clients = this.partManager.getClientNamesByParticipant(participantName);
		if(clients.isEmpty()){
			status = false;
			ProgramValidationMessage warning = new ProgramValidationMessage();
			warning.setParameterName("Invalid APX Aggregator");
			warning.setDescription(participantName+ " is not valid apx aggregator since it does not have any client");
			this.warnings.add(warning);
		}
		return status;
	}
	private void updateApxAggregatorTable(Event event) throws Exception{
		if(aggregatorAccNoParticipantAccNoListMap.isEmpty()){
			return;
		}
		String eventId = event.getEventName();
		Date startTime = event.getStartTime();
		Date endTime = event.getEndTime();
		Date issueTime = event.getIssuedTime();
		String uuid = getApxEventUuid(eventId);
		ApxAggregator agg = null;
		String clientIdMappedWithEp = "Unknown";
		for(String aggregatorAcc :aggregatorAccNoParticipantAccNoListMap.keySet() ){
			Participant part = getAggregator(aggregatorAcc, event.getProgramName());
			clientIdMappedWithEp = this.partManager.getClientNamesByParticipant(part.getParticipantName()).get(0);
			agg = new ApxAggregator();
			agg.setAggregatorName(aggregatorAcc);
			agg.setAggregatorAccountNumber(aggregatorAcc);
			agg.setApxEventStartTime(startTime);
			agg.setApxEventEndTime(endTime);
			agg.setApxEventIssueTime(issueTime);
			agg.setApxEventId(eventId);
			agg.setAggregatorClientIdMappedWithEndpoint(clientIdMappedWithEp);
			agg.setAggregatorResources(aggregatorAccNoParticipantAccNoListMap.get(aggregatorAcc));
			agg.setApxEventUuid(uuid);
			apxAggregatorEaoManager.createApxAggregator(agg);
		}
			
		
		
	}
	private boolean isSecondaryAccountEnabled(String programName){
		SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
		PSS2Features features = systemManager.getPss2Features();
		boolean secondaryAccountEnabled = features.isSecondaryAccountEnabled();
		//secondaryAccountEnabled =  CBP_PROGRAM_CLASS.equalsIgnoreCase(programName)|| secondaryAccountEnabled;
		return secondaryAccountEnabled;
	}
	
	private Event createEventObj(APXXmlParser xmlParser){
		String utilityProgramName = xmlParser.getProgramName();
		Program program=programManager.getProgramFromUtilityProgramName(utilityProgramName);
		Event event=new Event();
		event.setProgramName(program.getProgramName());
		event.setEventName(xmlParser.getEventName());
		event.setStartTime(xmlParser.getEventStartTime());
		event.setEndTime(xmlParser.getEventEndTime());
		event.setIssuedTime(new Date());
		event.setReceivedTime(new Date());
		event.setLocations(xmlParser.getLocations());
		event.setMessage("apxservice");
		return event;
	}
	
	private void fillAggregatorIdClientIdListMap(APXXmlParser xmlParser){
		aggregatorAccNoParticipantAccNoListMap.clear();
		List<String> accountNumberList = xmlParser.getAccountNumbers();
		for(String accountNumber : accountNumberList){
			
			int seperatorIndex = accountNumber.indexOf(CLIENTID_AGGREGATORID_SEPERATOR);
			if(-1 ==seperatorIndex ){
				String aggregatorAccNumber = "";
				Set<String> clientAccNumberList = new TreeSet<String>();
				String clientAccNumber = accountNumber;
				if(aggregatorAccNoParticipantAccNoListMap.containsKey(aggregatorAccNumber)){
					aggregatorAccNoParticipantAccNoListMap.get(aggregatorAccNumber).add(clientAccNumber);
					continue;
				}
				clientAccNumberList.add(clientAccNumber);
				aggregatorAccNoParticipantAccNoListMap.put(aggregatorAccNumber, clientAccNumberList);
				
			} else {
				String aggregatorAccNumber = accountNumber.substring(seperatorIndex+1);
				String clientAccNumber = accountNumber.substring(0,seperatorIndex);
				if(aggregatorAccNoParticipantAccNoListMap.containsKey(aggregatorAccNumber)){
					aggregatorAccNoParticipantAccNoListMap.get(aggregatorAccNumber).add(clientAccNumber);
					continue;
				}
				Set<String> clientAccNumberList = new TreeSet<String>();
				clientAccNumberList.add(clientAccNumber);
				aggregatorAccNoParticipantAccNoListMap.put(aggregatorAccNumber, clientAccNumberList);
			}
		}
		
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Collection<String> createEvent(Event event) {
		ProgramEJB program = systemManager.lookupProgramBean(event.getProgramName());
		return program.createEvent(event.getProgramName(), event, null);
	}
	private void setParticipantNotFoundWarning(String participantAccNumber , String programName){
		ProgramValidationMessage warning = new ProgramValidationMessage();
		StringBuffer desc = new StringBuffer();
		desc.append("Participant with account number ");
		desc.append(participantAccNumber);
		desc.append(" not found or not in program ");
		desc.append(programName);
		warning.setDescription(desc.toString());
		warning.setParameterName("AccountNumberError");
		this.warnings.add(warning);
	}
	private String getApxEventUuid(String apxEventId) throws Exception{
		Event apxEvent = eventEao.getByEventName(apxEventId);
		return apxEvent.getUUID();
	}
	private void clearWarnings(){
		this.warnings.clear();
	}

}
