/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.usecasetest.cases;

import java.util.List;

/**
 *
 * @author spierson
 *
 * Deletes a list of named participants
 *
  * mid-level usecase
 *
 * Deletes a list of participants
 */
public class DeleteSeveralParticipantsUseCase extends AbstractUseCase {

    private String password = "Test_1234";
    private List<String> participantNames = null;
    private List<String> associatedPrograms = null;
    private boolean deleteAfterCreate = true;

    public List<String> getParticipantNames() {return participantNames; }
    public final void setParticipantNames(List<String> participantNames) {this.participantNames = participantNames; }

    public DeleteSeveralParticipantsUseCase(List<String> participantNames) {
        setParticipantNames(participantNames);
    }

    @Override
    public Object runCase() throws Exception {

        for (String partName : participantNames) {
            System.out.println("Planning to delete part" + partName);
            new DeleteParticipantUsecase(partName).runCase();
        }
        return null;
    }

}
