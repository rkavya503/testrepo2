/**
 * 
 */
package com.akuacom.ejb;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;


/**
 * Generic interface encouraging naming standards and providing common methods
 * to Managers and a common implementation for those common items like CRUD and
 * Finders.
 * 
 * @author roller
 * 
 */
public interface BaseEAO<EntityType extends BaseEntity> {

	public final scala.Option None = scala.Option.apply(null);
	
	/**
	 * The D in CRUD. If it doesn't succeed it will throw an exception.
	 * 
	 * @param uuid
	 *            identifying what to delete
	 */
	public void delete(String uuid) throws EntityNotFoundException;

	/**Deletes the entity by finding using whatever means.  
	 * @see #get(BaseEntity)
	 * 
	 * @param entity the attached or detached entity. 
	 * @throws EntityNotFoundException when a detached entity cannot be found by any identifier
	 */
	public void delete(EntityType entity) throws EntityNotFoundException;
    public void deleteMany(Object... entities) throws EntityNotFoundException;
    public void delete(Collection entities) throws EntityNotFoundException;
	
	/**
	 * The R in CRUD. Finds the only entity represented by the given ID or null
	 * if not found.
	 * 
	 * @param id
	 * @return the attached entity
	 */
	public EntityType getById(String id) throws EntityNotFoundException;

	/**
	 * Provides the attached entity of what is likely not attached. This is
	 * useful because it will attempt to find the entity by other means than
	 * just the id (i.e. programName, etc)
	 * 
	 * @param entity
	 *            the fully filled unattached entity
	 * @return the attached entity
	 * @throws EntityNotFoundException
	 *             when can't be found by any identifier
	 */
	public EntityType get(EntityType entity) throws EntityNotFoundException;

	/**
	 * Persists the entity given and returns the attached entity for more fun by
	 * managers.
	 * 
	 * @param entity
	 *            to create fully populated
	 * @return the attached entity with the new id
	 */
    public EntityType create(EntityType entity) throws DuplicateKeyException;
    public List<EntityType> createMany(EntityType... entities) throws IllegalArgumentException;
    public Collection<EntityType> create(Collection<EntityType> entities);

	/**
	 * Updates the entity. It will not create the entity so it will throw an
	 * exception if given and ID that is not found in the system.
	 * 
	 * @param entity
	 * @return the attached entity or null
	 */
    public EntityType merge(EntityType entity);
	public EntityType update(EntityType entity) throws EntityNotFoundException;
    public Collection<EntityType> update(Collection<EntityType> entities);
    public void updateMany(EntityType... entities);

    /**Finds all the Entities for the type this EAO is managing.  
	 * 
	 * Use wisely as this list can get large and there is no built-in limit to the number of results.
	 * 
	 * @return
	 */
	public List<EntityType> getAll();
	
	public List<EntityType> getByFilters(String namedQueryName,
			List<Object> filters) throws EntityNotFoundException;


	
	/**
	 * Returns true if entity is bound to the persistence context
	 * @param entity the Entity in question
	 * @return
	 */
	boolean boundQ(EntityType entity);
	
	/**
	 * Returns true if there is a row in the database with this entity's id
	 * @param entity the Entity in question
	 * @return
	 */
	boolean inDbQ(Object entity);
	
	/**
	 * Use only for debugging
	 * @return the injected EntityManager 
	 */
	EntityManager getEm();
	
	
	/**
	 * 
	 * @return Count of EntityType instances in db 
	 */
	long count();
	
	
	/**
	 * @return  Safely checks if parameter entity is deployed
	 * @throws Exception
	 */
	boolean available();
	

	/**
	 * The S in CRUDS. (Actually, C or U) creates or updates a core property
	 * with the values given in the properties
	 * 
	 * @param prop
	 * @return the new or updated property fully populated (especially with ID).
	 * @throws DuplicateKeyException
	 *             if a unique constraint is found during creation
	 */
	public EntityType set(EntityType entity);

}
