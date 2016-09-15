package com.akuacom.jdbc.jpa;
import org.hibernate.jdbc.Work;

public abstract class NativeHibernateWork<T>  implements Work{

	protected T expectResult;
	
	public T getExpectResult() {
		return expectResult;
	}
	
}
