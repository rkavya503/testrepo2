package com.honeywell.drms.log.reader;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogBlock implements Serializable {
	protected static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss','SSS");
	
	private static final long serialVersionUID = 1L;

	//public static int BLOCK_SIZE = 128*1024; //64k
	
	private String fileName;
	private Date startTime;
	private Date endTime;
	private long size;
	private long start;
	private boolean eof;
	
	public LogBlock(String fileName) {
		super();
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	
	public boolean isEof() {
		return eof;
	}

	public void setEof(boolean eof) {
		this.eof = eof;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	private String d2S(Date date){
		if(date ==null) return "####-##-## ##:##:##,###";
		else
			return dateFormatter.format(date);
	}
	
	@Override
	public String toString() {
		return "LogBlock [startTime=" + d2S(startTime)
				+ ", endTime=" + d2S(endTime) + ", size=" + size + ", start="
				+ start + ", eof=" + eof + ",fileName=" + fileName + "]";
	}
	
}
