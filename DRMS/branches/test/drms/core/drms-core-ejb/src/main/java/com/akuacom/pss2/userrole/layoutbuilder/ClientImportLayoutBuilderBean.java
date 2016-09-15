package com.akuacom.pss2.userrole.layoutbuilder;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.akuacom.pss2.userrole.viewlayout.ClientImportLayout;
import com.akuacom.pss2.userrole.viewlayout.ViewLayout;
import com.akuacom.pss2.util.DrasRole;

@Stateless(mappedName="ClientImportLayoutBuilderBean")
public class ClientImportLayoutBuilderBean implements ViewLayoutBuilder.L {
	
	@Resource
	private SessionContext sctx;
	
	@Override
	public void build(ViewLayout layout) {
		if(!(layout instanceof ClientImportLayout)){
			return;
		}
		ClientImportLayout clientImportView = (ClientImportLayout)layout;
		if(sctx.isCallerInRole(DrasRole.Admin.name())) {
			buildLayoutForAdmin(clientImportView);
		} else if(sctx.isCallerInRole(DrasRole.Operator.name())) {
			buildLayoutForOperator(clientImportView);			
		}else if(sctx.isCallerInRole(DrasRole.FacilityManager.name())){
			buildLayoutForFacilityManager(clientImportView);			
		}else if(sctx.isCallerInRole(DrasRole.Readonly.name())){
			buildLayoutForReadOnly(clientImportView);			
		} else if(sctx.isCallerInRole(DrasRole.Dispatcher.name())){
			buildLayoutForDispatcher(clientImportView);
		}
	}
	
	private void buildLayoutForAdmin(ClientImportLayout clientImportLayout){
		clientImportLayout.setFileUploadEnabled(true);
		clientImportLayout.setImportModalDone(true);
	}
	
	private void buildLayoutForOperator(ClientImportLayout clientImportLayout){
		clientImportLayout.setFileUploadEnabled(true);
		clientImportLayout.setImportModalDone(true);
	}
	
	
	private void buildLayoutForFacilityManager(ClientImportLayout clientImportLayout){
		
	}
	
	private void buildLayoutForReadOnly(ClientImportLayout clientImportLayout){
		clientImportLayout.setFileUploadEnabled(false);
		clientImportLayout.setImportModalDone(false);
	}
	
	private void buildLayoutForDispatcher(ClientImportLayout clientImportLayout){
		clientImportLayout.setFileUploadEnabled(false);
		clientImportLayout.setImportModalDone(false);
	}

}
