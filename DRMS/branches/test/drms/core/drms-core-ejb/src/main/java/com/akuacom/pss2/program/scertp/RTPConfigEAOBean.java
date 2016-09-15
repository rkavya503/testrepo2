/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.scertp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.akuacom.common.exception.AppServiceException;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */
@Stateless
public class RTPConfigEAOBean extends com.akuacom.ejb.BaseEAOBean<RTPConfig>
        implements RTPConfigEAO.R, RTPConfigEAO.L {
    public RTPConfigEAOBean() {
        super(RTPConfig.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#createRTPConfig(com.akuacom
     * .pss2.program.scertp.RTPConfig)
     */
    public RTPConfig createRTPConfig(RTPConfig value) {
        try {
            value = (RTPConfig) super.create(value);
        } catch (Exception e) {
            throw new EJBException("ERROR_RTPCONFIG_CREATE", e);
        }
        return value;
    }
    
//    public String get

    public List<RTPConfig> getRTPConfigList(String programVersionUuid,
            String seasonName, Calendar cal, double temperature)
            throws AppServiceException {

        List<RTPConfig> configs = null;
        try {
            String QUERY_GET_RTPCONFIG = "SELECT c FROM RTPConfig c  WHERE c.programVersionUuid = ?1 AND c.seasonName = ?2 ";

            if (temperature != 0) {
                QUERY_GET_RTPCONFIG = QUERY_GET_RTPCONFIG
                        + " and (c.startTemperature <= ?3  or c.startTemperature = 0) and ( c.endTemperature > ?4 or c.endTemperature = 0 )";
            }

            QUERY_GET_RTPCONFIG = QUERY_GET_RTPCONFIG + " order by startTime";

            List<Object> values = new ArrayList<Object>();
            values.add(programVersionUuid);
            values.add(seasonName);

            if (temperature != 0) {
                values.add(temperature);
                values.add(temperature);
            }

            // Read a price column from the RTP config table
            configs = super.getByFilters(QUERY_GET_RTPCONFIG, values);

        } catch (Exception ex) {
            throw new AppServiceException("", ex);
        }
        return configs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#findRTPConfigs(java.lang
     * .String)
     */
    public List<RTPConfig> findRTPConfigs(String programVersionUuid) {
        try {
            String QUERY_GET_RTPCONFIG_BY_PORGRAM_NAME = "SELECT c FROM RTPConfig c  WHERE c.programVersionUuid = '"
                    + programVersionUuid + "' order by c.startTime, case "
            +" when name='EXTREMELY HOT SUMMER WEEKDAY' then 1"
            +" when name='VERY HOT SUMMER WEEKDAY' then 2"
            +" when name='HOT SUMMER WEEKDAY' then 3"
            +" when name='MODERATE SUMMER WEEKDAY' then 4"
            +" when name='MILD SUMMER WEEKDAY' then 5"
            +" when name='HIGH COST WINTER WEEKDAY' then 6"
            +" when name='LOW COST WINTER WEEKDAY' then 7"
            +" when name='HIGH COST WEEKEND' then 8"
            +" when name='LOW COST WEEKEND' then 9"
            +" ELSE 10 "
            +" END ";
                   
            List<Object> values = new ArrayList<Object>();

            return super.getByFilters(QUERY_GET_RTPCONFIG_BY_PORGRAM_NAME,
                    values);

        } catch (Exception ex) {
            throw new EJBException("", ex);
        }
    }

    /**
     * Delete rtp configs.
     * 
     * @param program
     *            the program
     */
    public void deleteRTPConfigs(String program) {
        try {
            String hqlDelete = "delete from program_rtp_config where program_UUID in (select c.UUID from program c where c.name = '"
                    + program + "')";
            this.runNativeSql(hqlDelete);

        } catch (Exception e) {
            throw new EJBException("ERROR_RTP_DELETE", e);
        }
    }

	@Override
	public String getRTPConfigName(String programUUID, String seasonName,
			double temperature) throws AppServiceException {
		String name = null;
		try {
			Query query = em.createNamedQuery("RTPConfig.getRTPConfigName");
			query.setParameter("programUUID", programUUID);
			query.setParameter("seasonName", seasonName);
			query.setParameter("temperature", temperature);
			name = (String) query.getSingleResult();
		} catch (Exception e) {
			String message = "could not get RTP config name. program UUID: "
					+ programUUID + ", season name: " + seasonName
					+ ", temperature: " + temperature;
			throw new AppServiceException(message, e);
		}

		return name;
	}

}