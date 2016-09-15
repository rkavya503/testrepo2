/**
 * 
 */
package com.akuacom.pss2.participant;

import com.akuacom.pss2.core.ParticipantManagerFixture;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.akuacom.test.TestUtil;
import junit.framework.Assert;
import junit.framework.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;




/**
 * Tests the {@link ParticipantEAO} and {@link ParticipantEAOBean}
 * implementation.
 * 
 * Participant does not follow the BaseEAO pattern so these tests are custom.
 * 
 * @author roller
 * 
 */
public class ParticipantEAOTest extends
		UserEAOTest {

    private static final String DEFAULT_PASSWORD = "Test_12345";

	public Participant createPersistedParticipant() {
		Participant participant = ParticipantUtil
				.getParicipantInstance(TestUtil.generateRandomInt());
		eao.createParticipant(participant);
		Participant found = eao.getParticipant(participant.getParticipantName());
		ParticipantUtil.compareParticipants(participant, found);
		return found;
	}

	@Test
	public void testCreate() {
		Participant participant = createPersistedParticipant();
		// the create method does a bunch of validation for us.
		eao.removeParticipant(participant.getParticipantName());
	}

	@Test
	public void testFindByAccountNumber() {
		Participant participant = createPersistedParticipant();
		List<String> accountNumbers = new ArrayList<String>();
		accountNumbers.add(participant.getAccountNumber());
        List<Participant> found = eao
				.findParticipantsByAccounts(accountNumbers);
		assertEquals(
				"we are supposed to have one and only one participant with this account number",
				1, found.size());
		Participant foundParticipant = found.get(0);
		assertEquals(participant.getParticipantName(),
				foundParticipant.getParticipantName());
		assertEquals(participant.getAccountNumber(),
				foundParticipant.getAccountNumber());
		eao.removeParticipant(participant.getParticipantName());

	}

    @Test
	public void testParticipantsByPageAndFilter() throws Exception {
        ParticipantManagerFixture.setUpManagers();
        int rNum =  TestUtil.generateRandomInt();
        String rString = Integer.toString(rNum);
        String filter = "part"+rString.substring(0, rString.length()-1);
        String column = "pName";
        String sort = "";
        boolean isClient = false;

        // create page with 4 participant
        for (int i=0; i<4; i++){
               Participant p = ParticipantUtil.getParicipantInstance(i+rNum);
               ParticipantManagerFixture.pm.createParticipant(p, DEFAULT_PASSWORD.toCharArray(), null);
               ParticipantManagerFixture.programParticipantManager.addParticipantToProgram(
                       ParticipantManagerFixture.getFirstCPPProgramName(), p.getUser(), false);
               System.out.println("Creating participant " + p.getParticipantName());
        }

         // get the page with filter for participant name
         List<Participant> p = eao.getUserPage(0, 10, filter, column, sort, isClient);

         for(Participant pp:p){
             // test filter
              System.out.println("Filtering participant " + pp.getParticipantName());
              assertEquals( filter, pp.getParticipantName().substring(0,(rString.length()+3)));
          
             // remove the page
             eao.removeParticipant(pp.getParticipantName());
         }

    }
}
