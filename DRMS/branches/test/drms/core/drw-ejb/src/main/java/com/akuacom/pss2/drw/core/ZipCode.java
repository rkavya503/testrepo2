/**
 * 
 */
package com.akuacom.pss2.drw.core;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * the entity ZipCode
 */
@Entity
@Table(name = "zipcode")
@NamedQueries( {
	@NamedQuery(name = "ZipCode.getZipCodeByLocation", 
			query = "select e from ZipCode e where e.locationType = :locationType and e.locationNumber=:locationNumber"),
	@NamedQuery(name = "ZipCode.getZipCodeByLocationType", 
			query = "select distinct e from ZipCode e where e.locationType = :locationType"),
	@NamedQuery(name = "ZipCode.getLocationByZipCode", query = "select e from ZipCode e where e.zipCode = :zipCode")
	})
public class ZipCode extends AbstractLibraryEntirty {

	private static final long serialVersionUID = -5486612242350151403L;

	private String zipCode;
	private String locationType;
	private String locationNumber;
	private String locationName;
	private String cityName;
	private String countyNo;
	private String countyName;
	private String block;
	
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public String getLocationNumber() {
		return locationNumber;
	}
	public void setLocationNumber(String locationNumber) {
		this.locationNumber = locationNumber;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
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
