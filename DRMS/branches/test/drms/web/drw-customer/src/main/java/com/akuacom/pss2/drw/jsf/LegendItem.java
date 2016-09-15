package com.akuacom.pss2.drw.jsf;

import java.io.Serializable;

public class LegendItem implements Serializable{
	private static final long serialVersionUID = 1L;
	private Boolean selected;
	private String label;
	private String programNm;
	private String eventKey; // this is a virtual key, it's an identifier of a group of eventDetail, mapping to event cach
	public Boolean getSelected() {
		if(selected==null){
			selected = false;
		}
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getProgramNm() {
		return programNm;
	}
	public void setProgramNm(String programNm) {
		this.programNm = programNm;
	}
	public String getEventKey() {
		return eventKey;
	}
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
}
