package com.akuacom.pss2.data.usage;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class DataCache {
	public final static int CACHE_COUNT_LIMITED = 1;

	public final static int DEFAULT_TYPE = CACHE_COUNT_LIMITED;
	
	public final static int DEFAULT_PARAM = 15;

	public abstract Object put(Object key, Object value);

	public abstract Object get(Object key);

	public abstract Object remove(Object key);

	public abstract Enumeration<?> elements();

	public abstract void expire(Object key);

	public abstract void clear();

	public abstract int size();

	public static DataCache create(int type, int param) {
		DataCache cache;

		if (type == 0) {
			type = DEFAULT_TYPE;
			param = DEFAULT_PARAM;
		}

		switch (type) {
		case CACHE_COUNT_LIMITED:
			if (param > 0)
				cache = new DataCache.CountLimited(param);
			else
				cache = new DataCache.CountLimited(DEFAULT_PARAM);
			break;
		default:
			cache = new DataCache.CountLimited(DEFAULT_PARAM);
		}
		return cache;
	}

	public static int mapType(String type) {
		if (type == null || type.equals(""))
			return 0;

		if (type.toLowerCase().equals("count-limited"))
			return CACHE_COUNT_LIMITED;

		return 0;
	}

	
	/**
	 * cache implementation, holds a certain count of elements
	 * once exceeded, oldest element is replaced
	 * elements are 'freshened' when accessed
	 *
	 */
	public static class CountLimited extends DataCache {
		LinkedHashMap<Object, Object> map ; 
		/**
		 *  size of store
		 */
		
		public CountLimited(final int size) {
		  map = new LinkedHashMap(size, .75F, true) {  
		             int maxSize = size;
		             protected boolean removeEldestEntry(Map.Entry eldest) {  
		               return size() > maxSize;  
		             }  
		           };  
		}
		
		public Object put(Object key, Object value) {
		    return map.put(key, value);
		}

		public Object get(Object key) {
			return map.get(key);
		}

		public Object remove(Object key) {
			return map.remove(key);
		}

		public Enumeration<?> elements() {
			return Collections.enumeration(map.values());
		}

		/* (non-Javadoc)
		 * @see com.akuacom.pss2.data.usage.DataCache#expire(java.lang.Object)
		 * Not sure if this should remove the element, or just move key to 
		 * oldest
		 */
		public void expire(Object key) {
	           if ( remove(key) == null ) {
	            }
	            else {
	            }
	            dispose(key);

		}
        protected void dispose( Object o ) {
        }

		public void clear() {
			map.clear();
		}

		public int size() {
			return map.size();
		}
	}
}