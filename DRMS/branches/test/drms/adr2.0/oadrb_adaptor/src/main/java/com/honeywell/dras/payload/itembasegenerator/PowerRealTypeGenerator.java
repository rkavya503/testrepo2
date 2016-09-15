package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;
import org.oasis_open.docs.ns.emix._2011._06.power.PowerRealType;

import com.honeywell.dras.vtn.api.report.ItemBase;
import com.honeywell.dras.vtn.api.report.PowerReal;

public class PowerRealTypeGenerator implements ItemBaseFactory{

	private PowerReal powerReal;
	
	@Override
	public ItemBase getData() {		
		return powerReal;
	}

	@Override
	public void setData(ItemBaseType oadrItemBase) {
		PowerRealType powerRealType = (PowerRealType) oadrItemBase;
		if(null == powerReal)
			powerReal = new PowerReal();
		powerReal.setItemDescription(powerRealType.getItemDescription().toString());
		powerReal.setItemUnits(powerRealType.getItemUnits());
		powerReal.setSiScaleCode(powerRealType.getSiScaleCode());
	}

}
