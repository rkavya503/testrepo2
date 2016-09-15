package com.akuacom.pss2.usecasetest.cases;

import static org.junit.Assert.*;

import com.akuacom.pss2.participant.Participant;

/**
 *
 * @author spierson
 *
 * low-level usecase
 *
 * deletes a participant along with all of its child clients
 *
 */
public class DeleteParticipantUsecase extends AbstractUseCase {

    private String participantName = null;

    public String getParticipantName() { return participantName; }
    public final void setParticipantName(String partName) {this.participantName = partName; }

    public DeleteParticipantUsecase() {
        this(null);
    }

    public DeleteParticipantUsecase(String participantName) {
        setParticipantName(participantName);
    }


    @SuppressWarnings("deprecation")
    @Override
    //@Test  /* This case is not meant to be run individually */
    public Object runCase() throws Exception {

        // Now look it up and compare with the one we made
        Participant found = getPartMgr().getParticipant(participantName);
        assertTrue(found != null);
        
        getPartMgr().removeParticipant(participantName);  // delete the test participant
        
        Participant shouldntExist = (Participant) new FindParticipantUsecase(participantName).runCase();
        assertTrue(shouldntExist == null);
        return null;
        
    }

}
