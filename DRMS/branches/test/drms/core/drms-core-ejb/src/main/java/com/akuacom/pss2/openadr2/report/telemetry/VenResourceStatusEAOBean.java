package com.akuacom.pss2.openadr2.report.telemetry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;


@Stateless
public class VenResourceStatusEAOBean implements VenResourceStatusEAO.L , VenResourceStatusEAO.R{
	private Logger log = Logger.getLogger(VenResourceStatusEAOBean.class);
	
	@PersistenceContext(unitName = "core")
	private EntityManager em;
	
	public void create(VenResourceStatus venResourceStatus){
		if(null == venResourceStatus){
			return;
		}
		this.em.persist(venResourceStatus);
	}
	public List<VenResourceStatus> findByEntryRange(GetResourceStatusRequest req) throws InvalidResourceStatusData{
		if(null == req){
			throw new InvalidResourceStatusData("Invalid resource status request data ");
		}
		String venId = req.getVenId();
		String  resourceId = req.getResourceId();
		Date start = req.getStart();
		Date end = req.getEnd();
		boolean isDataInValid = (null == venId || venId.isEmpty() || null == resourceId || resourceId.isEmpty() 
				|| null == start || null == end);
		if(isDataInValid){
			throw new InvalidResourceStatusData("Invalid resource status request data ");
		}
		Query createNamedQuery = em.createNamedQuery("VenResourceStatus.findByVenIdResourceNameAndEntryTimeRange");
		createNamedQuery.setParameter("venId", venId);
		createNamedQuery.setParameter("resName", resourceId);
		createNamedQuery.setParameter("start", start);
		createNamedQuery.setParameter("end", end);
		List<VenResourceStatus> result = new ArrayList<VenResourceStatus>();
		 try {
			result = createNamedQuery.getResultList();
		} catch (Exception e) {
			log.error(e.getStackTrace());
		}
		return result;
	}
	public VenResourceStatus findLatestEntry(String venId , String resourceName) throws InvalidResourceStatusData{
		if(null == venId || venId.isEmpty() || null == resourceName || resourceName.isEmpty()){
			throw new InvalidResourceStatusData("Invalid resource status request data ");
		}
		Query createNamedQuery = em.createNamedQuery("VenResourceStatus.findLatestByVenIdAndResourceName");
		createNamedQuery.setParameter("venId", venId);
		createNamedQuery.setParameter("resName", resourceName);
		createNamedQuery.setMaxResults(1);
		VenResourceStatus rs =null;
		 try {
			rs =(VenResourceStatus)createNamedQuery.getSingleResult();
		} catch (Exception e) {
			log.error(e.getStackTrace());
		}
		return rs;
	}

}
