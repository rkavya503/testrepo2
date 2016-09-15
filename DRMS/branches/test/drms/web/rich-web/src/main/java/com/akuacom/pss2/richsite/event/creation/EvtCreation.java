package com.akuacom.pss2.richsite.event.creation;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.program.Program;

public interface EvtCreation {
	
	public static final String BASE_EVT_LIST_PAGE="../../../pss2.website/uoEvent.do";
	public static final String BASE_PROGRAM_PAGE="../../../pss2.website/uoProgram.do";
	
	String getProgramName();
	
	String getEventListPage();
	
	String getProgramPage();
	
	Program getProgram();
		
	Event getEvent();
	
	boolean isCbpProgramEJB();
	
	boolean isSpanDays();
	
}
