package com.akuacom.pss2.openadr2.opt.eao;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.openadr2.opt.OptRequest;
import com.akuacom.pss2.participant.Participant;



public interface OptRequestManager {
	
	@Remote
	public interface R extends OptRequestManager {}
	@Local
	public interface L extends OptRequestManager {}	
	
	public List<Participant> findResourcesByPartyName(List<String> partyNameList);
	public List<Participant> findResourcesByVenId(List<String> venIdList);
	public List<Participant> findResourcesByProgramName(List<String> programNameList, String venId);
	
	public boolean createOptRequest(OptRequest optRequest);
	public boolean cancelOptRequest(String optId);
	public List<OptRequest> findOptRequestByResourceEntityId(String entityId);
	public OptRequest update(OptRequest optRequest) throws EntityNotFoundException;
	public List<OptRequest> findOptRequestByOptId(String optId);
	public List<Participant> removeResourcesNotLinkedWithVen(List<Participant> resources , String venId);
}
