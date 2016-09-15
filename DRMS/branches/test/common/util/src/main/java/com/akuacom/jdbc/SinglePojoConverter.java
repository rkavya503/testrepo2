package com.akuacom.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <tt>SinglePOjoConverter</tt> can converter a {@link java.sql.ResultSet} to a single Pojo 
 * object 
 * 
 * <li> if multiple objects can be created, throw a  {@link BeanCreationException}  </li>
 * <li> if single object can be created, just return it. </li>
 * <li> if no object can be created and null object is not allowed, throw a a {@link BeanCreationException}  </li>
 * <li> if no object can be created and null object is allowed, return null </li>
 * 
 * <p> By default, null object is not allowed.
 */
public class SinglePojoConverter<E> extends AbstractConverter<E> {

	private static final long serialVersionUID = 1L;

	private boolean canBeNull;
	
	private E singleInstance;
	
	private BeanFactory<E> factory;
	
	/**
	 * @param factory to create the Pojo
	 * @param canBeNull whether null object is allowed.
	 *  if non single row in  if null object is allowed, 
	 */
	public SinglePojoConverter(BeanFactory<E> factory,boolean canBeNull){
		this.canBeNull = canBeNull;
		this.factory= factory;
	}	
	
	public SinglePojoConverter(BeanFactory<E> factory){
		this(factory, false);
	}
	
	@Override
	public E initialize() {
		return null;
	}

	@Override
	public void processRow(ResultSet resultSet, E expectResult)
			throws SQLException {
		if(singleInstance==null){
			singleInstance =  newInstance(resultSet);
		}else{
			throw new BeanCreationException("should be only one instance");
		}
	}
	
	protected E newInstance(ResultSet rs) throws SQLException{
		if(!canBeNull && singleInstance==null)
			throw new BeanCreationException("Null instance not allowed");
		return factory.createInstance(rs);
	}
	
    public boolean isCanBeNull(){
    	return canBeNull;
    }
}
