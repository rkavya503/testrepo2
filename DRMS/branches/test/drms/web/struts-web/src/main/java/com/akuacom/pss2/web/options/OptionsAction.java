/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.options.OptionsAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.options;

import com.akuacom.accmgr.wsclient.AccMgrWSClient;
import com.akuacom.accmgr.ws.AccMgrWS;
import com.akuacom.accmgr.ws.User;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.utils.MD5Tool;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The Class OptionsAction.
 */
@SuppressWarnings({"UnusedDeclaration"})
public class OptionsAction extends DispatchAction {

    /* (non-Javadoc)
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ActionForward unspecified(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return edit(actionMapping, actionForm, request, response);
    }

    /**
     * Edits the.
     * 
     * @param mapping the mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     * 
     * @return the action forward
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    public ActionForward edit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        final OptionsForm form = (OptionsForm)actionForm;
        form.setUserName(request.getRemoteUser());
         return mapping.findForward("edit");
    }

    /**
     * Update.
     * 
     * @param mapping the mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     * 
     * @return the action forward
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    public ActionForward update(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        final OptionsForm form = (OptionsForm)actionForm;

        try {
            final AccMgrWSClient accmgrClient = new AccMgrWSClient();
            accmgrClient.initialize();

            final AccMgrWS stub = accmgrClient.getAccmgr();
            String userName = form.getUserName();
            User user = stub.getUserByName("PSS2", userName);
            String password, password1, password2;
            password = form.getOldPassword();
            password1 = form.getPassword();
            password2 = form.getPassword2();
            if (!user.getPassword().equals(MD5Tool.getHashString(password))) {
                ActionErrors errors = new ActionErrors();
                errors.add("password", new ActionMessage("pss2.options.oldpassword.wrong"));
                saveErrors(request, errors);
                return mapping.findForward("edit");
            } else if (user.getPassword().equals(MD5Tool.getHashString(password1))) {
                ActionErrors errors = new ActionErrors();
                errors.add("password", new ActionMessage("pss2.options.password.sameAsBefore"));
                saveErrors(request, errors);
                return mapping.findForward("edit");
            }

            // if everything is right, update password.            
            stub.changePassword(user.getId(), password, password1);

            // set pop up message
            String message;
            if (request.isUserInRole(DrasRole.Admin.toString())
                    || request.isUserInRole(DrasRole.Operator.toString())) {
                message = "pss2.options.updated";
            } else {
                message ="pss2.options.updated.clir";
            }
            request.setAttribute("popUpMessage", message);


        } catch (Exception e) {
            log.error("failed to update password", e);
        }

        return mapping.findForward("edit");
    }

    /**
     * Cancel.
     * 
     * @param mapping the mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     * 
     * @return the action forward
     */
    public ActionForward cancel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("cancel");
    }
}
