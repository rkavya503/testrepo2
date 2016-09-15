package com.akuacom.pss2.drw.event.creation.sdp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.akuacom.jsf.model.LazyListContentProvider;
import com.akuacom.jsf.model.TreeContentProvider;
import com.akuacom.pss2.drw.core.Event;
import com.akuacom.pss2.drw.core.EventDetail;
import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.event.AbstractEvent;
import com.akuacom.pss2.drw.event.EventDetailAdapter;
import com.akuacom.pss2.drw.event.UiEventDetail;
import com.akuacom.pss2.drw.event.creation.AbstractEventCreation;
import com.akuacom.pss2.drw.event.creation.CommonValidator;
import com.akuacom.pss2.drw.event.creation.CommonValidator.MSG;
import com.akuacom.pss2.drw.event.creation.CompoundValidator;
import com.akuacom.pss2.drw.event.creation.EventLocationValidator;
import com.akuacom.pss2.drw.event.creation.Wizard;
import com.akuacom.pss2.util.EventUtil;

public class SdpEventCreation extends AbstractEventCreation implements Serializable {
	
	private static final long serialVersionUID = -7801600190498201386L;
	private static final Logger log = Logger.getLogger(SdpEventCreation.class);
	
	private AbstractEvent sdpEvent;
	
	public AbstractEvent getSdpEvent() {
		Date start = new Date();
		log.debug("getSdpEvent() start at: "+start);
		if(this.sdpEvent==null){
			sdpEvent = new AbstractEvent(){
				private static final long serialVersionUID = -8509096512808138104L;
				
				@Override
				protected void reportError(String msg) {
					if(msg!=null)
						report(new MSG(CommonValidator.MSG_ERROR,msg));
				}
				
				@Override
				public String getProgramName() {
					return SdpEventCreation.this.getProgramName();
				}
				
				@Override
				public String getEventID() {
					return SdpEventCreation.this.getEventID();
				}
			};
		}
		
		Date end = new Date();
		log.debug("getSdpEvent() end at: "+end +"cost :" +(end.getTime()-start.getTime()));
		return sdpEvent;
	}

	private String eventID;

	public SdpEventCreation(String programName) {
		super(programName);
	}
	
	protected String getEventID() {
		if(eventID==null)
			eventID = EventUtil.getUniqueEventName(this.getProgramName()+"_");
		return eventID;
	}

	
	@Override
	protected Wizard createWizard() {
		Wizard wizard = new Wizard("drEventSchedule",
				"locationSelection","confirmation");
		return wizard;
	}
	
	@Override
	protected void installValidators() {
		Date start = new Date();
		log.debug("installValidators() start at: "+start);
		
		CommonValidator val = new  CommonValidator<AbstractEventCreation>() {
			private static final long serialVersionUID = -4749519418706486685L;
			
			@Override
			public MSG validate(AbstractEventCreation model) {
				SdpEventCreation evtmodel = (SdpEventCreation) model;
				AbstractEvent demo = evtmodel.getSdpEvent();
				String msg = null;
				msg = model.validateTiming();
				//rate is required
				// comment length should not be more than 255
				if(msg!=null&&msg.trim().length()>0){
					return new MSG(CommonValidator.MSG_ERROR,msg);
				}
				msg = demo.validateRate();
				//rate is required
				// comment length should not be more than 255
				if(msg!=null&&msg.trim().length()>0){
					return new MSG(CommonValidator.MSG_ERROR,msg);
				}
				msg = demo.validateComment();
				//rate is required
				// comment length should not be more than 255
				if(msg!=null&&msg.trim().length()>0){
					return new MSG(CommonValidator.MSG_ERROR,msg);
				}
				return null;
			}
		};
		registerValidator("drEventSchedule", val);
		registerValidator("locationSelection", new EventLocationValidator());
		registerValidator("confirmation", new CompoundValidator(
				val,new EventLocationValidator()));
		
		Date end = new Date();
		log.debug("installValidators() end at: "+end +"cost :" +(end.getTime()-start.getTime()));
	}
	@Override
	public TreeContentProvider<UiEventDetail> getSelectedLocations() {
		
		return new LazyListContentProvider<UiEventDetail>(){
			private static final long serialVersionUID = -1859578720476173504L;

			@Override
			public List<UiEventDetail> doGetContents() {
				List<Location> temp = getLocationSelection().getSelectedParticipant();
				List<UiEventDetail> result = new ArrayList<UiEventDetail>();
				AbstractEvent sdp = getSdpEvent();//TODO: when to apply these info,? 
				Event event = convert2Entiry(sdp);
				for(int i=0; i<temp.size(); i++){
					EventDetail el = new EventDetail();
					el.setEvent(event);
					el.setLocation(temp.get(i));
					if(getEventTiming().getEndTime()!=null){
						if(sdp.isEstimated()){
							el.setEstimatedEndTime(getEventTiming().getEndTime());
						}else{
							el.setActualEndTime(getEventTiming().getEndTime());
						}
					}
					EventDetailAdapter ui = new EventDetailAdapter();
					ui.setEvtDetail(el);
					ui.setRowIndex(i);
					result.add(ui);
				}
				return result;
			}
			
			private Event convert2Entiry(AbstractEvent sdp) {
				Event event = new Event();
				event.setStartTime(getEventTiming().getStartTime());
				event.setComment(sdp.getComments());
				event.setProgramName(sdp.getProgramName());
				event.setProduct(sdp.getRates());
				return event;
			}
		};
		
	}
	
	// hook method
	@Override
	protected void doCreateEvent(){
		Date start = new Date();
		log.debug("doCreateEvent() start at: "+start);
		Event event = new Event();
		AbstractEvent sdp = getSdpEvent();
		event.setStartTime(this.getEventTiming().getStartTime());
		event.setIssuedTime(this.getEventTiming().getIssuedTime()==null?new Date():this.getEventTiming().getIssuedTime());
		event.setComment(sdp.getComments());
		event.setProgramName(sdp.getProgramName());
		event.setProduct(sdp.getRates());
		Set<EventDetail> createdDetails=new HashSet<EventDetail>();
		//TODO: filter by category, all ?
		List<Location> locations = getLocationSelection().getSelectedParticipant();
		Date estimatedEndTime = null;
		Date actualEndTime = null;
		if(this.getEventTiming().getEndTime()!=null){
		if(sdp.isEstimated()){
			estimatedEndTime = this.getEventTiming().getEndTime();
		}else{
			actualEndTime = this.getEventTiming().getEndTime();
		}
	}
		getEvtManager().createEvent(event, locations, actualEndTime, estimatedEndTime);
		
	}
	
	public String getEventName(){
		if(eventName==null){
			eventName=EventUtil.getUniqueEventName(this.getProgramName()+"_");
		}
		return eventName;
	}


	@Override
	public String[] getDispatchByLabels() {
		return DISPATCH_LABEL;
	}
	public static String[] DISPATCH_LABEL = new String[]{
		"SLAP",
		"ABank",
		"Substation"
	};

	@Override
	public Date getStartTime() {
		return getEventTiming().getStartTime();
	}

	@Override
	public Date getEndTime() {
		return getEventTiming().getEndTime();
	}

	@Override
	public Date getIssuedTime() {
		return getEventTiming().getIssuedTime();
	}
	
	
	
	
}
