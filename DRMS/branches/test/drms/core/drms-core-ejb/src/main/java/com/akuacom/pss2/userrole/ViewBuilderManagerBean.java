package com.akuacom.pss2.userrole;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.pss2.userrole.viewlayout.BidEventViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ClientImportLayout;
import com.akuacom.pss2.userrole.viewlayout.ClientRuleViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ClientViewLayout;
import com.akuacom.pss2.userrole.viewlayout.EventLocationDetailLayout;
import com.akuacom.pss2.userrole.viewlayout.EventParticipantViewLayout;
import com.akuacom.pss2.userrole.viewlayout.EventViewLayout;
import com.akuacom.pss2.userrole.viewlayout.FacdashHeaderLayout;
import com.akuacom.pss2.userrole.viewlayout.JSFClientOfflineConfigLayout;
import com.akuacom.pss2.userrole.viewlayout.JSFContactLayout;
import com.akuacom.pss2.userrole.viewlayout.FacDashClientViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ParticipantImportLayout;
import com.akuacom.pss2.userrole.viewlayout.ParticipantViewLayout;
import com.akuacom.pss2.userrole.viewlayout.ProgramViewLayout;
import com.akuacom.pss2.userrole.viewlayout.JSFClientLayout;


@Stateless(mappedName="ViewBuilderManagerBean")
public class ViewBuilderManagerBean implements ViewBuilderManager.L, ViewBuilderManager.R{
	
	@EJB
	private ViewLayoutBuilderFactory.L builderFactory;

	@Override
	public void buildProgramViewLayout(ProgramViewLayout layout) {
		builderFactory.getLayoutBuilder("ProgramViewLayoutBuilderBean").build(layout);
	}

	@Override
	public void buildEventViewLayout(EventViewLayout layout) {
		builderFactory.getLayoutBuilder("EventViewLayoutBuilderBean").build(layout);
		
	}

	@Override
	public void buildParticipantViewLayout(ParticipantViewLayout layout) {
		builderFactory.getLayoutBuilder("ParticipantViewLayoutBuilderBean").build(layout);
		
	}

	@Override
	public void buildClientViewLayout(ClientViewLayout layout) {
		builderFactory.getLayoutBuilder("ClientViewLayoutBuilderBean").build(layout);
		
	}


	@Override
	public void buildFacdashHeaderLayout(FacdashHeaderLayout layout) {
		builderFactory.getLayoutBuilder("FacdashHeaderLayoutBuilderBean").build(layout);
		
	}
	@Override
	public void buildJSFClientLayout(JSFClientLayout layout)
	{
		builderFactory.getLayoutBuilder("JSFClientLayoutBuilderBean").build(layout);	
	}

	@Override
	public void buildParticipantImportLayout(ParticipantImportLayout layout) {
		builderFactory.getLayoutBuilder("ParticipantImportLayoutBuilderBean").build(layout);
	}



	@Override
	public void buildJSFContactLayout(JSFContactLayout layout) 
	{
		builderFactory.getLayoutBuilder("JSFContactLayoutBuilderBean").build(layout);		
	}


	@Override
	public void buildClientImportLayout(ClientImportLayout layout) {
		builderFactory.getLayoutBuilder("ClientImportLayoutBuilderBean").build(layout);
	}

	@Override
	public void buildFacClinetLayout(FacDashClientViewLayout layout) {
		builderFactory.getLayoutBuilder("FacDashClientViewLayoutBuilderBean").build(layout);
	}

	@Override
	public void buildBidEventDetailViewLayout(BidEventViewLayout layout) {
		builderFactory.getLayoutBuilder("BidEventDetailLayoutBuilderBean").build(layout);
		
	}

	@Override
	public void buildEventLocationDetailViewLayout(
			EventLocationDetailLayout layout) {
		builderFactory.getLayoutBuilder("LocationEventLayoutBuilder").build(layout);
		
	}
	@Override
	public void buildEventParticipantViewLayout(
			EventParticipantViewLayout layout) {
		builderFactory.getLayoutBuilder("EventParticipantLayoutBuilder").build(layout);
		
	}
	@Override
	public void buildClientRuleViewLayout(ClientRuleViewLayout layout) {
		builderFactory.getLayoutBuilder("ClientRuleLayoutBuilderBean").build(layout);
	}

	@Override
	public void buildJSFClientOfflineConfigBackingBeanLayout(
			JSFClientOfflineConfigLayout layout) 
	{
		builderFactory.getLayoutBuilder("JSFClientOfflineConfigBackingBuilderBean").build(layout);
		
	}
}
