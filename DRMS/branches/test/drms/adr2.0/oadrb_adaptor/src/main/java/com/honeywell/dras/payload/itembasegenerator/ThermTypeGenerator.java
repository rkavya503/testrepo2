package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;

import com.honeywell.dras.openadr2.payloads.b.ThermType;
import com.honeywell.dras.vtn.api.report.ItemBase;
import com.honeywell.dras.vtn.api.report.Therm;

public class ThermTypeGenerator implements ItemBaseFactory{

	private Therm therm;
	
	@Override
	public ItemBase getData() {		
		return therm;
	}

	@Override
	public void setData(ItemBaseType oadrItemBase) {
		ThermType thermType = (ThermType) oadrItemBase;
		if(null == therm)
			therm = new Therm();
		therm.setItemDescription(thermType.getItemDescription().toString());
		therm.setItemUnits(thermType.getItemUnits());	
		therm.setSiScaleCode(thermType.getSiScaleCode());
	}

}
