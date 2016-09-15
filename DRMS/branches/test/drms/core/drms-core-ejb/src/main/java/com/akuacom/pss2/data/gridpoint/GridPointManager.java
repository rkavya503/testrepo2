package com.akuacom.pss2.data.gridpoint;

import javax.ejb.Local;
import javax.ejb.Remote;


/**
 * the interface GridPointManager
 */
public interface GridPointManager {
	
    @Remote
    public interface R extends GridPointManager {   }
    @Local
    public interface L extends GridPointManager {   }

	void process();
    
}
