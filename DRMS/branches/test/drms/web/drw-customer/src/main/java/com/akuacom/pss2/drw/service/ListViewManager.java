package com.akuacom.pss2.drw.service;

import java.util.List;

public interface ListViewManager {
	/**
	 * Retrieve county list from system
	 * @return
	 */
	public List<String> getCountyList();
	
	/**
	 * Retrieve city list by county parameter from system
	 * @param county
	 * @return
	 */
	public List<String> getCityList(String county);
	
	
}
