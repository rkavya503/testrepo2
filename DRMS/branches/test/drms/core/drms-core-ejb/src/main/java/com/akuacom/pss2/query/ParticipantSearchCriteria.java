package com.akuacom.pss2.query;

import java.io.Serializable;
import java.util.List;

public class ParticipantSearchCriteria implements Serializable{

	private static final long serialVersionUID = 4745136892252616978L;
	
	private List<String> exactProgramNames;
	
	private String nameLeadingStr;
	
	private String accountLeadingStr;
	
	private List<String> programLeadingStr;
	
	private List<String> nameList;
	private List<String> accountList;

	public List<String> getExactProgramNames() {
		return exactProgramNames;
	}

	public void setExactProgramNames(List<String> exactProgramNames) {
		this.exactProgramNames = exactProgramNames;
	}

	public String getNameLeadingStr() {
		return nameLeadingStr;
	}

	public void setNameLeadingStr(String nameLeadingStr) {
		this.nameLeadingStr = nameLeadingStr;
	}

	public String getAccountLeadingStr() {
		return accountLeadingStr;
	}

	public void setAccountLeadingStr(String accountLeadingStr) {
		this.accountLeadingStr = accountLeadingStr;
	}

	public List<String> getProgramLeadingStr() {
		return programLeadingStr;
	}

	public void setProgramLeadingStr(List<String> programLeadingStr) {
		this.programLeadingStr = programLeadingStr;
	}



	/**
	 * @return the accountList
	 */
	public List<String> getAccountList() {
		return accountList;
	}

	/**
	 * @param accountList the accountList to set
	 */
	public void setAccountList(List<String> accountList) {
		this.accountList = accountList;
	}

	/**
	 * @return the nameList
	 */
	public List<String> getNameList() {
		return nameList;
	}

	/**
	 * @param nameList the nameList to set
	 */
	public void setNameList(List<String> nameList) {
		this.nameList = nameList;
	}
	
}
