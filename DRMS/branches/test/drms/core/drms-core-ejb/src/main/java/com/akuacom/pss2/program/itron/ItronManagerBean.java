/**
 * 
 */
package com.akuacom.pss2.program.itron;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.core.ProgramEJBBean;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.util.LogUtils;

/**
 * the class APXManagerBean
 * 
 */
@Stateless
public class ItronManagerBean implements ItronManager.L, ItronManager.R {
	
    private static final Logger log = Logger.getLogger(ItronManagerBean.class);
    
	@EJB
	SystemManager.L systemManager;
	@EJB
	ParticipantManager.L partManager;

	@Override
	public void createEvent(String programName, Event event) {
		
		try {
			Set<EventParticipant> existing=event.getEventParticipants();
			if (existing==null || existing.size()==0) {
				List<Participant> parts = partManager.findParticipantsByProgramName(programName);
				if (parts != null && parts.size() > 0) {
					Set<EventParticipant> eps = new HashSet<EventParticipant>();
	
					for (Participant part : parts) {
						EventParticipant ep = new EventParticipant();
						ep.setEvent(event);
						ep.setParticipant(part);
						part.getEventParticipants().add(ep);
						eps.add(ep);
					}
					event.setEventParticipants(eps);
				}
			}
			createEvent(event);
		} catch (AppServiceException e) {
            log.error(LogUtils.createLogEntry(programName, "PDP Itron Event Creation", e.getMessage(), null));
            
            throw new EJBException(e);
		}
	}
	
	
    private Collection<String> createEvent(Event event) {
			ProgramEJB program = systemManager.lookupProgramBean(event.getProgramName());
			return program.createEvent(event.getProgramName(), event, null);
    }
    
    @Override
	public void cancelEvent(String programName, String eventId) {
		try{
			ProgramEJB program = systemManager.lookupProgramBean(programName);
			program.cancelEvent(programName, eventId);
		}catch(Exception e){
			log.error(LogUtils.createLogEntry(programName, "PDP Itron Event Cancellation Failed", e.getMessage(), null));
		}
		
	}

	
}
