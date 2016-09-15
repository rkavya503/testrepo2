package com.akuacom.pss2.drw;

import java.sql.SQLException;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.jdbc.Converter;

/**
 * <tt>Pss2SQLExecutor</tt> is similar to <tt>SQLExecutor</tt>, but Pss2SQLExecutor provides
 * a default JDBC connection for SQL operation.  
 */
public interface DrwSQLExecutor {
	
	@Remote
    public interface R extends DrwSQLExecutor {}
	
    @Local
    public interface L extends DrwSQLExecutor {}
    
	public abstract <T> T  doNativeQuery(String sql,Object[]params, Converter<T> factory)
								throws SQLException;
	
	public abstract <T> T doNativeQuery(String sql, Converter<T> factory)
								throws SQLException;
	
	
	public abstract <T> T doNativeQuery(String sql, Map<String,Object> namedParameters,Converter<T> converter)
			throws SQLException;
	
	public abstract int execute(String sql) throws SQLException;
			
	public abstract int execute(String sql, Map<String,Object> namedParameters)
		    throws SQLException;
	
	
	public abstract int execute(String sql, Object[] parameters)
    		throws SQLException;
}
