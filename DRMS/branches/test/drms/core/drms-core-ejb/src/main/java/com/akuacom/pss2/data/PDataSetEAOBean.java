/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.program.services.ProgramServicerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.util.LogUtils;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */
@Stateless
public class PDataSetEAOBean extends BaseEAOBean<PDataSet> implements
        PDataSetEAO.R, PDataSetEAO.L {
    private static final Logger log = Logger.getLogger(PDataSetEAOBean.class);

    public PDataSetEAOBean() {
        super(PDataSet.class);
    }

    public PDataSet getDataSetByName(String name) {
        final Query namedQuery = em.createNamedQuery("PDataSet.findByName")
                .setParameter("name", name);

        PDataSet ret = null;
        try {
            List<PDataSet> list = namedQuery.getResultList();
            if (list != null && list.size() > 0) {
                ret = list.get(0);
            }
        } catch (Exception e) {
            // TODO 2992 why were these debugs?
            log.error(LogUtils.createExceptionLogEntry("", this.getClass()
                    .getName(), e));
        }
        return ret;
    }

    public List<String> getDataSetBySource(String sourceUUID)
            throws EntityNotFoundException {
        final Query namedQuery = em.createNamedQuery(
                "PDataSet.getDatasetUUIDByDatasourceOwner").setParameter(
                "sourceUUID", sourceUUID);
        return namedQuery.getResultList();
    }
}