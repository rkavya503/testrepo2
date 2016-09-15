package com.akuacom.pss2.openadr2.opt.eao;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.openadr2.opt.OptRequest;
import com.akuacom.pss2.openadr2.opt.OptRequestGenEAO;



public interface OptRequestEAO extends OptRequestGenEAO {
	
	@Remote
	public interface R extends OptRequestEAO {}
	@Local
	public interface L extends OptRequestEAO {}
	
	
	public List<OptRequest> getAllOptRequest();
	public List<OptRequest> getAllIncludesResourc(String entityId);
	public List<OptRequest> getByOptId(String optId);
	public List<OptRequest> getByRequestId(String requestId);
/*	public void create(OptRequest optRequest);
	public void update(OptRequest optRequest);*/
	public void deleteByOptId(String optId);

}
