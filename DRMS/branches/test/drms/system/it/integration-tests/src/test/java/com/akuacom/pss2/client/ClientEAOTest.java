package com.akuacom.pss2.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.naming.NamingException;

import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.akuacom.ejb.AbstractVersionedEAOTest;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.data.PDataSourceEAO;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantTest;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.utils.lang.StringUtil;

import java.util.List;

public class ClientEAOTest extends
		AbstractVersionedEAOTest<ClientEAO, Participant> {

	public ClientEAOTest() {
		super(ClientEAOBean.class, ClientEAOBean.class, Participant.class);
	}

	@Override
	protected void assertEntityValuesNotEquals(Participant created,
			Participant found) {
		assertTrue("Mutated values were equal, exptected to be not equal",
				found.getType() != created.getType());
	}

	@Override
	protected void mutate(Participant found) {
		found.setType((byte) 1);
	}

	@Override
	protected void assertEntityValuesEquals(Participant created,
			Participant found) {
		assertEquals("Expected objects to be identical except the UUID",
				created.getType(), found.getType());
		// TODO fill in some more variables
		assertTrue("Expected UUID to be differnt",
				created.getUUID() != found.getUUID());
	}

	@Override
	protected Participant generateRandomEntity() {
		Participant participant = new ParticipantTest()
				.generateRandomIncompleteEntity();
		participant.setClient(true);
		participant.setType((byte) 0);
		return participant;
	}
	
    public void generateParticipantsWithFullManager(ParticipantManager pm,
            ProgramParticipantManager ppm, ClientManager cm,
            String participantPrefix, String clientPrefix,
            String clientPassword, int numberOfParticipants)
            throws NamingException {
        PDataSourceEAO pdsEao = EJBFactory.getBean(PDataSourceEAO.class);
        for (int i = 0; i < numberOfParticipants; i++) {
            String numStr = StringUtil.toString(i, 3);
            String participantName = participantPrefix + numStr;
            String clientName = participantPrefix + numStr + "." + clientPrefix;
            Participant participant = new Participant();
            participant.setParticipantName(participantName);
            participant.setAccountNumber(participantName);
            participant.setDataEnabler(true);
            PDataSource pd = new PDataSource();
            pd.setOwnerID(participantName);
            pd.setName("meter1");
            pd.setEnabled(true);
            try {
                pdsEao.create(pd);
            } catch (DuplicateKeyException e) {
                e.printStackTrace();
            }
            for (ProgramParticipant ppc :participant.getProgramParticipants()) {
                ppc.setClientConfig(1);
                ppm.updateProgramParticipant(ppc.getProgramName(),
                        participantName, false, ppc);
            }

            Participant clientParticipant = new Participant();
            clientParticipant.setClient(true);
            clientParticipant.setUser(clientName);
            clientParticipant.setType((byte) 0);
            clientParticipant.setAccountNumber(clientName);
            clientParticipant.setParent(participantName);
            clientParticipant.setActivated(true);
            cm.createClient(clientParticipant, clientPassword.toCharArray());

            String cppName = getFirstCPPProgramName();
            ProgramParticipant pp = ppm.getClientProgramParticipants(
                    cppName, clientName, true);
            pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
            ppm.updateProgramParticipant(cppName, clientName, true, pp);
        }

    }
	
    protected String getFirstCPPProgramName() {
        ProgramManager.R programManager = (ProgramManager.R) EJBFactory.getBean(ProgramManager.class);
        List<Program> programs = programManager.getProgramsAsPrograms();
        String name = null;
        for (Program p : programs) {
            if ("com.akuacom.pss2.program.cpp.CPPProgramEJB".equals(p.getClassName())) {
                if (p.isMustIssueBDBE()) {
                    name = p.getProgramName();
                    break;
                }
            }
        }
        return name;
    }

    @Test
    public void testCreate1000() {
        if ("true".equals(System.getProperty("create1000clients"))) {
            Logger log = Logger.getLogger(getClass());
            log.info("here i am");
            try {
                ProgramParticipantManager.R ppm = (ProgramParticipantManager.R) EJBFactory
                        .getBean(ProgramParticipantManager.class);
                ClientManager.R cm = (ClientManager.R) EJBFactory
                        .getBean(ClientManager.class);
                ParticipantManager.R pm = (ParticipantManager.R) EJBFactory
                        .getBean(ParticipantManager.class);
                String password = "Test_1234";
                generateParticipantsWithFullManager(pm, ppm, cm, "testP",
                        "testC", password, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
