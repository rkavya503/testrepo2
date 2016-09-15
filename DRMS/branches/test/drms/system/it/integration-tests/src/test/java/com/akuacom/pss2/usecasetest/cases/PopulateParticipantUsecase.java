/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akuacom.pss2.usecasetest.cases;

import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantUtil;

/**
 *
 * @author spierson
 *
 *  low-level usecase
 *
 * Creates a participant and populates all of its fields with values
 */
public class PopulateParticipantUsecase extends AbstractUseCase {

    String testParticipantName = "testPerson1";


    @Override
    public Object runCase() throws Exception {

        Participant p = (Participant) new CreateNewParticipantUsecase(testParticipantName, "59821", "Test_1234").runCase();
        
        // Set all the properties that the basic creator did not
        p.setType(Participant.TYPE_AUTO);
        p.setHostAddress("0.0.0.0");
        p.setManualControl(false);
        p.setFirstName("first name");
        p.setLastName("last name");
        p.setMeterName("meter name");
        p.setMeterId("meter id");
        p.setFeedback(false);
        p.setNotificationLevel(1);
        p.setAddress("address");
        p.setGridLocation("grid location");
        p.setLatitude(20.0);
        p.setLongitude(10.0);
        p.setShedPerHourKW(30.0);
        p.setLastPrice(1.0);
        p.setStatus(ClientStatus.OFFLINE.ordinal());
        p.setOfflineWarning(true);
        p.setClient(false);
        p.setParent("parent");

        UpdateParticipantUsecase updater = new UpdateParticipantUsecase(p);
        Participant p2 = (Participant) updater.runCase();

        // Compare to make sure the found participant matches the created/modified one
 	ParticipantUtil.compareParticipants(p, p2);   // asserts equality

        return p;
    }
}
