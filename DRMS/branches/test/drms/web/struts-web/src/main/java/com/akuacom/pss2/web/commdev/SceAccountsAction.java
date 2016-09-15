/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.commdev.SceAccountsAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.commdev;

import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.subaccount.SubAccount;
import com.akuacom.pss2.web.util.EJBFactory;
import com.akuacom.pss2.client.ClientManager;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * The Class SceAccountsAction.
 */
public class SceAccountsAction extends DispatchAction {
    
    /* (non-Javadoc)
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ActionForward unspecified(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        SceAccountsForm form = (SceAccountsForm) actionForm;
        String user = form.getUserName();
        ParticipantManager pm = EJBFactory.getBean(ParticipantManager.class);
        List<SubAccount> list = pm.getSubAccounts(user);
        httpServletRequest.setAttribute("accounts", list);
        return actionMapping.findForward("success");
    }

    /**
     * Cancel.
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
    public ActionForward cancel(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect("commDevDetail.do?dispatch=edit&userName=" + request.getParameter("userName"));
        return null;
    }

    /**
     * Adds the.
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
    public ActionForward add(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SceAccountsForm form = (SceAccountsForm) actionForm;

        SubAccount account = new SubAccount();
        account.setSubAccountId(form.getSceAccount());
        ClientManager cm = EJBFactory.getBean(ClientManager.class);
        Participant p = cm.getClient(form.getUserName());
        account.setParticipant(p);
        account.setComment(form.getComment());

        ParticipantManager pm = EJBFactory.getBean(ParticipantManager.class);
        pm.createSubAccount(account, form.getUserName());

        form.setComment("");
        form.setSceAccount("");

        return unspecified(actionMapping, actionForm, request, response);
    }

    /**
     * Delete.
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
    public ActionForward delete(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SceAccountsForm form = (SceAccountsForm) actionForm;
        String[] accounts = request.getParameterValues("accounts");
        ParticipantManager pm = EJBFactory.getBean(ParticipantManager.class);
        List<SubAccount> list = pm.getSubAccounts(form.getUserName());
        for (String account : accounts) {
            for (int j = 0; j < list.size(); j++) {
                SubAccount info = list.get(j);
                if (info.getSubAccountId().equals(account)) {
                    list.remove(j);
                    break;
                }
            }
        }
        pm.updateSubAccounts(list, form.getUserName());
        return unspecified(actionMapping, actionForm, request, response);
    }

}
