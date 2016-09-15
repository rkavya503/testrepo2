package com.akuacom.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <tt>ListConverter</tt> is a converter which can 
 * <li> Convert a {@link java.sql.ResultSet} to a a {@link java.util.List} </li>
 * <li> Convert each row {@link java.sql.ResultSet} to a Pojo in list using a {@link com.akuacom.jdbc.BeanFactory} </li>
 */
public  class ListConverter<E> extends AbstractConverter<List<E>> {

	private static final long serialVersionUID = -7634700242212898853L;
	
	private BeanFactory<E> factory;

	
	public static <C> ListConverter<C> make(BeanFactory<C> factory){
		return new ListConverter<C>(factory);
	}
	
	
	public ListConverter(BeanFactory<E> factory){
		this.factory = factory;
	}
	
	@Override
	public void processRow(ResultSet resultSet, List<E> expectResult) throws SQLException {
		E instance= factory.createInstance(resultSet);
		if(instance!=null){
			expectResult.add(instance);
		}
	}
	
	@Override
	public List<E> initialize() {
		return new ArrayList<E>();
	}
}
