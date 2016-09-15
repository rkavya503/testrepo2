package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;
import org.oasis_open.docs.ns.emix._2011._06.power.EnergyApparentType;

import com.honeywell.dras.vtn.api.report.EnergyApparent;
import com.honeywell.dras.vtn.api.report.ItemBase;

public class EnergyApparentTypeGenerator implements ItemBaseFactory{

	private EnergyApparent energyApparent;
	
	@Override
	public ItemBase getData() {		
		return energyApparent;
	}

	@Override
	public void setData(ItemBaseType oadrItemBase) {
		EnergyApparentType energyApparentType = (EnergyApparentType) oadrItemBase;
		if(null == energyApparent)
			energyApparent = new EnergyApparent();
		energyApparent.setItemDescription(energyApparentType.getItemDescription().toString());
		energyApparent.setItemUnits(energyApparentType.getItemUnits());
		energyApparent.setSiScaleCode(energyApparentType.getSiScaleCode());
	}

}
