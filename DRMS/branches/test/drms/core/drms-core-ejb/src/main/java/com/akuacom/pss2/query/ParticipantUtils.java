package com.akuacom.pss2.query;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;

public class ParticipantUtils {
	
	/**
	 * this helper method is to convert participant candidate to event participant during event creation 
	 * @param event - event to be created 
	 * @param candidate - the participants of the event
	 */
	public static void createEventParticipantForEvent(
				Event event,
				EvtParticipantCandidate candidate){
		Participant p = new Participant();
		p.setParticipantName(candidate.getParticipantName());
		p.setAccountNumber(candidate.getAccount());
		p.setShedPerHourKW(candidate.getRegisterShed());
		p.setUUID(candidate.getUUID());
		p.setOptOut(candidate.isParticipantOptOut());
		p.setClient(candidate.isClient());
		
		EventParticipant eventParticipant = new EventParticipant();
		
		//set value at both ends of bi-literal relationship
		//EventParticipant - Event 
		eventParticipant.setEvent(event);
		event.getEventParticipants().add(eventParticipant);
		
		//set value at both ends of bi-literal relationship
		//EventParticipant - Participant
		eventParticipant.setParticipant(p);
		p.getEventParticipants().add(eventParticipant);
		
		for(EvtClientCandidate clientCandidate:candidate.getClients()){
			Participant c = new Participant();
			c.setParticipantName(clientCandidate.getClientName());
			c.setAccountNumber(clientCandidate.getAccount());
			c.setUUID(clientCandidate.getClientUUID());
			p.setOptOut(candidate.isParticipantOptOut());
			c.setClient(clientCandidate.isClient());
			
			EventParticipant evetnClient = new EventParticipant();
			
			//set value at both ends of bi-literal relationship
			//EventParticipant - Event 
			evetnClient.setEvent(event);
			event.getEventParticipants().add(evetnClient);
			
			//set value at both ends of bi-literal relationship
			//EventParticipant - Participant
			evetnClient.setParticipant(c);
			c.getEventParticipants().add(evetnClient);
		}
	}
}
