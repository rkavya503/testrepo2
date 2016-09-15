package com.akuacom.pss2.drw.event.creation.api;

import com.akuacom.pss2.drw.event.creation.EventCreationAdvisor;
import com.akuacom.pss2.drw.event.creation.EvtCreation;

public class ApiEventCreationAdvisor extends EventCreationAdvisor {

	private static final long serialVersionUID = 4014442059068157036L;
	
	@Override
	protected EvtCreation createCreationModel() {
		return new ApiEventCreation(this.getProgramName(),"API");
	}
	
}
