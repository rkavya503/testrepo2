package com.honeywell.dras.vtn.web;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.honeywell.dras.vtn.dras.service.VtnDrasService;

public interface VTNHelper {
	@Remote
    public interface R extends VTNHelper {}
    @Local
    public interface L extends VTNHelper {}
    
    public VtnDrasService getVtnDrasService();
    
}
