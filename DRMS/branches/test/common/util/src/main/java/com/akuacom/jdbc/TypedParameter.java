package com.akuacom.jdbc;


public class TypedParameter implements SQLSegment {
	
	private static final long serialVersionUID = -714474216078731578L;

	private  int sqlType;
	
	private Object value;
	
	public TypedParameter(int sqlType,Object value){
		this.sqlType = sqlType;
		this.value = value;
	}

	public int getSqlType() {
		return sqlType;
	}

	public Object getValue() {
		return value;
	}
	
	/**
	 * @return string which can be placed in sql statement, for example
	 * for a java.lang.String, single quotation marks should be placed before and after that String
	 */
	public String format(){
		return value.toString();
	}
	
}
