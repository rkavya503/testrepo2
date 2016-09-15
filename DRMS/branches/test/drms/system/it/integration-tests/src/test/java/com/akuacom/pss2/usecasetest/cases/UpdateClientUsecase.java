package com.akuacom.pss2.usecasetest.cases;

import org.junit.Test;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantUtil;

public class UpdateClientUsecase extends AbstractUseCase {

    private Participant client;

    public UpdateClientUsecase() {
        this(null);
    }

    public UpdateClientUsecase(Participant cl) {
        setParticipant(cl);
    }


    @Override
    @Test  
    public Object runCase() throws Exception {
    	getClientMgr().updateClient(getClient());
    	// Make the update operation effect 
        Participant found = (Participant) new FindClientUsecase(client.getParticipantName()).runCase();
        // verify the update result
        ParticipantUtil.compareParticipants(found, getClient());
        
        return found;
    }

    /**
     * @return the participant
     */
    public Participant getClient() {
        return client;
    }

    /**
     * @param participant the participant to set
     */
    public final void setParticipant(Participant participant) {
        this.client = participant;
    }

}
