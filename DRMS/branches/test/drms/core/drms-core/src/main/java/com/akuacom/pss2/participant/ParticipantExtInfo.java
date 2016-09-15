/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.model.ParticipantExtInfo.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.participant;

import java.util.List;

import com.akuacom.pss2.program.Program;

/**
 * Created by IntelliJ IDEA.
 * User: lin
 * Date: Aug 4, 2008
 * Time: 12:27:23 PM
 * To change this template use File | Settings | File Templates.
 */

//TODO lin: this class should be in utils package. Since it depends on Program and Program is in core, so I will have to put it here for now.
public class ParticipantExtInfo extends Participant
{
    
    /** The m_programs. */
    private List<Program> m_programs;

    /** The m_password. */
    private String m_password;

    /**
     * Gets the password.
     * 
     * @return the password
     */
    public String getPassword()
    {
        return m_password;
    }

    /**
     * Sets the password.
     * 
     * @param password the new password
     */
    public void setPassword(String password)
    {
        m_password = password;
    }

    /**
     * Gets the programs.
     * 
     * @return the programs
     */
    public List<Program> getPrograms()
    {
        return m_programs;
    }

    /**
     * Sets the programs.
     * 
     * @param programs the new programs
     */
    public void setPrograms(List<Program> programs)
    {
        m_programs = programs;
    }
}