package com.akuacom.pss2.program.apx.aggregator.processor;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.program.apx.queue.ApxQueueMessageProcessor;

/**
 * @author Ram Pandey
 */

public interface ApxAggregatorEventRequestProcessor  extends ApxQueueMessageProcessor{
	@Remote
    public interface R extends ApxAggregatorEventRequestProcessor {   }
    @Local
    public interface L extends ApxAggregatorEventRequestProcessor {   }

}
