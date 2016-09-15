package com.honeywell.dras.vtn.api;

/**
 * 
 * @author sunil
 *
 */
public enum CommsStatusType {
	
    ONLINE("online"),

    OFFLINE("offline");
    private final String value;

    CommsStatusType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CommsStatusType fromValue(String v) {
        for (CommsStatusType c: CommsStatusType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }


}
