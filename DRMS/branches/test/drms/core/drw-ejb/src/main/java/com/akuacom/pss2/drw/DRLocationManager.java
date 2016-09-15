/**
 * 
 */
package com.akuacom.pss2.drw;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.drw.core.Location;

/**
 * the interface DRDispatchManager
 */
public interface DRLocationManager {
    @Remote
    public interface R extends DRLocationManager {}
    @Local
    public interface L extends DRLocationManager {}

	List<Location> getLocation(String type);

	List<Location> getLocation(String type, String number, String name);
	List<Location> getLocation(List<String> type, String number, String name);

	List<String> getBlockNames(String product);
}
