package com.akuacom.jdbc;

public enum SQLKeyWord implements SQLSegment {

	DESC("desc"),
	ASC("asc"),
	ORDER_BY("order by"),
	WHERE("where");
	
	private String keyword;
	
	SQLKeyWord(String keyword){
		this.keyword =keyword;
	}
	
	@Override
	public String format() {
		return keyword;
	}
	
	@Override
	public String toString() {
		return keyword;
	}
}
