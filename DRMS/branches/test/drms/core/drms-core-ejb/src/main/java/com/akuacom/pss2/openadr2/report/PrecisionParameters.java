package com.akuacom.pss2.openadr2.report;

import java.io.Serializable;

public class PrecisionParameters implements Serializable {

	private static final long serialVersionUID = -4554009571565575857L;
	
    private String dataQuality;
    private Long confidence;
    private Float accuracy;
    
	public String getDataQuality() {
		return dataQuality;
	}

	public void setDataQuality(String dataQuality) {
		this.dataQuality = dataQuality;
	}

	public Long getConfidence() {
		return confidence;
	}

	public void setConfidence(Long confidence) {
		this.confidence = confidence;
	}

	public Float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Float accuracy) {
		this.accuracy = accuracy;
	}
}
