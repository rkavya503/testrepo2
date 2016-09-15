package com.akuacom.pss2.program.apx.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.email.Notifier;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.apx.APXManagerBean;
import com.akuacom.pss2.program.dbp.DBPNoBidProgramEJBBean;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.sceftp.MessageUtil;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.akuacom.pss2.util.LogUtils;

@Stateless
public class ApxManagerHelperBean implements ApxManagerHelper.L, ApxManagerHelper.R  {
	private static final Logger log = Logger.getLogger(ApxManagerHelperBean.class);
	private static final String LOG_CATEGORY="APX Event Creation";
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
	
	 public void sendEventCreationNotifications(Event event, List<ProgramValidationMessage> warnings){
			try {
				Event createdEvent = eventEAO.getByEventName(event.getEventName());
	    	
				StringBuilder subject=new StringBuilder();
			    EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
				String utilityName =  cache.getUtilityName("utilityDisplayName");;
				subject.append(utilityName);	
				subject.append(" APX Event " + event.getEventName());
				subject.append(" created");
				
				StringBuilder content=new StringBuilder();
				content.append("Event Name: " + createdEvent.getEventName());
				content.append("\nProgram: " + createdEvent.getProgramName());
				content.append("\nEvent Start Time: " + createdEvent.getStartTime().toString());
				content.append("\nEvent End Time: " + createdEvent.getEndTime().toString());
				
				content.append("\nParticipants: \n");
				for (EventParticipant part:createdEvent.getParticipants()) {
					if (!part.getParticipant().isClient()) {
						content.append("  ");
						content.append(part.toOperatorString());
						content.append("\n");
					}
				}
				content.append("Clients: \n");
				for (EventParticipant part:createdEvent.getParticipants()) {
					if (part.getParticipant().isClient()) {
						content.append("  ");
						content.append(part.toOperatorString());
						content.append("\n");
					}
				}
				
				if (warnings!=null && warnings.size()!=0) {
					content.append("The following account numbers do not exist in the DRAS: \n");
					for (ProgramValidationMessage msg:warnings) {
						content.append("  ");
						content.append(msg.getDescription());
						content.append(";\n");
					}
				}

				if (event.getWarnings()!=null && event.getWarnings().size()!=0) {
					content.append("The following warning messages exist: \n");
					for (ProgramValidationMessage msg:event.getWarnings()) {
						content.append("  ");
						content.append(msg.getDescription());
						content.append(";\n");
					}
				}

				//log.info(LogUtils.createLogEntry(createdEvent.getProgramName(), LOG_CATEGORY, subject.toString(), content.toString()));
				
				DBPNoBidProgramEJBBean.sendDRASOperatorEventNotification(subject.toString(), content.toString(), 
						NotificationMethod.getInstance(), new NotificationParametersVO(), createdEvent.getProgramName(), notifier);
				
			} catch (EntityNotFoundException e) {
				String desc="APX event "+event.getEventName() +" created, but failed to send notification to operator";
				log.warn(LogUtils.createLogEntry(event.getProgramName(), LOG_CATEGORY, desc, e.getMessage()));
			}
		}
	 	public void sendExceptionNotifications(String content, String programName) {

	        if (!content.startsWith("Event creation failure - ")) {
	            content = "Event creation failure - " + content;
	        }

	        DBPNoBidProgramEJBBean.sendDRASOperatorEventNotification(generateSubject(), content,
	                NotificationMethod.getInstance(), new NotificationParametersVO(), programName, notifier);

	    }
	 	public void sendExceptionNotifications(Exception e, String programName){
			StringBuilder subject=new StringBuilder();
	        String title = generateSubject();
			subject.append(title);
			
			StringBuilder content=new StringBuilder();
			if (e instanceof ProgramValidationException) {
				for (ProgramValidationMessage msg:((ProgramValidationException)e).getErrors()) {
					content.append(msg.getParameterName());
					content.append(": ");
					content.append(msg.getDescription());
					content.append("\n");
				}
			} else {
				content.append(MessageUtil.getErrorMessage(e));
			}

			DBPNoBidProgramEJBBean.sendDRASOperatorEventNotification(subject.toString(), content.toString(), 
					NotificationMethod.getInstance(), new NotificationParametersVO(), programName, notifier);
		}
	 	private String generateSubject() {
	        Date now = new Date();
	        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	        
				EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
	        	String utilityName =  cache.getUtilityName("utilityDisplayName");

	            return "Critical Exception in APX event creation web service on server " + utilityName  + " at " + format.format(now);
	    }

}
