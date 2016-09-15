/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.sc.BaseLineType.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data.usage;

/**
 * This is the type of how the BaseLine was calculated. To be filled by Ed.
 */
public enum BaseLineType {

	/**
	 * TODO
	 */
	THREE_TEN("ThreeTen", 3),

	/**
	 * TODO
	 */
	THREE_TEN_MA("ThreeTen", 3),

	/**
	 * TODO
	 */
	OAT_MA("Unknown", -1),

	/**
	 * TODO
	 */
	FIVE_TEN("FiveTen", 5),

	/**
	 * TODO
	 */
	TEN_TEN("TenTen", 10),

	/**
	 * TODO
	 */
	UNKNOWN_UNKNOWN("????", -1);
	
	

	private final String niceName;
	private final int sampleCount;

	BaseLineType(String name, int sampleCount) {
		this.niceName = name;
		this.sampleCount =sampleCount;
	}

	public int sampleCount() {
		return sampleCount;
	}
	
	public String niceName() {
		return niceName;
	}
	
	public static BaseLineType fromNiceName(String niceName) {
		for(BaseLineType blt : BaseLineType.values()) {
			if(blt.niceName().equalsIgnoreCase(niceName)) {
				return blt;
			}
		}
		return UNKNOWN_UNKNOWN;
	}
}
