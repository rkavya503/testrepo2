package com.akuacom.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 *  <tt>SQLExecutor</tt> is a very lightweight layer on top of jdbc which provides more OO style API for SQL operation. 
 *  for example, It will return a list of target Pojo not ResultSet for a SQL query.
 *  <PRE>
 *   public class SailsReport(){
 *  	private String area;
 *  	private double amount;
 *  		
 *  	//setters and getters
 *          ....
 *    }
 *      
 *    String reportsql = "select a.area as area, sum(c.price*count) as amount .... ";
 *    SQLExecutor sqlcommander = lookup(....);
 *    List<SailsReport> report = sqlcommander.execute(getConnection(),reportsql,
 *      				new ListConverter<SailsReport>(new ColumnAsFeatureFactory<SailsReport>(SailsReport.class));
 *      
 *    for(SailsReport item:SailsReport){
 *     	System.out.println(item.getArea());
 *    	System.out.println(item.getAmount());
 *    }
 *  </PRE>
 */
public interface SQLExecutor {

    /**
     * @param <T> the target pojo type 
     * @param jdbcConnection connection for sql command
     * @param sql the native sql statement
     * @param params parameters in order of its position in sql statement 
     * @param converter the converter to convert ResultSet to target Object
     * @return Object of expected type
     * @throws SQLException
     */
	public <T>  T doNativeQuery(Connection jdbcConnection,
							String sql,Object[]params, Converter<T> converter)
						throws SQLException;
	 /**
     * @param <T> the target pojo type 
     * @param jdbcConnection connection for sql command
     * @param sql the native sql statement
     * @param converter the converter to convert ResultSet to target Object
     * @return object of expected type  
     * @throws SQLException
     */
	public <T> T doNativeQuery(Connection jdbcConnection,
						String sql, Converter<T> converter)
						throws SQLException;
	
	
	/**
    * @param <T> the target pojo type 
    * @param jdbcConnection connection for sql command
    * @param sql the native sql statement with named parameter
    * @namedParameters the named parameter and values
    * @param converter the converter to convert ResultSet to target Object
    * @return object of expected type  
    * @throws SQLException
    */
	public <T> T doNativeQuery(Connection jdbcConnection,
						String sql, Map<String,Object> namedParameters,Converter<T> converter)
						throws SQLException;
	
	
	
	/**
	 * 
	 * @param jdbcConnection connection for sql command
	 * @param sql sql statement 
	 * @param namedParameters named parameter and values
	 * @return affected row count
	 * @throws SQLException
	 */
	public int execute(Connection jdbcConnection,String sql)
						throws SQLException;
	
	
	
	/**
	 * @param jdbcConnection db connection for sql command 
	 * @param sql sql statement with named parameter 
	 * @param parameters parameter name and value
	 * @return affected row count
	 * @throws SQLException
	 */
	public int execute(Connection jdbcConnection,
					   String sql,Map<String,Object> parameters)
					   throws SQLException;
		
	
	/**
	 * @param jdbcConnection db connection for sql command 
	 * @param sql sql statement with posiitoned parameter
	 * @param parameters parameters 
	 * @return affected row count
	 * @throws SQLException
	 */
	public int execute(Connection jdbcConnection,
			   String sql, Object[] parameters)
			   throws SQLException;
}