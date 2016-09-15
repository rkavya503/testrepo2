package com.akuacom.pss2.drw;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import com.akuacom.jdbc.Converter;
import com.akuacom.jdbc.SQLExecutorBean;

@Stateless
public class Pss2SQLExecutorBean extends SQLExecutorBean 
				implements Pss2SQLExecutor.L, Pss2SQLExecutor.R {
	
	@Resource(mappedName="java:mysql-pss2-ds") 
	private DataSource dataSource;
	
    
    @Override
	public <T> T doNativeQuery(String sql, Object[] params,
			Converter<T> factory) throws SQLException{
		Connection connection=null;
		try{
			connection = getConnection();
			return super.doNativeQuery(connection,sql, params, factory);
		}finally{
			if(connection!=null) 
				try{ connection.close();} catch(Exception e){};
		}
	}
	
	protected Connection getConnection() throws SQLException{
		return dataSource.getConnection();
	}
	
	@Override
	public <T> T doNativeQuery(String sql, Converter<T> factory)
								throws SQLException {
		Connection connection=getConnection();
		try{
			return super.doNativeQuery(connection,sql, factory);
		}finally{
			if(connection!=null) 
				try{ connection.close();} catch(Exception e){};
		}
	}
	
	@Override
	public int execute(String sql) throws SQLException {
		Connection connection=getConnection();
		try{
			 return execute(connection,sql);
		}finally{
			if(connection!=null) 
				try{ connection.close();} catch(Exception e){};
		}
	}
	
	@Override
	public <T> T doNativeQuery(String sql, Map<String, Object> namedParameters,
			Converter<T> converter) throws SQLException {
		Connection connection=getConnection();
		try{
			return super.doNativeQuery(connection,sql,namedParameters, converter);
		}finally{
			if(connection!=null) 
				try{ connection.close();} catch(Exception e){};
		}
	}

	@Override
	public int execute(String sql, Map<String, Object> namedParameters)
			throws SQLException {
		Connection connection=getConnection();
		try{
			return super.execute(connection,sql,namedParameters);
		}finally{
			if(connection!=null) 
				try{ connection.close();} catch(Exception e){};
		}
	}

	@Override
	public int execute(String sql, Object[] parameters) throws SQLException {
		Connection connection=getConnection();
		try{
			return super.execute(connection,sql,parameters);
		}finally{
			if(connection!=null) 
				try{ connection.close();} catch(Exception e){};
		}
	}
	
}
