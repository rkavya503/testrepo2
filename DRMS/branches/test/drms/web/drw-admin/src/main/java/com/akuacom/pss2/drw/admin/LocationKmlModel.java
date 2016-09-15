package com.akuacom.pss2.drw.admin;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.akuacom.jsf.model.AbstractTreeContentProvider;
import com.akuacom.pss2.drw.LocationKmlManager;
import com.akuacom.pss2.drw.value.LocationKmlStatus;

public class LocationKmlModel extends AbstractTreeContentProvider<LocationKmlStatus>{

	private static final Logger log = Logger.getLogger(LocationKmlModel.class.getName());
	private int totalCount = 0;
	private List<LocationKmlStatus> contents;
	private String participantName = "";
	
	@Override
	public int getTotalRowCount() {
		return totalCount;
	}
	
	@Override
	public List<LocationKmlStatus> getContents() {
		if(contents==null)
			return Collections.emptyList();
		else
			return contents;
	}
	
	@Override
	public void updateModel() {
			try {
				//totalCount= reportManager.getParticipantCount(participantName);
				//if(totalCount!=0){
				clearTreeNodeCache(null);
				contents= getDataManager().getAllLocationKmlStatus();
				totalCount = contents.size();
				//}
			} catch (Exception e) {
				FDUtils.addMsgError("Internal Error!");
			}
		}
	
	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	

	@Override
	public List<LocationKmlStatus> getChildren(LocationKmlStatus parent) {
		return Collections.emptyList();
	}

	@Override
	public boolean hasChildren(LocationKmlStatus parent) {
		return false;
	}
	
	private LocationKmlManager manager;
	
	private LocationKmlManager getDataManager() {
		if(manager == null){
			manager = ServiceLocator.findHandler(LocationKmlManager.class,
					"dr-pro/LocationKmlManager/remote");
		}
		return manager;
	}
	
	public void exportHtmlTableToExcel() throws IOException {
		try{
			 String filename = "GeographicConfiguration.csv";
		     FacesContext fc = FacesContext.getCurrentInstance();
		     HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
		     response.reset();
		     response.addHeader("cache-control", "must-revalidate");
		     response.setContentType("application/octet_stream");
		     response.setCharacterEncoding("utf-8");
		     response.setHeader("Content-Disposition", "attachment; filename=\""
		                    + filename + "\"");
		     
		     String table = getExportContents();
		     response.getWriter().print(table);
		     fc.responseComplete();
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
	}
	
	public String getExportContents(){
		StringBuffer buffer = new StringBuffer("Dispatch Type,Location Name,Location#,KML File Available,Size,Load Time");

		List<LocationKmlStatus> summary = getDataManager().getAllLocationKmlStatus();
		for (LocationKmlStatus location : summary) {
			buffer.append("\n");
			
			buffer.append("\""+location.getType()+"\""+",");
			buffer.append("\""+location.getName()+"\""+",");
			buffer.append("\""+location.getNumber()+"\""+",");
			buffer.append("\""+location.getKmlAvailable()+"\""+",");
			buffer.append("\""+location.getStrSize()+"\""+",");
			buffer.append("\""+location.getStrCreationTime()+"\""+",");
		}
		return buffer.toString();
	}

}
