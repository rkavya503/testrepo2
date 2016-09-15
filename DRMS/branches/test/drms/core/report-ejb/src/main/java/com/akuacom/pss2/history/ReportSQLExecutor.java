package com.akuacom.pss2.history;

import java.sql.SQLException;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.jdbc.Converter;
import com.akuacom.jdbc.SQLExecutor;

/**
 * <tt>ReportSQLExecutor</tt> is similar to <tt>SQLExecutor</tt>, but Pss2SQLExecutor provides
 * a default JDBC connection for SQL operation.  
 */
public interface ReportSQLExecutor extends SQLExecutor {
	
	@Remote
    public interface R extends ReportSQLExecutor {}
	
    @Local
    public interface L extends ReportSQLExecutor {}
    
	public abstract <T> T  doNativeQuery(String sql,Object[]params, Converter<T> factory)
								throws SQLException;
	
	public abstract <T> T doNativeQuery(String sql, Converter<T> factory)
								throws SQLException;
	
	public abstract <T> T doNativeQuery(String sql, Map<String,Object> namedParameters,Converter<T> converter)
								throws SQLException;
}
