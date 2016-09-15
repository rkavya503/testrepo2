package com.akuacom.pss2.customer.baseline;

import java.util.Collections;
import java.util.List;

import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.jsf.model.AbstractTreeContentProvider;
import com.akuacom.pss2.customer.report.FDUtils;
import com.akuacom.pss2.customer.report.WebUtil;
import com.akuacom.pss2.history.HistoryReportManager;
import com.akuacom.pss2.history.vo.ParticipantVO;

public class BParticipantModel extends AbstractTreeContentProvider<ParticipantVO>{

	private int totalCount = 0;
	private List<ParticipantVO> contents;
	private HistoryReportManager reportManager;
	private boolean searchBtnClicked = false;
	
	private String participantName = "";
	
	private BaselineGen baseline;
	
	public BParticipantModel(BaselineGen report){
		this.reportManager = report.getReportManager();
	}
	
	@Override
	public int getTotalRowCount() {
		return totalCount;
	}
	
	@Override
	public List<ParticipantVO> getContents() {
		if(contents==null)
			return Collections.emptyList();
		else
			return contents;
	}
	
	@Override
	public void updateModel() {
		if(isSortColumnChanged() || isRangeChanged() || searchBtnClicked){
			SearchConstraint  sc = WebUtil.getSearchConstraint(this);
			try {
				//totalCount= reportManager.getParticipantCount(participantName);
				//if(totalCount!=0){
				clearTreeNodeCache(null);
				contents=reportManager.findParticipants(participantName,sc);
				totalCount = contents.size();
				//}
			} catch (Exception e) {
				FDUtils.addMsgError("Internal Error!");
			}
		}
		searchBtnClicked = false;
	}
	
	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	
	public void searchAction(){
		searchBtnClicked = true;
	}

	@Override
	public List<ParticipantVO> getChildren(ParticipantVO parent) {
		return Collections.emptyList();
	}

	@Override
	public boolean hasChildren(ParticipantVO parent) {
		return false;
	}
	
	
	public BaselineGen getBaseline() {
		return baseline;
	}
}
