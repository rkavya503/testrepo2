package com.akuacom.pss2.userrole.layoutbuilder;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.akuacom.pss2.userrole.viewlayout.FacdashHeaderLayout;
import com.akuacom.pss2.userrole.viewlayout.ViewLayout;
import com.akuacom.pss2.util.DrasRole;

@Stateless(mappedName="FacdashHeaderLayoutBuilderBean")
public class FacdashHeaderLayoutBuilderBean implements ViewLayoutBuilder.L{

	@Resource
	private SessionContext sctx;
	
	@Override
	public void build(ViewLayout layout) {
		if(!(layout instanceof FacdashHeaderLayout)){
			//print log
			return;
		}
		FacdashHeaderLayout fascdashViewLayoutView = (FacdashHeaderLayout)layout;
		fascdashViewLayoutView.setEnableClientOptionTab(true);
		fascdashViewLayoutView.setEnableClientTestTab(true);
		fascdashViewLayoutView.setEnableCompleteInstallation(true);
		
		if(sctx.isCallerInRole(DrasRole.Dispatcher.toString()))
		{
			fascdashViewLayoutView.setEnableClientOptionTab(false);
			fascdashViewLayoutView.setEnableCompleteInstallation(false);
			
		}else if(sctx.isCallerInRole(DrasRole.Readonly.toString() ))
		{
			fascdashViewLayoutView.setEnableClientOptionTab(false);
			fascdashViewLayoutView.setEnableClientTestTab(false);
			fascdashViewLayoutView.setEnableCompleteInstallation(false);
		}
		
	}

}
