package com.akuacom.pss2.richsite.gridpoint;

import java.util.Date;

import com.akuacom.utils.lang.DateUtil;

public class GridPointRetrieveDataBackingBean {
	/** The time */
	private String time;
	
	/** The value */
	private String value;
	
	public GridPointRetrieveDataBackingBean(String time,String value){
		setTime(time);
		setValue(value);
	}
	
	public GridPointRetrieveDataBackingBean(Date time,Double value){
		setTime(DateUtil.format(time, GridPointConfigureBackingBeanValidator.timeFormatPattern));
		setValue(String.valueOf(value));
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
}
