/**
 * 
 */
package com.akuacom.pss2.drw.core;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * the entity ZipCodeEntry
 */
@Entity
@Table(name = "zipcode_entry")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class ZipCodeEntry extends AbstractApplicationEntity {

	private static final long serialVersionUID = 4666660547031443747L;

    @ManyToOne
    @JoinColumn(name = "eventDetail_uuid")
	private EventDetail eventDetail;
	private String zipCode;
	
	private String cityName;
	private String countyNo;
	private String countyName;
	private String block;

	public EventDetail getEventDetail() {
		return eventDetail;
	}
	public void setEventDetail(EventDetail eventDetail) {
		this.eventDetail = eventDetail;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCountyNo() {
		return countyNo;
	}
	public void setCountyNo(String countyNo) {
		this.countyNo = countyNo;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	
}
