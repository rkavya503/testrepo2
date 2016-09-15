package com.honeywell.drms.log.reader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kanaeki.firelog.util.FireLogEntry;

public class LogManager implements Serializable{
	
	private static final long serialVersionUID = -1267994630669124062L;
	//private static final Logger log = Logger.getLogger(LogManager.class);
	
	private String logLocation;
	private int lineTerminateSize = -1;
	private long blockSize = 0;
	
	private Map<String,LogFile> scanedLogFiles = new HashMap<String,LogFile>();
	
	private static int SCAN_THEAD_POOL_SIZE  = 3;
	private static int PARSER_THEAD_POOL_SIZE= 10;
	private static int BLOCK_SIZE = 256 * 1024;
	
	public LogManager(String logLocation){
		this.logLocation = logLocation;
	}
	
	public LogManager(){
		
	}
	
	
	public long getBlockSize() {
		if(blockSize==0)
			blockSize =  BLOCK_SIZE;
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getLineTerminateSize() {
		if(lineTerminateSize==-1)
			lineTerminateSize =LogUtils.getLineTerminatorlength();
		return lineTerminateSize;
	}

	public void setLineTerminateSize(int lineTerminateSize) {
		this.lineTerminateSize = lineTerminateSize;
	}

	public synchronized  void scanLogFiles(final Date startTime, final Date endTime){
		String targetFiles[]= getTargetLogFiles(startTime,endTime);
		
		//get files need to be rescanned 
		List<String> toScan= new ArrayList<String>();
		for(String file:targetFiles){
			 file = logLocation+"/"+file;
			 Date date = LogUtils.getLogFileDate(file);
			 //force to rescan today's log file
			 if(LogUtils.getStartOfDay(date).equals(LogUtils.getStartOfDay(new Date()))){
				 toScan.add(file);
			 //scan other files which not scanned 
			 }else if(hasOverlap(LogUtils.getStartOfDay(date),LogUtils.getEndOfDay(date),startTime,endTime) && !scanedLogFiles.containsKey(file)){
				 toScan.add(file); 
			 } 
		}
		//start scan 
		ExecutorService threadExecutor = Executors.newFixedThreadPool(SCAN_THEAD_POOL_SIZE);
		LogScanTracker  tracker = new LogScanTracker(toScan.size()){
			public synchronized void addLogBlock(String file,LogBlock logBlock){
				scanedLogFiles.get(file).getLogBlocks().add(logBlock);
			}
		};
		
		for(String file:toScan){
			LogFile logFile = new LogFile(file,LogUtils.getLogFileDate(file));
			scanedLogFiles.put(file,logFile);
			LogScanner scannerTask = new LogScanner(0,getBlockSize(),file,tracker,new LogEntryParser());
			scannerTask.setLineTerminatorLen(getLineTerminateSize());
			threadExecutor.execute(scannerTask);
		}
		
		threadExecutor.shutdown();
		while(true){
			if(tracker.isAllDone())
				break;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public String[] getTargetLogFiles(final Date startTime, final Date endTime){
		File dir = new File(logLocation);
		String[] children = dir.list();
		
		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        boolean b= LogUtils.isValidLogFileName(name);
		        if(!b) return false;
		        Date date = LogUtils.getLogFileDate(name);
		        Date start =LogUtils. getStartOfDay(date);
		        Date end =LogUtils.getEndOfDay(date);
		        
		        if(hasOverlap(startTime,endTime, start,end))
		        	return true;
		        return false;
		    }
		};
		children = dir.list(filter);
		return children;
	}
	
	public String[] getAllLogFiles(){
		File dir = new File(logLocation);
		String[] children = dir.list();
		
		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return LogUtils.isValidLogFileName(name);
		    }
		};
		children = dir.list(filter);
		return children;
	}
	
	private boolean hasOverlap(Date s1, Date e1,Date s2,Date e2){
		long ls1= s1.getTime();
		long ls2 = s2.getTime();
		long le1= e1.getTime();
		long le2 = e2.getTime();
		
		if(ls1>=ls2 && ls1<=le2  || le1>=ls2 && le1<=le2 ||
		   ls2>=ls1	&& ls2<=le1  || le2>=ls1 && le2<=le1)
			return true;
		return false;
	}
	
	public List<FireLogEntry> doQuery(LogSearchCriteria criteria){
		//first to scan log files to break it down into small log block if necessary 
		Date end   = criteria.getEndDate();
		Date start = criteria.getStartDate();
		
		//search by id
		if(criteria.getUuid()!=null){
			end=LogUtils.getEndOfDay(new Date());
			//ensure to scan all files in the folder
			start = LogUtils.getStartOfDay(new Date( (new Date()).getTime()- 365*LogUtils.MSEC_IN_DAY));
		}
		scanLogFiles(start,end);
		
		//selected blocks to do query against
		List<LogBlock> blocks = getTargetBlocks(start,end);
		
		//max progress
		LogQueryTracker tracker = new LogQueryTracker(blocks.size());
		
		//start search, multiple thread
		ExecutorService threadExecutor = Executors.newFixedThreadPool(PARSER_THEAD_POOL_SIZE);
		for(LogBlock block:blocks){
			LogQuery queryTask = new LogQuery(block,new LogEntryMatcher(),criteria,new LogEntryParser(),tracker);
			queryTask.setLineTerminateSize(this.getLineTerminateSize());
			threadExecutor.execute(queryTask);
		}
		
		threadExecutor.shutdown();
		while(true){
			if(tracker.isAllDone() || (criteria.getUuid()!=null && tracker.isIdFound()) ){
				threadExecutor.shutdownNow();
				break;
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
		return tracker.getResults();
	}
	
	public List<LogBlock> getTargetBlocks(Date start, Date end){
		List<LogBlock> blocks = new ArrayList<LogBlock>();
		for(LogFile file:scanedLogFiles.values()){
			for(LogBlock block:file.getLogBlocks()){
				if(hasOverlap(block.getStartTime(),block.getEndTime(),start,end)){
					blocks.add(block);
				}
			}
		}
		return blocks;
	}
	
	public Map<String, LogFile> getScanedLogFiles() {
		return scanedLogFiles;
	}
	
}
