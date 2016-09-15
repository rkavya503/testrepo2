/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.oasis.client.OASISClient.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.oasis.client;

import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import com.akuacom.pss2.oasis.jaxb.OASISReport;
import com.akuacom.pss2.oasis.jaxb.REPORTDATAOASIS;
import com.akuacom.pss2.oasis.jaxb.REPORTHEADEROASIS;
import com.akuacom.pss2.oasis.jaxb.REPORTITEM;
import com.akuacom.pss2.program.rtp.PriceTransition;
import com.akuacom.pss2.util.LogUtils;

/**
 * Class OASISClient.
 * 
 * This is a shared data access class used by more than one OASIS-based
 * RTPPriceConnectors (DAM and HASP, at a minimum)
 * 
 * Callers are responsible for formatting an OASIS request URL Then this class
 * will make the call and parse the returned prices
 */
public class OASISClient {
    private static final Logger log = Logger.getLogger(OASISClient.class);

    private static final String PRICE_REPORT_ITEM = "LMP_PRC";

    @SuppressWarnings("unchecked")
    protected List<PriceTransition> getPrices(String endPoint) {
        OASISReport report = null;
        try {
            StringBuilder xmlData = new StringBuilder();
            URLConnection connection = new URL(endPoint).openConnection();
            ZipInputStream zin = new ZipInputStream(connection.getInputStream());
            if (zin.getNextEntry() != null) {
                Scanner in = new Scanner(zin);
                while (in.hasNextLine())
                    xmlData.append(in.nextLine());
                zin.closeEntry();
            }
            zin.close();

            if (xmlData != null) {
                JAXBContext jc = JAXBContext
                        .newInstance("com.akuacom.pss2.oasis.jaxb");
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                report = ((JAXBElement<OASISReport>) unmarshaller
                        .unmarshal(new StreamSource(new StringReader(xmlData
                                .toString())))).getValue();
            }
        } catch (Exception e) {
            log.warn(LogUtils.createLogEntry("", LogUtils.CATAGORY_EVENT,
                    "error connecting to oasis web service", null));
            log.debug(LogUtils.createExceptionLogEntry("",
                    LogUtils.CATAGORY_EVENT, e));
        }

        if (report == null ||
                report.getMessagePayload() == null ||
                report.getMessagePayload().getRTO() == null ||
                report.getMessagePayload().getRTO().getREPORTITEM() == null ||
                report.getMessagePayload().getRTO().getREPORTITEM().size() == 0) {
            log.warn(LogUtils.createLogEntry("", LogUtils.CATAGORY_EVENT,
                    "OASIS web service returned no prices", null));           
            return null; // there are no prices available for this period
        }

        List<PriceTransition> priceList = new ArrayList<PriceTransition>();
        REPORTITEM priceItem = null;
        REPORTHEADEROASIS headerItem = null;
        int secondsPerInterval = 0;
        for (REPORTITEM item : report.getMessagePayload().getRTO()
                .getREPORTITEM()) {
            if (item.getREPORTDATA().get(0).getDATAITEM().value().equals(PRICE_REPORT_ITEM)) {
                priceItem = item;
                headerItem = priceItem.getREPORTHEADER();
                secondsPerInterval = headerItem.getSECPERINTERVAL().intValue();
                Calendar cal = Calendar.getInstance();
                for (REPORTDATAOASIS data : priceItem.getREPORTDATA()) {
                    PriceTransition priceData = new PriceTransition();

                    XMLGregorianCalendar xoprDate = data.getOPRDATE()
                            .getValue();
                    GregorianCalendar oprDate = xoprDate.toGregorianCalendar();
                    int intervalNum = data.getINTERVALNUM().getValue()
                            .intValue();

                    cal.setTime(oprDate.getTime()); // set the basic day of the
                                                    // date
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    cal.add(Calendar.SECOND, secondsPerInterval
                            * (intervalNum - 1));
                    priceData.setTime(cal.getTime());
                    priceData.setPrice(data.getVALUE().getValue() / 1000.0);
                    priceData.setDurationSeconds(secondsPerInterval);
                    priceData.setLocationID(data.getRESOURCENAME().toString());
                    priceList.add(priceData);
                }
            }
        }
        return priceList;
    }
}
