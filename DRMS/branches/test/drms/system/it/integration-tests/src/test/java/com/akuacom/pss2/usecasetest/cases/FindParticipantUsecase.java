package com.akuacom.pss2.usecasetest.cases;


import com.akuacom.pss2.participant.Participant;

/**
 *
 * @author spierson
 *
 * Looks a participant up by name
 *
 * low-level usecase
 */
public class FindParticipantUsecase extends AbstractUseCase {

    private String participantName = null;

    public String getParticipantName() { return participantName; }
    public final void setParticipantName(String participantName) {this.participantName = participantName; }

    public FindParticipantUsecase() {
        this(null);
    }

    public FindParticipantUsecase(String partName) {
        super();
        setParticipantName(partName);
    }

    @Override
    public Object runCase() throws Exception {

        // Now look it up and compare with the one we made
        return (Participant) getPartMgr().getParticipant(getParticipantName());

    }

}
