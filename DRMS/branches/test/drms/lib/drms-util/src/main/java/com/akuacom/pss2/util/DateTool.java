/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.DateTool.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.util;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by IntelliJ IDEA.
 * User: lin
 * Date: Aug 8, 2008
 * Time: 5:19:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateTool
{
    
    /**
     * Round time.
     * 
     * @param date the date
     * @param field the field
     * @param back the back
     * 
     * @return the date
     */
    static public Date roundTime(Date date, int field, boolean back)
    {
        if(date != null)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(field, 0);
            if(!back)
            {
                cal.add(field-1, 1);   
            }

            date = cal.getTime();
        }
        return date;
    }

    /**
     * Gets the field.
     * 
     * @param date the date
     * @param field the field
     * 
     * @return the field
     */
    static public int getField(Date date, int field)
    {
        if(date != null)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(field);
            
        }
        return -1;
    }

    /**
     * Conver date to xml gregorian calendar.
     * 
     * @param date the date
     * 
     * @return the xML gregorian calendar
     */
    static public XMLGregorianCalendar converDateToXMLGregorianCalendar(Date date)
    {
        try
        {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            return xmlCal;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    /**
     * Conver xml gregorian calendar to date.
     * 
     * @param date the date
     * 
     * @return the date
     */
    static public Date converXMLGregorianCalendarToDate(XMLGregorianCalendar date)
    {
        if(date == null) return null;
        return date.toGregorianCalendar().getTime();
    }
    
    /**
     * Gets an hour earlier.
     * 
     * @return the an hour earlier
     */
    static public Date getAnHourEarlier() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -3);
        return cal.getTime();
    }
}
