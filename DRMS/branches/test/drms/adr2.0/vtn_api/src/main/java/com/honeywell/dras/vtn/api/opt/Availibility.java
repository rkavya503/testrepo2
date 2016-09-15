package com.honeywell.dras.vtn.api.opt;

import java.util.Date;

public class Availibility {
	
	private Date startTime;

	private long duration;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

}
