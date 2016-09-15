package com.akuacom.pss2.ge;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.akuacom.pss2.cache.GeInterfaceCache;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.usage.UsageDataManager;
import com.akuacom.pss2.data.usage.UsageSummary;
import com.akuacom.pss2.data.usage.UsageUtil;
import com.akuacom.pss2.data.usage.calcimpl.DateEntrySelectPredicate;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantManager;
import com.akuacom.pss2.history.ClientParticipationStatus;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantEAO;
import com.akuacom.pss2.timer.TimerManagerBean;
import com.akuacom.utils.DateUtil;
import com.akuacom.utils.lang.TimingUtil;

@Stateless
public class GeTimerInterfaceManagerBean extends TimerManagerBean implements GeTimerInterfaceManager.L, GeTimerInterfaceManager.R {
	@EJB private ProgramParticipantEAO.L programParticipantEAO;
	@EJB private EventEAO.L eventEAO;
	@EJB private UsageDataManager.L usageDataManager;
	@EJB private DataManager.L dataManager;
	@EJB private UsageDataManager.L usageManager;
	@EJB private GeConfigurationEAO.L configurationEAO;
	@EJB private GeInterfaceManager.L geManager;
	@EJB private ParticipantEAO.L partEAO;
	@EJB private EventParticipantManager.L eventParticipantManager;
	private static final Logger log = Logger.getLogger(GeTimerInterfaceManagerBean.class);
	
	@Override
	public void timerService(Date time, int interval) {
		System.out.println("Ge timer running at "+new Date()+"  with interval: "+interval);
		GeConfiguration conf = geManager.getGeConfiguration();
		String programName = conf.getProgramName();
		List<ProgramParticipant> pps = programParticipantEAO.getProgramParticipantsByProgram(programName);
		if(pps==null||pps.isEmpty()){
			return;
		}
		
		List<Event> event = eventEAO.findByProgramName(programName);
		Event currentEvent = getNextEvent(event);
		for(ProgramParticipant pp : pps){
			Date lastContactTime = null;
			GeVo vo = new GeVo();
			vo.setTimeStamp(time);
			vo.setAccountNo(pp.getParticipant().getAccountNumber());
			String pname = pp.getParticipantName();
			boolean isIndividual = usageDataManager.isIndividualparticipant(pname);
			
			
			
//			vo.setLastPoll(pp.getParticipant().getCommTime());//how to dispalay a participant has never online?  lastcontacttime  participant commTime
			
			List<Participant> clients=partEAO.findClientsByParticipant(pp.getParticipantName());
			if (clients!=null && clients.size()>0) {
				lastContactTime = clients.get(0).getCommTime();
				vo.setLastPoll(clients.get(0).getCommTime());
			}
			
			String paEvtOpt = getEvtParticipantOptStatus(currentEvent, pp, isIndividual);
			vo.setEvtOptStatus(paEvtOpt);
			
			String ppOpt = getProgramParticipantOptStatus(pp, isIndividual);
			vo.setProgOptStatus(ppOpt);
			
			PDataSet usageSet = dataManager.getDataSetByName("Usage");
			PDataSet baselineSet = dataManager.getDataSetByName("Baseline");
			List<PDataEntry> usage = new ArrayList<PDataEntry>();
			if(isIndividual){
				usage = usageManager.findRealTimeEntryListForParticipant(pname, usageSet.getUUID(), time, isIndividual, true, false);// only return the actual point
		    }else{
		    	usage = usageManager.findRealTimeEntryListForParticipant(pname, usageSet.getUUID(), time, isIndividual, false, false);
		    }
			
//			List<PDataEntry> usage = usageManager.findRealTimeEntryListForParticipant(pname, usageSet.getUUID(), time, isIndividual, true);
			List<PDataEntry> baseline = usageManager.findBaselineEntryListForParticipant(pname, time, baselineSet.getUUID(),isIndividual,false);
		    UsageSummary usageSummary = getUsageSummaryFromList4Event(usage,  DateUtil.add(time, Calendar.MINUTE, 0-interval), time);
		    UsageSummary baseSummary = getUsageSummaryFromList4Event(baseline,  DateUtil.add(time, Calendar.MINUTE, 0-interval), time);
			
			vo.setLoad(""+convertNumber(usageSummary.getAverage()));//usage
			vo.setLoad(transferPresentate(vo.getLoad()));
			
			if(StatusModes.OUT.toString().equalsIgnoreCase(paEvtOpt)||(event.size()<1)){
				vo.setCallOff(StatusModes.NA.toString());//shed
			}else{
				vo.setCallOff(""+convertNumber(baseSummary.getAverage()-usageSummary.getAverage()));//shed	
			}
			vo.setCallOff(transferPresentate(vo.getCallOff()));
			
			GeClient cli = new GeClient(conf.getUrl(),conf.getNameSpace(),conf.getMethod());
			try {
				cli.updateScalarValues(vo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	
	private String transferPresentate(String input){
		if("0.0".equalsIgnoreCase(input.trim())){
			return StatusModes.NA.toString();
		}else{
			return input;
		}
	}
	
	private String getEvtParticipantOptStatus(Event currentEvent,
			ProgramParticipant pp, boolean isIndividual) {
		String paEvtOpt = null;
		if(currentEvent==null){
			paEvtOpt = StatusModes.NA.toString();
		}else{
			if(!isIndividual){
				paEvtOpt = StatusModes.NA.toString();
			}else{
				paEvtOpt = StatusModes.OUT.toString();
				
				List<Participant> clients=partEAO.findClientsByParticipant(pp.getParticipantName());
				if (clients!=null && clients.size()>0) {
					Participant client = clients.get(0);
					EventParticipant ep = eventParticipantManager.getEventParticipant(currentEvent.getEventName(),client.getParticipantName(), true);
					if(ep!=null){
						if(ep.getEventOptOut()==ClientParticipationStatus.ACTIVE_EVENT_OPT_OUT.getValue()||ep.getEventOptOut()==ClientParticipationStatus.INACTIVE_EVENT_OPT_OUT.getValue()){
							paEvtOpt = StatusModes.OUT.toString();
						}else{
							paEvtOpt = StatusModes.IN.toString();
						}
					}
				}
//				
//				
//				Set<EventParticipant> eps = currentEvent.getEventParticipants();
//				for(EventParticipant ep : eps){
//					if(ep.getParticipant().getParticipantName().equalsIgnoreCase(pp.getParticipantName())){
//						paEvtOpt = StatusModes.IN.toString();
//						break;
//					}
//				}
			}
			
		}
		return paEvtOpt;
	}
	private String getProgramParticipantOptStatus(ProgramParticipant pp,
			boolean isIndividual) {
		String ppOpt = StatusModes.NA.toString();
		if(isIndividual){
			ppOpt = StatusModes.OUT.toString();
			List<Participant> clients=partEAO.findClientsByParticipant(pp.getParticipantName());
			if (clients!=null && clients.size()>0) {
				Participant client = clients.get(0);
				Set<ProgramParticipant> pplist = client.getProgramParticipants();
				for(ProgramParticipant instance:pplist){
					if(instance.getProgramName().equalsIgnoreCase(pp.getProgramName())){
						// participanting(state = 1) & opt in(opt status = 0)
						if (instance.getState() == 1 && instance.getOptStatus() == 0){
							ppOpt = StatusModes.IN.toString();
						}
					}
				}
			}
		}
		return ppOpt;
	}
	
    private UsageSummary getUsageSummaryFromList4Event(List<PDataEntry> usageList, Date start, Date end){
    	UsageSummary summary = new UsageSummary();
    	if(usageList==null||usageList.isEmpty()) return summary;
    	//curTime >= startTime&& curTime <= endTime
    	
    	DateEntrySelectPredicate predicate = new DateEntrySelectPredicate(UsageUtil.getCurrentTime(start), UsageUtil.getCurrentTime(end), DateEntrySelectPredicate.MT_START_LET_END, PDataEntry.class,"time");
    	List<PDataEntry> usageDes = (List<PDataEntry>) CollectionUtils.select(usageList, predicate);
    	
    	if(usageDes==null||usageDes.isEmpty()) return summary;
    	
    	double sum = 0;
    	for(PDataEntry entry : usageDes){
    		sum += entry.getValue();
    	}
    	summary.setAverage(sum/usageDes.size());
    	double hours = (end.getTime() - start.getTime()) / 3600000.0;
    	summary.setTotal(summary.getAverage() * hours);
    	
    	return summary;
    }
    
	private static Double convertNumber(Double in){
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setGroupingUsed(false);
		nf.setMinimumFractionDigits(0);
		nf.setMaximumFractionDigits(2);
		
		return getDoubleValue(in, nf);
	}
	
	private static Double getDoubleValue(Double in, NumberFormat nf){
	   	
	   	if(Double.isNaN(in)) return 0.0;
	   	
	   	return Double.valueOf(nf.format(in));
	}
	
	private Event getNextEvent(List<Event> events){
		if(events==null||events.isEmpty()) return null;
		Collections.sort(events, new EventComparator());
		
		return events.get(0);
	}
	class EventComparator implements Comparator<Event> {
	    @Override
	    public int compare(Event a, Event b) {
	        return a.getStartTime().compareTo(b.getStartTime());
	    }
	}
	
	
	enum StatusModes {
		NA ("N/A"),
	    IN ("IN"),
	    OUT ("OUT");

	    private final String name;       

	    private StatusModes(String s) {
	        name = s;
	    }

	    public boolean equalsName(String otherName){
	        return (otherName == null)? false:name.equals(otherName);
	    }

	    public String toString(){
	       return name;
	    }

	}

    private static final String TIMER_NAME="GE_INTERFACE_TIMER";

	@Resource
	protected SessionContext context;
	private GeInterfaceCache cache = GeInterfaceCache.getInstance();
	@Override
	public String getTimersInfo() {
		Collection timersList = context.getTimerService().getTimers();
		return super.getTimersInfo(timersList);
	}

	@Override
	public void createTimers() {
		Integer originInterval = cache.getRunningState();
		if(originInterval==0) return;
		
		System.out.println("Ge timer create at "+new Date()+"  with interval: "+originInterval);
		
		try {
			cancelTimers();

			javax.ejb.TimerService timerService = context.getTimerService();
	
			timerService.createTimer(new Date(), originInterval
						* TimingUtil.MINUTE_MS, TIMER_NAME);

		} catch (Exception e) {
			String message = "Failed to create " +TIMER_NAME+": "+ e.getMessage();
			log.error(message);
		}
		
	}

	@Override
	public void cancelTimers() {
		javax.ejb.TimerService timerService = context.getTimerService();
        Collection timersList = timerService.getTimers();
        super.cancelTimers(timersList);
		
	}

	@Override	
	@Timeout
	public void timeoutHandler(Timer timer) {
		 if (timer.getInfo() != null 
	                && TIMER_NAME.equals(timer.getInfo())) {
	            Integer originInterval = cache.getRunningState();
	    		System.out.println(originInterval);
	            	timerService(new Date(), Integer.valueOf(originInterval));
	        }
		
	}

}
