package com.akuacom.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SQLLoader {
	
	public static String loadSQLFromFile(Class<?> packClass, String relativePathAndName) throws IOException  {
		String sql = "";
		InputStream is = null;
		try{  
			is = packClass.getResourceAsStream(relativePathAndName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String s = "";
			while ((s = br.readLine()) != null) {
				sql = sql + s + " \n ";
			}
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
