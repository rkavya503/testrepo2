package com.akuacom.pss2.usecasetest.cases;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantUtil;

public class UpdateParticipantUsecase extends AbstractUseCase {

    private Participant participant;

    public UpdateParticipantUsecase() {
        this(null);
    }

    public UpdateParticipantUsecase(Participant part) {
        setParticipant(part);
    }


    @SuppressWarnings("deprecation")
    @Override
    //@Test  /* This case is not meant to be run individually */
    public Object runCase() throws Exception {

        getPartMgr().updateParticipant(getParticipant());
        Participant found = (Participant) new FindParticipantUsecase(participant.getParticipantName()).runCase();
        ParticipantUtil.compareParticipants(participant, getParticipant());   // asserts equality

        return found;

    }

    /**
     * @return the participant
     */
    public Participant getParticipant() {
        return participant;
    }

    /**
     * @param participant the participant to set
     */
    public final void setParticipant(Participant participant) {
        this.participant = participant;
    }

}
