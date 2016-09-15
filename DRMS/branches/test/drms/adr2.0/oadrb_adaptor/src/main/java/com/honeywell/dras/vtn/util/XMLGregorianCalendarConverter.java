package com.honeywell.dras.vtn.util;

import ietf.params.xml.ns.icalendar_2.Dtstart;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Utility class for converting between XMLGregorianCalendar and java.util.Date
 */
public class XMLGregorianCalendarConverter {  
	
	
	
	public static XMLGregorianCalendar getCal(Date date)
			throws DatatypeConfigurationException {
		XMLGregorianCalendar cal = null;
		if (date != null) {

			SimpleDateFormat SDF = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

			TimeZone tz = TimeZone.getDefault();
			TimeZone utc = TimeZone.getTimeZone("UTC");
			SDF.setTimeZone(utc);
			String milliformat = SDF.format(date);
			String zulu = milliformat.substring(0, 19) + 'Z';
			DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
			cal = datatypeFactory.newXMLGregorianCalendar(zulu);


		}
		return cal;
	}

	
	

    /**
     * Needed to create XMLGregorianCalendar instances
     */
    private static DatatypeFactory df = null;
    static {
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException dce) {
            throw new IllegalStateException(
                "Exception while obtaining DatatypeFactory instance", dce);
        }
    }  

    /**
     * Converts a java.util.Date into an instance of XMLGregorianCalendar
     *
     * @param date Instance of java.util.Date or a null reference
     * @return XMLGregorianCalendar instance whose value is based upon the
     *  value in the date parameter. If the date parameter is null then
     *  this method will simply return null.
     */
    public static XMLGregorianCalendar asXMLGregorianCalendar(java.util.Date date) {
        if (date == null) {
            return null;
        } else {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(date.getTime());
            return df.newXMLGregorianCalendar(gc);
        }
    }

    /**
     * Converts an XMLGregorianCalendar to an instance of java.util.Date
     *
     * @param xgc Instance of XMLGregorianCalendar or a null reference
     * @return java.util.Date instance whose value is based upon the
     *  value in the xgc parameter. If the xgc parameter is null then
     *  this method will simply return null.
     */
    public static java.util.Date asDate(XMLGregorianCalendar xgc) {
        if (xgc == null) {
            return null;
        } else {
            return xgc.toGregorianCalendar().getTime();
        }
    }




	public static XMLGregorianCalendar getXMLGregorianCalendar(long millis, int TIMEZONE_OFFSET) throws DatatypeConfigurationException{
		Date startDate = new Date(millis);
		
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		
		   TimeZone tz = TimeZone.getDefault();
		   TimeZone utc = TimeZone.getTimeZone("UTC");
		   SDF.setTimeZone( utc );
		   String milliformat = SDF.format( startDate );
		   String zulu = milliformat.substring( 0, 19 ) + 'Z';
		   DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
			XMLGregorianCalendar startCal=datatypeFactory.newXMLGregorianCalendar(zulu);
			
			
		

		return startCal;
	}	
	
	public static ietf.params.xml.ns.icalendar_2.Dtstart getDtstart(long millis, int TIMEZONE_OFFSET) throws DatatypeConfigurationException{
		ietf.params.xml.ns.icalendar_2.ObjectFactory of = new ietf.params.xml.ns.icalendar_2.ObjectFactory();
		ietf.params.xml.ns.icalendar_2.Dtstart dtstart = of.createDtstart();
		Date startDate = new Date(millis);
		
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		
		   TimeZone tz = TimeZone.getDefault();
		   TimeZone utc = TimeZone.getTimeZone("UTC");
		   SDF.setTimeZone( utc );
		   String milliformat = SDF.format( startDate );
		   String zulu = milliformat.substring( 0, 19 ) + 'Z';
		   DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
			XMLGregorianCalendar startCal=datatypeFactory.newXMLGregorianCalendar(zulu);
		
//		XMLGregorianCalendar startCal = XMLGregorianCalendarConverter.asXMLGregorianCalendar(startDate);
		dtstart.setDateTime(startCal);
		return dtstart;
	}
	

}