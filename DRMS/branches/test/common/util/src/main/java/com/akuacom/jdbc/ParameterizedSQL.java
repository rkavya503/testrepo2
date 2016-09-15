package com.akuacom.jdbc;

import java.io.Serializable;
import java.util.Map;

public class ParameterizedSQL implements Serializable {

	private static final long serialVersionUID = -5621972818943416689L;
	
	private String sqlStatement;
	
	private Map<String,int[]> parameterIndex;
	
	public ParameterizedSQL(String sqlStatement,
			Map<String, int[]> parameterIndex) {
		this.sqlStatement = sqlStatement;
		this.parameterIndex = parameterIndex;
	}

	public Map<String, int[]> getParameterIndex() {
		return parameterIndex;
	}

	public String getSqlStatement() {
		return sqlStatement;
	}
	
}
