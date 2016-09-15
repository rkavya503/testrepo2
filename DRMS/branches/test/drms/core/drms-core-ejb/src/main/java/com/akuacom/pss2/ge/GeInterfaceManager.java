package com.akuacom.pss2.ge;

import javax.ejb.Local;
import javax.ejb.Remote;

public interface GeInterfaceManager {
	@Remote
    public interface R extends GeInterfaceManager {}

    @Local
    public interface L extends GeInterfaceManager {}
    
    GeConfiguration getGeConfiguration();
    
    int updateFTPConfig(String programName, String url, int shortInterval,
			int longInterval);
    
    void saveGeConfiguration(GeConfiguration conf);
}
