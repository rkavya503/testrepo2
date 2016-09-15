/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.uohome.UOParticipantAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.uohome;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.web.program.ProgramForm;
import com.akuacom.pss2.web.util.EJBFactory;

/**
 * The Class UOParticipantAction.
 */
public class UOParticipantAction extends DispatchAction {

    /* (non-Javadoc)
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ActionForward unspecified(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {

        final com.akuacom.pss2.program.ProgramManager programManager1 = com.akuacom.pss2.core.EJBFactory.getBean(com.akuacom.pss2.program.ProgramManager.class);


        ProgramForm form = (ProgramForm)actionForm;

        final String programName = form.getProgramName();
        if(programName != null && !programName.isEmpty())
        {
        	String uid = (String) request.getSession().getAttribute("uid");
        	ActionForward partForward = new ActionForward ("../pss2.utility/participant.jsf?program="+programName + "&uid=" + uid, true);
            return partForward;
            /*
      
            request.setAttribute("ProgNames", programName);
            final List<Participant> parts = programManager1.getParticipantsForProgramAsObject(programName);
            final List<Participant> allParts = UOParticipantAction.getMyParticipantList(request);
            final List<Participant> participants = new ArrayList<Participant>();
            for (Participant part : parts)
            {
                boolean canSee = false;
                for(int j=0; j<allParts.size(); j++)
                {
                    if(part.getAccountNumber().equals(allParts.get(j).getAccountNumber()))
                    {
                        canSee = true;
                        break;
                    }
                }
                if(canSee)
                {
                    participants.add(part);
                }
            }
            form.setParticipants(participants);
            return actionMapping.findForward("success");
            */
        }
        else
        {
            ActionForward partForward = new ActionForward ("../pss2.utility/participant.jsf", true);
            return partForward;
            /*
            final List<Participant> participants = UOParticipantAction.getMyParticipantList(request);
            form.setParticipants(participants);
            String progNames = "";
            List<Program> programList = UOProgramAction.getMyProgramList(request,actionForm);
            if(programList != null )
            {
                for(Program prog : programList)
                {
                    if(progNames.isEmpty())
                    {
                        progNames = progNames + prog.getProgramName() ;
                    }
                    else
                    {
                        progNames = progNames + "," + prog.getProgramName() ;
                    }
                }
            }
            request.setAttribute("ProgNames", progNames);

            return actionMapping.findForward("success");
        }
         */
        }
    }

    /**
     * Gets the my participant list.
     * 
     * @param request the request
     * 
     * @return the my participant list
     */
    static public List<Participant> getMyParticipantList(HttpServletRequest request)
    {
        List<Participant> partList = null;
        /*
        partList = (List<Participant>) request.getSession().getAttribute("MYPARTICIPANTS");
        if(partList != null )
        {
            return partList;
        }
        */

        ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);

        String[] partNames;
        if(request.isUserInRole(DrasRole.Admin.toString()) || request.isUserInRole(DrasRole.Operator.toString()))
        {
            List<String> list = participantManager.getParticipants();
            partNames = new String[list.size()];

            for(int i=0; i<list.size(); i++)
            {
                String partName = (String) list.get(i);
                partNames[i] = participantManager.getParticipant(partName).getAccountNumber();
            }

            //partNames = pm.getParticipants().toArray(partNames);
            
        }
        else
        {
            partNames = UOProgramAction.getMyListInTag(request, "UO_PARTICIPANTS");
        }
        for(int i=0; i<partNames.length; i++)
        {
            Participant part = participantManager.getParticipantByAccount(partNames[i]);
            if(partList == null)
            {
                partList = new ArrayList<Participant>();
            }
            partList.add(part);
        }
        request.getSession().setAttribute("MYPARTICIPANTS", partList);
        return partList;
    }
}