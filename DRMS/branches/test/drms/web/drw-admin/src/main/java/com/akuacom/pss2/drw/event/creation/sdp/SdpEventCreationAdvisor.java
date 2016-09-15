package com.akuacom.pss2.drw.event.creation.sdp;

import java.util.Date;

import org.apache.log4j.Logger;

import com.akuacom.pss2.drw.event.creation.EventCreationAdvisor;
import com.akuacom.pss2.drw.event.creation.EvtCreation;

public class SdpEventCreationAdvisor extends EventCreationAdvisor {

	private static final Logger log = Logger.getLogger(SdpEventCreationAdvisor.class);
	private static final long serialVersionUID = 4014442059068157036L;
	
	@Override
	protected EvtCreation createCreationModel() {
		Date start = new Date();
		log.debug("createCreationModel() start at: "+start);
//		return new SdpEventCreation(this.getProgramName()); comment out for performace tuning
		SdpEventCreation sd = new SdpEventCreation(this.getProgramName());
		Date end = new Date();
		log.debug("createCreationModel() end at: "+end +"cost :" +(end.getTime()-start.getTime()));
		return sd;
	}
	
}
