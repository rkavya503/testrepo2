/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.program.cpp.CPPProgram.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.cpp;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.akuacom.pss2.program.Program;
import java.io.Serializable;

/**
 * The Class CPPProgram.
 */
@Entity
@DiscriminatorValue("CPPProgram")
public class CPPProgram extends Program implements Serializable { 

    /**
	 * Serial version ID
	 */
	private static final long serialVersionUID = -396728390810470131L;


    @Override
    public Program getNewInstance() {
        return new CPPProgram();
    }

    /* (non-Javadoc)
     * @see com.akuacom.pss2.core.model.Program#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder rv = new StringBuilder("CPPProgram: ");
        rv.append(super.toString());
        return rv.toString();
   }
}
