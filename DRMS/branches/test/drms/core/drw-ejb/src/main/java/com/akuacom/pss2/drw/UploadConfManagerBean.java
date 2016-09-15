package com.akuacom.pss2.drw;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.ejb3.annotation.RemoteBinding;

import com.akuacom.pss2.drw.eao.EventEAO;

@Stateless	
@LocalBinding(jndiBinding="dr-pro/UploadConfManager/local")
@RemoteBinding(jndiBinding="dr-pro/UploadConfManager/remote")
public class UploadConfManagerBean implements UploadConfManager.L,UploadConfManager.R {
	
	@PersistenceContext(unitName = "drw")
	protected EntityManager em;
	
	@Resource(mappedName="java:mysql-drwebsite-ds") 
	private DataSource dataSource;
	
	@EJB
	EventEAO.L eventEAO;
	
	@EJB
	DrwSQLExecutor.L drwSqlExecutor;
	
	private static final Logger log = Logger
			.getLogger(UploadConfManagerBean.class);
	private static String DROP_TABLE_SQL; 
	private static String CREATE_TABLE_SQL;
	private static String INIT_TEMP_SQL;
	private static String TRUNCATE_ZIPCODE_SQL;
	private static String TRUNCATE_LOCATION_SQL;
	private static String LOAD_SLAP_SQL;
	private static String LOAD_ABANK_SQL;
	private static String LOAD_SUB_SQL;
	private static String LOAD_SLAP_ZIP_SQL;
	private static String LOAD_ABANK_ZIP_SQL;
	private static String LOAD_SUB_ZIP_SQL;
	
	//Static block, used to initialize sql
	static {
		try {
			DROP_TABLE_SQL = getSQLFromFile("drop_table.sql");
			CREATE_TABLE_SQL=getSQLFromFile("create_table.sql");
			INIT_TEMP_SQL = getSQLFromFile("init_temp.sql");
			TRUNCATE_ZIPCODE_SQL = getSQLFromFile("truncate_zipcode.sql");
			TRUNCATE_LOCATION_SQL = getSQLFromFile("truncate_location.sql");
			LOAD_SLAP_SQL = getSQLFromFile("load_slap.sql");
			LOAD_ABANK_SQL =getSQLFromFile("load_abank.sql");
			LOAD_SUB_SQL=getSQLFromFile("load_sub.sql");
			LOAD_SLAP_ZIP_SQL=getSQLFromFile("load_slap_zip.sql");
			LOAD_ABANK_ZIP_SQL=getSQLFromFile("load_abank_zip.sql");
			LOAD_SUB_ZIP_SQL= getSQLFromFile("load_sub_zip.sql");
			
		} catch (Exception e) {
			log.error("Error happened while trying to get SQL from source file." + e.getStackTrace());
		}		
	}
	
	/**
	 * Save uploaded file to database
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void batchInsert(List<String[]> result){
		Connection conn = null;
	    PreparedStatement pstmt = null;
	    try {
	      conn = getConnection();
	      conn.setAutoCommit(false);
	      String query = INIT_TEMP_SQL;
	      pstmt = conn.prepareStatement(query);
	      
	      for(String[] line:result){
	    	  pstmt.setString(1, line[0]);
	    	  pstmt.setString(2, line[1]);
	    	  pstmt.setString(3, line[2]);
	    	  pstmt.setString(4, line[3]);
	    	  pstmt.setString(5, line[4]);
	    	  pstmt.setString(6, line[5]);
	    	  pstmt.setString(7, line[6]);
	    	  pstmt.setString(8, line[7]);
	    	  pstmt.setString(9, line[8]);
	    	  pstmt.setString(10, line[9]);
	    	  pstmt.setString(11, line[10]);
	    	  
		      pstmt.addBatch();
	      }
	      pstmt.executeBatch();
	      conn.commit();
	    } catch (BatchUpdateException e) {
	      try {
	        conn.rollback();
	      } catch (Exception e2) {
	        e.printStackTrace();
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
		    try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		    try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	}
	
	/**
	 * Generate slap location info
	 */
	@Override
	public void loadSlap() {
		String query = LOAD_SLAP_SQL;
		try {
			drwSqlExecutor.execute(query, new HashMap<String, Object>());
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
	}

	/**
	 * Generate Abank location info
	 */
	@Override
	public void loadAbank() {
		String query = LOAD_ABANK_SQL;
		try {
			drwSqlExecutor.execute(query, new HashMap<String, Object>());
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
	}

	/**
	 * Generate substation info
	 */
	@Override
	public void loadSubstation() {
		String query = LOAD_SUB_SQL;
		try {
			drwSqlExecutor.execute(query, new HashMap<String, Object>());
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
	}

	/**
	 * Generate zipcode info for Slap
	 */
	@Override
	public void loadSlapZip() {
		String query = LOAD_SLAP_ZIP_SQL;
		try {
			drwSqlExecutor.execute(query, new HashMap<String, Object>());
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
	}

	/**
	 * Generate zipcode info for abank
	 */
	@Override
	public void loadAbankZip() {
		String query = LOAD_ABANK_ZIP_SQL;
		try {
			drwSqlExecutor.execute(query, new HashMap<String, Object>());
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
	}

	/**
	 * Generate zipcode info for substation
	 */
	@Override
	public void loadSubZip() {
		String query = LOAD_SUB_ZIP_SQL;
		try {
			drwSqlExecutor.execute(query, new HashMap<String, Object>());
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
	}

	/**
	 * Clear original location data
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void clearLocation() {
		String query = TRUNCATE_LOCATION_SQL;
		Connection conn = null;
	    PreparedStatement pstmt = null;
	    try {
	      conn = getConnection();
	      pstmt = conn.prepareStatement(query);
	      pstmt.execute();
	    }  catch (Exception e) {
	      e.printStackTrace();
	    } finally {
		    try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		    try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	}

	/**
	 * Clear original zipcode data
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void clearZipcode() {
		String query = TRUNCATE_ZIPCODE_SQL;
		Connection conn = null;
	    PreparedStatement pstmt = null;
	    try {
	      conn = getConnection();
	      
	      pstmt = conn.prepareStatement(query);
	      
	      pstmt.execute();
	    }  catch (Exception e) {
	      e.printStackTrace();
	    } finally {
		    try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		    try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		
	}

	/**
	 * drop the temp table used to store configuration info.
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void dropTemp() {
		String query = DROP_TABLE_SQL;
		Connection conn = null;
	    PreparedStatement pstmt = null;
	    try {
	      conn = getConnection();
	      pstmt = conn.prepareStatement(query);
	      pstmt.execute();
	    }  catch (Exception e) {
	      e.printStackTrace();
	    } finally {
		    try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		    try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		
	}

	/**
	 * Create the temp table used to store configuration info.
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void createTemp() {
		String query = CREATE_TABLE_SQL;
		Connection conn = null;
	    PreparedStatement pstmt = null;
	    try {
	      conn = getConnection();
	      pstmt = conn.prepareStatement(query);
	      pstmt.execute();
	    }  catch (Exception e) {
	      e.printStackTrace();
	    } finally {
		    try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		    try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		
	}

	@Override
	public long checkActiveEvents() {
		return eventEAO.getActiveEventCountsByPrograms(Arrays.asList("SDP","API"));
	}
	
	/**
	 * A private method used to load sql from outer files.
	 * @param sqlFileName
	 * @return
	 * @throws Exception
	 */
	private static String getSQLFromFile(String sqlFileName) throws Exception{
		String sql = "";
		InputStream is = null;
		try{  
			is = UploadConfManagerBean.class.getResourceAsStream("/com/akuacom/pss2/drw/" + sqlFileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String s = "";
			while ((s = br.readLine()) != null) {
				sql = sql + s + " \n ";
			}
		}
		catch (Exception e) {
			throw e;
		}
		finally{
			if(is != null){
				is.close();
			}
		}
		return sql;
	}
	
	protected Connection getConnection() throws SQLException{
		return dataSource.getConnection();
	}
	
}
