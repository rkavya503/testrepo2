/**
 * 
 */
package com.akuacom.ejb;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.junit.Test;

/**
 * @author roller
 *
 */
public class BaseEntityUtilTest {

	/**
	 * Test method for {@link com.akuacom.ejb.BaseEntityUtil#mapFromSet(java.util.Collection)}.
	 */
	@Test
	public void testMapFromSet() {
		Set<MockEntity> set = getMockEntities();
		Map<String,MockEntity> map = BaseEntityUtil.mapFromSet(set);
		Collection<MockEntity> values = map.values();
		Collection<MockEntity> leftovers = CollectionUtils.subtract(set, values);
		assertEquals(0,leftovers.size());
		
		for (MockEntity mockEntity : set) {
			MockEntity found = map.get(mockEntity.getUUID());
			assertEquals(mockEntity,found);
		}
	}
	
	public static Set<MockEntity> getMockEntities(){
		HashSet<MockEntity> set = new HashSet<MockEntity>();
		for(int which = 0; which < 10; which++){
			set.add(generateMockEntity());
		}
		return set;
	}

	/**
	 * @return
	 */
	private static MockEntity generateMockEntity() {
		MockEntity entity = new MockEntity();
		BaseEntityFixture.populateRandomProperties(entity);
		return entity;
	}

}
