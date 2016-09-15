package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;

import com.honeywell.dras.vtn.api.report.ItemBase;

public interface ItemBaseFactory {
	
	public abstract ItemBase getData();
	
	public abstract void setData(ItemBaseType oadrItemBase);
}


