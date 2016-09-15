package com.honeywell.dras.vtn.api.registration;


public enum ProfileName {
	
	PROFILEOBIX("OBIX"),
	
	PROFILE1("1.0a"),
	
	PROFILE2A("2.0a"),

	PROFILE2B("2.0b");
	
    private final String value;

    ProfileName(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ProfileName fromValue(String v) {
        for (ProfileName c: ProfileName.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }


}
