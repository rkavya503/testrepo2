package com.honeywell.dras.vtn.api;

public enum IntervalDurationResolution {

	DAY("day"),
	HOUR("hour"),
	MINUTE("minute"),
	SECOND("second");

    private final String value;

    IntervalDurationResolution(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static IntervalDurationResolution fromValue(String v) {
        for (IntervalDurationResolution c: IntervalDurationResolution.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
