package com.akuacom.pss2.program.apx.queue;

import java.io.Serializable;

import com.akuacom.pss2.program.apx.parser.APXXmlParser;

/**
 * @author Ram Pandey
 */

public class ApxQueueData implements Serializable{
	
	private static final long serialVersionUID = 238160454795406079L;
	private APXXmlParser parser; 
	private String apxQueueMsgProcessorType;

	public String getApxQueueMsgProcessorType() {
		return apxQueueMsgProcessorType;
	}
	public void setApxQueueMsgProcessorType(String apxQueueMsgProcessorType) {
		this.apxQueueMsgProcessorType = apxQueueMsgProcessorType;
	}
	public APXXmlParser getParser() {
		return parser;
	}
	public void setParser(APXXmlParser parser) {
		this.parser = parser;
	}
}
