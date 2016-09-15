package com.honeywell.drms.log.reader.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.junit.Test;

import com.honeywell.drms.log.reader.LogBlock;
import com.honeywell.drms.log.reader.LogEntryParser;
import com.honeywell.drms.log.reader.LogScanTracker;
import com.honeywell.drms.log.reader.LogScanner;

import junit.framework.TestCase;

public class LogScannerTest extends TestCase {
	
	private static boolean outputFile = false;
	private static String logFile ="server_1201.log";
	
	@Test
	public void testScanLogFile() {
		// URL file =
		//String logFile = "server.log";
		URL file = LogScannerTest.class.getResource(logFile);
		
		// one file
		LogScanTracker tracker = new LogScanTracker(1);
		LogEntryParser parser = new LogEntryParser();

		LogScanner scanner = new LogScanner(0, 64*1024, file, tracker, parser);
		scanner.doScan();
		
		int size = tracker.getLogBlocks().size();
//		/assertEquals(5,size);
		
		//for verify manually 
		for(int i=0;outputFile && i < tracker.getLogBlocks().size();i++){
			LogBlock block=tracker.getLogBlocks().get(i);
			System.out.println(block);
			String fileName = "C:/log/"+logFile+".block"+(i+1)+".log";
			writeFile(fileName,block);
		}
	}
	
	public  void atestURL() {
		URL file = LogScannerTest.class.getResource(logFile);
		String fileName = file.getPath();
		InputStream is = null;
		BufferedReader br  = null;
		try{
				is = new FileInputStream(fileName);
				br = new BufferedReader(new InputStreamReader(is));
				System.out.println(br.readLine());
		}catch(Exception e){
			
		}
		finally {		
			try{
				is.close();
				br.close();
				}catch(Exception e){}
		}
	}
	
	
	private static void writeFile(String fileName, LogBlock block ) {
		InputStream is = null;
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(new File(
					fileName), true));
			URL file = LogScannerTest.class.getResource(logFile);
			is = file.openStream();
			br = new BufferedReader(new InputStreamReader(is));
			
			br.skip(block.getStart());
			int size = 0;
			String s = "";
			while(size <= block.getSize() && (s = br.readLine()) != null){
					size += (s.length()+2);
					bw.write(s);
					bw.newLine();
			}
			bw.close();
		} catch (Exception e) {
			
		}finally{
			try{
			is.close();
			br.close();
			bw.close();
			}catch(Exception e){}
		}
	}
}
