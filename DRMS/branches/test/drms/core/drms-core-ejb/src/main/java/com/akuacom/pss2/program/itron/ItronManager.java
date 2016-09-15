/**
 * 
 */
package com.akuacom.pss2.program.itron;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.program.Program;

/**
 * the interface APXManagerBean
 * 
 */
public interface ItronManager {
    @Remote
    public interface R extends ItronManager {   }
    @Local
    public interface L extends ItronManager {   }
    
	void createEvent(String programName, Event event);
	
	void cancelEvent(String programName, String eventId);
	
	
}
