/**
 * 
 */
package com.akuacom.pss2.richsite.event.creation;

import java.io.Serializable;

/**
 *
 */
public class EventLocation implements Serializable {
	
	private static final long serialVersionUID = -6023853642042730065L;
	
	private boolean enrolled;
	private String ID;
	private String number;
	private String longName;
	
	public EventLocation(String ID, String name, String longName) {
		this.ID=ID;
		this.number=name;
		this.longName=longName;
	}
	
	public boolean isEnrolled() {
		return enrolled;
	}
	public void setEnrolled(boolean enrolled) {
		this.enrolled = enrolled;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
}