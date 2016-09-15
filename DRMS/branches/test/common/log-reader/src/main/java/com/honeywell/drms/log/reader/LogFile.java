package com.honeywell.drms.log.reader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogFile implements Serializable{
	private static final long serialVersionUID = 7156500382688555859L;

	private String fileName;
	
	private Date logDate;
	
	private List<LogBlock> blocks = new ArrayList<LogBlock>();

	public LogFile(String fileName, Date logDate) {
		super();
		this.fileName = fileName;
		this.logDate = logDate;
	}

	public String getFileName() {
		return fileName;
	}

	public Date getLogDate() {
		return logDate;
	}
	
	public List<LogBlock> getLogBlocks(){
		return blocks;
	}

	@Override
	public String toString() {
		return "LogFile [fileName=" + fileName + ", logDate=" + logDate
				+ ", blocks=" + blocks.size() + "]";
	}
	
}
