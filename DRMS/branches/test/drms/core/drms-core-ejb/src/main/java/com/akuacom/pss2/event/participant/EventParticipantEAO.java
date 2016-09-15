package com.akuacom.pss2.event.participant;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.participant.Participant;

/**
 * Remote stateless session bean that acts as a EAO layer for EventParticipant
 * access.
 */

public interface EventParticipantEAO extends EventParticipantGenEAO {
    @Remote
    public interface R extends EventParticipantEAO {}
    @Local
    public interface L extends EventParticipantEAO {}

    EventParticipant getEventParticipant(String eventName,
            String participantName, boolean isClient);
    
    void addChild(EventParticipant parent, EventParticipant child);

    Set<EventParticipant> getFlatDescendants(EventParticipant parent);

    void removeChildren(EventParticipant parent, Set<EventParticipant> children);

    void removeParent(EventParticipant child);

    Set<EventParticipant> getDescendants(EventParticipant parent);

    EventParticipant getAncestor(EventParticipant child);

    EventParticipant getRoot(EventParticipant child);

    EventParticipant getCommonAncestor(EventParticipant pp1, EventParticipant pp2);

    boolean isDescendant(EventParticipant parent, EventParticipant child);

    boolean isAncestor(EventParticipant parent, EventParticipant child);
    
    List<String> findPartNamesByEvent(java.lang.String eventName, boolean isClient);
    List<EventParticipant> findEventParticipantWithSignalsByAllClientUUID(List<String> clinets);
    List<EventParticipant> findEventParticipantWithSignalsByAllClientUUIDAndEventUUID(List<String> clinets, String eventName);
    List<EventParticipant> findEventParticipantWithSignalsByAllClientUUIDforEvent(List<String> clinets, String eventId);
    List<EventParticipant> findEventWithoutOptOut(String eventName);

	List<EventParticipant> findEventParticipantWithSignalsByAllEventIds(List<String> event_uuid);

	Map<String, List<Participant>> findEventParticipantWithParticipantByAccont(Set<String> accounts);

	Map<String, List<EventParticipant>> findEventParticipantWithSignalsByAllEventNameAndClientUUIDForEvent(List<String> participantName, Set<String> eventNames);
	List<EventParticipant> findEventParticipantForParent(String parent);

}
