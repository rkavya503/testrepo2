package com.akuacom.pss2.userrole.layoutbuilder;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.akuacom.pss2.userrole.viewlayout.JSFClientOfflineConfigLayout;
import com.akuacom.pss2.userrole.viewlayout.ViewLayout;
import com.akuacom.pss2.util.DrasRole;

@Stateless(mappedName="JSFClientOfflineConfigBackingBuilderBean")
public class JSFClientOfflineConfigBackingBuilderBean implements ViewLayoutBuilder.L{

	@Resource
	private SessionContext sctx;
	
	@Override
	public void build(ViewLayout layout) 
	{
		if(!(layout instanceof JSFClientOfflineConfigLayout))
		{
			//print log
			return;
		}
		
		JSFClientOfflineConfigLayout jsfClientOfflineConfigBackingBeanLayout = (JSFClientOfflineConfigLayout)layout;
		jsfClientOfflineConfigBackingBeanLayout.setClientReportOfflineConfigurationEnabled(true);
		jsfClientOfflineConfigBackingBeanLayout.setClientOfflineSummerNotificationEnabled(true);
		jsfClientOfflineConfigBackingBeanLayout.setClientOfflineWinterNotificationEnabled(true);
		if(sctx.isCallerInRole(DrasRole.Dispatcher.toString()) || sctx.isCallerInRole(DrasRole.Readonly.toString() ))
		{
			jsfClientOfflineConfigBackingBeanLayout.setClientReportOfflineConfigurationEnabled(false);
			jsfClientOfflineConfigBackingBeanLayout.setClientOfflineSummerNotificationEnabled(false);
			jsfClientOfflineConfigBackingBeanLayout.setClientOfflineWinterNotificationEnabled(false);
		}
		
	}

}
