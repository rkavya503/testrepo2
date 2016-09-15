/**
 * 
 */
package com.akuacom.pss2.drw.eao;

import java.util.List;

import com.akuacom.pss2.drw.core.AbstractBaseEntity;
import com.akuacom.pss2.drw.exception.DuplicatedKeyException;
import com.akuacom.pss2.drw.exception.EntityNotFoundException;

/**
 * the interface BaseEAO
 */
public interface BaseEAO<EntityType extends AbstractBaseEntity> {

	EntityType create(EntityType entity) throws DuplicatedKeyException;

	void delete(String uuid) throws EntityNotFoundException;

	EntityType find(String uuid);

	void delete(EntityType entity);

	List<EntityType> create(List<EntityType> entities) throws DuplicatedKeyException;

	EntityType update(EntityType entity);

	List<EntityType> getAll(String entityClassName);

}
