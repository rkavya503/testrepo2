/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.pc.PeakChoiceProgram.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.pc;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.akuacom.pss2.program.Program;

/**
 * The Class PeakChoiceProgram.
 */
@Entity
@DiscriminatorValue("PeakChoiceProgram")
public class PeakChoiceProgram extends Program {

	private static final long serialVersionUID = 531820410122573357L;

	@Override
    public Program getNewInstance() {
        return new PeakChoiceProgram();
    }

    /* (non-Javadoc)
     * @see com.akuacom.pss2.core.model.Program#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder rv = new StringBuilder("PLPProgram: ");
        rv.append(super.toString());
        return rv.toString();
   }
}
