/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.report.ReportManagerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.report.entities.CommunicationEmail;
/**
 * The Class ReportManagerBean.
 */
@Stateless
public class CommunicationEmailManagerBean implements CommunicationEmailManager.R, CommunicationEmailManager.L {

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(CommunicationEmailManagerBean.class);
    @EJB
	Pss2SQLExecutor.L pss2SqlExecutor;
	@Override
	public List<CommunicationEmail> find(Date from, Date to) {
		List<CommunicationEmail> result = new ArrayList<CommunicationEmail>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startTime",from);
		params.put("endTime", to);
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(SQL_TEMPLATE, params);
			result = pss2SqlExecutor.doNativeQuery(sql, params, new ListConverter<CommunicationEmail>(new ColumnAsFeatureFactory<CommunicationEmail>(CommunicationEmail.class)));
		}catch(Exception e){
			log.info(e);
			throw new EJBException(e);
		}
		return result;
	}

    private static final String SQL_TEMPLATE=" select message.to as emailAddress,message.subject as subject, message.creationTime as creationTime," +
    		" participant.participantName as clientName,participant.parent as participantName,participant_contact.description as contactName," +
    		" message.status,message.sentTime as sendTime from message inner join participant_contact " +
    		" on message.contactId = participant_contact.uuid left join participant on participant_contact.participant_uuid = participant.uuid " +
    		" where message.type='email' and message.creationTime >= ${startTime} and message.creationTime <= ${endTime} " +
    		" order by creationTime, contactName ";
}
