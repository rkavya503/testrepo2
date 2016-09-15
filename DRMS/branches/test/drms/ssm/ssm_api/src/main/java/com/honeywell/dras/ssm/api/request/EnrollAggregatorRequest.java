package com.honeywell.dras.ssm.api.request;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.ssm.api.request.data.AggregatorEnrollmentData;

@XmlRootElement
public class EnrollAggregatorRequest {
	
	private AggregatorEnrollmentData aggregatorEnrollmentData;

	public AggregatorEnrollmentData getAggregatorEnrollmentData() {
		return aggregatorEnrollmentData;
	}

	public void setAggregatorEnrollmentData(
			AggregatorEnrollmentData aggregatorEnrollmentData) {
		this.aggregatorEnrollmentData = aggregatorEnrollmentData;
	}
	
	

}
