/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.scertp.SCERTPProgram.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.scertp;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.akuacom.pss2.program.Program;

/**
 * The Class SCERTPProgram.
 */
@Entity
@DiscriminatorValue("SCERTPProgram")
public class SCERTPProgram extends Program {

	private static final long serialVersionUID = -3347762182172054519L;

	@Override
    public Program getNewInstance() {
        return new SCERTPProgram();
    }

    /* (non-Javadoc)
     * @see com.akuacom.pss2.core.model.Program#toString()
     */

    @Override
    public String toString() {
        StringBuilder rv = new StringBuilder("SCERTPProgram: ");
        rv.append(super.toString());
        return rv.toString();
    }
}