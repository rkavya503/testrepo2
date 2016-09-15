/**
 * 
 */
package com.akuacom.pss2.drw.util;

import java.util.HashMap;
import java.util.Map;

/**
 * the class ProductConverter
 */
public class ProductConverter {
	
	public static Map<String, String> getProductMap() {
		Map<String, String> map=new HashMap<String, String>();
		map.put(AdminProduct.SDP.name(), "Residential");
		map.put(AdminProduct.APS.name(), "Commercial Base");
		map.put(AdminProduct.APS_E.name(), "Commercial Enhanced");
		
		return map;
	}
	
	public static String toProduct(String rate){
		return getProductMap().get(rate);
	}
	
	public static String toRate(String product) {
		Map<String, String> map= getProductMap();
		for (String rate:map.keySet()) {
			if (map.get(rate).equals(product))
				return rate;
		}
		return null;
	}
}
