package com.akuacom.pss2.usecasetest.cases;


public class FindClientByParticipantUsecase extends AbstractUseCase {

    private String participantName = null;

    public String getPartName() { return participantName; }
    public final void setPartName(String participantName) {this.participantName = participantName; }

    public FindClientByParticipantUsecase() {
        this(null);
    }

    public FindClientByParticipantUsecase(String participantName) {
        super();
        setPartName(participantName);
    }

    @Override
    public Object runCase() throws Exception {
    	//Find  client names with the given participant name
    	return getPartMgr().getClientNamesByParticipant(getPartName());

    }

}
