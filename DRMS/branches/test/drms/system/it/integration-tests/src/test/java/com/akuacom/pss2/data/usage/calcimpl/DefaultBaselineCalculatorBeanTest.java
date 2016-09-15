package com.akuacom.pss2.data.usage.calcimpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.usage.BaselineConfig;
import com.akuacom.pss2.data.usage.DailyUsage;
import com.akuacom.pss2.data.usage.UsageDataEntry;
import com.akuacom.pss2.data.usage.UsageDataManager;
@Ignore
public class DefaultBaselineCalculatorBeanTest {
	private List<PDataEntry> deList;
	private BaselineConfig bc; 
	private Date date;
	private DateRange dr;
	public static final long MA_START_TIME = 12 * 60 * 60 * 1000;
	/** The Constant MA_END_TIME. */
	public static final long MA_END_TIME = 20 * 60 * 60 * 1000;
	    
	@SuppressWarnings("unchecked")
	@Before
	public void setup(){
		bc = (BaselineConfig) readObject("BaselineConfig");
		dr = (DateRange) readObject("DateRange");
		deList = (List<PDataEntry>) readObject("List-PDataEntry");
		date = (Date) readObject("Date");
	}
	
	
	@Test
	@Ignore
	public void testCalculate(){
		Double[] result = {14.207806785142866,12.34133033696504,14.584181356861764,14.313678346974182,12.705729355280406,
				           13.844261245303695,14.38719489577879,12.407312769023896,14.06631266327515,14.175124219626202,
				           12.290900806777753,14.5432146050755,12.813074810589544,24.066626623774027,26.743152150627147,
				           28.514414781868883,28.724472043692124,31.846621693440998,31.40728558407438,15.555610791171228,
				           16.005441186652767,18.714926488235797,19.339586091387407,20.883876452585756,23.40811834071832,
				           28.30030439767922,27.24489178556121,39.286052516890955,41.57852332681407,63.72978568162327,
				           65.11592214818143,69.36683975328869,69.98810977335744,66.36523657233022,64.75257547230099,
				           55.77518518827118,47.58022759582295,50.47099719693007,58.622137135647904,58.74076622331418,
				           63.01369643139391,58.196941476584364,65.37802504760339,62.44770888557063,60.27418227862896,
				           63.78183741095907,66.38416882497417,67.99577206072935,71.71524775751904,65.51996780449173,
				           66.65602312511757,62.93182850804352,66.98322610365044,59.48177300835997,59.886518596187386,
				           57.35425843833286,58.6778184926362,55.73645707839808,62.04608105701858,54.84759646350725,
				           60.094100117613706,56.4343112300822,59.06887627567515,55.466594744810806,61.67910642195547,
				           63.37517485097719,64.3487667840755,61.30366177578655,66.25327032714935,40.44149162444151,
				           36.10272806232709,37.598600553479386,33.76583081860324,31.423207024837957,31.00692350346239,
				           34.58300870433745,30.72651440243367,14.108497692752486,13.882029208984495,19.076780061804435,
				           19.01097516445815,18.74720737166362,13.965258493750063,15.900412160404636,14.325002291765262,
				           14.429933307991085,12.809064138931813,16.278908656599235,12.459257660273147,14.08520103535743,
				           14.677271733514637,31.42296482575844,12.619014224193549,14.691545707197708,14.255586828183699,
				           14.00574854835312
				           };
		
		BaselineCalculator baselineCalculator = ImplFactory.instance().getBaselineCalculation(null);
		List<PDataEntry> ret =  baselineCalculator.calculate(deList, bc, date, dr);
		assertEquals(ret.size(),96);
		
		for(int i=0;i<ret.size();i++){
			assertEquals(result[i],ret.get(i).getValue());
		}
		
	}
	
	private Object readObject(String name){
		
		URL testFile =
			Thread.currentThread().getContextClassLoader().getResource("");
		
		System.out.println("Thread.currentThread().getContextClassLoader().getResource(''): "+testFile);

		File folder = null;
		try {
			folder = new File(testFile.toURI());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		
		if(folder.isDirectory()){
			File [] files = folder.listFiles();
			if(files!=null){
				for(int i=0;i<files.length;i++){
					System.out.println("Files in this folder "+files[i].getName()+"   path is :"+files[i].getAbsolutePath());
				}
			}
		}
       
		name = name+".txt";
	    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
    	if (in == null) {
    		in = this.getClass().getClassLoader().getResourceAsStream(name);
    		if (in == null) {
    			in = this.getClass().getResourceAsStream("/"+name);
    			if (in == null) {
    				System.out.println("Could not find factory class for resource: " + name);
    			}
    			
    		}
    	}
	        
		ObjectInputStream oi = null;
		
		Object obj = null;
		try {
			 oi = new ObjectInputStream(in);
			
			 obj = oi.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				oi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return obj;
	}
}
