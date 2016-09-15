package com.akuacom.pss2.drw;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;

import com.akuacom.jdbc.SQLExecutor;
import com.akuacom.jdbc.SQLExecutorBean;
import com.akuacom.jdbc.jpa.HibernateManagedSQLExecutor;

@Stateless	
public class DrwSQLExecutorBean extends HibernateManagedSQLExecutor implements
		DrwSQLExecutor.L, DrwSQLExecutor.R {

	
	@PersistenceContext(unitName = "drw")
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