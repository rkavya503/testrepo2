package com.akuacom.pss2.userrole.layoutbuilder;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.akuacom.pss2.userrole.viewlayout.JSFClientLayout;
import com.akuacom.pss2.userrole.viewlayout.ViewLayout;
import com.akuacom.pss2.util.DrasRole;

@Stateless(mappedName="JSFClientLayoutBuilderBean")
public class JSFClientLayoutBuilderBean implements ViewLayoutBuilder.L{
	
	
		@Resource
		private SessionContext sctx;
		
		@Override
		public void build(ViewLayout layout) {
			if(!(layout instanceof JSFClientLayout))
			{
				//print log
				return;
			}
			JSFClientLayout jsfClientLayout = (JSFClientLayout)layout;
			jsfClientLayout.setUserAuthorized(true);
			
			if(sctx.isCallerInRole(DrasRole.Dispatcher.toString()) || sctx.isCallerInRole(DrasRole.Readonly.toString() ))
			{
				jsfClientLayout.setUserAuthorized(false);
			}
			
		}
		
	

}
