package com.akuacom.pss2;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;

import com.akuacom.jdbc.SQLExecutor;
import com.akuacom.jdbc.SQLExecutorBean;
import com.akuacom.jdbc.jpa.HibernateManagedSQLExecutor;

@Stateless
public class Pss2SQLExecutorBean extends HibernateManagedSQLExecutor
			implements Pss2SQLExecutor.L, Pss2SQLExecutor.R {
	
	@PersistenceContext(unitName = "core")
	protected EntityManager em;
	
    protected SQLExecutorBean executor;
	
    @PostConstruct
	protected void init() {
    	executor =new SQLExecutorBean();
    }
    
    protected Session getHibernateSession(){
    	//for hibernate only
    	return (Session)em.getDelegate();
    }
    
	@Override
	protected SQLExecutor getSQLExecutor() {
		return executor;
	}
}
