package com.akuacom.pss2.nssettings;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * NSSettingsManager is work for edit and update notification system global settings
 * 
 * @author e218290
 */

public interface NSSettingsManager {
    @Remote
    public interface R extends NSSettingsManager {}
    @Local
    public interface L extends NSSettingsManager {}

	/**
	 * Get notification system global settings
	 * 
	 * @return NSSettingsEntity
	 * Initial date Jul 01, 2010
	 */
	NSSettings getNSSettings();
	
	/**
	 * Save notification system global settings
	 * 
	 * @param nssettings
	 * Initial date Jul 01, 2010
	 */
	void saveNSSettings(NSSettings nssettings);
	
	
}
