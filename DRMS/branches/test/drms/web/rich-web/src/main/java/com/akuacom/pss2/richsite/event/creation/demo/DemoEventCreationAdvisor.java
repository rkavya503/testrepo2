package com.akuacom.pss2.richsite.event.creation.demo;

import com.akuacom.pss2.richsite.event.creation.EventCreationAdvisor;
import com.akuacom.pss2.richsite.event.creation.EvtCreation;

public class DemoEventCreationAdvisor extends EventCreationAdvisor {

	private static final long serialVersionUID = 4014442059068157036L;
	
	@Override
	protected EvtCreation createCreationModel() {
		return new DemoEventCreation(this.getProgramName());
	}
	
}
