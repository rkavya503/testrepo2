/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.ProgramManagerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.scertp2013;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.event.signal.EventSignalEntry;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.scertp.NotConfiguredException;
import com.akuacom.pss2.program.scertp.RTPConfig;
import com.akuacom.pss2.program.scertp.SCERTPEvent;
import com.akuacom.pss2.program.scertp.SCERTPEventRateInfo;
import com.akuacom.pss2.program.scertp.SCERTPProgramManagerBean;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.util.LogUtils;

/**
 * Stateless session bean providing a DRAS Entity BO facade.
 */
@Stateless
public class SCERTPProgramManager2013Bean extends SCERTPProgramManagerBean implements SCERTPProgramManager2013.R, SCERTPProgramManager2013.L {
    /** The Constant log. */
    private static final Logger log = Logger
            .getLogger(SCERTPProgramManager2013Bean.class);

    @EJB
	protected Pss2SQLExecutor.L sqlExecutor;
    



    /**
     * only used to validate season and strategy category configuration
     */
    public double getRate(String programName, Date time) {
        try {
            ProgramEJB programEJB = lookupProgramBean(programName);
            if (programEJB instanceof SCERTPProgramEJB2013) {
            	SCERTPProgramEJB2013 srejb = (SCERTPProgramEJB2013) programEJB;
                return srejb.getRate(programName, time);
            } else {
                throw new EJBException(
                        "method getRate is not supported for the program: "
                                + programName);
            }
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

	
	
	@Override
	public List<RTPConfig> findRtpCategoryByProgramName(
			String programName) {
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("program_name", programName);
		StringBuilder sql =new StringBuilder(); 
		sql.append(" SELECT name, startTemperature FROM program_rtp_config  ");
		sql.append(" WHERE program_uuid= (SELECT UUID FROM program WHERE name=${program_name} ) GROUP BY name");
		sql.append(" ORDER BY CASE name WHEN 'EXTREMELY HOT SUMMER WEEKDAY' THEN 1");
		sql.append(" WHEN 'VERY HOT SUMMER WEEKDAY' THEN 2");
		sql.append(" WHEN 'HOT SUMMER WEEKDAY' THEN 3");
		sql.append(" WHEN 'MODERATE SUMMER WEEKDAY' THEN 4");
		sql.append(" WHEN 'MILD SUMMER WEEKDAY' THEN 5");
		sql.append(" WHEN 'HIGH COST WINTER WEEKDAY' THEN 6");
		sql.append(" WHEN 'LOW COST WINTER WEEKDAY' THEN 7");
		sql.append(" WHEN 'HIGH COST WEEKEND' THEN 8");
		sql.append(" WHEN 'LOW COST WEEKEND' THEN 9");
		sql.append(" END");
		List<RTPConfig>report = null;
		try {
			String parameterizedSQL =SQLBuilder.buildNamedParameterSQL(sql.toString(),params);
			report = sqlExecutor.doNativeQuery(parameterizedSQL,params,
					new ListConverter<RTPConfig>(
							new ColumnAsFeatureFactory<RTPConfig>(
									RTPConfig.class)));
		} catch (Exception e1) {
			log.error(e1);
			throw new EJBException(e1);
		}
		return (report==null?new ArrayList<RTPConfig>():report);
	}
	

	
}