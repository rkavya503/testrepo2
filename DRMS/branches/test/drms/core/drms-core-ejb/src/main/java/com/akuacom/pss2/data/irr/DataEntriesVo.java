package com.akuacom.pss2.data.irr;

import java.io.Serializable;
import java.util.List;

import com.akuacom.pss2.data.PDataEntry;

public class DataEntriesVo implements Serializable{
	private static final long serialVersionUID = -9178097273008928766L;
	
	private List<String> allPaticipantNames;
	private List<String> contributedPaticipantNames;
	private String message;
	private List<PDataEntry> entries;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<PDataEntry> getEntries() {
		return entries;
	}
	public void setEntries(List<PDataEntry> entries) {
		this.entries = entries;
	}
	public List<String> getAllPaticipantNames() {
		return allPaticipantNames;
	}
	public void setAllPaticipantNames(List<String> allPaticipantNames) {
		this.allPaticipantNames = allPaticipantNames;
	}
	public List<String> getContributedPaticipantNames() {
		return contributedPaticipantNames;
	}
	public void setContributedPaticipantNames(
			List<String> contributedPaticipantNames) {
		this.contributedPaticipantNames = contributedPaticipantNames;
	}
	
}
