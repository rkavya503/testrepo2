package com.akuacom.pss2.program.apx.queue;

import javax.ejb.Local;
import javax.ejb.Remote;


public interface ApxEventRequestQueue {
	@Remote
    public interface R extends ApxEventRequestQueue {   }
    @Local
    public interface L extends ApxEventRequestQueue {   }
    
    public void apxMessageDispatch(ApxQueueData data) throws Exception ;

}
