/**
 * 
 */
package com.akuacom.pss2.drw;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.type.EntityType;
import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.ejb3.annotation.RemoteBinding;

import com.akuacom.drw.topic.TopicPublisher;
import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.MasterDetailFactory;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.pss2.drw.constants.DrwConstants;
import com.akuacom.pss2.drw.core.Event;
import com.akuacom.pss2.drw.core.EventDetail;
import com.akuacom.pss2.drw.core.HistoryEvtVo;
import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.core.Program;
import com.akuacom.pss2.drw.core.ZipCode;
import com.akuacom.pss2.drw.core.ZipCodeEntry;
import com.akuacom.pss2.drw.eao.EventEAO;
import com.akuacom.pss2.drw.eao.LocationEAO;
import com.akuacom.pss2.drw.eao.ProgramEAO;
import com.akuacom.pss2.drw.eao.ZipCodeEAO;
import com.akuacom.pss2.drw.value.EventValue;
import com.akuacom.pss2.drw.value.LocationValue;
import com.akuacom.pss2.drw.value.WeatherValue;
import com.akuacom.utils.DateUtil;
import com.akuacom.utils.drw.CacheNotificationMessage;

/**
 * the class DREventManagerBean
 */
@Stateless
@LocalBinding(jndiBinding="dr-pro/DREventManager/local")
@RemoteBinding(jndiBinding="dr-pro/DREventManager/remote")
public class DREventManagerBean implements DREventManager.L, DREventManager.R {
	
	@EJB
	ProgramEAO.L programEAO;
	
	@EJB
	EventEAO.L eventEAO;

	@EJB
	ZipCodeEAO.L zipCodeEAO;
	
	@EJB
	LocationEAO.L locationEAO;
	
	@EJB
	TopicPublisher.L topicPublisher;
	
    private static final Logger log = Logger.getLogger(DREventManagerBean.class);

    public static String ALL="All";
    
    @EJB
    DrwSQLExecutor.L drwSqlExecutor;
    
	/* (non-Javadoc)
	 * @see com.akuacom.pss2.drw.DREventManager#getProgram()
	 */
	@Override
	public List<Program> getAllProgram() {
		return programEAO.getAll(Program.class.getName());
	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.drw.DREventManager#getProgram(java.lang.String)
	 */
	@Override
	public Program getProgramByUtilityName(String utilityProgramName) {
		return programEAO.findByUtilityName(utilityProgramName);
	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.drw.DREventManager#getActiveEvent(java.lang.String)
	 */
	@Override
	public List<EventDetail> getActiveEvent(String program) {
		return eventEAO.getActiveEvent(program);
	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.drw.DREventManager#getActiveEvent(java.lang.String, java.util.List)
	 */
	@Override
	public List<EventDetail> getActiveEvent(String program, List<String> rates) {
		return eventEAO.getActiveEventByProduct(program, rates);
	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.drw.DREventManager#getHistoryEvent(java.lang.String)
	 */
	@Override
	public List<EventDetail> getHistoryEvent(String program, Date start, Date end) {
		return eventEAO.getHistoryEvent(program, start, end);
	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.drw.DREventManager#getHistoryEvent(java.lang.String, java.util.List, java.util.Date, java.util.Date)
	 */
	@Override
	public List<EventDetail> getHistoryEvent(String program, List<String> rates,
			Date start, Date end) {
		return eventEAO.getHistoryEvent(program, rates, start, end);
	}

	@Override
	public Event createEvent(String programName, String product, String eventName, Date startTime, Date endTime, Date issueTime, String[] slaps) {
		Event event=new Event();
		event.setProgramName(programName);
		event.setProduct(product);
		event.setEventName(eventName);
		event.setStartTime(startTime);
		event.setIssuedTime(issueTime);
		
		List<Location> locations=new ArrayList<Location>();
		for (String slap:slaps) {
			if (slap.equals("DLAP_SCE")) {
				Location location=new Location();
				location.setID(-1);
				location.setType("SLAP");
				locations.add(location);
			} else {
				Location location=locationEAO.getLocationByTypeAndNumber("SLAP", slap.substring(slap.length()-4, slap.length()));
				if (location!=null) {
					locations.add(location);
				}
			}
		}
		
		return doEventCreation(event, locations, endTime, endTime);
	}
	
	@Override
	public Event createEvent(String programName, String product, String dispatchType,String locationNumber, String eventName, Date issueTime,Date startTime, Date endTime,boolean isEstimated) {
		Event event=new Event();
		event.setProgramName(programName);
		event.setProduct(product);
		event.setEventName(eventName);
		event.setIssuedTime(issueTime);
		event.setStartTime(startTime);
		
		List<Location> locations=new ArrayList<Location>();
		String[] locationNumberList = locationNumber.split("-");
		
		if(DrwConstants.LOCATION_TYPE_ALL.equalsIgnoreCase(dispatchType)){
			Location allSlap = new Location();
			allSlap.setName("All"); 
			allSlap.setType("SLAP");
			allSlap.setNumber("");
			allSlap.setID(-1);
			locations.add(allSlap);
		}else if(DrwConstants.LOCATION_TYPE_SLAP.equalsIgnoreCase(dispatchType)){
			for(String ln:locationNumberList){
				ln=ln.trim();
				Location location=locationEAO.getLocationByTypeAndNumber(DrwConstants.LOCATION_TYPE_SLAP, ln);
				if (location!=null) {
					locations.add(location);
				}
			}
			
		}else if(DrwConstants.LOCATION_TYPE_ABANK.equalsIgnoreCase(dispatchType)){
			for(String ln:locationNumberList){
				ln=ln.trim();
				Location location=locationEAO.getLocationByTypeAndNumber(DrwConstants.LOCATION_TYPE_ABANK, ln);
				if (location!=null) {
					locations.add(location);
				}
			}
			
		}else if(DrwConstants.LOCATION_TYPE_SUBSTATION.equalsIgnoreCase(dispatchType)){
			for(String ln:locationNumberList){
				ln=ln.trim();
				Location location=locationEAO.getLocationByTypeAndNumber("Substation", ln);
				if (location!=null) {
					locations.add(location);
				}
			}
		}
		Date eD = null;
		Date aD = null;
		if(isEstimated){
			eD=endTime;
		}else{
			aD=endTime;
		}
		return createEvent(event, locations,aD,eD);
		
	}
	
	@Override
	public Event createEvent(String programName, String product, String dispatchType,String locationNumber, String eventName, Date issueTime,Date startTime, Date endTime) {
		Event event=new Event();
		event.setProgramName(programName);
		event.setProduct(product);
		event.setEventName(eventName);
		event.setIssuedTime(issueTime);
		event.setStartTime(startTime);
		
		List<Location> locations=new ArrayList<Location>();
		String[] locationNumberList = locationNumber.split("-");
		
		if(DrwConstants.LOCATION_TYPE_ALL.equalsIgnoreCase(dispatchType)){
			Location allSlap = new Location();
			allSlap.setName("All"); 
			allSlap.setType("SLAP");
			allSlap.setNumber("");
			allSlap.setID(-1);
			locations.add(allSlap);
		}else if(DrwConstants.LOCATION_TYPE_SLAP.equalsIgnoreCase(dispatchType)){
			for(String ln:locationNumberList){
				ln=ln.trim();
				Location location=locationEAO.getLocationByTypeAndNumber(DrwConstants.LOCATION_TYPE_SLAP, ln);
				if (location!=null) {
					locations.add(location);
				}
			}
			
		}else if(DrwConstants.LOCATION_TYPE_ABANK.equalsIgnoreCase(dispatchType)){
			for(String ln:locationNumberList){
				ln=ln.trim();
				Location location=locationEAO.getLocationByTypeAndNumber(DrwConstants.LOCATION_TYPE_ABANK, ln);
				if (location!=null) {
					locations.add(location);
				}
			}
			
		}else if(DrwConstants.LOCATION_TYPE_SUBSTATION.equalsIgnoreCase(dispatchType)){
			for(String ln:locationNumberList){
				ln=ln.trim();
				Location location=locationEAO.getLocationByTypeAndNumber("Substation", ln);
				if (location!=null) {
					locations.add(location);
				}
			}
		}
		return createEvent(event, locations, null, endTime);
	}
	@Override
	public void cancelEvent(String eventName, Date endTime, boolean active) {
		List<Event> evts = eventEAO.getByEventName(eventName);
		if (active) {
			List<String> eventDetails=eventEAO.getEventsByEventName(eventName);
			eventEAO.updateEndTime(eventDetails, endTime, true);
		} else {
			eventEAO.removeEventByEventName(eventName);
		}
		
		HashSet<CacheNotificationMessage> messages=new HashSet<CacheNotificationMessage>();
		
		for(Event event: evts){
			CacheNotificationMessage message=new CacheNotificationMessage();
			message.setProgramName(event.getProgramName());
			message.setProduct(event.getProduct());
			message.setuUID(event.getUUID());
			message.setActive(event.getStartTime().before(new Date()));
			messages.add(message);
		}
		
		topicPublisher.publish(messages);
		
	}
	
	@Override
	public Event createEvent(Event event) {
		return createEvent(event, null, null, null);
	}
	
	protected Event doEventCreation(Event event, List<Location> locations, Date actualEndTime, 
			Date estimatedEndTime) {
		try {
			Set<EventDetail> createdDetails=new HashSet<EventDetail>();
			if (locations!=null && locations.size()!=0) {
				for (Location loc: locations) {
					EventDetail detail=new EventDetail();
					List<ZipCode> zipCodes=new ArrayList<ZipCode>();
					
					if (loc.getID()<0) {
						detail.setAllLocationType(loc.getType());
						zipCodes=zipCodeEAO.getZipCodeByLocationType(loc.getType());
					} else {
						detail.setLocation(loc);
						zipCodes=zipCodeEAO.getZipCodeByLocation(loc);
					}

					if (actualEndTime!=null)
						detail.setActualEndTime(actualEndTime);
					if (estimatedEndTime!=null)
						detail.setEstimatedEndTime(estimatedEndTime);
					
					detail.setLastModifiedTime(new Date());
					
					//set zip codes
					Set<ZipCodeEntry> entries=new HashSet<ZipCodeEntry>();
					for (ZipCode zipCode: zipCodes) {
						ZipCodeEntry entry=new ZipCodeEntry();
						entry.setZipCode(zipCode.getZipCode());
						entry.setCountyName(zipCode.getCountyName());
						entry.setCountyNo(zipCode.getCountyNo());
						entry.setCityName(zipCode.getCityName());
						entry.setBlock(zipCode.getBlock());
						entry.setEventDetail(detail);
						entries.add(entry);
					}
					detail.setZipCodeEntries(entries);
					
					detail.setEvent(event);
					createdDetails.add(detail);
				}
			}
			
			if (event.getDetails()!=null) {
				event.getDetails().addAll(createdDetails);
			} else {
				event.setDetails(createdDetails);
			}
			
			Event evt =  eventEAO.create(event);
			//add by Frank
			publish(event.getProgramName(), event.getProduct(), event.getStartTime(), event.getUUID());
			
			return evt;
		}catch(Exception e) {
			log.error(e.getMessage());
			log.debug(e.getStackTrace());
			throw new EJBException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.akuacom.pss2.drw.DREventManager#createEvent(com.akuacom.pss2.drw.core.Event)
	 */
	@Override
	public Event createEvent(Event event, List<Location> locations, Date actualEndTime, 
			Date estimatedEndTime) {
		//TODO: move to doEventCreation method
		try {
			Set<EventDetail> createdDetails=new HashSet<EventDetail>();
			if (locations!=null && locations.size()!=0) {
				for (Location loc: locations) {
					EventDetail detail=new EventDetail();
					List<ZipCode> zipCodes=new ArrayList<ZipCode>();
					
					if (loc.getID()<0) {
						detail.setAllLocationType(loc.getType());
						zipCodes=zipCodeEAO.getZipCodeByLocationType(loc.getType());
					} else {
						detail.setLocation(loc);
						zipCodes=zipCodeEAO.getZipCodeByLocation(loc);
					}

					if (actualEndTime!=null)
						detail.setActualEndTime(actualEndTime);
					if (estimatedEndTime!=null){
						detail.setEstimatedEndTime(estimatedEndTime);
						//after Event transfer to DR event, this has not estimatedEndTime any more
//						detail.setActualEndTime(estimatedEndTime);
					}
						
					detail.setLastModifiedTime(new Date());
					
					//set zip codes
					Set<ZipCodeEntry> entries=new HashSet<ZipCodeEntry>();
					for (ZipCode zipCode: zipCodes) {
						ZipCodeEntry entry=new ZipCodeEntry();
						entry.setZipCode(zipCode.getZipCode());
						entry.setCountyName(zipCode.getCountyName());
						entry.setCountyNo(zipCode.getCountyNo());
						entry.setCityName(zipCode.getCityName());
						entry.setBlock(zipCode.getBlock());
						entry.setEventDetail(detail);
						entries.add(entry);
					}
					detail.setZipCodeEntries(entries);
					
					detail.setEvent(event);
					createdDetails.add(detail);
				}
			}
			
			if (event.getDetails()!=null) {
				event.getDetails().addAll(createdDetails);
			} else {
				event.setDetails(createdDetails);
			}
			
			event=eventEAO.create(event);
			
			if (actualEndTime==null || actualEndTime.after(new Date())) {
				publish(event.getProgramName(), event.getProduct(), event.getStartTime(), event.getUUID());
			}
		}catch(Exception e) {
			log.error(e.getMessage());
			log.debug(e.getStackTrace());
			throw new EJBException(e);
		}
		
		return event;
	}
	@Override
	public List<EventDetail> getAutoDispatchEvents(String programName,String product, Date startTime,String dispatchType,String locationNumber,boolean actualEndTimeFlag){
		return eventEAO.getAutoDispatchEvents(programName, product, startTime, dispatchType, locationNumber,actualEndTimeFlag);
	}
	
	@Override
	public int updateEndTime(List<String> eventDetails, Date endTime, boolean actual){
		int updated=0;
		try {
			if (eventDetails.size()>0) {
				updated= eventEAO.updateEndTime(eventDetails, endTime, actual);
				
				List<Event> events=eventEAO.getEventByEventDetail(eventDetails);
				HashSet<CacheNotificationMessage> messages=new HashSet<CacheNotificationMessage>();
				for (Event event:events) {
					CacheNotificationMessage message=new CacheNotificationMessage();
					message.setProgramName(event.getProgramName());
					message.setProduct(event.getProduct());
					message.setuUID(event.getUUID());
					message.setActive(event.getStartTime().before(new Date()));
					messages.add(message);
				}
				topicPublisher.publish(messages);
			}
		}catch(Exception e) {
			log.error(e.getMessage());
			log.debug(e.getStackTrace());
			throw new EJBException(e);
		}
		return updated;
	}
	
	
	/* (non-Javadoc)
	 * @see com.akuacom.pss2.drw.DREventManager#removeEvent(java.util.List)
	 */
	@Override
	public int removeEvent(List<String> eventDetails) {
		int deleted=0;
		try {
			if (eventDetails.size()>0) {
				List<Event> events=eventEAO.getActiveEventByEventDetail(eventDetails);
				deleted = eventEAO.removeEvent(eventDetails);
				
				HashSet<CacheNotificationMessage> messages=new HashSet<CacheNotificationMessage>();
				for (Event event:events) {
					CacheNotificationMessage message=new CacheNotificationMessage();
					message.setProgramName(event.getProgramName());
					message.setProduct(event.getProduct());
					message.setuUID(event.getUUID());
					message.setActive(event.getStartTime().before(new Date()));
					messages.add(message);
				}
				topicPublisher.publish(messages);
			}
		}catch(Exception e){
			log.error(e.getMessage());
			log.debug(e.getStackTrace());
			throw new EJBException(e);
		}
		return deleted;
	}
	
	private void publish(String programName, String product, Date startTime, String uuid) {
		CacheNotificationMessage message=new CacheNotificationMessage();
		message.setProgramName(programName);
		message.setuUID(uuid);
		message.setActive(startTime.before(new Date()));
		message.setProduct(product);			
		topicPublisher.publish(message);
	}
	
	@Override
	public List<EventDetail> getHistoryEventByEnd(String program, Date end) {
		return eventEAO.getHistoryEventByEnd(program, end);
	}

	@Override
	public List<EventDetail> getHistoryEventByStart(String programName,
			Date start) {
		return eventEAO.getHistoryEventByStart(programName, start);
	}

	@Override
	public List<EventDetail> getActiveEventLimited(String program, Integer limit) {
		return eventEAO.getActiveEventLimited(program, limit);
	}

	@Override
	public List<EventDetail> getHistoryEventByStartLimited(String programName,
			Date start, Integer limit) {
		return eventEAO.getHistoryEventByStartLimited(programName, start, limit);
	}

	@Override
	public List<EventDetail> getHistoryEvents(String programName,
			boolean filterByStart, Date from, Date to, String rate,
			String locationNo, String locationName) {
		return eventEAO.getHistoryEvents(programName, filterByStart, from, to, rate, locationNo, locationName);
	}

	@Override
	public List<EventDetail> getHistoryEvents(String programName,
			boolean filterByStart, Date from, Date to, List<String> blocks) {
		// TODO Auto-generated method stub
		return eventEAO.getHistoryEvents(programName, filterByStart, from, to, blocks);
	}
	@Override
	public List<EventDetail> getHistoryEvents(String programName,
			boolean filterByStart, Date from, Date to, List<String> blocks,List<String> products) {
		// TODO Auto-generated method stub
		return eventEAO.getHistoryEvents(programName, filterByStart, from, to, blocks,products);
	}
	@Override
	public List<EventDetail> getHistoryEvents(String programName,
			boolean filterByStart, Date from, Date to, String rate,
			String locationNo, String locationName,List<String> dispatchTypes) {
		StringBuffer sb = new StringBuffer();
		sb.append("  SELECT CAST(ed.uuid AS CHAR) uuid, ev.programName programName, ev.startTime startTime, ev.issuedTime issuedTime, ev.product product, ev.comment 'comment', ");
		sb.append("  ed.actualEndTime endTime,CAST(ed.locationID AS CHAR) locationID,ed.allLocationType allLocationType,ed.blockNames blockNames, ");
		sb.append("  loc.type locationType,loc.number locationNumber,loc.name locationName ");
		sb.append("  FROM `event_detail` ed INNER JOIN event ev ON ed.event_uuid = ev.uuid ");
		sb.append("  LEFT JOIN location loc ");
		sb.append("  ON ed.locationID = loc.id ");
		sb.append("  WHERE  ");
		sb.append("  ed.actualEndTime IS NOT NULL AND ed.actualEndTime<=NOW() ");
		sb.append("  AND ev.programName = ${programName}  ");
		sb.append("  AND DATE(ev.startTime) >= DATE(${start}) ");
		sb.append("  AND DATE(ed.actualEndTime) <= DATE(${end}) ");
		sb.append("  [AND ev.product = ${product}] ");
		sb.append("  [AND loc.number=${locationNo}] ");
		sb.append("  [AND loc.name =${locationName}] ");
		sb.append("  [AND loc.type IN ${dispatchTypes} ] ");

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName", programName);
		params.put("start", from);
		params.put("end", to);
		
		if(rate != null&&!"".equals(rate)){
			params.put("product", rate);
		}else{
			params.put("product", null);
		}
		if(locationNo != null&&!"".equals(locationNo)){
			params.put("locationNo", locationNo);
		}else{
			params.put("locationNo", null);
		}
		if(locationName != null&&!"".equals(locationName)){
			params.put("locationName", locationName);
		}else{
			params.put("locationName", null);
		}
		if(dispatchTypes != null&&!dispatchTypes.isEmpty()){
			params.put("dispatchTypes", dispatchTypes);
		}else{
			params.put("dispatchTypes", null);
		}
		
		List<HistoryEvtVo> result=new ArrayList<HistoryEvtVo>();
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sb.toString(), params);
			
			result = drwSqlExecutor.doNativeQuery(sql,params, new ListConverter<HistoryEvtVo>(
					new ColumnAsFeatureFactory<HistoryEvtVo>(HistoryEvtVo.class)));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		if(result.isEmpty()) return new ArrayList<EventDetail>();
		
		List<EventDetail> temp = new ArrayList<EventDetail>();
		for(HistoryEvtVo vo : result){
			EventDetail ed = new EventDetail();
			Event ev = new Event();
			ev.setIssuedTime(vo.getIssuedTime());
			ev.setStartTime(vo.getStartTime());
			ev.setComment(vo.getComment());
			ev.setProduct(vo.getProduct());
			ev.setProgramName(vo.getProgramName());
			
			ed.setEvent(ev);
			
			Location loc = new Location();
			loc.setType(vo.getLocationType());
			loc.setName(vo.getLocationName());
			loc.setNumber(vo.getLocationNumber());
			ed.setLocation(loc);
			
			if(StringUtils.isEmpty(vo.getAllLocationType())&&StringUtils.isEmpty(vo.getLocationType())){
				ed.setAllLocationType("-");
			}else{
				ed.setAllLocationType(vo.getAllLocationType());
			}
			ed.setActualEndTime(vo.getEndTime());
			ed.setUUID(vo.getUuid());
			ed.setBlockNames(vo.getBlockNames());
			
			temp.add(ed);
		}
		
		return temp;
		
	}

	@Override
	public void publishLocationMessage(String programName) {
		CacheNotificationMessage message=new CacheNotificationMessage();
		message.setProgramName(programName);
		topicPublisher.publish(message);
	}
}
