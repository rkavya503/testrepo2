package com.honeywell.drms.log.reader;

import java.util.ArrayList;
import java.util.List;

public class LogScanTracker {

	private int progress = 0;
	private int progressMax; 
	
	private List<LogBlock> blocks = new ArrayList<LogBlock>(40);
	
	public LogScanTracker(int progressMax){
		this.progressMax = progressMax;
	}
	
	public LogScanTracker(long progressMax){
		this.progressMax = (int) progressMax;
	}
	
	public synchronized void progress(int progress) {
		this.progress +=progress;
	}
	
	public synchronized void addLogBlock(String file,LogBlock logBlock){
		blocks.add(logBlock);
	}
	
	public int getProgress() {
		return progress;
	}

	public boolean isAllDone() {
		return progress == progressMax;
	}

	public List<LogBlock> getLogBlocks() {
		return blocks;
	}
	
}
