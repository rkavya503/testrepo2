/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.rtp.RTPProgram.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.rtp;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.cpp.CPPProgram;

/**
 * The Class RTPProgram.
 */
@Entity
@DiscriminatorValue("RTPProgram")
public class RTPProgram extends CPPProgram {

	private static final long serialVersionUID = -7550953273098511873L;

	@Override
    public Program getNewInstance() {
        return new RTPProgram();
    }

    /* (non-Javadoc)
     * @see com.akuacom.pss2.program.cpp.CPPProgram#toString()
     */
    public String toString()
    {
        StringBuilder rv = new StringBuilder("RTPProgram: ");
        rv.append(super.toString());
        return rv.toString();
   }
}
