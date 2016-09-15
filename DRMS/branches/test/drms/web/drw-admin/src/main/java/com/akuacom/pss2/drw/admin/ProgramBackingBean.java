package com.akuacom.pss2.drw.admin;

import java.io.Serializable;

public class ProgramBackingBean implements Serializable {

	/** serial version*/
	private static final long serialVersionUID = -6506349269890215222L;
	/** index for display*/
	private int index;
	/** program long name*/
	private String programLongName;
	/** program class*/
	private String programClass;
	/** program name*/
	private String programName;
	/** program utility name*/
	private String programUtilityName;
	/** program event create URL*/
	private String eventURL;
	private String activeEventURL;
	private String historyEventURL;
	private String eventURLStyle;
	private String activeEventURLStyle;
	private String historyEventURLStyle;
	private Boolean bipProgram; 
	
	/**
	 * constructor
	 */
	public ProgramBackingBean(){
		super();
	}
	// -----------------------------------------------------------Getter and Setter-----------------------------------------------------------
	public String getProgramLongName() {
		if(programLongName==null){
			programLongName = "";
		}
		return programLongName;
	}

	public String getProgramClass() {
		if(programClass==null){
			programClass="";
		}
		return programClass;
	}

	public String getProgramName() {
		if(programName==null){
			programName ="";
		}
		return programName;
	}

	public void setProgramLongName(String programLongName) {
		this.programLongName = programLongName;
	}

	public void setProgramClass(String programClass) {
		this.programClass = programClass;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getProgramUtilityName() {
		return programUtilityName;
	}

	public void setProgramUtilityName(String programUtilityName) {
		this.programUtilityName = programUtilityName;
	}
	/**
	 * @return the eventURL
	 */
	public String getEventURL() {
		if(eventURL==null){
			eventURL = "";
		}
		return eventURL;
	}
	/**
	 * @param eventURL the eventURL to set
	 */
	public void setEventURL(String eventURL) {
		this.eventURL = eventURL;
	}
	public String getActiveEventURL() {
		if(activeEventURL==null){
			activeEventURL = "";
		}
		return activeEventURL;
	}
	public void setActiveEventURL(String activeEventURL) {
		this.activeEventURL = activeEventURL;
	}
	public String getHistoryEventURL() {
		if(historyEventURL==null){
			historyEventURL = "";
		}
		return historyEventURL;
	}
	public void setHistoryEventURL(String historyEventURL) {
		this.historyEventURL = historyEventURL;
	}
	public String getEventURLStyle() {
		if(eventURLStyle==null) eventURLStyle="";
		return eventURLStyle;
	}
	public void setEventURLStyle(String eventURLStyle) {
		this.eventURLStyle = eventURLStyle;
	}
	public String getActiveEventURLStyle() {
		if(activeEventURLStyle==null) activeEventURLStyle="";
		return activeEventURLStyle;
	}
	public void setActiveEventURLStyle(String activeEventURLStyle) {
		this.activeEventURLStyle = activeEventURLStyle;
	}
	public String getHistoryEventURLStyle() {
		return historyEventURLStyle;
	}
	public void setHistoryEventURLStyle(String historyEventURLStyle) {
		this.historyEventURLStyle = historyEventURLStyle;
	}
	public Boolean getBipProgram() {
		return bipProgram;
	}
	public void setBipProgram(Boolean bipProgram) {
		this.bipProgram = bipProgram;
	}
	
}
