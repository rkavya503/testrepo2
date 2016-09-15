package com.akuacom.pss2.userrole.layoutbuilder;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.akuacom.pss2.userrole.viewlayout.JSFContactLayout;
import com.akuacom.pss2.userrole.viewlayout.ViewLayout;
import com.akuacom.pss2.util.DrasRole;

@Stateless(mappedName="JSFContactLayoutBuilderBean")
public class JSFContactLayoutBuilderBean implements ViewLayoutBuilder.L{
	
	@Resource
	private SessionContext sctx;
	
	@Override
	public void build(ViewLayout layout) {
		if(!(layout instanceof JSFContactLayout)){
			//print log
			return;
		}
		JSFContactLayout jsfContactLayout= (JSFContactLayout)layout;
		jsfContactLayout.setUserAuthorized(false);
		
		if(sctx.isCallerInRole(DrasRole.Dispatcher.toString()) || sctx.isCallerInRole(DrasRole.Readonly.toString() ))
		{
			jsfContactLayout.setUserAuthorized(true);
		}
		
	}

}
