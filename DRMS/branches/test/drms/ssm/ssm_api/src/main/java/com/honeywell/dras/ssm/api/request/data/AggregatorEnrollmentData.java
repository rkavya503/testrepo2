package com.honeywell.dras.ssm.api.request.data;

import java.util.List;

public class AggregatorEnrollmentData extends CustomerEnrollmentData{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean isNew;
	private List<CustomerEnrollmentData> childs;
	
	
	
	
	public Boolean getIsNew() {
		return isNew;
	}




	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}




	public List<CustomerEnrollmentData> getChilds() {
		return childs;
	}




	public void setChilds(List<CustomerEnrollmentData> childs) {
		this.childs = childs;
	}




	public CustomerEnrollmentData getAggregatorEnrollmentInfo(){
		
		return this;
	}

}
