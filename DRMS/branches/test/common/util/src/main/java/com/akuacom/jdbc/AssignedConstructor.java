package com.akuacom.jdbc;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * <tt>AssignedConstructor</tt> can create a pojo using column values in {@java.sql.ResultSet} directly.
 * It will firstly identify a constructor with given parameter types and the use it to create the 
 * target pojo
 *
 */

public class AssignedConstructor<T> implements BeanFactory<T> {

	private static final long serialVersionUID = 1L;

	private Constructor<T> constructor;
	
	private Class<?> initArgumentTypes[];
	
	private Class<T> type;
	
	private String[] keyColumns;
	
	public AssignedConstructor(Class<T> type, Class<?>...initArgumentTypes){
		this.initArgumentTypes = initArgumentTypes;
		this.type = type;
	}
	
	public AssignedConstructor(Class<T> type, String[] keyColumns, Class<?>...initArgumentTypes){
		this(type, initArgumentTypes);
		this.keyColumns = keyColumns;
	}
		
	@Override
	public T createInstance(ResultSet rs) {
		ResultSetMetaData metadata = null;
		try {
			metadata = rs.getMetaData();
			
			if(constructor==null)
				constructor= type.getDeclaredConstructor(initArgumentTypes);
			
			Object args[] = new Object[initArgumentTypes.length];
			
			for(int i =1;i<=Math.min(metadata.getColumnCount(),args.length);i++){
				args[i] = rs.getObject(i);
			}
			
			T instance = constructor.newInstance(args);
			return instance;
			
		} catch (Exception e) {
			throw new BeanCreationException(e);
		}
		
	}

	public String[] getKeyColumns() {
		if(this.keyColumns!=null)
			return keyColumns;
		else
			return new String[] {};
	}
	
}
