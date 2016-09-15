package com.akuacom.pss2.program.apx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.apx.parser.APXXmlParser;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.util.LogUtils;

@Stateless
public class SdgAPXManagerBean extends APXManagerBean implements APXManager {
	private static final Logger log = Logger.getLogger(SdgAPXManagerBean.class);
	private static final String CBP_PROGRAM_CLASS = "CBP";
	protected Set<EventParticipant> constructEventParticipants(
			APXXmlParser xmlParser, Program program, Event event,
			List<ProgramValidationMessage> warnings) {
		SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
		PSS2Features features = systemManager.getPss2Features();
		boolean flag = features.isEnrollAllParticipantsEnabled();
		String programName = program.getProgramName();
		
		String programClass = program.getProgramClass();
		if(CBP_PROGRAM_CLASS.equalsIgnoreCase(programClass)&&flag){
			Set<EventParticipant> eps=new HashSet<EventParticipant>();
			List<Participant> participants = getParticipantsByProgramName(programName);
			for (Participant participant: participants) {
					EventParticipant ep=new EventParticipant();
					ep.setParticipant(participant);
					ep.setEvent(event);
					ep.setAggregator(true);
					eps.add(ep);
			}
			return eps;
		}else{
			return super.constructEventParticipants(xmlParser, program, event, warnings);
		}
		
	}
	
	private List<Participant> getParticipantsByProgramName(String programName) {
		List<Participant> results=new ArrayList<Participant>();
        try {
        	results = partManager.findParticipantsByProgramName(programName);
        } catch (Exception e) {
            log.error(LogUtils.createExceptionLogEntry(programName,LogUtils.CATAGORY_EVENT, e));
        }
        return results;
    }

}
