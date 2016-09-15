package com.akuacom.pss2.query;

import java.io.Serializable;

public class ProgramSummary implements Serializable {

	private static final long serialVersionUID = -289058942817965822L;

	
	private String programName;
	private String className;
	private int participantCount;
	private int clientCount;
	private int priority;

	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public int getParticipantCount() {
		return participantCount;
	}
	public void setParticipantCount(int participantCount) {
		this.participantCount = participantCount;
	}
	public int getClientCount() {
		return clientCount;
	}
	public void setClientCount(int clientCount) {
		this.clientCount = clientCount;
	}
	
	
}
