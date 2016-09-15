package com.honeywell.drms.log.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.log4j.Logger;

import com.kanaeki.firelog.util.FireLogEntry;

public class LogQuery implements Runnable{

	private static final Logger log = Logger.getLogger(LogQuery.class);
	
	private LogBlock logBlock;
	private LogEntryMatcher matcher;
	private LogSearchCriteria searchCriteria;
	private LogEntryParser logParser;
	private int lineTerminateSize =-1;
	
	private LogQueryTracker tracker;
	
	public int getLineTerminateSize() {
		if(lineTerminateSize==-1)
			lineTerminateSize =LogUtils.getLineTerminatorlength();
		return lineTerminateSize;
	}
	
	public void setLineTerminateSize(int size){
		this.lineTerminateSize = size;
	}
	
	public LogQuery(LogBlock logBlock, LogEntryMatcher matcher,
			LogSearchCriteria searchCriteria, LogEntryParser logParser, LogQueryTracker tracker) {
		super();
		this.logBlock = logBlock;
		this.matcher = matcher;
		this.searchCriteria = searchCriteria;
		this.logParser = logParser;
		this.tracker = tracker;
	}
	
	public void run(){
		InputStream is = null;
		BufferedReader br  = null;
		boolean found = false;
		try{
			is = new FileInputStream(logBlock.getFileName());
			String s = "";
			br = new BufferedReader(new InputStreamReader(is));
			if(logBlock.getStart()<0) return;
			br.skip(logBlock.getStart());
			long size = 0;
			String lastEntry = null;
			Date lastDate = null;
			String currentEntry = "";
			String nextEntry = "";
			//read new line until to the block end
			while(size <= logBlock.getSize() && (lastEntry!=null || (s = br.readLine()) != null)){
				if(lastEntry!=null){
					s= lastEntry;
					lastEntry = null;
				}else{//read
					size += (s.length()+getLineTerminateSize());
				}
				currentEntry = s;
				
				//real till the end of this log entry, log entry may span several lines
				while(size <= logBlock.getSize() && (s = br.readLine()) != null){
					size += (s.length()+getLineTerminateSize());
					Date date = logParser.getLogTime(s);
					//continue to read line to current entry
					if(date==null){
						currentEntry+= ("\n"+s);
					}else{
						lastDate = date;
						nextEntry = s;
						lastEntry = nextEntry;
						break;
					}
				}
				//parse log entry
				FireLogEntry entry = logParser.parse(currentEntry);
				if(entry!=null && matcher.isMatch(entry, searchCriteria)){
					tracker.addLogEntry(entry);
					found = true;
				}
				if(searchCriteria.getUuid()==null && lastDate.after(searchCriteria.getEndDate()))
					break;
			}
		}catch(FileNotFoundException e){
			//file may be removed manually
			log.warn(e.getMessage(),e);
		}
		catch(Exception e){
			log.error("Error during search of log", e);
		}finally {
			tracker.progress(1);
			if(searchCriteria.getUuid()!=null && found){
				tracker.setIdFound(true);
			}
			if(is!=null)
				try {is.close();} catch (IOException e) {}
			if(br!=null)
				try {br.close();} catch (IOException e) {}
		}
	}

	public LogQueryTracker getTracker() {
		return tracker;
	}
}
