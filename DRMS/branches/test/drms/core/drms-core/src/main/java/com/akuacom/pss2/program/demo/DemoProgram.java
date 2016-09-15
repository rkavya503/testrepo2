/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.demo.DemoProgram.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.demo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.akuacom.pss2.event.EventTiming;
import com.akuacom.pss2.program.Program;

/**
 * The Class DemoProgram.
 */
@Entity
@DiscriminatorValue("DemoProgram")
public class DemoProgram extends Program {
    
	private static final long serialVersionUID = 4696020066619279296L;

	@Override
    public Program getNewInstance() {
        return new DemoProgram();
    }
	
	@Override
    public long getPendingLeadMS(EventTiming eventTiming) {
		if(eventTiming.getNearTime() != null)
		{
	        return eventTiming.getStartTime().getTime() - 
	        	eventTiming.getNearTime().getTime();
		}
		else
		{
			return super.getPendingLeadMS(eventTiming);
		}
    }

    /* (non-Javadoc)
     * @see com.akuacom.pss2.core.model.Program#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder rv = new StringBuilder("DemoProgram: ");
        rv.append(super.toString());
        return rv.toString();
   }
}
