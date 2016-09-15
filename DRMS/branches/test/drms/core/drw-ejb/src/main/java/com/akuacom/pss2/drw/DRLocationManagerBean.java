/**
 * 
 */
package com.akuacom.pss2.drw;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.ejb3.annotation.RemoteBinding;

import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.eao.LocationEAO;

/**
 * the class DRLocationManagerBean
 */
@Stateless
@LocalBinding(jndiBinding="dr-pro/DRLocationManager/local")
@RemoteBinding(jndiBinding="dr-pro/DRLocationManager/remote")
public class DRLocationManagerBean implements DRLocationManager.L, DRLocationManager.R {
	
	@EJB
	LocationEAO.L locationEAO;

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.drw.DRLocationManager#getLocation(java.lang.String)
	 */
	@Override
	public List<Location> getLocation(String type) {
		return locationEAO.getLocationByType(type);
	}

	@Override
	public List<Location> getLocation(String type, String number, String name){
		return locationEAO.getLocation(type, number, name);
	}
	@Override
	public List<Location> getLocation(List<String> type, String number,
			String name) {
		return locationEAO.getLocation(type, number, name);
	}

	@Override
	public List<String> getBlockNames(String product) {
		return locationEAO.getBlock(product, true);
	}
}
