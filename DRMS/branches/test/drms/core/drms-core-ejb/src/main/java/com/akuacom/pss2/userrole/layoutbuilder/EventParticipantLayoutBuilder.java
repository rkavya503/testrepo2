package com.akuacom.pss2.userrole.layoutbuilder;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.akuacom.pss2.userrole.viewlayout.EventParticipantViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ViewLayout;
import com.akuacom.pss2.util.DrasRole;

@Stateless(mappedName="EventParticipantLayoutBuilder")
public class EventParticipantLayoutBuilder implements ViewLayoutBuilder.L{

	@Resource
	private SessionContext sctx;
	@Override
	public void build(ViewLayout layout) {
		if(!(layout instanceof EventParticipantViewLayout)){
			//print log
			return;
		}
		EventParticipantViewLayout eventParticipantView = (EventParticipantViewLayout)layout;
		eventParticipantView.setCanParticipantOptOutOfEvent(false);
		if(sctx.isCallerInRole(DrasRole.Admin.toString()) ||sctx.isCallerInRole(DrasRole.Operator.toString())
				||sctx.isCallerInRole(DrasRole.FacilityManager.toString())){
			eventParticipantView.setCanParticipantOptOutOfEvent(true);
		}
		
	}

}
