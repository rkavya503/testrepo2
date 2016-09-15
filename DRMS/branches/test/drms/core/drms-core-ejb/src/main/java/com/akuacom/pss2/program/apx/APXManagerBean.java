/**
 * 
 */
package com.akuacom.pss2.program.apx;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.core.ValidatorFactory;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.email.Notifier;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.apx.common.ApxConstant;
import com.akuacom.pss2.program.apx.common.ApxManagerHelper;
import com.akuacom.pss2.program.apx.parser.APXXmlParser;
import com.akuacom.pss2.program.apx.queue.ApxEventRequestQueue;
import com.akuacom.pss2.program.apx.queue.ApxQueueData;
import com.akuacom.pss2.program.apx.validator.ApxRequestValidator;
import com.akuacom.pss2.program.dbp.DBPNoBidProgramEJBBean;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.sceftp.CreationFailureException;
import com.akuacom.pss2.program.sceftp.MessageUtil;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.StackTraceUtil;

/**
 * the class APXManagerBean
 * 
 */
@Stateless
public class APXManagerBean implements APXManager.L, APXManager.R {
	
	private static final Logger log = Logger.getLogger(APXManagerBean.class);
	
	public static final String LOG_CATEGORY="APX Event Creation";

	@EJB
	SystemManager.L systemManager;
    @EJB
    ProgramParticipantManager.L ppManager;
    @EJB 
    ParticipantManager.L partManager;
    @EJB
    Notifier.L notifier;
    @EJB
    ProgramManager.L programManager;
	@EJB
	EventEAO.L eventEAO;
	@EJB
	private ApxRequestValidator.L apxRequestValidator;
	@EJB
	private ApxEventRequestQueue.L queue;
	@EJB
	private ApxManagerHelper.L helperBean;
	
	public void processApxQueueMessage(APXXmlParser apxXmlParser){
		try {
			dispatchEvent(apxXmlParser);
		} catch (Exception e) {
			log.error(LogUtils.createLogEntry(apxXmlParser.getProgramName(), LogUtils.CATAGORY_EVENT, 
					LOG_CATEGORY, StackTraceUtil.getStackTrace(e)));
			
			sendExceptionNotifications(MessageUtil.getErrorMessage(e), null);
		}
		
	}
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void process(APXXmlParser xmlParser) throws CreationFailureException {
		try {
			//dispatchEvent(xmlParser, validateOnly);
			validateRequest(xmlParser);
			putRequestInQueue(xmlParser);
		} catch (CreationFailureException e) {
			log.error(LogUtils.createLogEntry(xmlParser.getProgramName(), LogUtils.CATAGORY_EVENT, 
					LOG_CATEGORY, StackTraceUtil.getStackTrace(e)));
			
			sendExceptionNotifications(e.getMessage(), null);
			throw e;
		} catch (Exception e) {
			log.error(LogUtils.createLogEntry(xmlParser.getProgramName(), LogUtils.CATAGORY_EVENT, 
					LOG_CATEGORY, StackTraceUtil.getStackTrace(e)));
			
			sendExceptionNotifications(MessageUtil.getErrorMessage(e), null);
			throw new CreationFailureException(MessageUtil.getErrorMessage(e));
		}
    }
    private void validateRequest(APXXmlParser xmlParser) throws Exception{
    	this.apxRequestValidator.validateApxRequest(xmlParser);
    }
    private void putRequestInQueue(APXXmlParser xmlParser) throws Exception{
    	ApxQueueData data = new ApxQueueData();
		data.setParser(xmlParser);
		data.setApxQueueMsgProcessorType(ApxConstant.APX);
		queue.apxMessageDispatch(data);
    	
    }
    private void dispatchEvent(APXXmlParser xmlParser) throws CreationFailureException, ProgramValidationException {
    	Program program=programManager.getProgramFromUtilityProgramName(xmlParser.getProgramName());
        String programName= program.getProgramName();
		Event event=new Event();
		event.setProgramName(programName);
		event.setEventName(xmlParser.getEventName());
		event.setStartTime(xmlParser.getEventStartTime());
		event.setEndTime(xmlParser.getEventEndTime());
		event.setIssuedTime(new Date());
		event.setReceivedTime(new Date());
		event.setMessage("apxservice");

		List<ProgramValidationMessage> warnings = new ArrayList<ProgramValidationMessage>();
        
        
            // Don't bother looking up participants if it's only validation
            Set<EventParticipant> eps = constructEventParticipants(xmlParser, program, event, warnings);
            eps.addAll(createAggregatorParticipant(event, xmlParser.getAccountNumbers()));
            event.setEventParticipants(eps);
        
		event.setLocations(xmlParser.getLocations());

            // Don't do the actual event creation if it's only validation
            createEvent(event);    	
            //sendEventCreationNotifications(event, warnings);
    }

	protected Set<EventParticipant> constructEventParticipants(
			APXXmlParser xmlParser, Program program, Event event,
			List<ProgramValidationMessage> warnings) {
		String programName = program.getProgramName();
		Set<EventParticipant> eps=new HashSet<EventParticipant>();
		for (String accountNumber: xmlParser.getAccountNumbers()) {
			Participant part=getParticipantByAccountNumber(programName, accountNumber);
			if (part==null) {
				ProgramValidationMessage warning = new ProgramValidationMessage();
				StringBuffer desc = new StringBuffer();
				desc.append("Participant with account number ");
				desc.append(accountNumber);
				desc.append(" not found or not in program ");
				desc.append(programName);
				warning.setDescription(desc.toString());
				warning.setParameterName("AccountNumberError");
				warnings.add(warning);
			} else {
				EventParticipant ep=new EventParticipant();
				ep.setParticipant(part);
				ep.setEvent(event);
				ep.setAggregator(true);
				eps.add(ep);
			}
		}
		return eps;
	}
	
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private Collection<String> createEvent(Event event) {
			ProgramEJB program = systemManager.lookupProgramBean(event.getProgramName());
			return program.createEvent(event.getProgramName(), event, null);
    }
    
    private Participant getParticipantByAccountNumber(String programName, String accountNumber) {
        try {
        	List<String> accountIDs=new ArrayList<String>();
        	accountIDs.add(accountNumber);

        	List<Participant> participants = partManager.getParticipantsByAccounts(accountIDs);
        	
			for (Participant part : participants) {
					List<String> programNames=partManager.getProgramsForParticipant(part.getParticipantName(), false);
					if (programNames.contains(programName))
						return part;
			}
        } catch (Exception e) {
            log.error(LogUtils.createExceptionLogEntry(programName,
                    LogUtils.CATAGORY_EVENT, e));
        }
        return null;
    }

	public List<EventParticipant> createAggregatorParticipant(Event event, List<String> accountNumbers){
    	List<EventParticipant> eps=new ArrayList<EventParticipant>();    	
    	List<Participant> parts=ppManager.getParticipantsForProgramAsObject(event.getProgramName());
    	for (Participant part:parts) {
    		 if (!accountNumbers.contains(part.getAccountNumber())) { 
    			EventParticipant ep=new EventParticipant();
    			ep.setParticipant(part);
    			ep.setEvent(event);
    			ep.setAggregator(true);
    			eps.add(ep);
    		}
    	}
    	return eps;
    }
	public void sendExceptionNotifications(String content, String programName) {
		this.helperBean.sendExceptionNotifications(content, programName);
    }
	public void sendExceptionNotifications(Exception e, String programName){
		this.helperBean.sendExceptionNotifications(e,programName);
	}
    public void sendEventCreationNotifications(Event event, List<ProgramValidationMessage> warnings){
    	this.helperBean.sendEventCreationNotifications(event,  warnings);
	}
}
