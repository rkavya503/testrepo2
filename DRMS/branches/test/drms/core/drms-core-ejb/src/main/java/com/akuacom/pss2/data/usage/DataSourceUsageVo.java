package com.akuacom.pss2.data.usage;

import java.io.Serializable;

public class DataSourceUsageVo implements Serializable{

	private static final long serialVersionUID = 6775921868463924443L;
	private String ownerID;
	private Boolean adjusted;
	private String eventName;
	
	public String getOwnerID() {
		return ownerID;
	}
	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}
	public Boolean getAdjusted() {
		return adjusted;
	}
	public void setAdjusted(Boolean adjusted) {
		this.adjusted = adjusted;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

}
