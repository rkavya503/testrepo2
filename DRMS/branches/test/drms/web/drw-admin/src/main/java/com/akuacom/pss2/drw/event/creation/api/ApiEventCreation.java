package com.akuacom.pss2.drw.event.creation.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

public class ApiEventCreation extends AbstractEventCreation implements Serializable {

	private static final long serialVersionUID = -7801600190498201386L;
	
	private AbstractEvent event;
	
	public AbstractEvent getEvent() {
		if(this.event==null){
			event = new AbstractEvent(){
				private static final long serialVersionUID = -8509096512808138104L;
				
				@Override
				protected void reportError(String msg) {
					if(msg!=null)
						report(new MSG(CommonValidator.MSG_ERROR,msg));
				}
				
				@Override
				public String getProgramName() {
					return ApiEventCreation.this.getProgramName();
				}
				
				@Override
				public String getEventID() {
					return ApiEventCreation.this.getEventID();
				}
			};
		}
		return event;
	}

	private String eventID;
	private String productName;
	
	public ApiEventCreation(String programName, String productName){
		super(programName);
		this.productName = productName;
	}
	
	protected String getEventID(){
		if(eventID==null)
			eventID = EventUtil.getUniqueEventName(this.getProgramName()+"_");
		return eventID;
	}

	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	protected Wizard createWizard() {
		Wizard wizard = new Wizard("drEventSchedule",
				"locationSelection","confirmation");
		return wizard;
	}
	
	@Override
	protected void installValidators() {
		CommonValidator val = new  CommonValidator<AbstractEventCreation>(){
			private static final long serialVersionUID = -4749519418706486685L;
			
			@Override
			public MSG validate(AbstractEventCreation model) {
				ApiEventCreation evtmodel = (ApiEventCreation) model;
				AbstractEvent event = evtmodel.getEvent();
				String msg = null;
				msg = model.validateTiming();
				//rate is required
				// comment length should not be more than 255
				if(msg!=null&&msg.trim().length()>0){
					return new MSG(CommonValidator.MSG_ERROR,msg);
				}
				msg = event.validateComment();
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
	}
	
	@Override
	public TreeContentProvider<UiEventDetail> getSelectedLocations(){
		return new LazyListContentProvider<UiEventDetail>(){
			private static final long serialVersionUID = -1859578720476173504L;

			@Override
			public List<UiEventDetail> doGetContents() {
				List<Location> temp = getLocationSelection().getSelectedParticipant();
				List<UiEventDetail> result = new ArrayList<UiEventDetail>();
				AbstractEvent evt = getEvent();
				Event event = convert2Entiry(evt);
				for(int i=0; i<temp.size(); i++){
					EventDetail el = new EventDetail();
					el.setEvent(event);
					el.setLocation(temp.get(i));
					if(getEventTiming().getEndTime()!=null){
						if(evt.isEstimated()){
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
				event.setProduct(getProductName());
				return event;
			}
		};
	}
	
	@Override
	protected void doCreateEvent(){
		
		Event event = new Event();
		AbstractEvent sdp = getEvent();
		event.setStartTime(getEventTiming().getStartTime());
		event.setIssuedTime(this.getEventTiming().getIssuedTime()==null?new Date():this.getEventTiming().getIssuedTime());
		event.setComment(sdp.getComments());
		event.setProgramName(sdp.getProgramName());
		event.setProduct(getProductName());
		
		Date estimatedEndTime = null;
		Date actualEndTime = null;
		if(sdp.isEstimated()){
			estimatedEndTime = getEventTiming().getEndTime();
		}else{
			actualEndTime = getEventTiming().getEndTime();
		}
		
		getEvtManager().createEvent(event, getLocationSelection().getSelectedParticipant(), actualEndTime, estimatedEndTime);
	}
	
	public String getEventName(){
		if(eventName==null){
			eventName=EventUtil.getUniqueEventName(this.getProgramName()+"_");
		}
		return eventName;
	}
	

	@Override
	public Date getStartTime() {
		// TODO Auto-generated method stub
		return getEventTiming().getStartTime();
	}

	@Override
	public Date getEndTime() {
		// TODO Auto-generated method stub
		return getEventTiming().getEndTime();
	}

	@Override
	public String[] getDispatchByLabels() {
		return DISPATCH_LABEL;
	}

	public static String[] DISPATCH_LABEL = new String[]{
		"SLAP",
		"ABank"
	};
	
	@Override
	public Date getIssuedTime() {
		return getEventTiming().getIssuedTime();
	}
}
