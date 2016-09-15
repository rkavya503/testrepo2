/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.program.ProgramMatrixAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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

import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.program.matrix.ProgramMatrix;
import com.akuacom.pss2.program.matrix.ProgramMatrixTrig;


/**
 * The Class ProgramMatrixAction.
 */
public class ProgramMatrixAction extends DispatchAction {
    

    
    /* (non-Javadoc)
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ActionForward unspecified(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        com.akuacom.pss2.program.ProgramManager programManager1 = com.akuacom.pss2.core.EJBFactory.getBean(com.akuacom.pss2.program.ProgramManager.class);

        ProgramMatrixTrig pmt = programManager1.getProgramMatrixTrig();
        httpServletRequest.setAttribute("ProgramMatrixTrig", pmt);
        return actionMapping.findForward("view");
    }
    
    public ActionForward cancel(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        return unspecified(actionMapping, actionForm, httpServletRequest, httpServletResponse);
    }

    /**
     * Update.
     * 
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param httpServletRequest the http servlet request
     * @param httpServletResponse the http servlet response
     * 
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    public ActionForward update(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ProgramMatrixForm form = (ProgramMatrixForm) actionForm;

        if(form.getMatrixCells() != null && form.getMatrixCells().length > 0)
        {
            String cells[] = form.getMatrixCells();
            List<ProgramMatrix> list = new ArrayList<ProgramMatrix>();
            for(int i =0 ; i<cells.length; i++)
            {
                String uuid1 = cells[i].split("_")[0];
                String uuid2 = cells[i].split("_")[1];
                if(!uuid1.equals(uuid2))
                {
                    ProgramMatrix pme = new ProgramMatrix();

                    pme.setProgram1UUID(uuid1);
                    pme.setProgram2UUID(uuid2);
                    pme.setCoexist(true);
                    list.add(pme);
                }
            }

            com.akuacom.pss2.program.ProgramManager programManager1 = com.akuacom.pss2.core.EJBFactory.getBean(com.akuacom.pss2.program.ProgramManager.class);

            try
            {
                programManager1.saveProgramMatrixTrig(list);
            }
            catch (Exception e)
            {
                ValidationException ve = ErrorUtil.getValidationException(e);
                ActionErrors errors = new ActionErrors();
                ActionMessage error;
                if (ve != null) {
                    error = new ActionMessage(ve.getLocalizedMessage(), false);
                } else {
                    error = new ActionMessage(ErrorUtil.getErrorMessage(e), false);
                }
                errors.add("programMatrixValidation", error);
                addErrors(httpServletRequest, errors);
            }
            ProgramMatrixTrig pmt = programManager1.getProgramMatrixTrig();
            httpServletRequest.setAttribute("ProgramMatrixTrig", pmt);
        } else {
        	List<ProgramMatrix> list = new ArrayList<ProgramMatrix>();
        	com.akuacom.pss2.program.ProgramManager programManager1 = com.akuacom.pss2.core.EJBFactory.getBean(com.akuacom.pss2.program.ProgramManager.class);
        	try
            {
                programManager1.saveProgramMatrixTrig(list);
            }
            catch (Exception e)
            {
                ValidationException ve = ErrorUtil.getValidationException(e);
                ActionErrors errors = new ActionErrors();
                ActionMessage error;
                if (ve != null) {
                    error = new ActionMessage(ve.getLocalizedMessage(), false);
                } else {
                    error = new ActionMessage(ErrorUtil.getErrorMessage(e), false);
                }
                errors.add("programMatrixValidation", error);
                addErrors(httpServletRequest, errors);
            }
            ProgramMatrixTrig pmt = programManager1.getProgramMatrixTrig();
            httpServletRequest.setAttribute("ProgramMatrixTrig", pmt);
        }
        return actionMapping.findForward("view");
    }

}