/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.participant;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.participant.Participant;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */
@Stateless
public class ProgramClientEAOBean extends ProgramUserBaseEAOBean implements
        ProgramClientEAO.R, ProgramClientEAO.L {
    /** The Constant log. */
    private static final Logger log = Logger
            .getLogger(ProgramClientEAOBean.class);

    public ProgramClientEAOBean() {
        super();
    }

    public List<ProgramParticipant> findProgramParticipantsByProgramNames(
            String progName1, String progName2) throws AppServiceException {
        try {
            String query = "select pp from ProgramParticipant pp where pp.programName = ?1 and pp.participant.participantName in (select pp2.participant.participantName from ProgramParticipant pp2 where pp2.programName = ?2  and pp2.participant.client = 1 ) ";

            List<Object> names = new ArrayList<Object>();
            names.add(progName1);
            names.add(progName2);
            List<ProgramParticipant> list = super.getByFilters(query, names);

            return list;
        } catch (Exception ex) {
            throw new AppServiceException("", ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.program.services.ProgramServicer#
     * findProgramParticipantsByProgramNameAndPartName(java.lang.String,
     * java.lang.String)
     */
    public ProgramParticipant findProgramParticipantsByProgramNameAndPartName(
            String progName, String partName) throws AppServiceException {
        ProgramParticipant resPP;
        try {
            Query query = em
                    .createNamedQuery(
                            "ProgramParticipant.findByProgramAndParticipant")
                    .setParameter("programName", progName)
                    .setParameter("participantName", partName)
                    .setParameter("client", true);
            resPP = (ProgramParticipant) query.getSingleResult();
        } catch (Exception e) {
            log.debug("ERROR_PROGRAM_PARTICIPANT_GET: " + progName + "/"
                    + partName + " | " + e.getMessage());
            resPP = null;
        }

        return resPP;

    }

    public ProgramParticipant findProgramParticipantsByProgramNameAndPartName(
            String progName, String partName, int state)
            throws AppServiceException {
        ProgramParticipant resPP;
        try {
            Query query = em
                    .createNamedQuery(
                            "ProgramParticipant.findAllByProgramAndParticipant")
                    .setParameter("programName", progName)
                    .setParameter("participantName", partName)
                    .setParameter("state", state).setParameter("client", true);
            resPP = (ProgramParticipant) query.getSingleResult();
        } catch (Exception e) {
            log.debug("ERROR_PROGRAM_PARTICIPANT_GET: " + progName + "/"
                    + partName + " | " + e.getMessage());
            resPP = null;
        }

        return resPP;

    }

    public ProgramParticipant findClientProgramParticipantsByProgramNameAndPartName(
            String progName, String partName) throws AppServiceException {
        ProgramParticipant resPP;
        try {
            Query query = em
                    .createNamedQuery(
                            "ProgramParticipant.findByProgramAndParticipantForClient")
                    .setParameter("programName", progName)
                    .setParameter("participantName", partName)
                    .setParameter("client", true);
            resPP = (ProgramParticipant) query.getSingleResult();
        } catch (Exception e) {
            log.debug("ERROR_PROGRAM_PARTICIPANT_GET_CLIENT: " + progName + "/"
                    + partName + " | " + e.getMessage());
            resPP = null;
        }

        return resPP;

    }

    protected Participant findPart(String participantName) {
        return participantEAO.findByNameAndClient(participantName, true);
    }
}