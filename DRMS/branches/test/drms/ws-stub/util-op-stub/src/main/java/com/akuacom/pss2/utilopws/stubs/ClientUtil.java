/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.utilopws.stubs.ClientUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.utilopws.stubs;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.openadr.dras.eventstate.EventInfoTypeID;
import org.openadr.dras.eventstate.EventState;
import org.openadr.dras.eventstate.GeneralInfoInstance;
import org.openadr.dras.eventstate.GeneralInfoValue;
import org.openadr.dras.eventstate.ListOfEventStates;
import org.openadr.dras.eventstate.ObjectFactory;
import org.openadr.dras.eventstate.OperationState;
import org.openadr.dras.eventstate.SimpleClientEventData;
import org.openadr.dras.eventstate.SmartClientDREventData;

import com.akuacom.pss2.util.DateTool;
import com.akuacom.pss2.util.ModeSlot;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * The Class ClientUtil.
 */
public class ClientUtil
{
    
    /**
     * Parses the event state.
     * 
     * @param participantName the participant name
     * @param eventState the event state
     * 
     * @return the org.openadr.dras.eventstate. event state
     */
    public static org.openadr.dras.eventstate.EventState parseEventState(
    	String participantName, String DrasName, com.akuacom.pss2.util.EventState eventState)
    {
    	org.openadr.dras.eventstate.EventState rvEventState = 
    		new org.openadr.dras.eventstate.EventState();
    	
        rvEventState.setProgramName(eventState.getProgramName());
    	rvEventState.setEventModNumber(eventState.getEventModNumber());
    	rvEventState.setEventIdentifier(eventState.getEventIdentifier());
    	rvEventState.setDrasClientID(eventState.getDrasClientID());
    	rvEventState.setEventStateID(eventState.getEventStateID());
    	rvEventState.setSchemaVersion("1.0");
    	rvEventState.setDrasName(DrasName);
    	rvEventState.setTestEvent(false);
    	rvEventState.setOffLine(false);

    	SimpleClientEventData simpleDRModeData  = new SimpleClientEventData();
    	simpleDRModeData.setEventStatus(eventState.getEventStatus().toString());
    	simpleDRModeData.setOperationModeValue(
    		eventState.getOperationModeValue().toString());
    	simpleDRModeData.setCurrentTime(BigDecimal.valueOf(
    		eventState.getCurrentTimeS()));

    	SimpleClientEventData.OperationModeSchedule operationModeSchedule = 
    		new SimpleClientEventData.OperationModeSchedule();
    	if(eventState.getOperationModeSchedule() != null)
    	{
	    	for(ModeSlot modeSlot: eventState.getOperationModeSchedule())
	        {
	    		OperationState operationState = new OperationState();
	    		operationState.setModeTimeSlot(
	    			BigInteger.valueOf((int)modeSlot.getTimeSlotS()));
	    		operationState.setOperationModeValue(modeSlot.getOperationModeValue().toString());
	    		operationModeSchedule.getModeSlot().add(operationState);
	        }
	    }
    	simpleDRModeData.setOperationModeSchedule(operationModeSchedule);
    	rvEventState.setSimpleDRModeData(simpleDRModeData);

    	SmartClientDREventData smartClientDREventData = new SmartClientDREventData();
    	smartClientDREventData.setNotificationTime(
    		DateTool.converDateToXMLGregorianCalendar(eventState.getNotificationTime()));
    	smartClientDREventData.setStartTime(
    		DateTool.converDateToXMLGregorianCalendar(eventState.getStartTime()));
    	smartClientDREventData.setEndTime(
    		DateTool.converDateToXMLGregorianCalendar(eventState.getEndTime()));
    	smartClientDREventData.setNearTime(DateTool.converDateToXMLGregorianCalendar(eventState.getNearTime()));
    	if(eventState.getEventInfoInstances() != null)
    	{
			for(com.akuacom.pss2.util.EventInfoInstance pss2EventInfoInstance: 
				eventState.getEventInfoInstances())
			{
				org.openadr.dras.eventstate.EventInfoInstance wsEventInfoInstance = 
					new org.openadr.dras.eventstate.EventInfoInstance();
				wsEventInfoInstance.setEventInfoName(pss2EventInfoInstance.getSignalName());
				EventInfoTypeID eventInfoTypeID = new EventInfoTypeID();
				eventInfoTypeID.setValue(pss2EventInfoInstance.getSignalType().toString());
				wsEventInfoInstance.setEventInfoTypeID(eventInfoTypeID);
				for(com.akuacom.pss2.util.EventInfoValue pss2EventInfoValue: 
					pss2EventInfoInstance.getEventInfoValues())
				{
					org.openadr.dras.eventstate.EventInfoValue wsEventInfoValue =
						new org.openadr.dras.eventstate.EventInfoValue();
					wsEventInfoValue.setTimeOffset((long)pss2EventInfoValue.getTimeOffsetS());
					wsEventInfoValue.setValue(BigDecimal.valueOf(pss2EventInfoValue.getValue()));
					wsEventInfoInstance.getEventInfoValues().add(wsEventInfoValue);
				}
		    	smartClientDREventData.getEventInfoInstances().add(wsEventInfoInstance);
			}
    	}
		rvEventState.setDrEventData(smartClientDREventData);
		
		rvEventState.setCustomData(new EventState.CustomData());
		
		//TODO: test
		if (eventState.getLocations()!=null && eventState.getLocations().size()>0) {
		
			List<GeneralInfoInstance> custInfos=new ArrayList<GeneralInfoInstance>();
			GeneralInfoInstance customeInfo=new GeneralInfoInstance();
			
			customeInfo.setInfoName("Location");
			customeInfo.setInfoType("String");
			
			List<GeneralInfoValue> values=new ArrayList<GeneralInfoValue>();
			
			for (String location:eventState.getLocations()) {
				GeneralInfoValue value=new GeneralInfoValue();
				value.setValue(location);
				value.setTimeOffset(0);
				values.add(value);
			}
			customeInfo.getInfoValues().addAll(values);
			custInfos.add(customeInfo);
			
			rvEventState.getCustomData().getGeneralInfoInstances().addAll(custInfos);
		}
		//TODO: end
		
		
	      // hack for forecast
//        if(participantName.equals("ed_akua1"))
//        {
//        	rvSB.append("xmlns:f=\"urn:GridForecast\"\n");
//        }
//        if(participantName.equals("ed_akua1"))
//        {
//        	rvSB.append("<p:customData>\n");
//        	rvSB.append("<f:gridForecast>\n");
//            GregorianCalendar calender = new GregorianCalendar();
//        	for(int i = 0; i < 5; i++)
//        	{
//        		calender.add(GregorianCalendar.DAY_OF_MONTH, 1);
//        		rvSB.append("<f:dailyForecast>\n");
//	        	rvSB.append("<f:date>" + 
//	        		utcDateFormat.format(calender.getTime()) + "</f:date>\n");
//	        	rvSB.append("<f:eventProbability>" + (i == 2 ? 1.0 : 0.0) + 
//	        		"</f:eventProbability>\n");
//	        	rvSB.append("</f:dailyForecast>\n");
//        	}
//        	rvSB.append("</f:gridForecast>\n");
//        	rvSB.append("</p:customData>\n");
//        }        
    	
    	return rvEventState;
     }
    
    /**
     * Event states to xml.
     * 
     * @param eventStates the event states
     * 
     * @return the string
     */
    public static String eventStatesToXML(ListOfEventStates eventStates)
    {
    	try
		{
	    	JAXBContext jc = JAXBContext.newInstance("org.openadr.dras.eventstate");
	        Marshaller marshaller = jc.createMarshaller();
	
	    	JAXBElement<org.openadr.dras.eventstate.ListOfEventStates> 
	    		wsListOfEventStatesElement = (new ObjectFactory()).
	    		createListOfEventState(eventStates);
	        java.io.StringWriter sw = new StringWriter();
			NamespacePrefixMapper npm = new PMapper();
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
				npm);
			marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
				"http://openadr.lbl.gov/src/1/EventState.xsd");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	    	marshaller.marshal(wsListOfEventStatesElement, sw);
	    	return sw.toString();
		}
		catch (PropertyException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JAXBException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
    }
}
