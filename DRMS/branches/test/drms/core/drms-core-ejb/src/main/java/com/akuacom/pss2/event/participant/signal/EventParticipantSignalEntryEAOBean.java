package com.akuacom.pss2.event.participant.signal;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class EventParticipantSignalEntryEAOBean extends EventParticipantSignalEntryGenEAOBean
        implements EventParticipantSignalEntryEAO.R, EventParticipantSignalEntryEAO.L {

    @SuppressWarnings({"unchecked"})
    @Override
    public List<EventParticipantSignalEntry> getAllUnexecutedSignalEntries(long now) {
        final Query query = em.createNamedQuery("EventParticipantSignalEntry.findAllUnexecutedSignalEntries")
                .setParameter("now", new Date(now));

        return (List<EventParticipantSignalEntry>) query.getResultList();
    }
    
    @Override
    public Date getLatestSignalStartTimeInEvent(String eventName) {
        final Query query = em.createNamedQuery("EventParticipantSignalEntry.getLatestSignalStartTimeInEvent")
                .setParameter("eventName", eventName);
        return (Date)query.getSingleResult();
    }
    
}
