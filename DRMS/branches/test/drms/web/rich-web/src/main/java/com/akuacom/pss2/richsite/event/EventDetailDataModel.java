package com.akuacom.pss2.richsite.event;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.SignalLevelMapper;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.usage.UsageDataManager;
import com.akuacom.pss2.data.usage.UsageSummary;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.bid.ParticipantBidState;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.bidding.BiddingProgramManager;
import com.akuacom.pss2.program.dbp.BidEntry;
import com.akuacom.pss2.program.dbp.DBPBidProgramEJB;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.program.dbp.DBPProgram;
import com.akuacom.pss2.program.dbp.EventBidBlock;
import com.akuacom.pss2.program.dbp.EventParticipantBidEntry;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.query.EventBidSummary;
import com.akuacom.pss2.query.EventClientSummary;
import com.akuacom.pss2.query.EventParticipantSummary;
import com.akuacom.pss2.query.NativeQueryManager;
import com.akuacom.pss2.richsite.FDUtils;
import com.akuacom.pss2.richsite.event.EventUsageDataModel.DateEntrySelectPredicate;
import com.akuacom.pss2.richsite.util.ParticipantShedCalculateUtil;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.userrole.ViewBuilderManager;
import com.akuacom.pss2.userrole.viewlayout.BidEventViewLayout;
import com.akuacom.pss2.userrole.viewlayout.EventLocationDetailLayout;
import com.akuacom.pss2.userrole.viewlayout.EventParticipantViewLayout;
import com.akuacom.utils.DateUtil;

public class EventDetailDataModel implements Serializable,BidEventViewLayout,EventLocationDetailLayout,EventParticipantViewLayout{

	private static final long serialVersionUID = -3896417842407852050L;
	
	private static final Logger log =
            Logger.getLogger(EventDetailDataModel.class.getName());

	private Event event = null;
    private String state = "FAR";
	List<ProgramParticipant> ppList = null;
	boolean usageEnabled = false;
	private boolean individualparticipant;
	private boolean bidding;
	private boolean drasBidding;
	private double[] reductionTotals = null;
	private String editParticipantName;
	private EventParticipantDataModel optoutPart;
	private List<BidEntry> currentBids;
	private List<BidModel> editableBids;
	private List<EventParticipantDataModel> biddingParticipants;
	private List<EventParticipantDataModel> optInParticipants;
	
	private boolean eventOptIn=false;
	private boolean showLocation = false;
	
	private EvetLocationModel locationModel;
	
	
	private static final String BID_PENDING = "Pending";
	private NativeQueryManager nativeQuery;
	public EventDetailDataModel() {
		loadEvent();
		retrieveShedEnabled();
		
		initOptinParticipants();
		buildViewLayout();
	}

	public Event getEvent() {
		if (event == null) {
			loadEvent();
			
		}
		return event;
	}
	
	private void initOptinParticipants(){
        SystemManager systemManager = EJB3Factory.getLocalBean(SystemManager.class);
        Boolean eventOptIn=systemManager.getPss2Features().isFeatureEventOptIn();
        
        if (eventOptIn==null || !eventOptIn) return;        
        
		String utilityName = systemManager.getPss2Properties().getUtilityName();

        if (event instanceof DBPEvent && utilityName.equalsIgnoreCase("sce"))
        	this.eventOptIn=true;
        else
        	return;
        
		optInParticipants=new ArrayList<EventParticipantDataModel>();
		
		try {
			List<EventParticipantSummary> summary = getQueryManager()
					.getEnrollParticipantSummary(event.getProgramName(), event.getEventName());
			for(EventParticipantSummary ps:summary){
					Participant participant = getPm().getParticipantAndShedsOnly(ps.getParticipantName());
					EventParticipantDataModel ep = new EventParticipantDataModel(participant, false);
					ep.setShedEnabled(shedEnabled);
					if(!shedEnabled){
						ep.setParticipantAvilableShed(0);
					}else{
						double estimatedShed = ParticipantShedCalculateUtil.calculateEstimatedShed(participant, event);
						double availableShed = ParticipantShedCalculateUtil.calcAvailableSheds(estimatedShed,ps.getValieClientCount(),ps.getClientCount());
						ep.setParticipantAvilableShed(availableShed);
					}
					
					ep.setEvent(event);
					DBPEvent dbpEvent = (DBPEvent) event;
					List<EventBidBlock> timeBlocks = dbpEvent.getBidBlocks();
					double[] bids = new double[timeBlocks.size()];
					ep.setBids(bids);
					ep.setDelete(false);
					optInParticipants.add(ep);
			}
		} catch (Exception e) {
			log.error("Internal Error", e);
			FDUtils.addMsgError("Internal Error");
		}
	}
	
	private void loadEvent() {
		long start = System.currentTimeMillis();
		Map<String, String> requestMap = 
			FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String eventName = requestMap.get("eventName");
        
        EventManager eventManager = EJBFactory.getBean(EventManager.class);
        event = (Event)eventManager.getEventWithParticipants(eventName);
        if (event != null) {
            setState(eventManager.getEventStatusString(event));
            
            List<String> participantNames = new ArrayList<String>();
    		for (EventParticipant part : event.getEventParticipants()) {
    			if (part != null && part.getParticipant() != null && !part.getParticipant().isClient()) {
    				participantNames.add(part.getParticipant().getParticipantName());
    			}
    		}
    		if(participantNames!=null&&participantNames.size()==1) individualparticipant = true;
    		
    		 SystemManager systemManager = EJB3Factory.getLocalBean(SystemManager.class);
    	        PSS2Features features = systemManager.getPss2Features();
    	        usageEnabled = features.isUsageEnabled();
    	        
    			// TODO: this should be in a utility class
    			ProgramManager programManager = EJB3Factory.getLocalBean(ProgramManager.class);
    			Program program = programManager.getProgramOnly(event.getProgramName());
    			if(program instanceof DBPProgram) {
    				bidding = true;
    	            Boolean isDrasBidding = programManager.isDrasBiddingByProgramName(event.getProgramName());
    	            if(isDrasBidding != null && isDrasBidding) {
    	                drasBidding = true;
    	            }        
    			}
    			if("BIP".equalsIgnoreCase(program.getProgramClass())){
    				showLocation = true;
    			}
        }
        long end = System.currentTimeMillis();
        log.info("Load event "+eventName+" costs :"+(end-start)+"milliseconds");
	}
	
    public void setEvent(Event event) {
		this.event = event;
	}

	public void addMsgError(String message) {
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
				message, message));
	}
	
	public Date getStartTime() {
		if (event != null) {
			return event.getStartTime();
		} else {
			return null;
		}
	}
	
	public Date getEndTime() {
		if (event != null) {
			return event.getEndTime();
		} else {
			return null;
		}
	}
	
	public String getReceivedTimeStr() {
		if (event != null) {
			return new SimpleDateFormat("MM/dd/yyyy HH:mm").format(event.getReceivedTime());
		} else {
			return "";
		}
	}
	
	public Date getIssuedTime() {
		if (event != null) {
			return event.getIssuedTime();
		} else {
			return null;
		}
	}


	public boolean isShowLocation(){
		return showLocation;
	}
	
	public boolean isDbp() {
        SystemManager systemManager = EJB3Factory.getLocalBean(SystemManager.class);
        String utilityName = systemManager.getPss2Properties().getUtilityName();
        return event instanceof DBPEvent && event.getProgramName().startsWith("DBP") && "pge".equals(utilityName);
    }

	public Date getDrasRespondBy() {
		if (event != null && isDbp()) {
            DBPEvent dbp = (DBPEvent) event;
            return dbp.getDrasRespondBy();
        } else {
			return null;
		}
	}

    public Date getRespondBy() {
		if (event != null && isDbp()) {
            DBPEvent dbp = (DBPEvent) event;
            return dbp.getRespondBy();
		} else {
			return null;
		}
	}

	public String getEstimatedShed() {
		long start = System.currentTimeMillis();
		double shed = 0.0;
		if(!shedEnabled){
			return Double.toString(shed) + " KW";
		}else{
			if (event != null) {
				for (EventParticipant part : event.getEventParticipants()) {
					if (part != null && part.getParticipant() != null 
							&& !part.getParticipant().isClient()) {
						if(part.getOptOutTime()!=null&&!part.getOptOutTime().after(part.getEvent().getStartTime())){
							continue;
							//don't calculate shed for opt out event
						}
//						shed += part.getParticipant().getShedPerHourKW();
						shed += ParticipantShedCalculateUtil.calculateEstimatedShed(part.getParticipant(), event);
					}
				}
			}
		}
		long end = System.currentTimeMillis();
        log.info("Get Estimated Shed costs :"+(end-start)+"milliseconds");
		return Double.toString(shed) + " KW";
	}
	
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	private List<ProgramParticipant> getProgramParticipants() {
		if (ppList == null) {
			ProgramParticipantManager ppM = EJBFactory.getBean(ProgramParticipantManager.class);
			ppList = ppM.getProgramParticipantsForProgramAsObject(this.getEvent().getProgramName());
		}
		return ppList;
	}
	
	public boolean isIndividualparticipant() {
		return individualparticipant;
	}

	public void setIndividualparticipant(boolean individualparticipant) {
		this.individualparticipant = individualparticipant;
	}

	private double calcAvailableSheds(Participant p) {
		ArrayList<Participant> clientList = new ArrayList<Participant>();
		int c = 0;
		for (ProgramParticipant client : getProgramParticipants()) {
			if (client.getParticipant().getParent() != null && client.getParticipant().getParent().equals(p.getParticipantName())) {
				clientList.add(client.getParticipant());
				if (client.getParticipant().getStatus() == 0) {
					c++;
				}
			}
		}
		double participantAvilableShed = 0;
		if (clientList.size() != 0 && c !=  0) {
			participantAvilableShed = ((p.getShedPerHourKW() / clientList.size()) * c);
		}
		
		return participantAvilableShed;
	}
	
	public List<EventParticipantDataModel> getAllParticipantsInEvent() {
		try {
			//if (eventParticipants == null) {
			List<EventParticipantSummary> summary = getQueryManager()
					.getEventParticipantSummary(event.getEventName());
			List<EventParticipantDataModel> parts= new ArrayList<EventParticipantDataModel>();
			for(EventParticipantSummary ps:summary){
					Participant participant = getPm().getParticipantAndShedsOnly(ps.getParticipantName());
					EventParticipantDataModel ep = new EventParticipantDataModel(participant, false);
					ep.setShedEnabled(shedEnabled);
					if(!shedEnabled){
						ep.setParticipantAvilableShed(0);
					}else{
						double estimatedShed = ParticipantShedCalculateUtil.calculateEstimatedShed(participant, event);
						double availableShed = ParticipantShedCalculateUtil.calcAvailableSheds(estimatedShed,ps.getValieClientCount(),ps.getClientCount());
						ep.setParticipantAvilableShed(availableShed);
					}
					
					
					//ep.setParticipantAvilableShed(ps.getAvailableShed());
					ep.setEvent(event);
					parts.add(ep);
			}
			return parts;
		} catch (Exception e) {
			log.error("Internal Error", e);
			FDUtils.addMsgError("Internal Error");
			return Collections.emptyList();
		}
	}
	
	
	
	public List<EventParticipantDataModel> getAllOptOutParticipantsInEvent() {
		ArrayList<EventParticipantDataModel> res = new ArrayList<EventParticipantDataModel>();
		if (event != null) {
	        for (EventParticipant part : event.getEventParticipants()) {
				if (part != null && part.getParticipant() != null 
						&& !part.getParticipant().isClient() && part.getEventOptOut()!=0) {
					EventParticipantDataModel ep = new EventParticipantDataModel(part.getParticipant(), false);
					ep.setShedEnabled(shedEnabled);
					if(!shedEnabled){
						ep.setParticipantAvilableShed(0);
					}else{
						double availableShed = ParticipantShedCalculateUtil.calcAvailableSheds(part.getParticipant(), event, getProgramParticipants());
						ep.setParticipantAvilableShed(availableShed);
					}
					
					//ep.setParticipantAvilableShed(calcAvailableSheds(part.getParticipant()));
					ep.setEvent(event);
					res.add(ep);
				}
			}
		}
		return res;
	}
	
	public List<EventParticipantDataModel> getBiddingParticipantsInEvent() {
		try {
			List<EventBidSummary> summary = getQueryManager()
					.getEventBidSummary(event.getEventName());
			boolean biddingEnabled = false;
			if (isBiddingEnabled()) {
				EventManager eventManager = EJBFactory
					.getBean(EventManager.class);
				// TODO: this doesn't seem right
				event.setState(eventManager.getEventStatusString(event));
				biddingEnabled = true;
				reductionTotals = new double[((DBPEvent) event)
						.getBidBlocks().size()];
			}
			biddingParticipants = new ArrayList<EventParticipantDataModel>();
			for(EventBidSummary bid:summary){
				if (biddingEnabled && !(bid.getBidState().equals(BID_PENDING) && !drasBidding)) {
					Participant p = new Participant();
					p.setParticipantName(bid.getParticipantName());
					p.setAccountNumber(bid.getAccountNumber());
					
					EventParticipantDataModel ep = new EventParticipantDataModel(p, false);
					ep.setBids(getBids(bid));
					ep.setBidStatus(getBidStatus(bid.getBidState()));
					ep.setEvent(event);
					biddingParticipants.add(ep);
				}
			}
			return biddingParticipants;
		} catch (Exception e) {
			log.error("Internal Error", e);
			FDUtils.addMsgError("Internal Error");
			return Collections.emptyList();
		}
	}
	
	private String getBidStatus(String bidState){
		ParticipantBidState state =  ParticipantBidState.valueOf(bidState);
		boolean bidAccepted =  state==ParticipantBidState.Accepted;
		boolean bidAcked  =  state.ordinal() > ParticipantBidState.Pending.ordinal();
		 String status;
	        if (bidAcked) {
	            if (bidAccepted) {
	                status = "Accepted";
	            } else {
	                status = "Rejected";
	            }
	        } else {
	            status = BID_PENDING;
	        }
	        return status;
	}
	
	public boolean isBiddingEnabled() {
		if (bidding
				&& event.getIssuedTime().getTime() < System.currentTimeMillis()) {
			return true;
		} else {
			return false;
		}
	}

	private double[] getBids(EventBidSummary evtBid){
		DBPEvent dbpEvent = (DBPEvent) event;
		List<EventBidBlock> timeBlocks = dbpEvent.getBidBlocks();
		double[] bids = new double[timeBlocks.size()];
		currentBids = evtBid.getBids();
		for (int i = 0; i < timeBlocks.size(); i++) {
			bids[i] = 0.0;
			Calendar cal = Calendar.getInstance();
			// time block start time is now 4 decimal digits in HHmm format.
			int startTime = timeBlocks.get(i).getStartTime();
			int hour = startTime / 100;
			int minute = startTime % 100;
			// match bid with time block.
			if (currentBids != null && currentBids.size() > 0) {
				for (BidEntry currentBid : currentBids) {
					cal.setTime(currentBid.getBlockStart());
					int h = cal.get(Calendar.HOUR_OF_DAY);
					int m = cal.get(Calendar.MINUTE);
					if (h == hour && m == minute) {
						if (currentBid.isActive()) {
							bids[i] = currentBid.getReductionKW();
						}
						break;
					}
				}
			}
			reductionTotals[i] += bids[i];
		}
		return bids;
	}
	
	private double[] getBids(EventManager eventManager, 
		BiddingProgramManager biddingManager, EventParticipant eventParticipant)
	{
		DBPEvent dbpEvent = (DBPEvent)event;
        List<EventBidBlock> timeBlocks = dbpEvent.getBidBlocks();                 
        double[] bids = new double[timeBlocks.size()];
        currentBids = biddingManager.getCurrentBid(
        	event.getProgramName(), event.getEventName(), 
        	eventParticipant.getParticipant().getParticipantName(), false);
        for (int i = 0; i < timeBlocks.size(); i++) {
            bids[i] = 0.0;
            Calendar cal = Calendar.getInstance();
            // time block start time is now 4 decimal digits in HHmm format.
            int startTime = timeBlocks.get(i).getStartTime();
            int hour = startTime / 100;
            int minute = startTime % 100;
            // match bid with time block.
            if (currentBids != null && currentBids.size() > 0) {
            	for(BidEntry currentBid: currentBids) {
                    cal.setTime(currentBid.getBlockStart());
                    int h = cal.get(Calendar.HOUR_OF_DAY);
                    int m = cal.get(Calendar.MINUTE);
                    if (h == hour && m == minute) {
                    	if(currentBid.isActive()) {
                    		 bids[i] = currentBid.getReductionKW();
                    	}
            			break;
            		}
            	}
            }
            reductionTotals[i] += bids[i];
        }
		return bids;
	}

    public List<EventClientSummary> getAllClientsInEvent() {
		if (event != null) {
			try {
				return getQueryManager().getEventClientSummary(event
						.getEventName());
			} catch (SQLException e) {
				log.info(e.getMessage(), e);
				return Collections.emptyList();
			}
		}
		return  Collections.emptyList();
	}
    
    public String getShedAmount(){
    	if(event!=null){
    		return String.valueOf(event.getShedAmount())+" MW";
    	}else{
    		return "";
    	}
    }
    
	public boolean isUsageEnabled() {
		return usageEnabled;
	}

	public void setUsageEnabled(boolean usageEnabled) {
		this.usageEnabled = usageEnabled;
	}
	
	public String getCurrentShed() {
		long start = System.currentTimeMillis();
		String result="";
		if (isUsageEnabled()) {
			ArrayList<String> names = new ArrayList<String>();
			if (event != null) {
				for (EventParticipant part : event.getEventParticipants()) {
					if (part != null && part.getParticipant() != null 
							&& !part.getParticipant().isClient()) {
						names.add(part.getParticipant().getParticipantName());
					}
				}
			}
			if (names.size() > 0) {
				boolean isIndividual = isIndividualparticipant();
				DataManager dataManager = (DataManager)  EJBFactory.getBean(DataManager.class);
				PDataSet baselineset = dataManager.getDataSetByName("Baseline");
				UsageDataManager usageDataManager = (UsageDataManager)  EJBFactory.getBean(UsageDataManager.class);
				
				List<PDataEntry> usage = usageDataManager.findRealTimeEntryListForEvent(event.getEventName(), null, isIndividual, false);
				List<PDataEntry> base = usageDataManager.findBaselineEntryListForEvent(event.getEventName(),baselineset.getUUID(), null,isIndividual, false);
				
				Date eventStart =  null;
				Date eventEnd =   null;
				Date today = new Date();
		        eventStart =  event.getStartTime();
		        eventEnd =   event.getEndTime();
		        if(eventEnd==null) eventEnd = DateUtil.getEndOfDay(eventStart);
		        if(!today.after(eventEnd)){
		       	    // when an event is in active status, calculate the usage report from event start time to current time
		       	    eventEnd = today;
		        }
		        UsageSummary baseEvent = getUsageSummaryFromList(base,eventStart,eventEnd);
		        UsageSummary actualEvent = getUsageSummaryFromList(usage,eventStart,eventEnd);
		        UsageSummary shedEvent = getShedSummaryForEvent(baseEvent,actualEvent);
			      
		        result= Double.toString(Math.round(shedEvent.getAverage()* 100.0)/100.0)+" KW";
			    
			} else {
				result= " 0 KW";
			}
		} else {
			result= "Usage data not enabled";
		}
		long end = System.currentTimeMillis();
        log.info("Get Current Shed costs :"+(end-start)+"milliseconds");
        return result;
	}

	private UsageSummary getUsageSummaryFromList(List<PDataEntry> usageList,
			Date start, Date end) {
		UsageSummary summary = new UsageSummary();
		if (usageList == null || usageList.isEmpty())
			return summary;

		DateEntrySelectPredicate predicate = new DateEntrySelectPredicate(
				start.getTime(), end.getTime());
		List<PDataEntry> usageDes = (List<PDataEntry>) CollectionUtils.select(
				usageList, predicate);

		if (usageDes == null || usageDes.isEmpty())
			return summary;

		double sum = 0;
		for (PDataEntry entry : usageDes) {
			sum += entry.getValue();
		}
		summary.setAverage(sum / usageDes.size());

		return summary;
	}
	  
	private UsageSummary getShedSummaryForEvent(UsageSummary baseEvent,
			UsageSummary actualEvent) {
		UsageSummary summary = new UsageSummary();
		if (baseEvent == null || actualEvent == null
				|| baseEvent.getAverage() == 0 || actualEvent.getAverage() == 0)
			return summary;

		summary.setAverage(baseEvent.getAverage() - actualEvent.getAverage());

		return summary;
	}
	 
	public boolean isBidding()
	{
		return bidding;
	}

	public void setBidding(boolean bidding)
	{
		this.bidding = bidding;
	}

	public boolean isDrasBidding()
	{
		return drasBidding;
	}

	public void setDrasBidding(boolean drasBidding)
	{
		this.drasBidding = drasBidding;
	}
	
	public String[] getBidBlocks()
	{
		if(bidding)
		{
	        DBPEvent dbpEvent = (DBPEvent)event;
	        List<EventBidBlock> timeBlocks = dbpEvent.getBidBlocks();
	        String[] blockTimes = new String[timeBlocks.size()];
	        int i = 0;
	        for (EventBidBlock timeBlock : timeBlocks) {
	            String blockTime = SignalLevelMapper.getTimeBlock(timeBlock);
	            blockTimes[i] = blockTime;
	            i++;
	        }
			return blockTimes;
		}
		else
		{
			return new String[0];
		}
	}

	public List<BidModel> getEditableBids()
	{		
		editableBids = new ArrayList<BidModel>();
	
		if(editParticipantName != null && event != null)
		{
			EventManager eventManager = EJBFactory.getBean(EventManager.class);
			BiddingProgramManager biddingManager =  
				EJB3Factory.getLocalBean(BiddingProgramManager.class);
			
	        for (EventParticipant eventParticipant : event.getEventParticipants()) {
				if (eventParticipant != null && 
						eventParticipant.getParticipant() != null && 
						!eventParticipant.getParticipant().isClient()) {
					if(eventParticipant.getParticipant().getParticipantName().
							equals(editParticipantName)) {
						double[] bids = 
							getBids(eventManager, biddingManager, eventParticipant);
						int i = 0;
						for(String bidBlock: getBidBlocks())
						{
							BidModel bidModel = new BidModel();
							bidModel.setTimeBlock(bidBlock);
							bidModel.setReduction(bids[i]);
							editableBids.add(bidModel);
							i++;
						}
						break;
					}
				}
	        }
		}
		return editableBids;
	}
	
	public double[] getReductionTotals()
	{
		return reductionTotals;
	}

	public void setReductionTotals(double[] reductionTotals)
	{
		this.reductionTotals = reductionTotals;
	}

	public String getEditParticipantName()
	{
		return editParticipantName;
	}

	public void setEditParticipantName(String editParticipantName)
	{
		this.editParticipantName = editParticipantName;
	}
	
	public String editBidsAction()
	{
        final String programName = event.getProgramName();
        final String eventName = event.getEventName();
        final String participantName = editParticipantName;

        BiddingProgramManager biddingManager =  
			EJB3Factory.getLocalBean(BiddingProgramManager.class);

        int i = 0;
        Calendar cal = Calendar.getInstance();
        for(BidEntry bid: currentBids)
        {
            cal.setTime(bid.getBlockStart());
            int h = cal.get(Calendar.HOUR_OF_DAY);
            int m = cal.get(Calendar.MINUTE);
            for (BidModel bidModel : editableBids) {
                String timeBlock = bidModel.getTimeBlock();
                String[] split = timeBlock.split("-");
                String[] times = split[0].split(":");
                if (h == new Integer(times[0]) && m == new Integer(times[1])) {
                    bid.setReductionKW(bidModel.getReduction());
                    break;
                }
            }
        }
        
        try
        {
            biddingManager.setCurrentBid(programName, eventName, participantName, 
            	false, currentBids, true);
        }
        catch (Exception e)
		{
            ValidationException ve = ErrorUtil.getValidationException(e);
            if (ve != null) {
                FDUtils.addMsgError(ve.getLocalizedMessage());
            } else {
                String errMsg = ErrorUtil.getErrorMessage(e);
                if(e instanceof NumberFormatException)
                {
                    errMsg = "Input format error: " +  errMsg;   
                }
                FDUtils.addMsgError(errMsg);
            }
        }
		return null;
	}
	
	public boolean isBidsEditable()
	{
		if(event.getStartTime().after(new Date()) && !drasBidding)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public String sendBidsAction()
	{
        DBPBidProgramEJB ejb = EJBFactory.getBean(DBPBidProgramEJB.class);
        ejb.sendBidsOut((DBPEvent) event);
        reloadEvent();        

        return null;
	}
	
	private void reloadEvent()
	{
        EventManager eventManager = EJBFactory.getBean(EventManager.class);
        event = eventManager.getEvent(event.getProgramName(), event.getEventName());        
	}
	
	public String acceptBidsAction()
	{
		BiddingProgramManager biddingManager =  
			EJB3Factory.getLocalBean(BiddingProgramManager.class);
        for (EventParticipantDataModel participant : biddingParticipants) {
        	if(participant.isBidSelected())
        	{
	            biddingManager.setBidAccepted(event.getProgramName(), 
	            	event.getEventName(), 
	            	participant.getParticipant().getParticipantName(), 
	            	false, true);
        	}
        }
        reloadEvent();        
        return null;
	}
	
	public String acceptAllBidsAction()
	{
		BiddingProgramManager biddingManager =  
			EJB3Factory.getLocalBean(BiddingProgramManager.class);
        for (EventParticipantDataModel participant : biddingParticipants) {
            biddingManager.setBidAccepted(event.getProgramName(), 
            	event.getEventName(), 
            	participant.getParticipant().getParticipantName(), 
            	false, true);
        }
        reloadEvent();        
		return null;
	}
	
	public String rejectBidsAction()
	{
		BiddingProgramManager biddingManager =  
			EJB3Factory.getLocalBean(BiddingProgramManager.class);
        for (EventParticipantDataModel participant : biddingParticipants) {
        	if(participant.isBidSelected())
        	{
	            biddingManager.setBidAccepted(event.getProgramName(), 
	            	event.getEventName(), 
	            	participant.getParticipant().getParticipantName(), 
	            	false, false);
        	}
        }
        reloadEvent();        
		return null;
	}
	
	public String rejectAllBidsAction()
	{
		BiddingProgramManager biddingManager =  
			EJB3Factory.getLocalBean(BiddingProgramManager.class);
        for (EventParticipantDataModel participant : biddingParticipants) {
            biddingManager.setBidAccepted(event.getProgramName(), 
            	event.getEventName(), 
            	participant.getParticipant().getParticipantName(), 
            	false, false);
        }
        reloadEvent();        
		return null;
	}
		
	public List<EventParticipantDataModel> getOptInParticipants() {
			return optInParticipants;
	}
	
	public void optOutAction(){
		try {
			EventManager em = EJBFactory.getBean(EventManager.class);
			em.removeParticipantFromEvent(this.event.getEventName(), this.optoutPart.getParticipant().getParticipantName());
			
			Participant participant = getPm().getParticipantAndShedsOnly(this.optoutPart.getParticipant().getParticipantName());
			EventParticipantDataModel model = new EventParticipantDataModel(participant, false);
			model.setShedEnabled(shedEnabled);
			model.setParticipantAvilableShed(this.optoutPart.getParticipantAvilableShed());
			
			model.setEvent(event);
			DBPEvent dbpEvent = (DBPEvent) event;
			List<EventBidBlock> timeBlocks = dbpEvent.getBidBlocks();
			double[] bids = new double[timeBlocks.size()];
			model.setBids(bids);
			model.setDelete(false);
			optInParticipants.add(model);
			
			this.optoutPart=null;
			if (model.getParticipant().getParticipantName().equals(editParticipantName))
				this.editParticipantName=null;
			
			reloadEvent();
		}catch(Exception e) {
			log.error("Internal Error", e);
			FDUtils.addMsgError("Internal Error");
		}
	}
	
	public void optInAction(){
		
		List<EventParticipant> eps=new ArrayList<EventParticipant>();
		
		DBPEvent dbpEvent = (DBPEvent)event;
        List<EventBidBlock> timeBlocks = dbpEvent.getBidBlocks();                 
		
        Calendar cal = Calendar.getInstance();
        cal.setTime(dbpEvent.getStartTime());
        
        Calendar now = Calendar.getInstance();
        
        List<EventParticipantDataModel> toRemove=new ArrayList<EventParticipantDataModel>();
		for (EventParticipantDataModel part:optInParticipants) {
			if (part.isDelete()) {
				EventParticipant ep=new EventParticipant();
				ep.setEvent(this.event);
				ep.setParticipant(part.getParticipant());
			
                Set<EventParticipantBidEntry> bidEntrySet = new HashSet<EventParticipantBidEntry>();
        		for (int i = 0; i < timeBlocks.size(); i++) {
                    EventParticipantBidEntry entryDao = new EventParticipantBidEntry();
                    entryDao.setActive(true);
                    
                    int start=timeBlocks.get(i).getStartTime()/100;
                    int end=timeBlocks.get(i).getEndTime()/100;
                    
                    if (now.get(Calendar.DAY_OF_YEAR)== cal.get(Calendar.DAY_OF_YEAR) && now.get(Calendar.HOUR_OF_DAY) >= start) {
                        entryDao.setReductionKW(0.0);
//                    } else if (now.get(Calendar.DAY_OF_YEAR)== cal.get(Calendar.DAY_OF_YEAR) && now.get(Calendar.HOUR_OF_DAY)==start ) {
//                    	double reduction= part.getBids()[i]*(60-now.get(Calendar.MINUTE))/60;
//                        entryDao.setReductionKW(reduction);
                    } else {
                        entryDao.setReductionKW(part.getBids()[i]);
                    }
                    
                    cal.set(Calendar.HOUR_OF_DAY, start);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
        			
                    entryDao.setStartTime(cal.getTime());
                    
                    cal.set(Calendar.HOUR_OF_DAY, end);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    entryDao.setEndTime(cal.getTime());
                    entryDao.setEventParticipant(ep);
                    entryDao.setPriceLevel(0.0);
                    
                    bidEntrySet.add(entryDao);
        		}
        		ep.setBidEntries(bidEntrySet);
        		ep.setBidState(ParticipantBidState.Accepted);
				eps.add(ep);
				
				toRemove.add(part);
			}
		}
		EventManager em = EJBFactory.getBean(EventManager.class);
		try {
			em.addEventParticipants(event.getProgramName(), event.getEventName(), eps);
			if (toRemove.size()>0) {
				for (EventParticipantDataModel model:toRemove)
					this.optInParticipants.remove(model);
			}
			
		}catch(Exception e) {
			log.error("Internal Error", e);
			FDUtils.addMsgError("Internal Error");
		}
		
		reloadEvent();		
	}
	
	public void setOptInParticipants(
			ArrayList<EventParticipantDataModel> optInParticipants) {
		this.optInParticipants = optInParticipants;
	}

	public boolean isEventOptIn() {
		return eventOptIn;
	}

	public void setEventOptIn(boolean eventOptIn) {
		this.eventOptIn = eventOptIn;
	}

	public EventParticipantDataModel getOptoutPart() {
		return optoutPart;
	}

	public void setOptoutPart(EventParticipantDataModel optoutPart) {
		this.optoutPart = optoutPart;
	}

	public String nextStateBidsAction()
	{
        DBPBidProgramEJB ejb = EJBFactory.getBean(DBPBidProgramEJB.class);
        ejb.nextState(event.getProgramName(),(DBPEvent) event);

        reloadEvent();        
		return null;
	}	

	public int getBidsPanelHeight()
	{
		return 120 + (editableBids.size() * 30);
	}
	public NativeQueryManager getQueryManager() {
		if (nativeQuery == null)
			nativeQuery = EJB3Factory.getBean(NativeQueryManager.class);
		return nativeQuery;
	}

	private static ParticipantManager pm;
	
	private static ParticipantManager getPm() {
		if(pm==null){
			pm = (ParticipantManager) EJBFactory.getBean(ParticipantManager.class);
		}
		return pm;
	}
	
	private boolean shedEnabled=true;
	private void retrieveShedEnabled(){
		SystemManager systemManager = EJB3Factory.getLocalBean(SystemManager.class);
        PSS2Features features = systemManager.getPss2Features();
        shedEnabled = features.isFeatureShedInfoEnabled();
	}
	
	public EvetLocationModel getLocationModel(){
		if(this.locationModel==null){
			locationModel= new EvetLocationModel(getEvent().getEventName());
		}
		return locationModel;
	}
	
	private boolean canEditBid;
	private boolean canEndEventForLocation;
	private boolean canParticipantOutOutOfEvent;
	
	public boolean getCanEditBid(){
		return this.canEditBid;
	}
	public void setCanEditBid(boolean value){
	 this.canEditBid =  value;
	}
	
	public boolean getCanEndEventForLocation(){
		return this.canEndEventForLocation;
	}
	public void setCanEndEventForLocation(boolean value){
		this.canEndEventForLocation = value;
	}
	public boolean getCanParticipantOptOutOfEvent(){
		return this.canParticipantOutOutOfEvent;
	}
	public void setCanParticipantOptOutOfEvent(boolean value){
		this.canParticipantOutOutOfEvent = value;
	}
	private void buildViewLayout(){
		try {
			ViewBuilderManager viewManager = getViewBuilderManager();
			viewManager.buildBidEventDetailViewLayout(this);
			viewManager.buildEventLocationDetailViewLayout(this);
			viewManager.buildEventParticipantViewLayout(this);
		} catch (NamingException e) {
			// log exception
		}
	}
	private ViewBuilderManager getViewBuilderManager() throws NamingException{
		return (ViewBuilderManager) new InitialContext().lookup("pss2/ViewBuilderManagerBean/local");
	}
	
}
