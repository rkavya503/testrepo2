package com.akuacom.pss2.openadr2.poll.eao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.openadr2.poll.OadrPollState;


@Stateless
public class OadrPollStateEAOBean implements OadrPollStateEAO.L,OadrPollStateEAO.R {
	@PersistenceContext(unitName="core")
	protected EntityManager em;
	private Logger log = Logger.getLogger(OadrPollStateEAOBean.class);
	

	@Override
	public OadrPollState findByVenId(String venId) {
		//@SuppressWarnings("unchecked")
		OadrPollState resul = null;
		try {
			Query createNamedQuery = getEm().createNamedQuery("OadrPollState.findByVenID");
			createNamedQuery.setParameter("id", venId);
			resul = (OadrPollState)createNamedQuery.getSingleResult();
		} catch (Exception e) {
		}
		return resul;
	}

	@Override
	public List<OadrPollState> findAll() {
		Query createNamedQuery = getEm().createNamedQuery("OadrPollState.findAll");
		@SuppressWarnings("unchecked")
		List<OadrPollState> resul = createNamedQuery.getResultList();
		return resul;
	}

	@Override
	public boolean create(OadrPollState pollState) {
		if(null == pollState){
			log.error("can not insert null oadrPollState record");
			return false;
		}
		boolean status = true;
		try {
			getEm().persist(pollState);
		} catch (Exception e) {
			log.error("Error in creating new oadrPollState record");
			status = false;
		}
		return status;
	}

	@Override
	public void update(OadrPollState pollState) throws EntityNotFoundException {
		if(null == pollState){
			return;
		}
		try {
			getEm().merge(pollState); // expect to have valid uuid
		} catch (Exception e) {
			log.error("Error in updating oadrPollState record ");
			throw new EntityNotFoundException("Error in updating oadrPollState record");
		}
	}

	@Override
	public void deleteByVenId(String venId) throws EntityNotFoundException {
		if((null == venId) || venId.equals("")){
			return;
		}
		OadrPollState pollState = findByVenId(venId);
		if(null == pollState){
			throw new EntityNotFoundException("");
		}
		try {
			getEm().remove(pollState);
		} catch (Exception e) {
			log.error("Error in deleting oadrPollState record ");
			throw new EntityNotFoundException("Error in deleting oadrPollState record ");
		}
	}
	private EntityManager getEm() {
		return em;
	}

}
