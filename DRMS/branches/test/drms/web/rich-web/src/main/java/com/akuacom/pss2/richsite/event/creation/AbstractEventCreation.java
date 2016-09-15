package com.akuacom.pss2.richsite.event.creation;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.akuacom.jsf.model.LazyListContentProvider;
import com.akuacom.jsf.model.TreeContentProvider;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.query.EventEnrollingItem;
import com.akuacom.pss2.query.EvtClientCandidate;
import com.akuacom.pss2.query.EvtParticipantCandidate;
import com.akuacom.pss2.query.ParticipantUtils;
import com.akuacom.pss2.richsite.FDUtils;
import com.akuacom.pss2.richsite.event.creation.Validator.MSG;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.EventUtil;

public abstract class AbstractEventCreation implements EvtCreation,Serializable {
	private static final long serialVersionUID = -7608034909996881515L;

	private static final Logger log = Logger.getLogger(AbstractEventCreation.class.getName());
	
	private Wizard wizard;
	
	private String programName;
	
	protected String eventName;
	
	private boolean spanDays=false;
	
	private Map<String,Validator> validators;
	
	private String warnings;
	private boolean confirm=false;
	
	private boolean warnConfirmEnabled;
	
	private EventEnrollment enrollment;
	
	private EventStatus eventStatus = EventStatus.NONE;
	
	transient private ProgramManager programManager;
	
	transient private Program program;
	
	/** sub class to override to provide a different wizard for event creation **/
	abstract protected  Wizard createWizard();
	
	abstract protected void installValidators();
	
	abstract protected EventEnrollment createEventEnrollment();
		 
	public abstract Date getIssueTime();
	
	public abstract Date getStartTime();
	
	public abstract Date getEndTime();
	
	
	public String getProgramName() {
		return programName;
	}
	
	public String getEventName(){
		if(eventName==null){
			eventName=EventUtil.getEventName();
		}
		return eventName;
	}
	
	public AbstractEventCreation(String programName){
		this.programName = programName;
		
		SystemManager sm=EJBFactory.getBean(SystemManager.class);
		this.warnConfirmEnabled=sm.getPss2Features().isFeatureWarnConfirmEnabled();
		installValidators();
	}
	
	public EventEnrollment getEventEnrollment(){
		if(enrollment==null){
			enrollment= this.createEventEnrollment();
		}
		return enrollment;
	}
	
	public String getActivePage() {
		return getWizard().getActivePage();
	}

	public void setActivePage(String activePage) {
		this.getWizard().setActivePage(activePage);
	}
	
	public Wizard getWizard(){
		if(this.wizard==null)
			wizard = createWizard();
		return wizard;
	}
	
	public Map<String,Validator> getValidators(){
		if(validators==null){
			validators = new HashMap<String,Validator>();
		}
		return validators;
	}
	
	public void nextPage() {
		Validator val = getValidators().get(getActivePage());
		if(val!=null){
			MSG msg=val.validate(this);
			report(msg);
			if(msg!=null && (msg.type==Validator.MSG_ERROR || this.confirm))
				return;
		}
		getWizard().nextPage();
	}
	
	protected void doCreateEvent(){
		Event event = converToEvent(true);
		//DRMS-8389
		if("DBP DA".equalsIgnoreCase(event.getProgramName())){
			event.getEventParticipants().clear();
		}
		final EventManager eventManager = EJBFactory.getBean(EventManager.class);
		eventManager.createEvent(event.getProgramName(), event);
	}
	
	public ProgramManager getProgramManager(){
		if(programManager==null){
			programManager=EJBFactory.getBean(ProgramManager.class);
		}
		return programManager;
	}
	
	
	public Program getProgram() {
		if(program==null){
			 program =getProgramManager().getProgramOnly(getProgramName());
		}
		return program;
	}
	
	public Event converToEvent(boolean withParticipants) {
		ProgramEJB programEJB = getProgramManager().lookupProgramBean(getProgram());
		
		Event event=programEJB.newProgramEvent();
		if (event instanceof DBPEvent)
			event.setDrEvent(true);
		
		event.setEndTime(this.getEndTime());
		event.setEventName(getEventName());
		
		event.setEventStatus(getEventStatus());
		event.setManual(true);
		event.setProgramName(getProgramName());
		event.setReceivedTime(new Date());
		event.setStartTime(getStartTime());
		
		if(this.getIssueTime()==null)
			event.setIssuedTime(new Date());
		else
			event.setIssuedTime(getIssueTime());
			
		if(withParticipants){
			this.populateEventParticipants(event);
		}
		//event.setState(model.getState());
		//event.setWarnings(model.getWarnings());
		return event;
	}
	
	public Event getEvent() {
		Event event = new Event();
		event.setStartTime(getStartTime());
		event.setEndTime(getEndTime());
		return event;
	}
	
	public void goToPage(String page){
		Validator val = getValidators().get(getActivePage());
		if(val!=null){
			MSG msg=val.validate(this);
			report(msg);
			if(msg!=null && (msg.type==Validator.MSG_ERROR || this.confirm))
				return;
		}
		getWizard().goToPage(page);
	}
	
	protected void registerValidator(String page,Validator validator){
		getValidators().put(page, validator);
	}
	
	protected void report(MSG msg){
		if(msg!=null){
			switch(msg.type){
			case Validator.MSG_ERROR:
				FDUtils.addMsgError(msg.body);
				break;
			case Validator.MSG_INFO:
				FDUtils.addMsgInfo(msg.body);
				break;
			case Validator.MSG_WARN:
				if (warnConfirmEnabled) {
					warnings=msg.body;
					confirm=true;
				} else {
					FDUtils.addMsgWarn(msg.body);
				}
				break;
			}
		}
	}
	
	public void backPage() {
		getWizard().backPage();
	}
	
	
	public String createEvent(){
		try{
			Validator val = getValidators().get(getActivePage());
			if(val!=null){
				MSG msg=val.validate(this);
				report(msg);
				if(msg!=null && msg.type==Validator.MSG_ERROR)
					return "failure";
			}
			doCreateEvent();
			return goToEventDisplayListPage();
		}catch(Exception e){
			final String s = ErrorUtil.getErrorMessage(e);
			FDUtils.addMsgError(s);
			log.error("pss2.event.create.creationError: " + s);
			return "success";
		}
	}
	
	public Set<EvtParticipantCandidate> getAllEventParticipant(){
		Set<EvtParticipantCandidate> participants = new HashSet<EvtParticipantCandidate>();
		for(EventEnrollingItem item:getEventEnrollment().getEnrollmentItems()){
			participants.addAll(item.getLegibleEventParticipants());
		}
		return participants;
	}
	
	
	protected void populateEventParticipants(Event event){
		for(EvtParticipantCandidate candidate:getAllEventParticipant()){
			ParticipantUtils.createEventParticipantForEvent(event, candidate);
		}
	}
	/** for jsf table components **/
	public TreeContentProvider<EvtParticipantCandidate> getSelectedParticipants(){
		return new LazyListContentProvider<EvtParticipantCandidate>(){
			private static final long serialVersionUID = -1859578720476173504L;

			@Override
			public List<EvtParticipantCandidate> doGetContents() {
				return new ArrayList<EvtParticipantCandidate>(getAllEventParticipant());
			}
		};
	}
	
	/** for jsf table components **/
	public TreeContentProvider<EvtClientCandidate> getSelectedClients(){
		return new LazyListContentProvider<EvtClientCandidate>(){
			private static final long serialVersionUID = 3934188275733232230L;

			@Override
			public List<EvtClientCandidate> doGetContents() {
				List<EvtClientCandidate> clients =new ArrayList<EvtClientCandidate>();
				for(EvtParticipantCandidate p:getAllEventParticipant()){
					clients.addAll(p.getClients());
				}
				return clients;
			}
		};
	}
	
	public String goToEventDisplayListPage(){
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec=context.getExternalContext();
		try{
			String eventURL = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("eventURL");
			if("".equalsIgnoreCase(eventURL)||(eventURL==null)){
				ec.redirect(getEventListPage());
			}else{
				ec.redirect(eventURL);
			}
		}catch(IOException e){
			return "failure";
		}
		context.responseComplete();			
		return "parent";
	}
	
	public String cancel(){
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec=context.getExternalContext();
		try{
			
			String cancelURL = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cancelURL");
			if("".equalsIgnoreCase(cancelURL)||(cancelURL==null)){
				ec.redirect(getProgramPage());
			}else{
				ec.redirect(cancelURL);
			}
		}catch(IOException e){
			return "failure";
		}
		context.responseComplete();
		return "success";
	}
	
	@Override
	public String getEventListPage() {
		return BASE_EVT_LIST_PAGE;
	}

	@Override
	public String getProgramPage() {
		return BASE_PROGRAM_PAGE;
	}

	public String getWarnings() {
		return warnings;
	}

	public boolean isConfirm() {
		return confirm;
	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}
	
	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}
	
	public boolean isSpanDays() {
		return spanDays;
	}

	public void setSpanDays(boolean spanDays) {
		this.spanDays = spanDays;
	}

	//TODO
	public UploadFile getUploadFile() {
		return new UploadFile(null);
	}
	
	//TODO remove this
	public List<EventLocation> getLocationList() {
		return Collections.emptyList();
	}
	//TODO 
	public boolean isUploadEnable(){
		return false;
	}
}
