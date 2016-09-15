/**
 * 
 */
package com.akuacom.pss2.drw.eao;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.drw.core.Location;

/**
 * the interface LocationEAO
 */
public interface LocationEAO extends BaseEAO<Location>{
    @Remote
    public interface R extends LocationEAO {}
    @Local
    public interface L extends LocationEAO {}
    
	List<Location> getLocationByType(String type);

	List<Location> getLocation(String type, String number, String name);

	List<String> getBlock(String product, boolean status);
	List<Location> getLocation(List<String> type, String number, String name);

	Location getLocationByTypeAndNumber(String type, String number);
}
