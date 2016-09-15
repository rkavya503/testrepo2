/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.tabs.TabsAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.tabs;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.web.util.Tab;
import com.akuacom.pss2.web.util.TabMap;
import com.akuacom.pss2.system.property.PSS2Features;

/**
 * This class controls which tab to be displayed and active.
 * 
 * @author Dichen Mao
 * @since 4.1
 */
public class TabsAction extends Action {

    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final HttpSession session = request.getSession();
        final String context = request.getContextPath();
        final String action = getActionString((String) session.getAttribute("actionUrl"), context);

        // set value for jsp page
        final Tab tab = TabMap.getActiveTab(action);
        session.setAttribute("activeTab", tab);

        List<TabVO> tabs = new ArrayList<TabVO>();
        // add role related tabs
        if (request.isUserInRole(DrasRole.Admin.toString())
				|| request.isUserInRole(DrasRole.Operator.toString())
				|| request.isUserInRole(DrasRole.Readonly.toString())
				|| request.isUserInRole(DrasRole.Dispatcher.toString())) {
            if(EJBFactory.getBean(SystemManager.class).getPss2Features().isFeatureOpdashEnabled() )
            {
               TabVO dashTab = new TabVO();
                dashTab.setAction("/opdash");
                dashTab.setTitle("Dashboard");
                dashTab.setActive(Tab.Dashboard == tab);
                tabs.add(dashTab);
            }

            TabVO programTab = new TabVO();
            programTab.setAction("/uoProgram");
            programTab.setTitle("Programs");
            programTab.setActive(Tab.Program == tab);
            tabs.add(programTab);

            TabVO participantsTab = new TabVO();
            participantsTab.setAction("/participantsMap");
            participantsTab.setTitle("Participants");
            participantsTab.setActive(Tab.Participants == tab);
            tabs.add(participantsTab);

            TabVO clientsTab = new TabVO();
            //TODO lin change back after fixing mapview https issue
            //if(TabsAction.isDemo())
            if(true)
            {
                clientsTab.setAction("/clientsMap");
                clientsTab.setTitle("Clients");
            }
            else
            {
                clientsTab.setAction("/clientsList");
                clientsTab.setTitle("Clients");
            }
            clientsTab.setActive(Tab.Clients == tab);
            tabs.add(clientsTab);
            
            TabVO eventTab = new TabVO();
            eventTab.setAction("/uoEvent");
            eventTab.setTitle("Events");
            eventTab.setActive(Tab.Event == tab);
            tabs.add(eventTab);

            if(EJBFactory.getBean(SystemManager.class).getPss2Features().isNewsEnabled() )
            {
            TabVO newsTab = new TabVO();
            newsTab.setAction("/news");
            newsTab.setTitle("News & Info");
            newsTab.setActive(Tab.News == tab);
            tabs.add(newsTab);
            }

            TabVO logsTab = new TabVO();
            logsTab.setAction("/logList");
            logsTab.setTitle("Reports");
            logsTab.setActive(Tab.Log == tab);
            tabs.add(logsTab);

        } else if (request.isUserInRole(DrasRole.UtilityOperator.toString())) {
            TabVO programTab = new TabVO();
            programTab.setAction("/uoProgram");
            programTab.setTitle("Programs");
            programTab.setActive(Tab.Program == tab);
            tabs.add(programTab);
            TabVO clientsTab = new TabVO();
            clientsTab.setAction("/uoParticipant");
            clientsTab.setTitle("Clients");
            clientsTab.setActive(Tab.Clients == tab);
            tabs.add(clientsTab);
            TabVO eventTab = new TabVO();
            eventTab.setAction("/uoEvent");
            eventTab.setTitle("Events");
            eventTab.setActive(Tab.Event == tab);
            tabs.add(eventTab);
        }

        // add default tabs
        TabVO optionsTab = new TabVO();
        optionsTab.setAction("/options");
        optionsTab.setTitle("Options");
        optionsTab.setActive(Tab.Options == tab);
        tabs.add(optionsTab);

        TabVO aboutTab = new TabVO();
        aboutTab.setAction("/about");
        aboutTab.setTitle("About");
        aboutTab.setActive(Tab.About == tab);
        tabs.add(aboutTab);

        
        if(request.isUserInRole(DrasRole.Admin.toString())){
			request.setAttribute("usrRole", DrasRole.Admin.toString());
		}
		else if(request.isUserInRole(DrasRole.Operator.toString())){
			request.setAttribute("usrRole", DrasRole.Operator.toString());
		}
		else if(request.isUserInRole(DrasRole.FacilityManager.toString())){
			request.setAttribute("usrRole", DrasRole.FacilityManager.toString());
		}
		else if(request.isUserInRole(DrasRole.Readonly.toString())){
			request.setAttribute("usrRole", DrasRole.Readonly.toString());
		}
		else if(request.isUserInRole(DrasRole.Dispatcher.toString())){
			request.setAttribute("usrRole", DrasRole.Dispatcher.toString());
		}
        
        request.setAttribute("tabs", tabs);

        return mapping.findForward("success");
    }

    /**
     * Gets the action string.
     * 
     * @param action the action
     * @param context the context
     * 
     * @return the action string
     */
    private String getActionString(String action, String context) {
        // remove context path
        final int i = action.indexOf(context);
        if (i > -1) {
            action = action.substring(i + context.length() + 1);
        }
        // remove string on and after ".do"
        final int j = action.indexOf(".do");
        action = action.substring(0, j);
        return action;
    }

    //This is a little hack for Ed's demo. We assume there is only one DEMO program in server
    /**
     * Checks if is demo.
     * 
     * @return true, if is demo
     */
    static public boolean isDemo()
    {
         return false;
    }

    /**
     * Checks if is demo.
     * 
     * @param progName the prog name
     * 
     * @return true, if is demo
     */
    static public boolean isDemo(String progName)
    {
        boolean ret = false;
        com.akuacom.pss2.program.ProgramManager programManager1 = com.akuacom.pss2.core.EJBFactory.getBean(com.akuacom.pss2.program.ProgramManager.class);

        List<String> prognames = programManager1.getPrograms();

        if(prognames == null ) return ret;

        for(String name : prognames)
        {
            Program prog = programManager1.getProgramOnly(name);
            if(prog.getUiScheduleEventString().equalsIgnoreCase("DemoSchedulePage")
                    && name.equals(progName))
            {
                return true;
            }
        }
        return ret;
    }
}
