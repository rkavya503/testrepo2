package com.akuacom.pss2.system.property;

public enum HeaderStyle {
    PRODUCTION_SERVER_UTILITY_OPERATOR("productionServerUtilityOperator"),
    PRODUCTION_SERVER_FACILITY_OPERATOR("productionServerFacilityOperator"),
    TEST_SERVER_UTILITY_OPERATOR("testServerUtilityOperator"),
    TEST_SERVER_FACILITY_OPERATOR("testServerFacilityOperator");

    private final String styleName;

    private HeaderStyle(String styleName) {
        this.styleName = styleName;
    }

    public String getStyleName() {
        return styleName;
    }
}