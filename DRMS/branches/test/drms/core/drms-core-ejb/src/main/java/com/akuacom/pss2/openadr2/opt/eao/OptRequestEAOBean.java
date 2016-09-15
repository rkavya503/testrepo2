package com.akuacom.pss2.openadr2.opt.eao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.akuacom.pss2.openadr2.opt.OptRequest;
import com.akuacom.pss2.openadr2.opt.OptRequestGenEAOBean;



@Stateless
public class OptRequestEAOBean extends OptRequestGenEAOBean implements OptRequestEAO.L , OptRequestEAO.R {
	
	public OptRequestEAOBean() {
		super(OptRequest.class);
	}
	
	public OptRequestEAOBean(Class<OptRequest> entityClass) {
		super(entityClass);
	}
	
	@Override
	public List<OptRequest> getAllOptRequest() {
		Query createNamedQuery = em.createNamedQuery("OptRequest.findAll");
		@SuppressWarnings("unchecked")
		List<OptRequest> resultList = createNamedQuery.getResultList();
		loadLazyData(resultList);
		return resultList;
	}
	@Override
	public List<OptRequest> getAllIncludesResourc(String resourceEntityId) {
		Query createNamedQuery = em.createNamedQuery("OptRequest.findAllIncludesResource");
		createNamedQuery.setParameter("Id", resourceEntityId);
		@SuppressWarnings("unchecked")
		List<OptRequest> resultList = createNamedQuery.getResultList();
		loadLazyData(resultList);
		return resultList;
	}

	@Override
	public List<OptRequest> getByOptId(String optId) {
		Query createNamedQuery = em.createNamedQuery("OptRequest.findByOptID");
		createNamedQuery.setParameter("id", optId);
		@SuppressWarnings("unchecked")
		List<OptRequest> resul =  createNamedQuery.getResultList();
		loadLazyData(resul);
		return resul;
	}

	@Override
	public List<OptRequest> getByRequestId(String requestId) {
		Query createNamedQuery = em.createNamedQuery("OptRequest.findByRequestID");
		createNamedQuery.setParameter("id", requestId);
		@SuppressWarnings("unchecked")
		List<OptRequest> resuls = createNamedQuery.getResultList();
		loadLazyData(resuls);
		return resuls;
	}

	

	/*@Override
	public OptRequest create(OptRequest optRequest) {
		em.persist(optRequest);
		return optRequest;		
	}

	@Override
	public OptRequest update(OptRequest optRequest) {
		return em.merge(optRequest);
	}*/

	@Override
	public void deleteByOptId(String optId) {
		/*Query query = getEm().createNamedQuery("OptRequest.deleteByOptID");
		query.setParameter("id", optId);
        query.executeUpdate();*/
		List<OptRequest> optRequests = getByOptId(optId);
		if(null == optRequests || 0 == optRequests.size()){
			return;
		}
		for(OptRequest optReq : optRequests){
			getEm().remove(optReq);
		}
	}
	/*private EntityManager getEm() {
		
		return em;
	}*/
	private void loadLazyData(List<OptRequest> optReqList){
		if(null == optReqList){
			return;
		}
		for(OptRequest optReq : optReqList){
			loadLazyData(optReq);
		}
	}
	private void loadLazyData(OptRequest optReq){
		if(null == optReq){
			return;
		}
		optReq.getOptAvailability().size();
		optReq.getOptTarget().size();
		optReq.getOptResource().size();
	}

}
