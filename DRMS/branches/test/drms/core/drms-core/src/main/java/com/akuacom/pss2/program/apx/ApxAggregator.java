package com.akuacom.pss2.program.apx;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import com.akuacom.ejb.VersionedEntity;

/**
 * @author Ram Pandey
 */

@Entity
@Table(name="apxaggregator")
@NamedQueries({
	@NamedQuery(name="ApxAggregator.findAll", query="SELECT aggregator FROM ApxAggregator aggregator"),
	@NamedQuery(name="ApxAggregator.findByApxEventId", query="SELECT aggregator FROM ApxAggregator aggregator WHERE aggregator.apxEventId=:eventId"),
	@NamedQuery(name="ApxAggregator.findByApxEventIdAndAggrClientId", query="SELECT aggregator FROM ApxAggregator aggregator WHERE aggregator.apxEventId=:eventId AND aggregator.aggregatorClientIdMappedWithEndpoint=:clientId"),
	//@NamedQuery(name="ApxAggregator.findByApxEventIdApxEventUUIDAndAggrClientId", query="SELECT aggregator FROM ApxAggregator aggregator WHERE aggregator.apxEventId=:eventId AND aggregator.aggregatorClientIdMappedWithEndpoint=:clientId"),
	@NamedQuery(name="ApxAggregator.deleteByApxEventId", query="DELETE FROM ApxAggregator aggregator WHERE aggregator.apxEventId=:eventId")
})
public class ApxAggregator extends VersionedEntity {
	
	private static final long serialVersionUID = 3363535843366175689L;
	private String aggregatorName;
	private String aggregatorAccountNumber;
	private String aggregatorClientIdMappedWithEndpoint;
	private String apxEventId;
	private Date apxEventStartTime;
	private Date apxEventEndTime;
	private Date apxEventIssueTime;
	@Column(name = "aggregatorResources", columnDefinition="LONGTEXT")
	private String aggregatorResources;
	private String apxEventUuid;
	public String getApxEventUuid() {
		return apxEventUuid;
	}
	public void setApxEventUuid(String apxEventUuid) {
		this.apxEventUuid = apxEventUuid;
	}
	public String getAggregatorName() {
		return aggregatorName;
	}
	public void setAggregatorName(String aggregatorName) {
		this.aggregatorName = aggregatorName;
	}
	public String getAggregatorAccountNumber() {
		return aggregatorAccountNumber;
	}
	public void setAggregatorAccountNumber(String aggregatorAccountNumber) {
		this.aggregatorAccountNumber = aggregatorAccountNumber;
	}
	public String getAggregatorClientIdMappedWithEndpoint() {
		return aggregatorClientIdMappedWithEndpoint;
	}
	public void setAggregatorClientIdMappedWithEndpoint(
			String aggregatorClientIdMappedWithEndpoint) {
		this.aggregatorClientIdMappedWithEndpoint = aggregatorClientIdMappedWithEndpoint;
	}
	public String getApxEventId() {
		return apxEventId;
	}
	public void setApxEventId(String apxEventId) {
		this.apxEventId = apxEventId;
	}
	public Date getApxEventStartTime() {
		return apxEventStartTime;
	}
	public void setApxEventStartTime(Date apxEventStartTime) {
		this.apxEventStartTime = apxEventStartTime;
	}
	public Date getApxEventEndTime() {
		return apxEventEndTime;
	}
	public void setApxEventEndTime(Date apxEventEndTime) {
		this.apxEventEndTime = apxEventEndTime;
	}
	public Date getApxEventIssueTime() {
		return apxEventIssueTime;
	}
	public void setApxEventIssueTime(Date apxEventIssueTime) {
		this.apxEventIssueTime = apxEventIssueTime;
	}
	public Set<String> getAggregatorResources() {
		Set<String> strList = new TreeSet<String>();
		if(null == this.aggregatorResources || this.aggregatorResources.isEmpty()){
			return strList;
		}
		strList.addAll(Arrays.asList(this.aggregatorResources.split(",")));
		return  strList;
	}
	public void setAggregatorResources(Set<String> aggregatorResources) {
		if(null == aggregatorResources || aggregatorResources.isEmpty()){
			return;
		}
		this.aggregatorResources = StringUtils.join(aggregatorResources.toArray(), ",");
	}
}
