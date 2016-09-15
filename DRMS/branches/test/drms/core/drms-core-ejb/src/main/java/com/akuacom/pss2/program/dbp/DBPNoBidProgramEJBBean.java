// $Revision$ $Date$
package com.akuacom.pss2.program.dbp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.eventinfo.EventInfoInstance.Participants;
import org.openadr.dras.eventinfo.EventInfoInstance.Values;
import org.openadr.dras.eventinfo.EventInfoValue;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.UtilityDREvent.BiddingInformation;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventInformation;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventTiming;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.client.ClientEAO;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactManager;
import com.akuacom.pss2.core.ProgramEJBBean;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.core.ValidatorFactory;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.email.Notifier;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.EventEmailFormatter;
import com.akuacom.pss2.event.TimeBlock;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.participant.bid.ParticipantBidState;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.bidding.BidBlock;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.sceftp.CreationFailureException;
import com.akuacom.pss2.program.sceftp.MessageUtil;
import com.akuacom.pss2.program.sceftp.SCEFTPConfig;
import com.akuacom.pss2.program.sceftp.SCEFTPConfigGenEAO;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties.PropertyName;
import com.akuacom.pss2.util.Environment;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.EventUtil;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.DateUtil;
import com.akuacom.utils.lang.StringUtil;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

@Stateless
public class DBPNoBidProgramEJBBean extends DBPProgramEJBBean implements
        DBPNoBidProgramEJB.R, DBPNoBidProgramEJB.L {
    private static final Logger log = Logger
            .getLogger(DBPNoBidProgramEJBBean.class);

    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final int RETURN_SUCCESS = 0;
    private static final int RETURN_FAILTURE = 1;
    private static final int RETURN_FTP_CONNECT_FAILTURE = 2;

    private static final String PROGRAM_NAME = "DBP DA";
    
    @EJB
    private com.akuacom.pss2.program.ProgramManager.L programManager;

    /** The dbp da. */
    @EJB
    protected DBPDataAccess.L dbpDA;

    @EJB
    ParticipantEAO.L participantEAO;

    @EJB
    ClientEAO.L clientEAO;

    @EJB
    DBPEventCreationGenEAO.L dbpEventCreationEAO;
    @EJB
	EventEAO.L eventEAO;
    @EJB
    ProgramParticipantManager.L programPartticipantManager;
	@EJB
	SCEFTPConfigGenEAO.L dispatchConfigEAO;
    @EJB
    SystemManager.L systemManager;

    /**
     * Allow a program implementation to do any initial setup on one of its
     * events.
     * 
     * This method is often overridden by specialized program implementations.
     * 
     * NOTE: This is not the place to create Event-level input signals. That
     * should be done in getEventInputEventSignals
     */
    @Override
    public Set<EventSignal> initializeEvent(Program program, Event event,
            UtilityDREvent utilityDREvent) throws ProgramValidationException {
        final DBPEvent dbpEvent = (DBPEvent) event;
        dbpEvent.setRespondBy(event.getIssuedTime());
        dbpEvent.setDrasRespondBy(event.getIssuedTime());
        dbpEvent.setCurrentBidState(BidState.PROCESSING_COMPLETE);
        createBidEntries((DBPProgram) program, event, utilityDREvent);
        return super.initializeEvent(program, event, utilityDREvent);
    }

    @Override
    public UtilityDREvent parseBidFile(String filename, String fileString) throws ProgramValidationException{
    	if (checkScheduledEvent()) {
    		String message="No event created due to one or more scheduled events exist in the system";
    		ProgramValidationException exception=new ProgramValidationException();
    		List<ProgramValidationMessage> messages=new ArrayList<ProgramValidationMessage>();
    		messages.add(new ProgramValidationMessage("EventCreation", message));
    		exception.setErrors(messages);
    		throw exception;
		}
    	
		SCEDBPEventFileParser parser=new SCEDBPEventFileParser(fileString, filename);
		UtilityDREvent drEvent = createUtilityDREvent(parser);
		
    	return drEvent;
    }

    private void createBidEntries(DBPProgram program, Event event,
            UtilityDREvent utilityDREvent) throws ProgramValidationException {
        DBPEvent dbpEvent = (DBPEvent) event;
        String eventName = dbpEvent.getEventName();
        int eventDurationS = (int) (((dbpEvent.getEndTime().getTime() - dbpEvent
                .getStartTime().getTime()) / MILLISECONDS_PER_SECOND));
        String programName = program.getProgramName();

        // for every participant
        if(utilityDREvent!=null && utilityDREvent.getEventInformation()!=null){
            EventInformation eventInformation = utilityDREvent.getEventInformation();
        	List<EventInfoInstance> eventInfos=eventInformation.getEventInfoInstance();
        	for (EventInfoInstance eventInfo : eventInfos) {
        		EventParticipant eventPart = null;
                for (EventParticipant ep : event.getEventParticipants()) {
                    if (ep != null
                            && ep.getParticipant()
                                    .getAccountNumber()
                                    .equals(eventInfo.getParticipants()
                                            .getAccountID().get(0))) {
                        eventPart = ep;
                        break;
                    }
                }

                if(eventPart == null)
                {
                	continue;
                }
                
                Participant participant = eventPart.getParticipant();
                String participantName = participant.getParticipantName();

                // bug 739
                if (participant.isClient()) {
                    String message = "Participant " + participantName
                            + " for event " + eventName + " in program "
                            + programName
                            + " is a client. Bid files should not contain clients.";

                    log.warn(message);

                    ProgramValidationException exception = new ProgramValidationException();
                    ProgramValidationMessage error = new ProgramValidationMessage();
                    error.setDescription(message);
                    error.setParameterName("DBP Bid File Error: ");
                    List<ProgramValidationMessage> errors = exception.getErrors();
                    if (errors == null) {
                        errors = new ArrayList<ProgramValidationMessage>();
                    }
                    errors.add(error);
                    exception.setErrors(errors);

                    throw exception;
                }

                List<BidEntry> bidEntries = new ArrayList<BidEntry>();

                // TODO: this should be rewritten to use the event bid blocks
                // calculated and set in the event in initializeEvent();

                // build an array of block end times
                int[] endTimes = new int[eventInfo.getValues().getValue().size()];
                int count = 0;

                for (EventInfoValue value : eventInfo.getValues().getValue()) {
                    if (count > 0) {
                        endTimes[count - 1] = value.getStartTime().intValue();
                    }
                    count++;
                }
                // the last ending time is the end time of the event
                endTimes[count - 1] = eventInfo.getEndTime().intValue();

                count = 0;
                boolean nonZeroBid = false;
                for (EventInfoValue value : eventInfo.getValues().getValue()) {
                    boolean inRange = false;
                    if (value.getStartTime() >= 0
                            && endTimes[count] <= eventDurationS) {
                        inRange = true;
                    }
                    TimeBlock timeBlock = new TimeBlock();
                    // TODO: non contiguous blocks should set the level
                    // to "normal" at the end of the block
                    GregorianCalendar blockStartCal = new GregorianCalendar();
                    blockStartCal.setTime(dbpEvent.getStartTime());
                    blockStartCal.add(Calendar.SECOND, value.getStartTime()
                            .intValue());
                    timeBlock.setStartHour(blockStartCal.get(Calendar.HOUR_OF_DAY));
                    timeBlock.setStartMinute(blockStartCal.get(Calendar.MINUTE));
                    timeBlock.setStartSecond(blockStartCal.get(Calendar.SECOND));
                    GregorianCalendar blockEndCal = new GregorianCalendar();
                    blockEndCal.setTime(dbpEvent.getStartTime());
                    blockEndCal.add(Calendar.SECOND, endTimes[count]);
                    timeBlock.setEndHour(blockEndCal.get(Calendar.HOUR_OF_DAY));
                    timeBlock.setEndMinute(blockEndCal.get(Calendar.MINUTE));
                    timeBlock.setEndSecond(blockEndCal.get(Calendar.SECOND));

                    BidEntry bidEntry = new BidEntry();
                    if (inRange) {
                        bidEntry.setActive(true);
                        nonZeroBid = true;
                    } else {
                        bidEntry.setActive(false);
                    }
                    bidEntry.setBlockStart(blockStartCal.getTime());
                    bidEntry.setBlockEnd(blockEndCal.getTime());
                    // TODO: level shouldn't be part of bid entry
                    bidEntry.setPriceLevel(0.0);
                    bidEntry.setReductionKW(value.getValue());
                    bidEntries.add(bidEntry);
                    count++;
                }
                if (!nonZeroBid) {
                    // if all the bids are zero, then remove the participant from
                    // the event

                    // TODO: this would be more efficient if we could look up
                    // the participant directly instead of iterating
                    Iterator<EventParticipant> i = dbpEvent.getParticipants()
                            .iterator();
                    while (i.hasNext()) {
                        EventParticipant eventParticipant = i.next();
                        if (participantName.equals(eventParticipant
                                .getParticipant().getParticipantName())) {
                            i.remove();

                            log.debug(LogUtils.createLogEntry(programName,
                                    LogUtils.CATAGORY_EVENT, "removing event "
                                            + eventName + " participant "
                                            + participantName
                                            + " will all zero bids", ""));
                            break; // while loop
                        }
                    }
                } else {
                    // validate the bid entries
                    DBPNoBidValidator validator = (DBPNoBidValidator) ValidatorFactory
                            .getProgramValidator(program);
                    validator.validateCurrentBids(program, bidEntries);

                    // convert and set the bids
                    Set<EventParticipantBidEntry> bidEntrySet = new HashSet<EventParticipantBidEntry>();
                    for (BidEntry entry : bidEntries) {
                        EventParticipantBidEntry entryDao = new EventParticipantBidEntry();
                        entryDao.setActive(entry.isActive());
                        entryDao.setEndTime(entry.getBlockEnd());
                        entryDao.setEventParticipant(eventPart);
                        entryDao.setPriceLevel(entry.getPriceLevel());
                        entryDao.setReductionKW(entry.getReductionKW());
                        entryDao.setStartTime(entry.getBlockStart());
                        bidEntrySet.add(entryDao);
                    }
                    eventPart.setBidEntries(bidEntrySet);
                    eventPart.setBidState(ParticipantBidState.Accepted);
                }
            }
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public int process(SCEFTPConfig config, boolean lastScan) {
    	int result=RETURN_FAILTURE;
		SimpleDateFormat format=new SimpleDateFormat(config.getFilenameTemplate());
		String filename=format.format(new Date());

        try {
        	String fileString=getRemoteFile(config, filename);
			if (fileString==null || fileString.trim().length()==0) {
				if (fileString!=null) {
					String message="Empty file " + filename;
	        		sendDBPEventFileNotifications(PROGRAM_NAME, filename, false, null,
		                  null, message, true);        		
	        		return result;
				}
				if (!config.getUpload() && lastScan && config.getRequired()) {
					String message="No available event file exists from " + config.getScanStartTime()+" to "+config.getScanEndTime();
	        		sendDBPEventFileNotifications(PROGRAM_NAME, null, false, null,
		                  null, message, true);        		
				}
        		return result;
			}
			
			parseFile(fileString, filename, true);
			
			result=RETURN_SUCCESS;
		} catch (AppServiceException e) {
			if (!config.getConnErrorNotified()) {
				sendNotification(PROGRAM_NAME, "FTP server isn't reachable("+getUtilityName()+" DBP Event Auto Dispatch)", e.getMessage());
//	          sendDBPEventFileNotifications(PROGRAM_NAME, filename, false, null,
//	        		  null, e.getMessage(), true);
				result=RETURN_FTP_CONNECT_FAILTURE;
			}
		} catch (ProgramValidationException e) {
            sendDBPEventFileNotifications(PROGRAM_NAME, filename, false, null,
                    e.getErrors(), null, true);
		} catch(Exception e) {
			sendDBPEventFileNotifications(PROGRAM_NAME, filename, false, null,
	        		  null, MessageUtil.getErrorMessage(e), true);
		}
    	
        return result;
    }
    
    
    @Override
    public void createEvent(String fileString, String filename) throws ProgramValidationException, CreationFailureException{
    	try {
			parseFile(fileString, filename, false);
		} catch (ProgramValidationException e) {
            sendDBPEventFileNotifications(PROGRAM_NAME, filename, false, null,
                    e.getErrors(), null, false);
            
            throw e;
		} catch(Exception e) {
			sendDBPEventFileNotifications(PROGRAM_NAME, filename, false, null,
	        		  null, MessageUtil.getErrorMessage(e), false);
			throw new CreationFailureException(MessageUtil.getErrorMessage(e));
		}
    }
    
    public void parseFile(String fileString, String filename, boolean auto) throws ProgramValidationException, CreationFailureException{
        	if (checkScheduledEvent()) {
        		String message="No event created due to one or more scheduled events exist in the system";
        		throw new CreationFailureException(message);
    		} else {
				SCEDBPEventFileParser parser=new SCEDBPEventFileParser(fileString, filename);
				dispatchEvent(parser, auto);
    		}
    }
    
    private String getRemoteFile(SCEFTPConfig config, String filename) throws AppServiceException{
    	SCEFTPClient ftpClient=null;
    	String fileString;
    	try {
	    	ftpClient=new SCEFTPClient(config.getHost(), config.getPort(), config.getUsername(), config.getPassword());
	    	ftpClient.setFilename(filename);
	    	ftpClient.setBackupPath(config.getBackupPath());
	
	    	ftpClient.connect();
	    	fileString=ftpClient.getFileContents();
	    	
	    	if (fileString!=null)
	    		ftpClient.backupEventFile(fileString);
    	}finally{
			if (ftpClient!=null)
				ftpClient.close();
    	}
    	return fileString;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void dispatchEvent(SCEDBPEventFileParser parser, boolean auto) throws ProgramValidationException, CreationFailureException {
        UtilityDREvent utilityDREvent = createUtilityDREvent(parser);

        Event event = createNewEvent(utilityDREvent);
        
        if(systemManager.getPss2Properties().getUtilityDisplayName().equalsIgnoreCase("SCE")){
        
			if (event.getProgramName().equalsIgnoreCase(PROGRAM_NAME)) {
				List<Event> events=eventEAO.findEventOnlyByProgramName(PROGRAM_NAME);
				for (Event ev : events) {
					if ((ev.getStartTime().getTime() == parser.getStartTime()
							.getTime())
							&& (ev.getEndTime().getTime() == parser
									.getEndTime().getTime())) {
							event.setUUID(ev.getUUID());
						break;
					}
				}
			}
        }
        try {
            super.createEvent(PROGRAM_NAME, event, utilityDREvent);
            sendDBPEventFileNotifications(PROGRAM_NAME, parser.getFilename(), true, event,parser.getMessages(), null, auto);
        }catch(EJBException e){
        	Exception clause=MessageUtil.getCauseException(e);
        	if (clause instanceof ProgramValidationException) {
        		((ProgramValidationException)clause).getErrors().addAll(parser.getMessages());
        		throw (ProgramValidationException)clause;
        	}
        	
        	throw new CreationFailureException(e.getMessage());
        }
    }
    
    protected boolean checkScheduledEvent(){
    	boolean existing=false;
    	List<Event> events=eventEAO.findEventOnlyByProgramName(PROGRAM_NAME);
    	List<Event> deletes=new ArrayList<Event>();
    	
    	if (events!=null && events.size()>0) {
    		if(systemManager.getPss2Properties().getUtilityDisplayName().equalsIgnoreCase("SCE")){
        		return existing;
    		}else{
	    		  for (Event event:events) {
					if (event.isDrEvent()) {
						deletes.add(event);
					} else if (event.getEventStatus()!=EventStatus.ACTIVE) {
		    			existing=true;
	    			}
	    		  }
    		}
    		if (deletes.size()!=0) {
    			for(Event delete:deletes) {
					try {
						eventEAO.delete(delete);
					} catch (EntityNotFoundException e) {
						log.debug(e.getMessage());
					}
    			}
    		}
    	}
    	return existing;
    }

    public Event createNewEvent(UtilityDREvent utilityDREvent) {
        if (utilityDREvent == null)
            return null;

        DBPEvent event = (DBPEvent) newProgramEvent();
        event.setManual(true);
        event.setProgramName(utilityDREvent.getProgramName());
        event.setEventName(utilityDREvent.getEventIdentifier());
        final UtilityDREvent.EventTiming timing = utilityDREvent
                .getEventTiming();
        if (timing != null) {
            event.setReceivedTime(new Date());
            event.setIssuedTime(timing.getNotificationTime()
                    .toGregorianCalendar().getTime());
            event.setStartTime(timing.getStartTime().toGregorianCalendar()
                    .getTime());
            event.setEndTime(timing.getEndTime().toGregorianCalendar()
                    .getTime());
            event.setRespondBy(event.getStartTime());
            event.setDrasRespondBy(event.getStartTime());
            event.setCurrentBidState(BidState.PROCESSING_COMPLETE);
        }

        // EventParicipant: participants and clients
        List<EventParticipant> participants = new ArrayList<EventParticipant>();
        List<String> accountList=new ArrayList<String>();
        
        final UtilityDREvent.EventInformation eventInformation = utilityDREvent
                .getEventInformation();
        if (eventInformation != null) {
            for (org.openadr.dras.eventinfo.EventInfoInstance eventInfo : eventInformation
                    .getEventInfoInstance()) {
                final org.openadr.dras.eventinfo.EventInfoInstance.Participants parts = eventInfo
                        .getParticipants();
                if (parts != null && parts.getAccountID() != null
                        && parts.getAccountID().size() > 0) {
                    Participant participant = participantManager
                            .getParticipantByAccount(parts.getAccountID()
                                    .get(0));
                    EventParticipant eventParticipant = new EventParticipant();
                    eventParticipant.setParticipant(participant);
                    eventParticipant.setEvent(event);
                    eventParticipant.setAggregator(true);
                    participants.add(eventParticipant);
                    
                    accountList.add(participant.getAccountNumber());

                }
            }
        }
        
        //aggregator participant
        participants.addAll(createEventAggregatorParticipant(event, accountList));
        
        event.setParticipants(participants);
        return event;
    }

    protected List<EventParticipant> createEventAggregatorParticipant(Event event, List<String> accountList){
    	List<EventParticipant> eps=new ArrayList<EventParticipant>();
    	List<Participant> parts=programPartticipantManager.getParticipantsForProgramAsObject(PROGRAM_NAME);
    	for (Participant part:parts) {
    		if (part.getAggregator() && !accountList.contains(part.getAccountNumber())) {
    			EventParticipant ep=new EventParticipant();
    			ep.setParticipant(part);
    			ep.setEvent(event);
    			ep.setAggregator(true);
    			eps.add(ep);
    		}
    	}
    	
    	return eps;
    }

	public UtilityDREvent createUtilityDREvent(SCEDBPEventFileParser parser) 
    	throws ProgramValidationException {
    	
    	UtilityDREvent drEvent=new UtilityDREvent();
    	drEvent.setProgramName(PROGRAM_NAME);
        drEvent.setEventIdentifier(EventUtil.getUniqueEventName(PROGRAM_NAME));

        EventTiming eventTiming = new EventTiming();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getDefault());
        
        cal.setTime(parser.getStartTime());
        eventTiming.setStartTime(new XMLGregorianCalendarImpl(cal));
        
        cal.setTime(parser.getEndTime());
        eventTiming.setEndTime(new XMLGregorianCalendarImpl(cal));

        eventTiming.setNotificationTime(new XMLGregorianCalendarImpl(
                new GregorianCalendar()));
        drEvent.setEventTiming(eventTiming);

        BiddingInformation biddingInformation = new BiddingInformation();
        biddingInformation.setOpeningTime(eventTiming.getStartTime());
        biddingInformation.setClosingTime(eventTiming.getStartTime());
        drEvent.setBiddingInformation(biddingInformation);
        
        EventInformation eventInformation = parseBidEntry(parser, drEvent.getProgramName());

        if (eventInformation.getEventInfoInstance().size() != 0) {
        	drEvent.setEventInformation(eventInformation);
        } else {
        	List<ProgramValidationMessage> errors=new ArrayList<ProgramValidationMessage>();
            errors.addAll(parser.getMessages());

            ProgramValidationException error = new ProgramValidationException();
            error.setErrors(errors);
            throw error;
        }

        return drEvent;
    }

    public EventInformation parseBidEntry(SCEDBPEventFileParser parser, String programName) {

        SimpleDateFormat format=new SimpleDateFormat("HH:mm");
        EventInformation eventInformation = new EventInformation();
        
    	DBPProgram program=(DBPProgram)programManager.getProgramWithLoadBid(programName);
    	Set<BidBlock> programBidBlocks=program.getBidConfig().getBidBlocks();

        Date eventDate=DateUtil.getStartOfDay(parser.getStartTime());
        double eventStartTimeS = (DateUtil.stripMillis(parser.getStartTime()).getTime()-eventDate.getTime())/DateUtil.MSEC_IN_SEC;
        double eventEndTimeS = (DateUtil.stripMillis(parser.getEndTime()).getTime()-eventDate.getTime())/DateUtil.MSEC_IN_SEC;
        
        boolean available=true;
        for (SCEDBPBidEntry entry: parser.getBidEntryList()){
            String serviceAccountNumber = entry.getServiceAccountNumber();
            // check accounts with leading zeros
            if (serviceAccountNumber.startsWith("0"))
                if (!checkAccountNumber(programName,serviceAccountNumber))
                // DRMS-5870 trim leading zero from ftp file
                serviceAccountNumber = StringUtil.trimLeadZeros(serviceAccountNumber);

            // confirm that the bid blocks exactly fit between the start and
            // end times
        	List<SCEDBPBidEntry.BidBlock> blocks=entry.getBidBlocks();
        	int size=blocks.size();
            if (blocks.get(0).getBidStartTime().getTime() !=parser.getStartTime().getTime() ||
        			blocks.get(size-1).getBidEndTime().getTime()!=parser.getEndTime().getTime()) {
        		
                ProgramValidationMessage error = new ProgramValidationMessage();
                StringBuffer desc = new StringBuffer();
                desc.append("Bid blocks for account number ");
                desc.append(serviceAccountNumber);
                desc.append(" don't fit exactly between ");
                desc.append(format.format(parser.getStartTime()));
                desc.append(" and ");
                desc.append(format.format(parser.getEndTime()));
                error.setDescription(desc.toString());
                error.setParameterName("BidBlockError");
                parser.getMessages().add(error);
        		continue;
            }

            int bidBlockIndex = 0;
            for (BidBlock programBidBlock : programBidBlocks) {
            	SCEDBPBidEntry.BidBlock block = blocks.get(bidBlockIndex);
                TimeBlock bb = DBPUtils.getTimeBlock(programBidBlock);
                
                Calendar cal=Calendar.getInstance();
                cal.setTime(block.getBidStartTime());
                cal.set(Calendar.HOUR_OF_DAY, bb.getStartHour());
                cal.set(Calendar.MINUTE, bb.getStartMinute());
                cal.set(Calendar.SECOND, bb.getStartSecond());
                
                int startTimeCompare=compareDateTime(block.getBidStartTime(), 
                		bb.getStartHour(), bb.getStartMinute(), bb.getStartSecond());
                if (startTimeCompare<0) {

                    ProgramValidationMessage error = new ProgramValidationMessage();
                    StringBuffer desc = new StringBuffer();
                    desc.append("Bid block ");
                    desc.append(block.toString());
                    desc.append(" for account number ");
                    desc.append(serviceAccountNumber);
                    desc.append(" is not a subset of program bid blocks");
                    error.setDescription(desc.toString());
                    error.setParameterName("BidBlockError");
                    parser.getMessages().add(error);
                    available=false;
            		break;
                }
                if (startTimeCompare == 0) {
                    if (compareDateTime(block.getBidEndTime(), 
                    		bb.getEndHour(), bb.getEndMinute(), bb.getEndSecond()) != 0) {
                        ProgramValidationMessage error = new ProgramValidationMessage();
                        StringBuffer desc = new StringBuffer();
                        desc.append("Bid block ");
                        desc.append(block.toString());
                        desc.append(" for account number ");
                        desc.append(serviceAccountNumber);
                        desc.append(" is not a subset of program bid blocks");
                        error.setDescription(desc.toString());
                        error.setParameterName("BidBlockError");
                        parser.getMessages().add(error);
                        available=false;
                		break;
                    }
                    // exact match, so go on to the next of each
                    bidBlockIndex++;
                    if (bidBlockIndex == size) {
                        // all done with bid blocks, so exit loop
                        break;
                    }
                }
            }
            if (!available) continue;
            
            if (!checkAccountNumber(programName, serviceAccountNumber)) {
				ProgramValidationMessage warning = new ProgramValidationMessage();
				StringBuffer desc = new StringBuffer();
				desc.append("Participant with account number ");
				desc.append(serviceAccountNumber);
				desc.append(" not found or not on program ");
				desc.append(programName);
				warning.setDescription(desc.toString());
				warning.setParameterName("AccountNumberError");
				parser.getMessages().add(warning);
				continue;
			}

            // strip quotes if they exist
            EventInfoInstance eventInfoInstance = new EventInfoInstance();
            Participants participants = new Participants();
            eventInfoInstance.setParticipants(participants);
            Values values = new Values();
            eventInfoInstance.setValues(values);
            eventInfoInstance.getParticipants().getAccountID()
                    .add(serviceAccountNumber);
//            int bidIndex = 1;
            for (SCEDBPBidEntry.BidBlock block:blocks) {
                EventInfoValue value = new EventInfoValue();
                double startTimeS = (DateUtil.stripMillis(block.getBidStartTime()).getTime()-eventDate.getTime())/DateUtil.MSEC_IN_SEC 
                	- eventStartTimeS;
                value.setStartTime(startTimeS);
                double v = block.getBidQuantity();
                value.setValue(v);
                eventInfoInstance.getValues().getValue().add(value);
//                bidIndex++;
            }
            eventInfoInstance.setEndTime(eventEndTimeS - eventStartTimeS);
            eventInformation.getEventInfoInstance().add(eventInfoInstance);
        }
        return eventInformation;
    }

    private int compareDateTime(Date date, int hour, int minute, int second){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
    	
		return date.compareTo(cal.getTime());
    }
        
    @Override
    public void addEventParticipant(String programName, String eventName, List<EventParticipant> optInParticipants) {
    	
    	try {
			DBPProgram program = (DBPProgram) programManager.getProgramWithParticipantsAndPRules(programName);
			DBPNoBidValidator validator = (DBPNoBidValidator) ValidatorFactory.getProgramValidator(program);

			Event event = eventEAO.findEventWithParticipantsAndSignals(eventName);

			List<String> optInParticipantNames = new ArrayList<String>();
			Set<EventParticipant> optInParticipantSet = new HashSet<EventParticipant>();

			// bid validation
			for (EventParticipant ep : optInParticipants) {
				List<BidEntry> bidEntries = new ArrayList<BidEntry>();

				for (EventParticipantBidEntry entry : ep.getBidEntries()) {
					BidEntry bidEntry = new BidEntry();
					bidEntry.setActive(entry.isActive());
					bidEntry.setBlockEnd(entry.getEndTime());
					bidEntry.setBlockStart(entry.getStartTime());
					bidEntry.setParticipantName(entry.getEventParticipant().getName());
					bidEntry.setPriceLevel(entry.getPriceLevel());
					bidEntry.setProgramName(event.getProgramName());
					bidEntry.setReductionKW(entry.getReductionKW());
					bidEntries.add(bidEntry);
				}
				validator.validateDefaultBids(program, bidEntries);

				optInParticipantSet.add(ep);
			}

			Set<EventParticipant> aggregatedEventParticipants = createAggregatedEventParticipants(
					optInParticipantSet, program);
			optInParticipantSet.addAll(aggregatedEventParticipants);

			Set<EventParticipant> filteredEventParticipants = programManager
					.filterEventParticipants(optInParticipantSet, program, event, true);

			Set<EventParticipant> eventClients = createEventClients(filteredEventParticipants, program);
			filteredEventParticipants.addAll(eventClients);

			for (EventParticipant eventParticipant : filteredEventParticipants) {
				optInParticipantNames.add(eventParticipant.getParticipant().getParticipantName());
			}

			// remove optout participant from current event participants
			List<EventParticipant> toRemoves = new ArrayList<EventParticipant>();
			for (EventParticipant eventPart : event.getEventParticipants()) {
				if (eventPart.getEventOptOut() != 0
						&& optInParticipantNames.contains(eventPart.getParticipant().getParticipantName())) {
					eventPart.getParticipant().getEventParticipants().remove(eventPart);
					toRemoves.add(eventPart);
				}
			}
			if (toRemoves.size() > 0) {
				event.getEventParticipants().removeAll(toRemoves);
			}

			EventStateCacheHelper esch = EventStateCacheHelper.getInstance();
			for (EventParticipant eventParticipant: filteredEventParticipants) {
				if (event.isActive()) {
					eventParticipant.setOptInTime(new Date());
				}
				eventParticipant.setEvent(event);
				esch.delete(eventParticipant.getParticipant().getParticipantName());				
			}
			event.getEventParticipants().addAll(filteredEventParticipants);

			processAddParticipantRulesAndSignals(program, event, filteredEventParticipants);			

			eventEAO.update(event);
			log.info(LogUtils.createLogEntry(
					event.getProgramName(),
					LogUtils.CATAGORY_EVENT,
					"New participants have been opted in to event "+event.getEventName(),
					null));

			sendOptInNotifications(event, filteredEventParticipants);
		} catch (Exception e) {
            String message = "failed to opt in participants to event " + eventName;
            log.error(message, e);
            throw new EJBException(message, e);
		}
    }
    
//    protected void processAddParticipantRulesAndSignals(Program programWithPPAndPRules, Event event,
//    		Set<EventParticipant> eventParticipantSet)
//            throws ProgramValidationException {
//		try {
//			
//			Set<EventSignal> eventSignal=event.getEventSignals();
//	        SignalDef pendingSignalDef = signalManager.getSignal("pending");
//	        SignalDef modeSignalDef = signalManager.getSignal("mode");
//	        
//			for (EventParticipant eventParticipant : eventParticipantSet) {
//				if (!eventParticipant.getParticipant().isClient()) {
//					processParticipantRulesAndSignals(programWithPPAndPRules,
//							event, null, eventParticipant,
//							null, eventSignal, new Date(),
//							pendingSignalDef, modeSignalDef);
//					
//			        for (EventParticipant ep : eventParticipantSet) {
//			        	if (ep.getParticipant().isClient() 
//			        			&& ep.getParticipant().getParent().equals(eventParticipant.getParticipant().getParticipantName())) {
//				        	for (EventParticipantSignal signal: ep.getSignals()) {
//				        		if ("pending".equals(signal.getSignalDef().getSignalName())) {
//				        			for (SignalEntry entry:signal.getSignalEntries()) {
////				        				if (event.getEventStatus().equals(EventStatus.ACTIVE))
////				        					entry.setTime(new Date());
//				        				entry.setParentSignal(signal);
//				        			}
//				        		}
//				        	}
//			        	}
//			        }
//				}
//			}
//			
//
//		} catch (Exception e) {
//			String message = "can't process rules for event:" + event;
//			log.error(message, e);
//			throw new EJBException(message, e);
//		}
//    }
//    
//    private void sendOptInNotifications(Event event, Set<EventParticipant> added) {
//            
//		String emailContentType = null;
//		String serverHost = null;
//		String utilityDisplayName = null;
//		try {
//			emailContentType = systemManager.getProperty(
//					PropertyName.EMAIL_CONTENT_TYPE).getStringValue();
//			serverHost = systemManager.getProperty(PropertyName.SERVER_HOST)
//					.getStringValue();
//			utilityDisplayName = systemManager.getProperty(
//					PropertyName.UTILITY_DISPLAY_NAME).getStringValue();
//		} catch (EntityNotFoundException ignore) {
//		}
//
//		EventEmailFormatter mailFactory = new EventEmailFormatter();
//		StringBuilder operatorMessage = new StringBuilder(
//				"The following participants have opted in:\n");
//
//		// send the participant notifications
//		for (EventParticipant eventParticipant : added) {
//			Participant participant = eventParticipant.getParticipant();
//			if (!participant.isClient()) {
//				operatorMessage.append(participant.getParticipantName());
//				operatorMessage.append("\n");
//			}
//			String subject = "Your " + utilityDisplayName + " DRAS client "
//					+ participant.getParticipantName() + " opted in event "
//					+ event.getEventName();
//			participant = participantEAO.findParticipantWithContacts(
//					participant.getParticipantName(), participant.isClient());
//			for (ParticipantContact pc : participant.getContacts()) {
//				if (pc == null
//						|| !ProgramEJBBean.wantsParticipantEventNotification(
//								eventParticipant, pc)) {
//					continue;
//				}
//				String emailContent = mailFactory.generateEmailContent(event,
//						new ArrayList<Signal>(), serverHost, emailContentType,
//						false, new Date(),null);
//				notifier.sendNotification(pc.getParticipantContactAsContact(),
//						participant.getParticipantName(), subject,
//						emailContent, emailContentType,
//						NotificationMethod.getInstance(),
//						new NotificationParametersVO(),
//						Environment.isAkuacomEmailOnly(), true, false,
//						event.getProgramName());
//			}
//		}
//
//		if (added.size() > 0) {
//			String subject = "Participants have opted in the "
//					+ systemManager.getPss2Properties().getUtilityDisplayName()
//					+ " program " + event.getProgramName() + " event "
//					+ event.getEventName();
//
//			ProgramEJBBean.sendDRASOperatorEventNotification(subject,
//					operatorMessage.toString(),
//					NotificationMethod.getInstance(),
//					new NotificationParametersVO(), event, notifier);
//		}
//
//  	}
    
    private boolean checkAccountNumber(String programName, String accountNumber) {
        try {
        	List<String> accountIDs=new ArrayList<String>();
        	accountIDs.add(accountNumber);

//			log.debug("account number: " + accountNumber);
			List<Participant> participants = participantManager.getParticipantsByAccounts(accountIDs);
			for (Participant part : participants) {
					List<String> programNames=participantManager.getProgramsForParticipant(part.getParticipantName(), false);
					if (programNames.contains(programName))
						return true;
			}
        } catch (Exception e) {
            // TODO 2992
            log.error(LogUtils.createExceptionLogEntry(programName,
                    LogUtils.CATAGORY_EVENT, e));
        }
        return false;
    }

    private void reportDBPEventCreation(String programName, String filename,
            boolean success, String description, boolean auto) {
        DBPEventCreation er = new DBPEventCreation();
        er.setDate(new Date());
        er.setFileName(filename);
        er.setStatus(success);
        er.setDescription(description);
        er.setAutoCreation(auto);
        
        try {
            er = dbpEventCreationEAO.create(er);
        } catch (Exception e) {

            log.error(LogUtils.createExceptionLogEntry(programName,
                    LogUtils.CATAGORY_EVENT, e));
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendNotification(String programName, String subject, String content){
		sendDRASOperatorEventNotification(subject, content, 
				NotificationMethod.getInstance(), new NotificationParametersVO(), programName, notifier);

    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendDBPEventFileNotifications(String programName,
            String filename, boolean success, Event event,
            List<ProgramValidationMessage> validationMessages,
            String errorMessage, boolean auto) {
    	String result;
    	if (!success) 
    		result="FAILED: ";
    	else if ((validationMessages!=null && validationMessages.size()!=0)|| errorMessage!=null)
    		result = "SUCCESS WITH ERROR: ";
    	else
    		result ="SUCCESS: ";
    	
        StringBuffer subject = new StringBuffer();
        subject.append(result);
        if (auto) {
        	subject.append("SCE DBP Event Dispatch from FTP file ");
            
        	if (filename!=null)
            	subject.append(filename);
        } else {
        	subject.append("SCE DBP Event Creation manually");
        }
        

        StringBuffer content = new StringBuffer();
        if (filename!=null) {
	        content.append("File name: ");
	        content.append(filename);
	        content.append("\n");
        }

        StringBuffer desc = new StringBuffer();
        if (success) {
            desc.append("Event name: ");
            desc.append(event.getEventName());
            desc.append("\n");
            desc.append("Participants: \n");
            Set<EventParticipant> eventParticipants = event
                    .getEventParticipants();
            for (EventParticipant eventParticipant : eventParticipants) {
                if (!eventParticipant.getParticipant().isClient()) {
                    desc.append("  ");
                    desc.append(eventParticipant.toOperatorString());
                    desc.append("\n");
                }
            }
        }
        
        content.append("Creation time: ");
        content.append(new Date().toString());
        content.append("\n");

        if (errorMessage != null && errorMessage.trim().length() != 0) {
            desc.append("Exception: ");
            desc.append(errorMessage);
        }
        
        if (validationMessages != null && validationMessages.size() != 0) {
            desc.append("Validation Error:\n");
            for (ProgramValidationMessage message : validationMessages) {
                desc.append("  " + message.getParameterName());
                desc.append(": ");
                desc.append(message.getDescription());
                desc.append(";\n");
            }
        }
        
        content.append(desc);

		sendDRASOperatorEventNotification(subject.toString(), content.toString(), 
				NotificationMethod.getInstance(), new NotificationParametersVO(), programName, notifier);
        // TODO 2992
        if (success) {
            log.info(LogUtils.createLogEntry(programName,
                    LogUtils.CATAGORY_EVENT, subject.toString(),
                    content.toString()));
        } else {
            log.error(LogUtils.createLogEntry(programName,
                    LogUtils.CATAGORY_EVENT, subject.toString(),
                    content.toString()));
        }
        reportDBPEventCreation(programName, filename, success, desc.toString(), auto);
    }

    public static synchronized void sendDRASOperatorEventNotification(
            String subject, String content, NotificationMethod method,
            NotificationParametersVO params, String programName, Notifier notifier) {
        // bug 761
        ContactManager cm = EJB3Factory.getLocalBean(ContactManager.class);

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
                    method, params, Environment.isAkuacomEmailOnly(),
                    programName);
        }
    }
    
    private String getUtilityName() {
    	String utilityName = systemManager.getPss2Properties().getUtilityName();
    	if (utilityName != null) {
    		utilityName = utilityName.toUpperCase();
    	} else {
    		utilityName = "";
    	}
    	return utilityName;
    }

}
