package com.akuacom.pss2.drw;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EventTimestamp {
	public EventTimestamp(){
	}
	public EventTimestamp(String type,String timestamp){
		this.type=type;
		this.timestamp=timestamp;
	}
	private String type;

    private String timestamp;
    
	
	public String toString(){
		
		return " type: "+this.getType()+"  ||  timestamp: "+this.getTimestamp();
	}


	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}


	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	
    
}
