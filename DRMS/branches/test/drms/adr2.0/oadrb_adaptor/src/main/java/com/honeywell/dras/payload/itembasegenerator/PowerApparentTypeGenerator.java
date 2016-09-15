package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;
import org.oasis_open.docs.ns.emix._2011._06.power.PowerApparentType;

import com.honeywell.dras.vtn.api.report.ItemBase;
import com.honeywell.dras.vtn.api.report.PowerApparent;

public class PowerApparentTypeGenerator implements ItemBaseFactory{

	private PowerApparent powerApparent;
	
	@Override
	public ItemBase getData() {		
		return powerApparent;
	}

	@Override
	public void setData(ItemBaseType oadrItemBase) {
		PowerApparentType powerApparentType = (PowerApparentType) oadrItemBase;
		if(null == powerApparent)
			powerApparent = new PowerApparent();
		powerApparent.setItemDescription(powerApparentType.getItemDescription().toString());
		powerApparent.setItemUnits(powerApparentType.getItemUnits());
		powerApparent.setSiScaleCode(powerApparentType.getSiScaleCode());
	}

}
