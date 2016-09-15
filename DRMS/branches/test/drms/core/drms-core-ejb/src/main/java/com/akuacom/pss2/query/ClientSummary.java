package com.akuacom.pss2.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.MemorySequence;
import com.akuacom.pss2.util.OperationModeValue;

public class ClientSummary implements Serializable{
	
	private static final long serialVersionUID = 1833336023480093744L;
	private String UUID;
	private String name;
	private String deviceType;
	private String parent;
	private boolean manualControl;
	private String type; /* auto or manual */
	
	private Date lastContact;
	private ClientStatus commsStatus;
	
	private EventStatus currentStatus;
	private EventStatus lastStatus;
	
	private OperationModeValue currentMode;
	private OperationModeValue lastMode;
	
	private List<ProgramParticipation> programs;
	
	private List<ManualSignal> msignals;
	private List<String> emailAddress;
	
	private boolean leadAccount;
	private String participantType;
	private String ABank;
	private boolean testParticipant;
	private boolean aggregator;
	private boolean customer;
	private boolean nonAutoDR;
	
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getTypeString(){
		if (Integer.parseInt(type) ==Participant.TYPE_AUTO) {
			return "AUTO";
		} else if (Integer.parseInt(type) ==Participant.TYPE_MANUAL) {
			return "MANUAL";
		} else {
			return "unknown";
		}
	}
	
	public List<String> getEmailAddress() {
		if(emailAddress==null)
			emailAddress = new ArrayList<String>();
		return emailAddress;
	}
	public Date getLastContact() {
		return lastContact;
	}

	public void setLastContact(Date lastContact) {
		this.lastContact = lastContact;
	}

	public ClientStatus getCommsStatus() {
		return commsStatus;
	}

	public void setCommsStatus(ClientStatus commsStatus) {
		this.commsStatus = commsStatus;
	}
	
	public EventStatus getCurrentStatus() {
		if(currentStatus==null)
			calculateCurrentEventStatusAndMode();
		return currentStatus;
	}

	public void setCurrentStatus(EventStatus currentStatus) {
		this.currentStatus = currentStatus;
	}

	public EventStatus getLastStatus() {
		return lastStatus;
	}

	public void setLastStatus(EventStatus lastStatus) {
		this.lastStatus = lastStatus;
	}

	public OperationModeValue getCurrentMode() {
		if(currentMode==null)
			calculateCurrentEventStatusAndMode();
		return currentMode;
	}

	public void setCurrentMode(OperationModeValue currentMode) {
		this.currentMode = currentMode;
	}

	public OperationModeValue getLastMode() {
		return lastMode;
	}

	public void setLastMode(OperationModeValue lastMode) {
		this.lastMode = lastMode;
	}

	public List<ProgramParticipation> getPrograms() {
		if(programs==null){
			programs = new ArrayList<ProgramParticipation>();
		}
		return programs;
	}
	
	public List<ManualSignal> getManualSignals() {
		if(msignals==null){
			msignals = new ArrayList<ManualSignal>(2);
		}
		return msignals;
	}
	
	public boolean isManualControl() {
		return manualControl;
	}
	public void setManualControl(boolean maunualControl) {
		this.manualControl = maunualControl;
	}
	
	public String getProgramNames() {
		List<String> programs = new ArrayList<String>();
		for(ProgramParticipation pp:this.getPrograms()){
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
	
	public List<Evt> getEvents(){
		List<Evt> ps = new ArrayList<Evt>();
		for(ProgramParticipation pp:this.getPrograms()){
			ps.addAll(pp.getEvents());
		}
		return ps;
	}
	
	private String getClientManualSignalValueAsString(String signalName){
		String res = null;
		if (getManualSignals() != null) {
			for (ManualSignal signalState : getManualSignals()) {
				if (signalState != null && signalState.getSignalName()!=null) {
					if (signalState.getSignalName().equals(signalName)) {
						res = signalState.getSignalValue();
						break;
					}
				}
			}
		}
		return res;
	}
	
	protected void calculateCurrentEventStatusAndMode(){
		List<EventState> eventStates = new ArrayList<EventState>();
		long nowMS = System.currentTimeMillis();
		HashMap<String,Integer> programPriorities = new HashMap<String,Integer>();
		for (ProgramParticipation pp : this.getPrograms()) {
			programPriorities.put(pp.getProgramName(), pp.getPriority());
			for(Evt evt:pp.getEvents()){
				EventState eventState = new EventState(); 
				eventState.setDrasClientID(getName());
				eventState.setEventIdentifier(evt.getEventName());
				eventState.setProgramName(pp.getProgramName());
				long startTimeMS = evt.getStartTime().getTime();
				if (isManualControl()) {
					String pendingValue = getClientManualSignalValueAsString("pending");
					if (pendingValue == null) {
						// hardcoded default
						pendingValue = "off";
					}
					eventState.setEventStatus((pendingValue.equals("on")? EventStatus.ACTIVE:EventStatus.NONE));
				} else {
					String pendingValue = evt
							.getSignalValueForEventParticipantAsString("pending");
					if (pendingValue == null) {
						pendingValue = "off";
					}
					if (startTimeMS < nowMS) {
						eventState.setEventStatus(EventStatus.ACTIVE);
					} else if (pendingValue.equals("on")) {
						eventState.setEventStatus(EventStatus.NEAR);
					} else {
						eventState.setEventStatus(EventStatus.FAR);
					}
				}
				
				
				String modeSignalValue;
				if (isManualControl()) {
					modeSignalValue = getClientManualSignalValueAsString("mode");
					if (modeSignalValue == null) {
						// hardcoded default.
						modeSignalValue = "normal";
					}
				} else {
					modeSignalValue = evt
							.getSignalValueForEventParticipantAsString("mode");
					if (modeSignalValue == null) {
						modeSignalValue = "normal";
					}
				}
				
				eventState.setOperationModeValue(modeSignalValue);
				eventState.setManualControl(isManualControl());
				eventState.setCurrentTimeS((nowMS - startTimeMS) / 1000.0);
				eventState.setNotificationTime(evt.getIssuedTime());
				eventState.setStartTime(evt.getStartTime());
				eventState.setEndTime(evt.getEndTime());
				
				//DRMS-7056
				int eventOptOutValue = evt.getEventOptOut();
				//ACTIVE_EVENT_OPT_OUT ("Event Opt-out", 20),
				//INACTIVE_EVENT_OPT_OUT ("Event Opt-out", 40),
				if(eventOptOutValue==20||eventOptOutValue==40){
					eventState.setEventStatus(EventStatus.OPT_OUT);
					eventState.setOperationModeValue("normal");
				}
				
				//that's enough for mode and status
				
				eventStates.add(eventState);
			}
		}
		
		if (eventStates.size() == 0) {
			// create event state based on manual control or a empty/no event
			// NORMAL one
			EventState eventState = new EventState();
			eventState.setDrasClientID(getName());
			eventState.setEventIdentifier("");
			eventState.setEventModNumber(0);
			eventState.setEventStateID(MemorySequence.getNextSequenceId());
			eventState.setProgramName("");

			String pendingSignalValue = "off";
			String modeSignalValue = "unknown";
			if (isManualControl()) {
				pendingSignalValue = getClientManualSignalValueAsString("pending");
				if(pendingSignalValue==null) pendingSignalValue="off";
				modeSignalValue = getClientManualSignalValueAsString("mode");
				if(modeSignalValue==null) modeSignalValue="unknown";
			} 
			
			if ("on".equals(pendingSignalValue)) {
				eventState.setEventStatus(EventStatus.ACTIVE);
			} else {
				eventState.setEventStatus(EventStatus.NONE);
			}
			eventState.setOperationModeValue(modeSignalValue);
			eventState.setManualControl(isManualControl());
			eventStates.add(eventState);
		}
		Collections.sort(eventStates, new EventState.PriorityComparator(
				programPriorities));
		
		//set current mode value and status
		setCurrentMode(eventStates.get(0).getOperationModeValue());
		setCurrentStatus(eventStates.get(0).getEventStatus());
	}
	
	public static class ProgramParticipation implements Serializable, Comparable<ProgramParticipation>{
		private static final long serialVersionUID = -3864198217818690444L;
		
		private String programName;
		private List<Evt> events;
		private int priority;
		private int state =0;
		
		public String getProgramName() {
			return programName;
		}
		public void setProgramName(String programName) {
			this.programName = programName;
		}
		public List<Evt> getEvents() {
			if(events==null){
				events= new ArrayList<Evt>(5);
			}
			return events;
		}
		public int getPriority() {
			return priority;
		}
		public void setPriority(int priority) {
			this.priority = priority;
		}
		
		public int getState() {
			return state;
		}
		public void setState(int state) {
			this.state = state;
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
	
	public static class Evt implements Serializable{
		private static final long serialVersionUID = -700414900115043070L;
		private String eventName;
		private Date   startTime;
		private Date   endTime;
		private Date issuedTime;
		private int eventOptOut;
	
		private List<EvtSignal> evtSignals;
		
		public String getEventName() {
			return eventName;
		}
		public void setEventName(String eventName) {
			this.eventName = eventName;
		}
		public Date getStartTime() {
			return startTime;
		}
		public void setStartTime(Date startTime) {
			this.startTime = startTime;
		}
		public Date getEndTime() {
			return endTime;
		}
		public void setEndTime(Date endTime) {
			this.endTime = endTime;
		}
		
		public Date getIssuedTime() {
			return issuedTime;
		}
		public void setIssuedTime(Date issuedTime) {
			this.issuedTime = issuedTime;
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
		public List<EvtSignal> getEvtSignals() {
			if(evtSignals==null) 
				evtSignals = new ArrayList<EvtSignal>();
			return evtSignals;
		}
		
		public String getSignalValueForEventParticipantAsString(String signalName){
			String res = null;
	        List<EvtSignal> signalEntries = getEvtSignals();
	        Collections.sort(signalEntries);
	        Collections.reverse(signalEntries);
	        for (EvtSignal signalEntry : signalEntries) {
	            if (signalEntry.getSignalStartTime().getTime()< System.currentTimeMillis()
	                    && signalEntry.getSignalName().equals(signalName)) {
	                if (signalEntry.isLevelSignal()) {
	                    res = signalEntry.getStringValue();
	                    break;
	                } else {
	                    res = Double.toString(signalEntry.getNumberValue());
	                    break;
	                }
	            }
	        }
	        return res;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((eventName == null) ? 0 : eventName.hashCode());
			result = prime * result
					+ ((startTime == null) ? 0 : startTime.hashCode());
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
			Evt other = (Evt) obj;
			if (eventName == null) {
				if (other.eventName != null)
					return false;
			} else if (!eventName.equals(other.eventName))
				return false;
			if (startTime == null) {
				if (other.startTime != null)
					return false;
			} else if (!startTime.equals(other.startTime))
				return false;
			return true;
		}
	}
	
	public static class EvtSignal implements Serializable,Comparable<EvtSignal>{
		private static final long serialVersionUID = -4958211089027812696L;
		private String signalName;
		private boolean levelSignal;
		private Double numberValue;
		private String stringValue;
		
		private Date signalStartTime;
		public String getSignalName() {
			return signalName;
		}
		public void setSignalName(String signalName) {
			this.signalName = signalName;
		}
		public boolean isLevelSignal() {
			return levelSignal;
		}
		public void setLevelSignal(boolean levelSignal) {
			this.levelSignal = levelSignal;
		}
		public Double getNumberValue() {
			return (numberValue !=null) ? numberValue : 0.0;
		}
		public void setNumberValue(double numberValue) {
			this.numberValue = numberValue;
		}
		public Date getSignalStartTime() {
			return signalStartTime;
		}
		public void setSignalStartTime(Date signalStartTime) {
			this.signalStartTime = signalStartTime;
		}
		
		public String getStringValue() {
			return stringValue;
		}
		public void setStringValue(String stringValue) {
			this.stringValue = stringValue;
		}
		@Override
		public int compareTo(EvtSignal o) {
			if (o == null) {
				return 1;
			}
			return signalStartTime.compareTo(o.getSignalStartTime());
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((signalName == null) ? 0 : signalName.hashCode());
			result = prime
					* result
					+ ((signalStartTime == null) ? 0 : signalStartTime
							.hashCode());
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
			EvtSignal other = (EvtSignal) obj;
			if (signalName == null) {
				if (other.signalName != null)
					return false;
			} else if (!signalName.equals(other.signalName))
				return false;
			if (signalStartTime == null) {
				if (other.signalStartTime != null)
					return false;
			} else if (!signalStartTime.equals(other.signalStartTime))
				return false;
			return true;
		}
	}
	
	public static class ManualSignal implements Serializable {
		private static final long serialVersionUID = 1L;
		private String signalName;
		private String signalValue;
		public String getSignalName() {
			return signalName;
		}
		public void setSignalName(String signalName) {
			this.signalName = signalName;
		}
		public String getSignalValue() {
			return signalValue;
		}
		public void setSignalValue(String signalValue) {
			this.signalValue = signalValue;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((signalName == null) ? 0 : signalName.hashCode());
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
			ManualSignal other = (ManualSignal) obj;
			if (signalName == null) {
				if (other.signalName != null)
					return false;
			} else if (!signalName.equals(other.signalName))
				return false;
			return true;
		}
	}

	/**
	 * @return the leadAccount
	 */
	public boolean isLeadAccount() {
		return leadAccount;
	}
	/**
	 * @param leadAccount the leadAccount to set
	 */
	public void setLeadAccount(boolean leadAccount) {
		this.leadAccount = leadAccount;
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
