package com.akuacom.jdbc;

public class SQLWord implements SQLSegment,NonParameterizable<String> {
	
	private static final long serialVersionUID = -6616132804535276802L;
	private String word;
	
	public SQLWord(String word){
		if(word == null)
			throw new IllegalArgumentException("sql word can not be null");
		this.word =word;
	}
	
	@Override
	public String format() {
		return word;
	}

	@Override
	public String getParameterizedReplacement() {
		return word;
	}

	@Override
	public String getParamKey(String paramName) {
		//single whitespace
		return word.replaceAll("\\s{1,}", "_");
	}
	
	@Override
	public int getParamCount(String paramName) {
		return 0;
	}

	@Override
	public String getValue() {
		return word;
	}
	
}
