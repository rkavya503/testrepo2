package com.akuacom.pss2.openadr2.poll.eao;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.openadr2.poll.OadrPollState;


public interface OadrPollStateEAO {
	
	@Remote
	public interface R extends OadrPollStateEAO {}
	@Local
	public interface L extends OadrPollStateEAO {}
	
	public OadrPollState findByVenId(String venId);
	public List<OadrPollState> findAll();
	public boolean create(OadrPollState pollState);
	public void update(OadrPollState pollState) throws EntityNotFoundException;
	public void deleteByVenId(String venId) throws EntityNotFoundException;
	
	
}
