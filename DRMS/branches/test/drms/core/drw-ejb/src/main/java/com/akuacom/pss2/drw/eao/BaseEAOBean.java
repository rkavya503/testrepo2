/**
 * 
 */
package com.akuacom.pss2.drw.eao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.akuacom.pss2.drw.core.AbstractBaseEntity;
import com.akuacom.pss2.drw.exception.DuplicatedKeyException;
import com.akuacom.pss2.drw.exception.EntityNotFoundException;

/**
 *
 */
public abstract class BaseEAOBean<EntityType extends AbstractBaseEntity> implements
BaseEAO<EntityType> {

	@PersistenceContext(unitName = "drw")
	protected EntityManager em;

	private Class<EntityType> entityClass;

	public BaseEAOBean(Class<EntityType> entityClass) {
		this.entityClass = entityClass;
	}

    @SuppressWarnings("unchecked")
	@Override
    public List<EntityType> getAll(String entityClassName) {
    	List<EntityType> result=new ArrayList<EntityType>();
    	
    	Query query = em.createQuery("select e from " + entityClassName + " e");
    	result=query.getResultList();
    	
    	return result;
    }
	
	
	
    @Override
    public EntityType create(EntityType entity) throws DuplicatedKeyException {
    	checkUniqueKey(entity);
    	em.persist(entity);
    	
        return entity;
    }
    
    protected abstract void checkUniqueKey(EntityType entity) throws DuplicatedKeyException;
    
	@Override
    public List<EntityType> create(List<EntityType> entities) throws DuplicatedKeyException {
        List<EntityType> created = new ArrayList<EntityType>();
	    for(EntityType entity : entities ) {
	        create(entity);
	        created.add(entity);
	    }
        return created;
	}
	
	@Override
	public EntityType update(EntityType entity) {
		EntityType updated=em.merge(entity);
		return updated;
	}
	
	@Override
	public void delete(String uuid) throws EntityNotFoundException {
		EntityType entity = find(uuid);
		
		if (entity==null)
			throw new EntityNotFoundException();
		
		em.remove(entity);
	}
	
	@Override
	public void delete(EntityType entity) {
		em.remove(entity);
	}
	
	@Override
	public EntityType find(String uuid) {
		EntityType result = em.find(entityClass, uuid);
		return result;
	}
}
