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
 * This usecase creates a participant
 * but DOES NOT associate it with any program(s)
 *
 * returns the new participant
 */
public class CreateNewParticipantUsecase extends AbstractUseCase {

    private String participantName = null;
    private String accountID = null;
    private String password = null;

    public String getAccountID() { return accountID; }
    public final void setAccountID(String accountID) { this.accountID = accountID; }
    public String getParticipantName() { return participantName; }
    public final void setParticipantName(String participantName) {this.participantName = participantName; }
    public String getPassword() { return password; }
    public final void setPassword(String password) { this.password = password; }

    public CreateNewParticipantUsecase() {
        this(null, null, null);
    }

    public CreateNewParticipantUsecase(String partName, String accountID, String password) {
        setParticipantName(partName);
        setAccountID(accountID);
        setPassword(password);
    }


    @SuppressWarnings("deprecation")
    @Override
    public Object runCase() throws Exception {

        Participant exists = (Participant) new FindParticipantUsecase(participantName).runCase();
        // We allow the participant to exist without it being an error
        if (exists != null) {
            return exists;
        }

        // Create a new participant object
        Participant participant = ParticipantUtil.getParicipantInstance(participantName);
        participant.setAccountNumber(participantName);
        participant.setType(Participant.TYPE_CLIR);
        getPartMgr().createParticipant( participant, password.toCharArray(), null);

        // Now look it up and compare with the one we made
        Participant found = (Participant) new FindParticipantUsecase(participantName).runCase();
        assertTrue(found != null);

        getPartMgr().getProgramParticpantsForClientConfig(participantName , false);

        List<ProgramParticipant> ppc = getPartMgr().getProgramParticpantsForClientConfig(participantName , false);
        for (ProgramParticipant progPart : ppc) {
            progPart.setClientConfig(1);
            getProgPartMgr().updateProgramParticipant(progPart.getProgramName(), found.getParticipantName(), false , progPart);
        }

        ParticipantUtil.compareParticipants(participant, found);
        return found;

    }

}
