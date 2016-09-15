package com.akuacom.pss2.data.usage;

import static org.junit.Assert.*;

import org.junit.Test;

import com.akuacom.pss2.data.usage.DataCache.CountLimited;
import com.akuacom.utils.lang.Dbg;

public class DataCacheTest {

	@Test public void testDataCache() {
		DataCache cache = new CountLimited(3);
		cache.put("a", 25);
		cache.put("b", 2);
		cache.put("c", 205);
		Dbg.info(Dbg.oS(cache.elements()));
		assertEquals(3, cache.size());
		cache.put("d", .8);
		Dbg.info(Dbg.oS(cache.elements()));
		assertEquals(3, cache.size());
		assertNull(cache.get("a"));
		cache.get("b"); // should freshen key and make "c" oldest entry
		cache.put("e", 1234);
		Dbg.info(Dbg.oS(cache.elements()));
		assertEquals(3, cache.size());
		assertNull(cache.get("c"));
		
	}
}
