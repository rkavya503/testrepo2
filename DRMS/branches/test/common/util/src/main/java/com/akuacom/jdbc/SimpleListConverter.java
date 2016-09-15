package com.akuacom.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <tt>SimpleListConverter</tt> can convert a specified column in all rows to a target List  
 * and by default the column is 1,e.g. first column. 
 */

public class SimpleListConverter<E> implements Converter<List<E>> {

	private static final long serialVersionUID = 3609971988081541355L;
	
    private int column = 1;

	protected Class<E> expectType; 
	
	public static <T> SimpleListConverter<T> make(Class<T> type){
		return new SimpleListConverter<T>(type);
	}
	
	public SimpleListConverter(Class<E> expectType){
		this.expectType = expectType;
	}

	
	public SimpleListConverter(Class<E> expectType, int column){
		this.expectType = expectType;
		this.column = column;
	}
	
	@Override
	public List<E> convert(ResultSet rs) throws SQLException {
		List<E> result = new ArrayList<E>();
		while(rs.next()){
			Object obj=rs.getObject(column);
			if(obj==null){
				result.add(null);
			}
			else{
				Class<?> actualType = obj.getClass();
				if(!JavaTypes.isAutoBoxing(actualType, expectType) && !expectType.isInstance(obj)){
					obj = JavaTypes.forceCast(obj, expectType);
				}
				result.add((E)obj);
			}
		}
		return result;
	}
}
