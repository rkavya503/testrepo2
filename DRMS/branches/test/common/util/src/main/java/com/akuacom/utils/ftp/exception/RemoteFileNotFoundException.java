package com.akuacom.utils.ftp.exception;

public class RemoteFileNotFoundException extends Exception {
	
	String filename;
	public RemoteFileNotFoundException(String filename){
		this.filename = filename;
	}
	
	@Override
	public String toString(){
		return  "Remote file " + this.filename + " not found.";
	}
}
