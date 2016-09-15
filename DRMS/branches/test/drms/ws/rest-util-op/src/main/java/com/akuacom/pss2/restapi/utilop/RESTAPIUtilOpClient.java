/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.restapi.utilop.RESTAPIUtilOpClient.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.restapi.utilop;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import com.akuacom.pss2.restapi.stubs.Destinations;
import com.akuacom.pss2.restapi.stubs.ObjectFactory;
import com.akuacom.pss2.restapi.stubs.Destinations.Participants;

/**
 * The Class RESTAPIUtilOpClient.
 */
public class RESTAPIUtilOpClient 
{
	
    /**
     * Instantiates a new rESTAPI util op client.
     */
    public RESTAPIUtilOpClient()
    {
		try
		{
			String endPoint = "http://localhost:8080/RESTAPIUtilOpWS/DestinationsWS";
			
			URLConnection connection = new URL(endPoint).openConnection();
			connection.setDoOutput(true);
	        String plain = "a:b";
	        String enocoded = new sun.misc.BASE64Encoder().encode(plain.getBytes());
	        connection.setRequestProperty("Authorization", "Basic " + enocoded);
			
			JAXBContext jc = JAXBContext.newInstance("com.akuacom.pss2.restapi.stubs");
	        Marshaller marshaller = jc.createMarshaller();
	
	        Destinations destinations = new Destinations();
	        destinations.setTransactionID("1234");
	        destinations.setParticipants(
	        	new ObjectFactory().createDestinationsParticipants(
	        	new Destinations.Participants()));
	        destinations.getParticipants().getValue().getParticipantID().add("hello");
	        java.io.StringWriter sw = new StringWriter();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	    	marshaller.marshal(destinations, sw);

            System.out.println(sw.getBuffer().toString());

            connection.getOutputStream().write(sw.getBuffer().toString().getBytes());
	    	
            Scanner in = new Scanner(connection.getInputStream());
            while (in.hasNextLine())
            {
                System.out.println(in.nextLine());
            }            
            in.close();           
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }
    
    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) 
    {
       new RESTAPIUtilOpClient();
    }    
}
