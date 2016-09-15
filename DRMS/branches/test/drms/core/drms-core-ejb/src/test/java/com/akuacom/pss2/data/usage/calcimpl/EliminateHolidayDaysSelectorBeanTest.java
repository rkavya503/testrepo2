//package com.akuacom.pss2.data.usage.calcimpl;
//
//import static org.junit.Assert.assertEquals;
//
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.HashSet;
//import java.util.Set;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.akuacom.pss2.data.DateRange;
//
//public class EliminateHolidayDaysSelectorBeanTest {
//	private Set<Date> holidays; 
//	private static DateFormat forrmat = new SimpleDateFormat("yyyy-MM-dd");
//	
//	
//	@Before
//	public void setup(){
//		holidays = new HashSet<Date>();
//		
//		try {
//			//January 1, 2010 (friday): New Year's Day [Jan. 1 every year]
//			holidays.add(forrmat.parse("2010-01-01"));
//			//January 18, 2010 (monday): Martin Luther King Day [3rd monday in Jan]
//			holidays.add(forrmat.parse("2010-01-18"));
//			//February 15, 2010 (monday): Presidents Day (observed) [3rd monday in Feb]
//			holidays.add(forrmat.parse("2010-02-15"));
//			//May 31, 2010 (monday): Memorial Day (observed) [last monday in May]
//			holidays.add(forrmat.parse("2010-05-31"));
//			//July 4, 2010 (sunday): Independence Day [July 4th every year]
//			holidays.add(forrmat.parse("2010-07-4"));
//			//July 5, 2010 (monday): federal employees extra day off for July 4th
//			holidays.add(forrmat.parse("2010-07-5"));
//			//September 6, 2010 (monday): Labor Day [1st monday in Sept]
//			holidays.add(forrmat.parse("2010-09-6"));
//			//October 11, 2010 (monday): Columbus Day (observed) [2nd monday in Oct]
//			holidays.add(forrmat.parse("2010-10-11"));
//			//November 11, 2010 (thursday): Veterans' Day [Nov. 11 every year]
//			holidays.add(forrmat.parse("2010-11-11"));
//			//November 25, 2010 (thursday): Thanksgiving Day [4th thursday in Nov]
//			holidays.add(forrmat.parse("2010-11-25"));
//			//December 24, 2010 (friday): federal employees extra day off for Christmas
//			holidays.add(forrmat.parse("2010-12-24"));
//			//December 25, 2010 (saturday): Christmas Day [Dec. 25 every year]
//			holidays.add(forrmat.parse("2010-12-25"));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
//    @Test
//    public void testGetDateRange() {
//    	DateRange dr = null;
//        Date date = null;
//		try {
//			date = forrmat.parse("2010-12-30");
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		
//        Calendar endDate = new GregorianCalendar();
//        dr = new EliminateHolidayDaysSelectorBean().getDateRange(date, holidays, endDate);
//        assertEquals("2010-12-15",forrmat.format(dr.getStartTime()));
//        assertEquals("2010-12-29",forrmat.format(dr.getEndTime()));
//        assertEquals(5,dr.getExcludedDays().size());
//
//    }
//}
