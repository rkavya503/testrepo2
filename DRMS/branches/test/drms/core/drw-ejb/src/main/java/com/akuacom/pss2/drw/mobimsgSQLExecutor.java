package com.akuacom.pss2.drw;

import java.sql.SQLException;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.jdbc.Converter;
import com.akuacom.jdbc.SQLExecutor;

public interface mobimsgSQLExecutor extends SQLExecutor {
	
	@Remote
    public interface R extends mobimsgSQLExecutor {}
	
    @Local
    public interface L extends mobimsgSQLExecutor {}
    
	public abstract <T> T doNativeQuery(String sql, Map<String,Object> namedParameters,Converter<T> converter)
	throws SQLException;

}