package com.akuacom.pss2.usecasetest.cases;

import com.akuacom.pss2.participant.Participant;

public class FindClientUsecase extends AbstractUseCase {

    private String clientName = null;

    public String getClientName() { return clientName; }
    public final void setClientName(String clientName) {this.clientName = clientName; }

    public FindClientUsecase() {
        this(null);
    }

    public FindClientUsecase(String cliName) {
        super();
        setClientName(cliName);
    }

    @Override
    public Object runCase() throws Exception {

        // Find a client object with the given client name
        return (Participant) getClientMgr().getClientLJF(getClientName());

    }

}
