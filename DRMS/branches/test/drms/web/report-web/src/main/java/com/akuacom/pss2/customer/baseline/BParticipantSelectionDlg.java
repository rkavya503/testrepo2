package com.akuacom.pss2.customer.baseline;

import java.io.Serializable;
import java.util.List;

import com.akuacom.pss2.history.vo.ParticipantVO;

public class BParticipantSelectionDlg implements Serializable{
	
	private static final long serialVersionUID = -5322083050730129386L;
	
	private BaselineGen baseline;
	
	public BParticipantSelectionDlg(BaselineGen baseline){
		this.baseline = baseline;
	}
	
	private BParticipantModel participant;
	
	public List<ParticipantVO> getSelection(){
		return getParticipant().getSelectedObjects();
	}
	
	public BParticipantModel getParticipant() {
		if( participant==null)
			participant = new BParticipantModel(baseline);
		return participant;
	}
	
	public void okAction(){
		baseline.getSearchCriteria().setFilterByObject(getSelection());
	}
	
	public BaselineGen getReport() {
		return baseline;
	}
}
