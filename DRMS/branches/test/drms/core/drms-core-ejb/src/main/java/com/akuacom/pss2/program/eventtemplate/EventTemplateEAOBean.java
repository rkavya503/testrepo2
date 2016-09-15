/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.eventtemplate;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.BaseEAOBean;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */
@Stateless
public class EventTemplateEAOBean extends BaseEAOBean<EventTemplate> implements
        EventTemplateEAO.R, EventTemplateEAO.L {

    public EventTemplateEAOBean() {
        super(EventTemplate.class);
    }

    /** The Constant log. */
    private static final Logger log = Logger
            .getLogger(EventTemplateEAOBean.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#createEventTemplate(com.
     * akuacom.pss2.program.eventtemplate.EventTemplate)
     */
    public EventTemplate createEventTemplate(EventTemplate value)
            throws AppServiceException {
        try {
            value = (EventTemplate) super.create(value);
        } catch (Exception e) {
            throw new AppServiceException("ERROR_PROGRAM_CREATE", e);
        }
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#updateEventTemplate(com.
     * akuacom.pss2.program.eventtemplate.EventTemplate)
     */
    public EventTemplate updateEventTemplate(EventTemplate value)
            throws AppServiceException {
        try {
            value = (EventTemplate) super.update(value);
        } catch (Exception e) {
            throw new AppServiceException("ERROR_PROGRAM_UPDATE", e);
        }
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#getEventTemplate(java.lang
     * .String)
     */
    public EventTemplate getEventTemplate(String name)
            throws AppServiceException {
        EventTemplate ret = null;
        try {
            final String QUERY_GET_EVENT_TEMPLATE_BY_NAME = "SELECT et FROM EventTemplate et  WHERE et.name = :name ";

            ret = super
                    .getByKey(QUERY_GET_EVENT_TEMPLATE_BY_NAME, "name", name);
        } catch (Exception ex) {
            throw new AppServiceException("", ex);
        }
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#findEventTemplates(java.
     * lang.String)
     */
    public List<EventTemplate> findEventTemplates(String program)
            throws AppServiceException {
        try {
            final String QUERY_GET_EVENT_TEMPLATE_BY_PORGRAM_NAME = "SELECT et FROM EventTemplate et  WHERE et.programName = ?1 ";
            List<Object> values = new ArrayList();
            values.add(program);
            return super.getByFilters(QUERY_GET_EVENT_TEMPLATE_BY_PORGRAM_NAME,
                    values);

        } catch (Exception ex) {
            throw new AppServiceException("", ex);
        }
    }

}