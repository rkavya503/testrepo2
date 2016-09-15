/**
 * 
 */
package com.akuacom.ejb;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Helpful methods when working with {@link BaseEntity}s.
 * 
 * @author roller
 * 
 */
public class BaseEntityUtil {

	/**
	 * Useful utility that will receive the collection of {@link BaseEntity}s
	 * and will return the Map of all entities keyed by the UUID of the entity.
	 * 
	 * @param collection
	 * @return the map with BaseEntity entries keyed by the entity's UUID.
	 */
	public static <BaseEntityT extends BaseEntity> Map<String, BaseEntityT> mapFromSet(
			Collection<BaseEntityT> collection) {
		HashMap<String, BaseEntityT> map = new HashMap<String, BaseEntityT>();
		for (BaseEntityT baseEntity : collection) {
			map.put(baseEntity.getUUID(), baseEntity);
		}
		return map;
	}
}
