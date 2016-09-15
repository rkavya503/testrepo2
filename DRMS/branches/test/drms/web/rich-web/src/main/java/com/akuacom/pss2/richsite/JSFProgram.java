/**
 * 
 */
package com.akuacom.pss2.richsite;

import java.io.Serializable;

/**
 *
 */
public class JSFProgram implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String VIEW_PARTICIPANTS_LABEL="View Participants ";
	public static final String ALL_PARTICIPANTS_LABEL="All Participants ";

    private String programName;
    private String className;
	private boolean participation;
    private int participantsCount;
    private int clientsCount;
    private boolean deleted;
    private boolean disabled;
    private boolean clonable;
    private boolean eventCreatable;
    private boolean participantViewable;
    private String viewParticipantLabel;
    private int priority;
    
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}

    public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	public boolean isParticipation() {
		return participation;
	}
	public void setParticipation(boolean participation) {
		this.participation = participation;
	}
	public int getParticipantsCount() {
		return participantsCount;
	}
	public void setParticipantsCount(int participantsCount) {
		this.participantsCount = participantsCount;
	}
	public int getClientsCount() {
		return clientsCount;
	}
	public void setClientsCount(int clientsCount) {
		this.clientsCount = clientsCount;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public boolean isClonable() {
		return clonable;
	}
	public void setClonable(boolean clonable) {
		this.clonable = clonable;
	}
	public boolean isEventCreatable() {
		return eventCreatable;
	}
	public void setEventCreatable(boolean eventCreatable) {
		this.eventCreatable = eventCreatable;
	}
	public boolean isParticipantViewable() {
		return participantViewable;
	}
	public void setParticipantViewable(boolean participantViewable) {
		this.participantViewable = participantViewable;
	}
	public String getViewParticipantLabel() {
		return viewParticipantLabel;
	}
	public void setViewParticipantLabel(String viewParticipantLabel) {
		this.viewParticipantLabel = viewParticipantLabel;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public boolean isCreateDRWebsiteEvent() {
		if (programName.equals("DBP DA"))
			return true;
		return false;
	}
//	public void setCreateDRWebsiteEvent(boolean createDRWebsiteEvent) {
//		this.createDRWebsiteEvent = createDRWebsiteEvent;
//	}
}
