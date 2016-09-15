package com.akuacom.pss2.web.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.openadr.dras.eventinfo.EventInfoValue;

import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalLevelDef;
import com.akuacom.utils.lang.DateUtil;


public class JSFDemoEventInfo implements EventConstants, Serializable {

	private static final long serialVersionUID = 6738929647465493814L;

	/** event time **/
	private Date dateTime;

	/** offset time in minutes from notification time **/
	private int offsetFromNotification;

	/** event status, FAR, NEAR, Active **/
	private String eventStatus;

	/** can be edited in user interface **/
	private boolean readOnly;

	/** can be removed or not **/
	private boolean removable = false;

	/** can add another event instance after this **/
	private boolean addable = false;

	/** event it, just to identify the event in UI layer **/
	private String id;

	private boolean offsetEditable = false;

	//type names are in order of alphabet
	private List<String> enabledSignalTypes;


	private Map<String,Double> signalValues;

	/**
	 * @param dateTime start time
	 * @param offset   offset from notification
	 * @param eventStatus event status
	 * @param signalTypes enabled signal types, must in order of alphabet order
	 */
	public JSFDemoEventInfo(Date dateTime,int offset,String eventStatus,
				List<String> signalTypes,boolean readOnly){
		this.dateTime = dateTime;
		this.offsetFromNotification = offset;
		this.eventStatus = eventStatus;
		this.id=UUID.randomUUID().toString();
		if(signalTypes==null )
			throw new IllegalArgumentException("signal types must not be null");
		this.enabledSignalTypes = signalTypes;
		this.readOnly = readOnly;
		if(!readOnly){
			signalValues =new HashMap<String,Double>();
			for(String type:enabledSignalTypes){
				signalValues.put(type, getDefaultValue(type));
			}
		}
	}

	public JSFDemoEventInfo(Date dateTime,int offset,String eventStatus,List<String> signalTypes){
		this(dateTime,offset,eventStatus,signalTypes,false);
	}

	public JSFDemoEventInfo(Date dateTime,int offset,String eventStatus,
			List<String> signalTypes, MODEL_LEVELS defaultLevel){
		this(dateTime,offset,eventStatus,signalTypes);
		this.signalValues.put(SIGNAL_MODE, defaultLevel.getValue());
	}

	@SuppressWarnings("unchecked")
	public static JSFDemoEventInfo nextEvent(JSFDemoEventInfo thisEvent,int interval){
		JSFDemoEventInfo info= new JSFDemoEventInfo(
				DateUtil.offSetBy(thisEvent.getDateTime(), interval),//datetime
				thisEvent.getOffsetFromNotification()+interval, //offset
				"active", //event status
				 thisEvent.enabledSignalTypes
		 );
		//copy all signal values
		info.signalValues = (Map<String,Double>) ((HashMap<String,Double>)thisEvent.signalValues).clone();
		info.setOffsetEditable(true);

		info.id=UUID.randomUUID().toString();
		return info;
	}

	public List<SelectItem> getSignalModes(){
		List<SelectItem> availableTypes = new ArrayList<SelectItem>();
		for(MODEL_LEVELS level:MODEL_LEVELS.class.getEnumConstants()){
			availableTypes.add(new SelectItem(level.getValue(),level.getLabel()));
		}
		return availableTypes;
	}


	public EventInfoValue toEventInfoValue(String signalType,int notice){
		EventInfoValue value = new EventInfoValue();
		//start time is offset from event start point, in second
		value.setStartTime(60.0*(getOffsetFromNotification()-notice));

		// TODO: not sure what this code is for?
//		if(enabledSignalWrappers!=null){
//			for(SignalDefWrapper wrapper:enabledSignalWrappers){
//				if(wrapper.getSignalDefName().equalsIgnoreCase(signalType)){
//					value.setValue(wrapper.getSelectValue());
//					break;
//				}
//			}
//		}

//		value.setValue(signalValues.get(signalType));

		//DRMS-6087,5694,5632
		//First get value from the enabledSignalWrapper if the signalType is kind of LOAD_LEVEL
		//If the signalType is not belong to the LOAD_LEVEL
		//When get value from the signalValues map
		boolean isLoadLevelSignalType=false;
		if(enabledSignalWrappers!=null){
			for(SignalDefWrapper wrapper:enabledSignalWrappers){
				if(wrapper.getSignalDefName().equalsIgnoreCase(signalType)){
					if(wrapper.getSignalDef().getType().equalsIgnoreCase("LOAD_LEVEL")){
						isLoadLevelSignalType = true;
						if(wrapper.getSignalDefName().equalsIgnoreCase("mode")){
							value.setValue(wrapper.getModeValue());
						}else{
							value.setValue(wrapper.getSelectValue());
						}
						break;
					}
				}
			}
		}

		if(!isLoadLevelSignalType){
			value.setValue(signalValues.get(signalType));
		}

		return value;
	}


	public double getSignalValue(String signalType){
		return this.signalValues.get(signalType);
	}

	public void setSignalValue(String signalType,double value){
		int idx = enabledSignalTypes.indexOf(signalType);
		if(idx <0)
			throw new IllegalArgumentException("No such signal type " + signalType);
		signalValues.put(signalType, value);
	}

	protected double getDefaultValue(String type){
		int idx = enabledSignalTypes.indexOf(type);
		if(idx <0)
			throw new IllegalArgumentException("No such signal type " + type);
		if(type.equals(SIGNAL_MODE))
			return 1.0;
		else
			return 0.0;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public int getOffsetFromNotification() {
		return offsetFromNotification;
	}

	public void setOffsetFromNotification(int offsetFromNotification) {
		this.offsetFromNotification = offsetFromNotification;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isRemovable() {
		return removable;
	}

	public boolean isAddable() {
		return addable;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public void setRemovable(boolean removable) {
		this.removable = removable;
	}

	public void setAddable(boolean addable) {
		this.addable = addable;
	}

	public boolean isOffsetEditable() {
		return offsetEditable;
	}

	public void setOffsetEditable(boolean offsetEditable) {
		this.offsetEditable = offsetEditable;
	}

	public Map<String, Double> getSignalValues() {
		return signalValues;
	}


	//------------------------------------------------------------------------------
	//DRMS-4687
	public JSFDemoEventInfo(Date dateTime,int offset,String eventStatus,
			List<String> signalTypes,boolean readOnly,List<SignalDef> enabledSignals){
		this.dateTime = dateTime;
		this.offsetFromNotification = offset;
		this.eventStatus = eventStatus;
		this.id=UUID.randomUUID().toString();
		if(signalTypes==null )
			throw new IllegalArgumentException("signal types must not be null");
		this.enabledSignalTypes = signalTypes;
		this.readOnly = readOnly;
		if(!readOnly){
			signalValues =new HashMap<String,Double>();
			for(String type:enabledSignalTypes){
				signalValues.put(type, getDefaultValue(type));
			}
		}
		this.enabledSignals=enabledSignals;
		buildEnabledSignalWrappers();
	}
	public JSFDemoEventInfo(Date dateTime,int offset,String eventStatus,
			List<String> signalTypes, MODEL_LEVELS defaultLevel,List<SignalDef> enabledSignals){
		this(dateTime,offset,eventStatus,signalTypes);
		this.signalValues.put(SIGNAL_MODE, defaultLevel.getValue());
		this.enabledSignals=enabledSignals;
		buildEnabledSignalWrappers();
	}
	public JSFDemoEventInfo(Date dateTime,int offset,String eventStatus,List<String> signalTypes,List<SignalDef> enabledSignals){
		this(dateTime,offset,eventStatus,signalTypes,false);
		this.enabledSignals=enabledSignals;
		buildEnabledSignalWrappers();
	}
	public static JSFDemoEventInfo nextEvent(JSFDemoEventInfo thisEvent,int interval,List<SignalDef> enabledSignals){
		JSFDemoEventInfo info= new JSFDemoEventInfo(
				DateUtil.offSetBy(thisEvent.getDateTime(), interval),//datetime
				thisEvent.getOffsetFromNotification()+interval, //offset
				"active", //event status
				 thisEvent.enabledSignalTypes,enabledSignals
		 );
		//copy all signal values
		info.signalValues = (Map<String,Double>) ((HashMap<String,Double>)thisEvent.signalValues).clone();
		info.setOffsetEditable(true);

		info.id=UUID.randomUUID().toString();
		return info;
	}
	private List<SignalDef> enabledSignals;
	private List<SignalDefWrapper> enabledSignalWrappers;


	private List<SignalDefWrapper> toSignalDefWrapperList(List<SignalDef> list){
		List<SignalDefWrapper> result = new ArrayList<SignalDefWrapper>();
		for(SignalDef signalDef:list){
			SignalDefWrapper wrapper = new SignalDefWrapper(signalDef);
			result.add(wrapper);
		}
		return result;
	}
	private void buildEnabledSignalWrappers(){
		if(enabledSignals!=null){
			setEnabledSignalWrappers(toSignalDefWrapperList(enabledSignals));
		}
	}

	public void setEnabledSignalWrappers(List<SignalDefWrapper> enabledSignalWrappers) {
		this.enabledSignalWrappers = enabledSignalWrappers;
	}

	public List<SignalDefWrapper> getEnabledSignalWrappers() {
		return enabledSignalWrappers;
	}
}
