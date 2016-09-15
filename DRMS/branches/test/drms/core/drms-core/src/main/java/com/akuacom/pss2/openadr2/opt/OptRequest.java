package com.akuacom.pss2.openadr2.opt;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.akuacom.ejb.VersionedEntity;

@Entity
@Table(name="optrequest")
@NamedQueries({
	@NamedQuery(name="OptRequest.findAll", query="SELECT optR FROM OptRequest optR WHERE optR.cancelled= 'false'"),
	@NamedQuery(name="OptRequest.findByOptID", query="SELECT optR FROM OptRequest optR WHERE optR.optID=:id AND optR.cancelled='false'"),
	@NamedQuery(name="OptRequest.findByRequestID", query="SELECT optR FROM OptRequest optR WHERE optR.requestId=:id AND optR.cancelled='false'"),
	@NamedQuery(name="OptRequest.deleteByOptID", query="DELETE FROM OptRequest optR WHERE optR.optID=:id"),
	@NamedQuery(name="OptRequest.findAllIncludesResource", query="SELECT optR FROM OptRequest optR , OptResource optRes WHERE optRes.resourceId=:Id AND optR.cancelled='false'")
})

public class OptRequest extends VersionedEntity implements Comparable<OptRequest>, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String optID;
	private String requestId;
	private String venId;
	private String optType;
	private String optReason;
	private Date requestTime = new Date();
	private boolean cancelled;
	
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy="optRequest")
	private List<OptTarget> optTarget;
   	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy="optRequest")
	private List<OptResource> optResource;
   	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy="optRequest")
	private List<OptAvailability> optAvailability;
 
	

	public String getOptID() {
		return optID;
	}
	public void setOptID(String optID) {
		this.optID = optID;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getVenId() {
		return venId;
	}
	public void setVenId(String venId) {
		this.venId = venId;
	}
	public String getOptType() {
		return optType;
	}
	public void setOptType(String optType) {
		this.optType = optType;
	}
	public String getOptReason() {
		return optReason;
	}
	public void setOptReason(String optReason) {
		this.optReason = optReason;
	}
	
	public List<OptTarget> getOptTarget() {
		return optTarget;
	}
	public void setOptTarget(List<OptTarget> optTargetList) {
		for(OptTarget optTarget : optTargetList){
			optTarget.setOptRequest(this);
		}
		this.optTarget = optTargetList;
	}
	public List<OptResource> getOptResource() {
		return optResource;
	}
	public void setOptResource(List<OptResource> optResourceList) {
		for(OptResource optResource : optResourceList){
			optResource.setOptRequest(this);
		}
		this.optResource = optResourceList;
	}
	public List<OptAvailability> getOptAvailability() {
		return optAvailability;
	}
	public void setOptAvailability(List<OptAvailability> optAvailabilityList) {
		for(OptAvailability optAvailability : optAvailabilityList){
			optAvailability.setOptRequest(this);
		}
		this.optAvailability = optAvailabilityList;
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	public boolean getCancelled() {
		return cancelled;
	}
	public void setCancelled(boolean isCancelled) {
		this.cancelled = isCancelled;
	}
	@Override
	public int compareTo(OptRequest o) {
		return this.getCreationTime().compareTo(o.getCreationTime());
	}
	
}
