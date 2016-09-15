package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;

import com.honeywell.dras.openadr2.payloads.b.TemperatureType;
import com.honeywell.dras.vtn.api.report.ItemBase;
import com.honeywell.dras.vtn.api.report.Temperature;

public class TemperatureTypeGenerator implements ItemBaseFactory{

	private Temperature temperature;
	
	@Override
	public ItemBase getData() {		
		return temperature;
	}

	@Override
	public void setData(ItemBaseType oadrItemBase) {
		TemperatureType temperatureType = (TemperatureType) oadrItemBase;
		if(null == temperature)
			temperature = new Temperature();
		temperature.setItemDescription(temperatureType.getItemDescription().toString());
		temperature.setItemUnits(temperatureType.getItemUnits().value());
		temperature.setSiScaleCode(temperatureType.getSiScaleCode());	
	}

}
