package com.akuacom.pss2.userrole.layoutbuilder;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.akuacom.pss2.userrole.viewlayout.ProgramViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ViewLayout;
import com.akuacom.pss2.util.DrasRole;


@Stateless(mappedName="ProgramViewLayoutBuilderBean")
public class ProgramViewLayoutBuilderBean implements ViewLayoutBuilder.L{

	@Resource
	private SessionContext sctx;
	@Override
	public void build(ViewLayout layout) {
		if(!(layout instanceof ProgramViewLayout)){
			//print log
			return;
		}
		
		ProgramViewLayout programViewLayout = (ProgramViewLayout)layout;
		if(sctx.isCallerInRole(DrasRole.Admin.toString())){
			buildLayoutForAdmin(programViewLayout);
		}
		else if(sctx.isCallerInRole(DrasRole.FacilityManager.toString())){
			buildLayoutForFacilityManager(programViewLayout);
			
		}
		else if(sctx.isCallerInRole(DrasRole.Readonly.toString())){
			buildLayoutForReadOnly(programViewLayout);
			
		}
		else if(sctx.isCallerInRole(DrasRole.Dispatcher.toString())){
			buildLayoutForDispatcher(programViewLayout);
			
		}else if(sctx.isCallerInRole(DrasRole.Operator.toString())){
			buildLayoutForOperator(programViewLayout);
		}
		
	}
	private void buildLayoutForAdmin(ProgramViewLayout programViewLayout){
		programViewLayout.setProgramActnEnabled(true);
		programViewLayout.setProgramAddEnabled(true);
		programViewLayout.setProgramCloneEnabled(true);
		programViewLayout.setProgramEditEnabled(true);
		programViewLayout.setProgramRemEnabled(true);
		programViewLayout.setProgramSelectEnabled(true);
		programViewLayout.setProgramPropsEnabled(true);
		programViewLayout.setProgramSignalEnabled(true);
		programViewLayout.setProgramSeasonEnabled(true);
		programViewLayout.setProgramRuleEnabled(true);
		programViewLayout.setProgramAddEventEnabled(true);
		programViewLayout.setProgramRtpEnabled(true);
		programViewLayout.setProgramExpEnabled(true);
	}
	private void buildLayoutForOperator(ProgramViewLayout programViewLayout){
		programViewLayout.setProgramActnEnabled(false);
		programViewLayout.setProgramAddEnabled(false);
		programViewLayout.setProgramCloneEnabled(false);
		programViewLayout.setProgramEditEnabled(false);
		programViewLayout.setProgramRemEnabled(false);
		programViewLayout.setProgramSelectEnabled(false);
		programViewLayout.setProgramPropsEnabled(false);
		programViewLayout.setProgramSignalEnabled(false);
		programViewLayout.setProgramSeasonEnabled(false);
		programViewLayout.setProgramRuleEnabled(false);
		programViewLayout.setProgramAddEventEnabled(false);
		programViewLayout.setProgramRtpEnabled(false);
		programViewLayout.setProgramExpEnabled(true);
	}
	private void buildLayoutForFacilityManager(ProgramViewLayout programViewLayout){
		programViewLayout.setProgramActnEnabled(true);
		programViewLayout.setProgramAddEnabled(true);
		programViewLayout.setProgramCloneEnabled(true);
		programViewLayout.setProgramEditEnabled(true);
		programViewLayout.setProgramRemEnabled(true);
		programViewLayout.setProgramSelectEnabled(true);
		programViewLayout.setProgramPropsEnabled(true);
		programViewLayout.setProgramSignalEnabled(true);
		programViewLayout.setProgramSeasonEnabled(true);
		programViewLayout.setProgramRuleEnabled(true);
		programViewLayout.setProgramAddEventEnabled(true);
		programViewLayout.setProgramRtpEnabled(false);
		programViewLayout.setProgramExpEnabled(false);
	}
	private void buildLayoutForReadOnly(ProgramViewLayout programViewLayout){
		programViewLayout.setProgramActnEnabled(false);
		programViewLayout.setProgramAddEnabled(false);
		programViewLayout.setProgramCloneEnabled(false);
		programViewLayout.setProgramEditEnabled(false);
		programViewLayout.setProgramRemEnabled(false);
		programViewLayout.setProgramSelectEnabled(false);
		programViewLayout.setProgramPropsEnabled(false);
		programViewLayout.setProgramSignalEnabled(false);
		programViewLayout.setProgramSeasonEnabled(false);
		programViewLayout.setProgramRuleEnabled(false);
		programViewLayout.setProgramAddEventEnabled(false);
		programViewLayout.setProgramRtpEnabled(false);
		programViewLayout.setProgramExpEnabled(false);
	}
	private void buildLayoutForDispatcher(ProgramViewLayout programViewLayout){
		programViewLayout.setProgramActnEnabled(false);
		programViewLayout.setProgramAddEnabled(false);
		programViewLayout.setProgramCloneEnabled(false);
		programViewLayout.setProgramEditEnabled(false);
		programViewLayout.setProgramRemEnabled(false);
		programViewLayout.setProgramSelectEnabled(false);
		programViewLayout.setProgramPropsEnabled(false);
		programViewLayout.setProgramSignalEnabled(false);
		programViewLayout.setProgramSeasonEnabled(false);
		programViewLayout.setProgramRuleEnabled(false);
		programViewLayout.setProgramAddEventEnabled(true);
		programViewLayout.setProgramRtpEnabled(false);
		programViewLayout.setProgramExpEnabled(false);
	}
	

}
