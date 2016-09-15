package com.akuacom.pss2.richsite.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.util.CBPUtil;
import com.akuacom.pss2.util.EventState;
import com.akuacom.utils.lang.DateUtil;

public class ClientInfoBackingBean {
	/** The serialVersionUID */
	private static final long serialVersionUID = 3831750127716274236L;
	/** The log */
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ClientInfoBackingBean.class);	
	/** Client: Client ID */
	private String client;	
	/** Participant: Participant ID */
	private String participant;
	/** Account Number: Account number of participant */
	private String accountNumber;
	/** Parent: Parent (commas separated parents if multiple) of participant for programs in the client programs(Active) list */
	private String parent;
	/** Premise Number: Premise Number field of participant */
	private String premiseNumber;
	/** Start Date: Start date from participant */
	private String startDate;
	/** End Date: End Date from Participant */
	private String endDate;
	/** Profile: Active if Start Date entered and no end date or future end date, Inactive if end date < today and Pending if no date entered. */
	private String profile;
	/** Client Program(Active): Programs that are enabled for client */
	private String activeClientPrograms;
	/** Client Program(Inactive) Programs in clients list but not checked */
	private String inactiveClientPrograms;
	/** Event Status: Event Status for Client */
	private String eventStatus;
	/** Mode: Current mode if in an event, NONE otherwise */
	private String mode;
	/** Comm Status: Online or Offline */
	private String commStatus;
	/** Last Contact: Last Contact time for client */
	private String lastContact;
	/** Client Type: Client Type */
	private String clientType;
	/** Device Type: Client Device Type */
	private String deviceType;
	/** Contact1 - First for client contact email addresses. */
	private String contact1;
	/** Contact2 - First for client contact email addresses. */
	private String contact2;
	/** Contact3 - First for client contact email addresses. */
	private String contact3;
	/** Contact4 - First for client contact email addresses. */
	private String contact4;
	/** Combine the contact1,2,3,4*/
	private String contacts;
	/**
	 * Constructor
	 * @param client	Participant Entity
	 */
	public ClientInfoBackingBean(Participant client){
		initParameters(client);
	}
	
	/**
	 * Transfer the participant entity to ClientInfoBackingBean entity
	 * @param client	Participant Entity
	 */
	private void initParameters(Participant client){
		if(client==null){
			return;
		}
		//client
		this.setClient(client.getParticipantName());
		//participant
		this.setParticipant(client.getParent());
		//account number
		this.setAccountNumber(client.getAccountNumber());
		//parent
		this.setParent(client.getParent());
		//premise number
		this.setPremiseNumber(client.getPremiseNumber());
		//start date
		this.setStartDate(DateUtil.format(client.getStartDate()));
		//end date
		this.setEndDate(DateUtil.format(client.getEndDate()));
		//profile
		//Active if Start Date entered and no end date or future end date, 
		//Inactive if end date < today and 
		//Pending if no date entered.
		Date startDate = client.getStartDate();
		Date endDate = client.getEndDate();
		Date today = new Date();
		if(((startDate!=null&&endDate==null))||((startDate!=null&&endDate!=null)&&(!DateUtil.getStartOfDay(endDate).before(DateUtil.getStartOfDay(today))))){
			this.setProfile("Active");
		}else if(endDate!=null){
			if(DateUtil.getStartOfDay(endDate).before(DateUtil.getStartOfDay(today))){
				this.setProfile("Inactive");
			}
		}else if(startDate==null&&endDate==null){
			this.setProfile("Pending");
		}
		//active client programs
		//inactive client programs
		StringBuffer activeProgramsString = new StringBuffer();
		StringBuffer inactiveProgramsString = new StringBuffer();
		Set<ProgramParticipant> clientPrograms=client.getProgramParticipants();
		List<ProgramParticipant> clientProgramList = new ArrayList<ProgramParticipant>(clientPrograms);
		if(isEnableCBPConsolidation()){
			clientProgramList=CBPUtil.transferProgramParticipant(clientProgramList);
		}
		for (ProgramParticipant clientProgram : clientProgramList) {
			if(!clientProgram.getProgramName().equalsIgnoreCase(TestProgram.PROGRAM_NAME)){
				if(clientProgram.getClientConfig()==0){
					inactiveProgramsString.append(clientProgram.getProgramName()+";");
				}else{
					activeProgramsString.append(clientProgram.getProgramName()+";");
				}
			}
		}
		if(activeProgramsString.toString().endsWith(";")){
			activeProgramsString.deleteCharAt(activeProgramsString.lastIndexOf(";"));
		}
		this.setActiveClientPrograms(activeProgramsString.toString());
		if(inactiveProgramsString.toString().endsWith(";")){
			inactiveProgramsString.deleteCharAt(inactiveProgramsString.lastIndexOf(";"));
		}
		this.setInactiveClientPrograms(inactiveProgramsString.toString());
		
		//event status
		 this.setEventStatus(EventState.loadEventStatus(client.getEventStatus()));
		//mode
		 this.setMode(client.getOperationMode().name());
		//common status
		if (client.getClientStatus() != null) {
			this.setCommStatus(client.getClientStatus().toString());
		} else {
			this.setCommStatus("");
		}
		//last contact
		this.setLastContact(DateUtil.format(client.getCommTime()));
		
		//client type
		this.setClientType(client.getTypeString());
		
		//device type
		this.setDeviceType(client.getDeviceType());
		
		//contact 1,2,3,4
		StringBuffer sb = new StringBuffer();
		int i=0;
		for (ParticipantContact clientContact : client.getContacts()) {
			i++;
			if(i==1){
				this.setContact1(clientContact.getAddress());
				if(this.getContact1().equalsIgnoreCase("")){
					
				}else{
					sb.append(this.getContact1()+";");	
				}
				
			}
			if(i==2){
				this.setContact2(clientContact.getAddress());
				if(this.getContact2().equalsIgnoreCase("")){
					
				}else{
					sb.append(this.getContact2()+";");	
				}
			}
			if(i==3){
				this.setContact3(clientContact.getAddress());
				if(this.getContact3().equalsIgnoreCase("")){
					
				}else{
					sb.append(this.getContact3()+";");	
				}
			}
			if(i==4){
				this.setContact4(clientContact.getAddress());
				if(this.getContact4().equalsIgnoreCase("")){
					
				}else{
					sb.append(this.getContact4()+";");	
				}
			}
			if(sb.lastIndexOf(";")==(sb.length()-1)){
				String result = sb.substring(0, sb.length()-1);
				this.setContacts(result);
			}else{
				this.setContacts(sb.toString());
			}
		}
		
		
	}
	
	//------------------------------------------------Setters and Getters---------------------------------------------------
	public String getClient() {
		if(client==null){client="";}
		return client;
	}
	public String getParticipant() {
		if(participant==null){participant="";}
		return participant;
	}
	public String getAccountNumber() {
		if(accountNumber==null){accountNumber="";}
		return accountNumber;
	}
	public String getParent() {
		if(parent==null){parent="";}
		return parent;
	}
	public String getPremiseNumber() {
		if(premiseNumber==null){premiseNumber="";}
		return premiseNumber;
	}
	public String getStartDate() {
		if(startDate==null){startDate="";}
		return startDate;
	}
	public String getEndDate() {
		if(endDate==null){endDate="";}
		return endDate;
	}
	public String getProfile() {
		if(profile==null){profile="";}
		return profile;
	}
	public String getActiveClientPrograms() {
		if(activeClientPrograms==null){activeClientPrograms="";}
		return activeClientPrograms;
	}
	public String getInactiveClientPrograms() {
		if(inactiveClientPrograms==null){inactiveClientPrograms="";}
		return inactiveClientPrograms;
	}
	public String getEventStatus() {
		if(eventStatus==null){eventStatus="";}
		return eventStatus;
	}
	public String getMode() {
		if(mode==null){mode="";}
		return mode;
	}
	public String getCommStatus() {
		if(commStatus==null){commStatus="";}
		return commStatus;
	}
	public String getLastContact() {
		if(lastContact==null){lastContact="";}
		return lastContact;
	}
	public String getClientType() {
		if(clientType==null){clientType="";}
		return clientType;
	}
	public String getDeviceType() {
		if(deviceType==null){deviceType="";}
		return deviceType;
	}
	public String getContact1() {
		if(contact1==null){contact1="";}
		return contact1;
	}
	public String getContact2() {
		if(contact2==null){contact2="";}
		return contact2;
	}
	public String getContact3() {
		if(contact3==null){contact3="";}
		return contact3;
	}
	public String getContact4() {
		if(contact4==null){contact4="";}
		return contact4;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public void setParticipant(String participant) {
		this.participant = participant;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public void setPremiseNumber(String premiseNumber) {
		this.premiseNumber = premiseNumber;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public void setActiveClientPrograms(String activeClientPrograms) {
		this.activeClientPrograms = activeClientPrograms;
	}
	public void setInactiveClientPrograms(String inactiveClientPrograms) {
		this.inactiveClientPrograms = inactiveClientPrograms;
	}
	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public void setCommStatus(String commStatus) {
		this.commStatus = commStatus;
	}
	public void setLastContact(String lastContact) {
		this.lastContact = lastContact;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public void setContact1(String contact1) {
		this.contact1 = contact1;
	}
	public void setContact2(String contact2) {
		this.contact2 = contact2;
	}
	public void setContact3(String contact3) {
		this.contact3 = contact3;
	}
	public void setContact4(String contact4) {
		this.contact4 = contact4;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	private SystemManager systemManager;
    protected SystemManager getSystemManager(){
		if(this.systemManager==null){
			systemManager = EJBFactory.getBean(SystemManager.class);
		}
		return systemManager;
	}
    private static ProgramManager pm;

	public static ProgramManager getPm() {
		if(pm==null){
			pm = EJB3Factory.getBean(ProgramManager.class);
		}
		return pm;
	}
	public boolean isEnableCBPConsolidation(){
		return CBPUtil.isEnableCBPConsolidation(getPm().getAllPrograms());
	}
}
