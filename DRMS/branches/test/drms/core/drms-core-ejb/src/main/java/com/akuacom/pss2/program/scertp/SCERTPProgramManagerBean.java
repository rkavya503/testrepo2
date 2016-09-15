/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.ProgramManagerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.scertp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramPerf;
import com.akuacom.pss2.util.LogUtils;

/**
 * Stateless session bean providing a DRAS Entity BO facade.
 */
@Stateless
public class SCERTPProgramManagerBean implements SCERTPProgramManager.R, SCERTPProgramManager.L {
    /** The Constant log. */
    private static final Logger log = Logger
            .getLogger(SCERTPProgramManagerBean.class);

    /** The program servicer. */
    @EJB
    protected ProgramManager.L programManager;

    @EJB
    protected RTPConfigEAO.L rtpConfigEAO;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.system.SystemManager#lookupProgramBean(java.lang.String)
     */
    public ProgramEJB lookupProgramBean(String programName) throws EJBException {
        Program program = EJBFactory.getBean(ProgramManager.class).getProgramOnly(
                programName);
        if (program == null) {
            ValidationException e = new ValidationException("no program named "
                    + programName);
            throw e;
        }
        String classNameBase = program.getClassName();
        return lookupProgramBeanFromClassName(classNameBase);
    }

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
    private ProgramEJB lookupProgramBeanFromClassName(String classNameBase)
            throws EJBException {
        try {
            final Class<?> aClass = Class.forName(classNameBase);
            return (ProgramEJB) EJBFactory.getBean(aClass);
        } catch (ClassNotFoundException e) {
            // 09.16.2010 Linda: DRMS-1666 modify
            log.debug(LogUtils.createLogEntry("", "",
                    "Can't find program ejb bean for: " + classNameBase, null));
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass()
                    .getName(), e));
            // log.fatal("Can't find program ejb bean for: " + classNameBase,
            // e);
            throw new EJBException(e.getMessage(), e);
        }
    }

    public List<RTPConfig> findRTPConfigs(String programName) {
        ProgramManager programManager = EJBFactory
                .getBean(ProgramManager.class);
        ProgramPerf program = programManager.getProgramPerf(programName);
        return rtpConfigEAO.findRTPConfigs(program.getUUID());
    }

    public double getRate(String programName, Date time) {
        try {
            ProgramEJB programEJB = lookupProgramBean(programName);
            if (programEJB instanceof SCERTPProgramEJB) {
                SCERTPProgramEJB srejb = (SCERTPProgramEJB) programEJB;
                return srejb.getRate(programName, time);
            } else {
                throw new EJBException(
                        "method getRate is not supported for the program: "
                                + programName);
            }
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#saveRTPConfig(java.util.
     * List, java.lang.String)
     */
    public void saveRTPConfig(List<RTPConfig> value, String programName) {
        try {
            // todo bulk delete doesn't work. research this later will delete
            // one by one for now.
            // rtpConfigEAO.deleteRTPConfigs(programName);
            ProgramPerf program = programManager
                    .getProgramPerf(programName);

            List<RTPConfig> listRtpConfigs = rtpConfigEAO
                    .findRTPConfigs(program.getUUID());
            if (listRtpConfigs != null && listRtpConfigs.size() > 0) {
                for (RTPConfig rtpConfig : listRtpConfigs) {
                    rtpConfigEAO.delete(rtpConfig);
                }
            }

            if (programName != null && !programName.isEmpty()) {
                final Program p = programManager.getProgramOnly(programName);
                if (p != null) {
                    for (RTPConfig rtp : value) {
                        rtp.setProgramVersionUuid(p.getUUID());
                        rtpConfigEAO.createRTPConfig(rtp);
                    }

                }
            }

        } catch (Exception e) {
            throw new EJBException("ERROR_RTP_UPDATE", e);
        }

    }

	@Override
	public String getRTPConfigName(String programName, String seasonName,
			double temperature) throws AppServiceException {
		String name=null;
		try {
			Program program=programManager.getProgramOnly(programName);
			name=rtpConfigEAO.getRTPConfigName(program.getUUID(), seasonName, temperature);
		}catch(Exception ex){
			String message = "could not get RTP config name. program name: "
				+ programName + ", season name: " + seasonName
				+ ", temperature: " + temperature;
			throw new AppServiceException(message, ex);
		}
		return name;
	}

	@Override
	public List<RTPConfig> findRTPConfigsByProgramId(String programUUID) {
		 return rtpConfigEAO.findRTPConfigs(programUUID);
	}
}