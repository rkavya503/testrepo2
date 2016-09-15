package com.akuacom.pss2.feature;

import static org.junit.Assert.assertNotNull;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.openadr.dras.akuautilityoperator.AkuaUtilityOperator;
import org.openadr.dras.akuautilityoperator.AkuaUtilityOperatorService;
import org.openadr.dras.participantoperator.ParticipantOperator;
import org.openadr.dras.participantoperator.ParticipantOperator_Service;
import org.openadr.dras.utilityoperator.UtilityOperator;
import org.openadr.dras.utilityoperator.UtilityOperator_Service;

import com.akuacom.pss2.core.ParticipantManagerFixture;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;

public class FeatureFixture extends ParticipantManagerFixture {

    protected UtilityOperator utilOp;
    protected ParticipantOperator partOp;
    protected AkuaUtilityOperator akuaOp;
    protected List<Program> programs = new ArrayList<Program>();
    protected List<Participant> parts = new ArrayList<Participant>();
    protected List<Participant> clients = new ArrayList<Participant>();

    public static int NUMBER_OF_PARTICIPANTS = 10;
    public static int NUMBER_OF_CLIENTS_IN_PARTICIPANT = 2;
    public static boolean POLLING = false;

    @Before
    public void setUp() throws Exception {
        String host = "http://localhost:8080";
        Authenticator.setDefault( new SimpleAuthenticator() );
        
        utilOp =
        		new UtilityOperator_Service(new URL(host +
        			"/UtilityOperatorWS/nossl/UtilityOperatorWS" + "?wsdl"),
        		new QName("http://www.openadr.org/DRAS/UtilityOperator/",
        		"UtilityOperatorWSService")).getUtilityOperatorPort();

        akuaOp =
                new AkuaUtilityOperatorService(new URL(host +
                    "/AkuaUtilityOperatorWS/nossl/AkuaUtilityOperatorWS" + "?wsdl"),
                new QName("http://www.openadr.org/DRAS/AkuaUtilityOperator/",
                "AkuaUtilityOperatorWSService")).getAkuaUtilityOperatorPort();

        partOp =
        		new ParticipantOperator_Service(new URL(host +
        			"/ParticipantOperatorWS/nossl/ParticipantOperatorWS" + "?wsdl"),
        		new QName("http://www.openadr.org/DRAS/ParticipantOperator/",
        			"ParticipantOperatorWSService")).getParticipantOperatorPort();

        assertNotNull("Did not get EventManager in setup", utilOp);
        assertNotNull("Did not get ParticipantManager in setup", partOp);
        assertNotNull("Did not get ClientManager in setup", akuaOp);

        List<String> programNames = programManager.getPrograms();
        for(String progName : programNames)
        {
            programs.add(programManager.getProgram(progName));
        }
    }

    private class SimpleAuthenticator extends Authenticator
	{

		/* (non-Javadoc)
		 * @see java.net.Authenticator#getPasswordAuthentication()
		 */
		public PasswordAuthentication getPasswordAuthentication()
		{
			System.out.println("Authenticating");
			return new PasswordAuthentication("a", "b"
				.toCharArray());
		}
	}
    
}