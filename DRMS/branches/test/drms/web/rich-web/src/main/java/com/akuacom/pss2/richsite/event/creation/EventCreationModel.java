package com.akuacom.pss2.richsite.event.creation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.system.SystemManager;

public class EventCreationModel extends AbstractEventCreation implements Serializable {
	
	private static final Logger log = Logger.getLogger(EventCreationModel.class.getName());
	
	private static final long serialVersionUID = 1L;
	
	private EventTiming eventTime;
	
	private UploadFile uploadFile;
	
	private String nextPageNoValidation;
	
	private String locations;
	
	
	public EventCreationModel(String program){
		super(program);
	}
	
	private List<EventLocation> locationList;
	
	public EventParticipantSelection getParticipantSelection() {
		EventParticipantSelection participantSelection = (EventParticipantSelection) getEventEnrollment();
		//Because the shed must be re-calculated after the event time changing, so this must be initialize any time it was be invoked
		participantSelection.recalculateShed();
		return participantSelection;
	}
	
	@Override
	protected EventEnrollment createEventEnrollment() {
		return  new EventParticipantSelection(this);
	}
	
	@Override
	public boolean isCbpProgramEJB() {
		if (getProgram().getClassName().equals("com.akuacom.pss2.program.cbp.CBPProgramEJB"))
			return true;
		
		return false;
	}

	public boolean isUploadEnable() {
		SystemManager manager=EJBFactory.getBean(SystemManager.class);
		boolean useSecondaryUtilityName=manager.getPss2Features().isUseSecondaryUtilityNameForAPX();
		if (useSecondaryUtilityName) {
			if(this.getProgram().getProgramClass()!=null){
				if (this.getProgram().getProgramClass().equals("CBP") || this.getProgram().getProgramClass().equals("DRC"))
					return true;			
			}
		} else if (ProgramConverter.getMapping().containsValue(getProgramName())) {
			return true;
		}
		
		return false;
	}

	protected void doCreateEvent(){
		Event event = this.converToEvent(true);
		//DRMS-8389
		if("DBP DA".equalsIgnoreCase(event.getProgramName())){
			event.getEventParticipants().clear();
		}
		final EventManager eventManager = EJBFactory.getBean(EventManager.class);
		eventManager.createEvent(event.getProgramName(), event);
	}
	
	public void goToConfirm(){
		this.goToPage("confirmation");
	}
	
	public void goToNextNoValidation(){
		if (this.nextPageNoValidation!=null && this.nextPageNoValidation.equals("confirmation"))
			this.getWizard().goToPage("confirmation");
		else
			this.getWizard().nextPage();
	}
	
	public void setNextPage(String nextPage){
		this.nextPageNoValidation=nextPage;
	}
	
	public Event converToEvent(boolean withParticipants) {
		ProgramEJB programEJB = getProgramManager().lookupProgramBean(getProgram());
		
		Event event=programEJB.newProgramEvent();
		if (event instanceof DBPEvent)
			event.setDrEvent(true);
		
		EventTiming evtTiming = this.getEventTiming();
		event.setEndTime(evtTiming.getEndTime());
		event.setEventName(getEventName());
		
		event.setEventStatus(getEventStatus());
		event.setManual(true);
		event.setProgramName(getProgramName());
		event.setReceivedTime(evtTiming.getReceivedTime());
		event.setStartTime(evtTiming.getStartTime());
		
		if(evtTiming.getReceivedTime()==null){
            evtTiming.setReceivedTime(new Date());
        }

        // DRMS-7630: null issue time for immediately issue
        // user may hangs on this UI for a while
        if (evtTiming.getIssuedTime() == null) {
            event.setIssuedTime(new Date());
            evtTiming.setImmediateIssue(true);
        } else {
			event.setIssuedTime(evtTiming.getIssuedTime());
		}
		
		if (isCbpProgramEJB()) {
			List<String> locStr=new ArrayList<String>();
			for (EventLocation loc: this.getLocationList()) {
				if (loc.isEnrolled())
					locStr.add(loc.getID());
			}
			if (locStr.size()>0) {
				if (locStr.contains("DLAP_SCE")) {
					List<String> all=new ArrayList<String>();
					all.add("DLAP_SCE");
					event.setLocations(all);
				} else {
					event.setLocations(locStr);
				}
				locations=event.getLocations().toString();
			}
		}

		if(withParticipants){
			this.populateEventParticipants(event);
			
			if (uploadFile.isAvailable() && uploadFile.getParser().getInvalidAccounts().size()>0) {
				ProgramValidationMessage msg=new ProgramValidationMessage();
				StringBuilder builder=new StringBuilder();
				builder.append("The following account numbers do not exist in the DRAS: ");
				for (String account: uploadFile.getParser().getInvalidAccounts()) {
					builder.append(account);
					builder.append(",");
				}
				msg.setParameterName("account");
				msg.setDescription(builder.substring(0, builder.toString().length()));
				if (event.getWarnings()==null) 
					event.setWarnings(new ArrayList<ProgramValidationMessage>());
				event.getWarnings().add(msg);
			}
		}
		
		//event.setState(model.getState());
		//event.setWarnings(model.getWarnings());
		return event;
	}
	
	
	/** sub class to override to provide a different wizard for event creation **/
	protected  Wizard createWizard(){
		Wizard wizard = new Wizard("eventTiming",
				"participantSelection",
				"confirmation");
		return wizard;
	}
	
	/** subclass to override to customize validators for each page **/
	protected void installValidators(){
		registerValidator("eventTiming", new EventTimingValidator());
		registerValidator("participantSelection", new EventParticipantValidator());
		registerValidator("confirmation", new CompoundValidator(
					new EventTimingValidator(),new EventParticipantValidator()));
	}
	
	
	public EventTiming getEventTiming() {
		if(eventTime==null){
			eventTime = new EventTiming();
			//initialize with default value in program constraint
		}
		return eventTime;
	}
	

	public UploadFile getUploadFile() {
		if (uploadFile==null)
			uploadFile=new UploadFile(this);
		return uploadFile;
	}
	public void setUploadFile(UploadFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	@Override
	public Date getIssueTime() {
		if(this.getEventTiming().getIssuedTime()!=null)
			return this.getEventTiming().getIssuedTime();
		else
			return new Date();
	}
	
	@Override
	public Date getStartTime() {
		return this.getEventTiming().getStartTime();
	}
	
	@Override
	public Date getEndTime() {
		return this.getEventTiming().getEndTime();
	}
	
	public String getLocations() {
		return locations;
	}

	public List<EventLocation> getLocationList() {
		if (locationList==null) {
			locationList = new ArrayList<EventLocation>();
			locationList.add(new EventLocation("DLAP_SCE", "All", ""));
			locationList.add(new EventLocation("SLAP_SCEC", "SCEC", "SCE Core (LA Basin)"));
			locationList.add(new EventLocation("SLAP_SCEN", "SCEN", "SCE North"));
			locationList.add(new EventLocation("SLAP_SCEW", "SCEW", "SCE West"));
			locationList.add(new EventLocation("SLAP_SCHD", "SCHD", "SCE High Desert"));
			locationList.add(new EventLocation("SLAP_SCLD", "SCLD", "SCE Low Desert"));
			locationList.add(new EventLocation("SLAP_SCNW", "SCNW", "SCE Northwest"));
		}
		return locationList;
	}

	public void setLocationList(List<EventLocation> locationList) {
		this.locationList = locationList;
	}
	
}
