package com.akuacom.pss2.signal;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.client.ClientManualSignal;
import com.akuacom.pss2.core.CoreConfig;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.util.LogUtils;

@Stateless
public class SignalManagerBean implements SignalManager.R, SignalManager.L {

    @EJB
    private SignalEAO.L signalEAO;

    private static final Logger log = Logger.getLogger(SignalManagerBean.class);

    /**
     * Lookup program bean from class name.
     * 
     * @param classNameBase
     *            the class name base
     * 
     * @return the program ejb
     * 
     * @throws javax.ejb.EJBException
     *             the EJB exception
     */
    public ProgramEJB lookupProgramBeanFromClassName(String classNameBase)
            throws EJBException {
        try {
            final Class<?> aClass = Class.forName(classNameBase);
            return (ProgramEJB) EJBFactory.getBean(aClass);
        } catch (ClassNotFoundException e) {
            // Linda: DRMS-1663
            log.debug(LogUtils.createLogEntry("", this.getClass().getName(),
                    "Can't find program ejb bean for: " + classNameBase, null));
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass()
                    .getName(), e));
            // log.fatal("Can't find program ejb bean for: " + classNameBase,
            // e);
            throw new EJBException(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.signal.SignalManager#lookupProgramBean(java.lang.String)
     */
    public ProgramEJB lookupProgramBean(String programName) throws EJBException {
        ProgramManager programManager = EJBFactory
                .getBean(ProgramManager.class);
        Program program = programManager.getProgramOnly(programName);
        if (program == null) {
            ValidationException e = new ValidationException("no program named "
                    + programName);
            throw e;
        }
        String classNameBase = program.getClassName();
        return lookupProgramBeanFromClassName(classNameBase);
    }

    @Override
    public List<com.akuacom.pss2.signal.SignalDef> findSignals() {
        try {
            return signalEAO.findSignals();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<ClientManualSignal> findSignalsWithDefaults(Participant p) {
        List<SignalDef> sigs = this.findSignals();
        List<ClientManualSignal> signalList = new ArrayList<ClientManualSignal>();

        for (SignalDef sig : sigs) {
            if (sig == null)
                continue;
            ClientManualSignal signalEAO = new ClientManualSignal();
            signalEAO.setName(sig.getSignalName());
            signalEAO.setParticipant(p);
            for (SignalLevelDef sl : sig.getSignalLevels()) {
                if (sl == null || !sl.isDefaultValue())
                    continue;
                signalEAO.setValue(sl.getStringValue());
            }
            if (signalEAO.getValue() != null)
                signalList.add(signalEAO);
        }

        return signalList;
    }

    @Override
    public com.akuacom.pss2.signal.SignalDef findSignal(String signalName) {
        try {
            return signalEAO.getSignal(signalName);
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public void saveSignals(List<com.akuacom.pss2.signal.SignalDef> signals) {
        try {
            signalEAO.setSignal(signals);
        } catch (EJBException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.signal.SignalManager#addSignal(com.akuacom.pss2.core
     * .model.Signal)
     */
    @Override
    public void addSignal(SignalDef signal) {
        signalEAO.addSignal(signal);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.signal.SignalManager#getSignals()
     */
    @Override
    public List<SignalDef> getSignals() {
        try {
            Properties config = null;

            List<com.akuacom.pss2.signal.SignalDef> signals = signalEAO
                    .findSignals();
            if (signals == null || signals.size() <= 0) {
                config = CoreConfig.loadSignalProperties();
            }

            return signalEAO.importSignal(config);

        } catch (Exception ex) {
            String message = "error getting signals";
            throw new EJBException(message, ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramNPCache#getSignalsAsString()
     */
    @Override
    public List<String> getSignalsAsString() {
        try {
            List<String> signalNames = new ArrayList<String>();

            Properties config = null;

            List<com.akuacom.pss2.signal.SignalDef> signals = signalEAO
                    .findSignals();
            if (signals == null || signals.size() <= 0) {
                config = CoreConfig.loadSignalProperties();
            }

            List<SignalDef> sigs = signalEAO.importSignal(config);

            for (SignalDef sig : sigs) {
                signalNames.add(sig.getSignalName());
            }

            return signalNames;
        } catch (Exception ex) {
            String message = "error getting signals";
            throw new EJBException(message, ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramNPCache#getSignal(java.lang.String)
     */
    @Override
    public SignalDef getSignal(String signalName) {
        try {
            if (signalName == null) {
                String message = "signal name is null";
                throw new EJBException(message);
            }

            SignalDef result = signalEAO.getSignal(signalName);
            return result;
        } catch (Exception ex) {
            String message = "error getting signal " + signalName;
            throw new EJBException(message, ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramNPCache#removeSignal(java.lang.String)
     */
    @Override
    public void removeSignal(String signalName) {
        String message = "remove signal not supported";
        throw new EJBException(message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.ProgramNPCache#updateSignal(com.akuacom.pss2.core
     * .model.Signal)
     */
    @Override
    public void updateSignal(SignalDef signal) {
        if (signal == null) {
            String message = "signal is null";
            throw new EJBException(message);
        }

        final String signalName = signal.getSignalName();
        if (signalName == null) {
            String message = "signal name is null";
            throw new EJBException(message);
        }

        String message = "update signal not supported";
        throw new EJBException(message);
    }

	@Override
	public List<SignalDef> findSignalsPerf() {
		List<SignalDef> signals=null;
		try {
			signals = signalEAO.findSignalsPerf();
		} catch (AppServiceException e) {
			e.printStackTrace();
		}
		
		return signals;
	}

	@Override
	public List<SignalDef> getSignals(List<String> signalName) {
		try {
            return signalEAO.getSignals(signalName);
        } catch (Exception e) {
            return null;
        }
	}
}