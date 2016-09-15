package com.akuacom.pss2.openadr2.endpoint;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.pss2.participant.Participant;


@Stateless
public class EndpointEAOBean extends BaseEAOBean<Endpoint> implements EndpointEAO.R, EndpointEAO.L {
	
	public EndpointEAOBean() {
		super(Endpoint.class);
	}
	
	@Override
	public void createEndpointParticipantLink(EndpointMapping link)  {
		link.setUUID(null);
    	getEm().persist(link);
	}
	
	@Override
	public EndpointMapping findEndpointParticipantLinkByParticipant(Participant participant) {
		Query q = getEm().createNamedQuery("EndpointMapping.findByParticipant");
		q.setParameter("participant", participant);
		
		EndpointMapping res = null;
		try {
			res = (EndpointMapping)q.getSingleResult();
		} catch (NoResultException nrex) {
			res = null;
		}
		
		return res;
	}
	
	
	@Override
	public EndpointMapping findEndpointParticipantLinkByUUId(String uuid) {
		Query q = getEm().createNamedQuery("EndpointMapping.findByByUuid");
		q.setParameter("UUID", uuid);
		
		return (EndpointMapping)q.getSingleResult();
	}
	
	@Override
	public EndpointMapping findEndpointParticipantLinkByVenId(String venId) {
		Query q = getEm().createNamedQuery("EndpointMapping.findByVenId");
		q.setParameter("venId", venId);
		
		EndpointMapping erl = null;
		try {
			erl = (EndpointMapping)q.getSingleResult();
		} catch (NoResultException nrex) {
			erl = null;
		}
		
		return erl;
	}
	
	@Override
	public List<EndpointMapping> findEndpointParticipantLinksByEndpointVenId(String venId) {
		Query q = getEm().createNamedQuery("EndpointMapping.findByEndpointVenId");
		q.setParameter("venId", venId);
		
		@SuppressWarnings("unchecked")
		List<EndpointMapping> erl = q.getResultList();
		
		return erl;
	}
	
	/*@Override
	public EndpointMapping findEndpointResourceLinkByResourceUUId(String resourceUUId) {
		Query q = getEm().createNamedQuery("EndpointMapping.findByResourceId");
		q.setParameter("entityId", resourceEntityId);
		
		return (EndpointMapping)q.getSingleResult();
	}*/
	
	@Override
	public EndpointMapping findEndpointParticipantLinkByParticipantUuid(String participantUuid) {
		Query q = getEm().createNamedQuery("EndpointMapping.findByParticipantUuid");
		q.setParameter("participant_uuid", participantUuid);
		
		return (EndpointMapping)q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EndpointMapping> findEndpointParticipantLinkByEndpointUUId(String endpointUuId) {
		Query q = getEm().createNamedQuery("EndpointMapping.findByEndpointUuid");
		q.setParameter("endpoint_uuid", endpointUuId);
		
		return q.getResultList();
	}
	
	@Override
	public void deleteEndpointParticipantLink(EndpointMapping erLink) {
		EndpointMapping result = getEm().find(EndpointMapping.class, erLink.getUUID());
		getEm().remove(result);
	}
	
	@Override
	public List<Endpoint> findEndpointsByCriteria(String venName, 
			String certcommonname, String fingerprint, ItemListRequest itReq) {
		
		Query q = prepQuery(venName, certcommonname, fingerprint, itReq);
		
		@SuppressWarnings("unchecked")
		List<Endpoint> res = q.getResultList();
		
		return res;
	}
	
	private Query prepQuery(String venName, String certCommonName, String fingerprint, ItemListRequest itReq) {
		StringBuilder sb = new StringBuilder("SELECT ep FROM Endpoint ep "); 
		
		if (venName != null || certCommonName != null || fingerprint != null) {
			sb.append("where canceled = false ");
		}
		
		if (venName != null) {
			venName = venName.replace('*', '%');
			sb.append("and upper(ep.venName) like upper(:venName) ");
		}
		
		if (certCommonName != null) {
			certCommonName = certCommonName.replace('*', '%');
			sb.append("and upper(ep.certCommonName) like upper(:certCommonName) ");
		}
		
		if (fingerprint != null) {
			fingerprint = fingerprint.replace('*', '%');
			sb.append("and upper(ep.fingerprint) like upper(:fingerprint) ");
		}
		
		if (itReq.getFieldToSortBy() != null) {
			sb.append("order by ");
			
			sb.append("ep.").append(itReq.getFieldToSortBy());
			
			sb.append(" ");
			if (!itReq.isSortAscending()) {
				sb.append("DESC ");
			}
		}
		
		Query q = getEm().createQuery(sb.toString());
		if (venName != null) {
			q.setParameter("venName", venName);
		}
		if (certCommonName != null) {
			q.setParameter("certCommonName", certCommonName);
		}
		if (fingerprint != null) {
			q.setParameter("fingerprint", fingerprint);
		}
		
		if (itReq.getRowsPerPage() != ItemListRequest.ALL) {
			q.setFirstResult(itReq.getRowNumber()); 
			q.setMaxResults(itReq.getRowsPerPage());
		}
		
//		System.out.println(">>>>>>>> query String=\n" + sb.toString());
//		System.out.println("         first result = " + q.getFirstResult() + ", max results = " + q.getMaxResults());
		
		return q;
	}

	@Override
	public Endpoint findByVenId(String venId) {
		Query q = em.createNamedQuery( "Endpoint.findByVenId" );
		q.setParameter("venId", venId);
		Endpoint res = null;
		try {
			res = (Endpoint)q.getSingleResult();
		} catch (NoResultException nrex) {
			res = null;
		}		
		return res;
	}
	
	@Override
	public Endpoint findByName(String name) {
		Query q = em.createNamedQuery( "Endpoint.findByName" );
		q.setParameter("name", name);		
		
		Endpoint res = null;
		try {
			res = (Endpoint)q.getSingleResult();
		} catch (NoResultException nrex) {
			res = null;
		}		
		return res;
	}
	
	public EndpointMapping findByParticipantName(java.lang.String name) {
		Query q = em.createNamedQuery( "EndpointMapping.findByParticipantName" );
		q.setParameter("participantName", name);
		EndpointMapping res = null;
		try {
			res = (EndpointMapping)q.getSingleResult();
		} catch (NoResultException nrex) {
			res = null;
		}		
		return res;
	}

	@Override
	public Endpoint findByVenIdAndRegistrationID(String venId, String regId) {
		Query q = em.createNamedQuery( "Endpoint.findByVenIdAndRegistrationId" );
		q.setParameter("venId", venId);
		q.setParameter("registrationId", regId);
		Endpoint res = null;
		try {
			res = (Endpoint)q.getSingleResult();
		} catch (NoResultException nrex) {
			res = null;
		}		
		return res;
	}

	@Override
	public List<Endpoint> findAll() {
		Query q = em.createNamedQuery( "Endpoint.findAll" );
		@SuppressWarnings("unchecked")
		List<Endpoint> resultList =  q.getResultList();
		return resultList;
	}	
}
