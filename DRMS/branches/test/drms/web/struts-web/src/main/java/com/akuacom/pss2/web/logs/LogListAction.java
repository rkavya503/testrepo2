/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.logs.LogListAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.logs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;
import org.displaytag.properties.SortOrderEnum;

import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.web.util.DisplayTagUtil;
import com.honeywell.drms.log.reader.LogManager;
import com.honeywell.drms.log.reader.LogSearchCriteria;
import com.honeywell.drms.log.reader.LogUtils;
import com.kanaeki.firelog.util.FireLogEntry;


public class LogListAction extends DispatchAction {

	/** The log. */
	private static Logger log = Logger.getLogger(LogListAction.class.getName());
	
	final String logDir = System.getProperty("jboss.server.log.dir");
	 
    /** The Constant QUERIED. */
    public static final String QUERIED = "Queried";

    /** variable put in session **/
    public static final String LOG_MANAGER="_log_manager";
    
    public static final String SEARCH_CRITERIA="_search_criteria";
    
    public static final String SEARCH_RESULT="_search_result";
    
    
    /* (non-Javadoc)
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ActionForward unspecified(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final HttpSession session = request.getSession();

        final Object queried = session.getAttribute(QUERIED);
        if (request.isUserInRole(DrasRole.Admin.toString())) {
            request.setAttribute("admin", true);
        }
        if (queried != null) {
            return list(actionMapping, actionForm, request, response);
        }
        return actionMapping.findForward("success");
    }

    /**
     * Back.
     * 
     * @param mapping the mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     * 
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    public ActionForward back(ActionMapping mapping, ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        LogListForm form = (LogListForm) actionForm;
        final HttpSession session = request.getSession();

        final Object queried = session.getAttribute(QUERIED);
        if (request.isUserInRole(DrasRole.Admin.toString())) {
            request.setAttribute("admin", true);
        }
        if (queried != null) {
            final Object obj = session.getAttribute("LOG_QUERY_OBJ");
            if (obj != null) {
                BeanUtils.copyProperties(form, obj);
            }
            return mapping.findForward("interim");
        }
        return mapping.findForward("success");
    }

    /**
     * List.
     * 
     * @param mapping the mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     * 
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    @SuppressWarnings({"UnusedDeclaration", "unchecked"})
    public ActionForward list(ActionMapping mapping, ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
		
        LogListForm form = (LogListForm) actionForm;
        final HttpSession session = request.getSession();

        if (request.isUserInRole(DrasRole.Admin.toString())) {
            request.setAttribute("admin", true);
        }
        
        if(session.getAttribute(LOG_MANAGER)==null){
        	String logPath = logDir;
        	session.setAttribute(LOG_MANAGER, new LogManager(logPath));
        }
        
        LogManager logManager = (LogManager) session.getAttribute(LOG_MANAGER);
        
        Boolean back = (Boolean) session.getAttribute("LOG_NAVIGATE_BACK");
        if (back != null && back) {
            session.removeAttribute("LOG_NAVIGATE_BACK");
            final Object queried = session.getAttribute(QUERIED);
            if (queried != null) {
                final Object obj = session.getAttribute("LOG_QUERY_OBJ");
                int page = form.getPage();
                if (obj != null) {
                    BeanUtils.copyProperties(form, obj);
//                    if (page > 1) { // don't override page from request
//                        form.setPage(page);
//                    }
                }
            }
        }
        int objectsPerPage = form.getObjectsPerPage();
    	final boolean export = DisplayTagUtil.isExport(request);
    	
    	Date startDateTime = null;
    	Date endDateTime = null;
		final ActionErrors errors = new ActionErrors();
		String dateTimeFormat=(String)request.getSession().getServletContext().getAttribute("dateTimeFormat");
		if (dateTimeFormat==null)
			dateTimeFormat="MM/dd/yyyy hh:mm:ss";

        SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(dateTimeFormat);
        try {
    		startDateTime = simpleDateTimeFormat.parse(form.getStartDate() + " " + form.getStartTime());
    	} catch (ParseException e) {
        	
			errors.add("log.report.startdate.invalid", 
					new ActionMessage("pss2.log.report.startdate.invalid", 
							form.getStartDate() + " " + form.getStartTime(), dateTimeFormat));
		} 
    	
    	try {
    		endDateTime = simpleDateTimeFormat.parse(form.getEndDate() + " " + form.getEndTime());
    	} catch (ParseException e) {
			errors.add("log.report.enddate.invalid", 
					new ActionMessage("pss2.log.report.enddate.invalid", 
							form.getEndDate() + " " + form.getEndTime(), dateTimeFormat));
		} 
    	
        if ((startDateTime != null) && (endDateTime != null)) {
        	LogSearchCriteria sc = new LogSearchCriteria(
        					startDateTime,
        					endDateTime, 
        					LogUtils.getIntLevel(form.getLogLevel().toUpperCase()),
        					form.getUserName(), 
        					getCategory(form),
        					getProgram(form), 
        					form.getDescriptionWord());
        	
        	
        	int pageNumber = form.getPage();
        	List<FireLogEntry> entries  = Collections.emptyList();
        	//new request when enter page
        	if(pageNumber<0){
        		session.removeAttribute(SEARCH_CRITERIA);
        		session.removeAttribute(SEARCH_RESULT);
        	}else{
        		LogSearchCriteria oldSC = (LogSearchCriteria) session.getAttribute(SEARCH_CRITERIA);
            	session.setAttribute(SEARCH_CRITERIA, sc);
            	  
            	if(LogSearchCriteria.needSearchAgain(oldSC, sc)){
            		entries = logManager.doQuery(sc);
            		session.setAttribute(SEARCH_RESULT, entries);
            	}else{
            		entries = (List<FireLogEntry>) session.getAttribute(SEARCH_RESULT);
            	}
        	}
        	
        	final int count = entries.size();
        	form.setLogCount(count);
        
        	if(pageNumber<0) pageNumber =1;
        	int startIndex = (pageNumber - 1) * objectsPerPage; // 0 based.
        	
        	if (export) {
        		objectsPerPage = 5000;
        		startIndex = 0;
        	}
        	
        	int end = startIndex + objectsPerPage;
        	if(end>count) end = count;
        	
        	//DRMS-6620, subList method returns type of java.util.RandomAccessSubList
        	//which dosn't implements serializable but will be put in session.
        	//java.io.NotSerializableException will be thrown with clustering environment
        	//where session will be serialized and replicated across different nodes
        	//List<FireLogEntry> sublist = entries.subList(startIndex, end);
        	FireLogEntry all[] =entries.toArray(new FireLogEntry[0]);
        	FireLogEntry sub[] = new FireLogEntry[end-startIndex];
        	System.arraycopy(all, startIndex, sub, 0, end-startIndex);
        	List<FireLogEntry> sublist = Arrays.asList(sub);
        	
        	final LogPageList pl = getLogPageList(objectsPerPage, count, pageNumber, sublist);
        	form.setLogPageList(pl);
        	
        	session.setAttribute(QUERIED, true);
        	final LogListForm logListForm = new LogListForm();
        	BeanUtils.copyProperties(logListForm, form);
        	session.setAttribute("LOG_QUERY_OBJ", logListForm);
        	if (export)
                return mapping.findForward("export");
        } 
		if (errors.size() > 0)
			saveErrors(request, errors);
		
	    return mapping.findForward("success");
    }
    
    
    /**
     * Gets the log page list.
     * 
     * @param objectsPerPage the objects per page
     * @param count the count
     * @param pageNumber the page number
     * @param list the list
     * 
     * @return the log page list
     */
    private LogPageList getLogPageList(int objectsPerPage, int count, int pageNumber, List<FireLogEntry> list) {
        final LogPageList pl = new LogPageList();
        pl.setFullListSize(count);
        pl.setList(list);
        pl.setObjectsPerPage(objectsPerPage);
        pl.setPageNumber(pageNumber);
        pl.setSortDirection(SortOrderEnum.DESCENDING);
        return pl;
    }

    /**
     * Gets the program.
     * 
     * @param form the form
     * 
     * @return the program
     */
    private String getProgram(LogListForm form) {
        final String program = form.getProgram();
        if (program == null || program.endsWith("--")) {
            return null;
        } else {
            return program;
        }
    }

    /**
     * Select between built-in dropdown menu and custom input.
     * 
     * @param form Form for parameters
     * 
     * @return the string of category query
     */
    private String getCategory(LogListForm form) {
        final String categoryRadio = form.getCategoryRadio();
        if ("builtin".equals(categoryRadio)) {
            final String builtInCategory = form.getBuiltInCategory();
            if ("--".equals(builtInCategory)) {
                return null;
            }
            return builtInCategory;
        } else if ("custom".equals(categoryRadio)) {
            return form.getCategory();
        }
        return form.getCategory();
    }

    /**
     */
    public ActionForward now(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogListForm form = (LogListForm) actionForm;
        Calendar cal = Calendar.getInstance();
        
		String dateTimeFormat=(String)request.getSession().getServletContext().getAttribute("dateTimeFormat");
		if (dateTimeFormat==null)
			dateTimeFormat="MM/dd/yyyy hh:mm:ss";

        String endTime = new SimpleDateFormat(dateTimeFormat).format(cal.getTime());
        form.setEndTime(endTime.substring(endTime.indexOf(' ') + 1));
        form.setEndHour("" + cal.get(Calendar.HOUR_OF_DAY));
        form.setEndMinute("" + cal.get(Calendar.MINUTE));
        form.setEndSecond("" + cal.get(Calendar.SECOND));
        return unspecified(mapping, form, request, response);
    }
}
