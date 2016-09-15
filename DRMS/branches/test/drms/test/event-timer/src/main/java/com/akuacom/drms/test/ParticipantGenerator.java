package com.akuacom.drms.test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.signal.SignalDef;

/**
 * Generates all the participants for testing
 * 
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

	/**
	 * Program Manager
	 */
	private ProgramManager programManager;

	private ProgramParticipantManager programParticipantManager;

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

	/**
	 * Signal Number
	 */
	public static final String SIGNAL_NUMBER = "number";

	/**
	 * Signal Name
	 */
	public static final String SIGNAL_NAME = "price";

	/**
	 * Fetches offset dat
	 * 
	 * @param date
	 *            date
	 * @param min
	 *            offset min
	 * @return offseted date
	 */
	private Date offSet(Date date, int min) {
		return new Date(date.getTime() + min * 60 * 1000);
	}

	/**
	 * Constructor
	 * 
	 * @param host
	 *            Host name of the machine
	 * @param loginUserName
	 *            Login user name
	 * @param loginPwd
	 *            Login Password
	 */
	public ParticipantGenerator(String host, String loginUserName,
			String loginPwd, String[] programNames, boolean useTopLevelPart) {
		this.host = (host != null ? host : EventTimer.DEFAULT_HOST_NAME);
		this.loginUserName = (loginUserName != null ? loginUserName
				: EventTimer.DEFAULT_USER_NAME);
		this.loginPwd = (loginPwd != null ? loginPwd
				: EventTimer.DEFAULT_USER_PASSWORD);
		this.programNames = (programNames != null ? programNames
				: new String[] { EventTimer.DEFAULT_PROGRAM_NAME });
		this.genPartsWithTopLevelEntity = useTopLevelPart;

	}

	/**
	 * Generates participants
	 * 
	 * @param participantPrefix
	 * @param clientPrefix
	 * @param clientPassword
	 * @param numberOfParticipants
	 * @throws NamingException
	 */
	public void generateParticipants(String participantPrefix,
			String clientPrefix, String clientPassword, int numberOfParticipants)
			throws NamingException {
		
		if (participantPrefix == null) {
			participantPrefix = EventTimer.DEFAULT_PARTICIPANT_PRIFIX;
		}
		if (clientPrefix == null) {
			clientPrefix = EventTimer.DEFAULT_CLIENT_PRIFIX;
		}
		if (clientPassword == null) {
			clientPassword = EventTimer.DEFAULT_CLIENT_PASSWORD;
		}
		if (numberOfParticipants == 0) {
			numberOfParticipants = EventTimer.DEFAULT_NUMBER_OF_PARTICIPANTS;
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

		for (int i = 0; i < numberOfParticipants; i++) {
			String participantName = participantPrefix + i;
			String clientName = participantPrefix + i + "." + clientPrefix + i;
			Participant participant = new Participant();
			participant.setParticipantName(participantName);
			participant.setAccountNumber(participantName);
			participant.setDataEnabler(true);
			getParticipantManager().createParticipant(participant,
					clientPassword.toCharArray(), programNames);

			for (ProgramParticipant ppc : getParticipantManager()
					.getProgramParticpantsForClientConfig(participantName,
							false)) {
				ppc.setClientConfig(1);
				getProgramParticipantManager().updateProgramParticipant(
						ppc.getProgramName(), participantName, false, ppc);
			}
			Participant clientParticipant = new Participant();
			clientParticipant.setClient(true);
			clientParticipant.setParticipantName(clientName);
			clientParticipant.setType((byte) 0);
			clientParticipant.setAccountNumber(clientName);
			clientParticipant.setParent(participantName);
			clientParticipant.setActivated(true);
			getClientManager().createClient(clientParticipant,
					clientPassword.toCharArray());

			ProgramParticipant pp = getProgramParticipantManager()
					.getClientProgramParticipants(programNames[0], clientName,
							true);
			pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
			getProgramParticipantManager().updateProgramParticipant(
					programNames[0], clientName, true, pp);
		}
	}
	
	private void generateParticipantsWithTopLevelPart(String participantPrefix,
			String clientPrefix, String clientPassword, int numberOfParticipants)
			throws NamingException {
		
		Program program = this.getProgramManager().getProgram(programNames[0]);
		
		for (int i = 0; i < numberOfParticipants; i++) {
			// create participant
			String participantName = participantPrefix + i;
			Participant participant = createParticipant(participantName, false, null);
			

        	// save participant
			ParticipantManager pm = getParticipantManager();
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
		
		getProgramManager().refreshProgram(program.getProgramName());
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
    	}
    	
    	return participant;
	}

	/**
	 * Creates an event for the program in context
	 * 
	 * @return Event Name as a String
	 * @throws NamingException
	 *             Failure
	 */
	public String createEvent() throws NamingException {
		Event event = generateEvent(programNames[0]);
		getEventManager().createEvent(programNames[0], event);
		String eventName = null;
		for (EventInfo i : getEventManager().getEvents()) {
			String iName = i.getEventName();
			if (iName.contains(programNames[0] + "-")) {
				eventName = iName;
				break;
			}

		}
		return eventName;
	}

	/**
	 * Clears the event for the program in context
	 * 
	 * @throws NamingException
	 *             Failure
	 */
	public void clearEvents() throws NamingException {
		List<EventInfo> events = getProgramManager().getEventsForProgram(
				programNames[0]);
		if (events != null && events.size() > 0) {
			for (EventInfo event : events) {
				getEventManager().removeEvent(programNames[0],
						event.getEventName());
			}
		}

	}

	/**
	 * Generates an Event
	 * 
	 * @param programName
	 *            program for which event needs to be generated
	 * @return
	 */
	private Event generateEvent(String programName) {
		Event event = new Event();
		event.setProgramName(programName);
		event.setEventName(programName + " Event");
		Date now = new Date();
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
		return event;
	}

	/**
	 * Clears the participants
	 * 
	 * @param participantPrefix
	 *            participant Prefix
	 * @param clientPrefix
	 *            client Prefix
	 * @param numberOfParticipants
	 *            number Of Participants
	 * @throws NamingException
	 *             Failure
	 */
	public void clearParticipants(String participantPrefix,
			String clientPrefix, int numberOfParticipants)
			throws NamingException {
		if (participantPrefix == null) {
			participantPrefix = EventTimer.DEFAULT_PARTICIPANT_PRIFIX;
		}
		if (clientPrefix == null) {
			clientPrefix = EventTimer.DEFAULT_CLIENT_PRIFIX;
		}
		if (numberOfParticipants == 0) {
			numberOfParticipants = EventTimer.DEFAULT_NUMBER_OF_PARTICIPANTS;
		}

		for (int i = 0; i < numberOfParticipants; i++) {
			String participantName = participantPrefix + i;
			String clientName = participantPrefix + i + "." + clientPrefix + i;
			getClientManager().removeClient(clientName);
			getParticipantManager().removeParticipant(participantName);
		}
	}

	/**
	 * Fetches the Event Manager
	 * 
	 * @return eventManager
	 * @throws NamingException
	 *             Failure
	 */
	private EventManager getEventManager() throws NamingException {
		if (eventManager == null) {
			eventManager = (EventManager) getInitialContext().lookup(
					"pss2/EventManagerBean/remote");
		}
		return eventManager;
	}

	/**
	 * Fetches the program Manager
	 * 
	 * @return programManager
	 * @throws NamingException
	 *             Failure
	 */
	private ProgramManager getProgramManager() throws NamingException {
		if (programManager == null) {
			programManager = (ProgramManager) getInitialContext().lookup(
					"pss2/ProgramManagerBean/remote");
		}
		return programManager;
	}

	private ProgramParticipantManager getProgramParticipantManager()
			throws NamingException {
		if (programParticipantManager == null) {
			programParticipantManager = (ProgramParticipantManager) getInitialContext()
					.lookup("pss2/ProgramParticipantManagerBean/remote");
		}
		return programParticipantManager;
	}

	/**
	 * Fetches the Participant Manager
	 * 
	 * @return ParticipantManager
	 * @throws NamingException
	 *             Failure
	 */
	private ParticipantManager getParticipantManager() throws NamingException {
		if (participantManager == null) {
			participantManager = (ParticipantManager) getInitialContext()
					.lookup("pss2/ParticipantManagerBean/remote");
		}
		return participantManager;
	}

	/**
	 * Fetches the Client Manager
	 * 
	 * @return ParticipantManager
	 * @throws NamingException
	 *             Failure
	 */
	private ClientManager getClientManager() throws NamingException {
		if (clientManager == null) {
			clientManager = (ClientManager) getInitialContext().lookup(
					"pss2/ClientManagerBean/remote");
		}
		return clientManager;
	}

	/**
	 * Fetches the initial context
	 * 
	 * @return InitialContext
	 * @throws NamingException
	 *             Failure
	 */
	private InitialContext getInitialContext() throws NamingException {
		if (initialContext == null) {

			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.SECURITY_PRINCIPAL, loginUserName);
			env.put(Context.SECURITY_CREDENTIALS, loginPwd);
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jboss.security.jndi.JndiLoginInitialContextFactory");
			env.put("java.naming.provider.url", host + ":1099");
			initialContext = new InitialContext(env);
		}

		return initialContext;
	}

	/**
	 * Set Signal Entries
	 * 
	 * @param eventName
	 *            Event Name
	 * @param participantPrefix
	 *            participant Prefix
	 * @param clientPrefix
	 *            client Prefix
	 * @param numberOfParticipants
	 *            number Of Participants
	 * @throws NamingException
	 *             Failure
	 */
	public void setSignalEntries(String eventName, String participantPrefix,
			String clientPrefix, int numberOfParticipants)
			throws NamingException {

		if (participantPrefix == null) {
			participantPrefix = EventTimer.DEFAULT_PARTICIPANT_PRIFIX;
		}
		if (clientPrefix == null) {
			clientPrefix = EventTimer.DEFAULT_CLIENT_PRIFIX;
		}
		if (numberOfParticipants == 0) {
			numberOfParticipants = EventTimer.DEFAULT_NUMBER_OF_PARTICIPANTS;
		}

		for (int i = 0; i < numberOfParticipants; i++) {
			//String participantName = participantPrefix + i;
			String clientName = participantPrefix + i + "." + clientPrefix + i;
			EventParticipant ep = getEventManager().getEventParticipant(
					eventName, clientName, true);
			Set<EventParticipantSignalEntry> signalEntries = new HashSet<EventParticipantSignalEntry>();
			Set<EventParticipantSignal> eventParticipantSignalSet = new HashSet<EventParticipantSignal>();

			SignalDef signalDef = new SignalDef();
			signalDef.setSignalName(SIGNAL_NAME);
			signalDef.setType("PRICE_ABSOLUTE");
			signalDef.setInputSignal(true);
			signalDef.setLevelSignal(false);

			EventParticipantSignal eps1 = new EventParticipantSignal();
			eps1.setEventParticipant(ep);
			eps1.setSignalDef(signalDef);

			Date now = new Date();
			EventParticipantSignalEntry signalEntry1 = new EventParticipantSignalEntry();
			// two hours ago
			signalEntry1.setTime(offSet(now, -120));
			signalEntry1.setNumberValue(120.0);
			signalEntry1.setEventParticipantSignal(eps1);

			EventParticipantSignalEntry signalEntry2 = new EventParticipantSignalEntry();
			// one hour ago
			signalEntry2.setTime(offSet(now, -60));
			signalEntry2.setNumberValue(150.0);
			signalEntry2.setEventParticipantSignal(eps1);

			EventParticipantSignalEntry signalEntry3 = new EventParticipantSignalEntry();
			// one hour later
			signalEntry3.setTime(offSet(now, 60));
			signalEntry3.setNumberValue(150.0);
			signalEntry3.setEventParticipantSignal(eps1);

			// add to entries in random sequence
			signalEntries.add(signalEntry3);
			signalEntries.add(signalEntry1);
			signalEntries.add(signalEntry2);
			eps1.setEventParticipantSignalEntries(signalEntries);
			eventParticipantSignalSet.add(eps1);
			ep.setSignals(eventParticipantSignalSet);
		}

	}

	/**
	 * Clean up the context and connection
	 */
	public void cleanup() {
		if (initialContext != null) {
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
