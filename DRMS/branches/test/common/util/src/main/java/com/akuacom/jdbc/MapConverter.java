package com.akuacom.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MapConverter<K,V> implements Converter<Map<K,V>> {

	private static final long serialVersionUID = 1L;
	
	protected BeanFactory<K> keyFactory;
	protected BeanFactory<V> valueFactory;
	
	
	public static <k, v> MapConverter<k,v> make(BeanFactory<k> keyFactory,BeanFactory<v> valueFactory) {
		return new MapConverter<k,v>(keyFactory,valueFactory);
	}
	
	
	public MapConverter(BeanFactory<K> keyFactory,BeanFactory<V> valueFactory){
		this.keyFactory   = keyFactory;
		this.valueFactory = valueFactory;
	}
	
	@Override
	public Map<K,V> convert(ResultSet rs) throws SQLException {
		Map<K,V> results = new HashMap<K,V>();
		while(rs.next()){
			K key = keyFactory.createInstance(rs);
			V value = valueFactory.createInstance(rs);
			results.put(key, value);
		}
		return results;
	}

}
