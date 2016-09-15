/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.eventtemplate;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.BaseEAO;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */

public interface EventTemplateEAO extends BaseEAO<EventTemplate> {
    @Remote
    public interface R extends EventTemplateEAO {}
    @Local
    public interface L extends EventTemplateEAO {}

	/**
	 * Creates the event template.
	 * 
	 * @param value
	 *            the value
	 * 
	 * @return the event template
	 * 
	 * @throws AppServiceException
	 *             the app service exception
	 */
	EventTemplate createEventTemplate(EventTemplate value)
			throws AppServiceException;

	/**
	 * Update event template.
	 * 
	 * @param value
	 *            the value
	 * 
	 * @return the event template
	 * 
	 * @throws AppServiceException
	 *             the app service exception
	 */
	EventTemplate updateEventTemplate(EventTemplate value)
			throws AppServiceException;

	/**
	 * Gets the event template.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return the event template
	 * 
	 * @throws AppServiceException
	 *             the app service exception
	 */
	EventTemplate getEventTemplate(String name) throws AppServiceException;

	/**
	 * Find event templates.
	 * 
	 * @param program
	 *            the program
	 * 
	 * @return the list< event template>
	 * 
	 * @throws AppServiceException
	 *             the app service exception
	 */
	List<EventTemplate> findEventTemplates(String program)
			throws AppServiceException;

}