/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.model.EventTemplate.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.event;


/**
 * The Class EventTemplate.
 */
public class EventTemplate extends Event
{
	
	/** The template. */
	private boolean template;

    /**
     * Checks if is template.
     * 
     * @return true, if is template
     */
    public boolean isTemplate()
    {
        return template;
    }

    /**
     * Sets the template.
     * 
     * @param template the new template
     */
    public void setTemplate(boolean template)
    {
        this.template = template;
    }

    /* (non-Javadoc)
     * @see com.akuacom.pss2.core.model.Event#toString()
     */
    public String toString()
    {
        StringBuilder rv = new StringBuilder("RDSEventTemplate: ");
	    rv.append(super.toString());
        rv.append("\ntemplate: " + template);
        return rv.toString();
   }
}