package com.akuacom.pss2.drw.event.creation.bip2013;

import com.akuacom.pss2.drw.event.creation.EventCreationAdvisor;
import com.akuacom.pss2.drw.event.creation.EvtCreation;
import com.akuacom.pss2.drw.event.creation.api.ApiEventCreation;

public class Bip2013EventCreationAdvisor extends EventCreationAdvisor {

	private static final long serialVersionUID = 4014442059068157036L;
	
	@Override
	protected EvtCreation createCreationModel() {
		return new ApiEventCreation(this.getProgramName(),"BIP2013");//product name: bip2013
	}
	
}
