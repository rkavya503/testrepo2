/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.rds.RDSProgram.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.rds;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.akuacom.pss2.program.Program;

/**
 * The Class RDSProgram.
 */
@Entity
@DiscriminatorValue("RDSProgram")
public class RDSProgram extends Program {
    @Override
    public Program getNewInstance() {
        return new RDSProgram();
    }

    /* (non-Javadoc)
     * @see com.akuacom.pss2.core.model.Program#toString()
     */

    @Override
    public String toString() {
        StringBuilder rv = new StringBuilder("RDSProgram: ");
        rv.append(super.toString());
        return rv.toString();
    }
}