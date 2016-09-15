package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;

import com.honeywell.dras.openadr2.payloads.b.CurrencyType;
import com.honeywell.dras.vtn.api.report.Currency;
import com.honeywell.dras.vtn.api.report.ItemBase;

public class CurrencyTypeGenerator implements ItemBaseFactory{

	private Currency currency;
	
	@Override
	public ItemBase getData() {		
		return currency;
	}

	@Override
	public void setData(ItemBaseType oadrItemBase) {
		CurrencyType localCurrencyType = (CurrencyType) oadrItemBase;
		if(null == currency)
			currency = new Currency();
		currency.setItemDescription(localCurrencyType.getItemDescription().toString());
		currency.setItemUnits(localCurrencyType.getItemUnits().value());
		currency.setSiScaleCode(localCurrencyType.getSiScaleCode());
	}

}
