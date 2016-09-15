/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.login.LoginServlet.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.login;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.SystemManagerBean;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.util.EncryptUtil;
import com.akuacom.pss2.util.UserType;

/**
 * The Class LoginServlet.
 */
public class LoginServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(LoginServlet.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if (request.isUserInRole(DrasRole.Admin.toString())
				|| request.isUserInRole(DrasRole.Operator.toString())
				|| request.isUserInRole(DrasRole.Readonly.toString())
				|| request.isUserInRole(DrasRole.Dispatcher.toString())) {
			String uid = (String) request.getSession().getAttribute("uid");
			
			response.sendRedirect("/pss2.utility/participant.jsf?loginFlag=true&uid="
					+ uid);
			// response.sendRedirect("index.do");
		} else if (request.isUserInRole(DrasRole.FacilityManager.toString())) {

			SystemManager systemManager = EJBFactory
					.getBean(SystemManager.class);
			String defaultPage = systemManager.getPss2Features()
					.getFeatureFacdashDefaultPage();
			Boolean newEnabled = systemManager.getPss2Features()
					.isNewsEnabled();
			boolean gotoSimplifiedDashboard = false;

			// this is here to avoid loggin out someone else? see LogoutAction
			// in struts-web
			String uid = (String) request.getUserPrincipal().getName();

			ParticipantManager pManager = EJBFactory
					.getBean(ParticipantManager.class);
			Participant loggedUser = pManager.getParticipantOnly(uid);

			if (loggedUser != null) {
				gotoSimplifiedDashboard = UserType.SIMPLE.equals(loggedUser
						.getUserType());
			}

			if (gotoSimplifiedDashboard) {
				log.debug("redirect to sipleDashboard");
				response.sendRedirect("/facdash/jsp/simpleDashboard.jsf");
			} else {
				log.debug("redirect to facdash");
				response.sendRedirect("/facdash");
			}
		}
		
		// get util name to add it to the session
		SystemManager systemManager = EJBFactory
				.getBean(SystemManagerBean.class);
		try {
			CoreProperty corp = systemManager.getPropertyByName("utilityName");
			request.getSession().setAttribute("utilityName",
					corp.getStringValue());
		} catch (EntityNotFoundException e) {

		}

		/*
		 * List<CoreProperty> coreProp = systemManager.getAllProperties();
		 * for(CoreProperty corp : systemManager.getAllProperties()){ if
		 * (corp.getPropertyName().equalsIgnoreCase("utilityName"))
		 * request.getSession().setAttribute("utilityName",
		 * corp.getStringValue()); }
		 */

		String headerStyle = "testServerUtilityOperator";
		if (systemManager.getPss2Features().isProductionServer()) {
			headerStyle = "productionServerUtilityOperator";
		}
		request.getSession(false).setAttribute("headerStyle", headerStyle);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
