/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.rds.RDSProgramEJB.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.rds;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import org.openadr.dras.akuautilitydrevent.UtilityDREvent;

import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.program.eventtemplate.EventTemplate;

/**
 * The Interface RDSProgramEJB.
 */

public interface RDSProgramEJB extends ProgramEJB {
    @Remote
    public interface R extends RDSProgramEJB {}
    @Local
    public interface L extends RDSProgramEJB {}
    
	/**
	 * Update event template.
	 * 
	 * @param eventTemplate
	 *            the event template
	 */
	void updateEventTemplate(EventTemplate eventTemplate);

	/**
	 * Creates the event template.
	 * 
	 * @param eventTemplate
	 *            the event template
	 */
	void createEventTemplate(EventTemplate eventTemplate);

	/**
	 * Delete event template.
	 * 
	 * @param eventTemplateName
	 *            the event template name
	 */
	void deleteEventTemplate(String eventTemplateName);

	/**
	 * Gets the event template.
	 * 
	 * @param eventTemplateName
	 *            the event template name
	 * 
	 * @return the event template
	 */
	com.akuacom.pss2.program.eventtemplate.EventTemplate getEventTemplate(
			String eventTemplateName);

	/**
	 * Gets the event templates.
	 * 
	 * @param programName
	 *            the program name
	 * 
	 * @return the event templates
	 */
	List<com.akuacom.pss2.program.eventtemplate.EventTemplate> getEventTemplates(
			String programName);

	/**
	 * Gets the event template by program.
	 * 
	 * @param programName
	 *            the program name
	 * 
	 * @return the event template by program
	 */
	com.akuacom.pss2.program.eventtemplate.EventTemplate getEventTemplateByProgram(
			String programName);

	/**
	 * Creates the event template.
	 * 
	 * @param programName
	 *            the program name
	 * @param utilityDREvent
	 *            the utility dr event
	 */
	void createEventTemplate(String programName, UtilityDREvent utilityDREvent);

	/**
	 * Update event template.
	 * 
	 * @param programName
	 *            the program name
	 * @param utilityDREvent
	 *            the utility dr event
	 */
	void updateEventTemplate(String programName, UtilityDREvent utilityDREvent);
	
}