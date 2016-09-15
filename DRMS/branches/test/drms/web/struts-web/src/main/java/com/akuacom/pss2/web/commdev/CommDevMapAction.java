/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.commdev.CommDevMapAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.commdev;

import java.sql.SQLException;
import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.ejb.search.AkuaCursor;
import com.akuacom.ejb.search.SearchCriterion;
import com.akuacom.ejb.search.SearchHandler;
import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.query.ClientSearchCriteria;
import com.akuacom.pss2.query.ClientSummary;
import com.akuacom.pss2.query.NativeQueryManager;
import com.akuacom.pss2.report.ReportManager;
import com.akuacom.pss2.report.entities.Account;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.web.util.DisplayTagUtil;


/**
 * The Class CommDevMapAction.
 */
@SuppressWarnings({"UnusedDeclaration"})
public class CommDevMapAction extends DispatchAction {
    // Logger
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(CommDevMapAction.class);

    private static final String MANUAL_POSTFIX = "(MAN)";

    /**
     * default behavior: list comm devices.
     * 
     * @param mapping the mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     * 
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    protected ActionForward unspecified(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return list(mapping, actionForm, request, response);
    }

    /**
     * List.
     * 
     * @param mapping the mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     * 
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    public ActionForward list(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommDevListForm form = (CommDevListForm)actionForm;
        if (DisplayTagUtil.isExport(request)) {
            return mapping.findForward("export");
        } else {
        	String uid = (String) request.getSession().getAttribute("uid");
            ActionForward partForward = new ActionForward ("../pss2.utility/clients.jsf?uid=" + uid, true);
            return partForward;
            //return mapping.findForward("success");
        }
    }

    /**
     * Delete.
     * 
     * @param mapping the mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     * 
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        CommDevListForm form = (CommDevListForm)actionForm;
        String[] usernames = form.getUsernames();
        if (usernames != null) {
        	ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);

            for (String pName : usernames) {
            	participantManager.removeParticipant(pName);
            }
        }

        return mapping.findForward("deleted");
    }

     /**
     * ExportClient
     *
     * @param mapping the mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     *
     * @return the action forward
     *
     * @throws Exception the exception
     */
    public ActionForward exportClient(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {

       CommDevListForm form = (CommDevListForm)actionForm;
       String table = CommDevMapAction.getClientListForExport(request);

        String filename = "clientList.csv";
        response.reset();
        response.addHeader("cache-control", "must-revalidate");
        response.setContentType("application/octet_stream");
        response.setHeader("Content-Disposition", "attachment; filename=\""
                    + filename + "\"");
        response.getWriter().print(table);
        return null;

    }

    /**
     * ExportParticipant
     *
     * @param mapping the mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     *
     * @return the action forward
     *
     * @throws Exception the exception
     */
    public ActionForward exportParticipant(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        CommDevListForm form = (CommDevListForm)actionForm;

        List<CommDevVO> commDevList = CommDevMapAction.getParticipantListForExport(request);
        String table = CommDevMapAction.convertToTable(commDevList);

        response.reset();
        response.addHeader("cache-control", "must-revalidate");

        String filename = "participantList.csv";
        response.setContentType("application/octet_stream");
        response.setHeader("Content-Disposition", "attachment; filename=\""
                    + filename + "\"");
         response.getWriter().print(table);
        //response.getWriter().write(table);
        //response.getWriter().flush();
         //response.getWriter().close();

         return null;
    }
    public ActionForward exportAccountInfo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ReportManager report = EJBFactory.getBean(ReportManager.class);
        final List<Account> list = report.getAccounts();
        
        String table = CommDevMapAction.convertToAccountInfoTable(list);

        response.reset();
        response.addHeader("cache-control", "must-revalidate");

        String filename = "accounts.csv";
        response.setContentType("application/octet_stream");
        response.setHeader("Content-Disposition", "attachment; filename=\""+ filename + "\"");
        response.getWriter().print(table);

        return null;
    }
    public static String convertToAccountInfoTable(List<Account> list){
        StringBuffer sb = new StringBuffer();
        sb.append("Participant,Account Number,Parent,Premise Number,Start Date,End Date,Programs,Active\n");

        for (Account account : list){
            sb.append(account.getParticipantName()+",");
            sb.append(account.getAccountNumber()+",");
            sb.append(account.getSubAccount()+",");
            if(account.getPremiseNumber()==null){
            	sb.append(",");	
            }else{
            	sb.append(account.getPremiseNumber()+",");
            }
            if(account.getStartDate()==null){
            	sb.append(",");	
            }else{
            	sb.append(account.getStartDate()+",");
            }
            if(account.getEndDate()==null){
            	sb.append(",");	
            }else{
            	sb.append(account.getEndDate()+",");
            }
            if(account.getProgramNames()==null){
            	sb.append(",");	
            }else{
            	sb.append(account.getProgramNames()+",");
            }
            if(account.getActive()==null){
            	sb.append(",");	
            }else{
            	sb.append(account.getActive()+",");
            }
            sb.append("\n");
        }
         return sb.toString();

    }
    
    public ActionForward exportEvents(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        EventManager em = (EventManager) EJBFactory.getBean(EventManager.class);

        Collection<Event> events = em.findAll();
		

        
        
        StringBuffer sb = new StringBuffer("Name,Program Name,Notification Time,Start Time,End Time,Status\n");
        

        
        
        for (Event event : events) {
        	sb.append("\"");
        	sb.append(event.getEventName());
        	sb.append("\",\"");
        	sb.append(event.getProgramName());
        	sb.append("\",\"");
            sb.append(event.getIssuedTime()==null ? "" : new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(event.getIssuedTime()));
        	sb.append("\",\"");
            sb.append(event.getStartTime()==null ? "" : new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(event.getStartTime()));
        	sb.append("\",\"");
            sb.append(event.getEndTime()==null ? "" : new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(event.getEndTime()));
        	sb.append("\",\"");
//        	sb.append(event.getEventStatus());
        	//DRMS-6317 Events tab: the event status in export file is different from the DRAS system.
        	sb.append(em.getEventStatusString(event));
        	sb.append("\"\n");
        }
        
           
        response.reset();
        response.addHeader("cache-control", "must-revalidate");
        String filename = "Events.csv";
        response.setContentType("application/octet_stream");
        response.setHeader("Content-Disposition", "attachment; filename=\""
                   + filename + "\"");
        response.getWriter().print(sb.toString());

return null;
}
    
    
    
    
    
    
        public ActionForward exportProgram(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

                 ProgramManager p = (ProgramManager) EJBFactory.getBean(ProgramManager.class);
                 List<String> programList =  p.getPrograms();

                    String str = "";
                    str+="Program Name\n";
                    for(String pp: programList){
                        str += pp.toString()+",";
                        str += '\n';
                    }
                    
                 response.reset();
                 response.addHeader("cache-control", "must-revalidate");
                 String filename = "Programs.csv";
                 response.setContentType("application/octet_stream");
                 response.setHeader("Content-Disposition", "attachment; filename=\""
                            + filename + "\"");
                 response.getWriter().print(str);

         return null;
    }

    /**
     * Convert to table.
     * 
     * @param commDevList the comm dev list
     * 
     * @return the string
     */
    public static String convertToTable(List<CommDevVO> commDevList)
    {
        String str = "";

        str+="Name,Account#,Programs,Type,Participant Notes\n";

        for (CommDevVO part : commDevList)
        {
            str += part.getUser()+","+
                part.getAccountNumber()+","+
                "\""+part.getPrograms()+"\","+
                part.getTypeString()+","+StringEscapeUtils.unescapeHtml(part.getNotes())+",";
            str += '\n';
        }
         return str;

    }

     /**
     * Gets the participant list for export.
     *
     * @param request the request
     *
     * @return the comm dev list for export
     */
    public static List<CommDevVO> getParticipantListForExport(HttpServletRequest request)
    {
        List<CommDevVO> ret = new ArrayList<CommDevVO>();

        ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);
        if(request.isUserInRole(DrasRole.Admin.toString()) || (request.isUserInRole(DrasRole.Operator.toString())))
                // || request.isUserInRole(DrasRole.UtilityOperator.toString()))
        {
            List<String> names = new ArrayList<String>();
            SearchCriterion sc = new SearchCriterion("p.client", "=", false);
            SearchHandler sh = new SearchHandler();
            List<SearchCriterion> cris = new ArrayList<SearchCriterion>();
            cris.add(sc);
            sh.setCriteria(cris);
            AkuaCursor drasCursor = null;
            sh.setCursor(drasCursor); 
            sh = participantManager.searchParticipants(names, sh);
            List<Participant> partList = (List<Participant>) sh.getResults();
            ret = mapToCommDevVO(partList);
        }
        return ret;
    }

     /**
     * Gets the client list for export.
     *
     * @param request the request
     *
     * @return the comm dev list for export
     */
    public static String getClientListForExport(HttpServletRequest request)
    {
    	String ret = "";
        ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);
        if(request.isUserInRole(DrasRole.Admin.toString()) || (request.isUserInRole(DrasRole.Operator.toString())))
                // || request.isUserInRole(DrasRole.UtilityOperator.toString()))
        {
            /*
            List<String> names = new ArrayList<String>();
            SearchCriterion sc = new SearchCriterion("p.client", "=", new Boolean(true));
            SearchHandler sh = new SearchHandler();
            List cris = new ArrayList();
            cris.add(sc);
            sh.setCriteria(cris);
            AkuaCursor drasCursor = null;
            sh.setCursor(drasCursor);
            sh = participantManager.searchParticipants(names, sh);

            */
           // List<Participant> partList = (List<Participant>) sh.getResults();
            List<Participant> partList =  participantManager.getAllClients();
            ret = mapToCSVForClient(partList);
        }
        return ret;
    }

    /**
     * map participant list to comm dev list for export.
     * 
     * @param partList the request
     * 
     * @return the comm dev list for export
     */
    public static List<CommDevVO> mapToCommDevVO(List<Participant> partList)
    {
        List<CommDevVO> ret = new ArrayList<CommDevVO>();
        ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);
        
            if(partList != null)
            {
                int c =0;
                for(Participant part : partList)
                {
                    if(part!= null && part.getParticipantName() != null && !part.getParticipantName().equals("")){
                    Set<ProgramParticipant> pps = part.getProgramParticipants();
                    String progNames = "";
                    if(pps != null)
                    {
                        int ind = 0;

                        for(ProgramParticipant pp : pps)
                        {
                            if(pp.getState() == ProgramParticipant.PROGRAM_PART_ACTIVE)
                            {
                                if(ind > 0) progNames = progNames + ",";
                                progNames = progNames + pp.getProgramName();
                                ind ++;
                            }
                        }
                    }    
//                    Participant p = participantManager.getParticipant(part.getParticipantName(), part.isClient());
                    Participant p = part;
                    final CommDevVO vo = new CommDevVO();
                    //log.error("---" + c++ + ".."+ p.getParticipantName() );
                    vo.setUser(p.getUser());
                    vo.setAccountNumber(p.getAccountNumber());
                    vo.setPrograms(progNames);
                    vo.setType(p.getType());
                    vo.setFirstName(p.getFirstName());
                    vo.setLastName(p.getLastName());
                    vo.setContacts(p.getContacts());
                    vo.setSignalLevels(""); // signal levels for participants don't make sense
                    String notes=p.getNotes()==null?"":p.getNotes();
                    notes=notes.replaceAll("<p>", "");
                    notes=notes.replaceAll("</p>", "");
                    notes=notes.replaceAll("\\r", " ");
                    notes=notes.replaceAll("\\n", " ");
                    vo.setNotes(notes);
                    ret.add(vo);
                }
            }
        }
        return ret;
    }

    public static String mapToCSVForClient(List<Participant> partList)
    {
    	StringBuilder str = new StringBuilder();
    	str.append("Client,Participant,Type,Programs,Event Status,Mode,Last Contact,Comm Status,Device Type,Contact1,Contact2,Contact3,Contact4");
    	
    	NativeQueryManager nativeQuery = EJBFactory.getBean(NativeQueryManager.class);
    	SearchConstraint sc = new SearchConstraint(0, Integer.MAX_VALUE);
    	ClientSearchCriteria csc = new ClientSearchCriteria();
    	sc.addSortColumn("participantName", SearchConstraint.ORDER.ASC);
    	try {
			List<ClientSummary> summ=nativeQuery.getClientSummary(csc,sc);
            for(ClientSummary part : summ){
            	str.append("\n");
            	str.append(part.getName()).append(",");
            	str.append(part.getParent()).append(",");
            	str.append(part.getTypeString()).append(",");
            	
            	str.append("\"").append(part.getProgramNames()).append("\",");
            	 
                // EventStatus
                //      see JSFParticipant for reference.
                String currentEventStatus = part.getCurrentStatus().name();
                String eventStatus = "NONE";
                if (part.getTypeString().equalsIgnoreCase("MANUAL")) {
                    // show current (server) status for manual clients only.
                    eventStatus = StringUtils.replace(currentEventStatus, "UNKNOWN", "?");
                } else {
                    String lastEventStatus;
                    if (part.getLastContact() == null) {
                        lastEventStatus = "?";  // Never heard from this unit.
                    } else {
                        lastEventStatus = part.getLastStatus().name();
                    }
                    
                    if (part.isManualControl()) {
                        eventStatus = lastEventStatus + MANUAL_POSTFIX;
                    } else if (!lastEventStatus.equals(currentEventStatus)) {
                        eventStatus = lastEventStatus + " [ " + currentEventStatus + " ]";
                    }
                }
                str.append(eventStatus).append(",");

                // Mode
                String currentMode = part.getCurrentMode().name();
                if ("NONE".equals(currentEventStatus) && "UNKNOWN".equals(currentMode)) {
                    currentMode = "NORMAL";
                }

                String mode;
                if (part.getTypeString().equalsIgnoreCase("MANUAL")) {
                    //show current (server) mode for manual clients only.
                    mode = StringUtils.replace(currentMode, "UNKNOWN", "?");
                } else {
                    String lastMode;
                    if (part.getLastContact() == null) {
                        lastMode = "UNKNOWN";
                    } else {
                        lastMode = part.getLastMode().name();
                    }
                    mode = lastMode;
                    if (part.isManualControl()) {
                        mode += MANUAL_POSTFIX;
                    } else if (!lastMode.equals(currentMode)) {
                        mode = lastMode + " [ " + currentMode + " ]";
                    }
                    mode = mode.replaceAll("UNKNOWN", "?");
                }
                str.append(mode).append(",");

                str.append(part.getLastContact()==null ? "" : new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(part.getLastContact())).append(",");
                str.append(part.getCommsStatus().name());
                //DRMS-6121
                String deviceType = part.getDeviceType();
                if(deviceType==null){
                	deviceType="";
                }
                str.append(deviceType).append(",");
                List<String> emailAdd = part.getEmailAddress();
                
                str.append(emailAdd.size()>0?emailAdd.get(0):"").append(",");
                str.append(emailAdd.size()>1?emailAdd.get(1):"").append(",");
                str.append(emailAdd.size()>2?emailAdd.get(2):"").append(",");
                str.append(emailAdd.size()>3?emailAdd.get(3):"");
            }
			
		} catch (SQLException e) {
			return "";
		}
        return str.toString();
    }

	/**
	 * Gets the client list for export.
	 * 
	 * @param request
	 *            the request
	 * 
	 * @return the comm dev list for export
	 */
	public static String getParticipantListMapInfoXML(HttpServletRequest request) {
		StringBuilder str = new StringBuilder();
		ParticipantManager participantManager = EJBFactory
				.getBean(ParticipantManager.class);
		if (request.isUserInRole(DrasRole.Admin.toString())
				|| (request.isUserInRole(DrasRole.Operator.toString()))) {
			List<Participant> partList = participantManager.getAllParticipants();

			str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			str.append("<dataInMap>");
			str.append("<listOfParticipants>");
			for (Participant part : partList) {

				if ((part != null) && (part.getParticipantName() != null)
						&& (!part.getParticipantName().equals("")) && (!part.isClient())) {
					str.append("<participant>");
					str.append("<name>" + CommDevMapAction.forXML(part.getParticipantName()) + "</name>");
					str.append("<shed>" + part.getShedPerHourKW() + "</shed>");
					
/*					
                    if (part.getClientStatus() != null) {
                    	str.append("<status>" + CommDevMapAction.forXML(part.getClientStatus().toString().toLowerCase()) + "</status>");
                    } else {
						str.append("<status>" + "UNKNOWN" + "</status>");						
					}
*/					
					str.append("<status>" + (participantManager.isParticipantOnline(part.getParticipantName()) ? "online" : "offline") + "</status>");
					
					
					str.append("<latitude>" + part.getLatitude() + "</latitude>");
					str.append("<longitude>" + part.getLongitude() + "</longitude>");

					str.append("<programs>");

/*					for (ProgramParticipant pp : part.getProgramParticipants()) {
						if (pp.getState() == ProgramParticipant.PROGRAM_PART_ACTIVE) {
							str.append("<programName>");
							str.append(pp.getProgramName());
							str.append("</programName>");
						}
					}					
*/
					for (String programName : participantManager.getProgramsForParticipant(part.getParticipantName(), part.isClient())) {
						str.append("<programName>");
						str.append(CommDevMapAction.forXML(programName));
						str.append("</programName>");						
					}
					str.append("</programs>");
					str.append("</participant>");
				}
			}
			str.append("</listOfParticipants>");
			str.append("</dataInMap>");
		}
		return str.toString();
	}

	/**
	 * Returns the list of participants in XML with geocoding information.
	 * Useful for map display.
	 * 
	 * @param mapping
	 *            the mapping
	 * @param actionForm
	 *            the action form
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * 
	 * @return the action forward
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public ActionForward participantListMapInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlDoc = CommDevMapAction.getParticipantListMapInfoXML(request);
		response.reset();
		response.addHeader("Cache-Control", "no-cache");
		response.setContentType("text/xml");		
		response.getWriter().print(xmlDoc);
		return null;
	}
	
	public static String forXML(String aText){
	    final StringBuilder result = new StringBuilder();
	    final StringCharacterIterator iterator = new StringCharacterIterator(aText);
	    char character =  iterator.current();
	    while (character != CharacterIterator.DONE ){
	      if (character == '<') {
	        result.append("&lt;");
	      }
	      else if (character == '>') {
	        result.append("&gt;");
	      }
	      else if (character == '\"') {
	        result.append("&quot;");
	      }
	      else if (character == '\'') {
	        result.append("&#039;");
	      }
	      else if (character == '&') {
	         result.append("&amp;");
	      }
	      else {
	        //the char is not a special one
	        //add it to the result as is
	        result.append(character);
	      }
	      character = iterator.next();
	    }
	    return result.toString();
	  }

}