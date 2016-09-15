package com.akuacom.utils.cvs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;
/**
 * CsvParseUtilTester
 * 
 * @author Yang Liu
 *
 */
public class CsvParseUtilTest extends TestCase {
	
	private String fileName ="src/test/resources/SampleOfCSV.csv";
	private String assertTrueResultContent="Account,15:00-16:00,16:00-17:00,17:00-18:00,18:00-19:00,19:00-20:00\npdbp,100,100,100,0,0\n";
	@Test
	public void testParseFileByFileName(){
		try {
			String result =CsvParseUtil.parseFile(fileName);
			assertNotNull(result);
			assertEquals(result,assertTrueResultContent);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	@Test
	public void testParseFileByInputString(){
		try {
			File file = new File(fileName);			
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			String result =CsvParseUtil.parseFile(inputStream);
			assertNotNull(result);
			assertEquals(result,assertTrueResultContent);
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}
	@Test
	public void testTransferCsvStringToMap(){
		Map<Integer, String[]> map;
		try {
			map = CsvParseUtil.transferCsvStringToMap(CsvParseUtil.parseFile(fileName), true);
			assertNotNull(map);
			assertEquals(2,map.size());	
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	@Test
	public void testTransferCsvStringToList(){
		List<String[]> list;
		try {
			list = CsvParseUtil.transferCsvStringToList(CsvParseUtil.parseFile(fileName), true);
			assertNotNull(list);
			assertEquals(2,list.size());	
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	@Test
	public void testPrintCsvContent(){
		try {
			String result1 = CsvParseUtil.printCsvContent(CsvParseUtil.transferCsvStringToList(CsvParseUtil.parseFile(fileName), true));	
			String result2 = CsvParseUtil.printCsvContent(CsvParseUtil.transferCsvStringToMap(CsvParseUtil.parseFile(fileName), true));
			assertNotNull(result1);
			assertNotNull(result2);
			assertEquals(result1,assertTrueResultContent);
			assertEquals(result2,assertTrueResultContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
