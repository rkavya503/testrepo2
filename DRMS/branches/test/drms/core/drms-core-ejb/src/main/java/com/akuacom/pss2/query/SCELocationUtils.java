package com.akuacom.pss2.query;

public class SCELocationUtils {
	public static SCELocationType getLocationType(String programClass){
		if("BIP".equals(programClass)){
			return SCELocationType.ABank;
		}
		//TODO
		return SCELocationType.Slap;
	}
	
	
}
