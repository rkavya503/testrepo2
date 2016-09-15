package com.akuacom.pss2.web.event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.program.ProgramManager;

/**
 *
 * Filename:    EventDetailDispatchAction.java
 * Description: Dispatch request to suitable EventDetail JSF component
 * Copyright:   Copyright (c)2010
 * Company:
 * @author:     Yang Liu
 * @version:
 * Create at:   Dec 9, 2010 2:55:43 PM
 *
 * Modification History:
 * Date         Author      Version     Description
 * ------------------------------------------------------------------
 * Dec 9, 2010   Yang Liu   1.0         1.0 Version
 */
public class EventDetailDispatchAction extends DispatchAction {
	private static final Logger log = Logger.getLogger(EventDetailDispatchAction.class.getName());
	public static final String CPPSchedulePage="CPPSchedulePage";
	public static final String FastDRSchedulePage ="FastDRSchedulePage";
	public static final String FastDRNotificationPage="FastDRNotificationPage";
	public static final String DBPNoBidSchedulePage="DBPNoBidSchedulePage";
	public static final String DBPSchedulePage="DBPSchedulePage";
	public static final String IssueSchedulePage="IssueSchedulePage";
	public static final String DemoSchedulePage="DemoSchedulePage";
	public static final String TestSchedulePage="TestSchedulePage";
	public static final String ButtonOnlySchedulePage="ButtonOnlySchedulePage";
	public static final String SCERTPSchedulePage="SCERTPSchedulePage";
	public static final String LocationBasedEventCreationPage = "LocationSchedulePage";

	public static final String CPPSchedulePageJSF="tabsCPPScheduleEvent.jsf";
	public static final String FastDRNotificationPageJSF ="createFastDRNotificationEvent.jsf";
	public static final String FastDRSchedulePageJSF="createFastDRScheduleEvent.jsf";
	public static final String ButtonOnlySchedulePageJSF="createButtonOnlyScheduleEvent.jsf";
	public static final String IssueSchedulePageJSF="createIssueScheduleEvent.jsf";
	public static final String TestSchedulePageJSF="createTestScheduleEvent.jsf";
	public static final String DBPSchedulePageJSF="createDBPScheduleEvent.jsf";
	public static final String DemoSchedulePageJSF="tabsDemoScheduleEvent.jsf";
	public static final String DBPNoBidSchedulePageJSF="createDBPNoBidScheduleEvent.jsf";

	public static final String PrefixWebLocation="../pss2.utility/jsp/event/";
	public static final String EVT_CREATION="../pss2.utility/jsp/evtcreate/";
	
	public static final String COMMON_EVT_WIZARD="event.jsf";
	public static final String DEMO_EVT_WIZARD="demo/demoEvent.jsf";
	
	public static final String LOCATION_BASED_EVT_WIZARD="location/locationEvent.jsf";
	
	/**
	 * Get the dispatch path wich is stored in Database with uiScheduleEventString attribute.
	 * @param programName
	 *
	 * @return
	 * CPPSchedulePage			CPP/DRC/CBP
	 * DBPNoBidSchedulePage		DBP
	 * DBPSchedulePage			DBP
	 * IssueSchedulePage
	 * DemoSchedulePage			DEMO
	 * TestSchedulePage
	 * ButtonOnlySchedulePage	RTP
	 *
	 */
	public String getDispatchPath(String programName){
		ProgramManager programManager = EJBFactory.getBean(ProgramManager.class);
		return programManager.getUiScheduleEventString(programName);
	}



	/**
	 * Dispatch the action to the suitable Presentation Layer
	 *
	 * @param actionMapping
	 * @param dispatchPath
	 * @param programName
	 * @return
	 */
    private ActionForward dispatchForwardByPath(ActionMapping actionMapping,String dispatchPath,String programName) {
    	if(dispatchPath.equalsIgnoreCase(EventDetailDispatchAction.CPPSchedulePage)){
    		//ActionForward partForward = new ActionForward (EventDetailDispatchAction.PrefixWebLocation+EventDetailDispatchAction.CPPSchedulePageJSF+"?renewFlag=true&programName="+programName, true);
    		ActionForward partForward = new ActionForward (
    				EVT_CREATION+COMMON_EVT_WIZARD+"?programName="+programName, true);
	        return partForward;//finished
    	}else if (dispatchPath.equalsIgnoreCase(EventDetailDispatchAction.LocationBasedEventCreationPage)){
    		//ActionForward partForward = new ActionForward (EventDetailDispatchAction.PrefixWebLocation+EventDetailDispatchAction.CPPSchedulePageJSF+"?renewFlag=true&programName="+programName, true);
    		ActionForward partForward = new ActionForward (
    				EVT_CREATION+LOCATION_BASED_EVT_WIZARD+"?programName="+programName, true);
	        return partForward;//finished)
    	}else if(dispatchPath.equalsIgnoreCase(EventDetailDispatchAction.FastDRSchedulePage)){
    		ActionForward partForward = new ActionForward (EventDetailDispatchAction.PrefixWebLocation+EventDetailDispatchAction.FastDRSchedulePageJSF+"?renewFlag=true&programName="+programName, true);
	        return partForward;//finished
    	}else if(dispatchPath.equalsIgnoreCase(EventDetailDispatchAction.FastDRNotificationPage)){
    		ActionForward partForward = new ActionForward (EventDetailDispatchAction.PrefixWebLocation+EventDetailDispatchAction.FastDRNotificationPageJSF +"?renewFlag=true&programName="+programName, true);
	        return partForward;//finished
    	}else if(dispatchPath.equalsIgnoreCase(EventDetailDispatchAction.DBPNoBidSchedulePage)){
    		ActionForward partForward = new ActionForward (EventDetailDispatchAction.PrefixWebLocation+EventDetailDispatchAction.DBPNoBidSchedulePageJSF+"?renewFlag=true&programName="+programName, true);
	        return partForward;//finished
    	}else if(dispatchPath.equalsIgnoreCase(EventDetailDispatchAction.DBPSchedulePage)){
    		//Some bug exist,need testing
    		//return actionMapping.findForward(dispatchPath);
    		ActionForward partForward = new ActionForward (EventDetailDispatchAction.PrefixWebLocation+EventDetailDispatchAction.DBPSchedulePageJSF+"?renewFlag=true&programName="+programName, true);
	        return partForward;
    	}else if(dispatchPath.equalsIgnoreCase(EventDetailDispatchAction.IssueSchedulePage)){
    		ActionForward partForward = new ActionForward (EventDetailDispatchAction.PrefixWebLocation+EventDetailDispatchAction.IssueSchedulePageJSF+"?renewFlag=true&programName="+programName, true);
	        return partForward;//finished
    	}else if(dispatchPath.equalsIgnoreCase(EventDetailDispatchAction.DemoSchedulePage)){
    		//Need design how to using flex with jsf
    		//ActionForward partForward = new ActionForward (EventDetailDispatchAction.PrefixWebLocation+EventDetailDispatchAction.DemoSchedulePageJSF+"?renewFlag=true&programName="+programName, true);
    		ActionForward partForward = 
    			new ActionForward (EVT_CREATION+DEMO_EVT_WIZARD+"?programName="+programName, true);
	        return partForward;//finished
    	}else if(dispatchPath.equalsIgnoreCase(EventDetailDispatchAction.TestSchedulePage)){
    		ActionForward partForward = new ActionForward (EventDetailDispatchAction.PrefixWebLocation+EventDetailDispatchAction.TestSchedulePageJSF+"?renewFlag=true&programName="+programName, true);
	        return partForward;//finished
    	}else if(dispatchPath.equalsIgnoreCase(EventDetailDispatchAction.ButtonOnlySchedulePage)){
    		ActionForward partForward = new ActionForward (EventDetailDispatchAction.PrefixWebLocation+EventDetailDispatchAction.ButtonOnlySchedulePageJSF+"?renewFlag=true&programName="+programName, true);
	        return partForward;//finished
    	}else if(dispatchPath.equalsIgnoreCase(EventDetailDispatchAction.SCERTPSchedulePage)){
    		ActionForward partForward = new ActionForward (EventDetailDispatchAction.PrefixWebLocation+EventDetailDispatchAction.ButtonOnlySchedulePageJSF+"?renewFlag=true&programName="+programName, true);
	        return partForward;//finished
    	}else{
    		return actionMapping.findForward(dispatchPath);
    	}
    }

	/**
	 * Dispatch request to suitable EventDetail JSF component
	 */
	protected ActionForward unspecified(ActionMapping actionMapping,ActionForm actionForm, HttpServletRequest request,HttpServletResponse response) throws Exception {

		String programName = request.getParameter("programName");

		 if(programName != null && !programName.isEmpty()){
			 String dispatchPath = getDispatchPath(programName);
			 return dispatchForwardByPath(actionMapping,dispatchPath,programName);
		 }

		 //Default Not Find the correct Event JSF page
		 return actionMapping.findForward("success");
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
    public ActionForward create(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return lookUpForward(request, actionMapping);
    }

    /**
     * Look up forward.
     *
     * @param request the request
     * @param actionMapping the action mapping
     *
     * @return the action forward
     */
    private ActionForward lookUpForward(HttpServletRequest request, ActionMapping actionMapping) {
//        final ProgramManager programManager = EJBFactory.getBean(ProgramManager.class);
//        final String programName = request.getParameter("programName");
//        final Program program = programManager.getProgram(programName);
//        return actionMapping.findForward(program.getUiScheduleEventString());
        String programName = request.getParameter("programName");
        String dispatchPath = getDispatchPath(programName);
		return dispatchForwardByPath(actionMapping,dispatchPath,programName);
    }

}