
package com.akuacom.pss2.util;

import java.util.UUID;

public class Openadr2Util {
	public static final String REQUEST_ID_PREFIX = "AKUACOM.7.REQ:";
	public static final String REGISTRATION_ID_PREFIX = "AKUACOM.7.REG:";
	public static final String VEN_ID_PREFIX = "AKUACOM.7.VEN.ID:";
	
	public static String generateVenID(){
		return VEN_ID_PREFIX + UUID.randomUUID().toString();
	}
	public static String generateRequestID(){
		return REQUEST_ID_PREFIX + UUID.randomUUID().toString();
	}
	public static String generateRegistrationID(){
		return REGISTRATION_ID_PREFIX + UUID.randomUUID().toString();
	}
}
