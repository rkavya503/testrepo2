/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.usecasetest.cases;

import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.client.ClientManagerBean;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.EventManagerBean;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantManagerBean;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramManagerBean;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.participant.ProgramParticipantManagerBean;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.signal.SignalManagerBean;

import java.util.List;

/**
 *
 * @author spierson
 */
public abstract class AbstractUseCase {

    private static ClientManager clientMgr = null;
    private static ParticipantManager partMgr = null;
    private static ProgramManager progMgr = null;
    private static ProgramParticipantManager progPartMgr = null;
    private static EventManager eventMgr = null;
    private static SignalManager signalMgr = null;

    protected ClientManager getClientMgr() {
        if (clientMgr == null) clientMgr = JBossFixture.lookupSessionRemote(ClientManagerBean.class);
        return clientMgr;
    }

    protected ParticipantManager getPartMgr() {
        if (partMgr == null)partMgr = JBossFixture.lookupSessionRemote(ParticipantManagerBean.class);
        return partMgr;
    }

    protected ProgramManager getProgMgr() {
        if (progMgr == null) progMgr = JBossFixture.lookupSessionRemote(ProgramManagerBean.class);
        return progMgr;
    }

    protected ProgramParticipantManager getProgPartMgr() {
        if (progPartMgr == null) progPartMgr = JBossFixture.lookupSessionRemote(ProgramParticipantManagerBean.class);
        return progPartMgr;
    }

    protected EventManager getEventMgr() {
        if (eventMgr == null) eventMgr = JBossFixture.lookupSessionRemote(EventManagerBean.class);
        return eventMgr;
    }

    protected SignalManager getSignalMgr() {
        if (signalMgr == null) signalMgr = JBossFixture.lookupSessionRemote(SignalManagerBean.class);
        return signalMgr;
    }
    
    /**
     * runCase
     *
     * This method executes the use case.  
     */
    public abstract Object runCase()  throws Exception;

    protected String getFirstCPPProgramName() {
        System.out.println("progMgr " +progMgr);
        List<Program> programs = getProgMgr().getProgramsAsPrograms();
        String name = null;
        for (Program p : programs) {
            if ("com.akuacom.pss2.program.cpp.CPPProgramEJB".equals(p.getClassName())) {
                if (p.isMustIssueBDBE()) {
                    name = p.getProgramName();
                    break;
                }
            }
        }
        return name;
    }
}
