package com.akuacom.pss2.opt.report;

import java.io.Serializable;
import java.util.List;

import com.akuacom.pss2.history.vo.ParticipantVO;

public class ParticipantSelectionDlg implements Serializable{
	
	private static final long serialVersionUID = -5322083050730129386L;
	
	private OperatorReports report;
	
	public ParticipantSelectionDlg(OperatorReports report){
		this.report = report;
	}
	
	private ParticipantModel participant;
	
	public List<ParticipantVO> getSelection(){
		return getParticipant().getSelectedObjects();
	}
	
	public ParticipantModel getParticipant() {
		if( participant==null)
			participant = new ParticipantModel(report);
		return participant;
	}
	
	public void okAction(){
		report.getSearchCriteria().setFilterByObject(getSelection());
	}
	
	public OperatorReports getReport() {
		return report;
	}
}
