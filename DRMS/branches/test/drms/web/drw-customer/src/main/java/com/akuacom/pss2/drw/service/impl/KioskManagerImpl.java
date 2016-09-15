package com.akuacom.pss2.drw.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.akuacom.pss2.drw.model.KioskEventDataModel;
import com.akuacom.pss2.drw.service.KioskManager;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.value.EventValue;

public class KioskManagerImpl implements KioskManager {
	
	/** logger **/
	private static final Logger log = Logger.getLogger(KioskManagerImpl.class);
	/** class instance **/
	private static KioskManagerImpl instance = new KioskManagerImpl();	
	/**
	 * Constructor
	 */
	private KioskManagerImpl(){
		super();
		initialize();
	}
	/**
	 * initialize business logic
	 */
	public void initialize(){
//		DRDataPool.getInstance().setKioskActiveEvents(getActiveEvents());
//		DRDataPool.getInstance().setKioskScheduledEvents(getScheduledEvents());
//		startDRTimer();
	}
	/**
	 * Function for start DR timer to retrieve data ,
	 * timer interval with 0.5 minutes
	 */
//	private void startDRTimer(){
//		Timer timer = new Timer();
//		timer.scheduleAtFixedRate(new DRKioskTimerTask(),0,30000);
//	}	
	/**
	 * Singleton function for get class instance
	 * @return
	 */
	public static KioskManagerImpl getInstance(){
		return instance;
	}
	@Override
	public List<KioskEventDataModel> getActiveEvents() {
		List<KioskEventDataModel> eventList = new ArrayList<KioskEventDataModel>();
		try{
			if(DRWUtil.getCFEventManager()!=null){
				List<EventValue> result = DRWUtil.getCFEventManager().getKioskEvent(true);
				for(EventValue event:result){
					eventList.add(new KioskEventDataModel(event));
				}
			}
		}catch(Exception e){
			log.error("DR webstie event manager get kiosk active events error: "+e.getMessage());
		}
		return eventList;
	}


	@Override
	public List<KioskEventDataModel> getScheduledEvents() {
		List<KioskEventDataModel> eventList = new ArrayList<KioskEventDataModel>();
		try{
			if(DRWUtil.getCFEventManager()!=null){
				List<EventValue> result = DRWUtil.getCFEventManager().getKioskEvent(false);
				for(EventValue event:result){
					eventList.add(new KioskEventDataModel(event));
				}
			}
		}catch(Exception e){
			log.error("DR webstie event manager get kiosk scheduled events error: "+e.getMessage());
		}
		return eventList;
	}

}
