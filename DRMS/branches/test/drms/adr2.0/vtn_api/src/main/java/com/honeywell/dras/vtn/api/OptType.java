package com.honeywell.dras.vtn.api;


/**
 * OptType
 * @author sunil
 *
 */
public enum OptType {

    OPT_IN("optIn"),

    OPT_OUT("optOut");
    private final String value;

    OptType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OptType fromValue(String v) {
        for (OptType c: OptType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

	
}
