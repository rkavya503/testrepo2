package com.akuacom.pss2.openadr2.opt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.akuacom.ejb.VersionedEntity;

@Entity
@Table(name="opttarget")
public class OptTarget extends VersionedEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="optrequestuuid")
	private OptRequest optRequest;
	private String targetType;
	private String targetValue;
	
	
	
	public OptRequest getOptRequest() {
		return optRequest;
	}
	public void setOptRequest(OptRequest optRequest) {
		this.optRequest = optRequest;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public List<String> getTargetValue() {
		List<String> strList = new ArrayList<String>();
		if((null != targetValue) || !("".equals(targetValue))){
			strList =  new ArrayList<String>(Arrays.asList(targetValue.split(",")));
		}
		
		return  strList;
	}
	public void setTargetValue(List<String> targetValueList) {
		System.out.println("Target Value : " + targetValueList);
		targetValue = StringUtils.join(targetValueList.toArray(), ",");
	}

}
