package com.akuacom.pss2.program.participant;

import javax.ejb.Local;
import javax.ejb.Remote;

public interface ProgramParticipantBidLevelMapEAO extends
        ProgramParticipantBidLevelMapGenEAO {
    @Remote
    public interface R extends ProgramParticipantBidLevelMapEAO {}

    @Local
    public interface L extends ProgramParticipantBidLevelMapEAO {}

    void deleteByProgramNameAndParticipantNameAndClient(String programName,
            String participantName, boolean isClient);
}
