package com.akuacom.pss2.richsite.event.creation.group;

import com.akuacom.pss2.richsite.event.creation.AbstractEventCreation;
import com.akuacom.pss2.richsite.event.creation.Validator;

public class LocationValidator extends Validator {

	private static final long serialVersionUID = -5881923286113877023L;

	@Override
	public MSG validate(AbstractEventCreation model) {
		if(model.getEventEnrollment().getEnrollmentItems().size()==0){
			return new MSG(Validator.MSG_ERROR,"Cannot Create Event with empty location list");
		}
		return null;
	}

}
