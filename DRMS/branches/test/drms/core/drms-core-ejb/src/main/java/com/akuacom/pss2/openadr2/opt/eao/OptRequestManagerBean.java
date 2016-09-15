package com.akuacom.pss2.openadr2.opt.eao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.openadr2.endpoint.EndpointManager;
import com.akuacom.pss2.openadr2.endpoint.EndpointMapping;
import com.akuacom.pss2.openadr2.opt.OptRequest;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;


@Stateless
public class OptRequestManagerBean implements OptRequestManager.L, OptRequestManager.R {
	
	@EJB
	OptRequestEAO.L optRequestEAO;
	
	@EJB
	EndpointManager.L endPointManager;
	
	@EJB
	ProgramManager.L programManager;
	
	@EJB
	ParticipantManager.L resourceManager;	
	
	@EJB
	ProgramParticipantManager.L programParticipantManager;
	
	@EJB	
	ParticipantEAO.L participantEAO;
	
	public boolean createOptRequest(OptRequest optRequest){
		
		boolean status = true;
		try{
			optRequestEAO.create(optRequest);
		}catch(Exception e){
			e.printStackTrace();
			status = false;
		}		
		return status;
	}
	public boolean cancelOptRequest(String optId){
		boolean status = true;
		try {
			List<OptRequest> optRequests = optRequestEAO.getByOptId(optId);
			if(null == optRequests || 0 == optRequests.size()){
				return false;
			}
			for(OptRequest optReq : optRequests ){
				optReq.setCancelled(true);
				optRequestEAO.update(optReq);
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		}
		return status;
	}
	
	
	public List<Participant> findResourcesByGroupName(List<String> groupNameList){
		List<Participant> resourceList = new ArrayList<Participant>();
		if(null == groupNameList){
			return resourceList;
		}
		/*for(String groupName : groupNameList){
			List<Participant> tmpResourceList = null;// groupManager.findResourcesByGroupName(groupName);
			if(null == tmpResourceList){
				continue;
			}
			resourceList.addAll(tmpResourceList);
		}*/
		return resourceList;
	}
	
	public List<Participant> findResourcesByPartyName(List<String> partyNameList){
		List<Participant> resourceList = new ArrayList<Participant>();
		if(null == partyNameList){
			return resourceList;
		}
		for(String partyName : partyNameList){
			resourceList.addAll(findResourceByPartyName(partyName));
		}
		return resourceList;
	}
	
	private List<Participant> findResourceByPartyName(String partyName){
		Participant party= null;
		List<Participant> resourceList = new ArrayList<Participant>();
		party = resourceManager.getParticipant(partyName, true);
		
		if(null == party){
			return resourceList;
		}
		 List<String> clientNamesByParticipant = resourceManager.getClientNamesByParticipant(party.getUUID());
		if(null == clientNamesByParticipant){
			return  resourceList;
		}
		
		for(String client :clientNamesByParticipant){
			Participant findByNameAndClient = participantEAO.findByNameAndClient(client, true);
			resourceList.add(findByNameAndClient);
		}		
		return resourceList;
	}
	
	public List<Participant> findResourcesByVenId(List<String> venIdList){
		List<Participant> resourceList = new ArrayList<Participant>();
		if(null == venIdList){
			return resourceList;
		}
		for(String venId : venIdList){
			resourceList.addAll(findResourceByVenId(venId));
		}
		return resourceList;
	}
	
	private List<Participant> findResourceByVenId(String venId){
		/*Endpoint endPoint =null;
		try {
			endPoint = endPointManager.findByVenId(venId);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		if(null == endPoint){
			return resourceList;
		}*/
		
		List<Participant> resourceList = new ArrayList<Participant>();
		List<EndpointMapping> endPointResourceLinkList = endPointManager.findEndpointParticipantLinksByEndpointVenId(venId);
		if(endPointResourceLinkList ==null){
			return resourceList;
		}
		
		for(EndpointMapping endPointResourceLink : endPointResourceLinkList){
			resourceList.add(endPointResourceLink.getParticipant());
		}
		return resourceList;
	}
	
	public List<Participant> findResourcesByProgramName(List<String> programNameList, String venId){
		List<Participant> resourceList = new ArrayList<Participant>();
		if(null == programNameList || null == venId || venId.equals("")){
			return resourceList;
		}
		for(String programName : programNameList){
			resourceList.addAll(findResourceByProgramName(programName));
		}
		resourceList = removeResourcesNotLinkedWithVen(resourceList, venId);
		return resourceList;
	}
	
	private List<Participant> findResourceByProgramName(String programName ){
		Program program = null;
		List<Participant> resourceList = new ArrayList<Participant>();
		program = programManager.getProgramOnly(programName);
		if(null == program){
			return resourceList;
		}		
		 List<ProgramParticipant> participantsForProgram = programParticipantManager.getProgramParticipantsForProgramAsObject(program.getProgramName());
		
		 if(participantsForProgram == null){
			 return resourceList;
		 }
		for(ProgramParticipant pp:participantsForProgram){
			Participant participant = pp.getParticipant();
			if(participant.isClient()){
				resourceList.add(participant);
			}
		}	
		return resourceList;
	}	
	
	public List<OptRequest> findOptRequestByResourceEntityId(String entityId){
		return optRequestEAO.getAllIncludesResourc(entityId);
	}
	@Override
	public OptRequest update(OptRequest optRequest) throws EntityNotFoundException {
		return optRequestEAO.update(optRequest);
	}	
	public List<OptRequest> findOptRequestByOptId(String optId){
		List<OptRequest> optRequests = new ArrayList<OptRequest>();
		if(null == optId || optId.equals("")){
			return optRequests;
		}
		optRequests = optRequestEAO.getByOptId(optId);
		return optRequests;
	}
	public List<Participant> removeResourcesNotLinkedWithVen(List<Participant> resources , String venId) {
		List<Participant> resourceList = new ArrayList<Participant>();
		List<Participant> venResourceList = findResourceByVenId(venId);
		if(null == venResourceList || 0 == venResourceList.size()){
			return resourceList;
		}
		List<String> venResourceEntityIds = new ArrayList<String>();
		for(Participant resource : venResourceList){
			venResourceEntityIds.add(resource.getUUID());
		}
		for(Participant resource : resources){
			if(venResourceEntityIds.contains(resource.getUUID())){
				resourceList.add(resource);
			}
		}
		return resourceList;
	}
	
}
