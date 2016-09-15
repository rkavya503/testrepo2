package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;
import org.oasis_open.docs.ns.emix._2011._06.power.EnergyRealType;

import com.honeywell.dras.vtn.api.report.Currency;
import com.honeywell.dras.vtn.api.report.ItemBase;

public class EnergyRealTypeGenerator implements ItemBaseFactory{

	private Currency energyReal;
	
	@Override
	public ItemBase getData() {		
		return energyReal;
	}

	@Override
	public void setData(ItemBaseType oadrItemBase) {
		EnergyRealType energyRealType = (EnergyRealType) oadrItemBase;
		if(null == energyReal)
			energyReal = new Currency();
		energyReal.setItemDescription(energyRealType.getItemDescription().toString());
		energyReal.setItemUnits(energyRealType.getItemUnits());
		energyReal.setSiScaleCode(energyRealType.getSiScaleCode());
	}

}
