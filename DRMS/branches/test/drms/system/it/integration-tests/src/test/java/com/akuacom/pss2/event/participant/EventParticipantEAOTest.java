package com.akuacom.pss2.event.participant;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.participant.ParticipantUtil;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.test.TestUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

public class EventParticipantEAOTest extends JBossFixture {

    public static final int PARTICIPANT_USER_MAX_LENGTH = 99;

    protected static ParticipantEAO pe;
    protected static EventEAO ee;
    protected static EventParticipantEAO epe;

    @BeforeClass
    public static void setUpManagers() throws Exception {
        pe = lookupSessionRemote(ParticipantEAO.class);
        assertNotNull("Did not get ParticipantEAO in setup", pe);
        ee = lookupSessionRemote(EventEAO.class);
        assertNotNull("Did not get EventEAO in setup", ee);
        epe = lookupSessionRemote(EventParticipantEAO.class);
        assertNotNull("Did not get EventParticipantEAO in setup", epe);
    }

    @Test
    public void testAggregationMethods() {
        try {
            // set up entities
            Participant p1 = generateParticipant();
            Assert.assertNotNull(p1);
            Participant p2 = generateParticipant();
            Assert.assertNotNull(p2);
            Participant p3 = generateParticipant();
            Assert.assertNotNull(p3);
            Participant p4 = generateParticipant();
            Assert.assertNotNull(p4);

            Event e = generateEvent();
            Assert.assertNotNull(e);

            EventParticipant ep1 = generateEventParticipant(p1, e);
            Assert.assertNotNull(ep1);
            EventParticipant ep2 = generateEventParticipant(p2, e);
            Assert.assertNotNull(ep2);
            EventParticipant ep3 = generateEventParticipant(p3, e);
            Assert.assertNotNull(ep3);
            EventParticipant ep4 = generateEventParticipant(p4, e);
            Assert.assertNotNull(ep4);
            

            // test tree operations
            epe.addChild(ep1, ep2);
            epe.addChild(ep1, ep3);
            ep2 = epe.findByKeys(e.getEventName(), p2.getParticipantName(), false).get(0);
            epe.addChild(ep2, ep4);

            Set<EventParticipant> descendants = epe.getFlatDescendants(ep1);
            Assert.assertEquals("test", descendants.size(), 3);

            descendants = epe.getDescendants(ep1);
            Assert.assertEquals("test", descendants.size(), 2);

            descendants = epe.getDescendants(ep2);
            Assert.assertEquals("test", descendants.size(), 1);

            epe.addChild(ep1, ep4);

            descendants = epe.getDescendants(ep1);
            Assert.assertEquals("test", descendants.size(), 3);

            descendants = epe.getDescendants(ep2);
            Assert.assertEquals("test", descendants.size(), 0);
            

            // clean up entities
            epe.delete(ep2);
            epe.delete(ep3);
            epe.delete(ep4);
            epe.delete(ep1);

            ee.delete(e);

            pe.delete(p1);
            pe.delete(p2);
            pe.delete(p3);
            pe.delete(p4);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Event generateEvent() throws DuplicateKeyException {
        Event e = new Event();
        e.setCreationTime(new Date());
        e.setEndTime(new Date());
        e.setEventName("test");
        e.setIssuedTime(new Date());
        e.setManual(true);
        e.setNearTime(new Date());
        e.setProgramName("test");
        e.setReceivedTime(new Date());
        e.setStartTime(new Date());
        e.setEventStatus(EventStatus.NONE);

        e = ee.create(e);
        return e;
    }

    private EventParticipant generateEventParticipant(Participant p1, Event e) throws DuplicateKeyException {
        EventParticipant ep1 = new EventParticipant();
        ep1.setEvent(e);
        ep1.setParticipant(p1);
        ep1 = epe.create(ep1);
        return ep1;
    }

    private Participant generateParticipant() throws DuplicateKeyException {
        Participant p1 = ParticipantUtil.getParicipantInstance(UUID.randomUUID().toString());
        p1 = pe.create(p1);
        return p1;
    }

}
