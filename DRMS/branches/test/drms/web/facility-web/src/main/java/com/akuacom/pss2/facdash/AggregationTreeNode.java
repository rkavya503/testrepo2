package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AggregationTreeNode implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String participantName;
	private String programName;
	private String accountNo;
	private String secondaryAccountNo;
	private boolean leafNode;
	private boolean rootNode;
	public String getParticipantName() {
		return participantName==null?"":participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getProgramName() {
		return programName==null?"":programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public boolean isLeafNode() {
		return leafNode;
	}
	public void setLeafNode(boolean leafNode) {
		this.leafNode = leafNode;
	}
	
	private List<AggregationTreeNode> contents;
	
	public List<AggregationTreeNode> getContents() {
		if(contents==null)
			contents = new ArrayList<AggregationTreeNode>();
		return contents;
	}
	public void setContents(List<AggregationTreeNode> contents) {
		this.contents = contents;
	}
	public boolean isRootNode() {
		return rootNode;
	}
	public void setRootNode(boolean rootNode) {
		this.rootNode = rootNode;
	}
	/**
	 * @return the consolidationProgramName
	 */
	public String getConsolidationProgramName() {
		if(CBPUtil.getCbpGroup().get("CBP").contains(programName)){
			return "CBP";
		}else{
			return programName;
		}
	}
	public String getSecondaryAccountNo() {
		return secondaryAccountNo;
	}
	public void setSecondaryAccountNo(String secondaryAccountNo) {
		this.secondaryAccountNo = secondaryAccountNo;
	}	
}
