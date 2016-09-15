package com.akuacom.pss2.richsite.event.creation;

public class EventParticipantValidator extends Validator {

	private static final long serialVersionUID = -5881923286113877023L;

	@Override
	public MSG validate(AbstractEventCreation model) {
//		if(model.getParticipantSelection().getSelectedParticipant().size()==0){
//			return new MSG(Validator.MSG_ERROR,"Cannot Create Event with empty participant list");
//		}
		return null;
	}

}
