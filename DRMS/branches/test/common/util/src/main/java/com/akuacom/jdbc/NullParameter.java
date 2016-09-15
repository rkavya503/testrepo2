package com.akuacom.jdbc;

import java.io.Serializable;

public class NullParameter implements Serializable {
	
	private static final long serialVersionUID = -1595006787812846295L;
	
	private  int sqlType;
	
	public NullParameter(int sqlType){
		this.sqlType = sqlType;
	}

	public int getSqlType() {
		return sqlType;
	}
}
