package com.akuacom.pss2.usecasetest.cases;

import static org.junit.Assert.*;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantUtil;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import java.util.List;


/**
 *
 * @author spierson
 *
 * low-level usecase
 *
 * This usecase creates a client under a participant
 * and makes that client participate in the parents
 * programs
 *
 * returns the client (participant)
 */
public class CreateClientUsecase extends AbstractUseCase {

    private Participant parentParticipant = null;
    private String clientName = null;
    private String password = null;

    public Participant getParentParticipant() { return parentParticipant; }
    public final void setParentParticipant(Participant parentParticipant) { this.parentParticipant = parentParticipant; }
    public String getClientName() { return clientName; }
    public final void setClientName(String clientName) {this.clientName = clientName; }
    public String getPassword() { return password; }
    public final void setPassword(String password) { this.password = password; }

    public CreateClientUsecase() {
        this(null, null, null);
    }

    public CreateClientUsecase(Participant parentParticipant, String clientName, String password) {
        setParentParticipant((Participant)parentParticipant);
        setClientName(clientName);
        setPassword(password);
    }


    @SuppressWarnings("deprecation")
    @Override
    public Object runCase() throws Exception {
        
        // Create a new participant object
        Participant client = new Participant();
        String participantName = getParentParticipant().getParticipantName();
        client.setParent(participantName);

        // Create a client (participant) object
        String fullClientName = participantName+"."+clientName;
        Participant exists = getClientMgr().getClientLJF(fullClientName);
        if (exists != null) {
            return exists; // It's not fatal if one already exists
        }
        

        client.setParticipantName(fullClientName);
        client.setAccountNumber(fullClientName);
        client.setClient(true);
        getClientMgr().createClient((Participant) client,getPassword().toCharArray());

        // Add this client to the required programs
        List<String> programs = getPartMgr().getProgramsForParticipant(participantName,false);
        if (programs != null) {
            for (String program : programs ) {
                ProgramParticipant ppt = getProgPartMgr().getClientProgramParticipants(program, fullClientName, true);
                ppt.setState(1);
                ppt.setClientConfig(1);
                getProgPartMgr().updateProgramParticipant(program, fullClientName, true, ppt);
            }
        }

        // Now look it up and compare with the one we made
        Participant found = getClientMgr().getClient(fullClientName);
        assertTrue(found != null);

        ParticipantUtil.compareParticipants(client, found);
        return found;

    }

}
