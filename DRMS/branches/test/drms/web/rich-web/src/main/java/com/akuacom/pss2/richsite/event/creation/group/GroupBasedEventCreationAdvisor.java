package com.akuacom.pss2.richsite.event.creation.group;

import com.akuacom.pss2.richsite.event.creation.EventCreationAdvisor;
import com.akuacom.pss2.richsite.event.creation.EvtCreation;

public class GroupBasedEventCreationAdvisor extends EventCreationAdvisor {

	private static final long serialVersionUID = -8336221830605687859L;
	
	@Override
	protected EvtCreation createCreationModel() {
		return new GroupEventCreationModel(this.getProgramName());
	}
	
}
