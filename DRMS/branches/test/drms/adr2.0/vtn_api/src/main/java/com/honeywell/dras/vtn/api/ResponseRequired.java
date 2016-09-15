package com.honeywell.dras.vtn.api;


/**
 * ResponseRequired
 * @author sunil
 *
 */
public enum ResponseRequired {

    /**
     * Always send a repsonse for every event received.
     * 
     */
    
    ALWAYS("always"),

    /**
     * Never repsond.
     * 
     */
    NEVER("never");
    private final String value;

    ResponseRequired(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ResponseRequired fromValue(String v) {
        for (ResponseRequired c: ResponseRequired.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
