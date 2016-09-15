package com.honeywell.dras.vtn.api;

/**
 * EventStatus
 * @author sunil
 *
 */
public enum EventStatus {
	
	
    /**
     * No event pending
     * 
     */
    NONE("none"),

    /**
     * event pending in the far future. The exact definition of how far in the future this refers is dependent upon the market context, but typically means the next day.
     * 
     */
    FAR("far"),

    /**
     * event pending in the near future. The exact definition of how near in the future the pending event is active is dependent on the market context
     * 
     */
    NEAR("near"),

    /**
     * The event has been initiated and is currently active.
     * 
     */
    ACTIVE("active"),

    /**
     * The event has completed.
     * 
     */
    COMPLETED("completed"),

    /**
     * The event has been canceled.
     * 
     */
    CANCELLED("cancelled");
    private final String value;

    EventStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EventStatus fromValue(String v) {
        for (EventStatus c: EventStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
