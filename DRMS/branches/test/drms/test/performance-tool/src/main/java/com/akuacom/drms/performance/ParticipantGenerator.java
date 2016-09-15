package com.akuacom.drms.performance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.util.UserType;

/**
 * Generates all the participants for testing
 * @author Sunil
 *
 */
public class ParticipantGenerator {

	/**
	 * InitialContext
	 */
	private InitialContext initialContext = null;
	
	/**
	 * ParticipantManager
	 */
	private ParticipantManager participantManager;

	private ProgramParticipantManager programParticipantManager;

	/**
	 * Program Manager
	 */
	private ProgramManager programManager;
	
	/**
	 * ClientManager
	 */
	private ClientManager clientManager;
	
	/**
	 * EventManager
	 */
	private EventManager eventManager;

	/**
	 * Host
	 */
	private String host = null;
	
	/**
	 * Login User Name
	 */
	private String loginUserName = null;
	
	/**
	 * Login Password
	 */
	private String loginPwd = null;
	
	/**
	 * Program Names
	 */
	private String[] programNames = null;

	/**
	 * Method to generate participants
	 * true = Use top level entity with limited manager calls
	 * false = use traditional manager calls
	 */
	private boolean genPartsWithTopLevelEntity = true;

	
	private int getClientSize(int clientSize){
		if(clientSize%100 == 0){
			return clientSize -1;
		}else{
			return clientSize;
		}
	}
	
	
	/**
	 * Constructor
	 * @param host Host name of the machine
	 * @param loginUserName Login user name
	 * @param loginPwd Login Password
	 */
	public ParticipantGenerator(String host, String loginUserName, String loginPwd, String[] programNames, boolean useTopLevelPart){
		this.host = (host != null ? host : PerformanceTool.DEFAULT_HOST_NAME);
		this.loginUserName = (loginUserName != null ? loginUserName : PerformanceTool.DEFAULT_USER_NAME );
		this.loginPwd = (loginPwd != null ? loginPwd : PerformanceTool.DEFAULT_USER_PASSWORD);
		this.programNames = (programNames != null ? programNames : new String[]{PerformanceTool.DEFAULT_PROGRAM_NAME});
		this.genPartsWithTopLevelEntity = useTopLevelPart;
	}
	

	private void generateParticipantsWithTopLevelPart(String participantPrefix,
			String clientPrefix, String clientPassword, int numberOfParticipants)
			throws NamingException {


 // This is fast - use it for less than 300 participant creation
		
		Program program = this.getProgramManager().getProgram(programNames[0]);
		ParticipantManager pm = getParticipantManager();

		List<Participant> parentList = new ArrayList<Participant>();
		List<Participant> clientList = new ArrayList<Participant>();
		List<char[]> participantPasswords = new ArrayList<char[]>();
		List<char[]> clientPasswords = new ArrayList<char[]>();
		for (int i = 0; i < numberOfParticipants; i++) {
			String participantName = participantPrefix + i;
			Participant participant = createParticipant(participantName, false, null);
			parentList.add(participant);
			participantPasswords.add(clientPassword.toCharArray());
			String clientName = participantPrefix + i + "." + clientPrefix + i;
			Participant clientParticipant = createParticipant(clientName, true, participantName);
			clientList.add(clientParticipant);
			clientPasswords.add(clientPassword.toCharArray());
		}
		
		pm.createParticipants(parentList, 
				participantPasswords, program);
		int clientSize = clientList.size();
		int batch = 100;
		for(int i = 0; i < clientSize; i = i + batch){
			int end = i + batch > clientSize ? getClientSize(clientSize) : i + batch;
			List<Participant> temp = new ArrayList<Participant>();
			temp.addAll(clientList.subList(i, end));
			getClientManager().createClient(temp, clientPasswords, program);
		}
		
		//getClientManager().createClient(clientList, clientPasswords, program);

		
		for (int i = 0; i < numberOfParticipants; i++) {
			// create participant
			String participantName = participantPrefix + i;

			// update Program Participant
			Participant participant = pm.getParticipant(participantName, false);
        	for(ProgramParticipant pp : participant.getProgramParticipants()){
        		pp.setProgramName(program.getProgramName());
        		pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
        		pp.setClientConfig(1);
        	}
        	pm.updateParticipant(participant);
			
			
			
			
			// create client
			String clientName = participantPrefix + i + "." + clientPrefix + i;

        	// Update client program particpant
        	Participant clientParticipant = pm.getParticipant(clientName, true);
        	for(ProgramParticipant pp : clientParticipant.getProgramParticipants()){
        		pp.setProgramName(program.getProgramName());
        		pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
        		pp.setClientConfig(1);
        	}
        	pm.updateParticipant(clientParticipant);
			
		}
		
		
		
/*		
		
		for (int i = 0; i < numberOfParticipants; i++) {
			// create participant
			String participantName = participantPrefix + i;
			Participant participant = createParticipant(participantName, false, null);

        	// save participant
			
			pm.createParticipant(participant, 
                clientPassword.toCharArray(), programNames);

			// update Program Participant
        	participant = pm.getParticipant(participantName, false);
        	for(ProgramParticipant pp : participant.getProgramParticipants()){
        		pp.setProgramName(program.getProgramName());
        		pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
        		pp.setClientConfig(1);
        	}
        	pm.updateParticipant(participant);
			
			
			
			
			// create client
			String clientName = participantPrefix + i + "." + clientPrefix + i;
			Participant clientParticipant = createParticipant(clientName, true, participantName);

			
        	// save client
        	getClientManager().createClient(clientParticipant, clientPassword.toCharArray());

        	// Update client program particpant
        	clientParticipant = pm.getParticipant(clientName, true);
        	for(ProgramParticipant pp : clientParticipant.getProgramParticipants()){
        		pp.setProgramName(program.getProgramName());
        		pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
        		pp.setClientConfig(1);
        	}
        	pm.updateParticipant(clientParticipant);
			
		}
*/		

		this.getProgramManager().refreshProgram(program.getProgramName());
	}
	
	private Participant createParticipant(String name, boolean isClient, String parent) {
		Participant participant = new Participant();
    	participant.setAccountNumber(name);
    	participant.setActivated(true);
    	participant.setDataEnabler(true);
    	participant.setClient(isClient);
    	participant.setCreationTime(new Date());
    	participant.setCreator("perfTest");
    	participant.setFeedback(false);
    	participant.setLatitude(0.0);
    	participant.setLongitude(0.0);
    	participant.setShedPerHourKW(0.0);
//    	participant.setLastPrice(-1);
    	participant.setManualControl(false);
//    	participant.setOfflineWarning(true);
    	participant.setParticipantName(name);
//    	participant.setStatus(2);
//    	participant.setTestAccount(false);
    	participant.setType((byte) 0);
    	participant.setVersion(0);
    	participant.setProgramParticipants(new LinkedHashSet<ProgramParticipant>());

    	
    	if (isClient) {
    		participant.setParent(parent);
    	}else{
    		participant.setUserType(UserType.SIMPLE);
    	}
    	
    	return participant;
	}

	
	
	
	/**
	 * Generates participants
	 * @param participantPrefix
	 * @param clientPrefix
	 * @param clientPassword
	 * @param numberOfParticipants
	 * @throws NamingException
	 */
	public void generateParticipants(String participantPrefix, String clientPrefix, String clientPassword, int numberOfParticipants) throws NamingException{
		
		if(participantPrefix == null){
			participantPrefix = PerformanceTool.DEFAULT_PARTICIPANT_PRIFIX;
		}
		if(clientPrefix == null){
			clientPrefix = PerformanceTool.DEFAULT_CLIENT_PRIFIX;
		}
		if(clientPassword == null){
			clientPassword = PerformanceTool.DEFAULT_CLIENT_PASSWORD;
		}
		if(numberOfParticipants == 0){
			numberOfParticipants = PerformanceTool.DEFAULT_NUMBER_OF_PARTICIPANTS;
		}
		if (genPartsWithTopLevelEntity) {
			generateParticipantsWithTopLevelPart(participantPrefix, 
					clientPrefix, clientPassword, numberOfParticipants);
		} else {
			generateParticipantsWithFullManager(participantPrefix, 
					clientPrefix, clientPassword, numberOfParticipants);
		}
		
		
	}
	
	private void generateParticipantsWithFullManager(String participantPrefix,
			String clientPrefix, String clientPassword, int numberOfParticipants)
			throws NamingException {

		
		for(int i = 0; i < numberOfParticipants; i++){
			String participantName = participantPrefix + i;
			String clientName = participantPrefix + i + "." + clientPrefix + i;
			Participant participant = new Participant();
	        participant.setParticipantName(participantName);
	        participant.setAccountNumber(participantName);
	        participant.setDataEnabler(true);
	        getParticipantManager().createParticipant(participant, clientPassword.toCharArray(), programNames);


      		for (ProgramParticipant ppc : getParticipantManager().getProgramParticpantsForClientConfig(participantName , false)) {
      			ppc.setClientConfig(1);
      			getProgramParticipantManager().updateProgramParticipant(ppc.getProgramName(), participantName, false , ppc);
    		}	        
	        
	        Participant clientParticipant = new Participant();
			clientParticipant.setClient(true);
			clientParticipant.setUser(clientName);
			clientParticipant.setType((byte)0);
			clientParticipant.setAccountNumber(clientName);
			clientParticipant.setParent(participantName);
			clientParticipant.setActivated(true);
			getClientManager().createClient(clientParticipant, clientPassword.toCharArray());


        	ProgramParticipant pp = getProgramParticipantManager().getClientProgramParticipants(programNames[0], clientName, true);
			pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
			getProgramParticipantManager().updateProgramParticipant(programNames[0], clientName, true, pp);
		}
		
		
	}
	

	/**
	 * Creates an event for the program in context
	 * @throws NamingException Failure
	 */
	public void createEvent() throws NamingException{
		Event event = generateEvent(programNames[0]);
		getEventManager().createEvent(programNames[0], event);
	}

	public void createEvent(String programName) throws NamingException{
		Event event = generateEvent(programName);
		getEventManager().createEvent(programName, event);
	}

	
	
	/**
	 * Clears the event for the program in context
	 * @throws NamingException Failure
	 */
	public void clearEvents() throws NamingException{
		List<EventInfo> events = getProgramManager().getEventsForProgram(programNames[0]);
		if(events != null && events.size() > 0){
			for(EventInfo event:events){
				getEventManager().removeEvent(programNames[0], event.getEventName());	
			}
		}
		
	}
	
	/**
	 * Generates an Event
	 * @param programName program for which event needs to be generated
	 * @return
	 */
	private Event generateEvent(String programName) {
		Event event = new Event();
		event.setProgramName(programName);
		event.setEventName(programName + " Event");
		int oneMinute = 60000;
		Date now = new Date(System.currentTimeMillis() + oneMinute);
        GregorianCalendar calender = new GregorianCalendar();
		calender.setTime(now);
		calender.add(Calendar.DAY_OF_MONTH, 1);
		calender.set(Calendar.HOUR_OF_DAY, 14);
		calender.set(Calendar.MINUTE, 0);
		calender.set(Calendar.SECOND, 0);
		calender.set(Calendar.MILLISECOND, 0);
		event.setStartTime(calender.getTime());
		calender.set(Calendar.HOUR_OF_DAY, 18);
        event.setEndTime(calender.getTime());
        event.setReceivedTime(now);
        event.setIssuedTime(now);
		return event;
	}	
	
	
	/**
	 * Clears the participants 
	 * @param participantPrefix participant Prefix
	 * @param clientPrefix client Prefix
	 * @param numberOfParticipants number Of Participants
	 * @throws NamingException Failure
	 */
	public void clearParticipants(String participantPrefix, String clientPrefix, int numberOfParticipants) throws NamingException{
		if(participantPrefix == null){
			participantPrefix = PerformanceTool.DEFAULT_PARTICIPANT_PRIFIX;
		}
		if(clientPrefix == null){
			clientPrefix = PerformanceTool.DEFAULT_CLIENT_PRIFIX;
		}
		if(numberOfParticipants == 0){
			numberOfParticipants = PerformanceTool.DEFAULT_NUMBER_OF_PARTICIPANTS;
		}

		
		for(int i = 0; i < numberOfParticipants; i++){
			String participantName = participantPrefix + i;
			String clientName = participantPrefix + i + "." + clientPrefix + i;
			getClientManager().removeClient(clientName);
			getParticipantManager().removeParticipant(participantName);
		}
	}
	

	/**
	 * Fetches the Event Manager
	 * @return eventManager
	 * @throws NamingException Failure
	 */
	private EventManager getEventManager() throws NamingException{
		if(eventManager == null){
			eventManager = (EventManager) getInitialContext().lookup("pss2/EventManagerBean/remote");	
		}
		return eventManager;
	}

	
	/**
	 * Fetches the program Manager
	 * @return programManager
	 * @throws NamingException Failure
	 */
	private ProgramManager getProgramManager() throws NamingException{
		if(programManager == null){
			programManager = (ProgramManager) getInitialContext().lookup("pss2/ProgramManagerBean/remote");	
		}
		return programManager;
	}
	
	private ProgramParticipantManager getProgramParticipantManager() throws NamingException{
		if(programParticipantManager == null){
			programParticipantManager = (ProgramParticipantManager) getInitialContext().lookup("pss2/ProgramParticipantManager/remote");	
		}
		return programParticipantManager;
	}
	
	/**
	 * Fetches the Participant Manager
	 * @return ParticipantManager
	 * @throws NamingException Failure
	 */
	private ParticipantManager getParticipantManager() throws NamingException{
		if(participantManager == null){
			participantManager = (ParticipantManager) getInitialContext().lookup("pss2/ParticipantManagerBean/remote");	
		}
		return participantManager;
	}
	
	/**
	 * Fetches the Client Manager
	 * @return ParticipantManager
	 * @throws NamingException Failure
	 */
	private ClientManager getClientManager() throws NamingException{
		if(clientManager == null){
			clientManager = (ClientManager) getInitialContext().lookup("pss2/ClientManagerBean/remote");	
		}
		return clientManager;
	}
	

	
	
	/**
	 * Fetches the initial context
	 * @return InitialContext
	 * @throws NamingException Failure
	 */
	private InitialContext getInitialContext() throws NamingException{
		if(initialContext == null){
			
			Hashtable<String, String> env = new Hashtable<String, String>(); 
			env.put(Context.SECURITY_PRINCIPAL, loginUserName); 
			env.put(Context.SECURITY_CREDENTIALS, loginPwd); 
			env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.security.jndi.JndiLoginInitialContextFactory"); 
			env.put("java.naming.provider.url", host +":1099"); 
			initialContext = new InitialContext (env);
		}

		return initialContext;
	}
	
	/**
	 * Clean up the context and connection
	 */
	public void cleanup(){
		if(initialContext != null){
			try {
				initialContext.close();
				initialContext = null;
				participantManager = null;
				clientManager = null;
				eventManager = null;
				programManager = null;
			} catch (NamingException e) {
			}
			
		}
	}
	
}
