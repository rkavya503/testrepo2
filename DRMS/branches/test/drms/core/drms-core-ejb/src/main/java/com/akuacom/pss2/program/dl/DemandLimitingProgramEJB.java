package com.akuacom.pss2.program.dl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.rule.Rule.Threshold;


public interface DemandLimitingProgramEJB extends ProgramEJB {
    @Remote
    public interface R extends DemandLimitingProgramEJB {  }
    @Local
    public interface L extends DemandLimitingProgramEJB {  }

    public void sendParticipantDispatches(Event event, String verb,
			List<ParticipantContact> participantContacts, Threshold threshold);  
    
	public Event createEvent(Event event);
	
	public void updateEvent(Event event);
	
//	public void reportEventParticipation(Event event);
	
	public List<ParticipantContact> getActiveProgramParticipantContacts(
			Event event, Threshold threshold);
}
