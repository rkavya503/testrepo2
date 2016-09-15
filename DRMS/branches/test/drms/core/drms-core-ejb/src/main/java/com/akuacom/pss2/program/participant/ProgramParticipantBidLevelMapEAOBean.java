package com.akuacom.pss2.program.participant;

import javax.ejb.Stateless;

@Stateless
public class ProgramParticipantBidLevelMapEAOBean extends
		ProgramParticipantBidLevelMapGenEAOBean implements
		ProgramParticipantBidLevelMapEAO.R, ProgramParticipantBidLevelMapEAO.L {

	public void deleteByProgramNameAndParticipantNameAndClient(
			String programName, String participantName, boolean isClient) {
		em.createNamedQuery(
				"ProgramParticipantBidLevelMap.deleteByProgramNameAndParticipantNameAndClient")
				.setParameter("programName", programName)
				.setParameter("participantName", participantName)
				.setParameter("client", isClient).executeUpdate();

	}

}
