/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.web.uohome.UOProgramAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.uohome;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.accmgr.ws.AccMgrWS;
import com.akuacom.accmgr.ws.Tag;
import com.akuacom.accmgr.ws.User;
import com.akuacom.accmgr.wsclient.AccMgrWSClient;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.pss2.web.event.EventListForm;
import com.akuacom.pss2.web.util.DisplayTagUtil;
import com.akuacom.pss2.web.util.EJBFactory;
import com.kanaeki.firelog.util.FireLogEntry;

/**
 * The Class UOProgramAction.
 */
public class UOProgramAction extends DispatchAction {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(UOProgramAction.class
			.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts
	 * .action.ActionMapping, org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward unspecified(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

        String uid = (String) request.getSession().getAttribute("uid");
        ActionForward partForward = new ActionForward ("../pss2.utility/programs.jsf?uid=" + uid, true);
            return partForward;
  /*
		EventListForm form = (EventListForm) actionForm;


		form.setProgramList(getMyProgramList(request, actionForm));
		final boolean export = DisplayTagUtil.isExport(request);
		boolean readonly = true;
		if (request.isUserInRole(DrasRole.Admin.toString())
				|| request.isUserInRole(DrasRole.Operator.toString())) {
			readonly = false;
		}
		if (readonly) {
			request.setAttribute("readonly", "true");
		} else {
			request.setAttribute("readonly", "false");
		}

		boolean editonly = true;
		if (request.isUserInRole(DrasRole.Admin.toString())) {
			editonly = false;
		}
		if (editonly) {
			request.setAttribute("editonly", "true");
		} else {
			request.setAttribute("editonly", "false");
		}

		if (export) {
			return actionMapping.findForward("export");
		} else {
			return actionMapping.findForward("success");
		}
*/
	}

	/**
	 * Gets the my list in tag.
	 * 
	 * @param request
	 *            the request
	 * @param tagName
	 *            the tag name
	 * 
	 * @return the my list in tag
	 */
	static public String[] getMyListInTag(HttpServletRequest request,
			String tagName) {
		String[] list = null;
		Principal principal = request.getUserPrincipal();
		String user = principal.getName();

		AccMgrWSClient accmgrClient = new AccMgrWSClient();
		accmgrClient.initialize();
		AccMgrWS accmgr = accmgrClient.getAccmgr();
		User userObj = accmgr.getUserByName("PSS2", user);

		List<Tag> programNamesTag = userObj.getTags();

		if (programNamesTag == null) {
			FireLogEntry logEntry = LogUtils.createLogEntry();
			logEntry.setDescription("The tag: " + tagName
					+ " doesn't exist for this user: " + user);
			log.error(logEntry);
		}

		for (Tag programTag : programNamesTag) {
			if (tagName.equalsIgnoreCase(programTag.getName())) {
				list = programTag.getValue().split(",");
				break;
			}
		}
		if (list == null || list.length <= 0) {
			FireLogEntry logEntry = LogUtils.createLogEntry();
			logEntry.setDescription("The tag: " + tagName
					+ " doesn't exist for this user: " + user);
			log.error(logEntry);
		}
		return list;
	}

	/**
	 * Gets the my program list.
	 * 
	 * @param request
	 *            the request
	 * 
	 * @return the my program list
	 */
	static public List<Program> getMyProgramList(HttpServletRequest request,
			ActionForm actionFrom) {

       

		EventListForm form = (EventListForm) actionFrom;

		request.getSession().getAttribute("MYPROGRAMS");
		List<Program> programList = (List<Program>) request.getSession()
				.getAttribute("MYPROGRAMS");
		if (programList != null) {
			// return programList;
		}

		com.akuacom.pss2.program.ProgramManager programManager1 = com.akuacom.pss2.core.EJBFactory
				.getBean(com.akuacom.pss2.program.ProgramManager.class);
		com.akuacom.pss2.participant.UserEAO UserEAOManager = com.akuacom.pss2.core.EJBFactory
				.getBean(com.akuacom.pss2.participant.UserEAO.class);
		ProgramParticipantManager programParticipantManager = EJBFactory
				.getBean(ProgramParticipantManager.class);

		if (request.isUserInRole(DrasRole.Admin.toString())
				|| (request.isUserInRole(DrasRole.Operator.toString()))) {
			programList = programManager1.getProgramsAsPrograms();
		} else {
			String[] progNames = UOProgramAction.getMyListInTag(request,
					"UO_PROGRAMS");
			programList = new ArrayList<Program>();
			for (String name : progNames) {
				Program p = programManager1.getProgram(name);
				programList.add(p);
			}

		}

		Map<String, List<String>> partProMap = new HashMap<String, List<String>>();

		for (Program p : programList) {
			String progName = p.getProgramName();
			int clientCount = 0;
			int participantCount = 0;
			List<String> countList = new ArrayList<String>();
			for (ProgramParticipant pp : p.getProgramParticipants()) {
				if (pp.getParticipant().isClient()) {
					clientCount++;
				} else {
					participantCount++;
				}
			}
			countList.add("" + clientCount);
			countList.add("" + participantCount);
			partProMap.put(progName, countList);

		}

		form.setParticipantInProgram(partProMap);
		return programList;
	}

	public ActionForward deleteEdit(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		EventListForm form = (EventListForm) actionForm;
		com.akuacom.pss2.program.ProgramManager programManager1 = com.akuacom.pss2.core.EJBFactory
				.getBean(com.akuacom.pss2.program.ProgramManager.class);

		String[] programs = form.getProgramNames();
		String[] programNames = request.getParameterValues("programNames");

		List<Program> proList = new ArrayList();
		form.setProgramList(null);

		try {
			if (programNames != null) {
				for (int i = 0; i < programNames.length; i++) {
					String name = programNames[i].split("#")[0];
					Program p = programManager1.getProgram(name);
					proList.add(p);
				}
				form.setProgramList(proList);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return actionMapping.findForward("deleteEdit");
	}

	/**
	 * Delete.
	 * 
	 * @param mapping
	 *            the mapping
	 * @param actionForm
	 *            the action form
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * 
	 * @return the action forward
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EventListForm form = (EventListForm) actionForm;
		com.akuacom.pss2.program.ProgramManager programManager1 = com.akuacom.pss2.core.EJBFactory
				.getBean(com.akuacom.pss2.program.ProgramManager.class);

		String[] programs = form.getProgramNames();
		String name = "";

		try {
			if (programs != null) {
				for (int i = 0; i < programs.length; i++) {
					name = programs[i].split("#")[0];
					programManager1.removeProgram(name);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		form.setProgramNames(null);
		return mapping.findForward("updated");
	}

	public ActionForward cancel(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return mapping.findForward("updated");
	}

}