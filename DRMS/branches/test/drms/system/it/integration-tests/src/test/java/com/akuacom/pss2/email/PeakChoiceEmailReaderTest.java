package com.akuacom.pss2.email;

import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.pss2.core.ParticipantManagerFixture;

public class PeakChoiceEmailReaderTest extends ParticipantManagerFixture {

    @Ignore
    @Test
    public void testCreatingEvents() {
        PeakChoiceEmailReader reader = new PeakChoiceEmailReaderBean();
        reader.poll();
/*
        for (DBPEvent event : events) {
            event.setProgramName("PeakChoice");
            final Date date = new Date();
            event.setIssuedTime(date);
            event.setReceivedTime(date);
            final Calendar cal = Calendar.getInstance();
            // set start time
            final Date start = event.getStartTime();
            cal.setTime(start);
            cal.set(Calendar.DATE, 7);
            cal.set(Calendar.MONTH, Calendar.APRIL);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            event.setStartTime(cal.getTime());
            // set end time
            final Date end = event.getEndTime();
            cal.setTime(end);
            cal.set(Calendar.DATE, 7);
            cal.set(Calendar.MONTH, Calendar.APRIL);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            event.setEndTime(cal.getTime());
            // set respond-by time
            final Date rb = event.getRespondBy();
            if (rb == null) {
                event.setRespondBy(date);
            } else {
                cal.setTime(rb);
                cal.set(Calendar.DATE, 6);
                cal.set(Calendar.MONTH, Calendar.APRIL);
                event.setRespondBy(cal.getTime());
            }

            final List<EventParticipant> list = event.getParticipants();
            for (EventParticipant ep : list) {
                final String accountNumber = ep.getAccountNumber();
                final Participant participant = pm.getParticipantByAccount(accountNumber);
                ep.setParticipantName(participant.getParticipantName(), participant.isClient());
            }
            em.createEvent("PeakChoice", event);
        }
*/
    }

}
