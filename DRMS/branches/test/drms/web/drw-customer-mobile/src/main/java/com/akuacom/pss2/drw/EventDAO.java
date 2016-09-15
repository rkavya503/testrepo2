package com.akuacom.pss2.drw;

import java.util.Date;
import java.util.List;

import com.akuacom.pss2.drw.core.Program;
import com.akuacom.pss2.drw.util.PredicateFilter;
import com.akuacom.pss2.drw.value.EventValue;
import com.akuacom.pss2.drw.value.WeatherValue;
import com.akuacom.pss2.drw.value.AlertValue;

public interface EventDAO {
	List<Event> getEvents(PredicateFilter filter);
	Date getLastUpdateTime(String category);

	List<Program> getPrograms();
	List<String> getProducts(String programName,boolean isIRProgram);
	List<EventValue> getHistoryEvents(String programName, String product,
			List<String> zipcode, String start, String end);
	List<WeatherValue> getHistoryTems(Date start, Date end);
	List<AlertValue> getAlertHistory(String deviceKey);

}
