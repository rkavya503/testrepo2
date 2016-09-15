package com.akuacom.pss2.userrole.layoutbuilder;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.akuacom.pss2.userrole.viewlayout.BidEventViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ProgramViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ViewLayout;
import com.akuacom.pss2.util.DrasRole;

@Stateless(mappedName="BidEventDetailLayoutBuilderBean")
public class BidEventDetailLayoutBuilderBean implements ViewLayoutBuilder.L{

	@Resource
	private SessionContext sctx;
	@Override
	public void build(ViewLayout layout) {
		if(!(layout instanceof BidEventViewLayout)){
			//print log
			return;
		}
		BidEventViewLayout bidEventViewLayout = (BidEventViewLayout)layout;
		bidEventViewLayout.setCanEditBid(false);
		if(sctx.isCallerInRole(DrasRole.Admin.toString()) || sctx.isCallerInRole(DrasRole.Operator.toString())
				|| sctx.isCallerInRole(DrasRole.FacilityManager.toString())){
			bidEventViewLayout.setCanEditBid(true);
		}
		
	}

}
