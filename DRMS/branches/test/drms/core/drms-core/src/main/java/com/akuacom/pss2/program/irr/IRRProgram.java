/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.program.irr.IRRProgram.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.irr;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.akuacom.pss2.program.Program;

/**
 * The Class IRRProgram.
 */
@Entity
@DiscriminatorValue("IRRProgram")
public class IRRProgram extends Program {

    /**
	 * Serial version ID
	 */
	private static final long serialVersionUID = -396728420810470171L;


    @Override
    public Program getNewInstance() {
        return new IRRProgram();
    }

    /* (non-Javadoc)
     * @see com.akuacom.pss2.core.model.Program#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder rv = new StringBuilder("IRRProgram: ");
        rv.append(super.toString());
        return rv.toString();
   }
}
