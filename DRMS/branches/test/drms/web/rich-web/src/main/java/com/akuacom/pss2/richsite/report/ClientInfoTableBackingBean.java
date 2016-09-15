package com.akuacom.pss2.richsite.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.akuacom.pss2.client.ClientEAO;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.report.ReportManager;
import com.akuacom.pss2.report.entities.ClientInfo;
import com.akuacom.pss2.richsite.util.AkuacomJSFUtil;



public class ClientInfoTableBackingBean {
	/** The serialVersionUID */
	private static final long serialVersionUID = 3831750127716274233L;
	/** The log */
	private static final Logger log = Logger.getLogger(ClientInfoTableBackingBean.class);	
	/** The retrieve data results */
	private List<ClientInfoBackingBean> clientInfoList = new ArrayList<ClientInfoBackingBean>();
	private List<ClientInfo> clientInfos =new ArrayList<ClientInfo>(); 
	/** The ClientEAO instance */
	private ClientEAO clientEAO;
	private ReportManager report;
	/**
	 * Constructor
	 */
	public ClientInfoTableBackingBean(){
		init();
	}
	/**
	 * Private function for retrieve data from system
	 */
	private void init(){
		log.debug("Initialize the ClientInfoTableBackingBean...");
		clientInfoList = new ArrayList<ClientInfoBackingBean>();
//		List<Participant> clients = getClientEAO().findClientsInfoByClient(true);
//		for(Participant client:clients){
//			ClientInfoBackingBean clientInfoBackingBean = new ClientInfoBackingBean(client);
//			clientInfoList.add(clientInfoBackingBean);
//		}
		retrieveData();
		log.debug("Initialize the ClientInfoTableBackingBean complete.");
	}
	private void retrieveData(){
		List<ClientInfo> list = getReport().getClientInfoResults();
		this.setClientInfos(list);
	}
	/**
	 * Function for export clients info to the excel.
	 * @throws IOException
	 */
    public void exportToExcel() throws IOException{
   	 String filename = "clientInfo.csv";
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
        response.reset();
        response.addHeader("cache-control", "must-revalidate");
        response.setContentType("application/octet_stream");
        response.setHeader("Content-Disposition", "attachment; filename=\""+ filename + "\"");
        String result =generateContent();
        response.getWriter().print(result);
        fc.responseComplete();
    }
    /**
     * Private function for generate the excel file content.
     * @return
     */
    private String generateContent(){
    	String result ="";
    	StringBuffer sb = new StringBuffer("Client,Participant,Account Number,Parent,Premise Number,Start Date,End Date,Profile,Configured Programs - Enabled,Configured Programs - Not Enabled,Event Status,Event Mode,Comm Status,Last Contact,Client Type,Device Type,Contacts,Participant Type,Opt Out,Lead Accounts,ABank,\n");
 
    	for(ClientInfo client : getClientInfos()){
    				
//    		sb.append(client.getClient()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getClient()) ;
//    		sb.append(client.getParticipant()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getParticipant()) ;
//    		sb.append(client.getAccountNumber()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getAccountNumber()) ;
//    		sb.append(client.getParent()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getParent()) ;
//    		sb.append(client.getPremiseNumber()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getPremiseNumber()) ;
    		sb.append(client.getStartDate()+",");
    		sb.append(client.getEndDate()+",");
//    		sb.append(client.getProfile()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getProfile()) ;
//    		sb.append(client.getActiveClientPrograms()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getActiveClientPrograms()) ;
//    		sb.append(client.getInactiveClientPrograms()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getInactiveClientPrograms()) ;
//    		sb.append(client.getEventStatus()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getEventStatus()) ;
//    		sb.append(client.getMode()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getMode()) ;
//    		sb.append(client.getCommStatus()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getCommStatus()) ;
//    		sb.append(client.getLastContact()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getLastContact()) ;
//    		sb.append(client.getClientType()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getClientType()) ;
//    		sb.append(client.getDeviceType()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getDeviceType()) ;
//    		sb.append(client.getContacts()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getContacts()) ;
    		String participantType = client.getParticipantType();
    		participantType = participantType.replaceAll(",", ";");
//    		sb.append(participantType+",");
    		AkuacomJSFUtil.writeNext(sb, participantType) ;
//    		sb.append(client.getEventOptOutDisplay()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getEventOptOutDisplay()) ;
//    		sb.append(client.getLeadAccountDisplay()+",");
    		AkuacomJSFUtil.writeNext(sb, client.getLeadAccountDisplay()) ;
    		String abank = client.getABank();
    		if(abank==null){abank="";}
//    		sb.append(abank+",");
    		AkuacomJSFUtil.writeNext(sb, abank) ;
    		sb.append("\n");
    	}
    	
    	
    	result = sb.toString();
    	return result;
    }
    
  //------------------------------------------------Setters and Getters---------------------------------------------------
	public List<ClientInfoBackingBean> getClientInfoList() {
		return clientInfoList;
	}

	public void setClientInfoList(List<ClientInfoBackingBean> clientInfoList) {
		this.clientInfoList = clientInfoList;
	}

	public ClientEAO getClientEAO() {
		if(clientEAO==null){
			clientEAO = EJBFactory.getBean(ClientEAO.class);
		}
		return clientEAO;
	}

	public void setClientEAO(ClientEAO clientEAO) {
		this.clientEAO = clientEAO;
	}
	public ReportManager getReport() {
		if(report==null){
			report = EJBFactory.getBean(ReportManager.class);	
		}
		return report;
	}
	public void setReport(ReportManager report) {
		this.report = report;
	}
	public List<ClientInfo> getClientInfos() {
		return clientInfos;
	}
	public void setClientInfos(List<ClientInfo> clientInfos) {
		this.clientInfos = clientInfos;
	}
	
	
}
