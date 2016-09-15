/**
 * 
 */
package com.akuacom.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Convert cell(1,1) - first column in first row from Result
 * <PRE>
 * 	String sql =" select count(*) from ... ";
 *  SQLExcecutor sqlcommander = lookup(...);
 *  long size=sqlcommander.doNativeQuery(connection,sql,new CellConverter<Long>(Long.class));
 *  </PRE>
 */
public class CellConverter<T> implements  Converter<T> {
	
	private static final long serialVersionUID = 3609971988081541355L;
	
	public  static <F> CellConverter<F> make(Class<F> expectType){
		return new CellConverter<F>(expectType);
	}
	
	protected Class<T> expectType; 
	
	public CellConverter(Class<T> expectType){
		this.expectType = expectType;
	}
	
	@Override
	public T convert(ResultSet rs) throws SQLException {
		if(rs.next()){
			Object obj=rs.getObject(1);
			if(obj==null) return null;
		
			Class<?> actualType = obj.getClass();
			if(!JavaTypes.isAutoBoxing(actualType, expectType) && !expectType.isInstance(obj)){
			obj = JavaTypes.forceCast(obj, expectType);
			}
			return (T) obj;
		}
		return null;
	}
	
}
