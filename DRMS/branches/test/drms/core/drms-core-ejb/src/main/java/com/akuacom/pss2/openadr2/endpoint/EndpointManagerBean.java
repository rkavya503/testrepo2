package com.akuacom.pss2.openadr2.endpoint;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.participant.Participant;
import com.honeywell.dras.vtn.api.registration.CreatePartyRegistration;

@Stateless
public class EndpointManagerBean implements EndpointManager.R, EndpointManager.L {
	@EJB
	EndpointEAO.L endpointEAO;

	@Override
	public void createEndpoint(Endpoint endpoint) throws DuplicateKeyException {
		endpointEAO.create(endpoint);
	}

	@Override
	public void deleteEndpoint(Endpoint endpoint) throws EntityNotFoundException {
		endpointEAO.delete(endpoint);
	}
	
	@Override
	public Endpoint findByName(String name) throws EntityNotFoundException {
		return endpointEAO.findByName(name);
	}
	
	@Override
	public Endpoint findByVenId(String venId) throws EntityNotFoundException {
		return endpointEAO.findByVenId(venId);
	}
	
	@Override
	public Endpoint findByVenIdAndRegistrationID(String venId, String regId)
			throws EntityNotFoundException {
		return endpointEAO.findByVenIdAndRegistrationID(venId, regId);
	}
	
	@Override
	public List<EndpointMapping> findEndpointParticipantLinksByEndpointVenId(String venId) {
		return endpointEAO.findEndpointParticipantLinksByEndpointVenId(venId);
	}
	
	@Override
	public Endpoint getByUUID(String uuid) throws EntityNotFoundException {
		return endpointEAO.getById(uuid);
	}

	@Override
	public List<Endpoint> findAll() throws EntityNotFoundException {
		return endpointEAO.findAll();
	}
	
	@Override
	public void updateEndpoint(Endpoint endpoint) throws EntityNotFoundException {
		endpointEAO.update(endpoint);
	}
	
	@Override
	public void createEndpointParticipantLink(EndpointMapping link)  {
		endpointEAO.createEndpointParticipantLink(link);
	}
	
	@Override
	public EndpointMapping findEndpointParticipantLinkByParticipant(Participant participant) {
		return endpointEAO.findEndpointParticipantLinkByParticipant(participant);
	}
	
	/*@Override
	public EndpointMapping findEndpointParticipantLinkByEntityId(String entityId) {
		return endpointEAO.findEndpointParticipantLinkByUUId(entityId);
	}*/
	
	@Override
	public EndpointMapping findEndpointParticipantLinkByVenId(String venId) {
		return endpointEAO.findEndpointParticipantLinkByVenId(venId);
	}
	
	/*@Override
	public EndpointMapping findEndpointParticipantLinkByParticipantEntityId(String resourceEntityId) {
		return endpointEAO.findEndpointResourceLinkByResourceEntityId(resourceEntityId);
	}*/
	
	@Override
	public EndpointMapping findEndpointParticipantLinkByParticipantUuid(String resourceUuid) {
		return endpointEAO.findEndpointParticipantLinkByParticipantUuid(resourceUuid);
	}
	
	@Override
	public List<EndpointMapping> findEndpointParticipantLinkByEndpointUUId(String endpointuuId) {
		return endpointEAO.findEndpointParticipantLinkByEndpointUUId(endpointuuId);
	}
	
	@Override
	public void deleteEndpointParticipantLink(EndpointMapping erLink) {
		endpointEAO.deleteEndpointParticipantLink(erLink);
	}
	
	@Override
	public List<Endpoint> findEndpointsByCriteria(String venName, String certcommonname, String fingerprint, ItemListRequest itReq) {
		return endpointEAO.findEndpointsByCriteria(venName, certcommonname, fingerprint, itReq);
	}
	
	@Override
	public void updateEndpointCommStatus(String endpointVenId, boolean online) throws EntityNotFoundException {
		Endpoint ep = this.findByVenId(endpointVenId);
		
        boolean changed = false;
		if (online && ep.getCommStatus() != CommStatusEnum.ONLINE) {
			ep.setCommStatus(CommStatusEnum.ONLINE);
			ep.setCommStatusTimeStamp(new Date());
            changed = true;
		} else if (!online && ep.getCommStatus() != CommStatusEnum.OFFLINE)    {
			ep.setCommStatus(CommStatusEnum.OFFLINE);
			ep.setCommStatusTimeStamp(new Date());
            changed = true;
		}
        if (changed) {
        	this.updateEndpoint(ep);
        }
	}
	
	@Override	
	public Endpoint getEndPointInstance(CreatePartyRegistration createPartyRegistration, String venId, String regId) {
		Endpoint instance = new Endpoint();
		//instance.setEntityId(venId);
		instance.setRegistrationId(regId);
		instance.setVenId(venId);
		instance.setProfileName(createPartyRegistration.getProfileName().value());
		instance.setTransport(createPartyRegistration.getTransports().get(0).value());
		instance.setCertCommonName(createPartyRegistration.getCertCommonName());
		instance.setFingerprint(createPartyRegistration.getFingerprint());
		instance.setHttpPullMode(createPartyRegistration.getHttpPullMode());
		instance.setReportOnly(createPartyRegistration.getReportOnly());
		instance.setSchemaVersion(createPartyRegistration.getSchemaVersion());
		instance.setTransportAddress(createPartyRegistration.getTransportAddress());
		instance.setVenName(createPartyRegistration.getVenName());
		instance.setXmlSignature(createPartyRegistration.getXmlSignature());
		
		return instance;
	}	
	
	public EndpointMapping findByParticipantName(java.lang.String name) {
		return endpointEAO.findByParticipantName(name);
	}
	
}
