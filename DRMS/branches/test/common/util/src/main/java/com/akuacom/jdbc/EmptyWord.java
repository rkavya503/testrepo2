package com.akuacom.jdbc;

public class EmptyWord extends SQLWord {
	private static final long serialVersionUID = 8350576857093543087L;
	
	private static final String  EMPTY_STR="";
	
	private static EmptyWord instance;
	
	private EmptyWord(String word) {
		super(word);
	}
	
	public static EmptyWord getInstance(){
		if(instance==null){
			instance = new EmptyWord(EMPTY_STR);
		}
		return instance;
	}
	
}
