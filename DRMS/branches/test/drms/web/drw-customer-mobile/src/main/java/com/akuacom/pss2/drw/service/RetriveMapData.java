package com.akuacom.pss2.drw.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.akuacom.pss2.drw.EventDAO;
import com.akuacom.pss2.drw.EventDAOImpl;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.util.Md5Util;


public class RetriveMapData extends HttpServlet {
	EventDAO evtDao = new EventDAOImpl();
	private static final long serialVersionUID = -6932468445264321294L;
	private static String header = "<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">";

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		String eventKeyAPI = req.getParameter("eventKeyapi");
		String eventKeyBIP = req.getParameter("eventKeybip");
		String eventKeyCBP = req.getParameter("eventKeycbp");
		String eventKeySDPR = req.getParameter("eventKeysdpr");
		String eventKeySDPC = req.getParameter("eventKeysdpc");
		
		response.setContentType("text/xml");

		int polygonCount = 0;
		Integer index = 0;
		StringBuilder sb = new StringBuilder();
		appendHeadAndStyle(sb);
				
		if(!("".equalsIgnoreCase(eventKeyAPI))){
			Map<String,List<String>> map = getEventKMLMapping(eventKeyAPI);
			Set<String> keySet = map.keySet();
			for(String key:keySet){
				polygonCount = generateKML(key, polygonCount, sb, index,map,"API","redcolour");
			}
		}
		if(!("".equalsIgnoreCase(eventKeyBIP))){
			Map<String,List<String>> map = getEventKMLMapping(eventKeyBIP);
			Set<String> keySet = map.keySet();
			for(String key:keySet){
				polygonCount = generateKML(key, polygonCount, sb, index,map,"BIP","blackcolour");
			}
		}
		if(!("".equalsIgnoreCase(eventKeyCBP))){
			Map<String,List<String>> map = getEventKMLMapping(eventKeyCBP);
			Set<String> keySet = map.keySet();
			for(String key:keySet){
				polygonCount = generateKML(key, polygonCount, sb, index,map,"CBP","browncolor");
			}
		}
		if(!("".equalsIgnoreCase(eventKeySDPR))){
			Map<String,List<String>> map = getEventKMLMapping(eventKeySDPR);
			Set<String> keySet = map.keySet();
			for(String key:keySet){
				polygonCount = generateKML(key, polygonCount, sb, index,map,"SDPR","sdprcolor");
			}
		}
		if(!("".equalsIgnoreCase(eventKeySDPC))){
			Map<String,List<String>> map = getEventKMLMapping(eventKeySDPC);
			Set<String> keySet = map.keySet();
			for(String key:keySet){
				polygonCount = generateKML(key, polygonCount, sb, index,map,"SDPC","violet colour");
			}
		}
		sb.append("  </Document>                                             													");
		sb.append("  </kml>                                             														");


		response.getWriter().write(polygonCount+"^"+sb.toString());
	}

	
	
	
	private Map<String,List<String>> getEventKMLMapping(String eventDetailIds){
		Map<String,List<String>> map = new HashMap<String,List<String>>();
		String[] eventkeys =null;
		if(eventDetailIds!=null){
			eventkeys = eventDetailIds.split(",");
		}else{
			eventkeys = new String[0];
		}
		
		for(String key:eventkeys){
			List<String> keyList = Md5Util.getEventDetailUuids(key);
			String searchInput="";
			
			for(String s:keyList){
				if("".equalsIgnoreCase(searchInput)){
					searchInput+=s;
				}else{
					searchInput+=","+s;
				}
				
			}
			
			
			List<String> kml = DRWUtil.getCFEventManager().getKML4EventDetails(searchInput,true);
			map.put(key, kml);
		}
		return map;
	}
	private int generateKML(String eventKey, int polygonCount,StringBuilder sb, Integer index, Map cache,String tagP,String color) {
		List<String> kml;
		kml = (List<String>)cache.get(eventKey);
		String title = "<Placemark id=\""+index++ +"\">   ";
		String name ="<name>"+tagP+":"+eventKey+"</name>";
		String tag ="<tag>"+tagP+"</tag>";
		if(kml!=null&&(!kml.isEmpty())) {
			sb.append(title);
			sb.append(name);
			sb.append(tag);
			sb.append("    <visibility>1</visibility>                                           							");
			sb.append("   <open>0</open>                                            										");
			sb.append("    <styleUrl>#"+color+"</styleUrl>                                          						");
			sb.append("   <Snippet maxLines=\"0\"/>                                            								");
			sb.append("    <description>                                           											");
			sb.append("     <![CDATA[                                          												");
			sb.append("     <br><br><table border=\"1\"padding=\"1\"width=\"97%\"><tr><td>"+tagP+":</td><td></td></tr></table>   ");
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
}
