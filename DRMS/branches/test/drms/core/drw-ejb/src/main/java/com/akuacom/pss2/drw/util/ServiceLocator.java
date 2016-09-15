package com.akuacom.pss2.drw.util;

import javax.naming.Context;
import javax.naming.InitialContext;

public class ServiceLocator {

	public static <T> T findHandler(Class<T> ServiceType, String serviceName){
		try{
			 Context namingContext=new InitialContext();
			 @SuppressWarnings("unchecked")
			 T ret = (T)namingContext.lookup(serviceName);
             return ret;
		}catch(Throwable e){
			e.printStackTrace();
			return null;
		}
	}
}
