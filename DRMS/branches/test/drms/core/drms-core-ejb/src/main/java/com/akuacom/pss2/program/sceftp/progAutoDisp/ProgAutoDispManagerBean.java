/**
 * 
 */
package com.akuacom.pss2.program.sceftp.progAutoDisp;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.UtilityDREvent.BiddingInformation;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventTiming;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactManager;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.email.Notifier;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.bip.BIPProgramEJB;
import com.akuacom.pss2.program.dbp.BidState;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.program.dbp.SCEFTPClient;
import com.akuacom.pss2.program.sceftp.SCEFTPConfig;
import com.akuacom.pss2.program.sceftp.SCEFTPManager;
import com.akuacom.pss2.program.sceftp.SCEFTPTimerManagerBean;
import com.akuacom.pss2.program.sceftp.progAutoDisp.validate.ProgAutoDispException;
import com.akuacom.pss2.program.sceftp.progAutoDisp.validate.ProgAutoDispValidator;
import com.akuacom.pss2.query.EvtClientCandidate;
import com.akuacom.pss2.query.EvtParticipantCandidate;
import com.akuacom.pss2.query.NativeQueryManager;
import com.akuacom.pss2.util.Environment;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.EventUtil;
import com.akuacom.pss2.util.LogUtils;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

/**
 * the class ProgAutoDispManagerBean
 */
@Stateless
public class ProgAutoDispManagerBean extends SCEFTPTimerManagerBean implements
		ProgAutoDispManager.L, ProgAutoDispManager.R {

	public static final String SCE_PROG_AUTO_DISP_CHECK_TIMER = "SCE_Prog_Auto_Disp_Timer";
	public static final String CONFIG_NAME = "SCE_PROG_AUTO_DISP";

	public static final String[] DRW_PROGRAMS = { "API", "TOU-BIP", "SDP" };
	// public static final List<String> DRW_PROGRAMS= new
	// ArrayList<String>({"API", "TOU-BIP", "SDP"});
	@EJB
	SCEFTPManager.L sceFTPManager;
	@EJB
	private EventManager.L eventManager;
	@EJB
	Notifier.L notifier;
	@EJB
	InterruptibleProgramManager.L interruptibleProgramManager;
	@EJB
    protected ProgramManager.L programManager;
	
	private static final Logger log = Logger
			.getLogger(ProgAutoDispManagerBean.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.program.sceftp.SCEFTPTimerManagerBean#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return systemManager.getPss2Features().isProgramAutoDispatch();
	}
	private String getUtilityName(){
		return systemManager.getPss2Properties().getUtilityDisplayName();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.sceftp.SCEFTPTimerManagerBean#processTimeout()
	 */
	@Override
	public void processTimeout() {
		SCEFTPConfig config = getDispatchConfig();
		autoDispatch(config);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.sceftp.SCEFTPTimerManagerBean#getTimerName()
	 */
	@Override
	public String getTimerName() {
		return SCE_PROG_AUTO_DISP_CHECK_TIMER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.sceftp.SCEFTPTimerManagerBean#getConfigName()
	 */
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}
	private boolean timeCheck(SCEFTPConfig config){
		boolean result = false;
		String startTimeString = config.getScanStartTime();
		String endTimeString = config.getScanEndTime();
		final SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		
		if("00:00".equalsIgnoreCase(startTimeString)&&"00:00".equalsIgnoreCase(endTimeString)){
			result = true;
			return result;
		}
		
		try{
			Date start =format.parse(startTimeString);
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.set(Calendar.HOUR_OF_DAY, start.getHours());
			startCalendar.set(Calendar.MINUTE, start.getMinutes());
			startCalendar.set(Calendar.SECOND,0);
			
			Date end =format.parse(endTimeString);
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.set(Calendar.HOUR_OF_DAY, end.getHours());
			endCalendar.set(Calendar.MINUTE, end.getMinutes());
			endCalendar.set(Calendar.SECOND,0);
			
			if(startCalendar.after(endCalendar)){
				result = false;
			}else{
				Calendar now = Calendar.getInstance();
				if(startCalendar.before(now)&&now.before(endCalendar)){
					result = true;
				}
			}
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public void autoDispatch(SCEFTPConfig config) {
		eventNamePool.clear();
		if (config == null) {
			return;
		}
		
		if(!timeCheck(config)){
			log.info("Program Auto Dispatch Time Check:[Not reach the run time yet].");
			return;
		}
		
		// 0 input: SCEFTPConfig instance
		// needs:
		// a, ftp url, username, password
		// b, event file folder
		// c, programs enable/disable
		// d, processed file folder
		// e, disabled file folder
		String host = config.getHost();
		int port = config.getPort();
		String userName = config.getUsername();
		String password = config.getPassword();
		String programsEnabled = config.getAppendix();	// DBP DA,SAI,SPD,API,TOU-BIP,SDP
		String eventFolder = config.getSourcePath();
		String processFolder = config.getBackupPath();
		String disabledFolder = config.getAppendixPath();

		// 1 scan the folder get files
		// fetch the files and list to three types: process need/process no
		// need/wrong file
		
		//Connect to FTP server and fetch the event folder files name
		
		
		SCEFTPClient ftpClient = null;
		try {
			ftpClient = new SCEFTPClient(host, port, userName, password);

			ftpClient.connect();
			
			List<String> filenames = ftpClient.getFilenames(eventFolder);
			
			List<FileUtil> filesNameValid = new ArrayList<FileUtil>();
			
			List<String> wrongFiles = new ArrayList<String>();
			List<FileUtil> filesNeedProcess = new ArrayList<FileUtil>();
			List<FileUtil> filesNoNeedProcess = new ArrayList<FileUtil>();

			for (String fileName : filenames) {
				try{
					ProgAutoDispValidator.vlidateFileName(fileName);
					filesNameValid.add(ProgAutoDispUtil.parseFileName(fileName));
				}catch(ProgAutoDispException e){
					wrongFiles.add(fileName);
				}
			}

			for (FileUtil file : filesNameValid) {
				String program = file.getProgram().toUpperCase();
				if (programsEnabled.contains(program)) {
					filesNeedProcess.add(file);
				} else {
					filesNoNeedProcess.add(file);
				}
			}
			// 1.1 handle process need
			// a, remove this files to the "Processed file folder"
			// b, process them
			for (FileUtil file : filesNeedProcess) {
				
				// 1.1.b process business logic:
				// 1.1.b.1 validate failed->send operator notification
				// 1.1.b.2 validate success->auto dispatch
				String fileContent = ftpClient.getFileContents(file.getFilename());
				try{
					
					ProgAutoDispValidator.vlidateFileContent(fileContent,file.getFilename());
					ProgAutoDispValidator.validateFileConsistency(fileContent, file.getFilename());
					FileUtil fileUtil = ProgAutoDispUtil.parseFileConsistency(file.getFilename(), fileContent);
					
					// validate success -> auto dispatch
					String program = fileUtil.getProgram().toUpperCase();
					// dispatch to pss2 events SAI|DBP DA|SPD
					if ("SAI|DBP DA|SPD".contains(program)) {
						createPss2Event(fileUtil);	
					}
					if ("TOU-BIP".contains(program)) {
						String actionName = ProgAutoDispUtil.getActionNameByFileName(file.getFilename());
						List<FileUtil> eventUnits = ProgAutoDispUtil.parseFileConsistencyEnhance(file.getFilename(), fileContent);
				    	if(ProgAutoDispUtil.ACTION_ACTIVATED.equalsIgnoreCase(actionName)){
				    		
				    		createBIPEvent(fileUtil,eventUnits);
				    		log.info("Interruptible Program Auto Dispatch ACTIVATED event.");
				    	}else if(ProgAutoDispUtil.ACTION_CONTINUED.equalsIgnoreCase(actionName)){
				    		//do nothing
				    		testEndBIPEvent(fileUtil);	
				    	}else if(ProgAutoDispUtil.ACTION_TERMINATED.equalsIgnoreCase(actionName)){
				    		terminateBIPEvent(fileUtil,eventUnits);
				    		log.info("Interruptible Program Auto Dispatch TERMINATED event.");
				    	}else if(ProgAutoDispUtil.ACTION_SCHEDULED.equalsIgnoreCase(actionName)){
				    		//do nothing	
				    		testDeleteBIPEvent(fileUtil);	
				    	}else if("TEST_END".equalsIgnoreCase(actionName)){
				    		testEndBIPEvent(fileUtil);			    		
				    	}else if("TEST_DELETE".equalsIgnoreCase(actionName)){
				    		testDeleteBIPEvent(fileUtil);			    		
				    	}
					}
					// dispatch to drw events API|TOU-BIP|SDP
					if ("API|SDP".contains(program)) {
						ProgAutoDispValidator.validateInterruptibleProgramDateScope(fileUtil);
						interruptibleProgramManager.dispatch(fileUtil.getFilename(), fileUtil.getFileContents(),config);
					}
					
				} catch(ProgAutoDispException e) {
					sendNotifications(getUtilityName()+" Program Automated Dispatch Catch Error",e.getMessage());
				}

				try {
					ftpClient.backupEventFile(file.getFilename(),fileContent, eventFolder, processFolder);
				} catch (AppServiceException ex) {
					log.error(ex.getMessage());
				}
			}

			// 1.2 handle process no need
			// just remove this files to the "Disabled file folder"
			for (FileUtil file : filesNoNeedProcess) {
				String fileContent = ftpClient.getFileContents(file.getFilename());
				ftpClient.backupEventFile(file.getFilename(),fileContent, eventFolder, disabledFolder);
			}
			// 1.3 handle wrong file
			// just ignore them
		} catch (AppServiceException e) {
			log.error(e.getMessage());
		} finally {
			if (ftpClient != null)
				ftpClient.close();
		}
	}
	@Override
	public void autoDispatch(SCEFTPConfig config, String fileName,String fileContent) {
		if (config == null) {
			return;
		}
		String programsEnabled = config.getAppendix();	// DBP DA,SAI,SPD,API,TOU-BIP,SDP

		// 1 scan the folder get files
		// fetch the files and list to three types: process need/process no
		// need/wrong file
		
		//Connect to FTP server and fetch the event folder files name

			List<FileUtil> filesNameValid = new ArrayList<FileUtil>();
			
			List<String> wrongFiles = new ArrayList<String>();
			List<FileUtil> filesNeedProcess = new ArrayList<FileUtil>();
			List<FileUtil> filesNoNeedProcess = new ArrayList<FileUtil>();

				try{
					ProgAutoDispValidator.vlidateFileName(fileName);
					filesNameValid.add(ProgAutoDispUtil.parseFileName(fileName));
				}catch(ProgAutoDispException e){
					wrongFiles.add(fileName);
				}


			for (FileUtil file : filesNameValid) {
				String program = file.getProgram();
				if (programsEnabled.contains(program)) {
					filesNeedProcess.add(file);
				} else {
					filesNoNeedProcess.add(file);
				}
			}
			// 1.1 handle process need
			// a, remove this files to the "Processed file folder"
			// b, process them
			for (FileUtil file : filesNeedProcess) {
				
				try{
					ProgAutoDispValidator.vlidateFileContent(fileContent,file.getFilename());
					ProgAutoDispValidator.validateFileConsistency(fileContent, file.getFilename());
					
					FileUtil fileUtil = ProgAutoDispUtil.parseFileConsistency(file.getFilename(), fileContent);
					// validate success -> auto dispatch
					String program = fileUtil.getProgram();
					// dispatch to pss2 events SAI|DBP DA|SPD
					if ("SAI|DBP DA|SPD".contains(program)) {
						createPss2Event(fileUtil);
					}
					if ("TOU-BIP".contains(program)) {
						String actionName = ProgAutoDispUtil.getActionNameByFileName(fileName);
						List<FileUtil> eventUnits = ProgAutoDispUtil.parseFileConsistencyEnhance(file.getFilename(), fileContent);
				    	if(ProgAutoDispUtil.ACTION_ACTIVATED.equalsIgnoreCase(actionName)){
				    		createBIPEvent(fileUtil,eventUnits);
				    		log.info("Interruptible Program Auto Dispatch ACTIVATED event.");
				    	}else if(ProgAutoDispUtil.ACTION_CONTINUED.equalsIgnoreCase(actionName)){
				    		//do nothing
				    		testEndBIPEvent(fileUtil);	
				    	}else if(ProgAutoDispUtil.ACTION_TERMINATED.equalsIgnoreCase(actionName)){
				    		terminateBIPEvent(fileUtil,eventUnits);
				    		log.info("Interruptible Program Auto Dispatch TERMINATED event.");
				    	}else if(ProgAutoDispUtil.ACTION_SCHEDULED.equalsIgnoreCase(actionName)){
				    		//do nothing		
				    		testDeleteBIPEvent(fileUtil);	
				    	}else if("TEST_END".equalsIgnoreCase(actionName)){
				    		testEndBIPEvent(fileUtil);			    		
				    	}else if("TEST_DELETE".equalsIgnoreCase(actionName)){
				    		testDeleteBIPEvent(fileUtil);			    		
				    	}
					}
					// dispatch to drw events API|TOU-BIP|SDP
					if ("API|SDP".contains(program)) {
						ProgAutoDispValidator.validateInterruptibleProgramDateScope(fileUtil);
						interruptibleProgramManager.dispatch(fileUtil.getFilename(), fileUtil.getFileContents(),config);
					}
				}catch(ProgAutoDispException e){
					sendNotifications(getUtilityName()+" Program Automated Dispatch Catch Error",e.getMessage());
				}
			}
			
	}
	
	private EventStatus eventStatus = EventStatus.NONE;
	
	
	private List<String> eventNamePool = new ArrayList<String>();
	
	private String generateEventName(Date date,boolean isDBP){
		final SimpleDateFormat format = new SimpleDateFormat("yyMMdd-HHmmss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String eventName ="";
		if(!isDBP){
			eventName = format.format(date);
		}else{
			eventName = "DBP DA"+format.format(date);	
		}
		
		if(!eventNamePool.contains(eventName)){
			eventNamePool.add(eventName);
		}else{
			calendar.add(Calendar.SECOND, 1);
			return generateEventName(calendar.getTime(),isDBP);
		}
		return eventName;
	}
	protected void createPss2Event(FileUtil file) {
		String program = file.getProgram().toUpperCase();
		Event event=null;
		if("SAI|SPD".contains(program)){
			event = new Event();
			
			Date now =new Date();
			if (file.getIssueTime()==null || file.getIssueTime().before(now))
				event.setIssuedTime(now);
			else
				event.setIssuedTime(file.getIssueTime());
			
			if (file.getStartTime()==null || file.getStartTime().before(now))
				event.setStartTime(now);
			else	
				event.setStartTime(file.getStartTime());
			
			event.setEndTime(file.getEndTime());
			String eventName = generateEventName(new Date(),false);
			event.setEventName(eventName);
			event.setEventStatus(eventStatus);
			event.setManual(true);
			event.setProgramName(program);
			event.setReceivedTime(now);
			populateEventParticipants(event);
			
			
			try {
				ProgAutoDispValidator.validateFileDateScope(programManager,event,file.getFilename(),false);
				eventManager.createEvent(event.getProgramName(), event);
			} catch (ProgAutoDispException e) {
				sendNotifications(getUtilityName()+" Program Automated Dispatch Catch Error",e.getMessage());
			}
		}if("DBP".contains(program)){
			event = generateDBPEvent(file);
			UtilityDREvent ue = createUtilityDREvent(file);
			try {
				ProgAutoDispValidator.validateFileDateScope(programManager,event,file.getFilename(),false);
				eventManager.createEvent(event.getProgramName(), event,ue);
			} catch (ProgAutoDispException e) {
				sendNotifications(getUtilityName()+" Program Automated Dispatch Catch Error",e.getMessage());
			}
		}
	}
    
	private Event generateDBPEvent(FileUtil file){
		Date now = new Date();
		DBPEvent event = new DBPEvent();
        event.setManual(true);
        event.setProgramName("DBP DA");
        event.setEventName(generateEventName(new Date(),true));

        event.setReceivedTime(now);
        if (file.getIssueTime()==null || file.getIssueTime().before(now))
        	event.setIssuedTime(now);
        else
        	event.setIssuedTime(file.getIssueTime());
        if (file.getStartTime()==null || file.getStartTime().before(now))
        	event.setStartTime(now);
        else
        	event.setStartTime(file.getStartTime());
        event.setEndTime(file.getEndTime());
        event.setRespondBy(event.getStartTime());
        event.setDrasRespondBy(event.getStartTime());
        event.setCurrentBidState(BidState.IDLE);
        
        event.setDrEvent(true);

        return event;
	}
	
	
	public UtilityDREvent createUtilityDREvent(FileUtil file)  {
	
		UtilityDREvent drEvent=new UtilityDREvent();
		drEvent.setProgramName("DBP DA");
	    drEvent.setEventIdentifier(EventUtil.getUniqueEventName("DBP DA"));
	
	    EventTiming eventTiming = new EventTiming();
	    GregorianCalendar cal = new GregorianCalendar();
	    cal.setTimeZone(TimeZone.getDefault());
	    
	    cal.setTime(file.getStartTime());
	    eventTiming.setStartTime(new XMLGregorianCalendarImpl(cal));
	    
	    cal.setTime(file.getEndTime());
	    eventTiming.setEndTime(new XMLGregorianCalendarImpl(cal));
	
	    eventTiming.setNotificationTime(new XMLGregorianCalendarImpl(
	            new GregorianCalendar()));
	    drEvent.setEventTiming(eventTiming);
	
	    BiddingInformation biddingInformation = new BiddingInformation();
	    biddingInformation.setOpeningTime(eventTiming.getStartTime());
	    biddingInformation.setClosingTime(eventTiming.getStartTime());
	    drEvent.setBiddingInformation(biddingInformation);

    return drEvent;
}
	
	public void sendNotifications(String subject, String content) {

		log.info(LogUtils.createLogEntry("", getUtilityName()+" AUTO DISPATCH event error: ",subject, content));

		sendDRASOperatorEventNotification(subject, content, null, null,
				NotificationMethod.getInstance(),
				new NotificationParametersVO(), "", notifier);
	}

	public static synchronized void sendDRASOperatorEventNotification(
			String subject, String content, String attachFilename,
			String attachFileContent, NotificationMethod method,
			NotificationParametersVO params, String programName,
			Notifier notifier) {
		ContactManager cm = EJB3Factory.getLocalBean(ContactManager.class);

		//List<Contact> contacts = cm.getOperatorContacts();
		 EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
			List<Contact> contacts =  cache.getEscacheforoperatorcontacts();
	        if(contacts.isEmpty()){
	        	contacts = cm.getOperatorContacts();
	        	cache.setEscacheforoperatorcontacts("OperatorContacts", contacts);
	        }
		List<Contact> sendList = new ArrayList<Contact>();

		for (Contact c : contacts) {
			if (c.eventNotificationOn()) {
				sendList.add(c);
			}
		}

		if (sendList.size() > 0) {
			notifier.sendNotification(sendList, "operator", subject, content,
					attachFilename, attachFileContent, method, params,
					Environment.isAkuacomEmailOnly(), programName);
		}
	}

	
	private void populateEventParticipants(Event event,String locationType,List<String> locationNumberList,boolean byLocation){
		List<EvtParticipantCandidate> candidates;
		try {
			candidates = nativeQueryManager.getEvtParticipantCandidate(event.getProgramName());

			for (EvtParticipantCandidate candidate : candidates) {
				boolean createFlag = true;
				if(byLocation){
					String plocationNumber="";
					if("SLAP".equalsIgnoreCase(locationType)){
						plocationNumber=candidate.getSlap();
					}else if("ABANK".equalsIgnoreCase(locationType)){
						plocationNumber=candidate.getaBank();
					}else if("SUBSTATION".equalsIgnoreCase(locationType)){
						plocationNumber=candidate.getSubstation();
					}else if("BLOCK NUMBER".equalsIgnoreCase(locationType)){
						plocationNumber=candidate.getBlockNumber();
					}
					createFlag = locationNumberList.contains(plocationNumber);
				}
				if(createFlag){					
					Participant p = new Participant();
					p.setParticipantName(candidate.getParticipantName());
					p.setAccountNumber(candidate.getAccount());
					p.setShedPerHourKW(candidate.getRegisterShed());
					p.setUUID(candidate.getUUID());
					p.setOptOut(candidate.isParticipantOptOut());
					p.setClient(candidate.isClient());

					EventParticipant eventParticipant = new EventParticipant();

					// set value at both ends of bi-literal relationship
					// EventParticipant - Event
					eventParticipant.setEvent(event);
					event.getEventParticipants().add(eventParticipant);

					// set value at both ends of bi-literal relationship
					// EventParticipant - Participant
					eventParticipant.setParticipant(p);
					p.getEventParticipants().add(eventParticipant);

					for (EvtClientCandidate clientCandidate : candidate
							.getClients()) {
						Participant c = new Participant();
						c.setParticipantName(clientCandidate.getClientName());
						c.setAccountNumber(clientCandidate.getAccount());
						c.setUUID(clientCandidate.getClientUUID());
						p.setOptOut(candidate.isParticipantOptOut());
						c.setClient(clientCandidate.isClient());

						EventParticipant evetnClient = new EventParticipant();

						// set value at both ends of bi-literal relationship
						// EventParticipant - Event
						evetnClient.setEvent(event);
						event.getEventParticipants().add(evetnClient);

						// set value at both ends of bi-literal relationship
						// EventParticipant - Participant
						evetnClient.setParticipant(c);
						c.getEventParticipants().add(evetnClient);
					}
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
	}
	
	@EJB
	NativeQueryManager.L nativeQueryManager;

	protected void populateEventParticipants(Event event) {
		populateEventParticipants(event,"",new ArrayList<String>(),false);
	}
	
	private Event parseEventByFileUtil(FileUtil file,boolean populateEventParticipants,boolean buildLocations,String programName,boolean dateCheck){
		Event event= new Event();
		
		Date now =new Date();
		if(dateCheck){
			if (file.getIssueTime()==null || file.getIssueTime().before(now))
				event.setIssuedTime(now);
			else
				event.setIssuedTime(file.getIssueTime());
		}else{
			if (file.getIssueTime()!=null){
				event.setIssuedTime(file.getIssueTime());
			}
		}
		if (file.getStartTime()!=null){
			event.setStartTime(file.getStartTime());
		}
		
		
		event.setEndTime(file.getEndTime());
		String eventName = generateEventName(new Date(),false);
		event.setEventName(eventName);
		event.setEventStatus(eventStatus);
		event.setManual(true);
		event.setProgramName(programName);
		event.setReceivedTime(now);
		if(populateEventParticipants){
			populateEventParticipants(event);
		}
		if(buildLocations){
			List<String> locations = new ArrayList<String>();
			String locationString = ProgAutoDispUtil.buildLocationName(file.getFileContents());
			String[] locationNumberList = locationString.split("-");
			for(String s:locationNumberList){
				locations.add(s);
			}
			event.setLocations(locations);
		}
		
		return event;
	}
	protected void createBIPEvent(FileUtil file,List<FileUtil> eventUnits) {

		Event eventPrepareToCreate = parseEventByFileUtil(file,false,true,"BIP",true);
		List<Event> eventsPrepareToValidate = new ArrayList<Event>();
		List<String> locationNumberList = new ArrayList<String>();
		for(FileUtil eventUnit:eventUnits){
			Event eventPrepareToValidate = parseEventByFileUtil(eventUnit,false,false,"BIP",true);	
			eventsPrepareToValidate.add(eventPrepareToValidate);
			locationNumberList.add(eventUnit.getLocationName());
		}
		this.populateEventParticipants(eventPrepareToCreate, "ABANK", locationNumberList, true);
		try {
			for(Event event:eventsPrepareToValidate){
				ProgAutoDispValidator.validateFileDateScope(programManager,event,file.getFilename(),true);
			}
			eventManager.createEvent(eventPrepareToCreate.getProgramName(), eventPrepareToCreate);
		} catch (ProgAutoDispException e) {
			sendNotifications(getUtilityName()+" Program Automated Dispatch Catch Error",e.getMessage());
		}
	}
	
	protected void terminateBIPEvent(FileUtil file,List<FileUtil> eventUnits) {
		Event eventPrepareToCreate = parseEventByFileUtil(file,true,true,"BIP",false);
		List<Event> eventsPrepareToValidate = new ArrayList<Event>();

		for(FileUtil eventUnit:eventUnits){
			Event eventPrepareToValidate = parseEventByFileUtil(eventUnit,false,false,"BIP",false);	
			eventsPrepareToValidate.add(eventPrepareToValidate);
		}
		try {
			for(Event event:eventsPrepareToValidate){
				ProgAutoDispValidator.validateFileDateScope(programManager,event,file.getFilename(),false);
			}
			eventManager.endEvent("BIP", eventPrepareToCreate.getLocations(), eventPrepareToCreate.getStartTime(),eventPrepareToCreate.getEndTime());
		} catch (ProgAutoDispException e) {
			sendNotifications(getUtilityName()+" Program Automated Dispatch Catch Error",e.getMessage());
		}
	}
	
	protected void testDeleteBIPEvent(FileUtil file) {
//		String program = file.getProgram().toUpperCase();
		String program = "BIP";
		Event event=null;
		event = new Event();
		
		Date now =new Date();
		if (file.getIssueTime()==null || file.getIssueTime().before(now))
			event.setIssuedTime(now);
		else
			event.setIssuedTime(file.getIssueTime());
		
		if (file.getStartTime()==null || file.getStartTime().before(now))
			event.setStartTime(now);
		else	
			event.setStartTime(file.getStartTime());
		
		event.setEndTime(file.getEndTime());
		String eventName = generateEventName(new Date(),false);
		event.setEventName(eventName);
		event.setEventStatus(eventStatus);
		event.setManual(true);
		event.setProgramName(program);
		event.setReceivedTime(now);
		populateEventParticipants(event);
		
		List<String> locations = new ArrayList<String>();
		String locationString = ProgAutoDispUtil.buildLocationName(file.getFileContents());
		String[] locationNumberList = locationString.split("-");
		for(String s:locationNumberList){
			locations.add(s);
		}
		event.setLocations(locations);
		
		try {
			ProgAutoDispValidator.validateFileDateScope(programManager,event,file.getFilename(),false);
			interruptibleProgramManager.deleteEvent("BIP", "BIP2013", "ABANK", locations, event.getStartTime());
		} catch (ProgAutoDispException e) {
			sendNotifications(getUtilityName()+" Program Automated Dispatch Catch Error",e.getMessage());
		}
	}
	protected void testEndBIPEvent(FileUtil file) {
//		String program = file.getProgram().toUpperCase();
		String program = "BIP";
		Event event=null;
		event = new Event();
		
		Date now =new Date();
		if (file.getIssueTime()==null || file.getIssueTime().before(now))
			event.setIssuedTime(now);
		else
			event.setIssuedTime(file.getIssueTime());
		
		if (file.getStartTime()==null || file.getStartTime().before(now))
			event.setStartTime(now);
		else	
			event.setStartTime(file.getStartTime());
		
		event.setEndTime(file.getEndTime());
		String eventName = generateEventName(new Date(),false);
		event.setEventName(eventName);
		event.setEventStatus(eventStatus);
		event.setManual(true);
		event.setProgramName(program);
		event.setReceivedTime(now);
		populateEventParticipants(event);
		
		List<String> locations = new ArrayList<String>();
		String locationString = ProgAutoDispUtil.buildLocationName(file.getFileContents());
		String[] locationNumberList = locationString.split("-");
		for(String s:locationNumberList){
			locations.add(s);
		}
		event.setLocations(locations);
		
		try {
			ProgAutoDispValidator.validateFileDateScope(programManager,event,file.getFilename(),false);
			interruptibleProgramManager.endEvent("BIP", "BIP2013", "ABANK", locations, event.getStartTime(),event.getEndTime());
		} catch (ProgAutoDispException e) {
			sendNotifications(getUtilityName()+" Program Automated Dispatch Catch Error",e.getMessage());
		}
	}
}
