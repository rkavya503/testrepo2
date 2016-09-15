package com.akuacom.pss2.openadr2.endpoint;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.participant.Participant;
import com.honeywell.dras.vtn.api.registration.CreatePartyRegistration;


public interface EndpointManager  {
	@Remote
    public interface R extends EndpointManager {}
    @Local
    public interface L extends EndpointManager {}
	
	public void createEndpoint(Endpoint endpoint) throws DuplicateKeyException;
	public void deleteEndpoint(Endpoint endpoint) throws EntityNotFoundException;
	public Endpoint findByName(String name) throws EntityNotFoundException;
	public Endpoint findByVenId(String venId) throws EntityNotFoundException;
	public Endpoint findByVenIdAndRegistrationID(String venId, String regId) throws EntityNotFoundException;
	public Endpoint getByUUID(String uuid) throws EntityNotFoundException;
	public List<Endpoint> findAll() throws EntityNotFoundException ;
	public void updateEndpoint(Endpoint endPoint) throws EntityNotFoundException;
	
	public void createEndpointParticipantLink(EndpointMapping link);
	public void deleteEndpointParticipantLink(EndpointMapping erLink);
	public EndpointMapping findEndpointParticipantLinkByParticipant(Participant resource);
	//public EndpointMapping findEndpointParticipantLinkByEntityId(String entityId);
	public EndpointMapping findEndpointParticipantLinkByVenId(String venId);
	public List<EndpointMapping> findEndpointParticipantLinksByEndpointVenId(String venId);
	//public EndpointMapping findEndpointParticipantLinkByParticipantEntityId(String resourceEntityId);
	public EndpointMapping findEndpointParticipantLinkByParticipantUuid(String participantUuid);
	public List<EndpointMapping> findEndpointParticipantLinkByEndpointUUId(String endpointuuId);
	
	public List<Endpoint> findEndpointsByCriteria(String venName, String certcommonname, String fingerprint, ItemListRequest itReq);
	
	public void updateEndpointCommStatus(String endpointVenId, boolean online) throws EntityNotFoundException;
	Endpoint getEndPointInstance(CreatePartyRegistration createPartyRegistration, String venId,String regId);
	public EndpointMapping findByParticipantName(java.lang.String name);
}
