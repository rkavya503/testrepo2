package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;

import com.honeywell.dras.openadr2.payloads.b.FrequencyType;
import com.honeywell.dras.vtn.api.report.Currency;
import com.honeywell.dras.vtn.api.report.ItemBase;

public class FrequencyTypeGenerator implements ItemBaseFactory{

	private Currency frequency;
	
	@Override
	public ItemBase getData() {		
		return frequency;
	}

	@Override
	public void setData(ItemBaseType oadrItemBase) {
		FrequencyType frequencyType = (FrequencyType) oadrItemBase;
		if(null == frequency)
			frequency = new Currency();
		frequency.setItemDescription(frequencyType.getItemDescription().toString());
		frequency.setItemUnits(frequencyType.getItemUnits());
		frequency.setSiScaleCode(frequencyType.getSiScaleCode());
	}

}
