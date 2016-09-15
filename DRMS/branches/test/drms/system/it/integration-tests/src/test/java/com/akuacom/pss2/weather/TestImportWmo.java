package com.akuacom.pss2.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.ejb.jboss.test.JBossFixture;

@Ignore
public class TestImportWmo {
	WeatherManager manager = JBossFixture.lookupSessionRemote(WeatherManager.class);
	
	private static final String REGEX = "<a href=\"/products.*</a>";
	
    private static Pattern pattern;
    private static Matcher matcher;
	
    @Ignore
	@Test
	public void testSaveNSWName() throws DuplicateKeyException {
		WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(this
		        .getClass().getClassLoader().getResourceAsStream("nswall.shtml")));
		try{
			storeData(manager, br);
		}finally{
			 try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
    @Ignore
	@Test
	public void testSaveVictoriaName() throws DuplicateKeyException {
		WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(this
		        .getClass().getClassLoader().getResourceAsStream("victoria.shtml")));
		try{
			storeData(manager, br);
		}finally{
			 try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    @Ignore
    @Test
	public void testSaveQldName() throws DuplicateKeyException {
		WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(this
		        .getClass().getClassLoader().getResourceAsStream("qldall.shtml")));
		try{
			storeData(manager, br);
		}finally{
			 try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    @Test
	public void testSaveSaName() throws DuplicateKeyException {
		WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(this
		        .getClass().getClassLoader().getResourceAsStream("saall.shtml")));
		try{
			storeData(manager, br);
		}finally{
			 try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    
    @Test
	public void testSaveWaName() throws DuplicateKeyException {
		WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(this
		        .getClass().getClassLoader().getResourceAsStream("waall.shtml")));
		try{
			storeData(manager, br);
		}finally{
			 try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    @Test
	public void testSaveTasaName() throws DuplicateKeyException {
		WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(this
		        .getClass().getClassLoader().getResourceAsStream("tasall.shtml")));
		try{
			storeData(manager, br);
		}finally{
			 try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    @Test
	public void testSaveAntaName() throws DuplicateKeyException {
		WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(this
		        .getClass().getClassLoader().getResourceAsStream("antall.shtml")));
		try{
			storeData(manager, br);
		}finally{
			 try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    @Test
	public void testSaveNtallName() throws DuplicateKeyException {
		WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(this
		        .getClass().getClassLoader().getResourceAsStream("ntall.shtml")));
		try{
			storeData(manager, br);
		}finally{
			 try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    
    

	private void storeData(WeatherManager manager, BufferedReader br) {
		String strLine;
		  //Read File Line By Line
		 int i = 0;
		  try {
			while ((strLine = br.readLine()) != null)   {
				  // Initialize
			        pattern = Pattern.compile(REGEX);
			        matcher = pattern.matcher(strLine);
			       
			        if(matcher.find()){
			        	ForecastWmo vo = new ForecastWmo();
				        String line = matcher.group();
				        int index = line.indexOf("products/");
				        int index2 = line.indexOf(".shtml");
				        vo.setWmo(line.substring(index+9, index2)+".axf");
				        
				        int index3 = line.indexOf(">");
				        int index4 = line.lastIndexOf("<");
				        vo.setCity(line.substring(index3+1, index4));
				        manager.saveWmoMapping(vo);
				        i++;
								       
			        }
			  }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
