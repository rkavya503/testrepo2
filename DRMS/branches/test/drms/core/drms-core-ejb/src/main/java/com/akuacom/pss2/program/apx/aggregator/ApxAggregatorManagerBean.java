package com.akuacom.pss2.program.apx.aggregator;



import javax.ejb.EJB;
import javax.ejb.Stateless;


import com.akuacom.pss2.program.apx.common.ApxConstant;
import com.akuacom.pss2.program.apx.common.ApxManagerHelper;
import com.akuacom.pss2.program.apx.parser.APXXmlParser;
import com.akuacom.pss2.program.apx.queue.ApxEventRequestQueue;
import com.akuacom.pss2.program.apx.queue.ApxQueueData;
import com.akuacom.pss2.program.apx.validator.ApxRequestValidator;
import com.akuacom.pss2.program.sceftp.MessageUtil;

/**
 * 
 * @author Ram Pandey
 *
 */

@Stateless
public class ApxAggregatorManagerBean implements ApxAggregatorManager.L,ApxAggregatorManager.R {
	
	public static final String LOG_CATEGORY="APX Event Creation";
	
	
	@EJB
	private ApxRequestValidator.L validator;
	@EJB
	private ApxEventRequestQueue.L queue;
	@EJB
	private ApxManagerHelper.L helperBean;
	
	public void processApxRequest(APXXmlParser xmlParser) throws Exception{
		try {
			validator.validateApxRequest(xmlParser);
			//createEventForApx(xmlParser);
			putApxEventRequestInQueue(xmlParser);
		} catch (Exception e) {
			e.printStackTrace();
			helperBean.sendExceptionNotifications(MessageUtil.getErrorMessage(e), null);
			throw e;
		}
	}
	private void putApxEventRequestInQueue(APXXmlParser xmlParser) throws Exception{
		ApxQueueData data = new ApxQueueData();
		data.setParser(xmlParser);
		data.setApxQueueMsgProcessorType(ApxConstant.APX_AGGREGATOR);
		queue.apxMessageDispatch(data);
		
	}
	/*private void createEventForApx(APXXmlParser xmlParser){
		clearWarnings();
		fillAggregatorIdClientIdListMap(xmlParser);
		Event event = createEventObj(xmlParser);
		List<EventParticipant> eventParticipants = createParticipants(event);
		event.setParticipants(eventParticipants);
		createEvent(event);    
		sendEventCreationNotifications(event,this.warnings);
	}
	private List<EventParticipant> createParticipants(Event event){
		List<EventParticipant> eventParticipantList = new ArrayList<EventParticipant>();
		List<String> clientAccNumberList = new ArrayList<String>();
		List<Participant> aggregatorList = new ArrayList<Participant>();
		String programName = event.getProgramName();
		Set<String> aggAccNumberList = aggregatorAccNoClientAccNoListMap.keySet();
		Participant aggregator = null;
		for(String aggAccNumber : aggAccNumberList){
			aggregator =getParticipantWhoIsAggregatorByAccNumber(aggAccNumber,programName);
			if(null == aggregator){
				continue;
			}
			aggregatorList.add(aggregator);
			clientAccNumberList.addAll(aggregatorAccNoClientAccNoListMap.get(aggAccNumber));
		}
		eventParticipantList.addAll(createEventParticipantForClient(clientAccNumberList,event));
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
	private List<EventParticipant> createEventParticipantForClient(List<String> clientAccNumberList , Event event){
		List<EventParticipant> eventParticipantList = new ArrayList<EventParticipant>();
		List<Participant> clients = getPaticipantsWhoIsClient(clientAccNumberList,event.getProgramName());
		for(Participant participant : clients){
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
	private List<Participant> getPaticipantsWhoIsClient(List<String> clientAccNumberList , String programName){
		if(isSecondaryAccountEnabled(programName)){
			return getParticipantBySecondaryAccNumberWhoIsClient(clientAccNumberList,programName);
		}
		return getParticipantByAccNumberWhoIsClient(clientAccNumberList,programName);
	}
	private List<Participant> getParticipantByAccNumberWhoIsClient(List<String> clientAccNumberList , String programNumber){
		List<Participant> clients = new ArrayList<Participant>();
		Participant participant = null;
		for(String clientAccNumber : clientAccNumberList ){
			participant = getParticipantByAccNumberWhoIsClient(clientAccNumber,programNumber);
			if(null == participant){
				setParticipantNotFoundWarning(clientAccNumber,programNumber);
				continue;
			}
			clients.add(participant);
		}
		return clients;
	}
	private List<Participant> getParticipantBySecondaryAccNumberWhoIsClient(List<String> clientAccNumberList , String programNumber){
		List<Participant> clients = new ArrayList<Participant>();
		Participant participant = null;
		for(String clientAccNumber : clientAccNumberList ){
			participant = getParticipantBySecondaryAccNumberWhoIsClient(clientAccNumber,programNumber);
			if(null == participant){
				setParticipantNotFoundWarning(clientAccNumber,programNumber);
				continue;
			}
			clients.add(participant);
		}
		return clients;
	}
	private Participant getParticipantByAccNumberWhoIsClient(String clientAccNumber , String programNumber){
		List<String> accountIDs=new ArrayList<String>();
    	accountIDs.add(clientAccNumber);
    	List<Participant> participants = partManager.getParticipantsByAccounts(accountIDs);
		for (Participant part : participants) {
			List<String> programNames=partManager.getProgramsForParticipant(part.getParticipantName(), true);
			if (programNames.contains(programNumber)){
				return part;
			}
		}
		return null;
	}
	private Participant getParticipantBySecondaryAccNumberWhoIsClient(String clientAccNumber , String programNumber){
		List<String> accountIDs=new ArrayList<String>();
    	accountIDs.add(clientAccNumber);
    	List<Participant> participants = partManager.getParticipantsBySecondaryAccounts(accountIDs);
		for (Participant part : participants) {
			List<String> programNames=partManager.getProgramsForParticipant(part.getParticipantName(), true);
			if (programNames.contains(programNumber)){
				return part;
			}
		}
		return null;
	}
	private Participant getParticipantWhoIsAggregatorByAccNumber(String aggregatorAccNumber , String programNumber){
		
		List<String> accountIDs=new ArrayList<String>();
    	accountIDs.add(aggregatorAccNumber);
    	List<Participant> participants = partManager.getParticipantsByAccounts(accountIDs);
		for (Participant part : participants) {
			List<String> programNames=partManager.getProgramsForParticipant(part.getParticipantName(), false);
			if (programNames.contains(programNumber)){
				return part;
			}
		}
		return null;
	}
	private boolean isSecondaryAccountEnabled(String programName){
		SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
		PSS2Features features = systemManager.getPss2Features();
		boolean secondaryAccountEnabled = features.isSecondaryAccountEnabled();
		secondaryAccountEnabled =  CBP_PROGRAM_CLASS.equalsIgnoreCase(programName)|| secondaryAccountEnabled;
		return secondaryAccountEnabled;
	}
	
	private Event createEventObj(APXXmlParser xmlParser){
		Event event=new Event();
		event.setProgramName(xmlParser.getProgramName());
		event.setEventName(xmlParser.getEventName());
		event.setStartTime(xmlParser.getEventStartTime());
		event.setEndTime(xmlParser.getEventEndTime());
		event.setIssuedTime(new Date());
		event.setReceivedTime(new Date());
		event.setLocations(xmlParser.getLocations());
		return event;
	}
	
	private void fillAggregatorIdClientIdListMap(APXXmlParser xmlParser){
		aggregatorAccNoClientAccNoListMap.clear();
		List<String> accountNumberList = xmlParser.getAccountNumbers();
		for(String accountNumber : accountNumberList){
			int seperatorIndex = accountNumber.indexOf(CLIENTID_AGGREGATORID_SEPERATOR);
			if(-1 ==seperatorIndex ){
				continue; // ignore clients those are not mapped with aggregater
			}
			String aggregatorAccNumber = accountNumber.substring(seperatorIndex+1);
			String clientAccNumber = accountNumber.substring(0,seperatorIndex);
			if(aggregatorAccNoClientAccNoListMap.containsKey(aggregatorAccNumber)){
				aggregatorAccNoClientAccNoListMap.get(aggregatorAccNumber).add(clientAccNumber);
				continue;
			}
			List<String> clientAccNumberList = new ArrayList<String>();
			clientAccNumberList.add(clientAccNumber);
			aggregatorAccNoClientAccNoListMap.put(aggregatorAccNumber, clientAccNumberList);
		}
		
	}
	private Collection<String> createEvent(Event event) {
		ProgramEJB program = systemManager.lookupProgramBean(event.getProgramName());
		return program.createEvent(event.getProgramName(), event, null);
	}
	public void sendEventCreationNotifications(Event event, List<ProgramValidationMessage> warnings){
		 APXManagerBean tmp = new APXManagerBean();
		 tmp.sendEventCreationNotifications(event, warnings);
	}
	public void sendExceptionNotifications(String content, String programName){
		 APXManagerBean tmp = new APXManagerBean();
		 tmp.sendExceptionNotifications(content, programName);
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
	private void clearWarnings(){
		this.warnings.clear();
	}*/

}
