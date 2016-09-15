package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;

import com.honeywell.dras.openadr2.payloads.b.BaseUnitType;
import com.honeywell.dras.vtn.api.report.BaseUnit;
import com.honeywell.dras.vtn.api.report.ItemBase;

public class BaseUnitTypeGenerator implements ItemBaseFactory{

	private BaseUnit baseUnit;
	
	@Override
	public ItemBase getData() {
		return baseUnit;
	}

	@Override
	public void setData(ItemBaseType oadrItemBase) {
		BaseUnitType oadrBaseUnitType = (BaseUnitType) oadrItemBase;
		if(null == baseUnit)
			baseUnit = new BaseUnit();
		baseUnit.setItemDescription(oadrBaseUnitType.getItemDescription().toString());
		baseUnit.setItemUnits(oadrBaseUnitType.getItemUnits());
		baseUnit.setSiScaleCode(oadrBaseUnitType.getSiScaleCode());
	}	
}
