package com.akuacom.pss2.drw.entry;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class LoadFileUtil {
  
  public static Reader loadSQLFromFile(Class<?> packClass, String relativePathAndName) throws IOException  {
		InputStream is = null;
		is = packClass.getResourceAsStream(relativePathAndName);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		return br;
	}
} 