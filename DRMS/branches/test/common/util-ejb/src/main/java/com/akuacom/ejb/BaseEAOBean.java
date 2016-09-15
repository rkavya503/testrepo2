/**
 * 
 */
package com.akuacom.ejb;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.NoSuchEntityException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import scala.Option;
import scala.Some;

import com.akuacom.ejb.search.AkuaCursor;
import com.akuacom.ejb.search.SearchCriterion;
import com.akuacom.ejb.search.SearchHandler;
import com.akuacom.ejb.util.EjbUtil;
import com.akuacom.utils.lang.Dbg;
import com.akuacom.utils.lang.FieldUtil;

/**
 * The Base implementation of the BaseEAO to provide common methods for
 * providing CRUD to your specific EntityTypes.
 * 
 * @author roller
 * 
 */
public abstract class BaseEAOBean<EntityType extends BaseEntity> implements
		BaseEAO<EntityType> {

	/** The Entity Manager. */
	// TODO: reference "core" string to some constant declared somewhere
	@PersistenceContext(unitName = "core")
	protected EntityManager em;

	private Logger log = Logger.getLogger("BaseEAOBean");

	public BaseEAOBean(Class<EntityType> entityClass) {
		this.entityClass = entityClass;
	}

	private Class<EntityType> entityClass;

	@Override
	public EntityType get(EntityType entity) throws EntityNotFoundException {
		EntityType found;
		try {
			found = getById(entity.getUUID());
		} catch (NoSuchEntityException e) {
			throw new EntityNotFoundException(e);
		}
		return found;
	}

	public Option<EntityType> find(String id) {
		EntityType val = em.find(entityClass, id);
		return val == null ? None : new Some<EntityType>(val);
	}

    @Override
    public EntityType merge(EntityType entity)  {
        return em.merge(entity);
    }
    
    @Override
    public EntityType update(EntityType entity) throws EntityNotFoundException {

		String uuid = entity.getUUID();
		if (uuid == null) {

			// let's find by something besides the ID
			// will throw not found exception if unable to identify
			EntityType found = getByOtherIdentifiers(entity);
			// transfer the known identifier to the given entity since the
			// entity provided is using
			// a business key to identify the entity, not the UUID
			entity.setUUID(found.getUUID());
		} else {
			// just make sure entity exists because merge will apparently create
			// one even if it is a bogus id
			getById(uuid);
		}
       return em.merge(entity);

    }

    //            

    @Override
    public Collection<EntityType> update(Collection<EntityType> entities) {
        try {
            Collection<EntityType> updated = (Collection<EntityType>) entities.getClass().newInstance();
            for(EntityType ent : entities) {
                updated.add(em.merge(ent));
            }
            return updated;
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void updateMany(EntityType... entities) {
        for(EntityType ent : entities) {
            em.merge(ent);
        }
    }    

	@SuppressWarnings("unchecked")
	@Override
	public List<EntityType> getAll() {
		StringBuffer queryValue = new StringBuffer();
		queryValue.append("select e from ");
		queryValue.append(entityClass.getSimpleName());
		queryValue.append(" e");
		Query query = em.createQuery(queryValue.toString());
		List<EntityType> result = query.getResultList();
		return result;
	}

	/**
	 * A useful method for a subclass to provide finders without much code in a
	 * stanard way.
	 * 
	 * @param namedQueryName
	 *            query
	 * @param parameterKey
	 *            key
	 * @param identifier
	 *            id
	 * @return entity
	 * @throws EntityNotFoundException
	 *             enity not found
	 */
	@SuppressWarnings("unchecked")
	public EntityType getByKey(String namedQueryName, String parameterKey,
			Object identifier) throws EntityNotFoundException {
		Query query = em.createNamedQuery(namedQueryName).setParameter(
				parameterKey, identifier);
		EntityType property;
		try {
			property = (EntityType) query.getSingleResult();
		} catch (NoResultException e) {
			throw new EntityNotFoundException(parameterKey, identifier, e);
		}
		return property;
	}

	@SuppressWarnings("unchecked")
	public List<EntityType> getByFilters(String namedQueryName,
			List<Object> filters) throws EntityNotFoundException {
		Query query = em.createQuery(namedQueryName);
		if (filters != null || filters.size() > 0) {
			for (int i = 0; i < filters.size(); i++)
				query.setParameter(i + 1, filters.get(i));
		}

		List<EntityType> list;
		try {
			list = (List<EntityType>) query.getResultList();
		} catch (NoResultException e) {
			throw new EntityNotFoundException(namedQueryName, filters, e);
		}
		return list;
	}

	// TODO implement this
	protected EntityType transferBaseProperties(EntityType from, EntityType to) {

		return to;
	}

	/**
	 * Each entity may have other identifiers (besides the uuid). This method
	 * should access the given entity for other identifiers and call the
	 * appropriate finder methods to find the entity by the other identifiers.
	 *
	 * @param entity
	 *            entity
	 * @return the found entity or null if none found
	 * @throws EntityNotFoundException
	 *             entity not found
	 */
	protected EntityType getByOtherIdentifiers(EntityType entity)
			throws EntityNotFoundException {
		throw new EntityNotFoundException(
				"No additional finders are avialable.  Override to provide your own.");
	}

	/**
	 * Given the UUID this will return the Entity represented by that ID.
	 * 
	 * @param uuid
	 *            the UUID of the Entity desired.
	 */
	@Override
	public EntityType getById(String uuid) throws EntityNotFoundException {
		EntityType result = em.find(entityClass, uuid);
		if (result == null) {
			throw new EntityNotFoundException(uuid);
		}
		return result;
	}

	/**
	 * Useful method for children to use since generated code sometimes returns
	 * a collection when only one is expected.
	 * 
	 * @param collection
	 *            the result of the finder
	 * @return the first item from the iterator.
	 */
	public EntityType getFirstFromCollection(Collection<EntityType> collection,
			String identifier) throws EntityNotFoundException {
		if (collection.isEmpty()) {
			throw new EntityNotFoundException(identifier);
		} else {
			return collection.iterator().next();
		}

	}

    @Override
    public EntityType create(EntityType entity) throws DuplicateKeyException {

    	entity.deleteUUID();
    	em.persist(entity);
        return entity;
    }
    
	@Override
    public Collection<EntityType> create(Collection<EntityType> entities) {
        try {
            Collection<EntityType> created = (Collection<EntityType>) entities.getClass().newInstance();
    	    for(EntityType ent : entities ) {
    	        em.persist(ent);
    	        created.add(ent);
    	    }
            return created;
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
	}
	
	@Override
	public List<EntityType> createMany(EntityType... entities) throws IllegalArgumentException {
	    List<EntityType> list = new ArrayList<EntityType>();
	    for(EntityType e : entities) {
	        em.persist(e);
	        list.add(e);
	    }
	    return list;
	}
	

	@Override
	public void delete(String uuid) throws EntityNotFoundException {
		EntityType entity = getById(uuid);
		em.remove(entity);

	}

	@Override
	public void delete(EntityType entity) throws EntityNotFoundException {
		delete(entity.getUUID());
	}

	public void runNativeSql(String sql) {
		em.createNativeQuery(sql).executeUpdate();
	}

	static public SearchHandler search(final SearchHandler searchHandler,
			String queryString, String countQueryString,
			final List<Object> values, EntityManager em)
			throws EntityNotFoundException {
		SearchHandler ret = null;
		// TODO: not using generics here because of Query.getResults doesn't use
		// them
		List results = new ArrayList();
		try {
			if (queryString != null && !queryString.equalsIgnoreCase("")) {
				List<SearchCriterion> searchCriteria = searchHandler
						.getCriteria();
				if (searchCriteria != null) {
					int preCount = values.size();
					for (int i = 0; i < searchCriteria.size(); i++) {
						SearchCriterion searchCriterion = (SearchCriterion) searchCriteria
								.get(i);
						if (searchCriterion.getFieldName() != null
								&& !searchCriterion.getFieldName()
										.equalsIgnoreCase("")) {
							if (searchCriterion.getJoinOperator() == null
									|| searchCriterion.getJoinOperator()
											.equalsIgnoreCase("")
									|| searchCriterion.getJoinOperator()
											.equalsIgnoreCase("AND")) {
								queryString = queryString + " AND "
										+ searchCriterion.getFieldName() + " "
										+ searchCriterion.getOperator() + " "
										+ "?" + (preCount + i + 1);
								countQueryString = countQueryString + " AND "
										+ searchCriterion.getFieldName() + " "
										+ searchCriterion.getOperator() + " "
										+ "?" + (preCount + i + 1);
							} else if (searchCriterion.getJoinOperator()
									.equalsIgnoreCase("OR")) {
								int lastAndIndex = queryString
										.lastIndexOf(" AND ");
								String lastAndPart = queryString
										.substring(lastAndIndex);
								queryString = queryString.substring(0,
										lastAndIndex)
										+ " AND ("
										+ lastAndPart.substring(5)
										+ " "
										+ searchCriterion.getJoinOperator()
										+ " "
										+ searchCriterion.getFieldName()
										+ " "
										+ searchCriterion.getOperator()
										+ " " + "?" + (preCount + i + 1) + " )";
								countQueryString = countQueryString.substring(
										0, lastAndIndex)
										+ " AND ("
										+ lastAndPart.substring(5)
										+ " "
										+ searchCriterion.getJoinOperator()
										+ " "
										+ searchCriterion.getFieldName()
										+ " "
										+ searchCriterion.getOperator()
										+ " " + "?" + (preCount + i + 1) + " )";
							}

							values.add(searchCriterion.getFieldValue());
						}

					}

					if (searchHandler.getSortField() != null
							&& !searchHandler.getSortField().equalsIgnoreCase(
									"")) {
						queryString = queryString + " order by "
								+ searchHandler.getSortField() + " "
								+ searchHandler.getSortByDirection();
					}

				}
			}

			AkuaCursor cursor = searchHandler.getCursor();
			if (cursor != null) {
				Query query = em.createQuery(countQueryString);
				if (values != null) {
					if (values != null || values.size() > 0) {
						for (int i = 0; i < values.size(); i++)
							query.setParameter(i + 1, values.get(i));
					}
				}

				int count = ((Long) query.getSingleResult()).intValue();
				cursor.setTotal(count);

				if (cursor.getStartIndex() > count) {
					throw new EntityNotFoundException("Out of range");
				}
			}

			Query query1 = em.createQuery(queryString);
			if (values != null) {
				if (values != null || values.size() > 0) {
					for (int i = 0; i < values.size(); i++)
						query1.setParameter(i + 1, values.get(i));
				}
			}
			if (cursor != null) {
				query1.setMaxResults(cursor.getMaxPageSize());
				query1.setFirstResult(cursor.getStartIndex());

			}

			results = query1.getResultList();

			ret = new SearchHandler();
			ret.setCriteria(searchHandler.getCriteria());
			if (cursor != null) {
				cursor.setPageSize(results.size());
				ret.setCursor(cursor);
			}
			ret.setResults(results);
		} catch (Exception e) {
			throw new EntityNotFoundException(
					"Unable to extract entities from database with queryString:"
							+ queryString, e);
		}

		return ret;
	}

	
	/**
	 * Returns true if entity is bound to the persistence context
	 * @param entity the Entity in question
	 * @return
	 */
	public boolean boundQ(EntityType entity) {
		return em.contains(entity);
	}
	
	/**
	 * Returns true if there is a row in the database with this entity's id
	 * @param entity the Entity in question
	 * @return
	 */
	public boolean inDbQ(Object entity) {
		try {
			return new EjbUtil().inDb(em, entity);
		} catch(Exception e) {
			log.error( e);
			return false;
		}
	}

	/**
	 * Use only for debugging
	 * @return the injected EntityManager 
	 */
	public EntityManager getEm() {
		return em;
	}

    @Override
    public void deleteMany(Object... entities) throws EntityNotFoundException {
        try {
            for(Object entity : entities) {
                Dbg.debug("entity " + entity);
                if(inDbQ(  entity)) {
                    Field f = FieldUtil.idField(entity);
                    Dbg.debug("f " + f);
                    Object id =  f.get(entity);
                    Dbg.debug("removing " + entity + " with id " + id);
                    Object e = em.find(entity.getClass(), id);
                    em.remove(e);
                } else {
                    Dbg.debug(entity + " not in db");
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
	@Override
	public void delete(Collection entities) throws EntityNotFoundException {
		Dbg.debug("here with " + Dbg.oS(entities));
		try {
			for(Object entity : entities) {
				Dbg.debug("entity " + entity);
				if(inDbQ(  entity)) {
					Field f = FieldUtil.idField(entity);
					Dbg.debug("f " + f);
					Object id =  f.get(entity);
					Dbg.debug("removing " + entity + " with id " + id);
					Object e = em.find(entity.getClass(), id);
					em.remove(e);
				} else {
					Dbg.debug(entity + " not in db");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}


	public long count() {
		return (Long) em.createQuery("select count(*) from " +entityClass.getSimpleName()).getSingleResult();
	}
	
	public boolean available() {
	    try {
	        count();
	        return true;
	    } catch(Exception e) {
	        //log.warn(entityClass.getSimpleName() + " is not available");
	        return false;
	    }
	}
	

	/**
	 * Given the Entity that this EAO manages this method will persist by
	 * merging with existing or creating a new Entity.
	 * 
	 */
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
