package com.honeywell.dras.vtn.api.registration;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum Result {
	
	SUCCESS("SUCCESS"),

	FAILURE("FAILURE");
	
    private final String value;

    Result(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Result fromValue(String v) {
        for (Result c: Result.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }


}
