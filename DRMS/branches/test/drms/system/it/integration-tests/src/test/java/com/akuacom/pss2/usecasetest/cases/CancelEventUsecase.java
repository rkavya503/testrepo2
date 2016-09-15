/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.usecasetest.cases;

import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.program.Program;
import java.util.Collection;
import java.util.List;



/**
 *
 * @author spierson
 *
 * low-level usecase
 *
 * This usecase takes a program name and a list of event names
 * and cancels those events
 *
 */
public class CancelEventUsecase extends AbstractUseCase {

    String programName;
    Collection<String>  eventNames;


    public CancelEventUsecase(String programName, Collection<String> eventNames) {
        this.programName = programName;
        this.eventNames = eventNames;
    }

    @Override
    public Object runCase() throws Exception {
        Program found = getProgMgr().getProgram(programName);
        if (eventNames != null) {
            for (String eventName : eventNames) {
                getEventMgr().removeEvent(programName, eventName);
            }
        }
        return found;
    }
}
