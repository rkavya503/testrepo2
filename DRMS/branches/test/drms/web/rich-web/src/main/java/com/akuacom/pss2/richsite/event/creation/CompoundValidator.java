package com.akuacom.pss2.richsite.event.creation;

public class CompoundValidator extends Validator {

	private static final long serialVersionUID = -4548088746979905575L;

	private Validator[] validators;
	
	
	public CompoundValidator(Validator... validators){
		this.validators = validators;
	}
	
	@Override
	public MSG validate(AbstractEventCreation model) {
		MSG msg =null;
		if(validators==null) return null;
		for(Validator val:validators){
			MSG newmsg=val.validate(model);
			if(newmsg!=null){
				if(msg==null || msg.type<newmsg.type)
					msg = newmsg;
			}
		}
		return msg;
	}
}
