package com.akuacom.pss2.core;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.system.property.PSS2Properties;

public interface ParticipantNotification {
	@Remote
    public interface R extends ParticipantNotification {}
    @Local
    public interface L extends ParticipantNotification {}
	
	
   void sendNotificationToParticipant(EventParticipant eventParticipant,Event event, String verb,
            boolean showClientStatus, boolean isRevision, PSS2Properties pss2Props);
   void sendParticipantNotifications(Event event, String verb,
           boolean showClientStatus, boolean isRevision);

}
