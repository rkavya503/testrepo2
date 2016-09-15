package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;
import org.oasis_open.docs.ns.emix._2011._06.power.EnergyReactiveType;

import com.honeywell.dras.vtn.api.report.EnergyReactive;
import com.honeywell.dras.vtn.api.report.ItemBase;

public class EnergyReactiveTypeGenerator implements ItemBaseFactory{

	private EnergyReactive energyReactive;
	
	@Override
	public ItemBase getData() {		
		return energyReactive;
	}

	@Override
	public void setData(ItemBaseType oadrItemBase) {
		EnergyReactiveType energyReactiveType = (EnergyReactiveType) oadrItemBase;
		if(null == energyReactive)
			energyReactive = new EnergyReactive();
		energyReactive.setItemDescription(energyReactiveType.getItemDescription().toString());
		energyReactive.setItemUnits(energyReactiveType.getItemUnits());
		energyReactive.setSiScaleCode(energyReactiveType.getSiScaleCode());
	}

}
