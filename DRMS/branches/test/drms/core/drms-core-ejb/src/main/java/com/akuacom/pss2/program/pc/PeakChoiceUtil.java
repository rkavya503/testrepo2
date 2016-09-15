package com.akuacom.pss2.program.pc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.email.PeakChoiceMessageEntity;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.dbp.BidState;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.util.LogUtils;

public class PeakChoiceUtil {
    private static final Logger log = Logger.getLogger(PeakChoiceUtil.class);

    public static DBPEvent getEvent(PeakChoiceMessageEntity entity) 
    	throws IOException, ParseException {
        String eventName = entity.getEventName();
        String programName = entity.getProgramName();
        String content = entity.getContent();
        final BufferedReader br = new BufferedReader(new StringReader(content));
        String line = br.readLine();
        DBPEvent event = new DBPEvent();
        event.setReceivedTime(new Date());
    	event.setIssuedTime(event.getReceivedTime());
        event.setCurrentBidState(BidState.IDLE);
        int step = 0;

       ParticipantManager participantManager = 
    	   EJBFactory.getBean(com.akuacom.pss2.participant.ParticipantManager.class);

       final List<EventParticipant> eps = new ArrayList<EventParticipant>();
        // find the 10 digit id that's unique in the string layout
        Pattern p = Pattern.compile("\\d{10}");
        while (line != null) {
            if (line.startsWith("Facility:")) {
                step = 1;
//                        } else
//                    if (line.startsWith("Event ID:")) {
//                        eventName = line.substring("Event ID:".length()).trim();
                event.setEventName(eventName);
//                    } else if (line.startsWith("Program:")) {
//                        programName = line.substring("Program:".length()).trim();
                event.setProgramName(programName);
            } else if (line.startsWith("Type:")) {
                step = 4;
            } else if (line.startsWith("Settlement:")) {
                step = 5;
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy, hh:mma z");
                if (line.startsWith("Curtailment Start:")) {
                    String startString = line.substring("Curtailment Start:".length()).trim();
                    event.setStartTime(simpleDateFormat.parse(startString));
                } else if (line.startsWith("Curtailment End:")) {
                    String endString = line.substring("Curtailment End:".length()).trim();
                    event.setEndTime(simpleDateFormat.parse(endString));
                } else if (line.startsWith("Respond By:")) {
                    String resString = line.substring("Respond By:".length()).trim();
                    event.setRespondBy(simpleDateFormat.parse(resString));
                }
            }

            if (step == 1) {
                final Matcher matcher = p.matcher(line);
                if (matcher.find()) {
                    String account = matcher.group();
                    final EventParticipant ep = new EventParticipant();
                    ep.setEvent(event);
                    ep.setParticipant(
                    	participantManager.getParticipantByAccount(account));
                    eps.add(ep);
                }
            }

            line = br.readLine();
        }
        //log.info("content processed");
        //09.20.2010 Frank: DRMS-1659 modify
        log.debug(LogUtils.createLogEntry(
				programName,
				PeakChoiceUtil.class.getName(),
				"content processed",
				"content processed"));

        event.setParticipants(eps);
        return event;
    }
}
