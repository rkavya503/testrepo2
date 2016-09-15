/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.season;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.utils.lang.DateUtil;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */
@Stateless
public class SeasonConfigEAOBean extends BaseEAOBean<SeasonConfig> implements
        SeasonConfigEAO.R, SeasonConfigEAO.L {

    public SeasonConfigEAOBean() {
        super(SeasonConfig.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#findSeasonConfigs(java.lang
     * .String)
     */
    public List<SeasonConfig> findSeasonConfigs(String programVersionUuid) {
        try {
            String QUERY_GET_RTPCONFIG_BY_PORGRAM_NAME = "SELECT c FROM SeasonConfig c  WHERE c.programVersionUuid = '"
                    + programVersionUuid + "' order by c.startDate";
            List<Object> values = new ArrayList<Object>();
            // values.add(program);

            return super.getByFilters(QUERY_GET_RTPCONFIG_BY_PORGRAM_NAME,
                    values);

        } catch (Exception ex) {
            throw new EJBException("", ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#createSeasonConfig(com.akuacom
     * .pss2.season.SeasonConfig)
     */
    public SeasonConfig createSeasonConfig(SeasonConfig value) {
        try {
            value = (SeasonConfig) super.create(value);
        } catch (Exception e) {
            throw new EJBException("ERROR_SEASONCONFIG_CREATE", e);
        }
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#updateSeasonConfig(com.akuacom
     * .pss2.season.SeasonConfig)
     */
    public SeasonConfig updateSeasonConfig(SeasonConfig value) {
        try {
            value = (SeasonConfig) super.update(value);
        } catch (Exception e) {
            throw new EJBException("ERROR_SEASONCONFIG_UPDATE", e);
        }
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#getSeasonConfig(java.lang
     * .String, java.util.Date)
     */
    public List<SeasonConfig> getSeasonConfig(String programVersionUuid,
            Date date) throws AppServiceException {
        try {
            Query q = em.createNamedQuery("SeasonConfig.findAllByDate");
            q.setParameter("pvUUID", programVersionUuid);
            q.setParameter("date1", DateUtil.stripTime(date));
            q.setParameter("date2", DateUtil.stripTime(date));

            return q.getResultList();
        } catch (Exception ex) {
            throw new AppServiceException("", ex);
        }

    }

    public SeasonConfig get(String uuid) throws AppServiceException {
        try {
            return (SeasonConfig) super.getById(uuid);
        } catch (Exception ex) {
            throw new AppServiceException("", ex);
        }

    }

	@Override
	public Set<Date> findHolidays() {
		 Set<Date> set= new HashSet<Date>();
		 try {
	            Query q = em.createNamedQuery("SeasonConfig.findHolidays");

	             final List list = q.getResultList();
	             
	             Iterator item = list.iterator();
	             while(item.hasNext()){
	            	 Object[] row = (Object[]) item.next();
	            	 Date start = (Date) row[0];
	            	 Date end = (Date) row[1];
	            	 
	            	 if(start.equals(end)){
	            		 set.add(DateUtil.stripTime(start));
	            	 }else{
	            		 while(end.after(start)){
	            			 set.add(DateUtil.stripTime(start));
	            			 start.setDate(start.getDate()+1);
	            		 }
	            	 }
	             }
	            
	        } catch (Exception ex) {
	        }
	        return set;
	}

}