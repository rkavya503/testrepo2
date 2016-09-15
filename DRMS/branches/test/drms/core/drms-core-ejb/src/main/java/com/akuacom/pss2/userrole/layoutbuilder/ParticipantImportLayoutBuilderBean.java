package com.akuacom.pss2.userrole.layoutbuilder;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.akuacom.pss2.userrole.viewlayout.ParticipantImportLayout;
import com.akuacom.pss2.userrole.viewlayout.ViewLayout;
import com.akuacom.pss2.util.DrasRole;

@Stateless(mappedName="participantImportLayoutBuilderBean")
public class ParticipantImportLayoutBuilderBean implements ViewLayoutBuilder.L {
	
	@Resource
	private SessionContext sctx;
	
	@Override
	public void build(ViewLayout layout) {
		if(!(layout instanceof ParticipantImportLayout)){
			return;
		}
		ParticipantImportLayout participantImportView = (ParticipantImportLayout)layout;
		if(sctx.isCallerInRole(DrasRole.Admin.name())) {
			buildLayoutForAdmin(participantImportView);
		} else if(sctx.isCallerInRole(DrasRole.Operator.name())) {
			buildLayoutForOperator(participantImportView);			
		}else if(sctx.isCallerInRole(DrasRole.FacilityManager.name())){
			buildLayoutForFacilityManager(participantImportView);			
		}else if(sctx.isCallerInRole(DrasRole.Readonly.name())){
			buildLayoutForReadOnly(participantImportView);			
		} else if(sctx.isCallerInRole(DrasRole.Dispatcher.name())){
			buildLayoutForDispatcher(participantImportView);
		}
	}
	
	private void buildLayoutForAdmin(ParticipantImportLayout participantImportLayout){
		participantImportLayout.setFileUploadEnabled(true);
		participantImportLayout.setImportModalCancel1(false);
		participantImportLayout.setImportModalCancel2(false);
		participantImportLayout.setImportModalDone(false);
	}
	
	private void buildLayoutForOperator(ParticipantImportLayout participantImportLayout){
		participantImportLayout.setFileUploadEnabled(true);
		participantImportLayout.setImportModalCancel1(false);
		participantImportLayout.setImportModalCancel2(false);
		participantImportLayout.setImportModalDone(false);
	}
	
	
	private void buildLayoutForFacilityManager(ParticipantImportLayout participantImportLayout){
		
	}
	
	private void buildLayoutForReadOnly(ParticipantImportLayout participantImportLayout){
		participantImportLayout.setFileUploadEnabled(false);
		participantImportLayout.setImportModalCancel1(true);
		participantImportLayout.setImportModalCancel2(true);
		participantImportLayout.setImportModalDone(true);
	}
	
	private void buildLayoutForDispatcher(ParticipantImportLayout participantImportLayout){
		participantImportLayout.setFileUploadEnabled(false);
		participantImportLayout.setImportModalCancel1(true);
		participantImportLayout.setImportModalCancel2(true);
		participantImportLayout.setImportModalDone(true);
	}

}
