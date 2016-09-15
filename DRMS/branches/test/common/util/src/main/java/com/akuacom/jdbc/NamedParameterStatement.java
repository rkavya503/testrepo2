package com.akuacom.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Map;

public class NamedParameterStatement {
	
	//cache all parsed sql statement which can be used to construct PreparedStatement 
	private static Map<String, ParameterizedSQL> parameterizedSQLCache = new Hashtable<String, ParameterizedSQL>();
	
	private String originalSQL;
	private Map<String,Object> parameters;
	private ParameterizedSQL   parameterizedSQL;
	private PreparedStatement  preparedStatement; 
	private Connection         connection;
	//for debug
	private String             debugSQL; 
	private String             indexParameterSQL;
	
	public NamedParameterStatement(String sql,Map<String,Object> parameters,Connection connection)
					throws SQLBuilderException{
		this.originalSQL = sql;
		this.parameters = parameters;
		this.parameterizedSQL = parse(sql,parameters);
		this.connection = connection;
	}
	
	protected ParameterizedSQL parse(String sqltemplate, Map<String,Object> parameters) throws SQLBuilderException{
		//there 
		String key = getDerivedKey(parameters)+"_"+sqltemplate;
		ParameterizedSQL preparedSQL = parameterizedSQLCache.get(key);
		
		if(preparedSQL==null){
			synchronized(key){
				preparedSQL =SQLBuilder.parseNamedPamameterSQL(sqltemplate, parameters);
				parameterizedSQLCache.put(key, preparedSQL);
			}
		}
		debugSQL = SQLBuilder.buildSQL(sqltemplate, parameters);
		indexParameterSQL = preparedSQL.getSqlStatement();
		
		return preparedSQL;
	}
	
	public void bindParameters() throws SQLException{
		PreparedStatement ps = this.getStatement();
		
		Map<String, int[]> paramLocations = this.parameterizedSQL.getParameterIndex();
		for(String key:parameterizedSQL.getParameterIndex().keySet()){
			Object paramValue = parameters.get(key);
			int pos[] = paramLocations.get(key);
			
			if(JavaTypes.isCollection(paramValue) || JavaTypes.isCollection(paramValue)){
				Object[] values = JavaTypes.valuesOfCollectionOrArray(paramValue);
				
				//for each of the collection occurrence 
				for(int i=0; pos!=null && i<pos.length;i++){
					//position only mark the start bind position of the collection or array
					//for each of value in the collection
					for(int j =0;j <values.length;j++){
						JavaTypes.bindParameter(pos[i]+j, values[j], ps);
					}
				}
			}else{
				for(int i =0;pos!=null && i<pos.length;i++){
					JavaTypes.bindParameter(pos[i], paramValue, ps);
				}
			}
		}
	}
	
	public String getOriginalSQL() {
		return originalSQL;
	}
	
	
	protected PreparedStatement getStatement() throws SQLException{
		if(preparedStatement==null && this.connection!=null){
			preparedStatement =  connection.prepareStatement(parameterizedSQL.getSqlStatement());
		}
		return preparedStatement;
	}
	
	
	protected int[] getIndexes(String key){
		return parameterizedSQL.getParameterIndex().get(key);
	}
	
	/* for debug proposal */
	protected String getDebugSQL() throws SQLBuilderException{
		return debugSQL;
	}
	
	protected String getIndexParameterSQL() {
		return indexParameterSQL;
	}
	
	/**
	 * Executes the statement.
	 * @return true if the first result is a {@link ResultSet}
	 * @throws SQLException
	 *             if an error occurred
	 * @see PreparedStatement#execute()
	 */
	public boolean execute() throws SQLException {
		return getStatement().execute();
	}

	/**
	 * Executes the statement, which must be a query.
	 * 
	 * @return the query results
	 * @throws SQLException
	 *             if an error occurred
	 * @see PreparedStatement#executeQuery()
	 */
	public ResultSet executeQuery() throws SQLException {
		return getStatement().executeQuery();
	}

	/**
	 * Executes the statement, which must be an SQL INSERT, UPDATE or DELETE
	 * statement; or an SQL statement that returns nothing, such as a DDL
	 * statement.
	 * 
	 * @return number of rows affected
	 * @throws SQLException
	 *             if an error occurred
	 * @see PreparedStatement#executeUpdate()
	 */
	public int executeUpdate() throws SQLException {
		return getStatement().executeUpdate();
	}

	/**
	 * Closes the statement.
	 * 
	 * @throws SQLException
	 *             if an error occurred
	 * @see Statement#close()
	 */
	public void close() throws SQLException {
		getStatement().close();
	}

	/**
	 * Adds the current set of parameters as a batch entry.
	 * 
	 * @throws SQLException
	 *             if something went wrong
	 */
	public void addBatch() throws SQLException {
		getStatement().addBatch();
	}

	/**
	 * Executes all of the batched statements.
	 * 
	 * See {@link Statement#executeBatch()} for details.
	 * 
	 * @return update counts for each statement
	 * @throws SQLException
	 *             if something went wrong
	 */
	public int[] executeBatch() throws SQLException {
		return getStatement().executeBatch();
	}
	
	

	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @throws SQLException
	 *             if an error occurred
	 * @throws IllegalArgumentException
	 *             if the parameter does not exist
	 * @see PreparedStatement#setObject(int, java.lang.Object)
	 */
	public void setObject(String name, Object value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setObject(indexes[i], value);
		}
	}

	
	public void setObject(String name, Object value, int sqlType) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setObject(indexes[i], value,sqlType);
		}
	}
	
	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @throws SQLException
	 *             if an error occurred
	 * @throws IllegalArgumentException
	 *             if the parameter does not exist
	 * @see PreparedStatement#setString(int, java.lang.String)
	 */
	public void setString(String name, String value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setString(indexes[i], value);
		}
	}

	public void setBoolean(String name, Boolean value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setBoolean(indexes[i], value);
		}
	}
	
	public void setShort(String name, Short value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setShort(indexes[i], value);
		}
	}
	
	public void setByte(String name, Byte value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setByte(indexes[i], value);
		}
	}
	
	
	public void setNull(String name, int sqlType) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setNull(indexes[i], sqlType);
		}
	}
	
	
	public void setDate(String name, Date date) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setDate(indexes[i], date);
		}
	}
	

	public void setTime(String name, Time value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setTime(indexes[i], value);
		}
	}
	
	public void setTimeStamp(String name, Timestamp value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setTimestamp(indexes[i], value);
		}
	}
	
	
	public void setBigDecimal(String name, BigDecimal value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setBigDecimal(indexes[i], value);
		}
	}
	
	
	public void setDouble(String name, Double value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setDouble(indexes[i], value);
		}
	}
	
	
	public void setFloat(String name, Float value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setFloat(indexes[i], value);
		}
	}
	
	
	public void setArray(String name, java.sql.Array value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setArray(indexes[i], value);
		}
	}
	
	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @throws SQLException
	 *             if an error occurred
	 * @throws IllegalArgumentException
	 *             if the parameter does not exist
	 * @see PreparedStatement#setInt(int, int)
	 */
	public void setInt(String name, int value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setInt(indexes[i], value);
		}
	}

	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @throws SQLException
	 *             if an error occurred
	 * @throws IllegalArgumentException
	 *             if the parameter does not exist
	 * @see PreparedStatement#setInt(int, int)
	 */
	public void setLong(String name, long value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setLong(indexes[i], value);
		}
	}

	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @throws SQLException
	 *             if an error occurred
	 * @throws IllegalArgumentException
	 *             if the parameter does not exist
	 * @see PreparedStatement#setTimestamp(int, java.sql.Timestamp)
	 */
	public void setTimestamp(String name, Timestamp value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			getStatement().setTimestamp(indexes[i], value);
		}
	}
	
	
	protected  String getDerivedKey(Map<String,Object> parameters){
		return SQLBuilder.getDerivedKey(parameters);
	}

}
