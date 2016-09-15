/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.program.ProgramForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.program;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;

/**
 * The Class ProgramForm.
 */
public class ProgramForm extends ActionForm {
    
    /** The program name. */
    private String programName;
    
    /** The program name clone */
    private String programNameClone;
    
    /** The program name clone */
    private String programCloneStatus;


    /** The program name Delete Msg */
    private String programDeleteStatus;

    /** The username. */
    private String username;

    /** The program names. */
    private String[] programNames;

    /** The participants. */
    private List<Participant> participants;

    /** The programs. */
    private List<Program> programs;

    /** The program list. */
    private List<Program> programList;

    
    
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
        ActionErrors errors = null;

        String dispatch = request.getParameter("dispatch");
        if ("clone".equals(dispatch) ) {
	
            errors = new ActionErrors();
           
            if (programNameClone == null || programNameClone.length() == 0) {
                errors.add("programNameClone", new ActionMessage("pss2.program.programNameClone.empty"));
            } else if (programNameClone.matches(".*[\\W&&[^\\._\\-/ ]].*")) {
                ActionMessage message = new ActionMessage("pss2.program.programNameClone.charNotAllowed");
                errors.add("programNameClone", message);
            }
        }
       
       return errors;
    }

    
    
    
    /**
     * Gets the program name.
     * 
     * @return the program name
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Sets the program name.
     * 
     * @param programName the new program name
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * Gets the program name clone.
     * 
     * @return the program name clone
     */
    public String getProgramNameClone() {
        return programNameClone;
    }

    /**
     * Sets the program name clone.
     * 
     * @param programNameClone the new program name clone
      */
    public void setProgramNameClone(String programNameClone) {
        this.programNameClone = programNameClone;
    }
    
 
    /**
     * Gets the program name clone.
     * 
     * @return the program name clone
     */
    public String getProgramCloneStatus() {
        return programCloneStatus;
    }

    /**
     * Sets the program name clone.
     * 
     * @param programCloneStatus the new program name clone
      */
    public void setProgramCloneStatus(String programCloneStatus) {
        this.programCloneStatus = programCloneStatus;
    }
    
     /**
     * Gets the program name delete.
     *
     * @return the program name delete
     */
    public String getProgramDeleteStatus() {
        return programDeleteStatus;
    }

    /**
     * Sets the program name delete.
     *
     * @param programCloneStatus the new program name delete
      */
    public void setProgramDeleteStatus(String programCloneStatus) {
        this.programDeleteStatus = programDeleteStatus;
    }


    /**
     * Gets the username.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     * 
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the participants.
     * 
     * @return the participants
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * Sets the participants.
     * 
     * @param participants the new participants
     */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }


    public List<Program> getPrograms() {
        return programs;
    }

    /**
     * Sets the programs.
     *
     * @param programs the new programs
     */
    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }


      public List<Program> getProgramList() {
        return programList;
    }

    /**
     * Sets the program list.
     *
     * @param programList the new program list
     */
    public void setProgramList(List<Program> programList) {
        this.programList = programList;
    }


        /**
     * Gets the program names.
     *
     * @return the program names
     */
    public String[] getProgramNames()
    {
        return programNames;
    }

    /**
     * Sets the program names.
     *
     * @param programNames the new program names
     */
    public void setProgramNames(String[] programNames)
    {
        this.programNames = programNames;
    }


}
