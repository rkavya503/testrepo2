/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.apx.sdg.cbp.CBPInputServlet.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.apx.sdg.cbp;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.util.Configuration;
import com.akuacom.pss2.util.LogUtils;
import com.kanaeki.firelog.util.FireLogEntry;

/**
 * The Class CBPInputServlet.
 */
public class CBPInputServlet extends HttpServlet {

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(CBPInputServlet.class);

    /** The Constant NO_ERROR. */
    private static final int NO_ERROR = 0;

    /** The Constant BAD_PROGRAM. */
    private static final int BAD_PROGRAM = 1;

    /** The Constant UNKNOWN_EXCEPTION. */
    private static final int UNKNOWN_EXCEPTION = 2;

    /** The Constant MESSAGE_PARSING_ERROR. */
    private static final String MESSAGE_PARSING_ERROR = "Message parsing error";

    /** The Constant PROGRAM_IN_PAST_ERROR. */
    private static final String PROGRAM_IN_PAST_ERROR = "Cant schedule program in the past";

    /** The Constant INVALID_PROGRAM_NAME_ERROR. */
    private static final String INVALID_PROGRAM_NAME_ERROR = "Invalid Program Name";

    /** The Constant INTERNAL_ERROR. */
    private static final String INTERNAL_ERROR = "Internal error";

    /** The program manager. */
    private EventManager eventManager = EJBFactory.getBean(EventManager.class);

    /** The temp folder. */
    private String tempFolder;

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

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        File rawFile = writeRawFile(request);

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

        String programName = null;
        Date startDate = null;
        Date endDate = null;
        String startDateStr = null;
        String endDateStr = null;
        String eventID = null;
        StringBuilder logSB = new StringBuilder();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(rawFile);
            NodeList nodeList = document.getElementsByTagName("MessageArg");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nameNode = findChildNodeName(nodeList.item(i), "Name");
                if (nameNode != null) {
                    if (findChildNodeValue(nameNode, "ProgramName") != null) {
                        Node valueNode =
                                findChildNodeName(nodeList.item(i), "Value");
                        if (valueNode != null) {
                            programName = findFirstValue(valueNode);
                            logSB.append("ProgramName: " + programName + "\n");
                        } else {
                            logSB.append("ProgramName: null\n");
                        }
                    } else if (findChildNodeValue(nameNode, "StartDate") != null) {
                        Node valueNode =
                                findChildNodeName(nodeList.item(i), "Value");
                        if (valueNode != null) {
                            startDateStr = findFirstValue(valueNode);
                            if (startDateStr != null)
                                startDate =
                                        new SimpleDateFormat("MM/dd/yyyy HH:mm z").parse(
                                                startDateStr);
                            logSB.append("StartDate: " + startDate + "\n");
                        } else {
                            logSB.append("StartDate: null\n");
                        }
                    } else if (findChildNodeValue(nameNode, "EndDate") != null) {
                        Node valueNode =
                                findChildNodeName(nodeList.item(i), "Value");
                        if (valueNode != null) {
                            endDateStr = findFirstValue(valueNode);
                            if (endDateStr != null)
                                endDate =
                                        new SimpleDateFormat("MM/dd/yyyy HH:mm z").parse(
                                                endDateStr);
                            logSB.append("EndDate: " + endDate + "\n");
                        } else {
                            logSB.append("EndDate: null\n");
                        }
                    } else if (findChildNodeValue(nameNode, "EventID") != null) {
                        Node valueNode =
                                findChildNodeName(nodeList.item(i), "Value");
                        if (valueNode != null) {
                            eventID = findFirstValue(valueNode);
                            logSB.append("eventID: " + eventID + "\n");
                        } else {
                            logSB.append("eventID: null\n");
                        }
                    }
                }
            }
            FireLogEntry logEntry = new FireLogEntry();
            logEntry.setDescription("MessageArgs");
            logEntry.setLongDescr(logSB.toString());
            logEntry.setCategory(LogUtils.CATAGORY_EVENT);
            log.debug(logEntry);
        }
        catch (SAXParseException spe) {
            log.log(Level.ERROR, "SAXParseException", spe);
            outputResponse(response, false, MESSAGE_PARSING_ERROR);
            return;
        }
        catch (SAXException sxe) {
            log.log(Level.ERROR, "SAXException", sxe);
            outputResponse(response, false, MESSAGE_PARSING_ERROR);
            return;
        }
        catch (ParserConfigurationException pce) {
            log.log(Level.ERROR, "ParserConfigurationException", pce);
            outputResponse(response, true, MESSAGE_PARSING_ERROR);
            return;
        }
        catch (IOException ioe) {
            log.log(Level.ERROR, "IOException", ioe);
            outputResponse(response, true, MESSAGE_PARSING_ERROR);
            return;
        }
        catch (ParseException pe) {
            // Date parse exception
            log.log(Level.ERROR, "ParseException", pe);
            outputResponse(response, true, MESSAGE_PARSING_ERROR);
            return;
        }

        if (programName == null) {
            outputResponse(response, true, MESSAGE_PARSING_ERROR);
            return;
        }
        if (startDate == null) {
            outputResponse(response, true, MESSAGE_PARSING_ERROR);
            return;
        }
        if (endDate == null) {
            outputResponse(response, true, MESSAGE_PARSING_ERROR);
            return;
        }

        //check if event time is in past, if yes, don't accept
        Date now = new Date();
        if (startDate.before(now)) {
            log.error("Cannot create event in past!");
            outputResponse(response, true, PROGRAM_IN_PAST_ERROR);
            return;
        }

        int error = process(programName, startDate, endDate, startDateStr,
                endDateStr, eventID);
        switch (error) {
            case BAD_PROGRAM:
                outputResponse(response, true, INVALID_PROGRAM_NAME_ERROR);
                break;
            case UNKNOWN_EXCEPTION:
                // TODO: parse some of these errors
                // we used to have: "Program already exists for this date"
                // i think this should be "Event" not "Program"
                outputResponse(response, true, INTERNAL_ERROR);
                break;
            default:
                outputResponse(response, false, "");
                break;
        }

    }

    /**
     * Output response.
     *
     * @param response the response
     * @param error the error
     * @param description the description
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void outputResponse(HttpServletResponse response,
                                boolean error, String description) throws IOException {
        int errors = 0;
        if (error) {
            errors = 1;
        }
        PrintWriter out = response.getWriter();
        String string = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n<Response>\n" +
                "    <Errors>" + errors + "</Errors>\n";
        if (error) {
            string += "    <ErrorDescription>" + description + "</ErrorDescription>\n";
        }
        string += "</Response>\n";
        out.println(string);
    }


    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Process.
     *
     * @param programName the program name
     * @param start the start
     * @param end the end
     * @param startDateStr the start date str
     * @param endDateStr the end date str
     * @param eventID the event id
     *
     * @return the int
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public int process(String programName, Date start, Date end,
                       String startDateStr, String endDateStr, String eventID) {
        // Create an EventCore and send to programManager
        try {
            Event event = new Event();

            event.setProgramName(programName);

            Date now = new Date();
            event.setEventName(eventID);
            event.setIssuedTime(new Date(now.getTime() + 60000));
            event.setStartTime(start);
            event.setEndTime(end);
            event.setReceivedTime(now);
            eventManager.createEvent(programName, event);
        }
        catch (Exception e) {
            log.warn("exception creating event", e);

            // TODO: this should be more robust. probably should be a special
            // exception type in indicate program not found
            if (e.getMessage().contains("can't find bean for program")) {
                return BAD_PROGRAM;
            } else {
                return UNKNOWN_EXCEPTION;
            }
        }
        return NO_ERROR;
    }

    /**
     * Find first value.
     *
     * @param node the node
     *
     * @return the string
     */
    protected static String findFirstValue(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeValue() != null &&
                    Character.isLetterOrDigit(nodeList.item(i).getNodeValue().charAt(0))) {
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
    protected static Node findChildNodeName(Node node, String name) {
        if (node == null) {
            return null;
        }
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeName().equals(name)) {
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
    protected static Node findChildNodeValue(Node node, String name) {
        if (node == null) {
            return null;
        }
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeValue().equals(name)) {
                return nodeList.item(i);
            }
        }
        return null;
    }//for dbp override this for custom response format

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
		log.debug("APEX request is written to " + rawFile.getName());
		return rawFile;
    }


}
