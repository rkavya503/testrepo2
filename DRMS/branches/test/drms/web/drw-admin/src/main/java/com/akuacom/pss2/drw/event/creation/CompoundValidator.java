package com.akuacom.pss2.drw.event.creation;

public class CompoundValidator extends CommonValidator<AbstractEventCreation> {

	private static final long serialVersionUID = -4548088746979905575L;

	private CommonValidator[] validators;
	
	
	public CompoundValidator(CommonValidator... validators){
		this.validators = validators;
	}
	
	@Override
	public MSG validate(AbstractEventCreation model) {
		MSG msg =null;
		if(validators==null) return null;
		for(CommonValidator val:validators){
			MSG newmsg=val.validate(model);
			if(newmsg!=null){
				if(msg==null || msg.type<newmsg.type)
					msg = newmsg;
			}
		}
		return msg;
	}
}
