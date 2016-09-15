/**
 * 
 */
package com.akuacom.pss2.program.testProgram;

import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.demo.DemoValidator;

/**
 *
 */
public class TestValidator extends DemoValidator {

	private static final long serialVersionUID = -5420293487825949713L;
	
    public void validateEventParticipants(Event event,
            UtilityDREvent utilityDREvent) throws ProgramValidationException {
    	
    }
    
    /**
     * no need to validate program participant for test program
     */
    @Override
    public void validateProgramParticipant(Participant participant)
			throws ProgramValidationException {
    	// do nothing for program participant validation for test program
	}

}
