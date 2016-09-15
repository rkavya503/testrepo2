package com.akuacom.pss2.price.australia;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.akuacom.utils.lang.DateUtil;

public class PriceWSClient {
	
	private static final Logger log = Logger.getLogger(PriceWSClient.class);
	
	public  static final String PRICE_SERVER="http://www.nemweb.com.au";
	public  static final String PRICE_SERVER_FILE_URL=PRICE_SERVER+"/REPORTS/CURRENT/DispatchIS_Reports/";
	public  static final String FILE_PREFIX="PUBLIC_PREDISPATCHIS_"; 
	public  static final int DISPATCH_INTERVAL = 5;  //min
	public  static final String PRICE_FILE_PREFIX="/REPORTS/CURRENT/DispatchIS_Reports/PUBLIC_DISPATCHIS";
	private static final TimeZone AUSTRALIA_TIME_ZONE = TimeZone.getTimeZone("GMT+10");
	private static final String  TIME_FORMAT_IN_FILE_NAME ="yyyyMMddHHmm";
	private static final Pattern FILE_URL_PATTERN=Pattern.compile(
					PRICE_FILE_PREFIX+"_((\\d){12})_((\\d){16}).ZIP");
			
	public PriceRecord getPrice(Date time,String location,int maxDelay) {
		String priceFileList = this.getFileListFromServer();
		if (priceFileList == null)
			return null;
		
		// get the file to be down-loaded
		Date alignedDate = alignToFiveMin(time);
		PriceFileRecord pricefile = getPriceFile(alignedDate,priceFileList);
		//try to get earlier price file according to max allowed delay
		if(pricefile==null){
			log.warn("NO price availabe for time "
						+DateUtil.format(alignedDate,"yyyy-MM-dd HH:mm:ss",AUSTRALIA_TIME_ZONE));
			
			PriceFileRecord latestpriceFile = getLatestFile(priceFileList);
			if(latestpriceFile==null){
				log.warn("NO available price file");
				return null;
			}
			try {
				Date latestDate = DateUtil.parseDate(latestpriceFile.getDateStr(),
							TIME_FORMAT_IN_FILE_NAME, AUSTRALIA_TIME_ZONE);
				//Earliest acceptable time according to max delay allowed 
				Date earilestTime = new Date(time.getTime() - maxDelay * DateUtil.MSEC_IN_MIN);
				
				if(latestDate.getTime()>earilestTime.getTime()){
					pricefile = latestpriceFile;
				}else{
					log.warn("NO delayed price availabe for time "
							+DateUtil.format(earilestTime,"yyyy-MM-dd HH:mm:ss",AUSTRALIA_TIME_ZONE));
					return null;
				}
			} catch (ParseException e) {
				log.error("No Match Time format in file Name "+latestpriceFile.getFileLocation() );
				return null;
			}
		}
		
		String priceFileUrl = PRICE_SERVER + pricefile.getFileLocation();
		byte data[] =getCompressedPriceFile(priceFileUrl);
		if (data == null){
			log.warn("NO price availabe due to empty content of file "+priceFileUrl);
			return null;
		}
		String fileContent = "";
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(data));
		try {
			fileContent = readFileZipStream(zis);
		} catch (IOException e) {
			log.error("error to read compressed stream ",e);
			return null;
		}finally{
			try {zis.close();} catch (IOException e) {}
		}
		
		// extract the price value for given location
		PriceRecord record = new PriceRecord();
		record.setLocation(location);
		record.setTime(alignedDate);
		
		String price=parsePrice(location, fileContent);
		if(price!=null)
			record.setPrice(Double.valueOf(price));
		else
			return null;
		return record;
	}
	
	protected String getFileListFromServer(){
		// Create a response handler
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		// read all file list
		String responseBody = getResponse(PRICE_SERVER_FILE_URL,
				responseHandler);
		return responseBody;
	}
	
	protected byte[] getCompressedPriceFile(String priceFileUrl ){
		// down-load the file
		ResponseHandler<byte[]> handler = new ResponseHandler<byte[]>() {
			public byte[] handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
						return EntityUtils.toByteArray(entity);
				} else {
					return null;
				}
			}
		};
		byte[] data = getResponse(priceFileUrl, handler);
		return data;
	}
	
	protected static <T> T getResponse(String serverURL, ResponseHandler<T> responseHandler){
		 HttpClient httpclient = new DefaultHttpClient();
	     try {
	         HttpGet httpget = new HttpGet(serverURL);
	         httpget.addHeader("Cache-Control", "no-cache");
	          //Create a response handler
	         T t=  httpclient.execute(httpget, responseHandler);
	         return t;
	     } catch(Exception e){
	    	 log.error("Http access error for "+serverURL,e);
	    	 return null;
	     }finally{
	    	  httpclient.getConnectionManager().shutdown();
	     }
	}
	
	protected String parsePrice(String location,String fileContent){
		StringBuffer patternStr = new StringBuffer();
		patternStr.append("D,DISPATCH,PRICE,4,");
		patternStr.append("\"\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}\",");
		patternStr.append("\\d{1},");
		patternStr.append(location+",");
		patternStr.append("\\d*,");
		patternStr.append("\\d*,");
		//price as a double number 
		patternStr.append("(\\d+(.\\d+)?)");
		Pattern pricePattern=Pattern.compile(patternStr.toString());
		
		Matcher m = pricePattern.matcher(fileContent);
		//the very fist match in the file content
		if(m.find()){
			return m.group(1);
		}
		return null;
	}
	
	protected String readFileZipStream(ZipInputStream zips) throws IOException{
		 ZipEntry entry = zips.getNextEntry(); //only one file
		 try{
			 StringBuffer content = new StringBuffer();
			 if(entry!=null){
			 	final int BUFFER = 1024;
			 	byte data[] = new byte[BUFFER];
			 	int size =0;
				while( (size=zips.read(data,0,BUFFER))>0){
					if(size==BUFFER)
						content.append(new String(data));
					else{
						byte d[] = new byte[size];
						System.arraycopy(data, 0, d, 0, size);
						content.append(new String(d));
					}
			 		data = new byte[BUFFER];
			 	}
			 }else{
				 log.warn("No Price available due to empty content in compressed file or bad zip format");
			 }
			 return content.toString();
		 }finally{
			 zips.closeEntry();
		 }
	}
	
	protected PriceFileRecord getPriceFile(Date date,String avaliableFiles){
		String reports=avaliableFiles;
		String patternStr =PRICE_FILE_PREFIX;
		patternStr+="_"+formatAsAustraliaPriceFile(date);;
		patternStr+="_((\\d){16}).ZIP";
			
		Pattern p = Pattern.compile(patternStr);
        Matcher m = p.matcher(reports);
        if(m.find()){
        	return new PriceFileRecord(m.group(1),m.group());
        }
        return null;
	}
	
	protected PriceFileRecord getLatestFile(String availableFiles){
        Matcher m = FILE_URL_PATTERN.matcher(availableFiles);
        String fileName = null,dateStr = null;
        while(m.find()){
        	fileName = m.group();
        	dateStr = m.group(1);
        }
        //the last match
        if(fileName!=null && dateStr!=null)
        	return new PriceFileRecord(dateStr,fileName);
        return null;
	}
	
	public static Date alignToFiveMin(Date date){
		long count = date.getTime() / (5*DateUtil.MSEC_IN_MIN);
		return new Date(count*5*DateUtil.MSEC_IN_MIN);
	}
	
	public static String formatAsAustraliaPriceFile(Date date){
		return DateUtil.format(date, TIME_FORMAT_IN_FILE_NAME,AUSTRALIA_TIME_ZONE);
	}
	
}
