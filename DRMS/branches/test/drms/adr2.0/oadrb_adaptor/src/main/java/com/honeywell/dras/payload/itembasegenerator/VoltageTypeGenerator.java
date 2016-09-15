package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;
import org.oasis_open.docs.ns.emix._2011._06.power.VoltageType;

import com.honeywell.dras.vtn.api.report.ItemBase;
import com.honeywell.dras.vtn.api.report.Voltage;

public class VoltageTypeGenerator implements ItemBaseFactory{

	private Voltage voltage;
	
	@Override
	public ItemBase getData() {		
		return voltage;
	}

	@Override
	public void setData(ItemBaseType oadrItemBase) {
		VoltageType voltageType = (VoltageType) oadrItemBase;
		if(null == voltage)
			voltage = new Voltage();
		voltage.setItemDescription(voltageType.getItemDescription().toString());
		voltage.setItemUnits(voltageType.getItemUnits());
		voltage.setSiScaleCode(voltageType.getSiScaleCode());
	}

}
