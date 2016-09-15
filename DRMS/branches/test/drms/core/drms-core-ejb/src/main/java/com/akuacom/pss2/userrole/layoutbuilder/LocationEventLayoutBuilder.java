package com.akuacom.pss2.userrole.layoutbuilder;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.akuacom.pss2.userrole.viewlayout.EventLocationDetailLayout;
import com.akuacom.pss2.userrole.viewlayout.ViewLayout;
import com.akuacom.pss2.util.DrasRole;

@Stateless(mappedName="LocationEventLayoutBuilder")
public class LocationEventLayoutBuilder implements ViewLayoutBuilder.L{

	@Resource
	private SessionContext sctx;
	@Override
	public void build(ViewLayout layout) {
		if(!(layout instanceof EventLocationDetailLayout)){
			//print log
			return;
		}
		EventLocationDetailLayout locEventDetailLayout = (EventLocationDetailLayout)layout;
		locEventDetailLayout.setCanEndEventForLocation(false);
		if(sctx.isCallerInRole(DrasRole.Admin.toString()) || sctx.isCallerInRole(DrasRole.Dispatcher.toString())){
			locEventDetailLayout.setCanEndEventForLocation(true);
		}
	}

}
