/**
 * 
 */
package com.akuacom.pss2.program.cbp;

import java.util.ArrayList;
import java.util.List;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.program.cpp.CPPValidator;
import com.akuacom.utils.drw.SCE_LOCATION_SLAP;

/**
 * the class CBPValidator
 */
public class CBPValidator extends CPPValidator {

	private static final long serialVersionUID = 7684168165244202052L;
	
	public void validateEvent(Event event) throws ProgramValidationException {
        
		super.validateEvent(event);
		
        validateLocation();
        
        processErrors("event validation failed", "");

	}

    protected void validateLocation() {
    	if (event.getLocations()==null || event.getLocations().size()==0) {
            addError(errors, "Location is required. Please make sure at least one location is selected.", "Location");
    	} else {
    		List<String> slaps=getSLAPList();
    		List<String> invalidList=new ArrayList<String>();
    		for (String loc: event.getLocations()) {
    			if (!slaps.contains(loc)) {
    				invalidList.add(loc);
    			}
    		}
    		if (invalidList.size()> 0) {
    			if (invalidList.size()>=event.getLocations().size()) {
                    addError(errors, "Location is required. Please make sure at least one location is selected.", "Location");
    			} else {
    	            ProgramValidationMessage error = new ProgramValidationMessage();
    	            String desc="Invalid locations: " + invalidList.toString();
    	            error.setDescription(desc);
    	            error.setParameterName("Location");
    	            warnings.add(error);
    	            event.getLocations().removeAll(invalidList);
    	            
    			}
    		}
    		
    	}
    }

	private List<String> getSLAPList() {
		List<String> slaps=new ArrayList<String>();
		
		for (SCE_LOCATION_SLAP slap:SCE_LOCATION_SLAP.values())
			slaps.add(slap.name());
		
		return slaps;
	}

}
