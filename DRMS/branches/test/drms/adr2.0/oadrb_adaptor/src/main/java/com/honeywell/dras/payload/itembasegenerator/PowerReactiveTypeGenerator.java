package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;
import org.oasis_open.docs.ns.emix._2011._06.power.PowerReactiveType;

import com.honeywell.dras.vtn.api.report.ItemBase;
import com.honeywell.dras.vtn.api.report.PowerReactive;

public class PowerReactiveTypeGenerator implements ItemBaseFactory{

	private PowerReactive powerReactive;
	
	@Override
	public ItemBase getData() {		
		return powerReactive;
	}

	@Override
	public void setData(ItemBaseType oadrItemBase) {
		PowerReactiveType powerReactiveType = (PowerReactiveType) oadrItemBase;
		if(null == powerReactive)
			powerReactive = new PowerReactive();
		powerReactive.setItemDescription(powerReactiveType.getItemDescription().toString());
		powerReactive.setItemUnits(powerReactiveType.getItemUnits());
		powerReactive.setSiScaleCode(powerReactiveType.getSiScaleCode());
	}

}
