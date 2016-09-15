package com.akuacom.pss2.drw.event.creation;

public class EventLocationValidator extends CommonValidator<AbstractEventCreation> {

	private static final long serialVersionUID = -5881923286113877023L;

	@Override
	public MSG validate(AbstractEventCreation model) {
		if(model.getLocationSelection().getSelectedParticipant().size()==0){
			return new MSG(CommonValidator.MSG_ERROR,"Cannot Create Event with empty location list");
		}
		return null;
	}

}
