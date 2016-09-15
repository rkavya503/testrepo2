package com.honeywell.drms.log.reader.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import com.honeywell.drms.log.reader.LogUtils;

public class UtilsTest  extends TestCase{
	
	@Test
	public void testValidLogFileName(){
		String str= "server.log.2011-03-04";
		assertTrue(LogUtils.isValidLogFileName(str));
		str="server.log";
		assertTrue(LogUtils.isValidLogFileName(str));
	}
	
	@Test
	public void testGetLogDate(){
		String str= "server.log.2011-03-04";
		Date date = LogUtils.getLogFileDate(str);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		assertEquals(dateFormatter.format(date),"2011-03-04");
		
		str= "/c:/test/server.log.2011-03-04";
		date = LogUtils.getLogFileDate(str);
		assertEquals(dateFormatter.format(date),"2011-03-04");
		
		str="server.log";
		date = LogUtils.getLogFileDate(str);
		assertEquals(dateFormatter.format(date),dateFormatter.format(new Date()));
	}

}

