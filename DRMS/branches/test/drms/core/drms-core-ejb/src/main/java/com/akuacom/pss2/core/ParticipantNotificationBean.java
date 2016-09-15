package com.akuacom.pss2.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.email.Notifier;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEmailFormatter;
import com.akuacom.pss2.event.SceEventEmailFormatter;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.util.Environment;

@Stateless
public class ParticipantNotificationBean implements ParticipantNotification.R, ParticipantNotification.L{
	@EJB
    protected Notifier.L notifier;
	
	@EJB
    protected SystemManager.L systemManager;
	
	@EJB
	private com.akuacom.pss2.program.ProgramManager.L programManager;

	@Override
	public void sendNotificationToParticipant(EventParticipant eventParticipant,Event event, String verb,
            boolean showClientStatus, boolean isRevision, PSS2Properties pss2Props){
    	 Participant participant = eventParticipant.getParticipant();

         String stateString = "";
         String clientString = " Auto DR Client " + participant.getParticipantName() + " ";
         String salutationString = "Your ";
         String eventLiteral = " ";

         if (participant.getType() != Participant.TYPE_MANUAL
                 && showClientStatus) {
             if (participant.getStatus().intValue() == ClientStatus.OFFLINE
                     .ordinal()) {
             	stateString = " is offline and ";
                 if (participant.isManualControl()) {
                     //stateString = " is offline and in manual mode and ";
                     salutationString = "";
                     clientString = " ";
                     eventLiteral = " event ";
                     stateString = "";
                 } 
                 //else {
                 //    stateString = " is offline and ";
                 //}
             } else if (participant.isManualControl()) {
                 //stateString = " is in manual mode and ";
                 salutationString = "";
                 clientString = " ";
                 eventLiteral = " event ";
             } else if (participant.getStatus().intValue() == ClientStatus.ONLINE.ordinal()) {
             	stateString = " is online and ";
             }
         } else if (participant.getType() == Participant.TYPE_MANUAL) {
             salutationString = "";
             clientString = " ";
             eventLiteral = " event ";           	
         }

         String actionString = verb;
         if (verb.equalsIgnoreCase("created")) {
         	actionString = " has been scheduled";
         } else if (verb.equalsIgnoreCase("issued")) {
         	actionString = " has been scheduled";            	
         } else if (verb.equalsIgnoreCase("started")) {
         	actionString = " has started";            	
         } else if (verb.equalsIgnoreCase("deleted")) {
         	actionString = " has been cancelled";            	
         } else if (verb.equalsIgnoreCase("cancelled")) {
         	actionString = " has been cancelled";            	
         } else if (verb.equalsIgnoreCase("completed")) {
         	actionString = " has completed";            	
         }
         EventStateCacheHelper eventCache = EventStateCacheHelper.getInstance();
         String utilityDisplayName = eventCache.getUtilityName("utilityDisplayName");
         String subject = salutationString + utilityDisplayName
                 + clientString + stateString + event.getProgramName() + eventLiteral + actionString;

         final Set<EventParticipantSignal> participantSignals = eventParticipant
                 .getSignals();
         Set<EventSignal> eventSignals = eventParticipant.getEvent()
                 .getEventSignals();

         List<Signal> combinedSignals = new ArrayList<Signal>();
         combinedSignals.addAll(eventSignals);
         combinedSignals.addAll(participantSignals);

         String emailContentType = pss2Props.getEmailContentType();
         EventEmailFormatter mailFactory = getEventEmailFormatter(eventParticipant);
         String serverHost = pss2Props
                 .getStringValue(PSS2Properties.PropertyName.SERVER_HOST);

//         Participant p = participantEAO.findParticipantWithContacts(participant.getParticipantName(), participant.isClient());
         for (ParticipantContact pc : participant.getContacts()) {
             if (pc == null
                     || !ProgramEJBBean.wantsParticipantEventNotification(eventParticipant,
                             pc, isRevision)) {
                 continue;
             }
                 String emailContent = mailFactory.generateEmailContent(event,
                         combinedSignals, serverHost, emailContentType,
                         isRevision, null,null);
                 notifier.sendNotification(pc.getParticipantContactAsContact(),
                         participant.getParticipantName(), subject,
                         emailContent, emailContentType,
                         NotificationMethod.getInstance(),
                         new NotificationParametersVO(),
                         Environment.isAkuacomEmailOnly(), true, false,
                         event.getProgramName());
             }
    }
	
	

	private EventEmailFormatter getEventEmailFormatter(EventParticipant ep){
		String programName = ep.getEvent().getProgramName();
		String className = programManager.getProgramClassName(programName);
		if("com.akuacom.pss2.program.scertp2013.SCERTPProgramEJB2013".equalsIgnoreCase(className)){
			return new SceEventEmailFormatter("com.akuacom.pss2.core.SceEmailResourceUtil");
		}
			
		return new EventEmailFormatter();
	}
	
	/**
     * Send participant notifications.
     * 
     * @param event
     *            the event
     */
    public void sendParticipantNotifications(Event event, String verb,
            boolean showClientStatus, boolean isRevision) {
        PSS2Properties pss2Props = systemManager.getPss2Properties();

        for (EventParticipant eventParticipant : event.getParticipants()) {
        	if (eventParticipant.getEventOptOut()!=0)
        		continue;
        	
            Participant participant = eventParticipant.getParticipant();

            String stateString = "";
            String clientString = " Auto DR Client " + participant.getParticipantName() + " ";
            String salutationString = "Your ";
            String eventLiteral = " ";

            if (participant.getType() != Participant.TYPE_MANUAL
                    && showClientStatus) {
                if (participant.getStatus().intValue() == ClientStatus.OFFLINE
                        .ordinal()) {
                	stateString = " is offline and ";
                    if (participant.isManualControl()) {
                        //stateString = " is offline and in manual mode and ";
                        salutationString = "";
                        clientString = " ";
                        eventLiteral = " event ";
                        stateString = "";
                    } 
                    //else {
                    //    stateString = " is offline and ";
                    //}
                } else if (participant.isManualControl()) {
                    //stateString = " is in manual mode and ";
                    salutationString = "";
                    clientString = " ";
                    eventLiteral = " event ";
                } else if (participant.getStatus().intValue() == ClientStatus.ONLINE.ordinal()) {
                	stateString = " is online and ";
                }
            } else if (participant.getType() == Participant.TYPE_MANUAL) {
                salutationString = "";
                clientString = " ";
                eventLiteral = " event ";           	
            }

            String actionString = verb;
            if (verb.equalsIgnoreCase("created")) {
            	actionString = " has been scheduled";
            } else if (verb.equalsIgnoreCase("issued")) {
            	actionString = " has been scheduled";            	
            } else if (verb.equalsIgnoreCase("started")) {
            	actionString = " has started";            	
            } else if (verb.equalsIgnoreCase("deleted")) {
            	actionString = " has been cancelled";            	
            } else if (verb.equalsIgnoreCase("cancelled")) {
            	actionString = " has been cancelled";            	
            } else if (verb.equalsIgnoreCase("completed")) {
            	actionString = " has completed";            	
            }
            EventStateCacheHelper eventCache = EventStateCacheHelper.getInstance();
            String utilityDisplayName = eventCache.getUtilityName("utilityDisplayName");
            String subject = salutationString + utilityDisplayName
                    + clientString + stateString + event.getProgramName() + eventLiteral + actionString;

            final Set<EventParticipantSignal> participantSignals = eventParticipant
                    .getSignals();
            Set<EventSignal> eventSignals = eventParticipant.getEvent()
                    .getEventSignals();

            List<Signal> combinedSignals = new ArrayList<Signal>();
            combinedSignals.addAll(eventSignals);
            combinedSignals.addAll(participantSignals);

            String emailContentType = pss2Props.getEmailContentType();
            EventEmailFormatter mailFactory = getEventEmailFormatter(eventParticipant);;
            String serverHost = pss2Props
                    .getStringValue(PSS2Properties.PropertyName.SERVER_HOST);

            for (ParticipantContact pc : participant.getContacts()) {
                if (pc == null
                        || !ProgramEJBBean.wantsParticipantEventNotification(eventParticipant,
                                pc, isRevision)) {
                    continue;
                }
                    String emailContent = mailFactory.generateEmailContent(event,
                            combinedSignals, serverHost, emailContentType,
                            isRevision, null,null);
                    notifier.sendNotification(pc.getParticipantContactAsContact(),
                            participant.getParticipantName(), subject,
                            emailContent, emailContentType,
                            NotificationMethod.getInstance(),
                            new NotificationParametersVO(),
                            Environment.isAkuacomEmailOnly(), true, false,
                            event.getProgramName());
                }
            }
        }

}
