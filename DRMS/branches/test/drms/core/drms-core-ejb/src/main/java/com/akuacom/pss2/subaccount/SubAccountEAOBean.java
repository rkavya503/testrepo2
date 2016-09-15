/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.subaccount;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.akuacom.ejb.EntityNotFoundException;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */
@Stateless
public class SubAccountEAOBean extends com.akuacom.ejb.BaseEAOBean<SubAccount>
        implements SubAccountEAO.R, SubAccountEAO.L {

    public SubAccountEAOBean() {
        super(SubAccount.class);
    }

    public List<SubAccount> getSubAccounts(String participantName,
            boolean isClient) {
        return em.createNamedQuery("SubAccount.findAllByParticipantName")
                .setParameter("name", participantName).getResultList();
    }

    public SubAccount getSubAccount(String subAccountName,
            String participantName, boolean isClient) {
    	SubAccount ret = null;
        final Query namedQuery = em.createNamedQuery(
				"SubAccount.findAllByParticipantNameAndSubAccountID").setParameter("name",
				participantName).setParameter("subAccountId", subAccountName);
        try {
        	ret = (SubAccount) namedQuery.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
			//return null
		} 

		return ret;

    }

    public void removeSubAccounts(String participantName, boolean isClient) {
        List<SubAccount> list = getSubAccounts(participantName, isClient);
        try {
            for (SubAccount saccount : list) {
                this.delete(saccount);
            }
        } catch (EntityNotFoundException e) {
            throw new EJBException("Entity Not Found!");
        }
    }
}