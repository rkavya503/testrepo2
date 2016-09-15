package com.akuacom.pss2.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.ejb.jboss.test.JBossFixture;

@Ignore
public class TestImportIconMapping {
	WeatherManager manager = JBossFixture.lookupSessionRemote(WeatherManager.class);
	
	private static final String filename = "IDA00002.dat";
	
	@Test
	public void testSaveNSWName() throws DuplicateKeyException {
		WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(this
		        .getClass().getClassLoader().getResourceAsStream(filename)));
		try{
			HashSet set = new HashSet();
		   	  String strLine;
		   	  //Read File Line By Line
		   	  boolean isFrist = true;
		   	  while ((strLine = br.readLine()) != null)   {
		   		  if(isFrist){
		   			isFrist = false;
		   			continue;
		   		  }
		   		  String[] content = strLine.split("#");
		   		  for(int i=22;i<content.length;i++){
		   			set.add(content[i]);
		   		  }
		   	  }
		   	  Iterator item = set.iterator();
		   	  while(item.hasNext()){
		   		  String type = item.next().toString();
		   		  manager.getWeatherIconMapping(type);
		   	  }
		   	  
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			 try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
