package com.akuacom.pss2.program.pc;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionManagement;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnectionFactory;

import org.apache.log4j.Logger;

import com.akuacom.pss2.email.PeakChoiceMessageEntity;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.util.LogUtils;

@TransactionManagement
@MessageDriven(name = "PeakChoiceMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
@Resources(@Resource(name = "jms/QueueFactory", type = QueueConnectionFactory.class))
public class PeakChoiceMDB implements MessageListener {
    private static final Logger log = Logger.getLogger(PeakChoiceMDB.class);

    @EJB
    ParticipantEAO.L eao;

    @EJB
    EventManager.L em;

    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            final ObjectMessage om = (ObjectMessage) message;
            try {
                final PeakChoiceMessageEntity entity = (PeakChoiceMessageEntity) om
                        .getObject();
                final DBPEvent event = PeakChoiceUtil.getEvent(entity);
                // DRMS-4382, check if the event is already created, only creates if non-exists
                Event e = em.getEventOnly(event.getEventName());
                if (e == null) {
                    // DRMS-4496, if no participant, don't create the event for BestEffort
                    if (event.getProgramName().contains("BestEffort")) {
                        if (event.getParticipants().size() > 0) {
                            createEvent(event.getProgramName(), event);
                        } else {
                            log.warn("The peakchoice email has no participant. Event " + event.getEventName() + " is ignored.");
                            log.debug(entity.getContent());
                        }
                    // DRMS-4496, no such restriction on Committed
                    } else {
                        createEvent(event.getProgramName(), event);
                    }
                }

            } catch (JMSException e) {
                // log.error("unexpected error in parsing peak choice email",
                // e);
                // 09.20.2010 Frank: DRMS-1659 modify
                // TODO:
                log.debug(LogUtils.createExceptionLogEntry("", this.getClass()
                        .getName(), e));
            } catch (IOException e) {
                // log.error("unexpected error in parsing peak choice email",
                // e);
                // 09.20.2010 Frank: DRMS-1659 modify
                log.debug(LogUtils.createExceptionLogEntry("", this.getClass()
                        .getName(), e));
            } catch (ParseException e) {
                // log.error("unexpected error in parsing peak choice email",
                // e);
                // 09.20.2010 Frank: DRMS-1659 modify
                log.debug(LogUtils.createExceptionLogEntry("", this.getClass()
                        .getName(), e));
            }
        }
    }

    private void createEvent(String programName, DBPEvent event) {
        final List<EventParticipant> list = event.getParticipants();
        if (!programName.contains("Committed")) {
            for (EventParticipant ep : list) {
                final String accountNumber = ep.getParticipant()
                        .getAccountNumber();
                try {
                    final Participant participant = eao
                            .getParticipantByAccount(accountNumber);
                    ep.setParticipant(participant);
                } catch (Exception e) {
                    // log.error("failed to find participant", e);
                    // 09.20.2010 Frank: DRMS-1659 modify
                    log.debug(LogUtils.createExceptionLogEntry(programName,
                            this.getClass().getName(), e));
                }
            }
            em.createEvent(event.getProgramName(), event);
        } else {
            // this is for cpp event creation
            Event e = new Event();
            e.setProgramName(event.getProgramName());
            e.setEventName(event.getEventName());
            e.setStartTime(event.getStartTime());
            e.setEndTime(event.getEndTime());

            e.getParticipants().clear();
            final Date now = new Date();
            e.setIssuedTime(now);
            e.setReceivedTime(now);
            em.createEvent(e.getProgramName(), e);
        }
    }
}
