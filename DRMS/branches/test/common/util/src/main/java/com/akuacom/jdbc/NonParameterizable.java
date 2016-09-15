package com.akuacom.jdbc;

public interface NonParameterizable<T> {
	
	T getValue();
	
	String getParameterizedReplacement();
	
	String getParamKey(String paramName);
	
	int getParamCount(String paramName);
}
