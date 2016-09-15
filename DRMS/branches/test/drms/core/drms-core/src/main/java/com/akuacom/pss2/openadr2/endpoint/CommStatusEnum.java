package com.akuacom.pss2.openadr2.endpoint;


public enum CommStatusEnum {
	ONLINE("Online"),
	OFFLINE("Offline"),
	UNKNOWN("Unknown"),
	MANUAL("Manual");
	
	private final String value;
	
	CommStatusEnum(String v) {
	    value = v;
	}
	
	public String value() {
	    return value;
	}
	
	public static CommStatusEnum fromValue(String v) {
	    for (CommStatusEnum c: CommStatusEnum.values()) {
	        if (c.value.equals(v)) {
	            return c;
	        }
	    }
	    throw new IllegalArgumentException(v);
	}

}
