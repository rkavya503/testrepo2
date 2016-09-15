/**
 * 
 */
package com.akuacom.pss2.program.DRwebsite;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.akuacom.pss2.event.Event;

/**
 * the class PREventEntry
 * 
 */
public class PREvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	String longProgramName;
	String programName;
	String programClass;	
	Event event;

    public String getLongProgramName() {
		return longProgramName;
	}
	public void setLongProgramName(String longProgramName) {
		this.longProgramName = longProgramName;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getProgramClass() {
		return programClass;
	}
	public void setProgramClass(String programClass) {
		this.programClass = programClass;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	
	public String getFormattedDate() {
		StringBuilder sb = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("MM/dd/yyyy HH:mm");
		sb.append(format.format(event.getStartTime()));
		sb.append("-");
		format.applyPattern("HH:mm");
		sb.append(format.format(event.getEndTime()));
		
		return sb.toString();
	}
}
