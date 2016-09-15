package com.akuacom.pss2.program.apx.aggregator;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.program.apx.parser.APXXmlParser;

/**
 * 
 * @author Ram Pandey
 *
 */

public interface ApxAggregatorManager {
	@Remote
    public interface R extends ApxAggregatorManager {   }
    @Local
    public interface L extends ApxAggregatorManager {   }
	public void processApxRequest(APXXmlParser xmlParser) throws Exception;

}
