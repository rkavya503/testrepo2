package com.honeywell.dras.vtn.util;

import org.joda.time.Chronology;
import org.joda.time.Period;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public class DurationUtil {

	
	public static long parse(String input){

		PeriodFormatter fmt = ISOPeriodFormat.standard();
	    Period period = fmt.parsePeriod(input);
	    long millis = period.toStandardDuration().getMillis();
	    return millis;

	}
	
	
	public static String convert(long millis){
		Chronology chrono = ISOChronology.getInstance();
		Period period = new Period(millis, chrono);
		String s = period.toString();
		return s;
	}

}
