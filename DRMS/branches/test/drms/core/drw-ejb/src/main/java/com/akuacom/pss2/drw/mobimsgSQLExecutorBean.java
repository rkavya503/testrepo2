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
public class mobimsgSQLExecutorBean extends SQLExecutorBean implements
		mobimsgSQLExecutor.L, mobimsgSQLExecutor.R {

	@Resource(mappedName = "java:mysql-mobimsg-ds")
	private DataSource dataSource;

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

	protected Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

}
