package com.akuacom.pss2.event.participant.signal;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;


public interface EventParticipantSignalEntryEAO extends EventParticipantSignalEntryGenEAO {
    @Remote
    public interface R extends EventParticipantSignalEntryEAO {}
    @Local
    public interface L extends EventParticipantSignalEntryEAO {}
    /**
     * Gets the all unexecuted signal entries.
     *
     * @param now the now
     *
     * @return the all unexecuted signal entries
     */
    List<EventParticipantSignalEntry> getAllUnexecutedSignalEntries(long now);
	Date getLatestSignalStartTimeInEvent(String eventName);
}

