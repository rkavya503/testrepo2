package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;
import org.w3._2005.atom.FeedType;

import com.honeywell.dras.openadr2.payloads.b.OadrGBItemBase;
import com.honeywell.dras.vtn.api.report.GBItemBase;
import com.honeywell.dras.vtn.api.report.ItemBase;

public class OadrGBItemBaseGenerator implements ItemBaseFactory{

	private GBItemBase gbItemBase;
	
	@Override
	public ItemBase getData() {		
		return gbItemBase;
	}

	@Override
	public void setData(ItemBaseType oadrItemBase) {
		OadrGBItemBase oadrGBItemBase = (OadrGBItemBase) oadrItemBase;
		if(null == gbItemBase)
			gbItemBase = new GBItemBase();
		
		FeedType oadrFeedType = oadrGBItemBase.getFeed();
		com.honeywell.dras.vtn.api.report.FeedType feedType = new com.honeywell.dras.vtn.api.report.FeedType();
		
		feedType.setBase(oadrFeedType.getBase());
		feedType.setLang(oadrFeedType.getLang());
		gbItemBase.setFeed(feedType);
	}

}
