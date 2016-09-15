package com.akuacom.pss2.userrole.layoutbuilder;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.akuacom.pss2.userrole.viewlayout.ParticipantViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ViewLayout;
import com.akuacom.pss2.util.DrasRole;


@Stateless(mappedName="ParticipantViewLayoutBuilderBean")
public class ParticipantViewLayoutBuilderBean implements ViewLayoutBuilder.L{
	
	@Resource
	private SessionContext sctx;
	
	@Override
	public void build(ViewLayout layout) {
		if(!(layout instanceof ParticipantViewLayout)){
			return;
		}
		ParticipantViewLayout participantView = (ParticipantViewLayout)layout;
		if(sctx.isCallerInRole(DrasRole.Admin.name())) {
			buildLayoutForAdmin(participantView);
		} else if(sctx.isCallerInRole(DrasRole.Operator.name())) {
			buildLayoutForOperator(participantView);			
		}else if(sctx.isCallerInRole(DrasRole.FacilityManager.name())){
			buildLayoutForFacilityManager(participantView);			
		}else if(sctx.isCallerInRole(DrasRole.Readonly.name())){
			buildLayoutForReadOnly(participantView);			
		} else if(sctx.isCallerInRole(DrasRole.Dispatcher.name())){
			buildLayoutForDispatcher(participantView);
		}
	}
	
	private void buildLayoutForAdmin(ParticipantViewLayout participantViewLayout){
		//rendered properties based on role
		participantViewLayout.setCanCreateParticipant(true);
		participantViewLayout.setCanDeleteParticipant(true);
		participantViewLayout.setEditParticipant(true);
		participantViewLayout.setViewParticipant(false);
		participantViewLayout.setCanUserAddClient(true);
		participantViewLayout.setAggregationEnabled(true);
		//enable/disable properties based on role
		participantViewLayout.setCanDeleteChecked(false);
		participantViewLayout.setCanClearParticipants(false);
		participantViewLayout.setCanExportParticipants(false);
		participantViewLayout.setCanSearchPaticipants(false);		
	}
	
	private void buildLayoutForOperator(ParticipantViewLayout participantViewLayout){
		//rendered properties based on role
		participantViewLayout.setCanCreateParticipant(true);
		participantViewLayout.setCanDeleteParticipant(true);
		participantViewLayout.setEditParticipant(true);
		participantViewLayout.setViewParticipant(false);
		participantViewLayout.setCanUserAddClient(true);
		participantViewLayout.setAggregationEnabled(true);
		//enable/disable properties based on role
		participantViewLayout.setCanDeleteChecked(false);
		participantViewLayout.setCanClearParticipants(false);
		participantViewLayout.setCanExportParticipants(false);
		participantViewLayout.setCanSearchPaticipants(false);
	}
	
	
	private void buildLayoutForFacilityManager(ParticipantViewLayout participantViewLayout){
		//rendered properties based on role
		participantViewLayout.setCanCreateParticipant(false);
		participantViewLayout.setCanDeleteParticipant(false);
	}
	
	private void buildLayoutForReadOnly(ParticipantViewLayout participantViewLayout){
		//rendered properties based on role
		participantViewLayout.setCanCreateParticipant(false);
		participantViewLayout.setCanDeleteParticipant(false);
		participantViewLayout.setEditParticipant(false);
		participantViewLayout.setViewParticipant(true);
		participantViewLayout.setCanUserAddClient(false);		
		participantViewLayout.setAggregationEnabled(false);
		//enable/disable properties based on role
		participantViewLayout.setCanClearParticipants(false);
		participantViewLayout.setCanExportParticipants(true);
		participantViewLayout.setCanSearchPaticipants(false);
		participantViewLayout.setCanDeleteChecked(true);
	}
	
	private void buildLayoutForDispatcher(ParticipantViewLayout participantViewLayout){
		//rendered properties based on role
		participantViewLayout.setCanCreateParticipant(false);
		participantViewLayout.setCanDeleteParticipant(false);
		participantViewLayout.setEditParticipant(false);
		participantViewLayout.setViewParticipant(true);
		participantViewLayout.setCanUserAddClient(false);
		participantViewLayout.setAggregationEnabled(false);
		//enable/disable properties based on role
		participantViewLayout.setCanClearParticipants(false);
		participantViewLayout.setCanExportParticipants(true);
		participantViewLayout.setCanDeleteChecked(true);
		participantViewLayout.setCanSearchPaticipants(false);
	}
}
