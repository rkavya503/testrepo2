/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.utilopws.client.Populate.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.utilopws.client;

// TODO: add SSL after we get the basic event issuing working

import java.math.BigInteger;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;

import org.openadr.dras.akuautilityoperator.AkuaUtilityOperator;
import org.openadr.dras.akuautilityoperator.AkuaUtilityOperatorService;
import org.openadr.dras.akuautilityprogram.AkuaSignalList;
import org.openadr.dras.akuautilityprogram.AkuaUtilityProgram;
import org.openadr.dras.programconstraint.ProgramConstraint;

/**
 * The Class Populate.
 */
public class Populate
{
	
	/**
	 * Instantiates a new populate.
	 */
	public Populate()
	{
       try 
        {
        	String endPoint = "http://localhost:8080/AkuaUtilityOperatorWS/AkuaUtilityOperatorWS";
        	//String endPoint = "http://pge2.openadr.com/UtilityOperatorWS/UtilityOperatorWS";

        	// TODO: add SSL after we get the basic event issuing working
         	Authenticator.setDefault( new SimpleAuthenticator() );

        	AkuaUtilityOperator service = 
        		new AkuaUtilityOperatorService(new URL(endPoint + "?wsdl"),
        		new QName("http://www.openadr.org/DRAS/AkuaUtilityOperator/", 
        		"AkuaUtilityOperatorWSService")).getAkuaUtilityOperatorPort();

        	for(int i = 20; i < 30; i++)
        	{
		        AkuaUtilityProgram prog = new AkuaUtilityProgram();
		        String programName = "Populate Program " + i;
		        prog.setValidatorClass("com.akuacom.pss2.program.cpp.CPPValidator");
		        prog.setUtiltyProgramName(programName);
		        prog.setClassName("com.akuacom.pss2.program.demo.DemoProgramEJB");
		        prog.setUiScheduleEventString("DemoSchedulePage");
		        prog.setNotificationParam1("");
		        prog.setTestProgram(false);
		        prog.setRepeatingEvents(false);
		        prog.setRemoteProgram(false);
		        // AkuaContactEmailList = new AkuaContactEmailList();
		        prog.setContactEmails(null);
		        prog.setMinIssueToStartM(0);
		        prog.setMustIssueBDBE(false);
		        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
		        GregorianCalendar calMax = new GregorianCalendar();
		        calMax.set(GregorianCalendar.HOUR_OF_DAY, 23);
		        calMax.set(GregorianCalendar.MINUTE, 59);
		        GregorianCalendar calMin = new GregorianCalendar();
		        calMin.set(GregorianCalendar.HOUR_OF_DAY, 23);
		        calMin.set(GregorianCalendar.MINUTE, 59);
		        prog.setMaxIssueTime(
		        	datatypeFactory.newXMLGregorianCalendar(calMax));
		        prog.setMinStartTime(
		        	datatypeFactory.newXMLGregorianCalendar(calMin));
		        prog.setMaxStartTime(
		        	datatypeFactory.newXMLGregorianCalendar(calMax));
		        prog.setMinEndTime(
		        	datatypeFactory.newXMLGregorianCalendar(calMin));
		        prog.setMaxEndTime(
		        	datatypeFactory.newXMLGregorianCalendar(calMax));
		        prog.setPendingTimeDBE(null);
		        GregorianCalendar cal = new GregorianCalendar();
		        cal.set(GregorianCalendar.HOUR_OF_DAY, 21);
		        prog.setPendingTimeDBE(
		        	datatypeFactory.newXMLGregorianCalendar(cal));
		        prog.setManualCreatable(true);
		        AkuaSignalList akuaSignalList = new AkuaSignalList();
		        akuaSignalList.getSignal().add("mode");
		        akuaSignalList.getSignal().add("pending");
		        prog.setSignals(akuaSignalList);
		        prog.setModeTransitions(null);
		        prog.setBiddingConfiguration(null);
 		        
		        prog.setName(programName);
		        prog.setPriority(BigInteger.valueOf(i));

		        ProgramConstraint constraint = new ProgramConstraint();
		        constraint.setProgramName(programName);
		        constraint.setConstraintID(programName);		        
		        prog.setProgramConstraints(constraint);
		
		        String ret = service.createProgram(prog);
		        if(ret.equals("SUCCESS"))
		        {
		        	System.out.println("create of " + programName + " succeeded");		        	
		        }
		        else
		        {
		        	System.out.println("create of " + programName + " failed with: " + ret);
		        }
        	}

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
		new Populate();
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
