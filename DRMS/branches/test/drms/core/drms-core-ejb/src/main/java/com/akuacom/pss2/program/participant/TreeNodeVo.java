package com.akuacom.pss2.program.participant;

import java.io.Serializable;

public class TreeNodeVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String participantName;
	private String programName;
	private String parentName;
	private String accountNumber;
	private Long treelevel;
	private String secondaryAccountNo;
	
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public Long getTreelevel() {
		return treelevel;
	}
	public void setTreelevel(Long treelevel) {
		this.treelevel = treelevel;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getSecondaryAccountNo() {
		return secondaryAccountNo;
	}
	public void setSecondaryAccountNo(String secondaryAccountNo) {
		this.secondaryAccountNo = secondaryAccountNo;
	}
}
