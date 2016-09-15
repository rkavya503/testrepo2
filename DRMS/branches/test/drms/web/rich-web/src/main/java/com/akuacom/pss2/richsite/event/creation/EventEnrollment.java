package com.akuacom.pss2.richsite.event.creation;

import java.util.List;

import com.akuacom.pss2.query.EventEnrollingItem;

public interface EventEnrollment {
	
	public List<? extends EventEnrollingItem> getEnrollmentItems();
	
}
