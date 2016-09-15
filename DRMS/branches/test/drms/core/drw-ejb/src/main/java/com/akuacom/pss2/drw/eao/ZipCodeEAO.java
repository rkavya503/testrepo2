/**
 * 
 */
package com.akuacom.pss2.drw.eao;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.core.ZipCode;

/**
 * the interface ZipCodeEAO
 */
public interface ZipCodeEAO extends BaseEAO<ZipCode>{
    @Remote
    public interface R extends ZipCodeEAO {}
    @Local
    public interface L extends ZipCodeEAO {}
    
	List<Location> getLocationByZipCode(String ZipCode);
    
	List<ZipCode> getZipCodeByLocation(Location location);
	
	List<ZipCode> getZipCodeByLocation(String locationType, String locationNumber);

	List<ZipCode> getZipCodeByLocationType(String locationType);
}
