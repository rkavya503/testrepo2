/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.logs.LogDetailAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.logs;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Level;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.pss2.util.DrasRole;
import com.honeywell.drms.log.reader.LogManager;
import com.honeywell.drms.log.reader.LogSearchCriteria;
import com.kanaeki.firelog.util.FireLogEntry;

/**
 * The Class LogDetailAction.
 */
public class LogDetailAction extends DispatchAction
{
	   /** variable put in session **/
    public static final String LOG_MANAGER="_log_manager";
	
    
    /** The Constant LOG_LIST_FROM. */
    public static final String LOG_LIST_FROM = "LogFilterForm";
    
    protected static Pattern SERVER_PATTERN = Pattern.compile("((.*\\\\server\\\\)(\\w*))(\\\\tmp\\\\.*)");
    

    /* (non-Javadoc)
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogDetailForm form = (LogDetailForm) actionForm;
        int index = form.getIndex();

        final HttpSession session = request.getSession();
        LogListForm listForm = (LogListForm) session.getAttribute(LOG_LIST_FROM);

        final LogListAction action = new LogListAction();
        final int count = listForm.getLogCount();

        int page = listForm.getPage();
        if(page<0) page =1;
        if (index == 0 && page == 1) {
            request.setAttribute("NO_PREVIOUS", true);
        } else if ((page - 1) * listForm.getObjectsPerPage() + index + 1 == count) {
            request.setAttribute("NO_NEXT", true);
        }

        if (index < 0) {
            listForm.setPage(page - 1);
            action.list(actionMapping, listForm, request, response);
            listForm = (LogListForm) session.getAttribute(LOG_LIST_FROM);
            index = listForm.getObjectsPerPage() - 1;
        } else if (index >= listForm.getObjectsPerPage()) {
            listForm.setPage(page + 1);
            action.list(actionMapping, listForm, request, response);
            listForm = (LogListForm) session.getAttribute(LOG_LIST_FROM);
            index = 0;
        }
        form.setIndex(index);
        
        final List list = listForm.getLogPageList().getList();

        FireLogEntry temp = (FireLogEntry) list.get(index);
        FireLogEntry entry = new FireLogEntry();
        PropertyUtils.copyProperties(entry, temp);

        entry.setStrLogLevel(Level.toLevel(entry.getLogLevel()).toString());

        // mask internal error for operators
        final String operator = DrasRole.Operator.toString();
        if (request.isUserInRole(operator)) {
            final String description = entry.getLongDescr();
            if (description != null && description.contains("Exception:")) {
                entry.setDescription("Internal Error");
            }
            request.setAttribute("userrole", operator);
        } else {
            request.setAttribute("userrole", DrasRole.Admin.toString());
        }

        String descr = entry.getLongDescr();
        if (descr != null) {
            String s = descr.replaceAll("<([^>]*)>", "&lt;$1&gt;");
            entry.setLongDescr(s.replaceAll("\n", "<br/>"));
        }
        request.setAttribute("fireLogEntryDetail", entry);

        return actionMapping.findForward ( "success" );
    }

    /**
     * Quick.
     * 
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the http servlet response
     * 
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public ActionForward quick(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogDetailForm form = (LogDetailForm) actionForm;
        final String id = form.getId();
        
        final HttpSession session = request.getSession();
        
        if(session.getAttribute(LOG_MANAGER)==null){
        	String warPath = request.getSession().getServletContext().getRealPath("");
        	Matcher m =SERVER_PATTERN.matcher(warPath);
        	String logPath = warPath;
        	if(m.find()){
        		logPath =m.group(1)+"\\log";
        	}else{
        		log.error("Unable to locate log path");
        	}
        	session.setAttribute(LOG_MANAGER, new LogManager(logPath));
        }
        
        LogManager logManager = (LogManager) session.getAttribute(LOG_MANAGER);
        LogSearchCriteria sc = new LogSearchCriteria(id);
        
    	List<FireLogEntry> results = logManager.doQuery(sc);
    	FireLogEntry entry = results.size()==1? results.get(0):null;
        
        if (entry == null) {
            final ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("pss2.log.id.nonExist", new ActionMessage("pss2.log.id.nonExist"));
            this.saveErrors(request.getSession(), actionErrors);
            return actionMapping.findForward("parent");
        }

        // mask internal error for operators
        final String operator = DrasRole.Operator.toString();
        if (request.isUserInRole(operator)) {
            final String description = entry.getLongDescr();
            if (description != null && description.contains("Exception:")) {
                entry.setDescription("Internal Error");
            }
            request.setAttribute("userrole", operator);
        } else {
            request.setAttribute("userrole", DrasRole.Admin.toString());
        }

        String descr = entry.getLongDescr();
        if (descr != null) {
            String s = descr.replaceAll("<([^>]*)>", "&lt;$1&gt;");
            entry.setLongDescr(s.replaceAll("\n", "<br/>"));
        }
        request.setAttribute("fireLogEntryDetail", entry);

        request.setAttribute("NO_PREVIOUS", true);
        request.setAttribute("NO_NEXT", true);

        return actionMapping.findForward ( "success" );
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public ActionForward back(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        request.getSession().setAttribute("LOG_NAVIGATE_BACK", true);
        return actionMapping.findForward("parent");
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public ActionForward go(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        return unspecified(actionMapping, actionForm, request, httpServletResponse);
    }
}
