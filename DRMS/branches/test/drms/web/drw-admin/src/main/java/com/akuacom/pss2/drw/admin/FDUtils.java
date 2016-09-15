/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.facdash.FDUtils.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.drw.admin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

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

	
	/**
	 * Adds the msg error.
	 *
	 * @param message
	 *            the message
	 */
	static public void addMsgWarn(String message)
	{
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
			message, message));
	}
	
    public static void export(String filename, String contents) throws IOException {
    	PrintWriter out = null;
    	FacesContext fc = null;
    	try{
	        String contentType = "application/vnd.ms-excel";
	        fc = FacesContext.getCurrentInstance();
	        filename = fc.getExternalContext().getUserPrincipal().toString() + "-"+ filename;
	        HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
	        response.setCharacterEncoding("UTF-8");
	        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
	        response.setContentType(contentType);
	
	        out = response.getWriter();
	        out.print(contents);
    	}finally{
    		 out.close();
    	     fc.responseComplete();
    	}
    }
    
    public static String filterSpecialCharacters(String in) {
    	if(StringUtils.isEmpty(in)) return "";
    	
    	in = StringEscapeUtils.unescapeHtml(in);
		//remove all html tag
    	in=in.replaceAll("<\\/?\\w+>", " ");
    	in=in.replaceAll("[\\n\\r\\t]", " ");
		//in case there is comma in the notes
		//comma is used as column separator
    	in=in.replaceAll(",",";");
    	in=in.replaceAll("\\s{2,}", " ");
    	
    	return in;
    }


}
