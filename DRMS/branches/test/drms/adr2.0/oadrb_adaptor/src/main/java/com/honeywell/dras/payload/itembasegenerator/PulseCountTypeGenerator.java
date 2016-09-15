package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;

import com.honeywell.dras.openadr2.payloads.b.PulseCountType;
import com.honeywell.dras.vtn.api.report.ItemBase;
import com.honeywell.dras.vtn.api.report.PulseCount;

public class PulseCountTypeGenerator implements ItemBaseFactory{

	private PulseCount pulseCount;
	
	@Override
	public ItemBase getData() {		
		return pulseCount;
	}

	@Override
	public void setData(ItemBaseType oadrItemBase) {
		PulseCountType pulseCountType = (PulseCountType) oadrItemBase;
		if(null == pulseCount)
			pulseCount = new PulseCount();
		pulseCount.setItemDescription(pulseCountType.getItemDescription().toString());
		pulseCount.setItemUnits(pulseCountType.getItemUnits());
		pulseCount.setSiScaleCode(String.valueOf(pulseCountType.getPulseFactor()));
	}
}
