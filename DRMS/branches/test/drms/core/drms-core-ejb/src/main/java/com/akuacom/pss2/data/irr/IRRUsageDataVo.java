package com.akuacom.pss2.data.irr;

import java.io.Serializable;
import java.util.List;


public class IRRUsageDataVo implements Serializable{
	
	private static final long serialVersionUID = -5767365247031639226L;

	private String participantName;
	private String message;
	private List<TreeDataSet> dataSets;
	private List<String> allPaticipantNames;
	private List<String> contributedPaticipantNames;
	public String getParticipantName() {
		return participantName;
	}

	public List<TreeDataSet> getDataSets() {
		return dataSets;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public void setDataSets(List<TreeDataSet> dataSets) {
		this.dataSets = dataSets;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
