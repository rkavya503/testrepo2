/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.utilopws.client.AkuaUtilityOperatorClient.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.utilopws.client;

// TODO: add SSL after we get the basic event issuing working
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;


import org.openadr.dras.programconstraint.ProgramConstraint;

import org.openadr.dras.utilityprogram.UtilityProgram;
import org.openadr.dras.utilityprogram.ParticipantList;
import org.openadr.dras.utilityprogram.ListOfProgramNames;
import org.openadr.dras.akuautilityoperator.AkuaUtilityOperator;
import org.openadr.dras.akuautilityoperator.AkuaUtilityOperatorService;
import org.openadr.dras.akuadrasclientdata.ListofClientData;


/**
 * The Class AkuaUtilityOperatorClient.
 */
public class AkuaUtilityOperatorClient
{
	
	/**
	 * Instantiates a new akua utility operator client.
	 */
	public AkuaUtilityOperatorClient()
	{
       try
        {
        	// String host = "http://192.168.149.13:8080";
        	String host = "http://localhost:8080";
        	// String host = "http://caiso.openadr.com";

        	// TODO: add SSL after we get the basic event issuing working
         	Authenticator.setDefault( new SimpleAuthenticator() );

        	AkuaUtilityOperator service =
        		new AkuaUtilityOperatorService(new URL(host +
        			"/AkuaUtilityOperatorWS/AkuaUtilityOperatorWS" + "?wsdl"),
        		new QName("http://www.openadr.org/DRAS/AkuaUtilityOperator/",
        		"AkuaUtilityOperatorWSService")).getAkuaUtilityOperatorPort();

            UtilityProgram prog = new UtilityProgram();
            prog.setBiddingConfiguration(new UtilityProgram.BiddingConfiguration());
            prog.setParticipants(new ParticipantList());
            prog.setProgramConstraints(new ProgramConstraint());
            Holder<ListofClientData> holderForListofClientData = new Holder<ListofClientData>();
            Holder<String> holderForRetValue = new Holder<String>();
            service.getDRASClientData(new ParticipantList(), new ListOfProgramNames(), holderForListofClientData, holderForRetValue);
            ListofClientData ret = holderForListofClientData.value;
            holderForListofClientData = new Holder<ListofClientData>();
            holderForRetValue = new Holder<String>();
            service.getDRASClientData(null, null, holderForListofClientData, holderForRetValue);
            ret = holderForListofClientData.value;

            System.out.println("######## End ######################################");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
	}

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args)
	{
		new AkuaUtilityOperatorClient();
    }

	// TODO: add SSL after we get the basic event issuing working
    /**
	 * The Class SimpleAuthenticator.
	 */
	private class SimpleAuthenticator extends Authenticator
	{
		
		/* (non-Javadoc)
		 * @see java.net.Authenticator#getPasswordAuthentication()
		 */
		public PasswordAuthentication getPasswordAuthentication()
		{
			System.out.println("Authenticating");
			return new PasswordAuthentication("u", "v"
				.toCharArray());
		}
	}
}