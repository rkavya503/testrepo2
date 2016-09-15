/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.program.services.ProgramServicerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.TupleUtil;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */
@Stateless
public class PDataSourceEAOBean extends BaseEAOBean<PDataSource> implements
        PDataSourceEAO.R, PDataSourceEAO.L {
    private static final Logger log = Logger
            .getLogger(PDataSourceEAOBean.class);

    public PDataSourceEAOBean() {
        super(PDataSource.class);
    }

    public PDataSource getDataSourceByNameAndOwner(String name, String owner) {
        final Query namedQuery = em
                .createNamedQuery("PDataSource.findByNameAndOwner")
                .setParameter("name", name).setParameter("ownerID", owner);
        try {
            List list = namedQuery.getResultList();
            if (list == null || list.size() == 0) {
                return null;
            } else if (list.size() == 1) {
                return (PDataSource) list.get(0);
            } else {
                throw new NonUniqueResultException("duplicated result, data corrupted.");
            }
        } catch (Exception e) {
            // TODO 2992
            log.error(LogUtils.createExceptionLogEntry("", this.getClass()
                    .getName(), e));
            return null;
        }
    }
    
    
    public List<PDataSource> getDataSourceByOwner(List<String> participantNames){
    	Query namedQuery = em.createNamedQuery("PDataSource.findByOwner");
    	namedQuery.setParameter("ownerIDs", participantNames);
    	List<PDataSource> result = namedQuery.getResultList();    			
    	return result;
    	
    }

    public Map<String, String> getDataSourceIdMapByParticipantNames(List<String> participantNames) {
        return TupleUtil.asMap( em.createNamedQuery("PDataSource.getUuidAndOwnerIdFromOwnerIds")
                .setParameter("ownerIDs", participantNames)
                .getResultList());
    }

	@Override
	public List<String> getDataSourceOwnerById(List<String> ids) {
		if(ids.isEmpty()) {
		    return new ArrayList<String>();
		}
		Query query = em.createNamedQuery("PDataSource.getOwnerIdsFromDataSourceIds");
		query.setParameter("ids", ids);
		return (List<String>) query.getResultList();
	}
	
    public Map<String, String> getDataSourcesByPartNames(List<String> partNames)
    {
        try
        {
            Map<String, String> datasourceUUIDs = new HashMap<String, String>();
            String sql = "select ds.UUID, ds.ownerID from PDataSource ds where ds.ownerID in (";
            for(int i=0; i<partNames.size(); i++)
            {
                if(i>0) sql = sql + ", ";
                String name = partNames.get(i);
                sql = sql + "'" + name + "'";
            }
            sql = sql + ")";
            Query query = em.createQuery(sql);
            List list = query.getResultList();
            for(int i=0; i<list.size(); i++)
            {
                Object[] arr = (Object[]) list.get(i);
                String uuid = (String) arr[0];
                String ownerID = (String) arr[1];
                datasourceUUIDs.put(uuid, ownerID);
            }
            return datasourceUUIDs;
         } catch (NoResultException e) {
		    return null;
	     }
    }

    @Override
    public void createMissing() {
        List<String> ownerIds = em.createNamedQuery("PDataSource.getMissingOwnerIds").getResultList();
        log.info("found " + ownerIds.size());
        for(String id : ownerIds) {
            PDataSource ds = new PDataSource();
            ds.setOwnerID(id);
            ds.setName("meter1");
            ds.setEnabled(true);
            em.persist(ds);
            log.info("saved " + id);
        }
        
    }

	@Override
	public List<String> getDataSourceIdByNameAndOwners(String name,
			List<String> owners) {
		  final Query namedQuery = em
          .createNamedQuery("PDataSource.findByNameAndOwners")
          .setParameter("name", name).setParameter("ownerIDs", owners);
		  List results = null;
		  try {
		      results = namedQuery.getResultList();
		  } catch (Exception e) {
		      log.error(LogUtils.createExceptionLogEntry("", this.getClass()
		              .getName(), e));
		      return Collections.EMPTY_LIST;
		  }
		  if(results ==null||results.isEmpty()) return Collections.EMPTY_LIST;
		  
		  List<String> datasourceIds = new ArrayList<String>();
		  Iterator it = results.iterator();
		  while(it.hasNext()){
			  datasourceIds.add((String) it.next());
		  }
		  
		  return datasourceIds;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PDataSource> getDataSourceBySiteID(){
		final Query namedQuery = em.createNamedQuery("PDataSource.getBySiteID");
		return namedQuery.getResultList();
	}
}