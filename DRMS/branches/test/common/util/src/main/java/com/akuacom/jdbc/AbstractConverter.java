package com.akuacom.jdbc;
import java.sql.ResultSet;
import java.sql.SQLException;



public abstract class AbstractConverter<E> implements Converter<E> {

	private static final long serialVersionUID = -7704181998374046274L;

	/**
	 * initialize the expected Object 
	 */
	protected abstract E initialize();
	
	/**
	 *  Process each row in ResultSet
	 * @param resultSet 
	 * @param expectResult
	 * @throws SQLException
	 */
	protected abstract void processRow(ResultSet resultSet, E expectResult) throws SQLException;
	
	@Override
	public E convert(ResultSet rs) throws SQLException {
		E initialValue = this.initialize();
		while(rs.next()){
			processRow(rs, initialValue);
		}
		return initialValue;
	}
	
}
