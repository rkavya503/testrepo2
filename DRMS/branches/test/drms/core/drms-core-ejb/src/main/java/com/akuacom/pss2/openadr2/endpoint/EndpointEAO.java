package com.akuacom.pss2.openadr2.endpoint;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;
import com.akuacom.pss2.participant.Participant;

public interface EndpointEAO extends BaseEAO<Endpoint> {
	@Remote
    public interface R extends EndpointEAO {}
    @Local
    public interface L extends EndpointEAO {}
    
    public void createEndpointParticipantLink(EndpointMapping link);
    public void deleteEndpointParticipantLink(EndpointMapping erLink);
    public EndpointMapping findEndpointParticipantLinkByParticipant(Participant resource);
    public EndpointMapping findEndpointParticipantLinkByUUId(String uuid);
	//public EndpointMapping findEndpointResourceLinkByResourceEntityId(String resourceuuId);
	public EndpointMapping findEndpointParticipantLinkByParticipantUuid(String participantUuid);
	public EndpointMapping findEndpointParticipantLinkByVenId(String venId);
	public List<EndpointMapping> findEndpointParticipantLinksByEndpointVenId(String venId);
	public List<EndpointMapping> findEndpointParticipantLinkByEndpointUUId(String endpointUuId);
    
    public List<Endpoint> findEndpointsByCriteria(String venName, String certcommonname, String fingerprint, ItemListRequest itReq);
    public Endpoint findByVenId(String venId);
    public Endpoint findByName(String name);
    public EndpointMapping findByParticipantName(java.lang.String name);
    public Endpoint findByVenIdAndRegistrationID(String venId, String regId);
    public List<Endpoint> findAll();
}
