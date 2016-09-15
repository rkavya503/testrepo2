/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.cpp.CPPProgramEJBBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.cpp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.pss2.core.ProgramEJBBean;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.event.signal.EventSignalEntry;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramRule;
import com.akuacom.pss2.program.participant.ProgramParticipantRule;
import com.akuacom.pss2.rule.Rule.Mode;
import com.akuacom.pss2.rule.Rule.Operator;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.system.SystemManager;

/**
 * The Class CPPProgramEJBBean.
 */
@Stateless
public class CPPProgramEJBBean extends ProgramEJBBean implements CPPProgramEJB.R,
        CPPProgramEJB.L {

    /** The program servicer. */
    @EJB
    private SystemManager.L systemManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJBBean#createProgramObject()
     */
    public Program createProgramObject() {
        return new CPPProgram();
    }

     @Override
    public Set<EventSignal> initializeEvent(Program program, Event event,
            UtilityDREvent utilityDREvent) {
        Set<EventSignal> eventSignals = new HashSet<EventSignal>();

        Signal cppPriceSignal = null;
        for (Signal signal : program.getSignals()) {
            if (signal.getSignalDef().getSignalName().equals("cpp_price")) {
                cppPriceSignal = signal;
            }
        }

        Double value = systemManager.getPss2Properties()
                .getRuleCPPPriceDefaultPrice();

        if (cppPriceSignal == null || value == null || program == null) {
            return eventSignals;
        }

        EventSignal priceProgramSignal = new EventSignal();
        priceProgramSignal.setSignalDef(cppPriceSignal.getSignalDef());
        Set<EventSignalEntry> priceEntries = new HashSet<EventSignalEntry>();

        EventSignalEntry priceEntry = new EventSignalEntry();
        priceEntry.setTime(event.getStartTime());
        priceEntry.setNumberValue(value);
        priceEntries.add(priceEntry);

        priceProgramSignal.setSignalEntries(priceEntries);
        eventSignals.add(priceProgramSignal);

        return eventSignals;
    }

    @Override
    public List<ProgramParticipantRule> createDefaultClientRules(Program program) {
        List<ProgramParticipantRule> rules = new ArrayList<ProgramParticipantRule>();

        List<ProgramRule> progRules = this.getProgramRules(program);
        for (ProgramRule progRule : progRules) {
            CPPUtils.createDefaultClientRules(program, progRule, rules);
        }

        return rules;
    }

    @Override
    protected List<ProgramRule> getProgramRules(Program program) {
        // Get Custom rules
        ArrayList<ProgramRule> progRules = new ArrayList<ProgramRule>();

        Set<ProgramRule> progRuleset = program.getRules();
        if(progRuleset != null && progRuleset.size() > 0 && 
        	systemManager.getPss2Features().isProgramRuleEnabled())
        {
            Iterator<ProgramRule> it = progRuleset.iterator();
            while(it.hasNext()) {
                progRules.add(it.next()) ;
            }

            return progRules;
        }

        ProgramRule pr = new ProgramRule();
        String mode = systemManager.getPss2Properties().getRuleCPPDefaultMode();
        pr.setMode(Mode.valueOf(mode));
        pr.setOperator(Operator.ALWAYS);
        pr.setValue(new Double(0.0));
        pr.setStart(new Date());
        // don't care about these in CCPUtils
        pr.setSource("Program");
        pr.setVariable(null);
        pr.setSortOrder(0);
        pr.setProgram(program);

        progRules.add(pr);

        return progRules;
    }
}
