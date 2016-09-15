package com.akuacom.pss2.richsite.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.core.ValidatorFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.dbp.DBPProgram;
import com.akuacom.pss2.richsite.event.creation.Validator;
import com.akuacom.pss2.richsite.event.creation.Validator.MSG;
import com.akuacom.pss2.richsite.util.ParticipantShedBean;
import com.akuacom.pss2.richsite.util.ParticipantShedCalculateUtil;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.util.EventStatus;

/**
 * 
 * Filename: EventDataModel.java Description: This class is the JSF backing bean
 * for DRAS Event model Copyright: Copyright (c)2010 Company:
 * 
 * @author Yang Liu
 * @version Create at: Dec 9, 2010 5:46:39 PM
 */

public class EventDataModel implements Serializable {

	private static final long serialVersionUID = -3331438914978701582L;

	/** The log */
	private static final Logger log = Logger.getLogger(EventDataModel.class
			.getName());

	/** The EventDataModelManager instance */
	private transient EventDataModelManager manager;

	/** The respondBy time for DBP only. */
	private Date respondBy;

	/** The respondBy hour. */
	private String respondByHour;

	/** The respondBy min. */
	private String respondByMin = "0";

	/** The drasRespondBy time for DBP only */
	private Date drasRespondBy;

	/** The event name. */
	private String eventName;

	/** The program name. */
	private String programName;

	/** The issued time. */
	private Date issuedTime;

	/** The issued hour. */
	private String issuedHour;

	/** The issued min. */
	private String issuedMin = "0";

	/** The issued sec. */
	private String issuedSec = "0";

	/** The start time. */
	private Date startTime;

	/** The end time. */
	private Date endTime;

	/** The received time. */
	private Date receivedTime;

	/** The participants. */
	private List<EventParticipant> eventParticipants = new ArrayList<EventParticipant>();

	/** The manual. */
	private boolean manual;

	/** The event status. */
	private EventStatus eventStatus = EventStatus.NONE;

	/** The state. */
	private transient String state; // TODO: push this field to to web UI layer

	/** The warnings. */
	private List<ProgramValidationMessage> warnings = new ArrayList<ProgramValidationMessage>();

	/** The eventSignals. */
	private List<EventSignal> eventSignals = new ArrayList<EventSignal>();

	/** The commit. */
	private boolean commit = true;

	/** The EventParticipantDataModel. */
	private List<EventParticipantDataModel> allParticipantsInProgram = new ArrayList<EventParticipantDataModel>();

	/** The clients. */
	private List<Participant> clients = new ArrayList<Participant>();

	/** The participants. */
	private List<Participant> participants = new ArrayList<Participant>();

	/** The renew. */
	private String renew;

	/** The renewFlag. */
	private boolean renewFlag = true;

	/** The event date. */
	private Date eventDate;

	/** The start hour. */
	private String startHour;

	/** The start min. */
	private String startMin = "0";

	/** The start sec. */
	private String startSec = "0";

	/** The end hour. */
	private String endHour;

	/** The end min. */
	private String endMin = "0";

	/** The end sec. */
	private String endSec = "0";

	/** The hour list. */
	private List<SelectItem> hourList;

	/** The min list. */
	private List<SelectItem> minList;

	/** The sec list. */
	private List<SelectItem> secList;

	private String selectLabel;

	private String filterLabel;

	private String FILTER_PARTICIPANT_NAME = "Participant";

	private String FILTER_PARTICIPANT_ACCOUNT = "Account #";

	private String FILTER_PARTICIPANT_SHED = "Shed";

	private boolean selectedFilteredParticipantListCache = false;
	private boolean emptySet = false;

	private String filterContent = "";
	private String filterType = "";

	private List<String> history_filter_labels = new ArrayList<String>();
	private String history_filter_label;

	private boolean searchInResult = false;
	private String panel_filter_label;

	private String ADD_PARTICIPANT_LABEL = "Add Participants";
	private String DELETE_PARTICIPANT_LABEL = "Remove Participants";

	private int participant_count;

	private List<EventParticipantDataModel> copyAllParticipantsInProgram = new ArrayList<EventParticipantDataModel>();
	private List<EventParticipantDataModel> selectedParticipantsInProgram = new ArrayList<EventParticipantDataModel>();
	private List<EventParticipantDataModel> selectedParticipantsInProgramForRemove = new ArrayList<EventParticipantDataModel>();

	private double participantAvilableShed;

	private boolean keyword_search_flag = true;

	private boolean add_selected_flag;
	private boolean remove_selected_flag;

	private boolean slide_search_flag;
	private int slide_search_min;
	private int slide_search_max;

	private boolean inFilter = false;

	private String linkSequence;

	private double totalAvilabeShed;
	private double totalRegisterShed;

	private double maxRegisterShed;
	private ProgramHelper ph;

	private boolean participantPage;

	private boolean activateSlider;

	private boolean isDBP;

	private int duration; // length of event, in minutes, for FastDR program

	private boolean instantEvent = true;
	
	private boolean isFastDR;

	private String warnMsg;
	private boolean warnMsgConfirm;
	private String nextPageNoValidation;
	/**
	 * Constructor
	 */
	public EventDataModel() {

		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			ExternalContext ec = context.getExternalContext();
			if (ec != null) {
				HttpServletRequest request = (HttpServletRequest) ec
						.getRequest();
				if (request != null) {

					programName = request.getParameter("programName");
					if (programName != null)
						this.setProgramName(programName);
					if (this.getProgramName() != null) {
						this.setProgramName(this.getProgramName());
					} else {
						this.setProgramName("DEMO");
					}
					if (programName.startsWith("Fast DR")) {
						eventDate = new Date();
						startTime = (Date) eventDate.clone();
						Calendar cal = Calendar.getInstance();
						cal.setTime(startTime);
						startHour = cal.get(Calendar.HOUR_OF_DAY) + "";
						startMin = cal.get(Calendar.MINUTE) + "";
						startSec = cal.get(Calendar.SECOND) + "";

						String[] strings = programName.split(" ");
						try {
							duration = Integer
									.parseInt(strings[strings.length - 1]);
						} catch (NumberFormatException e) {
							duration = 60;
						}

						cal.add(Calendar.MINUTE, duration);
						endTime = cal.getTime();
					}
				}
			}
			getManager();
		}
		if (getManager().getUserEAO() != null && this.programName != null && this.programName != "") {
			ProgramManager programManager = EJBFactory.getBean(ProgramManager.class);
	        String uiScheduleEventString = programManager.getUiScheduleEventString(programName);
	        
	        if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.FastDRSchedulePage)){
                this.isFastDR=true;
	        }else{
	        	this.isFastDR=false;
	        }
		}    
		retrieveShedEnabled();
	}

	private boolean shedEnabled = true;

	private void retrieveShedEnabled() {
		SystemManager systemManager = EJB3Factory
				.getLocalBean(SystemManager.class);
		PSS2Features features = systemManager.getPss2Features();
		shedEnabled = features.isFeatureShedInfoEnabled();
	}

	public List<EventParticipantDataModel> getAllParticipantsInProgram() {
		return getAllParticipantsInProgram(null);
	}

	public boolean isParticipantPage() {
		return participantPage;
	}

	public void setParticipantPage(boolean participantPage) {
		this.participantPage = participantPage;
	}

	public List<EventParticipantDataModel> getAllParticipantsInProgram(
			Program prog) {

		/*
    */

		if (allParticipantsInProgram.size() >= 0
				&& this.selectedFilteredParticipantListCache && (this.emptySet)) {
			return allParticipantsInProgram;
		}

		try {

			if (getManager().getUserEAO() != null && this.programName != null && this.programName != "") {
				
				
				Program eventProgram = null;
				if (prog != null) {
					eventProgram = prog;
				} else {
					eventProgram = getManager().getProgramManager()
							.getProgramWithParticipantsAndPRules(programName);
				}

				ph = new ProgramHelper(eventProgram);

				Set<EventParticipant> eventParticipants = new HashSet<EventParticipant>();

				eventParticipants = getManager().getProgramManager()
						.filterEventParticipants(ph.getEventParticipants(),
								eventProgram, null);

				List<Participant> participantList = (List<Participant>) ph
						.getParentParticipants();
				allParticipantsInProgram = new ArrayList<EventParticipantDataModel>();

				for (Participant p : participantList) {
					EventParticipantDataModel model = new EventParticipantDataModel(
							p, false);
					allParticipantsInProgram.add(model);
				}

				for (int j = 0; j < allParticipantsInProgram.size(); j++) {
					EventParticipantDataModel model = allParticipantsInProgram
							.get(j);
					String participantName = model.getParticipant()
							.getParticipantName();

					if (!shedEnabled) {
						model.setParticipantRegistedShed(0);
					} else {
						if(this.isFastDR){
							buildFastDRTime();
						}
						Participant p = model.getParticipant();
						List<Participant> clientList = ph.getChildren(p.getParticipantName());
						ParticipantShedBean shedBean = new ParticipantShedBean(p,clientList,getStartTime(),getEndTime(),false);
						model.setShedBean(shedBean);
						model.setParticipantRegistedShed(shedBean.getRegistedShed());
						model.setParticipantAvilableShed(shedBean.getAvailableShed());
					}

					for (EventParticipant eventParticipant : eventParticipants) {
						if (eventParticipant.getParticipant()
								.getParticipantName().trim()
								.equals(participantName)) {
							model.setDelete(false);
							model.setNonselectable(false);
							model.setSelect(true);
							break;
						}
					}

				}

			}
			this.emptySet = true;
			this.selectedFilteredParticipantListCache = true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return allParticipantsInProgram;
	}

	// ----------------------------------------------------------------Business
	// Logic
	// Method----------------------------------------------------------------

	/**
	 * When EventDataModel instance get request from JSF presentation layer, it
	 * should transfer the attribute of allParticipantsInProgram at presentation
	 * layer into its attributes of clients and participants.
	 */
	public void confirmEventParticipantList() {
		confirmEventParticipantList(null);
	}

	/**
	 * When EventDataModel instance get request from JSF presentation layer, it
	 * should transfer the attribute of allParticipantsInProgram at presentation
	 * layer into its attributes of clients and participants.
	 */
	public List<EventParticipantDataModel> confirmEventParticipantList(Program p) {
		List<EventParticipant> eventParticipantList = new ArrayList<EventParticipant>();
		List<Participant> clientList = new ArrayList<Participant>();
		List<Participant> participantList = new ArrayList<Participant>();
		List<EventParticipantDataModel> eList = null;
		if (p != null) {
			eList = getAllParticipantsInProgram(p);
		} else {
			eList = getAllParticipantsInProgram();
		}

		for (int i = 0; i < eList.size(); i++) {
			// if(eList.get(i).isSelect()){
			EventParticipant eventParticipant = new EventParticipant();

			Participant participant = eList.get(i).getParticipant();
			participantList.add(participant);

			eventParticipant.setParticipant(participant);
			eventParticipantList.add(eventParticipant);
			List<Participant> clients = eList.get(i)
					.getClientJoinParticipants();
			for (int j = 0; j < clients.size(); j++) {
				Participant client = clients.get(j);
				clientList.add(client);

				EventParticipant eventParticipant_Client = new EventParticipant();
				eventParticipant_Client.setParticipant(client);
				eventParticipantList.add(eventParticipant_Client);
			}
			// }
		}
		this.setClients(clientList);
		this.setParticipants(participantList);
		this.setEventParticipants(eventParticipantList);
		return eList;
	}

	/**
	 * Event JSF framework confirm request for dispatch to the
	 * EventDataModelManager to handler
	 * 
	 * @return JSF Response String
	 */
	public String confirm() {
		// capture the start/end time
		this.buildStartTime();
		this.buildEndTime();
		if (this.isIsDBP())
			this.checkUnSelectedParticipants();

		return getManager().confirmDispatchLogic(this.getProgramName());
	}

	/**
	 * Event JSF framework submit request for dispatch to the
	 * EventDataModelManager to handler
	 * 
	 * @return JSF Response String
	 */
	public String submitToDB() {
		return getManager().submitToDBDispatchLogic(this.getProgramName());
	}

	public String submitFastDRToDB() {
		// capture the start time
		SystemManager sm=EJBFactory.getBean(SystemManager.class);
		boolean confirmEnabled=sm.getPss2Features().isFeatureWarnConfirmEnabled();
		if (confirmEnabled && nextPageNoValidation!=null && nextPageNoValidation.equals("false")) 
			this.warnMsgConfirm=true;
		else
			this.warnMsgConfirm=false;
		
		buildFastDRTime();
		return getManager().submitToDBDispatchLogic(this.getProgramName());
	}
	
	public void setNextPageNoValidation(String nextPage) {
		this.nextPageNoValidation=nextPage;
	}
	
	/**
	 * Event JSF framework cancel request for dispatch to the
	 * EventDataModelManager to handler
	 * 
	 * @return JSF Response String
	 */
	public String cancel() {
		return getManager().confirmCancel();
	}

	private void checkUnSelectedParticipants() {
		Iterator<EventParticipantDataModel> i = allParticipantsInProgram
				.iterator();
		while (i.hasNext()) {
			EventParticipantDataModel it = i.next();
			if (!(it.isSelect())) {
				i.remove();
			}
		}
	}

	/**
	 * Event JSF framework for get Presentation Layer request for select all
	 * participants
	 */
	public void selectedAllParticipants() {
		for (int i = 0; i < allParticipantsInProgram.size(); i++) {
			allParticipantsInProgram.get(i).setSelect(true);
		}
		this.selectedFilteredParticipantListCache = true;
		renewFlag = false;
		getAllParticipantsInProgram();
		this.selectLabel = " >> Select All";
	}

	/**
	 * Event JSF framework for get Presentation Layer request for select none
	 * participants
	 */
	public void selectedNoneParticipants() {
		for (int i = 0; i < allParticipantsInProgram.size(); i++) {
			allParticipantsInProgram.get(i).setSelect(false);
		}

		renewFlag = true;
		this.selectedFilteredParticipantListCache = true;
		this.setEmptySet(true);
		getAllParticipantsInProgram();
		this.selectLabel = " >> Select None";

	}

	/* matches any string or sub-string in the start and the middle */
	/*
	 * private boolean keySearchMatching(String k1, String k2){ if
	 * (k1.equalsIgnoreCase(k2) || k1.matches("(?i)"+k2+".+") ) return true;
	 * return false; }
	 */

	// matches any string or sub-string in the start only
	private static boolean keySearchMatching(String k1, String k2) {
		if (k1.equalsIgnoreCase(k2)
				|| k1.toLowerCase().matches("" + k2.toLowerCase() + ".+")
				|| k1.matches("" + k2 + ".+"))
			return true;
		return false;
	}

	// register shed range matching
	private boolean keySearchMatching(int k1, int min, int max) {
		if (k1 != 0) {
			if (k1 >= min && k1 <= max)
				return true;
		}
		return false;
	}

	public void filterSelectedParticipantNameAccount() {

		if (!searchInResult) {
			// this.copyAllParticipantsInProgram.clear();
			// this.allParticipantsInProgram.clear();
			this.history_filter_labels.clear();
			history_filter_label = "";
			filterLabel = "";
			this.selectedFilteredParticipantListCache = false;
			// this.emptySet = false;
			this.getAllParticipantsInProgram();
		} else {
			// enable cache
			this.selectedFilteredParticipantListCache = true;
			this.emptySet = true;
		}

		// this.inFilter = true;
		if (filterType != null || filterContent != null) {
			for (int i = 0; i < this.allParticipantsInProgram.size(); i++) {
				if ((filterType.equals(FILTER_PARTICIPANT_NAME) && keySearchMatching(
						allParticipantsInProgram.get(i).getParticipant()
								.getParticipantName(), filterContent))
						|| (filterType.equals(FILTER_PARTICIPANT_ACCOUNT) && keySearchMatching(
								allParticipantsInProgram.get(i)
										.getParticipant().getAccountNumber(),
								filterContent))

						||

						(filterType.equals(FILTER_PARTICIPANT_SHED) && keySearchMatching(
								(int) allParticipantsInProgram.get(i)
										.getParticipant().getShedPerHourKW(),
								slide_search_min, slide_search_max))

				) {
					copyAllParticipantsInProgram.add(allParticipantsInProgram
							.get(i));
				}
			}

			allParticipantsInProgram.clear();
			allParticipantsInProgram.addAll(copyAllParticipantsInProgram);
			copyAllParticipantsInProgram.clear();

			if (!searchInResult) {
				if (history_filter_labels.size() > 1) {
					if (this.filterType.equals(FILTER_PARTICIPANT_SHED)) {
						this.filterContent = "[" + slide_search_min + "-"
								+ slide_search_max + "]";
						this.filterType = FILTER_PARTICIPANT_SHED;
					} else {
						this.filterLabel += " >> " + this.filterType + ":"
								+ this.filterContent;
					}
				} else {
					if (this.filterType.equals(FILTER_PARTICIPANT_SHED)) {
						this.filterContent = "[" + slide_search_min + "-"
								+ slide_search_max + "]";
						this.filterType = FILTER_PARTICIPANT_SHED;
					}
					this.filterLabel += this.filterType + ":"
							+ this.filterContent;
				}
			} else {
				if (this.filterType.equals(FILTER_PARTICIPANT_SHED))
					this.filterContent = "[" + slide_search_min + "-"
							+ slide_search_max + "]";
				filterLabel = this.filterType + ":" + this.filterContent;
			}

		}

		this.history_filter_labels.add(this.filterLabel);
		history_filter_label = "";
		for (Iterator<String> i = history_filter_labels.iterator(); i.hasNext();) {
			history_filter_label += i.next() + " >>";
		}

		this.setHistory_filter_label(history_filter_label);

		// renewFlag=true;
		this.selectedFilteredParticipantListCache = true;
		this.setAllParticipantsInProgram(allParticipantsInProgram);
		this.emptySet = true;
		this.setParticipant_count(participant_count);
		// this.inFilter = false;
		this.linkSequence = null;

	}

	public void historySearchAction() {

		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext()
				.getRequestParameterMap();

		this.linkSequence = params.get("linkSequence");
		String[] crums = this.getLinkSequence().split(":");

		for (int i = 0; i <= crums.length; i++) {
			if (i == 0)
				this.setFilterType(crums[i]);
			if (i == 1)
				this.setFilterContent(crums[i]);
		}

		this.selectedFilteredParticipantListCache = false;
		this.filterSelectedParticipantNameAccount();
	}

	public void clearParticipantsFilterSelection() {
		filterLabel = "";
		selectLabel = "";
		filterContent = " ";
		filterType = FILTER_PARTICIPANT_NAME;
		history_filter_label = "";
		history_filter_labels.clear();
		this.selectedParticipantsInProgram.clear();
		this.slide_search_min = 0;
		this.slide_search_max = 0;
		this.searchInResult = false;

		this.setSlide_search_max(0);
		this.setSlide_search_min(0);
		this.setFilterType(FILTER_PARTICIPANT_NAME);
		this.setFilterContent("");

		// get a DB copy of participant list
		this.selectedFilteredParticipantListCache = false;
		this.emptySet = false;

		getAllParticipantsInProgram();
	}

	public void closeModal() {
		filterLabel = "";
		selectLabel = "";
		filterContent = " ";
		filterType = FILTER_PARTICIPANT_NAME;
		history_filter_label = "";
		history_filter_labels.clear();
		this.slide_search_min = 0;
		this.slide_search_max = 0;
		this.searchInResult = false;

		this.setSlide_search_max(0);
		this.setSlide_search_min(0);
		this.setFilterType(FILTER_PARTICIPANT_NAME);
		this.setFilterContent("");

		// get a DB copy of participant list
		// this.selectedParticipantsInProgram.clear();

		if (selectedParticipantsInProgram.size() > 0 && add_selected_flag) {

			this.allParticipantsInProgram = selectedParticipantsInProgram;
			this.setAllParticipantsInProgram(selectedParticipantsInProgram);
			this.selectedFilteredParticipantListCache = true;
			this.emptySet = true;
			return;
		}
		if (selectedParticipantsInProgramForRemove.size() > 0
				&& remove_selected_flag) {
			this.allParticipantsInProgram = selectedParticipantsInProgramForRemove;
			this.setAllParticipantsInProgram(selectedParticipantsInProgramForRemove);
			this.selectedFilteredParticipantListCache = true;
			this.emptySet = true;
			return;
		} else {
			this.selectedFilteredParticipantListCache = false;
			this.emptySet = false;
			return;
		}

	}

	public void removeAllParticipantsAction() {

		Iterator<EventParticipantDataModel> i = selectedParticipantsInProgramForRemove
				.iterator();
		while (i.hasNext()) {
			EventParticipantDataModel it = i.next();
			i.remove();
		}

		this.selectedFilteredParticipantListCache = true;
		this.setAllParticipantsInProgram(selectedParticipantsInProgramForRemove);
		this.allParticipantsInProgram = selectedParticipantsInProgramForRemove;

	}

	public void restoreRemoveAllParticipantsAction() {

		List<EventParticipantDataModel> tempRemove = new ArrayList<EventParticipantDataModel>();
		for (EventParticipantDataModel from : selectedParticipantsInProgramForRemove) {
			tempRemove.add(from);
		}
		this.selectedFilteredParticipantListCache = true;
		this.setAllParticipantsInProgram(tempRemove);

	}

	public void removeParticipantsAction() {

		// get another remove container
		List<EventParticipantDataModel> tempRemove = new ArrayList<EventParticipantDataModel>();
		for (EventParticipantDataModel from : selectedParticipantsInProgramForRemove) {
			tempRemove.add(from);
		}

		// check for un selected items
		Iterator<EventParticipantDataModel> i = allParticipantsInProgram
				.iterator();
		while (i.hasNext()) {
			EventParticipantDataModel it = i.next();
			if (!(it.isSelect())) {
				i.remove();
			}
		}

		// remove operatrion
		tempRemove.removeAll(allParticipantsInProgram);
		selectedParticipantsInProgramForRemove
				.removeAll(allParticipantsInProgram);
		for (EventParticipantDataModel f2 : allParticipantsInProgram) {
			tempRemove.remove(f2);
		}

		// enable cashe so it would reflect in the screen
		this.selectedFilteredParticipantListCache = true;
		allParticipantsInProgram = null;
		this.setAllParticipantsInProgram(tempRemove);
		allParticipantsInProgram = tempRemove;
		this.selectedParticipantsInProgram.clear();
		this.selectedParticipantsInProgram.addAll(allParticipantsInProgram);

	}

	public void removeSelectedParticipants() {

		this.panel_filter_label = DELETE_PARTICIPANT_LABEL;
		this.setPanel_filter_label(DELETE_PARTICIPANT_LABEL);
		this.add_selected_flag = false;
		this.remove_selected_flag = true;
		this.history_filter_label = "";
		history_filter_labels.clear();

		List<EventParticipantDataModel> selectedList = allParticipantsInProgram;

		// reserve the selected list
		List<EventParticipantDataModel> tempSelectedList = new ArrayList<EventParticipantDataModel>();
		tempSelectedList.clear();
		tempSelectedList.addAll(selectedList);
		selectedParticipantsInProgramForRemove.clear();
		for (EventParticipantDataModel s : tempSelectedList) {
			selectedParticipantsInProgramForRemove.add(s);
		}

	}

	public void addParticipants() {

		history_filter_labels.clear();
		this.selectedFilteredParticipantListCache = true;
		List<EventParticipantDataModel> selectedList = allParticipantsInProgram;

		// reserve the selected list
		List<EventParticipantDataModel> tempSelectedList = new ArrayList<EventParticipantDataModel>();
		tempSelectedList.clear();
		tempSelectedList.addAll(selectedList);
		selectedParticipantsInProgram.clear();
		for (EventParticipantDataModel s : tempSelectedList) {
			selectedParticipantsInProgram.add(s);
		}

		// get outjoin of selected list
		this.selectedFilteredParticipantListCache = false;
		List<EventParticipantDataModel> epList = getAllParticipantsInProgram();

		// check for the first time reserve list
		if (selectedParticipantsInProgram.size() == epList.size())
			selectedParticipantsInProgram.clear();

		List<EventParticipantDataModel> fullList = epList;

		List<EventParticipantDataModel> fullList2 = new ArrayList<EventParticipantDataModel>();

		for (EventParticipantDataModel f : fullList) {
			fullList2.add(f);
		}
		boolean f;
		this.allParticipantsInProgram.clear();

		for (EventParticipantDataModel t1 : fullList2) {
			f = false;

			for (EventParticipantDataModel t2 : selectedList) {
				if (t1.getParticipant().getUUID()
						.equalsIgnoreCase(t2.getParticipant().getUUID())) {
					f = true;
					break;
				}
			}
			if (!f) {
				this.allParticipantsInProgram.add(t1);
			}
		}

		if (allParticipantsInProgram.size() == 0)
			this.selectedFilteredParticipantListCache = false;
		else
			// enable cashe so it would reflect in the screen
			this.selectedFilteredParticipantListCache = true;

		// this.emptySet = false;
		this.setAllParticipantsInProgram(allParticipantsInProgram);

		this.searchInResult = false;
		this.panel_filter_label = ADD_PARTICIPANT_LABEL;
		this.setPanel_filter_label(ADD_PARTICIPANT_LABEL);
		this.add_selected_flag = true;
		this.setAdd_selected_flag(true);
		this.remove_selected_flag = false;
		this.history_filter_label = "";

	}

	public void addSelectedParticipants() {

		// check for un selected items
		Iterator<EventParticipantDataModel> i = allParticipantsInProgram
				.iterator();
		while (i.hasNext()) {
			EventParticipantDataModel it = i.next();
			if (!(it.isSelect())) {
				i.remove();
			}
		}

		// merge the new selection from current to orginal
		for (EventParticipantDataModel s : selectedParticipantsInProgram) {
			this.allParticipantsInProgram.add(s);
		}

		// enable cashe so it would reflect in the screen
		this.selectedFilteredParticipantListCache = true;
		this.setAllParticipantsInProgram(allParticipantsInProgram);
		this.selectedParticipantsInProgram.clear();
		this.selectedParticipantsInProgram.addAll(allParticipantsInProgram);

	}

	public void deleteSelectedParticipants() {

		this.panel_filter_label = DELETE_PARTICIPANT_LABEL;
		allParticipantsInProgram = this.getAllParticipantsInProgram();

	}

	public void selectedParticipantsAddAction() {
		this.panel_filter_label = ADD_PARTICIPANT_LABEL;
		this.setPanel_filter_label(ADD_PARTICIPANT_LABEL);
		selectedFilteredParticipantListCache = false;
		this.getAllParticipantsInProgram();
	}

	public void buildStartTime() {
		if (this.eventDate != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(eventDate);
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getStartHour()));
			calendar.set(Calendar.MINUTE, Integer.parseInt(getStartMin()));
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			this.startTime = calendar.getTime();
		}
	}

	public void buildEndTime() {
		if (this.eventDate != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(eventDate);
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getEndHour()));
			calendar.set(Calendar.MINUTE, Integer.parseInt(getEndMin()));
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			this.endTime = calendar.getTime();
		}
	}

	public void buildIssuedTime() {
		if (this.issuedTime != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(issuedTime);
			calendar.set(Calendar.HOUR_OF_DAY,
					Integer.parseInt(getIssuedHour()));
			calendar.set(Calendar.MINUTE, Integer.parseInt(getIssuedMin()));
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			this.issuedTime = calendar.getTime();
		}
	}

	public void buildRespondByTime() {
		if (this.respondBy != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(respondBy);
			calendar.set(Calendar.HOUR_OF_DAY,
					Integer.parseInt(getRespondByHour()));
			calendar.set(Calendar.MINUTE, Integer.parseInt(getRespondByMin()));
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			this.respondBy = calendar.getTime();
		}
	}

	public void buildFastDRTime() {
		final Calendar calendar = Calendar.getInstance();
		this.issuedTime = calendar.getTime();
		if (this.instantEvent) {
			// generate event start time as now.
			this.startTime = calendar.getTime();
		} else {
			// generate event by specified value.
			calendar.setTime(eventDate);
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getStartHour()));
			calendar.set(Calendar.MINUTE, Integer.parseInt(getStartMin()));
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			this.startTime = calendar.getTime();
		}
		calendar.add(Calendar.MINUTE, this.duration);
		this.endTime = calendar.getTime();

	}

	// ------------------------------------------------------------------Getter
	// and Setter--------------------------------------------------------------

	public String getEventName() {
		return eventName;
	}

	public Date getRespondBy() {
		return respondBy;
	}

	public void setRespondBy(Date respondBy) {
		this.respondBy = respondBy;
	}

	public Date getDrasRespondBy() {
		return drasRespondBy;
	}

	public void setDrasRespondBy(Date drasRespondBy) {
		this.drasRespondBy = drasRespondBy;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public Date getIssuedTime() {
		return issuedTime;
	}

	public void setIssuedTime(Date issuedTime) {
		this.issuedTime = issuedTime;
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

	public Date getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(Date receivedTime) {
		this.receivedTime = receivedTime;
	}

	public List<EventParticipant> getEventParticipants() {
		return eventParticipants;
	}

	public void setEventParticipants(List<EventParticipant> eventParticipants) {
		this.eventParticipants = eventParticipants;
	}

	public boolean isManual() {
		return manual;
	}

	public void setManual(boolean manual) {
		this.manual = manual;
	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<ProgramValidationMessage> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<ProgramValidationMessage> warnings) {
		this.warnings = warnings;
	}

	public List<EventSignal> getEventSignals() {
		return eventSignals;
	}

	public void setEventSignals(List<EventSignal> eventSignals) {
		this.eventSignals = eventSignals;
	}

	public boolean isCommit() {
		return commit;
	}

	public void setCommit(boolean commit) {
		this.commit = commit;
	}

	public void setAllParticipantsInProgram(
			List<EventParticipantDataModel> allParticipantsInProgram) {
		this.allParticipantsInProgram = allParticipantsInProgram;
	}

	public List<Participant> getClients() {
		return clients;
	}

	public void setClients(List<Participant> clients) {
		this.clients = clients;
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	public EventDataModelManager getManager() {
		if (manager == null) {
			manager = new EventDataModelManagerImpl(this);
		}
		return manager;
	}

	public void setManager(EventDataModelManager manager) {
		this.manager = manager;
	}

	public String getRenew() {
		if (!renewFlag) {
			renewFlag = true;
			return renew;
		}
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			ExternalContext ec = context.getExternalContext();
			if (ec != null) {
				HttpServletRequest request = (HttpServletRequest) ec
						.getRequest();
				if (request != null) {
					String programName = request.getParameter("programName");
					if (programName == null || programName == "") {
						return renew;
					}
				}
			}
		}
		EventDataModel model = new EventDataModel();
		getManager().addSessionCache(model);
		return renew;
	}

	public void setRenew(String renew) {
		this.renew = renew;
	}

	public boolean isRenewFlag() {
		return renewFlag;
	}

	public void setRenewFlag(boolean renewFlag) {
		this.renewFlag = renewFlag;
	}

	public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		XMLGregorianCalendar xmlCal;
		try {
			xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			return xmlCal;
		} catch (DatatypeConfigurationException e) {
			return null;
		}
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public void setEventDate() {
		this.eventDate = eventDate;
	}

	public String getStartHour() {
		return startHour;
	}

	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}

	public String getStartMin() {
		return startMin;
	}

	public void setStartMin(String startMin) {
		this.startMin = startMin;
	}

	public String getStartSec() {
		return startSec;
	}

	public void setStartSec(String startSec) {
		this.startSec = startSec;
	}

	public String getEndHour() {
		return endHour;
	}

	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}

	public String getEndMin() {
		return endMin;
	}

	public void setEndMin(String endMin) {
		this.endMin = endMin;
	}

	public String getEndSec() {
		return endSec;
	}

	public void setEndSec(String endSec) {
		this.endSec = endSec;
	}

	public List<SelectItem> getHourList() {
		if (hourList == null) {
			hourList = new ArrayList<SelectItem>();
			for (int i = 0; i < 24; i++) {
				hourList.add(new SelectItem(String.valueOf(i), String
						.valueOf(i)));
			}
		}
		return hourList;
	}

	public List<SelectItem> getMinList() {
		if (minList == null) {
			minList = new ArrayList<SelectItem>();
			for (int i = 0; i < 60; i++) {
				minList.add(new SelectItem(String.valueOf(i), String.valueOf(i)));
			}
		}
		return minList;
	}

	public List<SelectItem> getSecList() {
		if (secList == null) {
			secList = new ArrayList<SelectItem>();
			for (int i = 0; i < 60; i++) {
				secList.add(new SelectItem(String.valueOf(i), String.valueOf(i)));
			}
		}
		return secList;
	}

	public void setHourList(List<SelectItem> hourList) {
		this.hourList = hourList;
	}

	public void setMinList(List<SelectItem> minList) {
		this.minList = minList;
	}

	public void setSecList(List<SelectItem> secList) {
		this.secList = secList;
	}

	public String getRespondByHour() {
		return respondByHour;
	}

	public void setRespondByHour(String respondByHour) {
		this.respondByHour = respondByHour;
	}

	public String getRespondByMin() {
		return respondByMin;
	}

	public void setRespondByMin(String respondByMin) {
		this.respondByMin = respondByMin;
	}

	public String getIssuedHour() {
		return issuedHour;
	}

	public void setIssuedHour(String issuedHour) {
		this.issuedHour = issuedHour;
	}

	public String getIssuedMin() {
		return issuedMin;
	}

	public void setIssuedMin(String issuedMin) {
		this.issuedMin = issuedMin;
	}

	public String getIssuedSec() {
		return issuedSec;
	}

	public void setIssuedSec(String issuedSec) {
		this.issuedSec = issuedSec;
	}

	public String getFilterLabel() {
		return filterLabel;
	}

	public void setFilterLabel(String filterLabel) {
		this.filterLabel = filterLabel;
	}

	public String getSelectLabel() {
		return selectLabel;
	}

	public void setSelectLabel(String selectLabel) {
		this.selectLabel = selectLabel;
	}

	public String getFilterContent() {
		return filterContent;
	}

	public void setFilterContent(String filterContent) {
		this.filterContent = filterContent;
	}

	public String getFilterType() {
		if (filterType.equalsIgnoreCase("shed")) {
			this.keyword_search_flag = false;
			this.slide_search_flag = true;
		} else {
			this.keyword_search_flag = true;
			this.slide_search_flag = false;
		}

		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public boolean isSearchInResult() {
		return searchInResult;
	}

	public void setSearchInResult(boolean searchInResult) {
		this.searchInResult = searchInResult;
	}

	public List<String> getHistory_filter_labels() {
		return history_filter_labels;
	}

	public void setHistory_filter_labels(List<String> history_filter_labels) {
		this.history_filter_labels = history_filter_labels;
	}

	public String getHistory_filter_label() {
		return history_filter_label;
	}

	public void setHistory_filter_label(String history_filter_label) {
		this.history_filter_label = history_filter_label;
	}

	public String getPanel_filter_label() {
		if (this.add_selected_flag)
			return this.ADD_PARTICIPANT_LABEL;
		else
			return this.DELETE_PARTICIPANT_LABEL;
	}

	public void setPanel_filter_label(String panel_filter_label) {
		this.panel_filter_label = panel_filter_label;
	}

	public int getParticipant_count() {
		participant_count = this.allParticipantsInProgram.size();
		return participant_count;
	}

	public void setParticipant_count(int participant_count) {
		this.participant_count = participant_count;
	}

	public double getParticipantAvilableShed(Participant p, ProgramHelper ph) {
		participantAvilableShed = 0.0;
		if (p != null) {
			List<Participant> clientList = ph.getChildren(p.getParticipantName());
			int c = 0;
			for (Participant client : clientList) {
				if (client.getStatus() == 0)
					c++;
			}
			try {
				if (!Double.isNaN(p.getShedPerHourKW())
						&& (clientList.size() > 0))
					participantAvilableShed = (((double) p.getShedPerHourKW() / clientList
							.size()) * c);
			} catch (Exception e) {
				participantAvilableShed = 0.0;
			}
		}
		this.setParticipantAvilableShed(participantAvilableShed);
		return participantAvilableShed;
	}
	public double getParticipantAvilableShed(Participant p) {
		participantAvilableShed = 0.0;
		if (p != null) {
			List<Participant> clientList = getManager().getUserEAO()
					.findClientsByParticipant(p.getParticipantName());
			int c = 0;
			for (Participant client : clientList) {
				if (client.getStatus() == 0)
					c++;
			}
			try {
				if (!Double.isNaN(p.getShedPerHourKW())
						&& (clientList.size() > 0))
					participantAvilableShed = (((double) p.getShedPerHourKW() / clientList
							.size()) * c);
			} catch (Exception e) {
				participantAvilableShed = 0.0;
			}
		}
		this.setParticipantAvilableShed(participantAvilableShed);
		return participantAvilableShed;
	}

	public double getParticipantAvilableShed() {
		return participantAvilableShed;
	}

	public void setParticipantAvilableShed(double ParticipantAvilableShed) {
		this.participantAvilableShed = ParticipantAvilableShed;
	}

	public boolean isKeyword_search_flag() {
		return keyword_search_flag;
	}

	public void setKeyword_search_flag(boolean keyword_search_flag) {
		this.keyword_search_flag = keyword_search_flag;
	}

	public boolean isSlide_search_flag() {
		return slide_search_flag;
	}

	public void setSlide_search_flag(boolean slide_search_flag) {
		this.slide_search_flag = slide_search_flag;
	}

	public int getSlide_search_max() {
		return slide_search_max;
	}

	public void setSlide_search_max(int slide_search_max) {
		this.slide_search_max = slide_search_max;
	}

	public int getSlide_search_min() {
		return slide_search_min;
	}

	public void setSlide_search_min(int slide_search_min) {
		this.slide_search_min = slide_search_min;
	}

	public boolean isAdd_selected_flag() {
		return add_selected_flag;
	}

	public void setAdd_selected_flag(boolean add_selected_flag) {
		this.add_selected_flag = add_selected_flag;
	}

	public boolean isRemove_selected_flag() {
		return remove_selected_flag;
	}

	public void setRemove_selected_flag(boolean remove_selected_flag) {
		this.remove_selected_flag = remove_selected_flag;
	}

	public boolean isInFilter() {
		return inFilter;
	}

	public void setInFilter(boolean inFilter) {
		this.inFilter = inFilter;
	}

	public String getLinkSequence() {
		return linkSequence;
	}

	public void setLinkSequence(String linkSequence) {
		this.linkSequence = linkSequence;
	}

	public double getTotalAvilabeShed() {
		totalAvilabeShed = 0;
		if (!shedEnabled) {
			return totalAvilabeShed;
		} else {
			if(this.isFastDR){
				buildFastDRTime();
			}
				
			for (int j = 0; j < allParticipantsInProgram.size(); j++) {
				EventParticipantDataModel model = allParticipantsInProgram.get(j);					
//				totalAvilabeShed += this.getParticipantAvilableShed(model.getParticipant());
				if(model.isSelect()){
					if(model.getShedBean()!=null){
						model.getShedBean().setStartTime(startTime);
						model.getShedBean().setEndTime(endTime);
						model.getShedBean().calculateAvailableShed();
					}
					double shed = model.getParticipantAvilableShed();
					if(Double.isNaN(shed)){
						shed=0;
					}else{
						totalAvilabeShed+=shed;	
					}
				}
			}
		}
		return totalAvilabeShed;
	}

	public void setTotalAvilabeShed(double totalAvilabeShed) {
		this.totalAvilabeShed = totalAvilabeShed;
	}

	public double getTotalRegisterShed() {
		totalRegisterShed = 0;
		if (!shedEnabled) {
			return totalRegisterShed;
		} else {
			if(this.isFastDR){
				buildFastDRTime();
			}
			for (int j = 0; j < allParticipantsInProgram.size(); j++) {
				EventParticipantDataModel model = allParticipantsInProgram.get(j);
				if(model.isSelect()){
					if(model.getShedBean()!=null){
						model.getShedBean().setStartTime(startTime);
						model.getShedBean().setEndTime(endTime);
						model.getShedBean().calculateRegistedShed();
					}
					double shed = model.getParticipantRegistedShed();
					if(Double.isNaN(shed)){
						shed=0;
					}else{
						totalRegisterShed+=shed;	
					}
				}
			}
		}
		return totalRegisterShed;
	}

	public void setTotalRegisterShed(double totalRegisterShed) {
		this.totalRegisterShed = totalRegisterShed;
	}

	public double getMaxRegisterShed() {
		maxRegisterShed = 0;
		for (Participant p : (List<Participant>) ph.getParentParticipants()) {
			if (p != null)
				if (p.getShedPerHourKW() > maxRegisterShed)
					maxRegisterShed = p.getShedPerHourKW();
		}
		if (maxRegisterShed == 0)
			maxRegisterShed = 0.01;

		return maxRegisterShed;
	}

	public void setMaxRegisterShed(double maxRegisterShed) {
		this.maxRegisterShed = maxRegisterShed;
	}

	public boolean isEmptySet() {
		return emptySet;
	}

	public void setEmptySet(boolean emptySet) {
		this.emptySet = emptySet;
	}

	public boolean isActivateSlider() {
		if (Double.isNaN(maxRegisterShed) || maxRegisterShed == 0.01)
			this.activateSlider = false;
		else
			this.activateSlider = true;
		return activateSlider;
	}

	public void setActivateSlider(boolean activateSlider) {
		this.activateSlider = activateSlider;
	}

	public boolean isIsDBP() {
		ProgramManager programManager = EJB3Factory
				.getLocalBean(ProgramManager.class);
		if (programManager.getProgramOnly(this.getProgramName()) instanceof DBPProgram)
			isDBP = true;
		else
			isDBP = false;
		return isDBP;
	}

	public void setIsDBP(boolean isDBP) {
		this.isDBP = isDBP;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isInstantEvent() {
		return instantEvent;
	}

	public void setInstantEvent(boolean instantEvent) {
		this.instantEvent = instantEvent;
	}

	/**
	 * @return the isFastDR
	 */
	public boolean isFastDR() {
		return isFastDR;
	}

	/**
	 * @param isFastDR the isFastDR to set
	 */
	public void setFastDR(boolean isFastDR) {
		this.isFastDR = isFastDR;
	}

	
	public String getWarnMsg() {
		return warnMsg;
	}

	public void setWarnMsg(String warnMsg) {
		this.warnMsg = warnMsg;
	}

	public boolean isWarnMsgConfirm() {
		return warnMsgConfirm;
	}

	public void setWarnMsgConfirm(boolean confirm) {
		this.warnMsgConfirm = confirm;
	}

	public void durationChangeAction() {
		if(this.isFastDR){
			reCalculateShed();
		}
	}
	
	public void reCalculateShed(){
		for (int j = 0; j < allParticipantsInProgram.size(); j++) {
			EventParticipantDataModel model = allParticipantsInProgram.get(j);
			if (!shedEnabled) {
				model.setParticipantRegistedShed(0);
			} else {
				if(this.isFastDR){
					buildFastDRTime();
				}
				ParticipantShedBean bean = model.getShedBean();
				bean.setStartTime(getStartTime());
				bean.setEndTime(getEndTime());
				bean.calculateRegistedShed();
				bean.calculateAvailableShed();
				model.setParticipantRegistedShed(bean.getRegistedShed());
				model.setParticipantAvilableShed(bean.getAvailableShed());
				model.setShedBean(bean);
			}
		}
	}
}
