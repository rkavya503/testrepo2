package com.akuacom.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Converter the very first row in ResultSet to a list, each colum is an element in the list
 */
public class ColumnsAsListConverter<E> implements Converter<List<E>> {

	private static final long serialVersionUID = 1L;

	private Class<E> expectType;
	
	public static <C> ColumnsAsListConverter<C> make(Class<C> expectType){
		return new ColumnsAsListConverter<C>(expectType);
	}
	
	public ColumnsAsListConverter(Class<E> expectType){
		this.expectType = expectType;
	}
	
 	@Override
	public List<E> convert(ResultSet resultSet) throws SQLException {
 		if(resultSet.next()){
			List<E> result = new ArrayList<E>();
			ResultSetMetaData metadata =resultSet.getMetaData();
			for(int i =1  ; i<= metadata.getColumnCount();i++){
				Object obj = resultSet.getObject(i);
				Class<?> actualType = obj.getClass();
				if(!JavaTypes.isAutoBoxing(actualType, expectType) && !expectType.isInstance(obj)){
					obj = JavaTypes.forceCast(obj, expectType);
				}
				result.add((E)obj);
			}
			return result;
 		}else
 			return Collections.emptyList();
	}
}
