package com.akuacom.pss2.richsite.event.demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import org.openadr.dras.eventinfo.EventInfoInstance.Values;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventTiming;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.demo.DemoProgramEJB;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.richsite.event.EventDataModel;
import com.akuacom.pss2.richsite.event.EventDataModelManager;
import com.akuacom.pss2.richsite.event.EventParticipantDataModel;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.util.EventUtil;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.pss2.web.event.DemoEvent;
import com.akuacom.pss2.web.event.JSFDemoEventInfo;
import com.kanaeki.firelog.util.FireLogEntry;

import java.util.Iterator;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletRequest;

public class JSFDemoEvent extends EventDataModel implements Serializable{

	private static final long serialVersionUID = 9153245681212907013L;
	private static final Logger log = Logger.getLogger(JSFDemoEvent.class.getName());
	
	private String eventID;
      /** The EventParticipantDataModel. */
	private List<EventParticipantDataModel> allParticipantsInProgram = new ArrayList<EventParticipantDataModel>();

        private String selectLabel;

    private String filterLabel;

    private String FILTER_PARTICIPANT_NAME = "Participant";

    private String FILTER_PARTICIPANT_ACCOUNT = "Account #";

    private String FILTER_PARTICIPANT_SHED = "Shed";

    private boolean selectedFilteredParticipantListCache = true;

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
    private List<EventParticipantDataModel> tempList = new ArrayList<EventParticipantDataModel>();
    private List<EventParticipantDataModel> selectedParticipantsInProgram = new ArrayList<EventParticipantDataModel>();
    private List<EventParticipantDataModel> selectedParticipantsInProgramForRemove = new ArrayList<EventParticipantDataModel>();

    private double participantAvilableShed;

    private boolean keyword_search_flag = true;

    private boolean slide_search_flag;

    private boolean add_selected_flag;
    private boolean remove_selected_flag;

    private int slide_search_min;
    private int slide_search_max;

    /** The renewFlag. */
	private boolean renewFlag = true;
    /** The renew. */
	private String renew;

    private double totalAvilabeShed;
    private double totalRegisterShed;

    private double maxRegisterShed;

    private boolean activateSlider;


	private DemoEvent delegation = new DemoEvent(false){

		private static final long serialVersionUID = 195823013483185754L;

		@Override
		protected void reportError(String msg) {
			JSFDemoEvent.this.reportError(msg);
		}

		@Override
		public String getProgramName() {
			return JSFDemoEvent.this.getProgramName();
		}

		@Override
		public String getEventID() {
			return JSFDemoEvent.this.getEventID();
		}
	};

	public JSFDemoEvent(){
		super();
        this.getMaxRegisterShed();
	}

	protected void reportError(String msg){
		if(msg!=null){
			this.getManager().addMsgError(msg);
		}
	}

	public String getEventID(){
		if(eventID==null)
			eventID = EventUtil.getUniqueEventName(this.getProgramName()+"_");
		return eventID;
	}


	public Event toEvent(UtilityDREvent drEvent){
		final Event event = new Event();
		event.setEventName(EventUtil.getUniqueEventName(this.getProgramName()+"_"));
		event.setProgramName(this.getProgramName());
		event.setStartTime(getStart());
		event.setEndTime(getEnd());

		final Date date = new Date();
		event.setIssuedTime(date);
		event.setReceivedTime(date);

		event.setManual(true);

		event.setReceivedTime(new Date());
		EventTiming timing = drEvent.getEventTiming();
		if(timing != null){
			event.setIssuedTime(timing.getNotificationTime()
                    .toGregorianCalendar().getTime());
			event.setStartTime(timing.getStartTime().toGregorianCalendar()
                    .getTime());
			event.setEndTime(timing.getEndTime().toGregorianCalendar()
                    .getTime());
			event.setNearTime(delegation.getNearEvent().getDateTime());
		}

		event.setParticipants(this.getEventParticipants());
		Set<EventParticipant> epSet = new HashSet<EventParticipant>();
		for(int i=0;i<this.getEventParticipants().size();i++){
			epSet.add(this.getEventParticipants().get(i));
		}
		event.setEventParticipants(epSet);

		return event;
	}

   public List<EventParticipantDataModel> getAllParticipantsInProgram() {
		if(allParticipantsInProgram.size()>0 && this.selectedFilteredParticipantListCache){
			return allParticipantsInProgram;
		}
		try{

            String programName = this.getProgramName();
			if(getManager().getUserEAO()!=null&&programName!=null&&programName!=""){

				List<Participant> participantAndClientList =getManager().getProgramParticipantManager().getParticipantsForProgramAsObject(programName);
				Set<EventParticipant> eventParticipants = new HashSet<EventParticipant>();
				eventParticipants = getManager().getProgramManager().filterEventParticipants(programName);

				List<Participant> participantList = new ArrayList<Participant>();
				allParticipantsInProgram = new ArrayList<EventParticipantDataModel>();

				for(int i=0;i<participantAndClientList.size();i++){
					if(participantAndClientList.get(i).isClient()){
						continue;
					}

					participantList.add(participantAndClientList.get(i));
					EventParticipantDataModel model = new EventParticipantDataModel(participantAndClientList.get(i),false);

                    for(ProgramParticipant pp: participantAndClientList.get(i).getProgramParticipants()){
                    	if(pp.getProgramName().equalsIgnoreCase(programName)){
                    		//DRMS-6084
                    		if(!pp.getOptStatus().equals(new Integer(1))){
                    			allParticipantsInProgram.add(model);	
                    		}
                    	}
                    }
				}

				for(int j=0;j<allParticipantsInProgram.size();j++){
					EventParticipantDataModel model = allParticipantsInProgram.get(j);
					String participantName = model.getParticipant().getParticipantName();

                    model.setParticipantAvilableShed(this.getParticipantAvilableShed(model.getParticipant()));

                    for(EventParticipant eventParticipant:eventParticipants){
						if(eventParticipant.getParticipant().getParticipantName().trim().equals(participantName)){
							model.setDelete(false);
							model.setNonselectable(false);
							model.setSelect(true);
							break;
						}
					}
				}
                selectedFilteredParticipantListCache = true;
			}
		}catch(Exception e){

		}
		return allParticipantsInProgram;
	}
	public void setAllParticipantsInProgram(
			List<EventParticipantDataModel> allParticipantsInProgram) {
		this.allParticipantsInProgram = allParticipantsInProgram;
	}


     /**
	 * Event JSF framework confirm request for dispatch to the EventDataModelManager to handler
	 * @return JSF Response String
	 */
	public String confirm(){
        return dispatchEvent(this , super.getManager());
	}

    /**
	 * Event JSF framework for get Presentation Layer request for select all participants
	 *
	public void selectedAllParticipants(){
		for(int i=0;i<allParticipantsInProgram.size();i++){
			allParticipantsInProgram.get(i).setSelect(true);
		}
        this.selectedFilteredParticipantListCache = false;
		renewFlag=false;
        getAllParticipantsInProgram();
        this.selectLabel = " >> Select All";
	}

	public void addSelectedParticipants(){
        getAllParticipantsInProgram();
        this.setAllParticipantsInProgram(allParticipantsInProgram);
	}


	/**
	 * Event JSF framework for get Presentation Layer request for select none participants
	 *
	public void selectedNoneParticipants(){

		for(int i=0;i<allParticipantsInProgram.size();i++){
			allParticipantsInProgram.get(i).setSelect(false);
		}

		renewFlag=true;
         this.selectedFilteredParticipantListCache = true;
        getAllParticipantsInProgram();
        this.selectLabel = " >> Select None";

	}
    */

/**
	 * Event JSF framework for get Presentation Layer request for select all participants
	 */
	public void selectedAllParticipants(){
		for(int i=0;i<allParticipantsInProgram.size();i++){
			allParticipantsInProgram.get(i).setSelect(true);
		}
        this.selectedFilteredParticipantListCache = true;
		renewFlag=false;
        getAllParticipantsInProgram();
        this.selectLabel = " >> Select All";
	}




	/**
	 * Event JSF framework for get Presentation Layer request for select none participants
	 */
	public void selectedNoneParticipants(){
		for(int i=0;i<allParticipantsInProgram.size();i++){
			allParticipantsInProgram.get(i).setSelect(false);
		}

		renewFlag=true;
        this.selectedFilteredParticipantListCache = true;
        getAllParticipantsInProgram();
        this.selectLabel = " >> Select None";

	}

    /* matches any string or sub-string in the start and the middle*/
    /*
    private boolean keySearchMatching(String k1, String k2){
        if (k1.equalsIgnoreCase(k2) ||  k1.matches("(?i)"+k2+".+") ) return true;
        return false;
    }
   */

     // matches any string or sub-string in the start only
    private static  boolean keySearchMatching(String k1, String k2){
        if (k1.equalsIgnoreCase(k2)
                ||  k1.toLowerCase().matches(""+k2.toLowerCase()+".+")
                || k1.matches(""+k2+".+")
                ) return true;
        return false;
    }



    // register shed range matching
    private boolean keySearchMatching(int k1, int min, int max){
        if (k1 > min && k1 <= max )
            return true;

        return false;
    }


	public void filterSelectedParticipantNameAccount(){

         if (!searchInResult){
           this.copyAllParticipantsInProgram.clear();
           this.allParticipantsInProgram.clear();
           this.getAllParticipantsInProgram();
           this.history_filter_labels.clear();
		   history_filter_label = "";
           filterLabel = "";
         }

          // enable cache
          this.selectedFilteredParticipantListCache = true;

        if (filterType != null || filterContent != null){
            for(int i=0;i<this.allParticipantsInProgram.size();i++){
                if (
                      (filterType.equals(FILTER_PARTICIPANT_NAME)
                            &&
                            keySearchMatching(allParticipantsInProgram.get(i).getParticipant().getParticipantName(),filterContent))
                        ||
                      (filterType.equals(FILTER_PARTICIPANT_ACCOUNT)
                            &&
                            keySearchMatching(allParticipantsInProgram.get(i).getParticipant().getAccountNumber(),filterContent))

                        ||

                        (filterType.equals(FILTER_PARTICIPANT_SHED)
                            &&
                            keySearchMatching((int)allParticipantsInProgram.get(i).getParticipant().getShedPerHourKW(),slide_search_min,slide_search_max))

                        ){
                    copyAllParticipantsInProgram.add(allParticipantsInProgram.get(i));
                }
            }

            allParticipantsInProgram.clear();
            allParticipantsInProgram.addAll(copyAllParticipantsInProgram);
			copyAllParticipantsInProgram.clear();

			if (!searchInResult){
			   if (history_filter_labels.size() > 1){
                    if (this.filterType.equals(FILTER_PARTICIPANT_SHED)) {
                        this.filterContent = "["+ slide_search_min + "-" + slide_search_max+ "]";
                        this.filterType = FILTER_PARTICIPANT_SHED;
                    }else{
                        this.filterLabel += " >> " + this.filterType + ":" +  this.filterContent;
                   }
               } else {
                    if (this.filterType.equals(FILTER_PARTICIPANT_SHED)){
                        this.filterContent = "["+ slide_search_min + "-" + slide_search_max+ "]";
                        this.filterType = FILTER_PARTICIPANT_SHED;
                    }
                    this.filterLabel +=  this.filterType + ":" +  this.filterContent;
                }
			}else{
                if (this.filterType.equals(FILTER_PARTICIPANT_SHED)) this.filterContent = "["+ slide_search_min + "-" + slide_search_max+ "]";
				filterLabel =  " >> " + this.filterType + ":" +  this.filterContent;
			}
        }


        this.history_filter_labels.add(this.filterLabel);
			 history_filter_label = "";
	    for(Iterator<String> i = history_filter_labels.iterator(); i.hasNext();){
			history_filter_label += i.next();
		}
		this.setHistory_filter_label(history_filter_label);
		renewFlag=true;
        this.selectedFilteredParticipantListCache = true;
        this.setParticipant_count(participant_count);

        // if (copyAllParticipantsInProgram.isEmpty()) allParticipantsInProgram.clear();

	}



    public void closeModal(){
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
        //this.selectedParticipantsInProgram.clear();

        if (selectedParticipantsInProgram.size() > 0 && add_selected_flag ){

           this.allParticipantsInProgram = selectedParticipantsInProgram;
           this.setAllParticipantsInProgram(selectedParticipantsInProgram);
           this.selectedFilteredParticipantListCache = true;
           //this.emptySet = true;
           return;
        }
        if (selectedParticipantsInProgramForRemove.size() > 0 && remove_selected_flag){
           this.allParticipantsInProgram = selectedParticipantsInProgramForRemove;
           this.setAllParticipantsInProgram(selectedParticipantsInProgramForRemove);
           this.selectedFilteredParticipantListCache = true;
           //this.emptySet = true;
           return;
        }else{
           this.selectedFilteredParticipantListCache = false;
           //this.emptySet = false;
           return;
        }

	}



     public void clearParticipantsFilterSelection(){
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
 
       // renewFlag=true;
        getAllParticipantsInProgram();
	}



    public void removeAllParticipantsAction(){

       /*
           List <EventParticipantDataModel> tempRemove = new ArrayList<EventParticipantDataModel>();
           for (EventParticipantDataModel from: selectedParticipantsInProgramForRemove ){
               tempRemove.add(from);
           }
        *
        */

        Iterator<EventParticipantDataModel> i = selectedParticipantsInProgramForRemove.iterator();
        while(i.hasNext()){
            EventParticipantDataModel it = i.next();
                 i.remove();
        }


       this.selectedFilteredParticipantListCache = true;
       this.setAllParticipantsInProgram(selectedParticipantsInProgramForRemove);
       this.allParticipantsInProgram = selectedParticipantsInProgramForRemove;

    }

      public void restoreRemoveAllParticipantsAction(){

       List <EventParticipantDataModel> tempRemove = new ArrayList<EventParticipantDataModel>();
       for (EventParticipantDataModel from: selectedParticipantsInProgramForRemove ){
           tempRemove.add(from);
       }
       this.selectedFilteredParticipantListCache = true;
       this.setAllParticipantsInProgram(tempRemove);


    }

     public void removeParticipantsAction(){

       // get another remove container
         /*
       List <EventParticipantDataModel> tempRemove = new ArrayList<EventParticipantDataModel>();
       for (EventParticipantDataModel from: selectedParticipantsInProgramForRemove ){
            tempRemove.add(from);
       }
       */
         this.selectedFilteredParticipantListCache = true;
         selectedParticipantsInProgram =  allParticipantsInProgram;

         // get complete part list
        this.selectedFilteredParticipantListCache = false;
        List <EventParticipantDataModel> tempRemove = new ArrayList<EventParticipantDataModel>();
       for (EventParticipantDataModel from:  this.getAllParticipantsInProgram() ){
            tempRemove.add(from);
       }
       
        // check for un selected items
        Iterator<EventParticipantDataModel> i = selectedParticipantsInProgram.iterator();
        while(i.hasNext()){
            EventParticipantDataModel it = i.next();
             if (!(it.isSelect()) ){
                 i.remove();
             }
        }

        // remove operatrion
         tempRemove.removeAll(selectedParticipantsInProgram);
         selectedParticipantsInProgramForRemove.removeAll(selectedParticipantsInProgram);
         for (EventParticipantDataModel f2: selectedParticipantsInProgram ){
               tempRemove.remove(f2);
            }


        // enable cashe so it would reflect in the screen
         this.selectedFilteredParticipantListCache = true;
         this.setAllParticipantsInProgram(tempRemove);
         allParticipantsInProgram = tempRemove;
         this.selectedParticipantsInProgram.clear();
      

	}

     public void removeSelectedParticipants(){
        this.panel_filter_label = DELETE_PARTICIPANT_LABEL;
        this.setPanel_filter_label(DELETE_PARTICIPANT_LABEL);
        this.add_selected_flag = false;
        this.remove_selected_flag = true;
        this.history_filter_label = "";
        history_filter_labels.clear();

        List<EventParticipantDataModel> selectedList = allParticipantsInProgram;

        // reserve the selected list
        List<EventParticipantDataModel> tempSelectedList  = new ArrayList<EventParticipantDataModel>();
        tempSelectedList.clear();
        tempSelectedList.addAll(selectedList);
        selectedParticipantsInProgramForRemove.clear();
        for (EventParticipantDataModel s:tempSelectedList ){
             selectedParticipantsInProgramForRemove.add(s);
         }



     }

    public void addParticipants(){
        this.panel_filter_label = ADD_PARTICIPANT_LABEL;
        this.setPanel_filter_label(ADD_PARTICIPANT_LABEL);
        this.add_selected_flag = true;
        this.remove_selected_flag = false;
        this.history_filter_label = "";
        history_filter_labels.clear();

        List<EventParticipantDataModel> selectedList = allParticipantsInProgram;

        // reserve the selected list
        List<EventParticipantDataModel> tempSelectedList  = new ArrayList<EventParticipantDataModel>();
        tempSelectedList.clear();
        tempSelectedList.addAll(selectedList);
        selectedParticipantsInProgram.clear();
        for (EventParticipantDataModel s:tempSelectedList ){
             selectedParticipantsInProgram.add(s);
         }

        // get outjoin of selected list
        this.selectedFilteredParticipantListCache = false;

         // check for the first time reserve list
        if (selectedParticipantsInProgram.size() ==
                this.getAllParticipantsInProgram().size())
        selectedParticipantsInProgram.clear();

        List<EventParticipantDataModel> fullList = this.getAllParticipantsInProgram();
        fullList = allParticipantsInProgram; fullList = this.getAllParticipantsInProgram();
        List<EventParticipantDataModel> fullList2 = new ArrayList<EventParticipantDataModel>();

        for (EventParticipantDataModel f:fullList ){
             fullList2.add(f);
         }
        boolean f;
        this.allParticipantsInProgram.clear();

        for (EventParticipantDataModel t1:fullList2 ){
            f = false;

            for(EventParticipantDataModel t2:selectedList ){
                if (t1.getParticipant().getUUID().equalsIgnoreCase(t2.getParticipant().getUUID()) ) {
                  f = true;
                  break;
                }
              }
                if (!f){
                      this.allParticipantsInProgram.add(t1);
                }
            }
        // enable cashe so it would reflect in the screen
         this.selectedFilteredParticipantListCache = true;
         this.setAllParticipantsInProgram(allParticipantsInProgram);

	}

    public void addSelectedParticipants(){

        // check for un selected items
        Iterator<EventParticipantDataModel> i = allParticipantsInProgram.iterator();
        while(i.hasNext()){
            EventParticipantDataModel it = i.next();
             if (!(it.isSelect()) ){
                 i.remove();
             }
        }

        // merge the new selection from current to orginal
          for(EventParticipantDataModel s:selectedParticipantsInProgram ){
                this.allParticipantsInProgram.add(s);
            }


         // enable cashe so it would reflect in the screen
         this.selectedFilteredParticipantListCache = true;
         this.setAllParticipantsInProgram(allParticipantsInProgram);
         this.selectedParticipantsInProgram.clear();
         this.selectedParticipantsInProgram.addAll(allParticipantsInProgram);

          // getting the unselected/selected
           /*
            List<EventParticipantDataModel> temp = new ArrayList<EventParticipantDataModel>();
             for(int i=0;i<allParticipantsInProgram.size();i++){
                if (!allParticipantsInProgram.get(i).isSelect());
                temp.add(allParticipantsInProgram.get(i));
            }
            */


	}


    public void deleteSelectedParticipants(){

        this.panel_filter_label = DELETE_PARTICIPANT_LABEL;
        allParticipantsInProgram =  this.getAllParticipantsInProgram();

	}

   public void selectedParticipantsAddAction(){
        this.panel_filter_label = ADD_PARTICIPANT_LABEL;
        this.setPanel_filter_label(ADD_PARTICIPANT_LABEL);
        selectedFilteredParticipantListCache = false;
        this.getAllParticipantsInProgram();
    }




    public void selectedParticipantsCancelAction(){
        this.panel_filter_label = DELETE_PARTICIPANT_LABEL;
        this.getAllParticipantsInProgram();
    }


     /**
	 * Function for check the participants which contain in event is empty using ValidationException
	 * @param Event JSF
	 * @param manager
	 */
    public void participantsEmptyAndParticipationValidation(EventDataModel eventDataModel,EventDataModelManager manager){
        List<EventParticipantDataModel> temp = new ArrayList<EventParticipantDataModel>();

        for(EventParticipantDataModel ep:this.getAllParticipantsInProgram()){
           // if (ep.isSelect())
                temp.add(ep);
        }
          setAllParticipantsInProgram(temp);
//          eventDataModel.setAllParticipantsInProgram(temp);

          Set<EventParticipant> eventParticipants = new HashSet<EventParticipant>();
		  eventParticipants = getManager().getProgramManager().filterEventParticipants(this.getProgramName());
          List <EventParticipant> epTemp = new ArrayList<EventParticipant>();
         	for(int j=0;j<this.getAllParticipantsInProgram().size();j++){
				EventParticipantDataModel model = allParticipantsInProgram.get(j);
				String participantName = model.getParticipant().getParticipantName();

				for(EventParticipant eventParticipant:eventParticipants){
					if(eventParticipant.getParticipant().getParticipantName().trim().equals(participantName)){
                        epTemp.add(eventParticipant);
						model.setDelete(false);
						model.setNonselectable(false);
						model.setSelect(true);
						break;
					}
				}
			}
            this.setEventParticipants(epTemp);

        if(this.getAllParticipantsInProgram().size()<=0 || temp.size() <= 0){
            throw new ValidationException("Cannot create an event with no participants");
        }else{

        }
    }
    
    public String dispatchEvent(EventDataModel eventDataModel,EventDataModelManager manager) {
    	String programName ="";
		try {
			confirmEventParticipantList();
//			eventDataModel.confirmEventParticipantList();
			participantsEmptyAndParticipationValidation(eventDataModel, this.getManager());
//			JSFDemoEvent demoEvent = (JSFDemoEvent) eventDataModel;
			String msg = validateTiming();
//			String msg = demoEvent.validateTiming();
			if (msg != null) {
				throw new ValidationException(msg);
			}
			UtilityDREvent drEvent = toUtilityDREvent();
			Event event = toEvent(drEvent);
			programName = event.getProgramName();
//			UtilityDREvent drEvent = demoEvent.toUtilityDREvent();
//			Event event = demoEvent.toEvent(drEvent);
			DemoProgramEJB demoProgram =  EJB3Factory.getLocalBean(DemoProgramEJB.class);

			demoProgram.createEvent(getProgramName(),event,drEvent);
			addEventLog(true, programName,getWelcomeName(), null);
			eventDataModel.setRenewFlag(true);
			manager.setFlag_GoToParent(true);
			return manager.goToEventDisplayListPage();
		} catch (Exception e) {
			final String s = ErrorUtil.getErrorMessage(e);
			manager.addMsgError(s);
			eventDataModel.setRenewFlag(false);
			addEventLog(false, programName, getWelcomeName(), e);
			return "validateNotPass_DemoSchedulePage";
		}
	}

     public String getRenew() {
		if(!renewFlag){
			renewFlag = true;
			return renew;
		}
		FacesContext context = FacesContext.getCurrentInstance();
		if(context!=null){
			ExternalContext ec = context.getExternalContext();
			if(ec!=null){
				HttpServletRequest request = (HttpServletRequest)ec.getRequest();
				if(request!=null){
					String programName =request.getParameter("programName");
					if(programName==null||programName==""){
						return renew;
					}
				}
			}
		}
		EventDataModel model = new EventDataModel();
		getManager().addSessionCache(model);
		return renew;
	}

	//======================== Delegations =========================//
	public void setRenew(String renew) {
		this.renew = renew;
	}

	public boolean isRenewFlag() {
		return renewFlag;
	}

	public void setRenewFlag(boolean renewFlag) {
		this.renewFlag = renewFlag;
	}


	public void updateModel() {
		delegation.updateModel();
	}

	public void startTimeChange(ValueChangeEvent event) {
		delegation.startTimeChange(event);
	}

	public void startDateChange(ValueChangeEvent event) {
		delegation.startDateChange(event);
	}

	public void noticeTimeChange(ValueChangeEvent event) {
		delegation.noticeTimeChange(event);
	}

	public void notificationDateChange(ValueChangeEvent event) {
		delegation.notificationDateChange(event);
	}

	public void notificationTimeChange(ValueChangeEvent event) {
		delegation.notificationTimeChange(event);
	}

	public void durationTimeChange(ValueChangeEvent event) {
		delegation.durationTimeChange(event);
	}

	public void endTimeChange(ValueChangeEvent event) {
		delegation.endTimeChange(event);
	}

	public void startOffSetChange(ValueChangeEvent event) {
		delegation.startOffSetChange(event);
	}

	public void endOffSetChange(ValueChangeEvent event) {
		delegation.endOffSetChange(event);

	}

	public void eventOffSetChange(ValueChangeEvent event) {
		delegation.eventOffSetChange(event);
	}

	public List<JSFDemoEventInfo> getEvents() {
		return delegation.getEvents();
	}

	public void addSingalEntry(ActionEvent evt) {
		delegation.addSingalEntry(evt);
	}
	
	public void removeSingalEntry(ActionEvent evt) {
		this.setRenewFlag(false);
		delegation.removeSingalEntry(evt);
	}

	public boolean equals(Object obj) {
		return delegation.equals(obj);
	}

	public UtilityDREvent toUtilityDREvent() {
		return delegation.toUtilityDREvent();
	}

	public Values toEventInfoInstanceValues(String signalType) {
		return delegation.toEventInfoInstanceValues(signalType);
	}


	public Date getCurrentClientTime() {
		return delegation.getCurrentClientTime();
	}

	public void setStart(Date date) {
		delegation.setStart(date);
	}

	public Date getStart() {
		return delegation.getStart();
	}

	public void setStartTimeStr(String time) {
		delegation.setStartTimeStr(time);
	}

	public String getStartTimeStr() {
		return delegation.getStartTimeStr();
	}

	public void setNotification(Date date) {
		delegation.setNotification(date);
	}

	public Date getNotification() {
		return delegation.getNotification();
	}

	public void setNotificationTimeStr(String timeStr) {
		delegation.setNotificationTimeStr(timeStr);
	}

	public String getNotificationTimeStr() {
		return delegation.getNotificationTimeStr();
	}

	public void setEndTimeStr(String timeStr) {
		delegation.setEndTimeStr(timeStr);
	}

	public String getEndTimeStr() {
		return delegation.getEndTimeStr();
	}

	public Date getEnd() {
		return delegation.getEnd();
	}

	public int getNotice() {
		return delegation.getNotice();
	}

	public void setNotice(int notice) {
		delegation.setNotice(notice);
	}

	public int getDuration() {
		return delegation.getDuration();
	}

	public void setDuration(int duration) {
		delegation.setDuration(duration);
	}

	public String validateTiming() {
		return delegation.validateTiming();
	}

	public Date getStartDateOnly() {
		return delegation.getStartDateOnly();
	}

	public void setStartDateOnly(Date startDateOnly) {
		delegation.setStartDateOnly(startDateOnly);
	}

	public Date getNotificationDateOnly() {
		return delegation.getNotificationDateOnly();
	}

	public void setNotificationDateOnly(Date notificationDateOnly) {
		delegation.setNotificationDateOnly(notificationDateOnly);
	}

	public List<String> getEnabledSignalTypes() {
		return delegation.getEnabledSignalTypes();
	}

	public List<SignalDef> getEnabledSignals() {
		return delegation.getEnabledSignals();
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
            this.keyword_search_flag = false; this.slide_search_flag = true;
        } else{
            this.keyword_search_flag = true; this.slide_search_flag = false;
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
        return panel_filter_label;
    }

    public void setPanel_filter_label(String panel_filter_label) {
        this.panel_filter_label = panel_filter_label;
    }

    public int getParticipant_count() {
        participant_count =  this.allParticipantsInProgram.size();
        return participant_count;
    }

    public void setParticipant_count(int participant_count) {
        this.participant_count = participant_count;
    }

    public double getParticipantAvilableShed(Participant p) {

      	participantAvilableShed = 0.0;
    	if (p != null) {
    		List<Participant> clientList =  getManager().getUserEAO().findClientsByParticipant(p.getParticipantName());
    		int c = 0;
            for (Participant client:clientList){
                if (client.getStatus()== 0) c++;
            }
            try{
            	if (!Double.isNaN(p.getShedPerHourKW()) && (clientList.size() > 0))
        			participantAvilableShed = ( ((double)p.getShedPerHourKW()/clientList.size()) * c );
            } catch(Exception e) {
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


    public double getTotalAvilabeShed() {
        totalAvilabeShed = 0;
        for(int j=0;j<allParticipantsInProgram.size();j++){
                    EventParticipantDataModel model = allParticipantsInProgram.get(j);
					totalAvilabeShed +=  Double.isNaN(this.getParticipantAvilableShed(model.getParticipant()) ) ? 0.0 : this.getParticipantAvilableShed(model.getParticipant()) ;
				}
        return totalAvilabeShed;
    }


    public void setTotalAvilabeShed(double totalAvilabeShed) {
        this.totalAvilabeShed = totalAvilabeShed;
    }

    public double getTotalRegisterShed() {
                totalRegisterShed = 0;
        	for(int j=0;j<allParticipantsInProgram.size();j++){
					EventParticipantDataModel model = allParticipantsInProgram.get(j);
					if (model.getParticipant() != null)
						totalRegisterShed += Double.isNaN(model.getParticipant().getShedPerHourKW()) ? 0.0 : model.getParticipant().getShedPerHourKW();
				}
        return totalRegisterShed;
    }

    public void setTotalRegisterShed(double totalRegisterShed) {
        this.totalRegisterShed = totalRegisterShed;
    }

    public double getMaxRegisterShed() {
        maxRegisterShed = 0;

        for(EventParticipantDataModel ep : allParticipantsInProgram){
					if (ep.getParticipant() != null)
						if (ep.getParticipant().getShedPerHourKW() > maxRegisterShed) maxRegisterShed = ep.getParticipant().getShedPerHourKW();
				}
        if (maxRegisterShed== 0)maxRegisterShed = 0.01;

        return maxRegisterShed;
    }

    public void setMaxRegisterShed(double maxRegisterShed) {
        this.maxRegisterShed = maxRegisterShed;
    }

    public boolean isActivateSlider() {
        if (Double.isNaN(maxRegisterShed)|| maxRegisterShed==0.01)
               this.activateSlider = false;
        else
               this.activateSlider = true;
        return activateSlider;
    }

    public void setActivateSlider(boolean activateSlider) {
        this.activateSlider = activateSlider;
    }


	public static void addEventLog(boolean successFlag,String programName,String userName,Exception ex){
		FireLogEntry logEntry = new FireLogEntry();
		logEntry.setUserParam1(programName);
		logEntry.setCategory(LogUtils.CATAGORY_EVENT);
		logEntry.setLongDescr(null);
		logEntry.setUserName(userName);
		if(successFlag){
			String message = "SUCCESS_CREATING_EVENT_STATE";
			logEntry.setDescription(message);
			log.info(logEntry);	
		}else{
			String message = "ERROR_CREATING_EVENT_STATE";
			logEntry.setDescription(message+":"+ex.getMessage());
			StringBuilder sb = new StringBuilder();
			sb.append(ex.toString());
			sb.append("\n");
			for(StackTraceElement element: ex.getStackTrace())
			{
				sb.append(element.toString());
				sb.append("\n");
			}
			logEntry.setLongDescr(sb.toString());
			log.error(logEntry);	
		}
	}
	
	public static String getWelcomeName() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		return request.getRemoteUser();
	}
}
