package com.akuacom.pss2.openadr2.opt;

import javax.ejb.Local;

import com.honeywell.dras.vtn.api.opt.CreateOpt;

public interface OptRequestProcessor {
	@Local
	public interface L extends OptRequestProcessor {}
	
	public OptRequest processCreateOpt(CreateOpt createOpt);

}
