package com.akuacom.pss2.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.util.CBPUtil;

public class ParticipantSummary implements Serializable{

	private static final String TYPE_AGGREGATOR = "Aggregator";
	private static final String TYPE_CUSTOMER = "Customer";
	private static final String TYPE_NONAUTODR = "Non-Auto DR";
	private static final String TYPE_TESTPARTICIPANT = "Test Participant";
	private static final long serialVersionUID = -2045279992397872910L;
	
	private String uuid;
	private String name;
	private String accountNumber;
	private String requestId;
	private boolean installer;
	private String notes;
	private String userType;
	private int type;
	private Date autoDrProfileStartDate;
	private String bcdRepName;
	private List<String> activeEvents;
	private List<ProgramParticipation> programParticipation;
	private boolean cbpConsolidation;
	public static class ProgramParticipation implements Serializable, Comparable<ProgramParticipation>{
		private static final long serialVersionUID = -3864198217818690444L;
		
		private String programName;
		private int optOut =0;
		private List<String> childParticipants;
		
		public ProgramParticipation() {
			super();
		}
		public String getProgramName() {
			return programName;
		}
		public void setProgramName(String programName) {
			this.programName = programName;
		}
		public int getOptOut() {
			return optOut;
		}
		public void setOptOut(int optOut) {
			this.optOut = optOut;
		}
		
		public List<String> getChildParticipants() {
			if(childParticipants==null)
				childParticipants = new ArrayList<String>();
			return childParticipants;
		}
		
		public void addChildParticipant(String child){
			List<String> temp = this.getChildParticipants();
			if(!temp.contains(child)){
				temp.add(child);
			}
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((programName == null) ? 0 : programName.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ProgramParticipation other = (ProgramParticipation) obj;
			if (programName == null) {
				if (other.programName != null)
					return false;
			} else if (!programName.equals(other.programName))
				return false;
			return true;
		}
		
		@Override
		public int compareTo(ProgramParticipation o) {
			if (this==null) return -1;
			if (o==null) return 1;
			if (this.programName==null) {
				if (o.programName!=null)
					return -1;
				return 0;
			} else if (o.programName==null) {
				return 1;
			}
			
			return this.getProgramName().compareTo(o.getProgramName());
		}
	}	
	
	public List<ProgramParticipation> getProgramParticipation() {
		if(programParticipation==null)
			programParticipation = new ArrayList<ProgramParticipation>();
		return programParticipation;
	}
	

	public void addProgramParticipation(ProgramParticipation pp){
		List<ProgramParticipation> list = this.getProgramParticipation();
		if(!list.contains(pp)){
			list.add(pp);
		}
	}
	
	public String getAllProgramNames() {
		List<String> programs = new ArrayList<String>();
		for(ProgramParticipation pp:this.getProgramParticipation()){
			programs.add(pp.getProgramName());
		}
		Collections.sort(programs);
		String result ="";
		for(String p: programs){
			result=result.concat(" "+p);
		}
		if(result.equals(""))
			return "";
		else
			return result.substring(1);
	}
	public String getAllProgramNamesWithConsolidation(){
		List<String> programs = new ArrayList<String>();
		for(ProgramParticipation pp:this.getProgramParticipation()){
			String key = pp.getProgramName();
			if(CBPUtil.getCbpGroup().get("CBP").contains(key)){
            	if(!containsCBP(programs)){
            		key="CBP";
            		programs.add(key);
            	}
            }else{
            	programs.add(key);
            }
		}
		Collections.sort(programs);
		String result ="";
		for(String p: programs){
			result=result.concat(" "+p);
		}
		if(result.equals(""))
			return "";
		else
			return result.substring(1);
	}
	private boolean containsCBP(List<String> pp){
		boolean result = false;
		for(String ppInstance:pp){
			if(ppInstance.equalsIgnoreCase("CBP")){
				result = true;
				return result;
			}
		}
		return result;
	}
	public void setType(String type){
		this.type = Byte.parseByte(type);
	}
	
	public String getType(){
		return this.type+"";
	}
	
	public String getTypeStr(){
		if (type == Participant.TYPE_AUTO) {
			return "AUTO";
		} else if (type == Participant.TYPE_MANUAL) {
			return "MANUAL";
		} else {
			return "unknown";
		}
	}
	
	public void addActiveEvent(String activeEvent){
		if(!getActiveEvents().contains(activeEvent)){
			activeEvents.add(activeEvent);
		}
	}
	
	public List<String> getActiveEvents(){
		if(this.activeEvents==null){
			activeEvents = new ArrayList<String>();
		}
		return activeEvents;
	}
	
	public String getUUID() {
		return uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public int getAggPartCount() {
		int count = 0;
		List<ProgramParticipation> ppList = this.getProgramParticipation();
		List<ProgramParticipation> result = new ArrayList<ParticipantSummary.ProgramParticipation>();
		if(this.isCbpConsolidation()){
			for(ProgramParticipation pp:ppList){
				String key = pp.getProgramName();
				if(CBPUtil.getCbpGroup().get("CBP").contains(key)){
	            	if(!contains(result)){
	            		result.add(pp);
	            	}
	            }else{
	            	result.add(pp);
	            }
			}
			ppList = result;
		}
		
		for(ProgramParticipation pp:ppList){
			count+=pp.getChildParticipants().size();
		}
		return count;
	}
	private boolean contains(List<ProgramParticipation> pp){
		boolean result = false;
		for(ParticipantSummary.ProgramParticipation ppInstance:pp){
			if(ppInstance.getProgramName().equalsIgnoreCase("CBP")||CBPUtil.getCbpGroup().get("CBP").contains(ppInstance.getProgramName())){
				result = true;
				return result;
			}
		}
		return result;
	}
	public boolean isInstaller() {
		return installer;
	}

	public void setInstaller(boolean installer) {
		this.installer = installer;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public boolean isHasNotes() {
		return (getNotes() != null) && (!getNotes().isEmpty());
	}
	
	
	private String customerName;
	
	/** The Street Address address. */
	private String address = "";
	//City
	private String serviceCityName;
    //Abank
	private String abank;
    //Start Date
	private Date startDate;
    //Deactivate Date
	private Date endDate;
    //Enrollment Date
	private Date enrollmentDate;
	private boolean dataEnabler;
	private String participantType;
	private boolean aggregator;
	private boolean testParticipant;
	private boolean nonAutoDR;
	private boolean customer;
	
	public boolean isTestParticipant() {
		return testParticipant;
	}

	public void setTestParticipant(boolean testParticipant) {
		this.testParticipant = testParticipant;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getServiceCityName() {
		return serviceCityName;
	}

	public void setServiceCityName(String serviceCityName) {
		this.serviceCityName = serviceCityName;
	}

	public String getAbank() {
		return abank;
	}

	public void setAbank(String abank) {
		this.abank = abank;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public boolean isAggregator() {
		return aggregator;
	}

	public void setAggregator(boolean aggregator) {
		this.aggregator = aggregator;
	}
	
	public boolean isDataEnabler() {
		return dataEnabler;
	}

	public void setDataEnabler(boolean dataEnabler) {
		this.dataEnabler = dataEnabler;
	}

	public String getParticipantType() {
		participantType = "";
		if(this.isAggregator()){
			participantType = participantType.concat(TYPE_AGGREGATOR);
		}
		if(this.isCustomer()){
			if(participantType.length()>0) participantType = participantType.concat(",");
			participantType = participantType.concat(TYPE_CUSTOMER);
		}
		if(this.isNonAutoDR()){
			if(participantType.length()>0) participantType = participantType.concat(",");
			participantType = participantType.concat(TYPE_NONAUTODR);
		}
		if(this.isTestParticipant()){
			if(participantType.length()>0) participantType = participantType.concat(",");
			participantType = participantType.concat(TYPE_TESTPARTICIPANT);
		}
		
		return participantType;
	}

	public void setParticipantType(String participantType) {
		this.participantType = participantType;
	}

	public boolean isNonAutoDR() {
		return nonAutoDR;
	}

	public void setNonAutoDR(boolean nonAutoDR) {
		this.nonAutoDR = nonAutoDR;
	}

	public boolean isCustomer() {
		return customer;
	}

	public void setCustomer(boolean customer) {
		this.customer = customer;
	}

	public Date getAutoDrProfileStartDate() {
		return autoDrProfileStartDate;
	}

	public void setAutoDrProfileStartDate(Date autoDrProfileStartDate) {
		this.autoDrProfileStartDate = autoDrProfileStartDate;
	}

	public String getBcdRepName() {
		return bcdRepName;
	}

	public void setBcdRepName(String bcdRepName) {
		this.bcdRepName = bcdRepName;
	}

	/**
	 * @return the cbpConsolidation
	 */
	public boolean isCbpConsolidation() {
		return cbpConsolidation;
	}

	/**
	 * @param cbpConsolidation the cbpConsolidation to set
	 */
	public void setCbpConsolidation(boolean cbpConsolidation) {
		this.cbpConsolidation = cbpConsolidation;
	}


	public String getRequestId() {
		return requestId;
	}


	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}


		
	
}
