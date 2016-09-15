package com.akuacom.pss2.openadr2.poll;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.honeywell.dras.vtn.api.poll.Poll;
import com.honeywell.dras.vtn.api.poll.PollResponse;
import com.honeywell.dras.vtn.dras.service.VtnDrasServiceException;


public interface PollRequestProcessor {
	@Remote
    public interface R extends PollRequestProcessor {}
    @Local
    public interface L extends PollRequestProcessor {}
    
    public PollResponse poll(Poll poll) throws VtnDrasServiceException ;

}
