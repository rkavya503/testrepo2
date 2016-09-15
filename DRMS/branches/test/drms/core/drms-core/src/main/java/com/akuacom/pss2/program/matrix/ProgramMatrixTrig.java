/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.matrix.ProgramMatrixTrig.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.matrix;

import com.akuacom.pss2.program.matrix.ProgramMatrix;

import java.io.Serializable;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The Class ProgramMatrixTrig.
 */
public class ProgramMatrixTrig implements Serializable
{
	
	/** The programs. */
	private HashMap programs;

    /** The program matrix. */
    private List<ProgramMatrix> programMatrix;

    /**
     * Gets the programs.
     * 
     * @return the programs
     */
    public HashMap getPrograms()
    {
        return programs;
    }

    /**
     * Sets the programs.
     * 
     * @param programs the new programs
     */
    public void setPrograms(HashMap programs)
    {
        this.programs = programs;
    }

    /**
     * Gets the program matrix.
     * 
     * @return the program matrix
     */
    public List<ProgramMatrix> getProgramMatrix()
    {
        return programMatrix;
    }

    /**
     * Sets the program matrix.
     * 
     * @param programMatrix the new program matrix
     */
    public void setProgramMatrix(List<ProgramMatrix> programMatrix)
    {
        this.programMatrix = programMatrix;
    }

    /**
     * Coexist.
     * 
     * @param programUUID1 the program uui d1
     * @param programUUID2 the program uui d2
     * 
     * @return true, if successful
     */
    public boolean coexist(String programUUID1, String programUUID2)
    {
        boolean ret = false;
        for(ProgramMatrix pme : programMatrix)
        {
            if( pme.getCoexist() && programUUID1.equals(pme.getProgram1UUID()) && programUUID2.equals(pme.getProgram2UUID()))
            {
                return true;
            }
            if( pme.getCoexist() && programUUID1.equals(pme.getProgram2UUID()) && programUUID2.equals(pme.getProgram1UUID()))
            {
                return true;
            }
            
        }
        return ret;
    }

    /**
     * Coexist by names.
     * 
     * @param name1 the name1
     * @param name2 the name2
     * 
     * @return true, if successful
     */
    public boolean coexistByNames(String name1, String name2)
    {
        String programUUID1 = "";
        String programUUID2 = "";

        Iterator it = this.programs.keySet().iterator();
        while(it.hasNext())
        {
            String uuid = (String) it.next();
            String name = (String) this.programs.get(uuid);
            if(name.equals(name1))
            {
                programUUID1 = uuid;
            }

            if(name.equals(name2))
            {
                programUUID2 = uuid;    
            }
        }


        boolean ret = false;
        for(ProgramMatrix pme : programMatrix)
        {
            if( pme.getCoexist() && programUUID1.equals(pme.getProgram1UUID()) && programUUID2.equals(pme.getProgram2UUID()))
            {
                return true;
            }
            if( pme.getCoexist() && programUUID1.equals(pme.getProgram2UUID()) && programUUID2.equals(pme.getProgram1UUID()))
            {
                return true;
            }

        }
        return ret;
    }
}