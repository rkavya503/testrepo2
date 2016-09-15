package com.akuacom.pss2.openadr2.opt;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.akuacom.ejb.VersionedEntity;

@Entity
@Table(name="optresource")
public class OptResource extends VersionedEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="optrequestuuid")
	private OptRequest optRequest;
	private String resourceId;
	
	
	public OptRequest getOptRequest() {
		return optRequest;
	}
	public void setOptRequest(OptRequest optRequest) {
		this.optRequest = optRequest;
	}
	public String getResourceID() {
		return resourceId;
	}
	public void setResourceID(String resourceID) {
		this.resourceId = resourceID;
	}
	

}
