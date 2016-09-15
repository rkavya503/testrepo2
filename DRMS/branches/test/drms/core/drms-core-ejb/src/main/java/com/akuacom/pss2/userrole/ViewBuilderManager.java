package com.akuacom.pss2.userrole;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.userrole.viewlayout.BidEventViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ClientImportLayout;
import com.akuacom.pss2.userrole.viewlayout.ClientRuleViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ClientViewLayout;
import com.akuacom.pss2.userrole.viewlayout.EventLocationDetailLayout;
import com.akuacom.pss2.userrole.viewlayout.EventParticipantViewLayout;
import com.akuacom.pss2.userrole.viewlayout.EventViewLayout;
import com.akuacom.pss2.userrole.viewlayout.FacDashClientViewLayout;
import com.akuacom.pss2.userrole.viewlayout.FacdashHeaderLayout;
import com.akuacom.pss2.userrole.viewlayout.JSFClientLayout;
import com.akuacom.pss2.userrole.viewlayout.JSFClientOfflineConfigLayout;
import com.akuacom.pss2.userrole.viewlayout.JSFContactLayout;
import com.akuacom.pss2.userrole.viewlayout.ParticipantImportLayout;
import com.akuacom.pss2.userrole.viewlayout.ParticipantViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ProgramViewLayout;


public interface ViewBuilderManager {
	
	@Remote
	public interface R extends ViewBuilderManager {}

	@Local
	public interface L extends ViewBuilderManager {}
	
	public void buildProgramViewLayout(ProgramViewLayout layout);
	public void buildEventViewLayout(EventViewLayout layout);
	public void buildParticipantViewLayout(ParticipantViewLayout layout);
	public void buildClientViewLayout(ClientViewLayout layout);

	public void buildFacdashHeaderLayout(FacdashHeaderLayout layout);
	public void buildJSFClientLayout(JSFClientLayout layout);
	public void buildJSFContactLayout(JSFContactLayout layout);

	public void buildParticipantImportLayout(ParticipantImportLayout layout);
	public void buildClientImportLayout(ClientImportLayout layout);
	public void buildFacClinetLayout(FacDashClientViewLayout layout);
	public void buildBidEventDetailViewLayout(BidEventViewLayout layout);
	public void buildEventLocationDetailViewLayout(EventLocationDetailLayout layout);
	public void buildEventParticipantViewLayout(EventParticipantViewLayout layout);
	public void buildClientRuleViewLayout(ClientRuleViewLayout layout);
	public void buildJSFClientOfflineConfigBackingBeanLayout(JSFClientOfflineConfigLayout layout);
}
