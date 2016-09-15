/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.usecasetest.cases;

import java.util.List;
import java.text.NumberFormat;
import com.akuacom.pss2.participant.Participant;
import java.util.ArrayList;

/**
 *
 * @author spierson
 *
 * Mid-level usecase
 *
 * Creates several participants (names supplied in a list)
 * associates the participants with a program
 * and adds a number of clients under each participant and participating
 * in the program
 */
public class CreateSeveralParticipantsUseCase extends AbstractUseCase {

    private String password = "Test_1234";
    private List<String> participantNames = null;
    private List<String> associatedPrograms = null;
    private boolean deleteAfterCreate = true;

    public List<String> getParticipantNames() {return participantNames; }
    public final void setParticipantNames(List<String> participantNames) {this.participantNames = participantNames; }
    public List<String> getAssociatedPrograms() { return associatedPrograms;}
    public final void setAssociatedPrograms(List<String> associatedPrograms) { this.associatedPrograms = associatedPrograms; }

    public CreateSeveralParticipantsUseCase(List<String> participantNames) {
        List<String> namesList = new ArrayList<String>();
        namesList.add(getFirstCPPProgramName()); // get default cpp program
        setParticipantNames(participantNames);
        setAssociatedPrograms(namesList);
    }

    public CreateSeveralParticipantsUseCase(List<String> participantNames, String associatedProgram) {
        List<String> namesList = new ArrayList<String>();
        namesList.add(associatedProgram);
        setParticipantNames(participantNames);
        setAssociatedPrograms(namesList);
    }

    public CreateSeveralParticipantsUseCase(List<String> participantNames, List<String> associatedPrograms) {
        setParticipantNames(participantNames);
        setAssociatedPrograms(associatedPrograms);
    }

    @Override
    public Object runCase() throws Exception {

        int count = 0;
        for (String partName : participantNames) {
            System.out.println(++count + " " + partName);
            CreateNewParticipantUsecase create = new CreateNewParticipantUsecase(partName, partName, password);
            AssociateParticipantWithProgramUsecase associate = new AssociateParticipantWithProgramUsecase(partName, associatedPrograms);
            Participant part = (Participant) create.runCase();
            associate.runCase();

            int randomNumClients = (int) (Math.random() * 5.0);
            // Now create a random number of clients (0 - 4) for this participant
            for (int i = 0; i < randomNumClients; ++i) {
                NumberFormat nf = NumberFormat.getIntegerInstance();
                nf.setMinimumIntegerDigits(3);
                String clientName = nf.format(i);
                new CreateClientUsecase(part, clientName, password).runCase();

            }
        }

        return null;
    }

}
