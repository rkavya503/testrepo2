package com.akuacom.pss2.userrole.layoutbuilder;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.akuacom.pss2.userrole.viewlayout.ClientRuleViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ViewLayout;
import com.akuacom.pss2.util.DrasRole;

@Stateless(mappedName="ClientRuleLayoutBuilderBean")
public class ClientRuleLayoutBuilderBean implements ViewLayoutBuilder.L{
	
	@Resource
	private SessionContext sctx;
	
	@Override
	public void build(ViewLayout layout) {
		if(!(layout instanceof ClientRuleViewLayout)){
			return;
		}
		ClientRuleViewLayout clinetRule = (ClientRuleViewLayout)layout;
		if(sctx.isCallerInRole(DrasRole.Admin.name())) {
			buildClientRuleLayoutForAdmin(clinetRule);
		} else if(sctx.isCallerInRole(DrasRole.Operator.name())) {
			buildClientRuleLayoutForOperator(clinetRule);			
		}else if(sctx.isCallerInRole(DrasRole.FacilityManager.name())){
			buildClientRuleLayoutForFacilityManager(clinetRule);			
		}else if(sctx.isCallerInRole(DrasRole.Readonly.name())){
			buildClientRuleLayoutForReadOnly(clinetRule);			
		} else if(sctx.isCallerInRole(DrasRole.Dispatcher.name())){
			buildLayoutForDispatcher(clinetRule);
		}
	}	
	
	private void buildClientRuleLayoutForAdmin(ClientRuleViewLayout clinetRuleView) {
		//to render property
		clinetRuleView.setCanAddRule(true);
		clinetRuleView.setCanDeleteRule(true);
		clinetRuleView.setCanSaveRule(true);
		clinetRuleView.setCanGoDownRule(true);
		clinetRuleView.setCanGoUpRule(true);
		
		//to disable property
		clinetRuleView.setDeleteCheckboxEnable(false);
	}
	
	private void buildClientRuleLayoutForOperator(ClientRuleViewLayout clinetRuleView) {
		//to render property
		clinetRuleView.setCanAddRule(true);
		clinetRuleView.setCanDeleteRule(true);
		clinetRuleView.setCanSaveRule(true);
		clinetRuleView.setCanGoDownRule(true);
		clinetRuleView.setCanGoUpRule(true);
		
		//to disable property
		clinetRuleView.setDeleteCheckboxEnable(false);
	}
	
	private void buildClientRuleLayoutForFacilityManager(ClientRuleViewLayout clinetRuleView) {
		//to render property
		clinetRuleView.setCanAddRule(true);
		clinetRuleView.setCanDeleteRule(true);
		clinetRuleView.setCanSaveRule(true);
		clinetRuleView.setCanGoDownRule(true);
		clinetRuleView.setCanGoUpRule(true);
		
		//to disable property
		clinetRuleView.setDeleteCheckboxEnable(false);
	}
	
	private void buildClientRuleLayoutForReadOnly(ClientRuleViewLayout clinetRuleView) {
		//to render property
		clinetRuleView.setCanAddRule(false);
		clinetRuleView.setCanDeleteRule(false);
		clinetRuleView.setCanSaveRule(false);
		clinetRuleView.setCanGoDownRule(false);
		clinetRuleView.setCanGoUpRule(false);
		
		//to disable property
		clinetRuleView.setDeleteCheckboxEnable(true);
	}

	private void buildLayoutForDispatcher(ClientRuleViewLayout clinetRuleView) {
		//to render property
		clinetRuleView.setCanAddRule(false);
		clinetRuleView.setCanDeleteRule(false);
		clinetRuleView.setCanSaveRule(false);
		clinetRuleView.setCanGoDownRule(false);
		clinetRuleView.setCanGoUpRule(false);
		
		//to disable property
		clinetRuleView.setDeleteCheckboxEnable(true);
	}	

}
