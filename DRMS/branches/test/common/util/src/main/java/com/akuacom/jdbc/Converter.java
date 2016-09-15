package com.akuacom.jdbc;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *	<tt>Converter</tt> is used to convert a {@link java.sql.ResultSet} to a pojo or a collection.
 * @param <T>
 */
public interface Converter<E> extends Serializable {
	
	E convert(ResultSet resultSet) throws SQLException;
	
}
