/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.itron.pge.dbp.in.ItronXmlParser.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.itron.pge.dbp.in;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.util.LogUtils;

// TODO make this class a immutable, and return different part of info later.
/**
 * The Class ItronXmlParser.
 */
@SuppressWarnings({"UnusedDeclaration"})
public class ItronXmlParser {
    
    /** The log. */
    private static Logger log = Logger.getLogger(ItronXmlParser.class);

    /** The Constant ProgramName. */
    public static final String ProgramName = "ProgramName";
    
    /** The Constant BlockPrice1. */
    private static final String BlockPrice1 = "BlockPrice1";
    
    /** The Constant ProgramType. */
    private static final String ProgramType = "ProgramType";
    
    /** The Constant BlockEndDate1. */
    private static final String BlockEndDate1 = "BlockEndDate1";
    
    /** The Constant BlockStartDate2. */
    private static final String BlockStartDate2 = "BlockStartDate2";
    
    /** The Constant BlockEndDate2. */
    private static final String BlockEndDate2 = "BlockEndDate2";
    
    /** The Constant BlockStartDate1. */
    private static final String BlockStartDate1 = "BlockStartDate1";
    
    /** The Constant AccountNumber. */
    private static final String AccountNumber = "Account number";
    
    /** The Constant EventStartDate. */
    private static final String EventStartDate = "EventStartDate";
    
    /** The Constant URL. */
    private static final String URL = "URL";
    
    /** The Constant OperatorMessage. */
    private static final String OperatorMessage = "OperatorMessage";
    
    /** The Constant SENDER. */
    private static final String SENDER = "SENDER";
    
    /** The Constant RespondBy. */
    private static final String RespondBy = "RespondBy";
    
    /** The Constant EventEndDate. */
    private static final String EventEndDate = "EventEndDate";
    
    /** The Constant EnergyUnit. */
    private static final String EnergyUnit = "EnergyUnit";
    
    /** The Constant MeterDescription. */
    private static final String MeterDescription = "MeterDescription";
    
    /** The Constant EnergyPriceUnit. */
    private static final String EnergyPriceUnit = "EnergyPriceUnit";
    
    /** The Constant EMAIL_ADDR. */
    private static final String EMAIL_ADDR = "EMAIL_ADDR";
    
    /** The Constant EventCondition. */
    private static final String EventCondition = "EventCondition";
    
    /** The Constant BlockPrice2. */
    private static final String BlockPrice2 = "BlockPrice2";
    
    /** The Constant EventID. */
    private static final String EventID = "EventID";
    
    /** The Constant TimeZone. */
    private static final String TimeZone = "TimeZone";
    
    /** The Constant ServicePointID. */
    private static final String ServicePointID = "ServicePointID";
    
    /** The Constant AccountID. */
    private static final String AccountID = "Account ID";
    
    /** The Constant MeterName. */
    private static final String MeterName = "MeterName";
    
    /** The Constant BODY. */
    private static final String BODY = "BODY";
    
    /** The Constant SettlementType. */
    private static final String SettlementType = "SettlementType";
    
    /** The Constant IsTestEvent. */
    private static final String IsTestEvent = "IsTestEvent";

    /** The event. */
    private DBPEvent event = null;
    
//    private String meterNameValue = null; 
    // TODO: is there a class tht doesn't requrie the redundancy? Set?
    /** The accounts. */
    private Map<String, String> accounts = null;
    
    private Map<String, String> meterNames = null;
    
    /** when it's event issued, all accounts' conditions are the same; when it's acceptence/rejection, there is only one account in the message. so there is only one condition for the message */
    private ItronEventCondition eventCondition = null;
    
    /** The contacts. */
    private Map<String, List<ParticipantContact>> contacts = null;

    /** The program manager. */
    private EventManager _eventManager = EJBFactory.getBean(EventManager.class);

    private ProgramManager programManager1 = EJBFactory.getBean(ProgramManager.class);

    private ParticipantManager partManager = EJBFactory.getBean(ParticipantManager.class); 
    
    private ProgramParticipantManager ppManager = EJBFactory.getBean(ProgramParticipantManager.class);

    /**
     * Gets the accounts.
     * 
     * @return the accounts
     */
    public Collection<String> getAccounts() {
        return accounts.values();
    }

    /**
     * Gets the event.
     * 
     * @return the event
     */
    public DBPEvent getEvent() {
        return event;
    }

    /**
     * Gets the event condition.
     * 
     * @return the event condition
     */
    public ItronEventCondition getEventCondition() {
        return eventCondition;
    }

    /**
     * Gets the contacts.
     * 
     * @return the contacts
     */
    public Map<String, List<ParticipantContact>> getContacts() {
        return contacts;
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
    public Map<String, String>[] parseMessages(Document doc)
    {
        List<Node> messages = getNodes(doc, "Message");
        // TODO: fix the warning. 
        // why can't we use "new HashMap<String, String>[messages.size()]" ?
    	Map<String, String>[] maps = new HashMap[messages.size()];
    	StringBuilder logSB = new StringBuilder();
        for (int i = 0; i < messages.size(); i++) {
            final Node msg = messages.get(i);
            Node arg = getFirstChildWithName(msg, "MessageArg");
            maps[i] = new HashMap<String, String>();
            do
            {
    			try
    			{
	            	if(arg.getNodeName().equals("MessageArg"))
	            	{
	            		Node nameNode = getFirstChildWithName(arg, "Name");
	            		Node valueNode = getFirstChildWithName(arg, "Value");
	            		if(nameNode != null && valueNode != null)
	            		{
	            			String name = nameNode.getFirstChild().getNodeValue().trim();
	            			Node valueValueNode = getFirstNonEmptyChild(valueNode);
	            			if(valueValueNode != null)
	            			{
		            			String value = valueValueNode.getNodeValue().trim();
			 	             	if(name != null && value != null)
				             	{
			 	             		logSB.append(name);
			 	             		logSB.append(":");
			 	             		logSB.append(value);
			 	             		logSB.append("\n");
				             		maps[i].put(name, value);
				             	}
	            			}
	            		}
    				}
            	}
    			catch(NullPointerException e)
    			{
    				log.warn(e);
    			}
	            arg = arg.getNextSibling();
	        } while( arg != null );

            if(contacts == null)
            {
            	contacts = new HashMap<String, List<ParticipantContact>>();
            }
            String accountID = maps[i].get(AccountID);
            List<ParticipantContact> list = contacts.get(accountID);
            if (list == null) {
                list = new ArrayList<ParticipantContact>();
            }

         	// extract every single email/fax/etc
            final Node contactNode = getContactNode(msg);

            final Node firstNameNode = getFirstChildWithName(contactNode, "FirstName");
            final String firstName = getFirstNonEmptyChild(firstNameNode).getNodeValue().trim();
            
            final Node lastNameNode = getFirstChildWithName(contactNode, "LastName");
            Node firstNonEmptyChild = getFirstNonEmptyChild(lastNameNode);
            String lastName = "";
            if (firstNonEmptyChild != null) {
                lastName = firstNonEmptyChild.getNodeValue().trim();
            }
            
            final Node contactMethodNode = getFirstChildWithName(contactNode, "ContactMethod");
            final Node transport = getFirstChildWithName(contactMethodNode, "Transport");
            Node addressNode = getFirstChildWithName(contactMethodNode, "EmailAddress");
            if (addressNode == null) {
                addressNode = getFirstChildWithName(contactMethodNode, "PhoneNum");
            }
            String address = getFirstNonEmptyChild(addressNode).getNodeValue().trim();

            ParticipantContact contact = new ParticipantContact();
            contact.setDescription(firstName + " " + lastName);
            final String type = getFirstNonEmptyChild(transport).getNodeValue().trim();
            if (type.equals("email")) {
                contact.setType(Contact.EMAIL_ADDRESS);
            } else if (type.equals("phone")){
                contact.setType(Contact.PHONE_NUMBER);
            } else if (type.equals("fax")){
                contact.setType(Contact.FAX_NUMBER);
            } else if (type.equals("page")){
                contact.setType(Contact.FAX_NUMBER);
            } else if (type.equals("SMS")) {
            	contact.setType(Contact.SMS);
            }
            contact.setAddress(address);
            contact.setExternal(true);

            list.add(contact);
            contacts.put(accountID, list);
        }
/*
		FireLogEntry logEntry = new FireLogEntry();
		logEntry.setDescription("MessageArgs");
		logEntry.setLongDescr(logSB.toString());			
        log.debug(logEntry);
*/
        return maps;
    }

    /**
     * Gets the contact node.
     * 
     * @param msg the msg
     * 
     * @return the contact node
     */
    private Node getContactNode(Node msg) {
        Node node = msg.getNextSibling();
        while (!"Contact".equals(node.getNodeName())) {
            node = node.getNextSibling();
        }
        return node;
    }

    /**
     * Parses the.
     * 
     * @param doc the doc
     * @param maps the maps
     */
    public void parse(Document doc, Map<String, String>[] maps) {
        validateDocument(doc);

        accounts = new HashMap<String, String>();
        meterNames = new HashMap<String, String>();
        Date now = new Date();
        boolean isEventInit = false;
        List<String> invalid = new ArrayList<String>();
        for(Map<String, String> map: maps)
        {
        	String programName = null;
			try
			{
            	programName = programManager1.
            		getProgramFromUtilityProgramName(map.get(ProgramName)).
            		getProgramName();
			}
			catch (Exception e)
			{
				log.warn("can't find program name", e);
			}
			
            String accountID = map.get(AccountID);
            Participant check = getParticipantByAccountNumber(programName, accountID);
            if (check==null) {
            	if (!invalid.contains(accountID))
            		invalid.add(accountID);
                continue;
            }
            String tempMeterName = map.get(MeterName);
            if (tempMeterName!=null && tempMeterName.length() > 1) {
//            	this.meterNameValue = tempMeterName;
            	meterNames.put(accountID, tempMeterName);
            }
            accounts.put(accountID, accountID);
            accountID = checkAccountsAggregation(programName, check);
            if(accountID != null && !accountID.isEmpty()) {
            	 accounts.put(accountID, accountID);
            }            

            //noinspection ConstantConditions
            if (!isEventInit) {
                eventCondition = ItronEventCondition.valueOf(map.get(EventCondition));

                if( eventCondition == ItronEventCondition.EventIssued ) {
                	event = new DBPEvent();
                    event.setProgramName(programName);
                    event.setEventName(map.get(EventID));
                    event.setIssuedTime(new Date());
                    event.setStartTime(getDate(map, EventStartDate));
                    event.setEndTime(getDate(map, EventEndDate));
                    event.setReceivedTime(now);
                    event.setRespondBy(getDate(map, RespondBy));
                    
                }  else if ( eventCondition == ItronEventCondition.EventCancellation){
                	event = (DBPEvent)
	                		_eventManager.getEvent(programName, map.get(EventID));
                	if(event == null){
                		invalid.add(EventID);
                	}
                }
                else {
                    // it's acceptence or rejection.
                    // and we need to look it up 
					try
					{
	                	event = (DBPEvent)
	                		_eventManager.getEvent(programName, map.get(EventID));
					}
					catch (Exception e)
					{
						log.warn("received accept/reject for unknown event", e);
					}
                }

                isEventInit = true;
            }

        }
        
        if (event!=null && invalid.size()!=0) {
        	String desc="";
        	for (String a:invalid) {
        		contacts.remove(a);
        		desc+=a+",\n";
        	}
        	
	        ProgramValidationMessage msg=new ProgramValidationMessage();
	        msg.setDescription("The following account(s) from the itron xml file but do not exist in DRAS: \n"+desc.substring(0, desc.length()-2));
	        msg.setParameterName("ITORN_INVALID_ACCOUNT");
	        if (event.getWarnings()==null)
	        	event.setWarnings(new ArrayList<ProgramValidationMessage>());
	        event.getWarnings().add(msg);
        }
    }

    private String checkAccountsAggregation(String programName2, Participant check) {
    	ProgramParticipant programParticipant = ppManager.getProgramParticipant(programName2, check.getParticipantName(), check.isClient());
    	if(programParticipant != null) {
    		ProgramParticipant parent = programParticipant.getParent();
    		if(parent != null) {
    			Participant participant = parent.getParticipant();
    			return participant.getAccountNumber();
    		}
    	}   
    	return null;
	}

    private Participant getParticipantByAccountNumber(String programName, String accountNumber) {
        try {
        	List<String> accountIDs=new ArrayList<String>();
        	accountIDs.add(accountNumber);

			List<Participant> participants = partManager.getParticipantsByAccounts(accountIDs);
			for (Participant part : participants) {
					List<String> programNames=partManager.getProgramsForParticipant(part.getParticipantName(), false);
					if (programNames.contains(programName))
						return part;
			}
        } catch (Exception e) {
            log.error(LogUtils.createExceptionLogEntry(programName,
                    LogUtils.CATAGORY_EVENT, e));
        }
        return null;
    }

    
    /**
     * Gets the date.
     * 
     * @param map the map
     * @param arg the arg
     * 
     * @return the date
     */
    private Date getDate(Map map, final String arg) {
        String start = (String) map.get(arg);
        if (start == null) {
            return null;
        }
        start = start.replace('T', ' ');
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start);
        } catch (ParseException e) {
            //
        }
        return date;
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

    // TODO: validate the document's xml INPUT_DATE_FORMAT only
    /**
     * Validate document.
     * 
     * @param doc the doc
     */
    private void validateDocument(Document doc) {}

    /**
     * Gets the nodes.
     * 
     * @param document the document
     * @param type the type
     * 
     * @return the nodes
     */
    private List<Node> getNodes(Document document, String type) {
        final List<Node> results = new ArrayList<Node>();

        final Node jobNode = document.getFirstChild();
        final NodeList list = jobNode.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (type.equals(node.getNodeName())) {
                results.add(node);
            }
        }

        return results;
    }

//	public String getMeterNameValue() {
//		return meterNameValue;
//	}

	public Map<String, String> getMeterNames() {
		return meterNames;
	}
}
