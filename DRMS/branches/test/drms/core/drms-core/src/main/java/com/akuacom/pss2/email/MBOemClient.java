/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.MBOemClient.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

// Note: To generate docs:
//   - comment out package line (otherwise it spits out relative links,
//     which doesn't work well for a standalone document)
//   - javadoc -version -noindex -nohelp -nonavbar -notree -d html MBOemClient.java

package com.akuacom.pss2.email;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * <p/>
 * This is sample code to demonstrate accessing MessageBlaster's OEM
 * interface from java.  It takes an XML file and a URL to send it
 * to as arguments, and outputs the response from the server.
 * <p/>
 * Note that this code contains both:<ul>
 * <li>a library which can be called from another Java program, and</li>
 * <li>a command-line frontend to the library</li>
 * </ul>
 * <p/>
 * <b>Command line version:</b>
 * <p/>
 * Here are a few examples of using the command-linefrontend:<ul>
 * <li>Send a simple XML request to the default server, in the default domain:
 * <pre>  java MBOemClient Request.xml</pre>
 * <li>Send an XML request with a file attachment:
 * <pre>  java MBOemClient -a Attachment.doc Request.xml</pre>
 * <li>Send a request with two attachments:
 * <pre>  java MBOemClient -a Attachment1.doc -a Attachment2.tiff Request.xml</pre>
 * <li>Send the request with myproxy.com as the proxy server, on port 8000:
 * <pre>  java MBOemClient -p myproxy.com:8000 Request.xml</pre>
 * <li>Send the request using the ACME domain:
 * <pre>  java MBOemClient -d ACME Request.xml</pre>
 * </ul>
 * <p/>
 * For a full list of the command-line options, try:
 * <pre> java MBOemClient </pre>
 * <p/>
 * <b>Library usage:</b>
 * <p/>
 * Using the MBOemClient object to send requests is pretty straightforward:<ol>
 * <li>Create an MBOemClient object.
 * <li>Call the *_Set() and *_Add() methods on the MBOemClient object to setup the request.
 * <li>Call sendRequest() on the MBOemClient object to send the
 * request.  The return value of sendRequest() is the XML response
 * from the server.
 * </ol>
 * <p/>
 * <b>Note on using HTTPS:</b>
 * <p/>
 * There are a couple of key points to look at before adapting this
 * piece of sample code.
 * <p/>
 * In order to establish a connection to a secure http site (https),
 * you need to download and install Sun's secure socket extensions
 * (JSSE) from
 * <a href="http://java.sun.com/products/jsse">
 * http://java.sun.com/products/jsse</a>.
 * <p/>
 * Once installed, point your classpath at jcert.jar, jnet.jar, and
 * jsse.jar (or extract them to a new directory).
 * <p/>
 * You then need to add the following lines of code before attempting
 * to construct an URL for an https site (see 'MBOemClient::sendRequest()'
 * in this file for an example):<tt><pre>
 * System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
 * Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());</pre></tt>
 * <p/>
 * You also must make sure to connect to the correct SSL port of the
 * server. The standard SSL port is 443 which is optional to include,
 * but you may specify a different one if you need to:<tt><pre>
 * URL url = new URL("https://[your server]:7002");</pre></tt>
 * <p/>
 * For complete details about implementing SSL connections in Java,
 * check out the following:
 * <a href="http://www.javaworld.com/javaworld/javatips/jw-javatip96.html">
 * http://www.javaworld.com/javaworld/javatips/jw-javatip96.html</a>
 * <p/>
 * Copyright (c) 2001 EnvoyWorldWide, Inc.
 * 
 * @author Keith Costorf
 * @version 2.0, January 2001
 */

@SuppressWarnings({"StringConcatenationInsideStringBufferAppend"})
public class MBOemClient {

    /** The m hostname. */
    private String mHostname;
    
    /** The m domain. */
    private String mDomain;
    
    /** The m use builtin xml. */
    private boolean mUseBuiltinXml;
    
    /** The m use https. */
    private boolean mUseHttps;
    
    /** The m attachments. */
    private List<String> mAttachments;
    
    /** The m xml string. */
    private String mXmlString;

    /** The OUTPUTPARS e_ outo f_ xml. */
    final int OUTPUTPARSE_OUTOF_XML = 0;
    
    /** The OUTPUTPARS e_ i n_ xml. */
    final int OUTPUTPARSE_IN_XML = 1;

    // Below envoyww.com is old URL and deprecated as
    // https://developer3.envoyww.com/PGE/bin/pwisapi.dll

    /** The m content boundary. */
    private static String mContentBoundary = "AaBbCcDd";
    
    /** The m pwisapi. */
    private static String mPwisapi = "bin/pwisapi.dll";
    
    /** The m default hostname. earlier default set as 'xpress.beta.vrli.com' as per the SGSFI-19 setting default as*/ 
    private static String mDefaultHostname = "api.envoyxpress.com";
    
    /** The m default domain. */
    private static String mDefaultDomain = "PGE";
    
    /** The m default xml string. */
    private static String mDefaultXmlString =
            "<?xml version=\"1.0\"?>\n" +
                    "<!-- Test MBML Script. -->\n" +
                    "<Request>\n" +
                    "  <username>pge_autodbpadmin</username>\n" +
                    "  <Password>pgeadmin</password>\n" +
                    "  <domain>PGE</domain>\n" +
                    "  <oemid>pge_autodbp_oem</oemid>\n" +
                    "  <oempassword>pge$oem</oempassword>\n" +
                    "  <requesttype>commit</requesttype>\n" +
                    "  <Job>\n" +
                    "    <Message>\n" +
                    "      <subject>Third Message</subject>\n" +
                    "      <MessageArg>\n" +
                    "        <name>SENDER</name>\n" +
                    "        <value>Sender Guy</value>\n" +
                    "      </MessageArg>\n" +
                    "      <MessageArg>\n" +
                    "        <name>BODY</name>\n" +
                    "        <value>Third message BODY</value>\n" +
                    "      </MessageArg>\n" +
                    "      <MessageArg>\n" +
                    "        <Name>THEME</Name>\n" +
                    "        <Value>PGE_AUTODBP:PGE_AUTODBP;PGE:;VOICETALENT:LESLIE;</Value>\n" +
                    "      </MessageArg>\n" +
                    "    </Message>\n" +
                    "    <Contact>\n" +
                    "      <FirstName>test</FirstName>\n" +
                    "      <LastName>user</LastName>\n" +
                    "  	   <ContactMethod>\n" +
                    "  		 <Transport>email</Transport>\n" +
                    "  		 <Qualifier>office</Qualifier>\n" +
                    "  		 <Ordinal>0</Ordinal>\n" +
//                    "  		 <Emailaddress>recipient@envoyww.com</Emailaddress>\n" +
//                    "  		 <Emailaddress>recipient@envoyww.com</Emailaddress>\n" +
//                    "  		 <Emailaddress>recipient@envoyww.com</Emailaddress>\n" +
                    "  		 <Emailaddress>dichen@akuacom.com</Emailaddress>\n" +
                    "  	   </ContactMethod>\n" +
                    "  	 </Contact>\n" +
                    "  </Job>\n" +
                    "</Request>\n";

    /**
     * Main constructor.
     * Sets the defaults.
     */
    public MBOemClient() {
        mHostname = mDefaultHostname;
        mDomain = mDefaultDomain;
        mXmlString = "";
        mUseBuiltinXml = false;
        mUseHttps = true;
        mAttachments = new ArrayList<String>();
    }

    /**
     * Outputs a multi-part header.
     * 
     * @return The multi-part header.
     * 
     * @see #multiPartPart
     * @see #multiPartEnd
     */
    private String multiPartBegin() {
        return ("This is a multi-part message in MIME format.\r\n\r\n");
    }

    /**
     * Outputs a name/value pair as a multi-part "part".
     * 
     * @param name  The name of the part
     * @param value The contents of the part
     * 
     * @return The encapsulated "part" string.
     * 
     * @see #multiPartBegin
     * @see #multiPartEnd
     */
    private String multiPartPart(String name, String value) {
        StringBuffer part = new StringBuffer();

        part.append("--" + mContentBoundary + "\r\n");
        part.append("Content-Disposition: form-data;");
        part.append("\tname=\"" + name + "\"\r\n");
        part.append("\r\n");
        part.append(value + "\r\n");
        return part.toString();
    }

    /**
     * Used when a part is a file attachment.
     * Outputs a name/value pair as a multi-part "part", with a filename in the header.
     * 
     * @param name     The name of the part
     * @param filename The name of the file being attached
     * @param value    The contents of the file being attached
     * 
     * @return The encapsulated "part" string.
     * 
     * @see #multiPartBegin
     * @see #multiPartEnd
     * @since 2.0
     */
    private String multiPartPart(String name, String filename, String value) {
        StringBuffer part = new StringBuffer();

        part.append("--" + mContentBoundary + "\r\n");
        part.append("Content-Disposition: form-data;\r\n");
        part.append("\tname=\"" + name + "\"\r\n");
        part.append("\tfilename=\"" + filename + "\"\r\n");
        part.append("\tContent-Length: " + value.length() + "\r\n");
        part.append("\tContent-Type: text/plain\r\n");
        part.append("\r\n");
        part.append(value + "\r\n");
        return part.toString();
    }

    /**
     * Outputs a multi-part trailer.
     * 
     * @return The multi-part trailer.
     * 
     * @see #multiPartBegin
     * @see #multiPartPart
     */
    private String multiPartEnd() {
        return ("--" + mContentBoundary + "--\r\n");
    }

    /**
     * Submits the XML request.
     * Used after the MBOemClient object has all of its settings
     * finalized for the request.
     * 
     * @return The response XML document from the server.
     * 
     * @throws IOException I/O exception
     */
    public String sendRequest() throws IOException {
        StringBuffer responseBuf = new StringBuffer();

        String urlString = "http" + (mUseHttps ? "s" : "") + "://" +
                mHostname + "/" + mDomain + "/" + mPwisapi;

        /* Uncomment this section to allow https.
           * Read documentation above for details.
           */

        // Allow use of 'https'
        if (urlString.startsWith("https")) {
            System.setProperty("javax.net.ssl.trustStore", System.getenv("JAVA_HOME") + "/jre/lib/security/cacerts");
            System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        }

        URL url = new URL(urlString);

        URLConnection conn;
        PrintStream outStream;
        BufferedReader in;
        String inputLine;

        conn = url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" +
                        mContentBoundary);

        outStream = new PrintStream(conn.getOutputStream());
        outStream.print(multiPartBegin());
        outStream.print(multiPartPart("PWFORM", "38"));
        outStream.print(multiPartPart("PWF_MBML", getXmlString()));

        if (mAttachments != null) {
            String aAttachmentNameBase = "PWF_FILEPATH";
            String aAttachmentName = aAttachmentNameBase;

            for (int ii = 0; ii < mAttachments.size(); ii++) {
                if (ii > 0)
                    aAttachmentName = aAttachmentNameBase + "_" + (ii + 1);
                outStream.print(multiPartPart(aAttachmentName,
                        mAttachments.get(ii),
                        loadFileToString(mAttachments.get(ii))));
            }
        }

        outStream.print(multiPartEnd());
        outStream.close();

        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        int parseState = OUTPUTPARSE_OUTOF_XML;
        while ((inputLine = in.readLine()) != null) {
            // Let's parse out everything but the xml response
            String inCheck = inputLine.trim();
            if (inCheck.length() >= 5) {
                inCheck = inCheck.substring(0, 5);
                if (parseState == OUTPUTPARSE_OUTOF_XML) {
                    if (inCheck.equalsIgnoreCase("<?xml")) {
                        parseState = OUTPUTPARSE_IN_XML;
                    }
                } else {
                    // Check for the dashes
                    if (inCheck.equalsIgnoreCase("-----")) {
                        parseState = OUTPUTPARSE_OUTOF_XML;
                    }
                }
            }

            // Only output XML
            if (parseState == OUTPUTPARSE_IN_XML) {
                responseBuf.append(inputLine + "\n");
            }
        }
        in.close();

        return responseBuf.toString();
    }

    /**
     * Load file to string.
     * 
     * @param filename the filename
     * 
     * @return the string
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private String loadFileToString(String filename) throws IOException {
        File f = new File(filename);
        if (!f.exists()) {
            System.err.println("Error: " + filename + " does not exist.\n");
            System.exit(1);
        }

        InputStreamReader isr = new InputStreamReader(new FileInputStream(filename));
        int len = (int) f.length();
        char buf[] = new char[len];
        isr.read(buf, 0, len);
        isr.close();

        return new String(buf);
    }

    /**
     * Sets the hostname for the request.
     * 
     * @param iHostname The hostname for the request.
     * 
     * @since 2.0
     */
    public void setHostname(String iHostname) {
        mHostname = iHostname;
    }

    /**
     * Sets the domain for the request @param iDomain The domain for the request.
     * 
     * @param iDomain The domain name
     * 
     * @since 2.0
     */
    public void setDomain(String iDomain) {
        mDomain = iDomain;
    }

    /**
     * Sets whether to use the builtin XML for the request.
     * 
     * @param iUseBuiltinXml If true, use the builtin XML.
     * 
     * @since 2.0
     */
    public void setUseBuiltinXml(boolean iUseBuiltinXml) {
        mUseBuiltinXml = iUseBuiltinXml;
    }

    /**
     * Sets whether to use https for the request.
     * 
     * @param iUseHttps If true, use https.
     * 
     * @since 2.0
     */
    public void setUseHttps(boolean iUseHttps) {
        mUseHttps = iUseHttps;
    }

    /**
     * Sets the XML string for the request.
     * 
     * @param iXmlString The XML string for the request.
     * 
     * @since 2.0
     */
    public void setXmlString(String iXmlString) {
        mXmlString = iXmlString;
    }

    /**
     * Adds an attachment file to the request.
     * 
     * @param iAttachment The filename to attach.
     * 
     * @since 2.0
     */
    public void addAttachmentFile(String iAttachment) {
        mAttachments.add(iAttachment);
    }

    /**
     * Sets the XML file for the request.
     * 
     * @param iFilename The filename of the request.
     * 
     * @throws IOException io exception
     * 
     * @since 2.0
     */
    public void setXmlFile(String iFilename)
            throws IOException {
        setXmlString(loadFileToString(iFilename));
    }

    /**
     * Gets the hostname.
     * 
     * @return The current hostname for the request
     * 
     * @since 2.0
     */
    public String getHostname() {
        return mHostname;
    }

    /**
     * Gets the domain.
     * 
     * @return The current domain for the request
     * 
     * @since 2.0
     */
    public String getDomain() {
        return mDomain;
    }

    /**
     * Gets the use builtin xml.
     * 
     * @return True if the request is using the builtin XML
     * 
     * @since 2.0
     */
    public boolean getUseBuiltinXml() {
        return mUseBuiltinXml;
    }

    /**
     * Gets the use https.
     * 
     * @return True if the request is using https
     * 
     * @since 2.0
     */
    public boolean getUseHttps() {
        return mUseHttps;
    }

    /**
     * Gets the xml string.
     * 
     * @return The current XML string for the request
     * 
     * @since 2.0
     */
    public String getXmlString() {
        return (mUseBuiltinXml ? mDefaultXmlString : mXmlString);
    }

/**
 * *********************************************************************.
 */
    /* End of library implementation, start of command-line implementation. */
/**
 * ********************************************************************
 */

    private static void usage() {
        System.err.println(
                "Usage: \n" +
                        "  [Attachment options] [Connection options] [Output options] <Request options>\n" +
                        "\n" +
                        "  Attachment options: [-a <attachfile>]\n" +
                        "\t-a <attachfile> Attach this file to the request (multiple -a's are allowed)\n" +
                        "\n" +
                        "  Connection options: [-d <domain>] [-h <hostname>] [-s] [-p <proxyhost>[:<proxyport>]]\n" +
                        "\t-d <domain>     Use a different domain than the default\n" +
                        "\t                (default: " + mDefaultDomain + ")\n" +
                        "\t-h <hostname>   Use this MessageBlaster hostname as a server\n" +
                        "\t                (default: " + mDefaultHostname + ")\n" +
                        "\t-s              Use https instead of http\n" +
                        "\t-p <proxyhost>[:<proxyport>]\n" +
                        "\t                Use the specified host/port as an http proxy for the request\n" +
                        "\n" +
                        "  Output options: [-o <outfile>]\n" +
                        "\t-o <outfile>    Dump all output to the specified file instead of standard out\n" +
                        "\n" +
                        "  Request options: -b | <xml_doc_file>\n" +
                        "\t-b              Use builtin XML default document instaead of xml_doc_filename\n" +
                        "\t<xml_doc_file>  The XML document containing the request\n" +
                        "");
        System.exit(1);
    }

    /**
     * Usage.
     * 
     * @param msg the msg
     */
    private static void usage(String msg) {
        System.err.println("\n*** Error: " + msg + " ***\n");
        usage();
    }

    /**
     * Bail out on file exception.
     * 
     * @param e the e
     * @param file the file
     * @param mode the mode
     */
    private static void bailOutOnFileException(Exception e, String file, String mode) {
        e.printStackTrace();
        System.err.println("\n*** Error: Failed to open file \"" + file +
                "\" for " + mode + ", aborting. ***\n");
        System.exit(1);
    }

    /**
     * Parses the args.
     * 
     * @param iMBOem the i mb oem
     * @param args the args
     * 
     * @return the prints the writer
     */
    private static PrintWriter parseArgs(MBOemClient iMBOem, String args[]) {
        boolean aParsingOptions = true;
        String aXmlFilename = null;
        PrintWriter out = null;

        for (int i = 0; i < args.length; i++) {
            if (!aParsingOptions) {
                usage("Don't understand command-line argument \"" + args[i] + "\".");
            } else {
                if (args[i].charAt(0) == '-') {
                    if (args[i].length() != 2) {
                        usage("Illegal command line option \"" + args[i] + "\".");
                    } else {
                        switch (args[i].charAt(1)) {
                            case 'a':
                                if (++i < args.length)
                                    iMBOem.addAttachmentFile(args[i]);
                                else
                                    usage("You must supply a filename after a \"-a\".");
                                break;
                            case 'b':
                                iMBOem.setUseBuiltinXml(true);
                                break;
                            case 'd':
                                if (++i < args.length)
                                    iMBOem.setDomain(args[i]);
                                else
                                    usage("You must supply a domain after a \"-d\".");
                                break;
                            case 'h':
                                if (++i < args.length)
                                    iMBOem.setHostname(args[i]);
                                else
                                    usage("You must supply a hostname after a \"-h\".");
                                break;
                            case 'o':
                                if (++i < args.length) {
                                    try {
                                        FileOutputStream fos = new FileOutputStream(args[i]);
                                        out = new PrintWriter(fos);
                                    }
                                    catch (FileNotFoundException fnfe) {
                                        bailOutOnFileException(fnfe, args[i], "output stream write");
                                    }
                                    catch (IOException ioe) {
                                        bailOutOnFileException(ioe, args[i], "output stream write");
                                    }
                                } else
                                    usage("You must supply a filename after a \"-o\".");
                                break;
                            case 'p':
                                if (++i < args.length) {
                                    int index, aPort = -1;
                                    String aHost = args[i];

                                    if ((index = aHost.indexOf(':')) > -1) {
                                        aPort = new Integer(aHost.substring(index + 1));
                                        aHost = aHost.substring(0, index);
                                    }

                                    Properties aProp = System.getProperties();
                                    aProp.put("proxySet", "true");
                                    aProp.put("proxyHost", aHost);
                                    if (aPort > -1)
                                        aProp.put("proxyPort", String.valueOf(aPort));
                                    System.setProperties(aProp);
                                } else
                                    usage("You must supply a proxy host[:port] after a \"-p\".");
                                break;
                            case 's':
                                iMBOem.setUseHttps(true);
                                break;
                            default:
                                usage("Illegal command line option \"" + args[i] + "\".");
                                break;
                        }
                    }
                } else {
                    aParsingOptions = false;
                    if (iMBOem.getUseBuiltinXml())
                        usage("You cannot use -d and give an XML filename at the same time.");
                    aXmlFilename = args[i];
                }
            }
        }
        if (!iMBOem.getUseBuiltinXml() && aXmlFilename == null) {
            usage("You must supply an XML document filename or use the \"-d\" option.");
        }
        if (!iMBOem.getUseBuiltinXml()) {
            try {
                iMBOem.setXmlFile(aXmlFilename);
            }
            catch (FileNotFoundException fnfe) {
                bailOutOnFileException(fnfe, aXmlFilename, "xml file read");
            }
            catch (IOException ioe) {
                bailOutOnFileException(ioe, aXmlFilename, "xml file read");
            }
        }

        return out;
    }

    /**
     * Implementation of a command-line frontend to the MBOemClient object.
     * 
     * @param args command line arguments
     */
    public static void main(String args[]) {
        MBOemClient oem = new MBOemClient();
        PrintWriter out = parseArgs(oem, args); // build req from cmd line args
        String responseString = "";

        try {
            responseString = oem.sendRequest();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            System.err.println("\n*** Error: Badly formed URL, aborting. ***");
            System.exit(1);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("\n*** Error: IO Exception, aborting. ***");
            System.exit(1);
		}
		
		if (out != null)
		{
			out.print(responseString);
			out.close();
		}
		else
		{
			System.out.println(responseString);
		}
		
		System.exit(0); // return success
	}
}
