package com.akuacom.pss2.openadr2.opt;


public enum OptTargetType {
	
	
	MARKETCONTEXT("MarketContext"),
	PARTY("party"),
	RESOURCE("Resource"),
	EVENT("Event"),
	GROUP("Group"),
	VEN("Ven");
	
	private final String value;

	OptTargetType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}
	
	public static OptTargetType fromValue(String v) {
		for (OptTargetType c : OptTargetType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

}
