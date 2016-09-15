package com.honeywell.dras.vtn.api;


public enum SignalType {

	  /**
     * Signal indicates the amount to change (denominated in Itembase or in the EMIX Product) from what one would have used without the Signal. This may or may not be accompanied by a baseline. Payload Type Quantity
     * 
     */
    DELTA("delta"),

    /**
     * Signal indicates a Program Level. Payload Type is Program Level
     * 
     */
    LEVEL("level"),

    /**
     * Signal indicates a multiplier applied to the current rate of  delivery or usage (denominated in Itembase or in the EMIX Product) from what one would have used without the Signal. This may or may not be accompanied by a baseline. Payload Type is Float
     * 
     */
    MULTIPLIER("multiplier"),

    /**
     * Signal indicates the Price. Extended Price is the value multiplied by the number of units units (denominated in Itembase or in the EMIX Product). Payload Type is emix:price
     * 
     */
    PRICE("price"),

    /**
     * Signal indicates the Price Multiplier. Extended Price is the computed price (as described in EMIX) the value multiplied by the number of units units (denominated in Itembase or in the EMIX Product). Payload Type is emix:priceMultiplier
     * 
     */
    PRICE_MULTIPLIER("priceMultiplier"),

    /**
     * Signal indicates the Relative Price. Extended Price is the computed price (as described in EMIX) the value multiplied by the number of units units (denominated in Itembase or in the EMIX Product). Payload Type is emix:priceRelative
     * 
     */
    PRICE_RELATIVE("priceRelative"),

    /**
     * Signal indicates the Product for each interval. Payload Type is an EMIX Product Description
     * 
     */
    PRODUCT("product"),

    /**
     * Signal indicates a target amount of units (denominated in Itembase or in the EMIX Product). Payload Type is Quantity
     * 
     */
    SETPOINT("setpoint"),
    
    /**
     * Load Control Capacity
     * 
     */
    LOAD_CONTROL_CAPACITY("x-loadControlCapacity"),    
    
    /**
     * Load Control Level Offset
     * 
     */
    LOAD_CONTROL_LEVEL_OFFSET("x-loadControlLevelOffset"),  
    

    /**
     * Load Control Percent Offset
     * 
     */
    LOAD_CONTROL_PERCENT_OFFSET("x-loadControlPercentOffset"),  
    

    /**
     * Load Control Setpoint
     * 
     */
    LOAD_CONTROL_SETPOINT("x-loadControlSetpoint");   
    
     
    
    
    private final String value;

    SignalType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SignalType fromValue(String v) {
        for (SignalType c: SignalType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

	
}
