/**
 * 
 */
package com.akuacom.pss2.program.apx;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.program.apx.parser.APXXmlParser;
import com.akuacom.pss2.program.apx.queue.ApxQueueMessageProcessor;
import com.akuacom.pss2.program.sceftp.CreationFailureException;

/**
 * the interface APXManagerBean
 * 
 */
public interface APXManager extends ApxQueueMessageProcessor {
    @Remote
    public interface R extends APXManager {   }
    @Local
    public interface L extends APXManager {   }
	
	void process(APXXmlParser xmlParser) throws CreationFailureException;
}
