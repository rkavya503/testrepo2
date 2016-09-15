package com.akuacom.pss2.drw.admin;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import com.akuacom.pss2.drw.LocationKmlManager;
import com.akuacom.pss2.drw.core.LocationKmlEntry;


public class FileHandler implements Runnable{
	Logger log = Logger.getLogger(FileHandler.class);
	private LocationKmlManager manager;
	private String filePath;
	private ProgressUpdater progressUpdater;
	private HttpSession session;
	private String locationType;
	
	public FileHandler(LocationKmlManager manager, ProgressUpdater progressUpdater, String filePath, HttpSession session, String locationType) {
		this.manager = manager;
		this.filePath = filePath;
		this.progressUpdater = progressUpdater;
		this.session = session;
		this.locationType = locationType;
	}
	
	@Override
	public void run() {
		LineNumberReader lnr = null;
    	try{
    		lnr = new LineNumberReader(new FileReader(filePath));
    		String lineRead = "";
    		while ((lineRead = lnr.readLine()) != null) {
    			String[] tokens = lineRead.split("','");
    			if (tokens.length == 2) {
					String number = tokens[0].replaceAll("'", "");
					String lineValue = tokens[1].replaceAll("'", "");// multi poylgons
					
					String strpattern = "<Polygon>.*?</Polygon>";
					Pattern pattern = Pattern.compile(strpattern);
					Matcher matcher = pattern.matcher(lineValue);
					
					manager.delete(locationType, number);
					while(matcher.find()){
						String temp = matcher.group();//polygon
						
						//*****
						StringBuffer sb = new StringBuffer();
						sb.append("{\"POLYGON\":[{");
						sb.append("\"outerBoundaryIs\": [");
						sb.append(" {\"coordinates\":[");
						//***
						String strpatternO = "<outerBoundaryIs>.*?</outerBoundaryIs>";
						
						
						Pattern patternO = Pattern.compile(strpatternO);
						Matcher matcherO = patternO.matcher(temp);
						LatLngBounds bounds = new LatLngBounds();
						while(matcherO.find()){
							String outerBoundaryIs = matcherO.group();//<outerBoundaryIs>...</outerBoundaryIs>
//							<outerBoundaryIs><LinearRing><coordinates>-118.192308697
							//loop
							 String line = getTagValue("coordinates", outerBoundaryIs);
						      String[] values = line.split(" ");
						      boolean first = true;
						      for(String cur: values){
						    	  if(!first){
						    		  sb.append(",");
						    	  }
						    	  sb.append("{\"lat\":"+cur.split(",")[1]+",\"lng\":"+cur.split(",")[0]+"}");
						    	  bounds.extend(cur.split(",")[0], cur.split(",")[1]);
						    	  first = false;
						      }
						}
						//end loop
						sb.append("]}");//end of coordinates
						sb.append("]");//end of outerBoundaryIs
						
						sb.append(",\"innerBoundaryIs\": [ ");
						
						String strpatterni = "<innerBoundaryIs>.*?</innerBoundaryIs>";
						Pattern patterni = Pattern.compile(strpatterni);
						Matcher matcheri = patterni.matcher(temp);
						boolean isFirstCoordinates = true;
						while(matcheri.find()){
							if(!isFirstCoordinates){
								sb.append(",");
							}
							sb.append("{\"coordinates\":[");
//							<innerBoundaryIs><LinearRing><coordinates>
							String innerBoundaryIs = matcheri.group();//<innerBoundaryIs>...</innerBoundaryIs>
							 String line = getTagValue("coordinates", innerBoundaryIs);
						      String[] values = line.split(" ");
						      boolean first = true;
						      for(String cur: values){
						    	  if(!first){
						    		  sb.append(",");
						    	  }
						    	  sb.append("{\"lat\":"+cur.split(",")[1]+",\"lng\":"+cur.split(",")[0]+"}");
						    	  first = false;
						      }
						      sb.append("]}");
						      isFirstCoordinates = false;
						}
						
						sb.append(" ]");//end of innerBoundaryIs:
						//******
						
						sb.append(",\"bounds\":"+bounds.getJsonString());
						//**end of polygon content
						 sb.append("}]" + " }");
						
						LocationKmlEntry entry = new LocationKmlEntry();
	        			entry.setNumber(number);
	        			entry.setKml(sb.toString());
	        			entry.setLocationType(locationType);
	        			manager.insert(entry); // insert new records
					}
					
				}
    			progressUpdater.progress(1);
    			session.setAttribute("curvalue", progressUpdater.getDonePercentage());
    		}
    		
           
    	}catch (Exception ex){
    		log.debug("###Baseline generation error### : error occured"+ex);
    	}finally{
    		try {
    			lnr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
	}
	
	private static String  getTagValue(String tag, String in){
		int start = in.indexOf("<"+tag+">");
		int end = in.indexOf("</"+tag+">");
		return in.substring(start+2+tag.length(), end);
		
	}
}
