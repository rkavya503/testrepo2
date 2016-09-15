package com.akuacom.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollapseAsMapConverter<K,V> implements Converter<Map<K,List<V>>> {

	private static final long serialVersionUID = -6271486613804464642L;
	
	private BeanFactory<K> keyFactory;
	private BeanFactory<V> valueFactory;
	private boolean keyInOrder = false;
	
	
	public static <K,V> CollapseAsMapConverter<K,V>  
		make(BeanFactory<K> keyFactory,BeanFactory<V> valueFactory){
		return new CollapseAsMapConverter<K,V>(keyFactory,valueFactory);
	}
	
	public static <K,V> CollapseAsMapConverter<K,V>  
				make(BeanFactory<K> keyFactory,BeanFactory<V> valueFactory,boolean keyInorder){
		return new CollapseAsMapConverter<K,V>(keyFactory,valueFactory,keyInorder);
	}
	
	
	public CollapseAsMapConverter(BeanFactory<K> keyFactory,
			BeanFactory<V> valueFactory) {
		this.keyFactory=keyFactory;
		this.valueFactory= valueFactory;
	}

	public CollapseAsMapConverter(BeanFactory<K> keyFactory,
			BeanFactory<V> valueFactory, final boolean keyInorder){
		this(keyFactory,valueFactory);
		this.keyInOrder = keyInorder;
	}
	
	@Override
	public Map<K,List<V>> convert(ResultSet rs) throws SQLException {
		final Map<K,List<V>> results = new HashMap<K,List<V>>();
		MasterDetailFactory<K,V> masterDetailFactory= 
					new MasterDetailFactory<K,V>(keyFactory, valueFactory,keyInOrder){
			private static final long serialVersionUID = 6435647661150835554L;
			@Override
			protected void buildUp(K master, V detail) {
				List<V> values = results.get(master);
				if(values==null){
					 values = new ArrayList<V>();
					 results.put(master, values);
				}
				values.add(detail);
			}
		};
		while(rs.next()){
			masterDetailFactory.createInstance(rs);
		}
		return results;
	}
}
