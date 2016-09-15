package com.akuacom.pss2.report.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.util.CBPUtil;
import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.EventStatus;


public class ClientInfo implements Serializable,Comparable<ClientInfo>{

	private static final long serialVersionUID = -5777978730832250207L;
	
	public ClientInfo(){
		super();
	}
	
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
	private String state;
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
	
	private String program;
//	private String clientConfig;
	private String address;
	
	
	private String autoControlValue;
	private String manualControlValue;
	private String manualControl;
	
	
	private int priority;// program priority
	private int eventOptOut;// event_participant opt out
	private String eventOptOutDisplay;
	private Date eventStartTime;// event_participant creation time
	private Date eventEndTime;
	private String participantType;
	private boolean leadAccount;
	private String leadAccountDisplay;
	private String ABank;

	private boolean testParticipant;
	private boolean aggregator;
	private boolean customer;
	private boolean nonAutoDR;
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
		if(contacts==null){contacts="";}
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
//	public String getClientConfig() {
//		return clientConfig;
//	}
//	public void setClientConfig(String clientConfig) {
//		this.clientConfig = clientConfig;
//	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String contact) {
		this.address = contact;
	}
	public static String loadClientStatus(String value){
		String result="";
		if(value!=null){
			if(value.equalsIgnoreCase("0")){
				result="ONLINE";
			}
			if(value.equalsIgnoreCase("1")){
				result="ERROR";
			}
			if(value.equalsIgnoreCase("2")){
				result="OFFLINE";
			}
		}
		return result;
	}
	public static String loadClientType(String value){
		if (value.equalsIgnoreCase("0")) {
			return "AUTO";
		} else if (value.equalsIgnoreCase("2")) {
			return "MANUAL";
		} else {
			return "unknown";
		}
	}
	public static List<ClientInfo> transfer(List<ClientInfo> originalList,boolean consolidationFlag){
		List<ClientInfo> resultList = new ArrayList<ClientInfo>();
		Map<String,List<ClientInfo>> map = new HashMap<String,List<ClientInfo>>();
		for(ClientInfo instance: originalList){
			if(map.containsKey(instance.getClient())){
				map.get(instance.getClient()).add(instance);
			}else{
				List<ClientInfo> list = new ArrayList<ClientInfo>();
				list.add(instance);
				map.put(instance.getClient(), list);
			}
		}
		Set<String> keySet = map.keySet();
		Iterator<String> iterator = keySet.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			List<ClientInfo> sameClientResults = map.get(key);
			ClientInfo clientInfo = sameClientResults.get(0);//sure index 0 has vlue and not be null
			int contactIndex=0;
			List<String> activeProgramList=new ArrayList<String>();
			
			List<String> inactiveProgramList=new ArrayList<String>();
			
			StringBuffer contactsString = new StringBuffer();
			StringBuffer parentsString = new StringBuffer();
			
			//mode 
			String manualControl = clientInfo.getManualControl();
			if(manualControl.equalsIgnoreCase("1")){
				//manual
				String munaulControlValue = clientInfo.getManualControlValue();
				if(munaulControlValue.equalsIgnoreCase("")){
					clientInfo.setMode("NORMAL");
				}else{
					clientInfo.setMode(munaulControlValue.toUpperCase());
				}
			}else if(manualControl.equalsIgnoreCase("0")){
				//auto
				String autoControlValue = clientInfo.getAutoControlValue();
				if(autoControlValue.equalsIgnoreCase("")){
					clientInfo.setMode("NONE");
				}else{
					clientInfo.setMode(autoControlValue.toUpperCase());
				}
			}
			for(int i =0;i<sameClientResults.size();i++){
				ClientInfo tmpCI = sameClientResults.get(i);
				
				
				//build programs inactive or active
				String program = tmpCI.getProgram();
				String state = tmpCI.getState();
				String parent = tmpCI.getParent();
				if(parent!=null&&(!parent.equalsIgnoreCase(""))){
					if(parentsString.indexOf(parent+";")<=-1){
						parentsString.append(parent+";");
					}
				}
				if(program!=null&&state!=null){
					if(!program.equalsIgnoreCase(TestProgram.PROGRAM_NAME)){
						if(state.equalsIgnoreCase("0")){
							if (!inactiveProgramList.contains(program))
								inactiveProgramList.add(program);
							
						}else{
							if (!activeProgramList.contains(program))
								activeProgramList.add(program);
						}
					}	
				}
				
				//build contacts
				String address = tmpCI.getAddress();
				if(address!=null){
					if((contactsString.indexOf(address+";")<=-1)&&(contactIndex<4)){
						contactsString.append(address+";");
						contactIndex++;
					}
				}
				
			}
			
			Collections.sort(activeProgramList);
			if(consolidationFlag){
				activeProgramList = CBPUtil.transferList(activeProgramList);
			}
			String activeProgramsString ="";
			for(String p: activeProgramList){
				activeProgramsString=activeProgramsString.concat(" "+p);
			}
			if(!activeProgramsString.equals(""))
				activeProgramsString= activeProgramsString.substring(1);
			
			clientInfo.setActiveClientPrograms(activeProgramsString);
			
			Collections.sort(inactiveProgramList);
			if(consolidationFlag){
				inactiveProgramList = CBPUtil.transferList(inactiveProgramList);
			}
			String inactiveProgramsString ="";
			for(String p: inactiveProgramList){
				inactiveProgramsString=inactiveProgramsString.concat(" "+p);
			}
			if(!inactiveProgramsString.equals(""))
				inactiveProgramsString= inactiveProgramsString.substring(1);
			
			clientInfo.setInactiveClientPrograms(inactiveProgramsString);
			
			if(contactsString.toString().endsWith(";")){
				contactsString.deleteCharAt(contactsString.lastIndexOf(";"));
			}
			clientInfo.setContacts(contactsString.toString());
			if(parentsString.toString().endsWith(";")){
				parentsString.deleteCharAt(parentsString.lastIndexOf(";"));
			}
			clientInfo.setParent(parentsString.toString());
			
			
			//DRMS-7896-opt out value
			clientInfo.setEventOptOutDisplay(calculateOptOutValue(sameClientResults));
			resultList.add(clientInfo);
		}
		
		Set<ClientInfo> set = new TreeSet<ClientInfo>(resultList);
		resultList.clear();
		for(ClientInfo instance:set){
			resultList.add(instance);
		}
		return resultList;
	}
	
	@Override
    public int compareTo(ClientInfo o) {
        if (o.getClient() == null) {
            return 1;
        } else if (this.getClient() == null) {
            return -1;
        } else {
            return this.getClient().compareTo(o.getClient());
        }
    }
	public void setState(String state) {
		this.state = state;
	}
	public String getState() {
		return state;
	}
	public String getAutoControlValue() {
		return autoControlValue;
	}
	public void setAutoControlValue(String autoControlValue) {
		if(autoControlValue==null){autoControlValue="";}
		this.autoControlValue = autoControlValue;
	}
	public String getManualControlValue() {
		if(manualControlValue==null){manualControlValue="";}
		return manualControlValue;
	}
	public void setManualControlValue(String manualControlValue) {
		this.manualControlValue = manualControlValue;
	}
	public String getManualControl() {
		if(manualControl==null){manualControl="";}
		return manualControl;
	}
	public void setManualControl(String manualControl) {
		this.manualControl = manualControl;
	}
	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * @return the eventOptOut
	 */
	public int getEventOptOut() {
		return eventOptOut;
	}
	/**
	 * @param eventOptOut the eventOptOut to set
	 */
	public void setEventOptOut(int eventOptOut) {
		this.eventOptOut = eventOptOut;
	}
	/**
	 * @return the eventCreationTime
	 */
	public Date getEventStartTime() {
		return eventStartTime;
	}
	/**
	 * @param eventCreationTime the eventCreationTime to set
	 */
	public void setEventStartTime(Date eventStartTime) {
		this.eventStartTime = eventStartTime;
	}
	/**
	 * @return the participantType
	 */
	public String getParticipantType() {
		participantType="";
		if(isAggregator()){
			participantType+=Aggregator+",";
		}
		if(isTestParticipant()){
			participantType+=Test+",";
		}
		if(this.isCustomer()){
			participantType+=Customer+",";
		}
		if(this.isNonAutoDR()){
			participantType+=Non+",";
		}
		if(participantType.endsWith(",")){
			participantType = participantType.substring(0, participantType.length()-1);
		}
		return participantType;
	}
	/**
	 * @param participantType the participantType to set
	 */
	public void setParticipantType(String participantType) {
		this.participantType = participantType;
	}

	/**
	 * @return the aBank
	 */
	public String getABank() {
		return ABank;
	}
	/**
	 * @param aBank the aBank to set
	 */
	public void setABank(String aBank) {
		ABank = aBank;
	}
	//DRMS-7896 for opt out value
	public static String calculateOptOutValue(List<ClientInfo> sameClientResults){
		HashMap<String,Integer> programPriorities = new HashMap<String,Integer>();
		List<EventState> eventStates = new ArrayList<EventState>();
		for (ClientInfo info : sameClientResults) {
			if(info.getEventStartTime()==null||info.getEventEndTime()==null){
				continue;
			}
			programPriorities.put(info.getProgram(), info.getPriority());
			EventState eventState = new EventState(); 
			eventState.setProgramName(info.getProgram());
			eventState.setStartTime(info.getEventStartTime());
			eventState.setEndTime(info.getEventEndTime());
			int eventOptOutValue =info.getEventOptOut();
			if(eventOptOutValue==20||eventOptOutValue==40){
				eventState.setEventStatus(EventStatus.OPT_OUT);
			}else{
				eventState.setEventStatus(EventStatus.NONE);
			}
			eventStates.add(eventState);
		}
		Collections.sort(eventStates, new EventState.PriorityComparator(programPriorities));
		if(eventStates.size()>0){
			EventState eventState = eventStates.get(0);

			return eventState.getEventStatus().name();
		}else{
			return "";
		}
	}
	/**
	 * @return the eventOptOutDisplay
	 */
	public String getEventOptOutDisplay() {
		if(("OPT_OUT").equalsIgnoreCase(eventOptOutDisplay)||("OPT OUT").equalsIgnoreCase(eventOptOutDisplay)){
			eventOptOutDisplay = "OPT OUT";
		}else{
			eventOptOutDisplay = "";
		}
		return eventOptOutDisplay;
	}
	/**
	 * @param eventOptOutDisplay the eventOptOutDisplay to set
	 */
	public void setEventOptOutDisplay(String eventOptOutDisplay) {
		this.eventOptOutDisplay = eventOptOutDisplay;
	}
	/**
	 * @return the leadAccountDisplay
	 */
	public String getLeadAccountDisplay() {
		if(leadAccount){
			leadAccountDisplay="Y";
		}else{
			leadAccountDisplay="N";
		}
		return leadAccountDisplay;
	}
	/**
	 * @param leadAccountDisplay the leadAccountDisplay to set
	 */
	public void setLeadAccountDisplay(String leadAccountDisplay) {
		this.leadAccountDisplay = leadAccountDisplay;
	}
	/**
	 * @param leadAccount the leadAccount to set
	 */
	public void setLeadAccount(boolean leadAccount) {
		this.leadAccount = leadAccount;
	}
	/**
	 * @return the leadAccount
	 */
	public boolean isLeadAccount() {
		return leadAccount;
	}
	/**
	 * @return the eventEndTime
	 */
	public Date getEventEndTime() {
		return eventEndTime;
	}
	/**
	 * @param eventEndTime the eventEndTime to set
	 */
	public void setEventEndTime(Date eventEndTime) {
		this.eventEndTime = eventEndTime;
	}

	/**
	 * @return the testParticipant
	 */
	public boolean isTestParticipant() {
		return testParticipant;
	}
	/**
	 * @param testParticipant the testParticipant to set
	 */
	public void setTestParticipant(boolean testParticipant) {
		this.testParticipant = testParticipant;
	}
	/**
	 * @return the aggregator
	 */
	public boolean isAggregator() {
		return aggregator;
	}
	/**
	 * @param aggregator the aggregator to set
	 */
	public void setAggregator(boolean aggregator) {
		this.aggregator = aggregator;
	}
	
	/**
	 * @return the customer
	 */
	public boolean isCustomer() {
		return customer;
	}
	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(boolean customer) {
		this.customer = customer;
	}
	/**
	 * @return the nonAutoDR
	 */
	public boolean isNonAutoDR() {
		return nonAutoDR;
	}
	/**
	 * @param nonAutoDR the nonAutoDR to set
	 */
	public void setNonAutoDR(boolean nonAutoDR) {
		this.nonAutoDR = nonAutoDR;
	}

	private static final String Aggregator="Aggregator";
	private static final String Test="Test Participant ";
	private static final String Customer="Customer";
	private static final String Non="Non-Auto DR";
}
