package com.akuacom.pss2.userrole.layoutbuilder;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.akuacom.pss2.userrole.viewlayout.ClientViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ViewLayout;
import com.akuacom.pss2.util.DrasRole;

@Stateless(mappedName="ClientViewLayoutBuilderBean")
public class ClientViewLayoutBuilderBean implements ViewLayoutBuilder.L{
	
	@Resource
	private SessionContext sctx;
	
	@Override
	public void build(ViewLayout layout) {
		if(!(layout instanceof ClientViewLayout)){
			return;
		}
		ClientViewLayout clinetView = (ClientViewLayout)layout;
		if(sctx.isCallerInRole(DrasRole.Admin.name())) {
			buildLayoutForAdmin(clinetView);
		} else if(sctx.isCallerInRole(DrasRole.Operator.name())) {
			buildLayoutForOperator(clinetView);			
		}else if(sctx.isCallerInRole(DrasRole.FacilityManager.name())){
			buildLayoutForFacilityManager(clinetView);			
		}else if(sctx.isCallerInRole(DrasRole.Readonly.name())){
			buildLayoutForReadOnly(clinetView);			
		} else if(sctx.isCallerInRole(DrasRole.Dispatcher.name())){
			buildLayoutForDispatcher(clinetView);
		}
	}
	
	private void buildLayoutForAdmin(ClientViewLayout clinetView) {
		clinetView.setCanDeleteClient(true);
		clinetView.setCanClearClient(false);
		clinetView.setCanExport(false);
		clinetView.setCanExportContacts(false);
		clinetView.setCanSearchClient(false);
		clinetView.setSearchInResults(false);
		clinetView.setDeleteCheckBoxDisable(false);
	}
	
	private void buildLayoutForOperator(ClientViewLayout clinetView) {
		clinetView.setCanDeleteClient(true);
		clinetView.setCanClearClient(false);
		clinetView.setCanExport(false);
		clinetView.setCanExportContacts(false);
		clinetView.setCanSearchClient(false);
		clinetView.setSearchInResults(false);
		clinetView.setDeleteCheckBoxDisable(false);
	}
	
	private void buildLayoutForFacilityManager(ClientViewLayout clinetView) {
		
	}
	
	private void buildLayoutForReadOnly(ClientViewLayout clinetView) {
		clinetView.setCanDeleteClient(false);
		clinetView.setCanClearClient(false);
		clinetView.setCanExport(true);
		clinetView.setCanExportContacts(true);
		clinetView.setCanSearchClient(false);
		clinetView.setSearchInResults(false);
		clinetView.setDeleteCheckBoxDisable(true);
	}


	private void buildLayoutForDispatcher(ClientViewLayout clinetView) {
		clinetView.setCanDeleteClient(false);
		clinetView.setCanClearClient(false);
		clinetView.setCanExport(true);
		clinetView.setCanExportContacts(true);
		clinetView.setCanSearchClient(false);
		clinetView.setSearchInResults(false);
		clinetView.setDeleteCheckBoxDisable(true);
	}
}
