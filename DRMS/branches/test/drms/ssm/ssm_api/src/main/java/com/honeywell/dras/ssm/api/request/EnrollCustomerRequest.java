package com.honeywell.dras.ssm.api.request;

import javax.xml.bind.annotation.XmlRootElement;
import com.honeywell.dras.ssm.api.request.data.CustomerEnrollmentData;

@XmlRootElement
public class EnrollCustomerRequest {
	private CustomerEnrollmentData customerEnrollmentData;

	public CustomerEnrollmentData getCustomerEnrollmentData() {
		return customerEnrollmentData;
	}

	public void setCustomerEnrollmentData(
			CustomerEnrollmentData customerEnrollmentData) {
		this.customerEnrollmentData = customerEnrollmentData;
	}
	
	

}
