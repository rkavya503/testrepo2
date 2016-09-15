package com.akuacom.pss2.price.australia.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.zip.ZipInputStream;

import org.apache.log4j.lf5.util.StreamUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.akuacom.pss2.price.australia.PriceFileRecord;
import com.akuacom.pss2.price.australia.PriceWSClient;
import com.akuacom.utils.lang.DateUtil;

public class PriceWSClientTest {
	
	private static byte[] priceFileContent;
	private static String publishedFileList;
	
	@BeforeClass  
	public static void setupForAll(){
		InputStream is1 =null;
		InputStream is2 =null;
		try{
			is1 =PriceWSClientTest.class.getResourceAsStream("testPage.htm");
			String str = new String(StreamUtils.getBytes(is1));
			publishedFileList = str;
			is2 =PriceWSClientTest.class.getResourceAsStream("PUBLIC_DISPATCHIS_201209011345_0000000238795937.ZIP");
			priceFileContent=StreamUtils.getBytes(is2);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {	if(is1!=null) is1.close();} catch (IOException e) {e.printStackTrace();}
			try {   if(is2!=null) is2.close();} catch (IOException e) {e.printStackTrace();}
		}
	}
	
	@Test 
	public void testGetPrice() throws Exception{
		PriceWSClient client= new PriceWSClient()  {
			protected String getFileListFromServer(){
				return publishedFileList;
			};
			
			protected byte[] getCompressedPriceFile(String priceFileUrl ){
				return priceFileContent;
			}
		};  
		
		//latest file 2012-09-03 18:50
		Date time = DateUtil.parseDate("2012-09-03 18:50:00", 
					"yyyy-MM-dd HH:mm:ss",TimeZone.getTimeZone("GMT+10"));
		
		assertEquals(46.15,client.getPrice(time, "VIC1",0).getPrice(),0.001);
		
		time = DateUtil.parseDate("2012-09-03 18:54:00", 
				"yyyy-MM-dd HH:mm:ss",TimeZone.getTimeZone("GMT+10"));
		assertEquals(46.15,client.getPrice(time, "VIC1",0).getPrice(),0.001);
		
		//considering delay  
		time = DateUtil.parseDate("2012-09-03 18:55:00", 
				"yyyy-MM-dd HH:mm:ss",TimeZone.getTimeZone("GMT+10"));
		assertNull(client.getPrice(time, "VIC1",0));
		
		time = DateUtil.parseDate("2012-09-03 18:57:00", 
				"yyyy-MM-dd HH:mm:ss",TimeZone.getTimeZone("GMT+10"));
		assertEquals(46.15,client.getPrice(time, "VIC1",9).getPrice(),0.001);
		
		time = DateUtil.parseDate("2012-09-03 18:57:00", 
				"yyyy-MM-dd HH:mm:ss",TimeZone.getTimeZone("GMT+10"));
		assertNull(client.getPrice(time, "VIC1",6));
	}
	
	@Test
	public void testReadCompressedContent() throws IOException{
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(priceFileContent));
		TestablePriceWSClient client = new TestablePriceWSClient();
		String str=client.readFileZipStream(zis).trim();
		assertTrue(str.startsWith("C,NEMP.WORLD,DISPATCHIS,AEMO,PUBLIC,2012/09/01," +
				"13:40:21,0000000238795937,DISPATCHIS,0000000238795936"));
		assertTrue(str.endsWith("C,\"END OF REPORT\",642"));
	}
	
	@Test 
	public void testExtractPriceFromZipFile() throws IOException{
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(priceFileContent));
		TestablePriceWSClient client = new TestablePriceWSClient();
		String str=client.readFileZipStream(zis).trim();
		
		assertEquals("46.15",client.parsePrice("VIC1", str));
		assertEquals("43.76907",client.parsePrice("QLD1", str));
		assertEquals("43.5775",client.parsePrice("TAS1", str));
		assertEquals("48.1803",client.parsePrice("NSW1", str));
		assertEquals("50.99901",client.parsePrice("SA1", str));
	}
	
	//UT for UTIL Method 
	@Test
	public void testAlignToFiveMin(){
		Date date = DateUtil.parseStringToDate("2012-09-03 09:20:00",null);
		Date aligned = PriceWSClient.alignToFiveMin(date);
		assertEquals("2012-09-03 09:20:00", DateUtil.format(aligned, "yyyy-MM-dd HH:mm:ss"));
		
		date = DateUtil.parseStringToDate("2012-09-03 09:21:00",null);
		aligned = PriceWSClient.alignToFiveMin(date);
		assertEquals("2012-09-03 09:20:00", DateUtil.format(aligned, "yyyy-MM-dd HH:mm:ss"));
		
		date = DateUtil.parseStringToDate("2012-09-03 09:24:59",null);
		aligned = PriceWSClient.alignToFiveMin(date);
		assertEquals("2012-09-03 09:20:00", DateUtil.format(aligned, "yyyy-MM-dd HH:mm:ss"));
		
		date = DateUtil.parseStringToDate("2012-09-03 09:25:01",null);
		aligned = PriceWSClient.alignToFiveMin(date);
		assertEquals("2012-09-03 09:25:00", DateUtil.format(aligned, "yyyy-MM-dd HH:mm:ss"));
	}
	
	
	@Test 
	public void testLatestPriceFile() throws Exception{
		InputStream is =
			PriceWSClientTest.class.getResourceAsStream("testPage.htm");
		String fileList = new String(StreamUtils.getBytes(is));
		is.close();
		TestablePriceWSClient client = new TestablePriceWSClient();
		PriceFileRecord file =client.getLatestFile(fileList);
		assertEquals("201209031850",file.getDateStr());
	}
	
	@Test 
	public void testFormatAsAustraliaPriceFile() throws Exception{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TimeZone bjTimeZone = TimeZone.getTimeZone("GMT+8");
		format.setTimeZone(bjTimeZone);
		
		Date date = format.parse("2012-09-03 09:20:00");
		String str =PriceWSClient.formatAsAustraliaPriceFile(date);
		assertEquals("201209031120",str);
	}
	
	
	public static class TestablePriceWSClient extends PriceWSClient{
		@Override
		public String parsePrice(String location, String fileContent) {
			return super.parsePrice(location, fileContent);
		}
		
		@Override
		public String readFileZipStream(ZipInputStream zips)
				throws IOException {
			return super.readFileZipStream(zips);
		}
		
		@Override
		public PriceFileRecord getLatestFile(String fileList){
			return super.getLatestFile(fileList);
		}
	}
}

