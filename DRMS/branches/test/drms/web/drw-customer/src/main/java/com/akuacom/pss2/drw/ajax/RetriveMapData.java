package com.akuacom.pss2.drw.ajax;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.akuacom.pss2.drw.constant.DRWConstants;
import com.akuacom.pss2.drw.model.EventCache;

public class RetriveMapData extends HttpServlet {

	private static final long serialVersionUID = -6932468445264321294L;
	private static String header = "<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">";

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		//TODO: need to handle multi events  different name according to event key, need 2 cache for sdp
		String eventKey=null;
		eventKey = req.getParameter("eventKey");
		String[] eventkeys =null;
		if(eventKey!=null){
			eventkeys = eventKey.split(",");
		}else{
			eventkeys = new String[0];
		}
		
		
		Boolean active = Boolean.valueOf(req.getParameter("active"));
		
		System.out.println(eventKey+"  active: "+active);
		boolean apiEnable = Boolean.parseBoolean(req.getParameter("api"));
		boolean sdpcEnable = Boolean.parseBoolean(req.getParameter("sdpc"));
		boolean sdprEnable = Boolean.parseBoolean(req.getParameter("sdpr"));
		boolean bipEnable = Boolean.parseBoolean(req.getParameter("bip"));
		boolean cbpEnable = Boolean.parseBoolean(req.getParameter("cbp"));
		boolean blockEnable = Boolean.parseBoolean(req.getParameter("blockFlag"));
		response.setContentType("text/xml");

		int polygonCount = 0;
		Integer index = 0;
		StringBuilder sb = new StringBuilder();
		appendHeadAndStyle(sb);
		if(sdprEnable){
			//List<String> sdprKMl = (List<String>) EventCache.getInstance().getSdprKMl();
			Map cache = null;
			if(active){
				cache = EventCache.getInstance().getActSDPResiKml().getValue();
			}else{
				cache = EventCache.getInstance().getScheSDPResiKml().getValue();
			}
			if("ALL".equalsIgnoreCase(eventKey)){
				Set keys = cache.keySet();
				Iterator item = keys.iterator();
				while(item.hasNext()){
					String key = (String) item.next();
					polygonCount = generateKML4Sdpr(key, polygonCount, sb, index,cache);
				}
			}else{
				for(String key:eventkeys){
					polygonCount = generateKML4Sdpr(key, polygonCount, sb, index,cache);
				}
				
			}
			
			
		}
		if(sdpcEnable){
			Map cache = null;
			if(active){
				cache = EventCache.getInstance().getActSDPComeKml().getValue();
			}else{
				cache = EventCache.getInstance().getScheSDPComeKml().getValue();
			}
			if("ALL".equalsIgnoreCase(eventKey)){
				Set keys = cache.keySet();
				Iterator item = keys.iterator();
				while(item.hasNext()){
					String key = (String) item.next();
					polygonCount = generateKML4Sdpc(key, polygonCount, sb,index,cache);
				}
			}else{
				for(String key:eventkeys){
					polygonCount = generateKML4Sdpc(key, polygonCount, sb, index,cache);
				}
			}
			
		}
		
		if(apiEnable){
			List<String> apiKMl = null;
			Map cache = null;
			if(active){
				 cache = EventCache.getInstance().getActApiKml().getValue();
			}else{
				 cache = EventCache.getInstance().getScheApiKml().getValue();
			}
			if("ALL".equalsIgnoreCase(eventKey)){
				Set keys = cache.keySet();
				Iterator item = keys.iterator();
				while(item.hasNext()){
					String key = (String) item.next();
					polygonCount = generateKML4Api(key, polygonCount, sb, index,cache);
				}
			}else{
				for(String key:eventkeys){
					polygonCount = generateKML4Api(key, polygonCount, sb, index,cache);
				}
			}
			
		}
		if(bipEnable){
			List<String> bipKMl = null;
			Map cache = null;
			if(active){
				 cache = EventCache.getInstance().getActBipKml().getValue();
			}else{
				 cache = EventCache.getInstance().getScheBipKml().getValue();
			}
			if("ALL".equalsIgnoreCase(eventKey)){
				Set keys = cache.keySet();
				Iterator item = keys.iterator();
				while(item.hasNext()){
					String key = (String) item.next();
					polygonCount = generateBipKML(key,polygonCount, sb, index,cache);
				}
			}else{
				for(String key:eventkeys){
					polygonCount = generateBipKML(key,polygonCount, sb, index,cache);
				}
			}
			
		}
		
		if(cbpEnable){
			List<String> cbpKMl = null;
			Map cache = null;
			if(active){
				 cache = EventCache.getInstance().getActCbpKml().getValue();
			}else{
				 cache = EventCache.getInstance().getScheCbpKml().getValue();
			}
			if("ALL".equalsIgnoreCase(eventKey)){
				Set keys = cache.keySet();
				Iterator item = keys.iterator();
				while(item.hasNext()){
					String key = (String) item.next();
					polygonCount = generateCbpKML(key,polygonCount, sb, index,cache);
				}
			}else{
				for(String key:eventkeys){
					polygonCount = generateCbpKML(key,polygonCount, sb, index,cache);
				}
			}
			
		}
		if(blockEnable){
			blockAction(req,sb,polygonCount);
		}

		kioskViewAction(req,sb,polygonCount);
		
		sb.append("  </Document>                                             													");
		sb.append("  </kml>                                             														");


		response.getWriter().write(polygonCount+"^"+sb.toString());
	}
	
	private int generateCbpKML(String eventKey, int polygonCount,
			StringBuilder sb, Integer index, Map cache) {
		List<String> cbpKml;
		cbpKml = (List<String>)cache.get(eventKey);
		String title = "<Placemark id=\""+index++ +"\">   ";
		String name ="<name>CBP"+":"+eventKey+"</name>";
		String tag ="<tag>CBP</tag>";
		if(cbpKml!=null&&(!cbpKml.isEmpty())) {
			sb.append(title);
			sb.append(name);
			sb.append(tag);
			sb.append("    <visibility>1</visibility>                                           							");
			sb.append("   <open>0</open>                                            										");
			sb.append("    <styleUrl>#browncolor</styleUrl>                                          						");
			sb.append("   <Snippet maxLines=\"0\"/>                                            								");
			sb.append("    <description>                                           											");
			sb.append("     <![CDATA[                                          												");
			sb.append("     <br><br><table border=\"1\"padding=\"1\"width=\"97%\"><tr><td>CBP:</td><td></td></tr></table>   ");
			sb.append("    ]]>                                           													");
			sb.append("   </description>                                            										");
			sb.append("   <MultiGeometry>                                            										");
			sb.append("[");
			boolean first = true;
			for(String line : cbpKml) {
				if(!first){
					sb.append(",");
				}
				polygonCount++;
				sb.append(line);
				
				first = false;
			}
			sb.append("]");
			sb.append("  </MultiGeometry>                                             										");
			sb.append("   </Placemark>                                            											");
		}
		return polygonCount;
	}



	private int generateBipKML(String eventKey, int polygonCount,
			StringBuilder sb, Integer index, Map cache) {
		List<String> bipKMl;
		bipKMl = (List<String>)cache.get(eventKey);
		String title = "<Placemark id=\""+index++ +"\">   ";
		String name ="<name>BIP"+":"+eventKey+"</name>";
		String tag ="<tag>BIP</tag>";
		if(bipKMl!=null&&(!bipKMl.isEmpty())) {
			sb.append(title);
			sb.append(name);
			sb.append(tag);
			sb.append("    <visibility>1</visibility>                                           							");
			sb.append("   <open>0</open>                                            										");
			sb.append("    <styleUrl>#blackcolour</styleUrl>                                          						");
			sb.append("   <Snippet maxLines=\"0\"/>                                            								");
			sb.append("    <description>                                           											");
			sb.append("     <![CDATA[                                          												");
			sb.append("     <br><br><table border=\"1\"padding=\"1\"width=\"97%\"><tr><td>BIP:</td><td></td></tr></table>   ");
			sb.append("    ]]>                                           													");
			sb.append("   </description>                                            										");
			sb.append("   <MultiGeometry>                                            										");
			sb.append("[");
			boolean first = true;
			for(String line : bipKMl) {
				if(!first){
					sb.append(",");
				}
				polygonCount++;
				sb.append(line);
				
				first = false;
			}
			sb.append("]");
			sb.append("  </MultiGeometry>                                             										");
			sb.append("   </Placemark>                                            											");
		}
		return polygonCount;
	}



	private int generateKML4Api(String eventKey, int polygonCount,
			StringBuilder sb, Integer index, Map cache) {
		List<String> apiKMl;
		apiKMl = (List<String>)cache.get(eventKey);
		String title = "<Placemark id=\""+index++ +"\">   ";
		String name ="<name>API"+":"+eventKey+"</name>";
		String tag ="<tag>API</tag>";
		if(apiKMl!=null&&(!apiKMl.isEmpty())) {
			sb.append(title);
			sb.append(name);
			sb.append(tag);
			sb.append("    <visibility>1</visibility>                                           							");
			sb.append("   <open>0</open>                                            										");
			sb.append("    <styleUrl>#redcolour</styleUrl>                                          						");
			sb.append("   <Snippet maxLines=\"0\"/>                                            								");
			sb.append("    <description>                                           											");
			sb.append("     <![CDATA[                                          												");
			sb.append("     <br><br><table border=\"1\"padding=\"1\"width=\"97%\"><tr><td>API:</td><td></td></tr></table>   ");
			sb.append("    ]]>                                           													");
			sb.append("   </description>                                            										");
			sb.append("   <MultiGeometry>                                            										");
			sb.append("[");
			boolean first = true;
			for(String line : apiKMl) {
				if(!first){
					sb.append(",");
				}
				polygonCount++;
				sb.append(line);
				
				first = false;
			}
			sb.append("]");
			sb.append("  </MultiGeometry>                                             										");
			sb.append("   </Placemark>                                            											");
		}
		return polygonCount;
	}



	private int generateKML4Sdpc(String eventKey, int polygonCount,
			StringBuilder sb, Integer index, Map cache) {
		List<String> sdpcKMl;
		sdpcKMl = (List<String>) cache.get(eventKey);
		String title = "<Placemark id=\""+index++ +"\">   ";
		String name ="<name>SDP-C"+":"+eventKey+"</name>";
		String tag ="<tag>SDP-C</tag>";
		if(sdpcKMl!=null&&(!sdpcKMl.isEmpty())) {
			sb.append(title);
			sb.append(name);
			sb.append(tag);
			sb.append("    <visibility>1</visibility>                                          								");
			sb.append("    <open>0</open>                                           										");
			sb.append("    <styleUrl>#violet colour</styleUrl>                                           						");
			sb.append("    <Snippet maxLines=\"0\"/>                                           								");
			sb.append("    <description>                                           											");
			sb.append("    <![CDATA[                                           												");
			sb.append("    <br><br><table border=\"1\"padding=\"1\"width=\"97%\"><tr><td>SDP-C:</td><td></td></tr></table>  ");
			sb.append("    ]]>                                           													");
			sb.append("   </description>                                            										");
			sb.append("   <MultiGeometry>                                            										");
			sb.append("[");
			boolean first = true;
			for(String line : sdpcKMl) {
				if(!first){
					sb.append(",");
				}
				polygonCount++;
				sb.append(line);
				
				first = false;
			}
			sb.append("]");
			sb.append("  </MultiGeometry>                                             										");
			sb.append("   </Placemark>                                            											");
			}
		return polygonCount;
	}



	private int generateKML4Sdpr(String eventKey, int polygonCount,
			StringBuilder sb, Integer index,Map cache) {
		List<String> sdprKMl;
		sdprKMl = (List<String>) cache.get(eventKey);
		String title = "<Placemark id=\""+index++ +"\">   ";
		String name ="<name>SDP-R"+":"+eventKey+"</name>";
		String tag ="<tag>SDP-R</tag>";
		if(sdprKMl!=null&&(!sdprKMl.isEmpty())) {
			sb.append(title);
			sb.append(name);
			sb.append(tag);
			sb.append("   <Snippet maxLines=\"0\"/>                                            								");
			sb.append("   <description>                                            											");
			sb.append("  <![CDATA[                                             												");
			sb.append("  <br><br><table border=\"1\"padding=\"1\"width=\"97%\"><tr><td>SDP-R:</td><td></td></tr></table>  	");
			sb.append("  ]]>                                             													");
			sb.append("  </description>	                                             										");
			sb.append("  <visibility>1</visibility>                                            								");
			sb.append("  <open>0</open>                                             										");
			sb.append("  <styleUrl>#sdprcolor</styleUrl>                                                    				");
			sb.append("   <MultiGeometry>                                            										");
			sb.append("[");
			boolean first = true;
			for(String line : sdprKMl) {
				if(!first){
					sb.append(",");
				}
				polygonCount++;
				sb.append(line);
				
				first = false;
			}
			sb.append("]");
			sb.append("  </MultiGeometry>                                             										");
			sb.append("  </Placemark>                                            											");
		}
		
		return polygonCount;
	}



	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	private void appendHeadAndStyle(StringBuilder sb) {
		sb.append(header);
		sb.append("    <Document>                                         					 	 ");
		sb.append("    <name>Voter Districts</name>                                          	 ");
		sb.append(" <Style id=\"redcolour\">                                             	 ");
		sb.append(" <LineStyle>                                             					 ");
		sb.append("  <color>641400FF</color>                                            		 ");
		sb.append("  <width>1</width>                                            				 ");
		sb.append(" </LineStyle>                                             					 ");
		sb.append(" <PolyStyle>                                             					 ");
		sb.append("  <color>3c1400DC</color>                                           			 ");
		sb.append("   <fill>1</fill>                                           					 ");
		sb.append("   <outline>1</outline>                                           			 ");
		sb.append("    </PolyStyle>                                          					 ");
		sb.append("    </Style>                                          						 ");
		sb.append(" <Style id=\"greencolour\">                                             	 ");
		sb.append(" <LineStyle>                                             					 ");
		sb.append("  <color>17008000</color>                                            		 ");
		sb.append("  <width>1</width>                                            				 ");
		sb.append(" </LineStyle>                                             					 ");
		sb.append(" <PolyStyle>                                             					 ");
		sb.append("  <color>591c801c</color>                                           			 ");//change transparency
		sb.append("   <fill>1</fill>                                           					 ");
		sb.append("   <outline>1</outline>                                           			 ");
		sb.append("  </PolyStyle>                                          					 ");
		sb.append("  </Style>                                          						 ");
		sb.append(" <Style id=\"blackcolour\">                                             	 ");
		sb.append(" <LineStyle>                                             					 ");
		sb.append("  <color>ff0f0f0f</color>                                            		 ");
		sb.append("  <width>1</width>                                            				 ");
		sb.append(" </LineStyle>                                             					 ");
		sb.append(" <PolyStyle>                                             					 ");
		sb.append("  <color>590f0f0f</color>                                           			 ");//change transparency
		sb.append("   <fill>1</fill>                                           					 ");
		sb.append("   <outline>1</outline>                                           			 ");
		sb.append("  </PolyStyle>                                          					 ");
		sb.append("  </Style>                                          						 ");
		sb.append("  <Style id=\"bluecolour\">                                            		 ");
		sb.append("  <LineStyle>                                            					 ");
		sb.append("  <color>ff765739</color>                                            		 ");
		sb.append("  <width>1</width>                                            				 ");
		sb.append("  </LineStyle>                                            					 ");
		sb.append("  <PolyStyle>                                            					 ");
		sb.append("   <color>59765739</color>                                           		 ");
		sb.append("  </PolyStyle>                                            					 ");
		sb.append("  </Style>                                           						 ");
		sb.append("   <Style id=\"violet colour\">                                            	 ");
		sb.append("  <LineStyle>                                             					 ");
		sb.append("  <color>ff7c0582</color>                                             		 ");
		sb.append("  <width>1</width>                                             			 ");
		sb.append("   </LineStyle>                                            					 ");
		sb.append("   <PolyStyle>                                            					 ");
		sb.append("  <color>597c0582</color>                                             		 ");
		sb.append("  </PolyStyle>                                             					 ");
		sb.append(" </Style>                                              						 ");
		//------------------------SCEC color
		sb.append(" <Style id=\"redcolor\">                                             		 ");
		sb.append(" <LineStyle>                                             					 ");
		sb.append("  <color>ff0000ff</color>                                            		 ");
		sb.append("  <width>1</width>                                            				 ");
		sb.append(" </LineStyle>                                             					 ");
		sb.append(" <PolyStyle>                                             					 ");
		sb.append("  <color>590000ff</color>                                           			 ");//change transparency
		sb.append("   <fill>1</fill>                                           					 ");
		sb.append("   <outline>1</outline>                                           			 ");
		sb.append("  </PolyStyle>                                          					 	 ");
		sb.append("  </Style>                                          						 	 ");
		//------------------------SCHD color
		sb.append(" <Style id=\"schdcolor\">                                             	 	");
		sb.append(" <LineStyle>                                             					 ");
		sb.append("  <color>ff00008e</color>                                            		 ");
		sb.append("  <width>1</width>                                            				 ");
		sb.append(" </LineStyle>                                             					 ");
		sb.append(" <PolyStyle>                                             					 ");
		sb.append("  <color>6900008e</color>                                           			 ");//change transparency
		sb.append("   <fill>1</fill>                                           					 ");
		sb.append("   <outline>1</outline>                                           			 ");
		sb.append("  </PolyStyle>                                          					 ");
		sb.append("  </Style>                                          						 ");
		//------------------------SCLD color
		sb.append(" <Style id=\"darkbluecolor\">                                             	 ");
		sb.append(" <LineStyle>                                             					 ");
		sb.append("  <color>ffff8400</color>                                            		 ");
		sb.append("  <width>1</width>                                            				 ");
		sb.append(" </LineStyle>                                             					 ");
		sb.append(" <PolyStyle>                                             					 ");
		sb.append("  <color>69ff8400</color>                                           			 ");//change transparency
		sb.append("   <fill>1</fill>                                           					 ");
		sb.append("   <outline>1</outline>                                           			 ");
		sb.append("  </PolyStyle>                                          					 ");
		sb.append("  </Style>                                          						 ");
		//------------------------SCEN
		sb.append(" <Style id=\"yellowcolor\">                                             	 ");
		sb.append(" <LineStyle>                                             					 ");
		sb.append("  <color>ff0dc8bd</color>                                            		 ");
		sb.append("  <width>1</width>                                            				 ");
		sb.append(" </LineStyle>                                             					 ");
		sb.append(" <PolyStyle>                                             					 ");
		sb.append("  <color>690dc8bd</color>                                           			 ");//change transparency
		sb.append("   <fill>1</fill>                                           					 ");
		sb.append("   <outline>1</outline>                                           			 ");
		sb.append("  </PolyStyle>                                          					 ");
		sb.append("  </Style>                                          						 ");
		//------------------------SCEN
		sb.append(" <Style id=\"scencolor\">                                             	 ");
		sb.append(" <LineStyle>                                             					 ");
		sb.append("  <color>ffe700ff</color>                                            		 ");
		sb.append("  <width>1</width>                                            				 ");
		sb.append(" </LineStyle>                                             					 ");
		sb.append(" <PolyStyle>                                             					 ");
		sb.append("  <color>59e700ff</color>                                           			 ");//change transparency
		sb.append("   <fill>1</fill>                                           					 ");
		sb.append("   <outline>1</outline>                                           			 ");
		sb.append("  </PolyStyle>                                          					 ");
		sb.append("  </Style>                                          						 ");
		//------------------------SCNW
		sb.append(" <Style id=\"scnwcolor\">                                             	 ");
		sb.append(" <LineStyle>                                             					 ");
		sb.append("  <color>ff0a9abe</color>                                            		 ");
		sb.append("  <width>1</width>                                            				 ");
		sb.append(" </LineStyle>                                             					 ");
		sb.append(" <PolyStyle>                                             					 ");
		sb.append("  <color>590a9abe</color>                                           			 ");//change transparency
		sb.append("   <fill>1</fill>                                           					 ");
		sb.append("   <outline>1</outline>                                           			 ");
		sb.append("  </PolyStyle>                                          					 ");
		sb.append("  </Style>                                          						 ");
		//------------------------SCNW
		sb.append(" <Style id=\"lightbluecolor\">                                             	 ");
		sb.append(" <LineStyle>                                             					 ");
		sb.append("  <color>fffff600</color>                                            		 ");
		sb.append("  <width>1</width>                                            				 ");
		sb.append(" </LineStyle>                                             					 ");
		sb.append(" <PolyStyle>                                             					 ");
		sb.append("  <color>69fff600</color>                                           			 ");//change transparency
		sb.append("   <fill>1</fill>                                           					 ");
		sb.append("   <outline>1</outline>                                           			 ");
		sb.append("  </PolyStyle>                                          					 ");
		sb.append("  </Style>                                          						 ");
		//------------------------purple
		sb.append(" <Style id=\"purplecolor\">                                             	 ");
		sb.append(" <LineStyle>                                             					 ");
		sb.append("  <color>ff800080</color>                                            		 ");
		sb.append("  <width>1</width>                                            				 ");
		sb.append(" </LineStyle>                                             					 ");
		sb.append(" <PolyStyle>                                             					 ");
		sb.append("  <color>59800080</color>                                           			 ");//change transparency
		sb.append("   <fill>1</fill>                                           					 ");
		sb.append("   <outline>1</outline>                                           			 ");
		sb.append("  </PolyStyle>                                          					 	 ");
		sb.append("  </Style>   																 ");
		//------------------------SDP-R
		sb.append(" <Style id=\"sdprcolor\">                                             	 ");
		sb.append(" <LineStyle>                                             					 ");
		sb.append("  <color>ffe700fc</color>                                            		 ");
		sb.append("  <width>1</width>                                            				 ");
		sb.append(" </LineStyle>                                             					 ");
		sb.append(" <PolyStyle>                                             					 ");
		sb.append("  <color>59e700fc</color>                                           			 ");//change transparency
		sb.append("   <fill>1</fill>                                           					 ");
		sb.append("   <outline>1</outline>                                           			 ");
		sb.append("  </PolyStyle>                                          					 	 ");
		sb.append("  </Style>   																 ");
		
		sb.append(" <Style id=\"browncolor\">                                             	 ");
		sb.append(" <LineStyle>                                             					 ");
		sb.append("  <color>640078B4</color>                                            		 ");
		sb.append("  <width>1</width>                                            				 ");
		sb.append(" </LineStyle>                                             					 ");
		sb.append(" <PolyStyle>                                             					 ");
		sb.append("  <color>640078B4</color>                                           			 ");//change transparency
		sb.append("   <fill>1</fill>                                           					 ");
		sb.append("   <outline>1</outline>                                           			 ");
		sb.append("  </PolyStyle>                                          					 	 ");
		sb.append("  </Style>   																 ");
		
		
		
	}
	private void kioskViewAction(HttpServletRequest req,StringBuilder sb,int polygonCount){
		boolean apiAllEnable = Boolean.parseBoolean(req.getParameter("ALL_ACT_API"));
		boolean bipAllEnable = Boolean.parseBoolean(req.getParameter("ALL_ACT_BIP"));
		boolean cbpAllEnable = Boolean.parseBoolean(req.getParameter("ALL_ACT_CBP"));
		boolean sdprAllEnable = Boolean.parseBoolean(req.getParameter("ALL_ACT_SDPR"));
		boolean sdpcAllEnable = Boolean.parseBoolean(req.getParameter("ALL_ACT_SDPC"));
		if(sdprAllEnable){
			List<String> kml = (List<String>) EventCache.getInstance().getSdprKmls().getValue().get("KML");
			if(kml!=null&&(!kml.isEmpty())) {
				sb.append(getMapContent("SDP-R","sdprcolor","SDP-R",kml,polygonCount));
			}
		}
		if(sdpcAllEnable){
			List<String> kml =(List<String>) EventCache.getInstance().getSdpcKmls().getValue().get("KML");
			if(kml!=null&&(!kml.isEmpty())) {
				sb.append(getMapContent("SDP-C","violet colour","SDP-C",kml,polygonCount));
				}
			}
		
		if(apiAllEnable){
			List<String> kml = (List<String>) EventCache.getInstance().getApiKmls().getValue().get("KML");
			if(kml!=null&&(!kml.isEmpty())) {
				sb.append(getMapContent("API","redcolour","API",kml,polygonCount));
			}
		}
		if(bipAllEnable){
			List<String> kml = (List<String>) EventCache.getInstance().getBipKmls().getValue().get("KML");
			if(kml!=null&&(!kml.isEmpty())) {
				sb.append(getMapContent("BIP","blackcolour","BIP",kml,polygonCount));
			}
		}
		if(cbpAllEnable){
			List<String> kml = (List<String>) EventCache.getInstance().getCbpKmls().getValue().get("KML");
			if(kml!=null&&(!kml.isEmpty())) {
				sb.append(getMapContent("CBP","browncolor","CBP",kml,polygonCount));
			}
		}
	}
	
	private void blockAction(HttpServletRequest req,StringBuilder sb,int polygonCount){
		//a---SLAP level
		boolean scecEnable = Boolean.parseBoolean(req.getParameter(DRWConstants.SCEC));
		boolean scenEnable = Boolean.parseBoolean(req.getParameter(DRWConstants.SCEN));
		boolean scnwEnable = Boolean.parseBoolean(req.getParameter(DRWConstants.SCNW));
		boolean scewEnable = Boolean.parseBoolean(req.getParameter(DRWConstants.SCEW));
		boolean schdEnable = Boolean.parseBoolean(req.getParameter(DRWConstants.SCHD));
		boolean scldEnable = Boolean.parseBoolean(req.getParameter(DRWConstants.SCLD));
		//do slap thing
		
		if(scecEnable){
			List<Number> blocks = EventCache.getInstance().getSlapBlockMap().get(DRWConstants.SCEC);
			for(Number block:blocks){
				String blockNumber = String.valueOf(block);
				List<String> kml = EventCache.getInstance().getBlockKML(false, null, blockNumber);
				sb.append(getMapContent(DRWConstants.BLOCK+"_"+blockNumber,"redcolor",DRWConstants.BLOCK+"_"+blockNumber,kml,polygonCount));
			}
		}if(schdEnable){
			List<Number> blocks = EventCache.getInstance().getSlapBlockMap().get(DRWConstants.SCHD);
			for(Number block:blocks){
				String blockNumber = String.valueOf(block);
				List<String> kml = EventCache.getInstance().getBlockKML(false, null, blockNumber);
				sb.append(getMapContent(DRWConstants.BLOCK+"_"+blockNumber,"schdcolor",DRWConstants.BLOCK+"_"+blockNumber,kml,polygonCount));
			}
		}if(scldEnable){
			List<Number> blocks = EventCache.getInstance().getSlapBlockMap().get(DRWConstants.SCLD);
			for(Number block:blocks){
				String blockNumber = String.valueOf(block);
				List<String> kml = EventCache.getInstance().getBlockKML(false, null, blockNumber);
				sb.append(getMapContent(DRWConstants.BLOCK+"_"+blockNumber,"darkbluecolor",DRWConstants.BLOCK+"_"+blockNumber,kml,polygonCount));
			}
		}if(scenEnable){
			List<Number> blocks = EventCache.getInstance().getSlapBlockMap().get(DRWConstants.SCEN);
			for(Number block:blocks){
				String blockNumber = String.valueOf(block);
				List<String> kml = EventCache.getInstance().getBlockKML(false, null, blockNumber);
				sb.append(getMapContent(DRWConstants.BLOCK+"_"+blockNumber,"scencolor",DRWConstants.BLOCK+"_"+blockNumber,kml,polygonCount));
			}
		}if(scnwEnable){
			List<Number> blocks = EventCache.getInstance().getSlapBlockMap().get(DRWConstants.SCNW);
			for(Number block:blocks){
				String blockNumber = String.valueOf(block);
				List<String> kml = EventCache.getInstance().getBlockKML(false, null, blockNumber);
				sb.append(getMapContent(DRWConstants.BLOCK+"_"+blockNumber,"scnwcolor",DRWConstants.BLOCK+"_"+blockNumber,kml,polygonCount));
			}
		}if(scewEnable){
			List<Number> blocks = EventCache.getInstance().getSlapBlockMap().get(DRWConstants.SCEW);
			for(Number block:blocks){
				String blockNumber = String.valueOf(block);
				List<String> kml = EventCache.getInstance().getBlockKML(false, null, blockNumber);
				sb.append(getMapContent(DRWConstants.BLOCK+"_"+blockNumber,"purplecolor",DRWConstants.BLOCK+"_"+blockNumber,kml,polygonCount));
			}
		}
		//b---ABANK level
		String blockNumber = req.getParameter("blockNumber");
		//do abank thing
		if(blockNumber!=null&&(!blockNumber.equalsIgnoreCase(""))){
			String slapGroup = EventCache.getInstance().getSLAPGroupName(blockNumber);
			List<String> kml = EventCache.getInstance().getBlockKML(false, null, blockNumber);
			if(DRWConstants.SCEC.equalsIgnoreCase(slapGroup)){
				sb.append(getMapContent(DRWConstants.BLOCK+"_"+blockNumber,"redcolor",DRWConstants.BLOCK+"_"+blockNumber,kml,polygonCount));
			}else if(DRWConstants.SCHD.equalsIgnoreCase(slapGroup)){
				sb.append(getMapContent(DRWConstants.BLOCK+"_"+blockNumber,"schdcolor",DRWConstants.BLOCK+"_"+blockNumber,kml,polygonCount));
			}else if(DRWConstants.SCLD.equalsIgnoreCase(slapGroup)){
				sb.append(getMapContent(DRWConstants.BLOCK+"_"+blockNumber,"darkbluecolor",DRWConstants.BLOCK+"_"+blockNumber,kml,polygonCount));
			}else if(DRWConstants.SCEN.equalsIgnoreCase(slapGroup)){
				sb.append(getMapContent(DRWConstants.BLOCK+"_"+blockNumber,"scencolor",DRWConstants.BLOCK+"_"+blockNumber,kml,polygonCount));
			}else if(DRWConstants.SCNW.equalsIgnoreCase(slapGroup)){
				sb.append(getMapContent(DRWConstants.BLOCK+"_"+blockNumber,"scnwcolor",DRWConstants.BLOCK+"_"+blockNumber,kml,polygonCount));
			}else if(DRWConstants.SCEW.equalsIgnoreCase(slapGroup)){
				sb.append(getMapContent(DRWConstants.BLOCK+"_"+blockNumber,"purplecolor",DRWConstants.BLOCK+"_"+blockNumber,kml,polygonCount));
			}
			

		}
	}
	
	private String getMapContent(String name,String color,String tag,List<String> kml,int polygonCount){
		StringBuffer sb = new StringBuffer();
		if(kml!=null&&(!kml.isEmpty())) {
			sb.append("   <Placemark id=\"10\">                                            									");
			sb.append("    <name>"+name+"</name>                                                                            ");
			sb.append("    <tag>"+name+"</tag>                                                                            ");
			sb.append("    <visibility>1</visibility>                                           							");
			sb.append("   <open>0</open>                                            										");
			sb.append("    <styleUrl>#"+color+"</styleUrl>                                          						");
			sb.append("   <Snippet maxLines=\"0\"/>                                            								");
			sb.append("    <description>                                           											");
			sb.append("     <![CDATA[                                          												");
			sb.append("     <br><br><table border=\"1\"padding=\"1\"width=\"97%\"><tr><td>"+tag+":</td><td></td></tr></table>   ");
			sb.append("    ]]>                                           													");
			sb.append("   </description>                                            										");
			sb.append("   <MultiGeometry>                                            										");
			sb.append("[");
			boolean first = true;
			for(String line : kml) {
				if(!first){
					sb.append(",");
				}
				polygonCount++;
				sb.append(line);
				
				first = false;
			}
			sb.append("]");
			sb.append("  </MultiGeometry>                                             										");
			sb.append("   </Placemark>                                            											");
		}
		return sb.toString();
	}
}
