package com.honeywell.dras.vtn.api.event;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.common.BaseClass;

@XmlRootElement
public class RequestEvent extends BaseClass {
	
	private Long replyLimit;

	/**
	 * @return the replyLimit
	 */
	public Long getReplyLimit() {
		return replyLimit;
	}

	/**
	 * @param replyLimit the replyLimit to set
	 */
	public void setReplyLimit(Long replyLimit) {
		this.replyLimit = replyLimit;
	}

}
