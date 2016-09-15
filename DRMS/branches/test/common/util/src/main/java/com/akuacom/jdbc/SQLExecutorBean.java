package com.akuacom.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;


public class SQLExecutorBean implements SQLExecutor {
	@Override
	public <T> T doNativeQuery(Connection jdbcConnection, String sql,
									Object[] params,Converter<T> converter) 
									throws SQLException{
		 PreparedStatement ps =null;
		 ResultSet rs = null;
		 try{
			 ps= jdbcConnection.prepareStatement(sql);
			 
			 for(int i = 0; i<params.length; i++){
				 bindPositionParameter(ps,params[i],i+1);
			 }
			 rs = ps.executeQuery(sql);
			 
			 T expectResult = converter.convert(rs); 
			 return expectResult;
		 }catch(Exception e){
			 throw new SQLException(e);
		 }finally{
			 if(ps!=null)  try {ps.close(); }catch(Exception e){}; 
			 if(rs!=null)  try {rs.close(); }catch(Exception e){};
		 }
	}
	
	@Override
	public <T> T doNativeQuery(Connection jdbcConnection, String sql,
									Converter<T> converter) 
									throws SQLException {
		 Statement st =null;
		 ResultSet rs = null;
		 try{
			 st= jdbcConnection.createStatement();
			 rs = st.executeQuery(sql);
			 T expectResult = converter.convert(rs);
			 return expectResult;
		 }catch(Exception e){
			 throw new SQLException(e);
		 }finally{
			 if(st!=null)  try {st.close(); }catch(Exception e){}; 
			 if(rs!=null)  try {rs.close(); }catch(Exception e){};
		 }
	}
	
	
	@Override
	public <T> T doNativeQuery(Connection jdbcConnection, String sql,
			Map<String, Object> namedParameters, Converter<T> converter)
			throws SQLException {
		
		NamedParameterStatement ps = null;
		ResultSet rs = null;
		try{
			ps = new NamedParameterStatement(sql,namedParameters,jdbcConnection);
			//use ps.getDebugSQL() for sql debug
			ps.bindParameters();
			rs = ps.executeQuery();
			T expectResult = converter.convert(rs);
			return expectResult;
		} catch (Exception e) {
			throw new SQLException(e);
		}finally{
			 if(ps!=null)  try {ps.close(); }catch(Exception e){}; 
			 if(rs!=null)  try {rs.close(); }catch(Exception e){};
		}
	}
	
	@Override
	public int execute(Connection jdbcConnection, String sql) throws SQLException {
		 Statement st =null;
		 try{
			 st= jdbcConnection.createStatement();
			 st.execute(sql);
			 return st.getUpdateCount();
		 }catch(Exception e){
			 throw new SQLException(e);
		 }finally{
			 if(st!=null)  try {st.close(); }catch(Exception e){}; 
		 }
	}
	
	@Override
	public int execute(Connection jdbcConnection, String sql,
			Map<String, Object> parameters) throws SQLException {
		NamedParameterStatement ns = null;
		try{
			ns = new NamedParameterStatement(sql,parameters,jdbcConnection);
			ns.bindParameters();
			return ns.executeUpdate();
		}catch(Exception e){
			throw new SQLException(e);
		}finally{
			if(ns!=null) try {ns.close();}catch(Exception e){}
		}
	}

	@Override
	public int execute(Connection jdbcConnection, String sql,
			Object[] parameters) throws SQLException {
		PreparedStatement ps = null;
		try{
			ps =  jdbcConnection.prepareStatement(sql);
			return ps.executeUpdate();
		}catch(Exception e){
			throw new SQLException(e);
		}finally{
			if(ps!=null) try {ps.close();}catch(Exception e){}
		}
	}
	
	protected void bindPositionParameter(PreparedStatement ps,Object paramValue,int pos) 
			throws SQLException{
		JavaTypes.bindParameter(pos, paramValue, ps);
	}
	
}
