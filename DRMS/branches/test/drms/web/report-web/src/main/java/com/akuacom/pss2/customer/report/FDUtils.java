/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.FDUtils.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package com.akuacom.pss2.customer.report;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class FDUtils.
 */
public class FDUtils
{
	
	/**
	 * Adds the msg info.
	 * 
	 * @param message the message
	 */
	static public void addMsgInfo(String message)
	{
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
			message, message));
	}

	/**
	 * Adds the msg error.
	 * 
	 * @param message
	 *            the message
	 */
	static public void addMsgError(String message)
	{
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
			message, message));
	}
	
	static public void addMsgError(String forTarget, String message)
	{
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(forTarget, new FacesMessage(FacesMessage.SEVERITY_ERROR,
			message, message));
	}
	
	
    static public void reDirect(String url) throws IOException{
         HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().
                    getExternalContext().getResponse();
         response.sendRedirect(url);
    }

    
    static public SearchCriteria getSearchCriteria()
	{
		return (SearchCriteria)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("sc");
	}
    
    
	static public ReportContext getReportWebContext()
	{
		return (ReportContext)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("reportWebContext");
	}
	
	
    public static void export(String filename, String contents) throws IOException {
    	PrintWriter out = null;
    	FacesContext fc = null;
    	try{
	        String contentType = "application/vnd.ms-excel";
	        fc = FacesContext.getCurrentInstance();
	        filename = fc.getExternalContext().getUserPrincipal().toString() + "-"+ filename;
	        HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
	        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
	        response.setContentType(contentType);
	
	        out = response.getWriter();
	        out.print(contents);
    	}finally{
    		 out.close();
    	     fc.responseComplete();
    	}
    }
    
    static public String getLoginUserName()
	{
    	ExternalContext context = 
			FacesContext.getCurrentInstance().getExternalContext();
    	
		String loginName =  (String) context.getSessionMap().get("loginUserName");
		if(loginName==null||loginName.trim().length()==0){
			loginName = context.getUserPrincipal().getName();
		}
		
		return loginName;
	}
}












