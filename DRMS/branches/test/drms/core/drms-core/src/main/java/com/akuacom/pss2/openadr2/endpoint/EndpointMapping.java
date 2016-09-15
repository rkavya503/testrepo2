package com.akuacom.pss2.openadr2.endpoint;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.akuacom.pss2.participant.Participant;

/**
 * This is the VEN
 *here Resource is Client
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "openadr2_endpoint_mapping", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"endpoint_uuid", "participant_uuid" }) })
@NamedQueries({ 
	@NamedQuery(name = "EndpointMapping.findByByUuid", query = "SELECT ep FROM EndpointMapping ep WHERE ep.UUID = :uuid"),
	@NamedQuery(name = "EndpointMapping.findByParticipantUuid", query = "SELECT ep FROM EndpointMapping ep WHERE ep.participant.UUID = :uuid"),
	@NamedQuery(name = "EndpointMapping.findByEndpointUuid", query = "SELECT ep FROM EndpointMapping ep WHERE ep.endpoint.UUID = :uuid"),
	@NamedQuery(name = "EndpointMapping.findByVenId", query = "SELECT ep FROM EndpointMapping ep WHERE ep.venId = :venId"),
	@NamedQuery(name = "EndpointMapping.findByEndpointVenId", query = "SELECT ep FROM EndpointMapping ep WHERE ep.endpoint.venId = :venId"),
	@NamedQuery(name = "EndpointMapping.findByParticipant", query = "SELECT ep FROM EndpointMapping ep WHERE ep.participant = :participant"),
	@NamedQuery(name = "EndpointMapping.findByParticipantName", query ="SELECT ep FROM EndpointMapping ep WHERE ep.participantName = :participantName")
})
public class EndpointMapping extends Link {

	private static final long serialVersionUID = 924983217192777378L;

	@ManyToOne(optional=false, fetch=FetchType.EAGER) 
	private Endpoint endpoint;
	
	@OneToOne(optional=false, fetch=FetchType.EAGER) 
    private Participant participant;
	
	private String venId;
	private String participantName;

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}
		
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public String getVenId() {
		return venId;
	}

	public void setVenId(String venId) {
		this.venId = venId;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	
}
