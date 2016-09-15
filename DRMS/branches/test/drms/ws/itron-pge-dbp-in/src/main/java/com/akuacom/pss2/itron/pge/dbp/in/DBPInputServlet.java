/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.itron.pge.dbp.in.DBPInputServlet.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.itron.pge.dbp.in;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.contact.ContactEventNotificationType;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.bidding.BiddingProgramManager;
import com.akuacom.pss2.program.dbp.BidState;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.util.Configuration;
import com.kanaeki.firelog.util.FireLogEntry;

/**
 * The Class DBPInputServlet.
 */
public class DBPInputServlet extends HttpServlet {

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(DBPInputServlet.class);

    /*
     * The following are getInstance variables, but only assigned once.
     * can't set to final due to servlet init spec.
     */
    /** The temp folder. */
    private String tempFolder;
    
    /** The program manager. */
    @EJB private EventManager.L _eventManager;
    /** The bidding program manager. */
    @EJB private BiddingProgramManager.L biddingProgramManager;
    
    /** The participant manager. */
    @EJB private ParticipantManager.L participantManager;

    /*
     * These variables should be local variables. They are not thread safe!
     */
    /** The event. */
    private DBPEvent event;
    // TODO: set?
    /** The accounts. */
    private Collection<String> accounts;
    
    /** The event condition. */
    private ItronEventCondition eventCondition;
    private ItronEventType eventType;
    private Map<String, String> meterNames;

    /** The contacts. */
    private Map<String,List<ParticipantContact>> contacts;

    /* (non-Javadoc)
     * @see javax.servlet.GenericServlet#init()
     */
    public void init() throws ServletException {
        super.init();

        // get temp folder location
        Configuration config = new Configuration();
        tempFolder = config.getLogPath();
        log.debug("tempFolder = " + tempFolder);
    }

    /**
     * Create a temp file for incoming request.
     * 
     * @return a file handle
     */
    private File generateTempFile() {
        File result = null;
        try {
            result = File.createTempFile("inter_act_request_", ".xml", new File(tempFolder));
        } catch (IOException e) {
            log.warn("Failed to create temp file for itron request", e);
        }
        return result;
    }

    /**
     * Parses the request.
     * 
     * @param factory the factory
     * @param request the request
     * @param response the response
     * 
     * @return the int
     */
    protected int parseRequest(DocumentBuilderFactory factory,
      HttpServletRequest request, HttpServletResponse response) {
        int errors = 0;
        try {
            // save the raw file
        	File rawFile = writeRawFile(request);
            File xmlFile = parseAndSaveXmlFile(rawFile);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            ItronXmlParser itronXmlParser = new ItronXmlParser();
            Map<String, String>[] maps = itronXmlParser.parseMessages(document);
            // if CPP, process and create event right here.
            // TODO: since cpp selects and update participants as well
            // TODO: there is no difference between cpp and dbp except
            // TODO: the processing bid part and the event details
            // TODO: should consider a clean merge of the 2.
            final String s = maps[0].get(ItronXmlParser.ProgramName);
            if(s.equals(PGECPP.ZONE1_PROGRAM_NAME) || s.startsWith("PDP")) {
//                accounts = itronXmlParser.getAccounts();
//                final List<EventParticipant> list = processAccountIDs();
                errors = PGECPP.getInstance().parseDocument(document, null);
            	eventType = ItronEventType.CPP;
            } else if (s.startsWith("PeakChoice")) {
                itronXmlParser.parse(document, maps);
                event = itronXmlParser.getEvent();
                accounts = itronXmlParser.getAccounts();
                eventType = ItronEventType.PeakChoice;
                eventCondition = itronXmlParser.getEventCondition();
                renameTempXmlFile(xmlFile, event.getEventName());
            } else {
                itronXmlParser.parse(document, maps);
                event = itronXmlParser.getEvent();
                accounts = itronXmlParser.getAccounts();
                eventType = ItronEventType.DBP;
                eventCondition = itronXmlParser.getEventCondition();
                /* contacts = itronXmlParser.getContacts(); */ 
               contacts = null;                
                meterNames = itronXmlParser.getMeterNames();
                if(event != null){
                	renameTempXmlFile(xmlFile, event.getEventName());
                }
            }
        }
        catch (SAXParseException spe) {
            // Error generated by the parser
            log.info("\n** Parsing error"
                    + ", line " + spe.getLineNumber()
                    + ", uri " + spe.getSystemId());
            log.fatal(spe);
            errors++;
        }
        catch (SAXException sxe) {
            log.fatal(sxe);
            errors++;
        }
        catch (ParserConfigurationException pce) {
            // Parser with specified options can't be built
            errors++;
            log.fatal(pce);
        }
        catch (IOException ioe) {
            // I/O error
            log.fatal(ioe);
            errors++;
        }

        //TODO send informative error message back e.g. facility not a valid commDevice
        return errors;
    }

    /**
     * Rename temp xml file.
     * 
     * @param requestXmlFile the request xml file
     * @param eventId the event id
     */
    private void renameTempXmlFile(File requestXmlFile, String eventId) {
        assert(requestXmlFile != null);

        String name = requestXmlFile.getPath();
        String s = extractRandomNumberFromFilename(name);
        String prefix = name.substring(0, name.lastIndexOf('_'));
        File dest = new File(prefix + "_" + eventId + "_" + s + ".xml");
        requestXmlFile.renameTo(dest);

        log.debug("itron request xml renamed to " + dest.getPath());
    }

    /**
     * Extract random number from filename.
     * 
     * @param name the name
     * 
     * @return the string
     */
    protected String extractRandomNumberFromFilename(String name) {
        int start = name.lastIndexOf('_');
        int end = name.lastIndexOf('.');
        return name.substring(start + 1, end);
    }

    /**
     * Write raw file.
     * 
     * @param request the request
     * 
     * @return the file
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private File writeRawFile(HttpServletRequest request)
      throws IOException
    {
    	File rawFile = generateTempFile();
		InputStream in = request.getInputStream();
		BufferedWriter w1 = new BufferedWriter(new FileWriter(rawFile));
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null)
		{
			w1.write(line);
			w1.newLine();
		}
		reader.close();
		w1.close();
		log.debug("ITRON request is written to " + rawFile.getName());
		return rawFile;
    }

    /** The end str. */
    private String endStr = "</Job>";

    /**
     * Parses the and save xml file.
     * 
     * @param itronRequestRawFile the itron request raw file
     * 
     * @return the file
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private File parseAndSaveXmlFile(File itronRequestRawFile)
		throws IOException
	{
		String line;// now read file1, apply hack and save to file2
		BufferedReader fReader =
			new BufferedReader(new FileReader(itronRequestRawFile));
		File requestXmlFile = generateTempFile();
		BufferedWriter out2 =
			new BufferedWriter(new FileWriter(requestXmlFile));
		boolean headerStripped = false;
		while ((line = fReader.readLine()) != null)
		{
			// if we haven't stripped off the header yet
			if(!headerStripped)
			{
				// look for the beginning of the xml
				int begIndex = line.indexOf("<?xml version=");
		        if (begIndex == -1)
		        {
		        	// if it isn't on this line, skip to the next line
		            continue;
		        }
		        // strip off the leading characters
		        line = line.substring(begIndex);
		        headerStripped = true;
			}
			// look for the end
	        int endIndex = line.lastIndexOf(endStr);
	        if (endIndex != -1)
	        {
	        	// if we are there, write to the last xml byte and we're done
	            out2.write(line.substring(0, endIndex + endStr.length()));
	            break;
	        }
	        // otherise, write line and go on
	        out2.write(line);
		}
		fReader.close();
		out2.close();
		log.debug("Done writing hacked input to file " + requestXmlFile.getName());
		return requestXmlFile;
	}

    /**
     * Process.
     * 
     * @return the int
     */
    protected int process() {
        if (eventType == ItronEventType.CPP) {
            return 0;
        }
        
        if(null ==eventCondition){
        	log.error("No Participants found in system");
            return 1;
        }
        switch (eventCondition) {
            case EventIssued:
                return processEventIssued();
            case EventCancellation:
            	return processEventCancellation();
            case DemandBidAcceptance:
            case DemandBidRejection:
                return processBiddingResult();
            default:
                log.error("No valid event condition found");
                return 1;
        }
    }

    // Create an EventCore and send to programManager
    /**
     * Process event issued.
     * 
     * @return the int
     */
    private int processEventIssued() {

        // TODO: what if this fails? separate transaction needed?
        try {
        	if ( meterNames != null && meterNames.size() != 0) {
        		participantManager.updateMeterName(event.getProgramName(), accounts, meterNames);
        	}
        	if ((contacts != null) && !(contacts.isEmpty())){
    			participantManager.createExternalContacts(event.getProgramName(), contacts);
    		}
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return -1;
        }

        List<EventParticipant> participants = processAccountIDs();
        if (participants.size() == 0) {
            log.error("failed to create dbp event: no valid account");
            return -1;
        }
        // now dbp event is ready to be created
        event.setParticipants(participants);

        // TODO: this will throw an exception if there is a
        // validation error. we should probably return the cause
        // to the calling client
        event.setCurrentBidState(BidState.IDLE);
        _eventManager.createEvent(event.getProgramName(), event);

        // TODO: log creation of event

        return 0;
    }

    // process contacts:
    //	- get the contacts for each account
    //  - update the contacts for the corresponding participant in
    //    DataAccess
    /**
     * Process account i ds.
     * 
     * @return the list< event participant>
     */
    private List<EventParticipant> processAccountIDs()
    {
    	// use a map to make sure the list is unique
        Map<String, EventParticipant> participants = new HashMap<String, EventParticipant>();

        boolean notFound = false;
        StringBuilder sb = new StringBuilder();
        for (String accountNumber: accounts)
        {
            try
            {
                Participant p = participantManager.getParticipantByAccount(accountNumber);

                // collect info for event participant
                EventParticipant ep = new EventParticipant();
                ep.setParticipant(p);
                participants.put(p.getUser(), ep);
            }
			catch (Exception e)
			{
				notFound = true;
				sb.append(accountNumber);
				sb.append("\n");
			}
        }
        if(notFound)
        {
			FireLogEntry logEntry = new FireLogEntry();
			logEntry.setDescription("failed to find account numbers");
			logEntry.setLongDescr(sb.toString());			
	        log.error(logEntry);
        }
        // TODO: isn't there a sdk call for this?
        List<EventParticipant> participantsList = new ArrayList<EventParticipant>();
        for(EventParticipant ep: participants.values())
        {
        	participantsList.add(ep);
        }
        return participantsList;
    }

    // first get event for event ID
    //		then get eventParticipant for this event for accountID
    //		check if bids accepted, then set flag ?? on participant
    /**
     * Process bidding result.
     * 
     * @return the int
     */
    private int processBiddingResult()
	{
		int errorCode = 0;
		for (EventParticipant eventParticipant : processAccountIDs())
		{
			try
			{
				boolean accepted =
					eventCondition == ItronEventCondition.DemandBidAcceptance;
				Participant participant =
					participantManager.getParticipantOnly(eventParticipant.getParticipant()
						.getParticipantName(), eventParticipant.getParticipant().isClient());
				biddingProgramManager.setBidAccepted(event.getProgramName(), event
					.getEventName(), participant.getUser(), participant.isClient(), accepted);
			}
			catch (Exception e)
			{
				log.error("process bid confirmation failed", e);
				errorCode = 1;
			}
		}

		return errorCode;
	}

    
    
    /**
     * Process event cancellation.
     * 
     * @return the int
     */
    private int processEventCancellation() {

        // TODO: what if this fails? separate transaction needed?
        try {
        	if(event != null){
        	 _eventManager.removeEvent(event.getProgramName(), event.getEventName());
        	}
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return -1;
        }

        return 0;
    }
    // for dbp override this for custom response format
// private String respFilename = "ResponseToEventNotice.xml";

    /**
     * Send response.
     * 
     * @param errors the errors
     * @param request the request
     * @param response the response
     * 
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected void sendResponse(int errors,
                                HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {
        //open sample response file
        // fill in values for eventID accountID & name, hardcoded?
        // put time stamp and send it out
        PrintWriter out = response.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n"
                + "<Response>\n    <Errors>"
                + errors + "</Errors>\n</Response>");
    }

    /**
     * Find first value.
     * 
     * @param node the node
     * 
     * @return the string
     */
    protected static String findFirstValue(Node node)
    {
        NodeList nodeList = node.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++)
        {
            if(nodeList.item(i).getNodeValue() != null &&
              Character.isLetterOrDigit(nodeList.item(i).getNodeValue().charAt(0)))
            {
                return nodeList.item(i).getNodeValue();
            }
        }
        return null;
    }

    /**
     * Find child node name.
     * 
     * @param node the node
     * @param name the name
     * 
     * @return the node
     */
    protected static Node findChildNodeName(Node node, String name)
    {
        if(node == null)
        {
            return null;
        }
        NodeList nodeList = node.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++)
        {
            if(nodeList.item(i).getNodeName().equals(name))
            {
                return nodeList.item(i);
            }
        }
        return null;
    }

    /**
     * Find child node value.
     * 
     * @param node the node
     * @param name the name
     * 
     * @return the node
     */
    protected static Node findChildNodeValue(Node node, String name)
    {
        if(node == null)
        {
            return null;
        }
        NodeList nodeList = node.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++)
        {
            if(nodeList.item(i).getNodeValue().equals(name))
            {
                return nodeList.item(i);
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	log.debug("***** START ProgramInputServlet *****");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        int errors = parseRequest(factory, request, response);

        if(errors == 0)
        {
                 errors = process();
        }
        sendResponse(errors, request, response);
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
