package com.akuacom.pss2.program.aggregator;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import com.akuacom.ejb.VersionedEntity;

@Entity
@Table(name="programaggregator")
@NamedQueries({
	@NamedQuery(name="ProgramAggregator.findAll", query="SELECT aggregator FROM ProgramAggregator aggregator"),
	@NamedQuery(name="ProgramAggregator.findByEventId", query="SELECT aggregator FROM ProgramAggregator aggregator WHERE aggregator.eventId=:eventId"),
	@NamedQuery(name="ProgramAggregator.deleteByEventId", query="DELETE FROM ProgramAggregator aggregator WHERE aggregator.eventId=:eventId"),
	@NamedQuery(name="ProgramAggregator.findByEventIdAndAggrClientId",query="SELECT aggregator FROM ProgramAggregator aggregator WHERE aggregator.eventId=:eventId AND aggregator.aggregatorClientIdMappedWithEndpoint=:clientId"),
	@NamedQuery(name="ProgramAggregator.findResourcesByEventIdEventUUIDAndAggrClientId",query="SELECT aggregator.aggregatorResource FROM ProgramAggregator aggregator WHERE aggregator.eventId=:eventId AND aggregator.aggregatorClientIdMappedWithEndpoint=:clientId AND aggregator.eventUuid=:eventUuid AND aggregator.aggregatorResource IN (:aggregatorResource)"),
	@NamedQuery(name="ProgramAggregator.deleteByApxEventId", query="DELETE FROM ProgramAggregator aggregator WHERE aggregator.eventId=:eventId"),
	
	
	@NamedQuery(name="ProgramAggregator.findResourcesByClientName", query="SELECT distinct(aggregator.eventId) FROM ProgramAggregator aggregator WHERE aggregator.aggregatorName=:aggregatorName"),
	@NamedQuery(name="ProgramAggregator.findEventUUIDByClientName", query="SELECT distinct(aggregator.eventUuid) FROM ProgramAggregator aggregator WHERE aggregator.aggregatorName=:aggregatorName"),
	@NamedQuery(name="ProgramAggregator.findAccNumberByClientNameAndEventID", query="SELECT aggregator FROM ProgramAggregator aggregator WHERE aggregator.aggregatorName =:aggregatorName and aggregator.eventId =:eventId"),
	@NamedQuery(name="ProgramAggregator.findAccountsByAggIdAndEventIds", query="SELECT aggregator FROM ProgramAggregator aggregator WHERE aggregator.aggregatorName =:aggregatorName and aggregator.eventId IN (:eventId)")
})
public class ProgramAggregator extends VersionedEntity {
	
	private static final long serialVersionUID = 3363535843366175690L;
	private String aggregatorName;
	private String aggregatorClient;
	private String aggregatorAccountNumber;
	private String aggregatorClientIdMappedWithEndpoint;
	private String eventId;
	private Date eventStartTime;
	private Date eventEndTime;
	private Date eventIssueTime;
	private String aggregatorResource;
	private String eventUuid;
	private String secondaryaccount;
	@Column(name = "childrenaccounts", columnDefinition="LONGTEXT")
	private String childrenaccounts;
	
	
	public String getAggregatorName() {
		return aggregatorName;
	}
	public void setAggregatorName(String aggregatorName) {
		this.aggregatorName = aggregatorName;
	}
	public String getAggregatorClient() {
		return aggregatorClient;
	}
	public void setAggregatorClient(String aggregatorClient) {
		this.aggregatorClient = aggregatorClient;
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
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public Date getEventStartTime() {
		return eventStartTime;
	}
	public void setEventStartTime(Date eventStartTime) {
		this.eventStartTime = eventStartTime;
	}
	public Date getEventEndTime() {
		return eventEndTime;
	}
	public void setEventEndTime(Date eventEndTime) {
		this.eventEndTime = eventEndTime;
	}
	public Date getEventIssueTime() {
		return eventIssueTime;
	}
	public void setEventIssueTime(Date eventIssueTime) {
		this.eventIssueTime = eventIssueTime;
	}
	public String getAggregatorResource() {
		return aggregatorResource;
	}
	public void setAggregatorResource(String aggregatorResource) {
		this.aggregatorResource = aggregatorResource;
	}
	public String getEventUuid() {
		return eventUuid;
	}
	public void setEventUuid(String eventUuid) {
		this.eventUuid = eventUuid;
	}
	public String getSecondaryaccount() {
		return secondaryaccount;
	}
	public void setSecondaryaccount(String secondaryaccount) {
		this.secondaryaccount = secondaryaccount;
	}
	public String getChildrenaccounts() {
		return childrenaccounts;
	}
	public void setChildrenaccounts(String childrenaccounts) {
		this.childrenaccounts = childrenaccounts;
	}
	
		
}
