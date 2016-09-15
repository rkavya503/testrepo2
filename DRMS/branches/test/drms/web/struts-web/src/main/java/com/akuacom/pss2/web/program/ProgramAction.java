/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.program.ProgramAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.program;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.pss2.core.ErrorResourceUtil;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.web.util.DisplayTagUtil;
import com.akuacom.pss2.web.util.EJBFactory;

/**
 * The Class ProgramAction.
 */
@SuppressWarnings({"UnusedParameters", "UnusedDeclaration"})
public class ProgramAction extends DispatchAction {
    
    /* (non-Javadoc)
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ActionForward unspecified(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProgramForm form = (ProgramForm)actionForm;

        final String programName = form.getProgramName();

        ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);
        ProgramParticipantManager programParticipantManager = EJBFactory.getBean(ProgramParticipantManager.class);
        
        final List<Participant> parts = programParticipantManager.getParticipantsForProgramAsObject(programName);
        final List<Participant> participants = new ArrayList<Participant>(parts.size());
        for (Participant part : parts) {
            final Participant participant = participantManager.getParticipant(part.getParticipantName(), part.isClient());
            participants.add(participant);
        }
        form.setParticipants(participants);

        if (DisplayTagUtil.isExport(request)) {
            return actionMapping.findForward("export");
        } else {
            return actionMapping.findForward("list");
        }
    }

    /**
     * Creates the.
     * 
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     * 
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    public ActionForward create(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        return actionMapping.findForward("create");
    }
    

    public ActionForward cloneEdit(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	ProgramForm form = (ProgramForm)actionForm;
	    	form.setProgramNameClone("");
	    	form.setProgramCloneStatus("");

       return actionMapping.findForward("cloneEdit");
    }


    
    public ActionForward clone(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	try{
            ProgramForm form = (ProgramForm)actionForm;
            String programName = form.getProgramName();
            String cloneName = form.getProgramNameClone().trim();
            ProgramManager programManager = EJBFactory.getBean(ProgramManager.class);
            Program existing = programManager.getProgram(programName);
            Program temp = programManager.getProgram(cloneName);

            if (temp != null) {
                throw new ValidationException(ErrorResourceUtil
                        .getErrorMessage("ERROR_CREATE_PROGRAM_ALREADY_EXIST", cloneName));
            } else {
                Program newOne = programManager.copyProgram(existing.getUUID(), cloneName);
                if (newOne != null) {
                    form.setProgramCloneStatus("Successfully added: " + cloneName);
                } else {
                    form.setProgramCloneStatus("Error adding: " + cloneName);
                }
            }
            return actionMapping.findForward("clone");
        } catch (Exception e) {
            ValidationException ve = ErrorUtil.getValidationException(e);
            ActionErrors errors = new ActionErrors();
            ActionMessage error;
            if (ve != null) {
                error = new ActionMessage(ve.getLocalizedMessage(), false);
            } else {
                error = new ActionMessage(ErrorUtil.getErrorMessage(e), false);
            }
            errors.add("cloningValidation", error);
            addErrors(request, errors);
            return actionMapping.findForward("cloneEdit");
        }
    }
    
    /**
     * Edits the.
     * 
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     * 
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    public ActionForward edit(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        ProgramForm form = (ProgramForm)actionForm;
        request.setAttribute("programName", form.getProgramName()) ;
        return actionMapping.findForward("edit");
    }
}
