/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.signal;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.pss2.signal.SignalDef;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */
@Stateless
public class ProgramSignalEAOBean extends
        com.akuacom.ejb.BaseEAOBean<ProgramSignal> implements ProgramSignalEAO.R,
        ProgramSignalEAO.L {
    public ProgramSignalEAOBean() {
        super(ProgramSignal.class);
    }

    /** The Constant log. */
    private static final Logger log = Logger
            .getLogger(ProgramSignalEAOBean.class);

    
    /*public List<SignalDef> findSignals(String programName) {
        try {
            List<SignalDef> signals = new ArrayList<SignalDef>();
            List values = new ArrayList();
            values.add(programName);

            List<ProgramSignal> psigs = super
                    .getByFilters(
                            "SELECT c.signals FROM Program c where c.programName = ?1 ",
                            values);

            if (psigs != null) {
                for (ProgramSignal psig : psigs) {
                    signals.add(psig.getSignalDef());
                }
            }
            return signals;
        } catch (Exception ex) {
            throw new EJBException("", ex);
        }
    }*/

}