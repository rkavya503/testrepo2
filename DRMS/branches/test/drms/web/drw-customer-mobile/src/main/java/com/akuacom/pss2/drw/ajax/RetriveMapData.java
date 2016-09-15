package com.akuacom.pss2.drw.ajax;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.akuacom.pss2.drw.CFEventManager;
import com.akuacom.pss2.drw.DREvent2013Manager;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.value.BlockValue;


public class RetriveMapData extends HttpServlet {

	private static final long serialVersionUID = -6932468445264321294L;
	private static String header = "<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">";

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		//TODO: need to handle multi events  different name according to event key, need 2 cache for sdp
		boolean blockEnable = Boolean.parseBoolean(req.getParameter("blockFlag"));
		response.setContentType("text/xml");

		int polygonCount = 0;
		StringBuilder sb = new StringBuilder();
		appendHeadAndStyle(sb);
		if(blockEnable){
			blockAction(req,sb,polygonCount);
		}

		
		sb.append("  </Document>                                             													");
		sb.append("  </kml>                                             														");


		response.getWriter().write(polygonCount+"^"+sb.toString());
	}
//	
//	private int generateCbpKML(String eventKey, int polygonCount,
//			StringBuilder sb, Integer index, Map cache) {
//		List<String> cbpKml;
//		cbpKml = (List<String>)cache.get(eventKey);
//		String title = "<Placemark id=\""+index++ +"\">   ";
//		String name ="<name>CBP"+":"+eventKey+"</name>";
//		String tag ="<tag>CBP</tag>";
//		if(cbpKml!=null&&(!cbpKml.isEmpty())) {
//			sb.append(title);
//			sb.append(name);
//			sb.append(tag);
//			sb.append("    <visibility>1</visibility>                                           							");
//			sb.append("   <open>0</open>                                            										");
//			sb.append("    <styleUrl>#browncolor</styleUrl>                                          						");
//			sb.append("   <Snippet maxLines=\"0\"/>                                            								");
//			sb.append("    <description>                                           											");
//			sb.append("     <![CDATA[                                          												");
//			sb.append("     <br><br><table border=\"1\"padding=\"1\"width=\"97%\"><tr><td>CBP:</td><td></td></tr></table>   ");
//			sb.append("    ]]>                                           													");
//			sb.append("   </description>                                            										");
//			sb.append("   <MultiGeometry>                                            										");
//			sb.append("[");
//			boolean first = true;
//			for(String line : cbpKml) {
//				if(!first){
//					sb.append(",");
//				}
//				polygonCount++;
//				sb.append(line);
//				
//				first = false;
//			}
//			sb.append("]");
//			sb.append("  </MultiGeometry>                                             										");
//			sb.append("   </Placemark>                                            											");
//		}
//		return polygonCount;
//	}
//
//
//
//	private int generateBipKML(String eventKey, int polygonCount,
//			StringBuilder sb, Integer index, Map cache) {
//		List<String> bipKMl;
//		bipKMl = (List<String>)cache.get(eventKey);
//		String title = "<Placemark id=\""+index++ +"\">   ";
//		String name ="<name>BIP"+":"+eventKey+"</name>";
//		String tag ="<tag>BIP</tag>";
//		if(bipKMl!=null&&(!bipKMl.isEmpty())) {
//			sb.append(title);
//			sb.append(name);
//			sb.append(tag);
//			sb.append("    <visibility>1</visibility>                                           							");
//			sb.append("   <open>0</open>                                            										");
//			sb.append("    <styleUrl>#blackcolour</styleUrl>                                          						");
//			sb.append("   <Snippet maxLines=\"0\"/>                                            								");
//			sb.append("    <description>                                           											");
//			sb.append("     <![CDATA[                                          												");
//			sb.append("     <br><br><table border=\"1\"padding=\"1\"width=\"97%\"><tr><td>BIP:</td><td></td></tr></table>   ");
//			sb.append("    ]]>                                           													");
//			sb.append("   </description>                                            										");
//			sb.append("   <MultiGeometry>                                            										");
//			sb.append("[");
//			boolean first = true;
//			for(String line : bipKMl) {
//				if(!first){
//					sb.append(",");
//				}
//				polygonCount++;
//				sb.append(line);
//				
//				first = false;
//			}
//			sb.append("]");
//			sb.append("  </MultiGeometry>                                             										");
//			sb.append("   </Placemark>                                            											");
//		}
//		return polygonCount;
//	}
//
//
//
//	private int generateKML4Api(String eventKey, int polygonCount,
//			StringBuilder sb, Integer index, Map cache) {
//		List<String> apiKMl;
//		apiKMl = (List<String>)cache.get(eventKey);
//		String title = "<Placemark id=\""+index++ +"\">   ";
//		String name ="<name>API"+":"+eventKey+"</name>";
//		String tag ="<tag>API</tag>";
//		if(apiKMl!=null&&(!apiKMl.isEmpty())) {
//			sb.append(title);
//			sb.append(name);
//			sb.append(tag);
//			sb.append("    <visibility>1</visibility>                                           							");
//			sb.append("   <open>0</open>                                            										");
//			sb.append("    <styleUrl>#redcolour</styleUrl>                                          						");
//			sb.append("   <Snippet maxLines=\"0\"/>                                            								");
//			sb.append("    <description>                                           											");
//			sb.append("     <![CDATA[                                          												");
//			sb.append("     <br><br><table border=\"1\"padding=\"1\"width=\"97%\"><tr><td>API:</td><td></td></tr></table>   ");
//			sb.append("    ]]>                                           													");
//			sb.append("   </description>                                            										");
//			sb.append("   <MultiGeometry>                                            										");
//			sb.append("[");
//			boolean first = true;
//			for(String line : apiKMl) {
//				if(!first){
//					sb.append(",");
//				}
//				polygonCount++;
//				sb.append(line);
//				
//				first = false;
//			}
//			sb.append("]");
//			sb.append("  </MultiGeometry>                                             										");
//			sb.append("   </Placemark>                                            											");
//		}
//		return polygonCount;
//	}
//
//
//
//	private int generateKML4Sdpc(String eventKey, int polygonCount,
//			StringBuilder sb, Integer index, Map cache) {
//		List<String> sdpcKMl;
//		sdpcKMl = (List<String>) cache.get(eventKey);
//		String title = "<Placemark id=\""+index++ +"\">   ";
//		String name ="<name>SDP-C"+":"+eventKey+"</name>";
//		String tag ="<tag>SDP-C</tag>";
//		if(sdpcKMl!=null&&(!sdpcKMl.isEmpty())) {
//			sb.append(title);
//			sb.append(name);
//			sb.append(tag);
//			sb.append("    <visibility>1</visibility>                                          								");
//			sb.append("    <open>0</open>                                           										");
//			sb.append("    <styleUrl>#violet colour</styleUrl>                                           						");
//			sb.append("    <Snippet maxLines=\"0\"/>                                           								");
//			sb.append("    <description>                                           											");
//			sb.append("    <![CDATA[                                           												");
//			sb.append("    <br><br><table border=\"1\"padding=\"1\"width=\"97%\"><tr><td>SDP-C:</td><td></td></tr></table>  ");
//			sb.append("    ]]>                                           													");
//			sb.append("   </description>                                            										");
//			sb.append("   <MultiGeometry>                                            										");
//			sb.append("[");
//			boolean first = true;
//			for(String line : sdpcKMl) {
//				if(!first){
//					sb.append(",");
//				}
//				polygonCount++;
//				sb.append(line);
//				
//				first = false;
//			}
//			sb.append("]");
//			sb.append("  </MultiGeometry>                                             										");
//			sb.append("   </Placemark>                                            											");
//			}
//		return polygonCount;
//	}
//
//
//
//	private int generateKML4Sdpr(String eventKey, int polygonCount,
//			StringBuilder sb, Integer index,Map cache) {
//		List<String> sdprKMl;
//		sdprKMl = (List<String>) cache.get(eventKey);
//		String title = "<Placemark id=\""+index++ +"\">   ";
//		String name ="<name>SDP-R"+":"+eventKey+"</name>";
//		String tag ="<tag>SDP-R</tag>";
//		if(sdprKMl!=null&&(!sdprKMl.isEmpty())) {
//			sb.append(title);
//			sb.append(name);
//			sb.append(tag);
//			sb.append("   <Snippet maxLines=\"0\"/>                                            								");
//			sb.append("   <description>                                            											");
//			sb.append("  <![CDATA[                                             												");
//			sb.append("  <br><br><table border=\"1\"padding=\"1\"width=\"97%\"><tr><td>SDP-R:</td><td></td></tr></table>  	");
//			sb.append("  ]]>                                             													");
//			sb.append("  </description>	                                             										");
//			sb.append("  <visibility>1</visibility>                                            								");
//			sb.append("  <open>0</open>                                             										");
//			sb.append("  <styleUrl>#sdprcolor</styleUrl>                                                    				");
//			sb.append("   <MultiGeometry>                                            										");
//			sb.append("[");
//			boolean first = true;
//			for(String line : sdprKMl) {
//				if(!first){
//					sb.append(",");
//				}
//				polygonCount++;
//				sb.append(line);
//				
//				first = false;
//			}
//			sb.append("]");
//			sb.append("  </MultiGeometry>                                             										");
//			sb.append("  </Placemark>                                            											");
//		}
//		
//		return polygonCount;
//	}



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
//	private void kioskViewAction(HttpServletRequest req,StringBuilder sb,int polygonCount){
//		boolean apiAllEnable = Boolean.parseBoolean(req.getParameter("ALL_ACT_API"));
//		boolean bipAllEnable = Boolean.parseBoolean(req.getParameter("ALL_ACT_BIP"));
//		boolean cbpAllEnable = Boolean.parseBoolean(req.getParameter("ALL_ACT_CBP"));
//		boolean sdprAllEnable = Boolean.parseBoolean(req.getParameter("ALL_ACT_SDPR"));
//		boolean sdpcAllEnable = Boolean.parseBoolean(req.getParameter("ALL_ACT_SDPC"));
//		if(sdprAllEnable){
//			List<String> kml = (List<String>) EventCache.getInstance().getSdprKmls().getValue().get("KML");
//			if(kml!=null&&(!kml.isEmpty())) {
//				sb.append(getMapContent("SDP-R","sdprcolor","SDP-R",kml,polygonCount));
//			}
//		}
//		if(sdpcAllEnable){
//			List<String> kml =(List<String>) EventCache.getInstance().getSdpcKmls().getValue().get("KML");
//			if(kml!=null&&(!kml.isEmpty())) {
//				sb.append(getMapContent("SDP-C","violet colour","SDP-C",kml,polygonCount));
//				}
//			}
//		
//		if(apiAllEnable){
//			List<String> kml = (List<String>) EventCache.getInstance().getApiKmls().getValue().get("KML");
//			if(kml!=null&&(!kml.isEmpty())) {
//				sb.append(getMapContent("API","redcolour","API",kml,polygonCount));
//			}
//		}
//		if(bipAllEnable){
//			List<String> kml = (List<String>) EventCache.getInstance().getBipKmls().getValue().get("KML");
//			if(kml!=null&&(!kml.isEmpty())) {
//				sb.append(getMapContent("BIP","blackcolour","BIP",kml,polygonCount));
//			}
//		}
//		if(cbpAllEnable){
//			List<String> kml = (List<String>) EventCache.getInstance().getCbpKmls().getValue().get("KML");
//			if(kml!=null&&(!kml.isEmpty())) {
//				sb.append(getMapContent("CBP","browncolor","CBP",kml,polygonCount));
//			}
//		}
//	}
	private List<String> getKMLForBlock(List<BlockValue> blockKMLs,String number){
		
		List<String> kml = new ArrayList<String>();
		
		for(BlockValue block:blockKMLs){
			String blockS =  block.getBlockNumber();
			if(blockS!=null&&blockS.equalsIgnoreCase(number)){
				kml.addAll(block.getKmls());	
			}
		}
		
		return kml;
	}
	
	private void blockAction(HttpServletRequest req,StringBuilder sb,int polygonCount){
		CFEventManager manager=DRWUtil.getCFEventManager();
		
		List<BlockValue> blockKMLs = new ArrayList<BlockValue>();
		DREvent2013Manager manager2=DRWUtil.getDREvent2013Manager();
		blockKMLs = manager2.getKMLS(manager.getAllBlock());//FIXME: performance risk

		//b---ABANK level
		String blockNumber = req.getParameter("blockNumber");
		Boolean multi = Boolean.valueOf(req.getParameter("multi"));
		//do abank thing
		String color = "";
		if(multi){
			String[] blocks = blockNumber.split(",");
			
			for(String block:blocks){
				if(color.length()==0){
					String slapGroup = manager2.getSlap4Block(block);
					//init color
					if("SCEC".equalsIgnoreCase(slapGroup)){
						color="redcolor";
					}else if("SCHD".equalsIgnoreCase(slapGroup)){
						color="schdcolor";
					}else if("SCLD".equalsIgnoreCase(slapGroup)){
						color="darkbluecolor";
					}else if("SCEN".equalsIgnoreCase(slapGroup)){
						color="scencolor";
					}else if("SCNW".equalsIgnoreCase(slapGroup)){
						color="scnwcolor";
					}else if("SCEW".equalsIgnoreCase(slapGroup)){
						color="purplecolor";
					}
				}
				List<String> kml = getKMLForBlock(blockKMLs,block);
				sb.append(getMapContent("BLOCK_"+block,color,"BLOCK_"+block,kml,polygonCount));
			}
			
		}else{
			if(blockNumber!=null&&(!blockNumber.equalsIgnoreCase(""))){
				String slapGroup = manager2.getSlap4Block(blockNumber);
				List<String> kml = new ArrayList<String>();
				
				for(BlockValue block:blockKMLs){
					String blockS =  block.getBlockNumber();
					if(blockS!=null&&blockS.equalsIgnoreCase(req.getParameter("blockNumber"))){
						kml.addAll(block.getKmls());	
					}
				}
				if("SCEC".equalsIgnoreCase(slapGroup)){
					sb.append(getMapContent("BLOCK_"+blockNumber,"redcolor","BLOCK_"+blockNumber,kml,polygonCount));
				}else if("SCHD".equalsIgnoreCase(slapGroup)){
					sb.append(getMapContent("BLOCK_"+blockNumber,"schdcolor","BLOCK_"+blockNumber,kml,polygonCount));
				}else if("SCLD".equalsIgnoreCase(slapGroup)){
					sb.append(getMapContent("BLOCK_"+blockNumber,"darkbluecolor","BLOCK_"+blockNumber,kml,polygonCount));
				}else if("SCEN".equalsIgnoreCase(slapGroup)){
					sb.append(getMapContent("BLOCK_"+blockNumber,"scencolor","BLOCK_"+blockNumber,kml,polygonCount));
				}else if("SCNW".equalsIgnoreCase(slapGroup)){
					sb.append(getMapContent("BLOCK_"+blockNumber,"scnwcolor","BLOCK_"+blockNumber,kml,polygonCount));
				}else if("SCEW".equalsIgnoreCase(slapGroup)){
					sb.append(getMapContent("BLOCK_"+blockNumber,"purplecolor","BLOCK_"+blockNumber,kml,polygonCount));
				}
				

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
