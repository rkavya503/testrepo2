package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;

import com.honeywell.dras.openadr2.payloads.b.CurrentType;
import com.honeywell.dras.vtn.api.report.ItemBase;

public class CurrentTypeGenerator implements ItemBaseFactory {
	
	private com.honeywell.dras.vtn.api.report.Current current;
	
	@Override
	public ItemBase getData() {
		return current;
	}

	@Override
	public void setData(ItemBaseType oadrItemBase) {
		CurrentType currentType = (CurrentType) oadrItemBase;
		if(null == current)
			current = new com.honeywell.dras.vtn.api.report.Current();
		current.setItemDescription(currentType.getItemDescription().toString());
		current.setItemUnits(currentType.getItemUnits());
		current.setSiScaleCode(currentType.getSiScaleCode());
	}

}
