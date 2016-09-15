package com.akuacom.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MockResultSet implements InvocationHandler {
	private int cursor = 0;
	private ResultSetMetaData metaData;
	private int rowCount;
	private Object[][] data;
	private String columns[];
	
	public MockResultSet(ResultSetMetaData metaData, Object[][] data,String columns[]){
		this.metaData = metaData;
		this.rowCount = data.length;
		this.data = data;
		this.columns =columns;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {	
		if(method.getName().equals("getMetaData"))
			return getMetaData();
		else if(method.getName().equals("getObject")){
			if(args[0] instanceof String){
				return getObject((String) args[0]);
			}else{
				return getObject((Integer) args[0]);
			}
		}else if(method.getName().equals("next")){
			return this.next();
		}
		return null;
	}
	
	public ResultSetMetaData getMetaData(){
		return metaData;
	}
	
	private int indexOfColumn(String column){
		for(int i =0;i<columns.length;i++){
			String col = columns[i];
			if(col.equals(column)){
				return i+1;
			}
		}
		return -1;
	}
	
	public Object getObject(String column) throws SQLException {
		return data[cursor-1][indexOfColumn(column)-1];
	}

	public boolean next() throws SQLException {
		cursor++;
		if (cursor >= rowCount + 1)
			return false;
		return true;
	}

	public Object getObject(int column) {
		return data[cursor-1][column-1];
	}
}