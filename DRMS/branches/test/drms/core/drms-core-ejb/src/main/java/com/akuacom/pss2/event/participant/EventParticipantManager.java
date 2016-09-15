package com.akuacom.pss2.event.participant;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * The Interface EventParticipantManager.
 */

public interface EventParticipantManager {
    @Remote
    public interface R extends EventParticipantManager {}
    @Local
    public interface L extends EventParticipantManager {}

	EventParticipant getEventParticipant(String eventName, String participantName, boolean isClient);

	List<String> getParticipantsForEvent(String eventName, boolean isClient);
	
	EventParticipant updateEventParticipant(String eventName,
            String partipantName, boolean isClient, EventParticipant pp);
	
}
