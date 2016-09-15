/**
 * 
 */
package com.akuacom.pss2.program.bip;

import java.util.ArrayList;
import java.util.List;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.program.cpp.CPPValidator;
import com.akuacom.utils.drw.SCE_LOCATION_SLAP;

/**
 * the class BIPValidator
 */
public class BIPValidator extends CPPValidator {

	private static final long serialVersionUID = 7684168165244202052L;
	
	public void validateEvent(Event event) throws ProgramValidationException {
		super.validateEvent(event);
        processErrors("event validation failed", "");
	}
	
    public void validateLocation() {
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
	
	 protected void validateEndTime() {
		 //DO nothing since end time can be null
		 if( event.getEndTime()!=null 
				 && !event.getStartTime().before(event.getEndTime() )){
		  ProgramValidationMessage error = new ProgramValidationMessage();
          error.setDescription("end time must be later than start time");
          error.setParameterName("endTime");
          errors.add(error);
		 }
	 }
	 
	 protected void validateDuration() {
		 //DO nothing since end time can be null
	 }
	 
	 public void validateIssueTime(){
		  // make sure issue time is within the last 60 seconds
        if (event.getIssuedTime().getTime() < (nowMS - TIME_BUFFER_MS)) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            error.setDescription("issue time is in the past");
            error.setParameterName("issuedTime");
            errors.add(error);
        }
	 }

	 protected void validateStartTime() {
		//DO nothing 
	 }
}
