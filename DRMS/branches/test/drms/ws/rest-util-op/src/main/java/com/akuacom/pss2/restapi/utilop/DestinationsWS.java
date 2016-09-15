/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.restapi.utilop.DestinationsWS.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.restapi.utilop;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.akuacom.pss2.restapi.stubs.Destinations;

/**
 * The Class DestinationsWS.
 */
public class DestinationsWS extends HttpServlet
{
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(DestinationsWS.class);

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
	    PrintWriter out = response.getWriter();
		try
		{
			JAXBContext jc = JAXBContext.newInstance("com.akuacom.pss2.restapi.stubs");
	        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        Destinations destinations = (Destinations)
	            	unmarshaller.unmarshal(request.getInputStream());
	        log.debug("got transaction: " + destinations.getTransactionID());
			out.println("OK");
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
			out.println("ERROR");

		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		doGet(request, response);
	}
}
