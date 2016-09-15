package com.akuacom.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* A <code>ColumnAsObjectFactory</code> will return the specify column in ResultSet as an object, and do 
* object conversion automatically if required 
*/

public class ColumnAsObjectFactory<T> implements BeanFactory<T>{

	private static final long serialVersionUID = 1L;

	private String columnName;
	private String[] keyColumn;
	private Class<T> expectType;
	
	public  static <F>  ColumnAsObjectFactory<F> make(Class<F> expectType,String columnName){
		return new ColumnAsObjectFactory<F>(expectType,columnName);
	}
	
	public  static <F>  ColumnAsObjectFactory<F> make(Class<F> expectType,String columnName,String... keyColumn){
		return new ColumnAsObjectFactory<F>(expectType,columnName,keyColumn);
	}
	
	public ColumnAsObjectFactory(Class<T> expectType,String columnName){
		this.columnName = columnName;
		this.expectType = expectType;
		this.keyColumn =  new String[]{columnName};
	}
	
	public ColumnAsObjectFactory(Class<T> expectType,String columnName,String... keyColumn){
		this.columnName = columnName;
		this.keyColumn = keyColumn;
		this.expectType = expectType;
	}
	
	@Override
	public T createInstance(ResultSet rs) throws SQLException {
		Object obj = rs.getObject(columnName);
		if(obj==null) return null;
		Class<?> actualType = obj.getClass();
		if(!JavaTypes.isAutoBoxing(actualType, expectType) && !expectType.isInstance(obj)){
			obj = JavaTypes.forceCast(obj, expectType);
		}
		return (T) obj;
	}
	
	@Override
	public String[] getKeyColumns() {
		if(keyColumn==null)
			return new String[]{};
		else
			return keyColumn;
	}
}
