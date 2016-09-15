
package com.akuacom.pss2.drw;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.drw.core.Program;
import com.akuacom.pss2.drw.value.EventLegend;

import com.akuacom.pss2.drw.value.BlockValue;
import com.akuacom.pss2.drw.value.EventValue;
import com.akuacom.pss2.drw.value.EventVo;
import com.akuacom.pss2.drw.value.SlapBlockRange;


public interface DREvent2013Manager {
    @Remote
    public interface R extends DREvent2013Manager {
		@Local
		public interface L extends DREvent2013Manager {}
	}
    @Local
    public interface L extends DREvent2013Manager {}

	
	List<EventValue> getEvents(List<String> programClass,boolean isCommercial, boolean isActive);
	List<EventValue> getHistoryEvents(String program, String product,Date start, Date end, List<String> zipCodes);
	List<EventLegend> getActiveEvents(String programName, List<String> products);
	List<EventLegend> getScheduleEvents(String programName, List<String> products);
	List<BlockValue> getKMLS(List<String> abanksNumber);
	List<EventValue> getDrwEvents(String eventDetailIds);
	/**
	 * @param eventDetailIds
	 * @return
	 */
	List<EventValue> getDrwCBPEvents(String eventDetailIds);
	boolean scheduledEventsNeedUpdate();
	
	boolean activeEventsNeedUpdate();
	
	
	List<EventVo> getActiveEventDetails(String programName, List<String> products);
	List<EventVo> getScheduleEventDetails(String programName, List<String> products);
	
	List<EventVo> getDREvents(List<String> programClass, boolean isActive);
	List<Program> getAllProgram();
	List<String> getProducts(String programName,boolean isIRProgram);
	List<String> getBlocksForSlap(String number);
	SlapBlockRange getSlapBlockRange(String number);
	List<String> getAllSlaps();
	List<EventVo> getDREvents4Mobile(List<String> programClass, boolean isActive);
	String getSlap4Block(String number);
}
