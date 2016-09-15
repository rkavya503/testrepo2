package com.akuacom.pss2.userrole.layoutbuilder;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.akuacom.pss2.userrole.viewlayout.FacDashClientViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ViewLayout;
import com.akuacom.pss2.util.DrasRole;

@Stateless(mappedName="FacDashClientViewLayoutBuilderBean")
public class FacDashClientViewLayoutBuilderBean implements ViewLayoutBuilder.L{
	
	@Resource
	private SessionContext sctx;
	
	@Override
	public void build(ViewLayout layout) {
		if(!(layout instanceof FacDashClientViewLayout)){
			return;
		}
		FacDashClientViewLayout clinetView = (FacDashClientViewLayout)layout;
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

	private void buildLayoutForAdmin(FacDashClientViewLayout clinetView) {
		clinetView.setAddClient(false);
		clinetView.setDeleteChecked(false);
		clinetView.setDeleteClinet(false);
	}	

	private void buildLayoutForOperator(FacDashClientViewLayout clinetView) {
		clinetView.setAddClient(false);
		clinetView.setDeleteChecked(false);
		clinetView.setDeleteClinet(false);
	}
	
	private void buildLayoutForFacilityManager(FacDashClientViewLayout clinetView) {
		clinetView.setAddClient(false);
		clinetView.setDeleteChecked(false);
		clinetView.setDeleteClinet(false);
	}

	private void buildLayoutForReadOnly(FacDashClientViewLayout clinetView) {
		clinetView.setAddClient(true);
		clinetView.setDeleteChecked(true);
		clinetView.setDeleteClinet(true);
	}

	private void buildLayoutForDispatcher(FacDashClientViewLayout clinetView) {
		clinetView.setAddClient(true);
		clinetView.setDeleteChecked(true);
		clinetView.setDeleteClinet(true);
	}
}
