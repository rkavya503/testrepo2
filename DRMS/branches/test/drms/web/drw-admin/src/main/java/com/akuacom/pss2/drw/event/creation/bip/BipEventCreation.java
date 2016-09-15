package com.akuacom.pss2.drw.event.creation.bip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.akuacom.jsf.model.LazyListContentProvider;
import com.akuacom.jsf.model.TreeContentProvider;
import com.akuacom.pss2.drw.core.Event;
import com.akuacom.pss2.drw.core.EventDetail;
import com.akuacom.pss2.drw.event.AbstractEvent;
import com.akuacom.pss2.drw.event.EventDetailAdapter;
import com.akuacom.pss2.drw.event.UiEventDetail;
import com.akuacom.pss2.drw.event.creation.AbstractEventCreation;
import com.akuacom.pss2.drw.event.creation.CommonValidator;
import com.akuacom.pss2.drw.event.creation.CommonValidator.MSG;
import com.akuacom.pss2.drw.event.creation.Wizard;
import com.akuacom.pss2.util.EventUtil;

public class BipEventCreation extends AbstractEventCreation implements Serializable {

	private static final long serialVersionUID = -7801600190498201386L;
	
	private AbstractEvent bipEvent;
	
	public AbstractEvent getEvent() {
		if(this.bipEvent==null){
			bipEvent = new AbstractEvent(){
				private static final long serialVersionUID = -8509096512808138104L;
				
				@Override
				protected void reportError(String msg) {
					if(msg!=null)
						report(new MSG(CommonValidator.MSG_ERROR,msg));
				}
				
				@Override
				public String getProgramName() {
					return BipEventCreation.this.getProgramName();
				}
				
				@Override
				public String getEventID() {
					return BipEventCreation.this.getEventID();
				}
			};
		}
		return bipEvent;
	}

	private String eventID;
	
	

	public BipEventCreation(String programName){
		super(programName);
	}
	
	protected String getEventID(){
		if(eventID==null)
			eventID = EventUtil.getUniqueEventName(this.getProgramName()+"_");
		return eventID;
	}

	
	
	@Override
	protected Wizard createWizard() {
		Wizard wizard = new Wizard("drEventSchedule",
				"confirmation");
		return wizard;
	}
	
	@Override
	protected void installValidators() {
		CommonValidator val = new  CommonValidator<AbstractEventCreation>(){
			private static final long serialVersionUID = -4749519418706486685L;
			
			@Override
			public MSG validate(AbstractEventCreation model) {
				BipEventCreation evtmodel = (BipEventCreation) model;
				AbstractEvent demo = evtmodel.getEvent();
				String msg = null;
				msg = model.validateTiming();
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
				
				msg = demo.validateProduct();
				//rate is required
				// comment length should not be more than 255
				if(msg!=null&&msg.trim().length()>0){
					return new MSG(CommonValidator.MSG_ERROR,msg);
				}
				
				msg = demo.validateBlocks();
				//rate is required
				// comment length should not be more than 255
				if(msg!=null&&msg.trim().length()>0){
					return new MSG(CommonValidator.MSG_ERROR,msg);
				}
				return null;
			}
		};
		registerValidator("drEventSchedule", val);
	}
	
	
	@Override
	protected void doCreateEvent(){
		Event event = new Event(); //entity 
		AbstractEvent sdp = getEvent();
		event.setStartTime(this.getEventTiming().getStartTime());
		event.setComment(sdp.getComments());
		event.setProgramName(sdp.getProgramName());
		event.setProduct(sdp.getProduct());
		
		Set<EventDetail> createdDetails=new HashSet<EventDetail>();
		int[] block = sdp.getBlock();
		for(int index:block){
			EventDetail detail=new EventDetail();
			detail.setBlockNames(AbstractEvent.BIP_ALL_LABEL[index]);
			if(this.getEventTiming().getEndTime()!=null){
				if(sdp.isEstimated()){
					detail.setEstimatedEndTime(this.getEventTiming().getEndTime());
				}else{
					detail.setActualEndTime(this.getEventTiming().getEndTime());
				}
			}
			
			detail.setLastModifiedTime(new Date());
			detail.setEvent(event);
			createdDetails.add(detail);
		}

		event.setDetails(createdDetails);
		
		getEvtManager().createEvent(event);
	}
	
	
	public String getEventName(){
		if(eventName==null){
			eventName=EventUtil.getUniqueEventName(this.getProgramName()+"_");
		}
		return eventName;
	}

	@Override
	public String[] getDispatchByLabels() {
		return null;
	}

	@Override
	public TreeContentProvider<UiEventDetail> getSelectedLocations() {
		return new LazyListContentProvider<UiEventDetail>(){
			private static final long serialVersionUID = -1859578720476173504L;

			@Override
			public List<UiEventDetail> doGetContents() {
				List<UiEventDetail> result = new ArrayList<UiEventDetail>();
				
				AbstractEvent bip = getEvent();//TODO: when to apply these info,? 
				Event event = convert2Entiry(bip);
				event.setProduct(bip.getProduct());
				
				Set<EventDetail> createdDetails=new HashSet<EventDetail>();
				int[] block = bip.getBlock();
				for(int index:block){
					EventDetail detail=new EventDetail();
					detail.setBlockNames(AbstractEvent.BIP_ALL_LABEL[index]);
					if(getEventTiming().getEndTime()!=null){
						if(bip.isEstimated()){
							detail.setEstimatedEndTime(getEventTiming().getEndTime());
						}else{
							detail.setActualEndTime(getEventTiming().getEndTime());
						}
					}
					
					detail.setLastModifiedTime(new Date());
					detail.setEvent(event);
					createdDetails.add(detail);
					EventDetailAdapter ui = new EventDetailAdapter();
					ui.setEvtDetail(detail);
					ui.setRowIndex(index);
					result.add(ui);
				}
				event.setDetails(createdDetails);
				return result;
			}

			private Event convert2Entiry(AbstractEvent sdp) {
				
				Event event = new Event();
				event.setStartTime(getEventTiming().getStartTime());
				event.setComment(sdp.getComments());
				event.setProgramName(sdp.getProgramName());
				
				return event;
			}
		};
	
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
	public Date getIssuedTime() {
		return getEventTiming().getIssuedTime();
	}
}
