package com.akuacom.pss2.core;


import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.JMSException;

import org.apache.log4j.Logger;

import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.system.SystemManager;

@Stateless
public class VaroliNotificationBean extends ProgramEJBBean implements VaroliNotification.L, VaroliNotification.R {
	@EJB
	private SystemManager.L systemManager;
	@EJB
	protected ParticipantEAO.L participantEAO;
	private static final Logger log = Logger.getLogger(VaroliNotificationBean.class);
	
	@Override
	public void sendVaroliParticipantNotifications(Event event, String verb) {
		boolean isVaroliiNotification=systemManager.getPss2Features().isFeatureVaroliiNotificationEnabled();
    	if (isVaroliiNotification) {    		
	        String theme;
	        Date endTime = event.getEndTime();
	        if ("started".equals(verb)) {
	            theme = "HECO:StartEvent;HECO:;VOICETALENT:M_ENG_4;";
	        } else if ("completed".equals(verb)) {
	            theme = "HECO:EndEvent;HECO:;VOICETALENT:M_ENG_4;";
	        } else if ("canceled".equals(verb)) {
	            long now = System.currentTimeMillis();
	            // don't send out event end notification since the event has not started yet.
	            if (now < event.getStartTime().getTime()) {
	                return;
	            } else {
	                endTime = new Date(now);
	            }
	            theme = "HECO:EndEvent;HECO:;VOICETALENT:M_ENG_4;";
	        } else {
	            // do nthing.
	            return;
	        }
	
	        NotificationMethod notificationMethod = NotificationMethod
	                .getInstance();
	        notificationMethod.setMedia(NotificationMethod.ENVOY_MESSAGE);
	        notificationMethod.setEmail(true);
	        notificationMethod.setVoice(true);
	        notificationMethod.setSms(true);
	
	        NotificationParametersVO np = new NotificationParametersVO();
	        np.setTheme(theme);
	        np.setEmail("energyscout@heco.com");
	        np.setProgramName("Fast DR");
	        np.setEventStartDate(event.getStartTime());
	        np.setEventEndDate(endTime);
	
	        String subject = np.getEventId() + " and " + np.getEventStartDate();
	        // TODO: combine event participants into one xml request
	        for (EventParticipant eventParticipant : event.getParticipants()) {
	            //don't send message to the participant has already Opt-out of the event.
	            if(eventParticipant.getEventOptOut()!=0) continue;
	
	            Participant p = eventParticipant.getParticipant();
	            Participant participant = participantEAO.findByName(p.getParticipantName(), p.isClient());
	
	            ArrayList<Contact> contacts = new ArrayList<Contact>();
	            Set<ParticipantContact> participantContacts = participant.getContacts();
	            for (ParticipantContact pc : participantContacts) {
	                if (pc == null || !wantsParticipantEventNotification(eventParticipant, pc))
	                    continue;
	                contacts.add(pc.getParticipantContactAsContact());
	            }
	            if (contacts.size() == 0) {
	                continue;
	            }
	
	            try {
	                notifier.sendVaroliiNotification(contacts, participant.getParticipantName(), subject, notificationMethod, np, false);
	            } catch (JMSException e) {
	                log.error(e.getMessage(), e);
	            }
	        }
    	}
	}
	

}
