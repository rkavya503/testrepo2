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
 * This usecase associates a participant with zero or more programs
 *
 */
public class AssociateParticipantWithProgramUsecase extends AbstractUseCase {

    private String participantName = null;
    private String accountID = null;
    private String password = null;
    private List<String> programs;

    public String getParticipantName() { return participantName; }
    public final void setParticipantName(String participantName) {this.participantName = participantName; }
    public final void setPrograms(List<String> programs) { this.programs = programs; }
    public List<String> getPrograms() { return programs; }


    public AssociateParticipantWithProgramUsecase() {
        this(null, null);
    }

    public AssociateParticipantWithProgramUsecase(String partName, List<String> programs) {
        setParticipantName(partName);
        setPrograms(programs);
    }

    @SuppressWarnings("deprecation")
    @Override
    public Object runCase() throws Exception {

        // the participant up
        Participant found = (Participant) new FindParticipantUsecase(participantName).runCase();
        assertTrue(found != null);

        if (programs != null) {
            for (String programName : programs) {
                getProgPartMgr().addParticipantToProgram(programName, participantName, false);
            }
        }

        getPartMgr().getProgramParticpantsForClientConfig(participantName , false);

        List<ProgramParticipant> ppc = getPartMgr().getProgramParticpantsForClientConfig(participantName , false);
        for (ProgramParticipant progPart : ppc) {
            progPart.setClientConfig(1);
            getProgPartMgr().updateProgramParticipant(progPart.getProgramName(), found.getParticipantName(), false , progPart);
        }

        return null;
    }

}
