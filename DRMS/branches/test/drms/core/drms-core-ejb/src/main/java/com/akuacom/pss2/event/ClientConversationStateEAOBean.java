package com.akuacom.pss2.event;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.akuacom.ejb.BaseEAOBean;

@Stateless
public class ClientConversationStateEAOBean extends BaseEAOBean<ClientConversationState> implements ClientConversationStateEAO.R, ClientConversationStateEAO.L {

    public ClientConversationStateEAOBean() {
        super(ClientConversationState.class);
    }

    @Override
    public ClientConversationState findByConversationStateId(int eventStateId) {
    	ClientConversationState res = null;
        Query query = em.createNamedQuery("ClientConversationState.findByConversationStateId")
                .setParameter("stateId", eventStateId);
        try {
        	res = (ClientConversationState) query.getSingleResult();
        } catch (NoResultException e) {
			// ignore
		}
        return res;
    }

    @Override
    public List<ClientConversationState> findByDrasClientId(String drasClientId) {
        Query query = em.createNamedQuery("ClientConversationState.findByDrasClientId")
                .setParameter("clientId", drasClientId);
        return  query.getResultList();
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<ClientConversationState> findByTimedOut() {
        Query query = em.createNamedQuery("ClientConversationState.findByTimedOut");
        query.setParameter("timeOut", new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        query.setMaxResults(120);
        return (List<ClientConversationState>) query.getResultList();
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<ClientConversationState> findByPushAndTimedOut() {
        Query query = em.createNamedQuery("ClientConversationState.findPushClientByTimedOut");
        query.setParameter("timeOut", new Date(System.currentTimeMillis() - (60 * 1000)));
        query.setParameter("push", true);
        query.setMaxResults(120);
        return (List<ClientConversationState>) query.getResultList();
    }
}
