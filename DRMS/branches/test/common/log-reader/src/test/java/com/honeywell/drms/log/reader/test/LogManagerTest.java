package com.honeywell.drms.log.reader.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Priority;
import org.junit.Ignore;
import org.junit.Test;

import com.honeywell.drms.log.reader.LogFile;
import com.honeywell.drms.log.reader.LogManager;
import com.honeywell.drms.log.reader.LogSearchCriteria;
import com.kanaeki.firelog.util.FireLogEntry;

public class LogManagerTest{
	
	boolean consoleOuput = false;
	
	protected SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	protected static SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected static SimpleDateFormat dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss','SSS");
	
	@Test
	public void testGetLogFiles(){
		URL folder =LogScannerTest.class.getResource("");
		LogManager manager = new LogManager(folder.getPath());
		manager.setLineTerminateSize(2);
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date start;
		try {
			start = dateFormatter.parse("2011-11-02");
			Date end = dateFormatter.parse("2011-11-03");
			//manager.scanLogFiles(start, end);
			String[] files = manager.getTargetLogFiles(start, end);
			assertEquals(2,files.length);
			
			files = manager.getTargetLogFiles(start, start);
			assertEquals(1,files.length);
			assertEquals(files[0],"server.log.2011-11-02");
			
		}
		catch (ParseException e) {}
	}
	
	@Test
	public void testScanLogFiles(){
		URL folder =LogScannerTest.class.getResource("");
		LogManager manager = new LogManager(folder.getPath());
		manager.setLineTerminateSize(2);
		manager.setBlockSize(128*1024);
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date start=null,end = null;
		try{
			start = dateFormatter.parse("2011-11-02");
			end = dateFormatter.parse("2011-11-03");
		}catch(Exception e){}
		
		manager.scanLogFiles(start, end);
		//server.log.2011-11-02: 3 blocks
		//server.log.2011-11-03: 5 blocks
		
		Map<String, LogFile> scannedLogFiles= manager.getScanedLogFiles();
		assertEquals(2,scannedLogFiles.size());
		for(String key:scannedLogFiles.keySet()){
			if(key.endsWith("server.log.2011-11-02")){
				assertEquals(2,scannedLogFiles.get(key).getLogBlocks().size());
			}else if(key.endsWith("server.log.2011-11-03")){
				assertEquals(3,scannedLogFiles.get(key).getLogBlocks().size());
			}
		}
	}
	
	@Test
	public void testDoQuery(){
		URL folder =LogScannerTest.class.getResource("");
		LogManager manager = new LogManager(folder.getPath());
		manager.setLineTerminateSize(2);
		
		Date d1 = str2date("2011-11-04 13:20:00");
		Date d2 = str2date("2011-11-04 13:21:00");
		
		LogSearchCriteria sc = new LogSearchCriteria(d1,d2,Priority.INFO_INT,"","","","");
		
		List<FireLogEntry> entries = manager.doQuery(sc);
		outputEntries(entries);
		
		assertEquals(12,entries.size());
	}
	
	
	@Test
	public void testDoQuery1(){
		URL folder =LogScannerTest.class.getResource("");
		LogManager manager = new LogManager(folder.getPath());
		manager.setLineTerminateSize(2);
		
		Date d1 = str2date("2011-11-02 00:00:00");
		Date d2 = str2date("2011-11-04 23:59:59");
		
		LogSearchCriteria sc = new LogSearchCriteria(d1,d2,Priority.ERROR_INT,"","","","javax.xml.ws.WebServiceException");
		
		List<FireLogEntry> entries = manager.doQuery(sc);
		assertEquals(1,entries.size());
		
		if(consoleOuput){
			for(FireLogEntry entry:entries ){
				System.out.println(entry);
			}
		}
	}
	
	@Test
	public void testDoQuery2(){
		URL folder =LogScannerTest.class.getResource("");
		LogManager manager = new LogManager(folder.getPath());
		manager.setLineTerminateSize(2);
		
		Date d1 = str2date("2011-11-15 00:00:00");
		Date d2 = str2date("2011-11-15 19:00:00");
		
		LogSearchCriteria sc = new LogSearchCriteria(d1,d2,Priority.INFO_INT,"","","","java.lang.ClassCastException");
		
		List<FireLogEntry> entries = manager.doQuery(sc);
		assertEquals(2,entries.size());
		FireLogEntry entry1 = entries.get(1);
		assertEquals("2011-11-15 16:11:30,129",dateFormatter2.format(entry1.getLogDate()));
		assertEquals(Priority.ERROR_INT,entry1.getLogLevel());
		assertEquals("(HDScanner)  StandardWrapper.Throwable",entry1.getDescription());
		assertTrue(entry1.getLongDescr().contains("java.lang.ClassCastException"));
		
		if(consoleOuput){
			for(FireLogEntry entry:entries ){
				System.out.println(logEntryToString(entry));
			}
		}
	}
	
	@Test
	public void testDoQuery3(){
		URL folder =LogScannerTest.class.getResource("");
		LogManager manager = new LogManager(folder.getPath());
		manager.setLineTerminateSize(2);
		
		Date d1 = str2date("2011-12-01 00:00:00");
		Date d2 = str2date("2011-12-01 23:00:00");
		
		LogSearchCriteria sc = new LogSearchCriteria(d1,d2,Priority.INFO_INT,"","","","ConstraintViolationException");
		
		List<FireLogEntry> entries = manager.doQuery(sc);
		assertEquals(50,entries.size());
		if(consoleOuput){
			for(FireLogEntry entry:entries ){
				System.out.println(logEntryToString(entry));
			}
		}
	}
	
	@Test
	public void testQueryLinux1(){
		URL folder =LogScannerTest.class.getResource("");
		LogManager manager = new LogManager(folder.getPath());
		manager.setLineTerminateSize(1);
		
		//file server.log.2011-11-30 is cerated on linux
		//windows
		manager.setLineTerminateSize(1);
		Date d1 = str2date("2011-11-30 15:47:30");
		Date d2 = str2date("2011-11-30 15:47:38");
		
		LogSearchCriteria sc = new LogSearchCriteria(d1,d2,Priority.INFO_INT,"","","","GMS: address is 192.168.120.118:50986");
		
		List<FireLogEntry> entries = manager.doQuery(sc);
		assertEquals(2,entries.size());
		if(consoleOuput){
			for(FireLogEntry entry:entries ){
				System.out.println(logEntryToString(entry));
			}
		}
	}
	
	@Test
	public void testQueryLinux2(){
		URL folder =LogScannerTest.class.getResource("");
		LogManager manager = new LogManager(folder.getPath());
		manager.setLineTerminateSize(1);
		
		//file server.log.2011-11-30 is cerated on linux
		//windows
		manager.setLineTerminateSize(1);
		Date d1 = str2date("2011-11-30 00:00:00");
		Date d2 = str2date("2011-11-30 23:47:38");
		
		LogSearchCriteria sc = new LogSearchCriteria(d1,d2,Priority.ERROR_INT,"","","","java.lang.IllegalStateException");
		
		List<FireLogEntry> entries = manager.doQuery(sc);
		assertEquals(1,entries.size());
		if(consoleOuput){
			for(FireLogEntry entry:entries ){
				System.out.println(logEntryToString(entry));
			}
		}
	}
	
	@Test
	@Ignore
	public void testDoQueryByUUID(){
		URL folder =LogScannerTest.class.getResource("");
		LogManager manager = new LogManager(folder.getPath());
		manager.setLineTerminateSize(2);
		LogSearchCriteria sc = new LogSearchCriteria("d837fda8602c4008aacb257d94002a77");
		List<FireLogEntry> entries = manager.doQuery(sc);
		assertEquals(1,entries.size());
		
		assertEquals("WebService",entries.get(0).getCategory());
		
		sc = new LogSearchCriteria("6ae1ddb8273741e88e03e6643af5cadd");
		entries = manager.doQuery(sc);
		assertEquals(1,entries.size());
		assertEquals("AbstractKernelController",entries.get(0).getCategory());
		assertEquals(Priority.ERROR_INT,entries.get(0).getLogLevel());
	}
	
	protected void outputEntries(List<FireLogEntry> entries){
		if(consoleOuput){
			for(FireLogEntry entry:entries){
				System.out.println(logEntryToString(entry));
			}
			System.out.println("\n");
		}
	}
	
	public String logEntryToString(FireLogEntry entry){
		String str=dateFormatter2.format(entry.getLogDate());
		switch(entry.getLogLevel()){
		case Priority.DEBUG_INT:
			str+=" DEBUG";
			break;
		case Priority.INFO_INT:
			str+=" INFO ";
			break;
		case Priority.WARN_INT:
			str+=" WARN ";
			break;
		case Priority.ERROR_INT:
			str+=" ERROR";
			break;
		case Priority.FATAL_INT:
			str+=" FATAL";
			break;
		}
		str+= ("["+entry.getCategory()+"]");
		str+= (entry.getDescription());
		if(entry.getLongDescr()!=null)
			str+= (entry.getLongDescr());
		return str;
	}
	
	protected static Date str2date(String str){
		try {
			return dateFormatter1.parse(str);
		} catch (ParseException e) {
			return null;
		}
	}
}
