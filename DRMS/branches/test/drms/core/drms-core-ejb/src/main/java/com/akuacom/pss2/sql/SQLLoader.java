package com.akuacom.pss2.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.akuacom.pss2.data.DataManagerBean;

public class SQLLoader {
	
	private static final Logger log = Logger.getLogger(SQLLoader.class);
	
	public static final String INSERT_EVENT_BASELINE_SQL;
		
	static {
		INSERT_EVENT_BASELINE_SQL = getSQLFromFile("insertEventBaseline.sql");
	}
	
	private static String getSQLFromFile(String sqlFileName) {
		String sql = "";
		InputStream is = null;
		try{  
			is = DataManagerBean.class.getResourceAsStream("/com/akuacom/pss2/sql/" + sqlFileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String s = "";
			while ((s = br.readLine()) != null) {
				sql = sql + s + " \n ";
			}
		}
		catch (Exception e) {
			log.error("unable to load sql file " +sqlFileName);
		}
		finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {}
			}
		}
		if(sql.equals(""))
			return null;
		return sql;
	}
}
