package com.honeywell.drms.log.reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.kanaeki.firelog.util.FireLogEntry;

public class LogQueryTracker {
	
	private int progress = 0;
	private int progressMax;  
	private boolean idFound = false;
	
	private List<FireLogEntry> entries = new ArrayList<FireLogEntry>();
	
	
	public LogQueryTracker(int progressMax) {
		super();
		this.progressMax = progressMax;
	}

	public int getProgress() {
		return progress;
	}

	public boolean isAllDone() {
		return progress == progressMax;
	}
	
	public synchronized void progress(int progress) {
		this.progress +=progress;
	}
	
	public synchronized void addLogEntry(FireLogEntry logEntry){
		entries.add(logEntry);
	}
	
	public synchronized List<FireLogEntry> getResults(){
		Collections.sort(entries, new Comparator<FireLogEntry>(){
			@Override
			public int compare(FireLogEntry o1, FireLogEntry o2) {
				return o2.getLogDate().compareTo(o1.getLogDate());
			}
		});
		return entries;
	}

	public boolean isIdFound() {
		return idFound;
	}

	public synchronized void  setIdFound(boolean idFound) {
		this.idFound = idFound;
	}
	
}
