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
public class PgeAPXManagerBean extends APXManagerBean implements APXManager {
	private static final Logger log = Logger.getLogger(PgeAPXManagerBean.class);
	private static final String CBP_PROGRAM_CLASS = "CBP";
	
	protected Set<EventParticipant> constructEventParticipants(
			APXXmlParser xmlParser, Program program, Event event,
			List<ProgramValidationMessage> warnings) {
		SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
		PSS2Features features = systemManager.getPss2Features();
		boolean secondaryAccountEnabled = features.isSecondaryAccountEnabled();
		String programName = program.getProgramName();
		if(!CBP_PROGRAM_CLASS.equalsIgnoreCase(program.getProgramClass())||!secondaryAccountEnabled){
			return super.constructEventParticipants(xmlParser, program, event, warnings);
		}
		//enable secondary account number:
		// 1. CBP program
		// 2. Core property secondaryAccountEnabled is true
		Set<EventParticipant> eps=new HashSet<EventParticipant>();
		for (String accountNumber: xmlParser.getAccountNumbers()) {
			Participant part=getParticipantBySecondAccountNumber(programName, accountNumber);
			if (part==null) {
				ProgramValidationMessage warning = new ProgramValidationMessage();
				StringBuffer desc = new StringBuffer();
				desc.append("Participant with account number ");
				desc.append(accountNumber);
				desc.append(" not found or not in program ");
				desc.append(programName);
				warning.setDescription(desc.toString());
				warning.setParameterName("AccountNumberError");
				warnings.add(warning);
			} else {
				EventParticipant ep=new EventParticipant();
				ep.setParticipant(part);
				ep.setEvent(event);
				ep.setAggregator(true);
				eps.add(ep);
			}
		}
		return eps;
	}
	
	private Participant getParticipantBySecondAccountNumber(String programName, String accountNumber) {
        try {
        	List<String> accountIDs=new ArrayList<String>();
        	accountIDs.add(accountNumber);
        	List<Participant> participants = partManager.getParticipantsBySecondaryAccounts(accountIDs);
			for (Participant part : participants) {
					List<String> programNames=partManager.getProgramsForParticipant(part.getParticipantName(), false);
					if (programNames.contains(programName))
						return part;
			}
        } catch (Exception e) {
            log.error(LogUtils.createExceptionLogEntry(programName,
                    LogUtils.CATAGORY_EVENT, e));
        }
        return null;
    }

}
