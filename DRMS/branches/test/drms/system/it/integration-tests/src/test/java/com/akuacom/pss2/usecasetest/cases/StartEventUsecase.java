/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.usecasetest.cases;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventTest;
import com.akuacom.pss2.program.Program;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;



/**
 *
 * @author spierson
 *
 * low-level usecase
 *
 * This low-level use case starts an event in the specified
 * program.  NOTE: Some programs will, in some circumstances
 * create more than one event in response to this.
 * That's why this case returns a collection of event names.
 */
public class StartEventUsecase extends AbstractUseCase {

    String programName;


    public StartEventUsecase(String programName) {
        this.programName = programName;
    }

    @Override
    public Object runCase() throws Exception {
        Program found = getProgMgr().getProgram(programName);
        assert(found != null);
        Event evt = new Event();
        evt.setProgramName(programName);
        evt.setEventName(EventTest.generateRandomEventName());

		int oneMinute = 60000;
		Date now = new Date(System.currentTimeMillis() + oneMinute);
		evt.setIssuedTime(now);
		GregorianCalendar calender = new GregorianCalendar();
		calender.setTime(now);
		calender.add(Calendar.DAY_OF_MONTH, 1);
		calender.set(Calendar.HOUR_OF_DAY, 14);
		calender.set(Calendar.MINUTE, 0);
		calender.set(Calendar.SECOND, 0);
		calender.set(Calendar.MILLISECOND, 0);
		evt.setStartTime(calender.getTime());
		calender.set(Calendar.HOUR_OF_DAY, 18);
		evt.setEndTime(calender.getTime());
		evt.setReceivedTime(now);

        Collection<String> events = getEventMgr().createEvent(programName, evt);
        assert(!events.isEmpty());
        return events;
    }

}
