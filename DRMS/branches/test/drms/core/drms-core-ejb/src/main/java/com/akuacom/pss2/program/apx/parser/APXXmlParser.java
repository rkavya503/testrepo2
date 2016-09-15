/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.itron.pge.dbp.in.ItronXmlParser.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.apx.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.akuacom.pss2.core.ProgramValidationMessage;

/**
 * The Class ItronXmlParser.
 */
public class APXXmlParser implements Serializable{
	private static final long serialVersionUID = 874866247843674402L;

	/** The log. */
    private static Logger log = Logger.getLogger(APXXmlParser.class);

    private static final String PROGRAM_NAME = "ProgramName";
    private static final String EVENT_START_DATE = "StartDate";
    private static final String EVENT_END_DATE= "EndDate";
    private static final String LOCATION = "Location";
    private static final String EVENT_ID = "EventID";
    private static final String ACCOUNT_NUMBER = "AccountNumber";

    private String programName;
    private String eventName;
    private Date eventStartTime;
    private Date eventEndTime;
    private List<String> locations=new ArrayList<String>();
    private List<String> accountNumbers=new ArrayList<String>();
    
    List<ProgramValidationMessage> messages=new ArrayList<ProgramValidationMessage>();

    public APXXmlParser(File file){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(file);

			Map<String,Object>[] maps = parseMessages(document);
			parse(maps);

		} catch (Exception e) {
        	String desc="Can not parse message file: "+e.getMessage();
        	ProgramValidationMessage errorMsg=new ProgramValidationMessage("MessageFile", desc);
        	messages.add(errorMsg);
			log.warn(e);
		}
    }
    
    public APXXmlParser(InputStream is){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(is);

			Map<String,Object>[] maps = parseMessages(document);
			parse(maps);

		} catch (Exception e) {
        	String desc="Can not parse message file: "+e.getMessage();
        	ProgramValidationMessage errorMsg=new ProgramValidationMessage("MessageFile", desc);
        	messages.add(errorMsg);
			log.warn(e);
		}
    }
    
    /**
     * Gets the first child with name.
     * 
     * @param node the node
     * @param name the name
     * 
     * @return the first child with name
     */
    private Node getFirstChildWithName(Node node, String name)
    {
    	Node child = node.getFirstChild();
    	while(child != null)
    	{
    		if(child.getNodeName().equals(name))
    		{
    			return child;
    		}
    		child = child.getNextSibling();
    	}
    	return null;
    }

    private List<Node> getChildsWithName(Node node, String name)
    {
    	List<Node> childs=new ArrayList<Node>();
    	Node child = node.getFirstChild();
    	while(child != null)
    	{
    		if(child.getNodeName().equals(name))
    		{
    			childs.add(child);
    		}
    		child = child.getNextSibling();
    	}
    	return childs;
    }

    /**
     * Gets the first non empty child.
     * 
     * @param node the node
     * 
     * @return the first non empty child
     */
    private Node getFirstNonEmptyChild(Node node)
    {
    	Node child = node.getFirstChild();
    	while(child != null)
    	{
    		if(!child.getNodeValue().trim().equals(""))
    		{
    			return child;
    		}
    		child = child.getNextSibling();
    	}
    	return null;
    }
    

    /**
     * Parses the messages.
     * 
     * @param doc the doc
     * 
     * @return the map< string, string>[]
     */
	@SuppressWarnings("unchecked")
	public Map<String, Object>[] parseMessages(Document doc)
    {
		Node jobNode=getJobNode(doc);
        List<Node> messages = getNodes(jobNode, "Message");
        // TODO: fix the warning. 
        // why can't we use "new HashMap<String, String>[messages.size()]" ?
    	Map<String, Object>[] maps = new HashMap[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            final Node msg = messages.get(i);
            Node arg = getFirstChildWithName(msg, "MessageArg");
            maps[i] = new HashMap<String, Object>();
            while(arg != null) {
    			try {
	            	if(arg.getNodeName().equals("MessageArg")) {
	            		Node nameNode = getFirstChildWithName(arg, "Name");
	            		List<Node> valueNodes = getChildsWithName(arg, "Value");
	            		if(nameNode != null && valueNodes.size()>0) {
	            			String name = nameNode.getFirstChild().getNodeValue().trim();
	            			List<String> values=new ArrayList<String>();
	            			for (Node node: valueNodes) {
		            			Node valueValueNode = getFirstNonEmptyChild(node);
		            			if(valueValueNode != null) {
			            			String value = valueValueNode.getNodeValue().trim();
				 	             	if(name != null && value != null) {
				 	             		values.add(value);
					             	}
		            			}
	            			}
	            			
	            			if (values.size()==1)
	            				maps[i].put(name, values.get(0));
	            			else
			             		maps[i].put(name, values);
	            		}
    				}
            	} catch(NullPointerException e) {
    				log.warn(e);
    			}
	            arg = arg.getNextSibling();
	        } 
        }
        
        return maps;
    }

	public void parse(Map<String, Object>[] maps){
        if (maps.length<=0) {
        	String desc="No avaliable event information";
        	ProgramValidationMessage errorMsg=new ProgramValidationMessage("MessageFile", desc);
        	messages.add(errorMsg);
        	return;
        }
        
        validate(maps[0]);
        
    }

    @SuppressWarnings("unchecked")
    protected void validate(Map<String, Object> map){
        String desc = null;
		programName = (String) map.get(PROGRAM_NAME);
		eventName = (String) map.get(EVENT_ID);
		Object location = map.get(LOCATION);
		
		Object accounts=map.get(ACCOUNT_NUMBER);
		if (accounts instanceof List)
			accountNumbers = (List) accounts;
		else if (accounts instanceof String)
			accountNumbers.add((String) accounts);

		if (programName == null) {
			desc = "No available program name";
			addMessage(desc, "ProgramName");
		}
		if (eventName == null) {
			desc = "No available event name";
			addMessage(desc, "EventID");
		}
		
		if (location !=null) {
			if (location instanceof List)
				locations = (List) location;
			else if (location instanceof String && ((String)location).trim().length()!=0)
				locations.add((String) location);
		}
//		if (locations.size() == 0) {
//			desc = "No available location";
//			addMessage(desc, "Location");
//		}
//		if (accountNumbers == null || accountNumbers.size() == 0) {
//			desc = "No available account number";
//			addMessage(desc, "AccountNumber");
//		}

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
        try {
			String startTime = (String) map.get(EVENT_START_DATE);
			if (startTime == null) {
				desc = "No available start date";
				addMessage(desc, "StartDate");
			} else {
				eventStartTime = simpleDateFormat.parse(startTime);
				if (eventStartTime.getTime()<=(new Date()).getTime()) {
					desc = "Start time can not be the past time";
					addMessage(desc, "StartDate");
					eventStartTime=null;
				}
			}
		} catch (ParseException e) {
			desc = "Incorrect start time. Please define event start time with format: MM/dd/yyyy HH:mm z";
			addMessage(desc, "StartDate");
		}

		try {
			String endTime = (String) map.get(EVENT_END_DATE);
			if (endTime == null) {
				desc = "No available end date";
				addMessage(desc, "EndDate");
			}

			if (endTime != null)
				eventEndTime = simpleDateFormat.parse(endTime);
		} catch (ParseException e) {
			desc = "Incorrect end time. Please define event end time with format: MM/dd/yyyy HH:mm z";
			addMessage(desc, "EndDate");
		}
		
		if (eventStartTime!=null && eventEndTime!=null && eventStartTime.getTime()>=eventEndTime.getTime()) {
			desc = "Event start time must be ealier than event end time";
			addMessage(desc, "EventDate");
		}
    }
    
    protected void addMessage(String desc, String paraName){
    	ProgramValidationMessage errorMsg=new ProgramValidationMessage(paraName, desc);
    	messages.add(errorMsg);
    }
    
    /**
     * Gets the time string.
     * 
     * @param now the now
     * 
     * @return the time string
     */
    protected String getTimeString(String now) {
        return now.replaceAll("[/\\\\:]", "-");
    }

    private Node getJobNode(Document document) {
        final Node requestNode = document.getFirstChild();
        final NodeList requestlist = requestNode.getChildNodes();

        for (int i = 0; i < requestlist.getLength(); i++) {
            Node node = requestlist.item(i);
            if (node.getNodeName().equals("Job")) {
                return node;
            }
        }
        return null;
    }
    
    private List<Node> getNodes(Node jobNode, String type) {
        final List<Node> results = new ArrayList<Node>();
        final NodeList list = jobNode.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (type.equals(node.getNodeName())) {
                results.add(node);
            }
        }
        return results;
    }
    
	public String getProgramName() {
		return programName;
	}
	public String getEventName() {
		return eventName;
	}
	public Date getEventStartTime() {
		return eventStartTime;
	}
	public Date getEventEndTime() {
		return eventEndTime;
	}
	public List<String> getLocations() {
		return locations;
	}
	public void setLocations(List<String> locations) {
		this.locations = locations;
	}
	public List<String> getAccountNumbers() {
		return accountNumbers;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public void setEventStartTime(Date eventStartTime) {
		this.eventStartTime = eventStartTime;
	}
	public void setEventEndTime(Date eventEndTime) {
		this.eventEndTime = eventEndTime;
	}
	public void setAccountNumbers(List<String> accountNumbers) {
		this.accountNumbers = accountNumbers;
	}
	public List<ProgramValidationMessage> getMessages() {
		return messages;
	}
	public void setMessages(List<ProgramValidationMessage> messages) {
		this.messages = messages;
	}

	public static void main(String[] args)  {
    	String filename = "D:\\ProjectAccess\\Template\\SCE_AutoDR_Template.xml";

		APXXmlParser parser;
		try {
			parser = new APXXmlParser(new FileInputStream(filename));
			for (ProgramValidationMessage message : parser.getMessages()) {
				System.out.println(message.getParameterName() + ": "
						+ message.getDescription());

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
}
