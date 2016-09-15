/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.utilopws.AkuaWSValidator.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.utilopws;

//$Id: AkuaWSValidator.java,v 1.3 2010-02-05 20:26:44 chris Exp $

import org.jboss.ws.extensions.validation.StrictlyValidErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.apache.log4j.Logger;
import com.kanaeki.firelog.util.FireLogEntry;
import com.akuacom.pss2.util.LogUtils;

/**
 * An error handler that throws a @{SAXException} on error and fatal error.
 */
public class AkuaWSValidator extends StrictlyValidErrorHandler
{
   // provide logging
   /** The Constant log. */
   private static final Logger log = Logger.getLogger(AkuaWSValidator.class);
   
   /* (non-Javadoc)
    * @see org.jboss.ws.extensions.validation.StrictlyValidErrorHandler#error(org.xml.sax.SAXParseException)
    */
   public void error(SAXParseException ex) throws SAXException
   {
      FireLogEntry logEntry = LogUtils.createLogEntry();
      logEntry.setLongDescr(ex.getMessage());
      log.error(logEntry);
      super.error(ex);
   }

   /* (non-Javadoc)
    * @see org.jboss.ws.extensions.validation.StrictlyValidErrorHandler#fatalError(org.xml.sax.SAXParseException)
    */
   public void fatalError(SAXParseException ex) throws SAXException
   {
      FireLogEntry logEntry = LogUtils.createLogEntry();
      logEntry.setLongDescr(ex.getMessage());
      log.fatal(logEntry);
      super.fatalError(ex);
   }

   /* (non-Javadoc)
    * @see org.jboss.ws.extensions.validation.StrictlyValidErrorHandler#warning(org.xml.sax.SAXParseException)
    */
   public void warning(SAXParseException ex) throws SAXException
   {
      FireLogEntry logEntry = LogUtils.createLogEntry();
      logEntry.setLongDescr(ex.getMessage());
      log.warn(logEntry);
      super.warning(ex);
   }
}
