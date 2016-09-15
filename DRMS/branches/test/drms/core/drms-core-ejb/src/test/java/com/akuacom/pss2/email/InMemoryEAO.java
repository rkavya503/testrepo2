package com.akuacom.pss2.email;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJBException;
import javax.persistence.EntityManager;

import scala.Option;
import scala.Some;

import com.akuacom.ejb.BaseEAO;
import com.akuacom.ejb.BaseEntity;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;

public class InMemoryEAO<EntityType extends BaseEntity> implements BaseEAO<EntityType> {
	
	@Override
    public void updateMany(EntityType... entities) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteMany(Object... entities) throws EntityNotFoundException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<EntityType> createMany(EntityType... entities) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
	public boolean boundQ(EntityType entity) {
		return false;
	}
	
	@Override
	public EntityManager getEm() {
		return null;
	}
	

	protected List<EntityType> inmemorydb = new ArrayList<EntityType>();
	
	@Override
	public EntityType create(EntityType entity) throws DuplicateKeyException {
		for(EntityType exist:inmemorydb){
			if(exist.equals(entity)){
				throw new DuplicateKeyException();
			}
		}
		entity.setUUID(UUID.randomUUID().toString());
		inmemorydb.add(entity);
		return entity;
	}
	
	@Override
	public void delete(String uuid) throws EntityNotFoundException {
		EntityType ent= null;
		for(EntityType entity:inmemorydb){
			if(entity.getUUID().equals(uuid)){
				ent=entity;
				break;
			}
		}
		if(ent==null){
			throw new EntityNotFoundException(uuid);
		}
		inmemorydb.remove(ent);
	}
	
	@Override
	public void delete(EntityType entity) throws EntityNotFoundException {
		if(entity.getUUID()==null)
			throw new EntityNotFoundException(entity.getUUID());
		delete(entity.getUUID());
	}

	@Override
	public EntityType get(EntityType entity) throws EntityNotFoundException {
		EntityType ent= null;
		for(EntityType exist:inmemorydb){
			if(exist.getUUID().equals(entity.getUUID())){
				ent=entity;
				break;
			}
		}
		if(ent==null){
			throw new EntityNotFoundException(entity.getUUID());
		}
		return ent;
	}
	
	@Override
	public boolean inDbQ(Object entity) {
		return inmemorydb.contains(entity);
	}
	
	public Option<EntityType> find(String id) {
		EntityType ent= null;
		for(EntityType entity:inmemorydb){
			if(entity.getUUID().equals(id)){
				ent=entity;
				break;
			}
		}
		return ent == null ? None : new Some<EntityType>(ent);
	}


	@Override
	public List<EntityType> getAll() {
		return inmemorydb;
	}

	@Override
	public List<EntityType> getByFilters(String namedQueryName,
			List<Object> filters) throws EntityNotFoundException {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public EntityType getById(String id) throws EntityNotFoundException {
		EntityType ent= null;
		for(EntityType entity:inmemorydb){
			if(entity.getUUID().equals(id)){
				ent=entity;
				break;
			}
		}
		if(ent==null){
			throw new EntityNotFoundException(id);
		}
		return ent;
	}

	@Override
	public EntityType merge(EntityType entity) {
		try {
			EntityType ent=get(entity);
		} catch (EntityNotFoundException e) {
			try {
				return create(entity);
			} catch (DuplicateKeyException e1) {
			}
		}
		return entity;
	}

	@Override
	public EntityType update(EntityType entity) throws EntityNotFoundException {
		EntityType ent= null;
		for(EntityType exist:inmemorydb){
			if(exist.getUUID().equals(entity.getUUID())){
				ent=entity;
				break;
			}
		}
		if(ent==null){
			throw new EntityNotFoundException(entity.getUUID());
		}
		return ent;
	}

	@Override
	public void delete(Collection entities) throws EntityNotFoundException {
		for(Object e : entities) {
			inmemorydb.remove(e);
		}
		
	}

	@Override
	public long count() {
		return inmemorydb.size();
	}
	
    public boolean available() {
        return true;
    }

    @Override
    public Collection<EntityType> create(Collection<EntityType> entities) {
        for(EntityType t : entities) {
            try {
                create(t);
            } catch (DuplicateKeyException e) {
                e.printStackTrace();
            }
        }
        return entities;
    }

    @Override
    public Collection<EntityType> update(Collection<EntityType> entities) {
        for(EntityType t : entities) {
            try {
                update(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entities;
    }
    

	@Override	
	public EntityType set(EntityType entity) {
		try {
			return update(entity);
		} catch (EntityNotFoundException e) {
			// fine, let's create instead.
			try {
				return create(entity);
			} catch (DuplicateKeyException e1) {
				throw new EJBException(
						entity
								+ " not found, but yet can't create due to duplication.  Finders are not complete.",
						e1);
			}
		}
	}


}
