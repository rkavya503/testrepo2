package com.akuacom.pss2.asynch;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.EJBFactory;

public class EJBAsynchRunable implements AsynchRunable {

	private static final Logger log = Logger.getLogger(EJBAsynchRunable.class);
	 
	private static final long serialVersionUID = 5650330077663894027L;
	
	transient private Runnable wrapper;
	
	private Class<?> ejbInterface;
	
	private String methodName;
	
	private Object[] arguments;
	
	private Class<?>[] parameterTypes;
	
	public EJBAsynchRunable(Class<?> ejbInterface, String method,
			 Class<?>[] argumentTypes,Object[] arguments) {
		this.ejbInterface = ejbInterface;
		this.methodName = method;
		this.arguments = arguments;
		this.parameterTypes = argumentTypes;
	}
	
	@Override
	public void run() {
		Object ejb  = EJBFactory.getBean(ejbInterface);
		try {
			Method method =ejb.getClass().getMethod(methodName, parameterTypes);
			method.invoke(ejb, arguments);
		} catch (Exception e) {
			log.error("Fail to execute "+ejbInterface.getName()+"."+methodName,e);
		}
	}

	public void setWrapper(Runnable runnable){
		this.wrapper = runnable;
	}
	
	@Override
	public Runnable getWrapper() {
		return wrapper;
	}	
}
