package com.honeywell.dras.payload.itembasegenerator;

import org.oasis_open.docs.ns.emix._2011._06.ItemBaseType;

import com.honeywell.dras.vtn.api.report.ItemBase;

public class ItemBaseGenerator {

	public ItemBase getItemBase(ItemBaseType oadrItemBaseType,String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		
		Class<?> name = Class.forName("com.honeywell.dras.payload.itembasegenerator." +className+ "Generator");
		Object newInstance = name.newInstance();
		
		((ItemBaseFactory)newInstance).setData(oadrItemBaseType);
		
		return ((ItemBaseFactory)newInstance).getData();
	}
	
}
