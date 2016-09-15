package com.akuacom.pss2.openadr2.report;

import java.io.Serializable;
import java.util.Date;

import com.akuacom.pss2.openadr2.report.DataSetDefEnums.DataSetOwnerType;

public class TelemetryDataBusData implements Serializable {

	private static final long serialVersionUID = -4554009571565575857L;
	
	private com.akuacom.pss2.openadr2.report.DataSetDefEnums.DataSetOwnerType ownerType; 
	private String ownerEntityID;
	private String definitionName;
	private Date entryTime;
	private Double doubleValue;
	private Integer intValue;
	private Boolean booleanValue;
    private String stringValue;
    private boolean preserveConstantLevelEntries = true;
    private boolean preserveFutureEntries = true;
    private boolean continuityBreak = false;
	private PrecisionParameters precisionParameters;
    /**
     * @return the ownerType
     */
    public DataSetOwnerType getOwnerType() {
        return ownerType;
    }

    /**
     * @param ownerType the ownerType to set
     */
    public void setOwnerType(DataSetOwnerType ownerType) {
        this.ownerType = ownerType;
    }

    /**
     * @return the ownerEntityID
     */
    public String getOwnerEntityID() {
        return ownerEntityID;
    }

    /**
     * @param ownerEntityID the ownerEntityID to set
     */
    public void setOwnerEntityID(String ownerEntityID) {
        this.ownerEntityID = ownerEntityID;
    }

    /**
     * @return the definitionName
     */
    public String getDefinitionName() {
        return definitionName;
    }

    /**
     * @param definitionName the definitionName to set
     */
    public void setDefinitionName(String definitionName) {
        this.definitionName = definitionName;
    }

    /**
     * @return the entryTime
     */
    public Date getEntryTime() {
        return entryTime;
    }

    /**
     * @param entryTime the entryTime to set
     */
    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    /**
     * @return the doubleValue
     */
    public Double getDoubleValue() {
        return doubleValue;
    }

    /**
     * @param doubleValue the doubleValue to set
     */
    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    /**
     * @return the intValue
     */
    public Integer getIntValue() {
        return intValue;
    }

    /**
     * @param intValue the intValue to set
     */
    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    /**
     * @return the booleanValue
     */
    public Boolean getBooleanValue() {
        return booleanValue;
    }

    /**
     * @param booleanValue the booleanValue to set
     */
    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    
	@Override
	public String toString() {
		return "TelemetryDataBusData [ownerType=" + getOwnerType()
				+ ", ownerEntityID=" + getOwnerEntityID() + ", definitionName="
				+ getDefinitionName() + ", entryTime=" + getEntryTime()
				+ ", doubleValue=" + getDoubleValue() + ", intValue=" + getIntValue()
				+ ", booleanValue=" + getBooleanValue() + "]";
	}    

    /**
     * @return the continuityBreak
     */
    public boolean isContinuityBreak() {
        return continuityBreak;
    }

    /**
     * @param continuityBreak the continuityBreak to set
     */
    public void setContinuityBreak(boolean continuityBreak) {
        this.continuityBreak = continuityBreak;
    }

    /**
     * @return the preserveConstantLevelEntries
     */
    public boolean isPreserveConstantLevelEntries() {
        return preserveConstantLevelEntries;
    }

    /**
     * @param preserveConstantLevelEntries the preserveConstantLevelEntries to set
     */
    public void setPreserveConstantLevelEntries(boolean preserveConstantLevelEntries) {
        this.preserveConstantLevelEntries = preserveConstantLevelEntries;
    }

    /**
     * @return the stringValue
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * @param stringValue the stringValue to set
     */
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    /**
     * @return the preserveFutureEntries
     */
    public boolean isPreserveFutureEntries() {
        return preserveFutureEntries;
    }

    /**
     * @param preserveFutureEntries the preserveFutureEntries to set
     */
    public void setPreserveFutureEntries(boolean preserveFutureEntries) {
        this.preserveFutureEntries = preserveFutureEntries;
    }

	public PrecisionParameters getPrecisionParameters() {
		return precisionParameters;
	}

	public void setPrecisionParameters(PrecisionParameters precisionParameters) {
		this.precisionParameters = precisionParameters;
	}

}
