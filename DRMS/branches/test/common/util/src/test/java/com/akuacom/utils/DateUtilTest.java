/**
 * 
 */
package com.akuacom.utils;

import java.util.Date;

import org.junit.Test;

import com.akuacom.utils.lang.DateUtil;

import static org.junit.Assert.*;
import static com.akuacom.utils.lang.DateUtil.*;




/**Covers the tests for {@link DateUtil}.
 * @author roller
 *
 */
public class DateUtilTest {

	public static final long NO_MILLIS = 123456789000l;
	public static long WITH_MILLIS = 123456789123l;
	public static Date NO_MILLIS_DATE = new Date(NO_MILLIS );
	public static Date WITH_MILLIS_DATE = new Date(WITH_MILLIS);
	
	@Test
	public void testStripMillis(){
		
		Date result = stripMillis(WITH_MILLIS_DATE);
		assertEquals(NO_MILLIS,result.getTime());
		assertEquals(NO_MILLIS_DATE.getTime(),result.getTime());
		assertEquals(NO_MILLIS_DATE,result);
		assertTrue(!NO_MILLIS_DATE.equals(WITH_MILLIS_DATE));
		
	}
}
