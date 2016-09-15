package com.akuacom.pss2.event.participant;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

@Stateless
public class EventParticipantManagerBean implements EventParticipantManager.R,
        EventParticipantManager.L {

    @EJB
    EventParticipantEAO.L eventParticipantEAO;

    private static final Logger log = Logger
            .getLogger(EventParticipantManagerBean.class);

    public EventParticipant getEventParticipant(String eventName,
            String participantName, boolean isClient) {
        return eventParticipantEAO.getEventParticipant(eventName,
                participantName, isClient);
    }
    
    public List<String> getParticipantsForEvent(String eventName, boolean isClient){
        return eventParticipantEAO.findPartNamesByEvent(eventName, isClient);
    	
    }
    
    public EventParticipant updateEventParticipant(String eventName,
            String participantName, boolean isClient, EventParticipant epIn) {
        if (eventName == null || epIn.getEvent().getEventName() == null) {
            String message = "program name is null";
            throw new EJBException(message);
        }
        if (!eventName.equals(epIn.getEvent().getEventName())) {
            String message = "program names do not match";

            throw new EJBException(message);
        }
        if (participantName == null || epIn.getParticipant().getParticipantName() == null) {
            String message = "participant name is null";
            throw new EJBException(message);
        }
        if (!participantName.equals(epIn.getParticipant().getParticipantName())) {
            String message = "participant names do not match";
            throw new EJBException(message);
        }
        if (epIn.getUUID() == null || epIn.getUUID().length() < 32) {
            String message = "UUID is null";
            throw new EJBException(message);
        }

        try {
                
        	epIn = eventParticipantEAO.merge(epIn);
        	            return epIn;
                

        } catch (Exception ex) {
            String message = "error updating participant " + participantName + " in program " + eventName + " with " + epIn;
            throw new EJBException(message, ex);
        }

       
    }
    	

}
