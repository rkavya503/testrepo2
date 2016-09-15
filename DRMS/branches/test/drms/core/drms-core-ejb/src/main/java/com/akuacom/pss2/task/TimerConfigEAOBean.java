package com.akuacom.pss2.task;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;

import com.akuacom.ejb.BaseEAO;
import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.contact.Contact;

@Stateless
public class TimerConfigEAOBean extends BaseEAOBean<TimerConfig> implements TimerConfigEAO.L,TimerConfigEAO.R {

	public TimerConfigEAOBean() {
		super(TimerConfig.class);
	}
	@Override
	public TimerConfig getTimerConfig(String timerConfigName){
		TimerConfig result = null;
        try {
        	List<TimerConfig> list =  em.createNamedQuery("TimerConfig.findByName").setParameter("name", timerConfigName).getResultList();
        	if(list.size()>0){
        		result = list.get(0);
        	}
        } catch (Exception e) {
            throw new EJBException("ERROR_TimerConfig_GET1", e);
        }
        return result;
	}
}
