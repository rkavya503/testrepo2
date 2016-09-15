package com.akuacom.jdbc;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <tt>BeanFactory </tt> can create a Pojo for current row in {@link java.sql.ResultSet}
 */
public interface BeanFactory<T> extends Serializable{
	
	/** create a instance of target object based on current row in ResuletSet **/
	T createInstance(ResultSet rs) throws SQLException;
	
	/**
	 * column name(s) to identify a row.
	 */
	String[] getKeyColumns();
}
