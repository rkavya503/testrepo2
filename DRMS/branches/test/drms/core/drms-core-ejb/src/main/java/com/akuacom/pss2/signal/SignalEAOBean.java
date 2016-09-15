/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.signal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.util.EventInfoInstance;
import com.akuacom.pss2.util.LogUtils;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */
@Stateless
public class SignalEAOBean extends com.akuacom.ejb.BaseEAOBean<SignalDef>
        implements SignalEAO.R, SignalEAO.L {
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(SignalEAOBean.class);

    public SignalEAOBean() {
        super(SignalDef.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#getSignal(java.lang.String)
     */
    public SignalDef getSignal(String name) throws AppServiceException {
        try {
            final Query query = em.createNamedQuery(
                    "SignalDef.findBySignalName").setParameter("signalName",
                    name);
            final List<SignalDef> list = query.getResultList();
            if (list != null && list.size() > 0) {
                return list.get(0);
            } else {
                return null;
            }
        } catch (Exception ex) {
            throw new AppServiceException("", ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.program.services.ProgramServicer#findSignals()
     */
    public List<SignalDef> findSignals() throws AppServiceException {
        try {
            final Query query = em.createNamedQuery("SignalDef.findAll");
			final List<SignalDef> list = query.getResultList();
            return list;
        } catch (Exception ex) {
            throw new AppServiceException("", ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#setSignal(java.util.List)
     */
    public void setSignal(List<SignalDef> signals) {
        try {
            if (signals != null) {
                for (SignalDef sig : signals) {
                    if (sig.getSignalName().equalsIgnoreCase("mode")
                            || sig.getSignalName().equalsIgnoreCase("pending")) {
                        continue;
                    } else {
                        if (sig.getUUID() != null && !sig.getUUID().isEmpty()) {
                            /*
                             * not handling update at this moment. Signal s =
                             * (Signal) super.get(SignalImpl.class,
                             * sig.getUUID());
                             * s.setSignalLevels(sig.getSignalLevels());
                             * s.setType(sig.getType()); super.update(s);
                             */
                        } else {
                            super.create(sig);
                        }
                    }
                }

                /*
                 * todo not handling delete at this point
                 */
            }

        } catch (Exception e) {
            throw new EJBException("ERROR_SIGNAL_SET", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.program.services.ProgramServicer#importSignal(java.util.
     * Properties)
     */
    public List<SignalDef> importSignal(Properties config)
            throws AppServiceException {
        try {
            List<SignalDef> returnList = new ArrayList<SignalDef>();

            if (config == null) {
                List<SignalDef> sigs = this.findSignals();
                if (sigs == null || sigs.size() < 1)
                    throw new AppServiceException("no signals defined!");
                for (int i = 0; i < sigs.size(); i++) {
                    SignalDef sig = sigs.get(i);
                    returnList.add(sig);
                }
                return returnList;
            }

            int number = new Integer(config.getProperty("number"));

            for (int i = 0; i < number; i++) {
                String name = config.getProperty("s" + i + ".name");
                SignalDef signal = this.getSignal(name);
                if (signal == null) {
                    signal = new SignalDef();
                    signal.setType(config.getProperty("s" + i + ".type"));
                    signal.setSignalName(config.getProperty("s" + i + ".name"));

                    Boolean levelSignal = new Boolean(false);
                    Boolean inputSignal = new Boolean(false);
                    try {
                        levelSignal = Boolean.parseBoolean(config
                                .getProperty("s" + i + ".levelSignal"));
                        inputSignal = Boolean.parseBoolean(config
                                .getProperty("s" + i + ".inputSignal"));
                    } catch (Exception ex) {
                        // no-op already defaulted
                    }

                    signal.setLevelSignal(levelSignal);
                    signal.setInputSignal(inputSignal);

                    if (EventInfoInstance.SignalType.LOAD_LEVEL.name().equals(
                            signal.getType())) {

                        String s = config.getProperty("s" + i + ".levels");
                        String defaultLevel = config.getProperty("s" + i
                                + ".defaultLevel");
                        String[] levels = s.split(",");
                        Set<SignalLevelDef> levelSet = new HashSet<SignalLevelDef>();
                        for (int j = 0; j < levels.length; j++) {
                            SignalLevelDef level = new SignalLevelDef();
                            level.setStringValue(levels[j]);
                            if (levels[j].equalsIgnoreCase(defaultLevel)) {
                                level.setDefaultValue(true);
                            } else {
                                level.setDefaultValue(false);
                            }
                            levelSet.add(level);
                            level.setSignal(signal);
                        }
                        signal.setSignalLevels(levelSet);

                    } else {
                        SignalLevelDef level = new SignalLevelDef();
                        final String dlString = config.getProperty("s" + i
                                + ".defaultLevel");
                        level.setStringValue(dlString);
                        level.setDefaultValue(true);
                        Set<SignalLevelDef> levelSet = new HashSet<SignalLevelDef>();
                        levelSet.add(level);
                        signal.setSignalLevels(levelSet);
                        level.setSignal(signal);
                    }
                    signal = em.merge(signal);
                    returnList.add(signal);
                } else {
                    returnList.add(signal);
                }
            }
            return returnList;
        } catch (Exception e) {
            throw new AppServiceException("ERROR_PROGRAM_CREATE", e);
        }
    }

    public void addSignal(SignalDef signal) {
        if (signal == null) {
            String message = "signal is null";
            throw new EJBException(message);
        }

        try {
            final Query query = em.createNamedQuery(
                    "SignalDef.findBySignalName").setParameter("signalName",
                    signal.getSignalName());
            final List<SignalDef> list = query.getResultList();
            if (list.size() > 0) {
                String message = "signal " + signal.getSignalName() + " exists";
                throw new EJBException(message);
            }
            em.persist(signal);
        } catch (Exception e) {
            String message = "error creating signal " + signal;
            throw new EJBException(message, e);
        }
    }

	@Override
	public List<SignalDef> findSignalsPerf() throws AppServiceException {
		  try {
	            final Query query = em.createNamedQuery("SignalDef.findSignalPerf");
				final List<SignalDef> list = query.getResultList();
	            return list;
	        } catch (Exception ex) {
	            throw new AppServiceException("", ex);
	        }
	}

	@Override
	public List<SignalDef> getSignals(List<String> name) throws AppServiceException {
		try {
            final Query query = em.createNamedQuery(
                    "SignalDef.findSignalPerfByNames").setParameter("signalNames",
                    name);
            List<SignalDef> list = query.getResultList();
            if(list==null) list = Collections.EMPTY_LIST;
            
            return list;
        } catch (Exception ex) {
            throw new AppServiceException("", ex);
        }
	    
	}
}