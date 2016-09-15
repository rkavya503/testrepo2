package com.akuacom.pss2.drw.event.creation.bip;

import com.akuacom.pss2.drw.event.creation.EventCreationAdvisor;
import com.akuacom.pss2.drw.event.creation.EvtCreation;

public class BipEventCreationAdvisor extends EventCreationAdvisor {

	private static final long serialVersionUID = 4014442059068157036L;
	
	@Override
	protected EvtCreation createCreationModel() {
		return new BipEventCreation(this.getProgramName());
	}
	
}
