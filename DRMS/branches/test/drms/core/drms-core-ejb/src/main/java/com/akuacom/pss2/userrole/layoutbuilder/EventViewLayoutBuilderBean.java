package com.akuacom.pss2.userrole.layoutbuilder;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.akuacom.pss2.userrole.viewlayout.ClientViewLayout;
import com.akuacom.pss2.userrole.viewlayout.EventViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ParticipantViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ProgramViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ViewLayout;
import com.akuacom.pss2.util.DrasRole;


@Stateless(mappedName="EventViewLayoutBuilderBean")
public class EventViewLayoutBuilderBean implements ViewLayoutBuilder.L{
	
	@Resource
	private SessionContext sctx;

	@Override
	public void build(ViewLayout layout) {
		if(!(layout instanceof EventViewLayout)){
			//print log
			return;
		}
		EventViewLayout eventView = (EventViewLayout)layout;
		if(sctx.isCallerInRole(DrasRole.Admin.toString())){
			buildLayoutForAdmin(eventView);
		}
		else if(sctx.isCallerInRole(DrasRole.FacilityManager.toString())){
			buildLayoutForFacilityManager(eventView);
			
		}
		else if(sctx.isCallerInRole(DrasRole.Readonly.toString())){
			buildLayoutForReadOnly(eventView);
			
		}
		else if(sctx.isCallerInRole(DrasRole.Dispatcher.toString())){
			buildLayoutForDispatcher(eventView);
		}
		else if(sctx.isCallerInRole(DrasRole.Operator.toString())){
			buildLayoutForOperator(eventView);
		}
		
	}
	
	private void buildLayoutForAdmin(EventViewLayout layout){
		layout.setCanDeleteEvent(true);
		layout.setCanOptOutOfEvent(true);
		layout.setCanDeleteDemoEvent(true);
	}
	private void buildLayoutForFacilityManager(EventViewLayout layout){
		layout.setCanDeleteEvent(true);
		layout.setCanOptOutOfEvent(true);
		layout.setCanDeleteDemoEvent(false);
	}
	private void buildLayoutForReadOnly(EventViewLayout layout){
		layout.setCanDeleteEvent(false);
		layout.setCanOptOutOfEvent(false);
		layout.setCanDeleteDemoEvent(false);
	
	}
	private void buildLayoutForDispatcher(EventViewLayout layout){
		layout.setCanDeleteEvent(true);
		layout.setCanOptOutOfEvent(false);
		layout.setCanDeleteDemoEvent(true);
	}
	private void buildLayoutForOperator(EventViewLayout layout){
		layout.setCanDeleteEvent(false);
		layout.setCanOptOutOfEvent(true);
		layout.setCanDeleteDemoEvent(true);
	}

}
