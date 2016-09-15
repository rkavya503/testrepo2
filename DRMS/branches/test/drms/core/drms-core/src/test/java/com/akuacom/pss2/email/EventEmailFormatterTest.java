/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.email;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEmailFormatter;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.event.signal.EventSignalEntry;
import com.akuacom.pss2.program.scertp.SCERTPEvent;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 *
 * @author spierson
 */
public class EventEmailFormatterTest  {
	Logger log= Logger.getLogger(EventEmailFormatterTest.class);
    @Test
    public void testGenerateSCEEmail() {
        EventEmailFormatter formatter = new EventEmailFormatter();

        SCERTPEvent event = new SCERTPEvent();
        event.setStartTime(new Date());
        event.setEventName("Test Event");
        event.setHighTemperature(98.0);
        event.setReportingWeatherStation("KCQT");

        List<Signal> programSignals = new ArrayList<Signal>();

        Signal modeSignal = getModeSignal();
        Signal priceSignal = getPriceSignal();

        Set<SignalEntry> priceEntries = (Set<SignalEntry>) priceSignal.getSignalEntries();
        EventSignalEntry price = new EventSignalEntry();
        price.setTime(new Date());
        price.setNumberValue(39.0);
        priceEntries.add( price);

        programSignals.add(modeSignal);
        programSignals.add(priceSignal);

        // normal event
        String mail = formatter.generateEmailContent( event, programSignals, "localhost", "text" , false, null,null);
        log.debug(mail);
        log.debug("-----------------------------------------------------");

        // station failover to long beach
        event.setReportingWeatherStation("KLGB");
        mail = formatter.generateEmailContent( event, programSignals, "localhost", "text" , false, null,null);
        log.debug(mail);
        log.debug("-----------------------------------------------------");

        // correction primary station
        event.setReportingWeatherStation("KCQT");
        mail = formatter.generateEmailContent( event, programSignals, "localhost", "text" , true, null,null);
        log.debug(mail);
        log.debug("-----------------------------------------------------");

        // correction long beach station
        event.setReportingWeatherStation("KLGB");
        mail = formatter.generateEmailContent( event, programSignals, "localhost", "text" , true, null,null);
        log.debug(mail);
        log.debug("-----------------------------------------------------");

        // no weather info at all
        event.setReportingWeatherStation(null);
        mail = formatter.generateEmailContent( event, programSignals, "localhost", "text" , false, null,null);
        log.debug(mail);
        log.debug("-----------------------------------------------------");

    }

    @Test
    public void testGenerateEmail() {
        EventEmailFormatter formatter = new EventEmailFormatter();

        Event event = new Event();
        event.setStartTime(new Date());

        event.setEventName("Test Event");

        List<Signal> programSignals = new ArrayList<Signal>();

        Signal modeSignal = getModeSignal();
        Signal priceSignal = getPriceSignal();
        Signal pendingSignal = getPendingSignal(new Date());

        Set<SignalEntry> priceEntries = (Set<SignalEntry>) priceSignal.getSignalEntries();
        EventSignalEntry price = new EventSignalEntry();
        price.setTime(new Date());
        price.setNumberValue(39.0);
        priceEntries.add( price);

        programSignals.add(modeSignal);
        programSignals.add(priceSignal);
        programSignals.add(pendingSignal);

        String mail = formatter.generateEmailContent( event, programSignals, "localhost", "text" , false, null,null);

        log.debug(mail);
    }

    private Signal getPendingSignal(Date pendingTime) {
        EventSignal pendingSignal = new EventSignal();
        SignalDef signalDef = new SignalDef();
        signalDef.setSignalName("pending");
        signalDef.setLevelSignal(true);
        pendingSignal.setSignalDef(signalDef);
        Set<EventSignalEntry> pendingEntries = new HashSet<EventSignalEntry>();

        EventSignalEntry pend = new EventSignalEntry();
        pend.setTime(pendingTime);
        pend.setLevelValue("on");
        pend.setParentSignal(pendingSignal);
        pendingEntries.add(pend);
        
        pendingSignal.setSignalEntries(pendingEntries);
        return pendingSignal;
    }

    private Signal getPriceSignal() {
        EventSignal priceSignal = new EventSignal();
        SignalDef priceDef = new SignalDef();
        priceDef.setSignalName("price");
        priceSignal.setSignalDef(priceDef);
        Set<EventSignalEntry> priceEntries = new HashSet<EventSignalEntry>();
        priceSignal.setSignalEntries(priceEntries);
        return priceSignal;
    }

    private Signal getModeSignal() {
        EventParticipantSignal modeSignal = new EventParticipantSignal();
        SignalDef modeDef = new SignalDef();
        modeDef.setSignalName("mode");
        modeSignal.setSignalDef(modeDef);
        Set<EventParticipantSignalEntry> modeEntries = new HashSet<EventParticipantSignalEntry>();
        modeSignal.setSignalEntries(modeEntries);
        return modeSignal;
    }
}
