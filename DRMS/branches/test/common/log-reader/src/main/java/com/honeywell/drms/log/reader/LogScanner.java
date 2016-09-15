package com.honeywell.drms.log.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

import com.kanaeki.firelog.util.FireLogEntry;

/**
 * <tt>LoggerScanner</tt> scans a log file whose size may be very big and split it into small blocks so that all blocks can be 
 * proceeded simultaneously 
 * 
 * LoggerScanner will not split a big log files into small files, however, it will return a list of <tt>LogBlock</tt>
 * which will mark the start and end position of each block
 */
public class LogScanner  implements Runnable{
	
	private long scanStart;
	private long size;
	private String fileName;
	private URL fileURL;
	private int lineTerminatorLen = 1;
	
	private LogScanTracker tracker;
	private LogEntryParser logParser;
	
	
	public LogScanner(long scanStart, long blockSize, URL fileUrl,
			LogScanTracker progressUpdater,LogEntryParser logParser) {
		super();
		this.scanStart = scanStart;
		this.size = blockSize;
		this.fileURL = fileUrl;
		this.tracker = progressUpdater;
		this.logParser = logParser;
	}
	
	
	public LogScanner(long scanStart, long maxSize,String fileName,
			LogScanTracker progressUpdater,LogEntryParser logParser) {
		super();
		this.scanStart = scanStart;
		this.size = maxSize;
		this.fileName = fileName;
		this.tracker = progressUpdater;
		this.logParser = logParser;
	}
	
	public int getLineTemininatorLen(){
		return lineTerminatorLen;
	}
	
	public void setLineTerminatorLen(int lineTerminatorLen) {
		this.lineTerminatorLen = lineTerminatorLen;
	}
	
	public void doScan(){
		InputStream is = null;
		BufferedReader br  = null;
		int terminatorSize = this.getLineTemininatorLen();
		try{
			if(fileURL!=null){
				is = fileURL.openStream();
				fileName = fileURL.getPath();
			}
			else{
				is = new FileInputStream(fileName);
			}
			
			br = new BufferedReader(new InputStreamReader(is));
			br.skip(scanStart);
			long currentScan = scanStart;
			//find the entry of a new log entry, NOTE, one log entry may span several lines
			String s = null;
			LogBlock block = null;
			long blockSize = 0;
			while (true) {
				//find the start of this block
				while ( (s = br.readLine()) != null) {
					//find exact start of the block
					boolean tobreak =false;
					if(logParser.isNewRecordEntry(s)){
						block = new LogBlock(fileName);
						FireLogEntry entry=logParser.parse(s);
						block.setStartTime(entry.getLogDate());
						block.setStart(currentScan);
						//exit
						blockSize = (s.length()+terminatorSize);
						tobreak = true;
					}
					currentScan += (s.length()+terminatorSize);
					if(tobreak) break;
				}
				
				//go to end of this block
				long actuallySkip = br.skip(size);
				//move to the end of the file
				if(actuallySkip< size){
					block.setEof(true);
					block.setSize(blockSize+actuallySkip);
					tracker.addLogBlock(this.fileName,block);
					//set end date as latest time for the log file
					Date date = LogUtils.getLogFileDate(fileName);
					block.setEndTime(LogUtils.getEndOfDay(date));
					//exit 
					break;
				}else{
					currentScan  += size;
					blockSize=blockSize+size;
					//read one or more entries with same log time to close this block,one log entry may span several lines
					while ( (s = br.readLine()) != null) {
						blockSize+=(s.length()+terminatorSize);
						boolean tobreak =false;
						
						if(logParser.isNewRecordEntry(s)){
							//end of the block
							FireLogEntry entry=logParser.parse(s);
							block.setEndTime(entry.getLogDate());
							block.setSize(blockSize);
							tracker.addLogBlock(fileName,block);
							tobreak=true;
						}
						currentScan+= (s.length()+terminatorSize);
						if(tobreak) break;
					}
				}
			}
			
		}catch(Exception e){
			
		}finally{
			//finish scan of one file
			tracker.progress(1);
			if(is!=null)
				try {is.close();} catch (IOException e) {}
			if(br!=null)
				try {br.close();} catch (IOException e) {}
		}
	}
	
	@Override
	public void run() {
		doScan();
	}
}
