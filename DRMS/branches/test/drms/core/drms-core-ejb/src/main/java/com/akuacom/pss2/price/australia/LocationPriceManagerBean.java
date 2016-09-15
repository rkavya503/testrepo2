package com.akuacom.pss2.price.australia;

import java.util.Collection;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import org.apache.log4j.Logger;

import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.timer.TimerManagerBean;

@Stateless
public class LocationPriceManagerBean extends TimerManagerBean 
	implements LocationPriceManager.L,LocationPriceManager.R {

	@EJB 
	private SystemManager.L systemManager;
	
	@Resource
	private TimerService timerService;
	
	private static final int TIMER_INTERVAL = 2*60*1000; //2 min 
	
	private static final int PRICE_UPDATE_INTERVAL_MS = 5*60*1000; //min
	 
	private static final String TIMER= LocationPriceManager.class.getName();
	
    private static final Logger log = Logger.getLogger(LocationPriceManagerBean.class);
    
    //TODO enable cluster cache
    private static PriceRecord lastPrice; // cache 
    
    private String location;
    private int maxDelay;
    
    @PostConstruct
	public void init() {
		PSS2Features features=systemManager.getPss2Features();
		maxDelay=features.getAustraliaPriceMaxDelay();
		location=features.getAustraliaPriceLocation();
    }
    
	@Override
	public PriceRecord getPrice() {
		if(lastPrice!=null){
			Date current = new Date();
			//check if the price is out of date
			if(current.getTime() -lastPrice.getTime().getTime()>PRICE_UPDATE_INTERVAL_MS)
				return null;
		}
		return lastPrice;
	}
	
	protected void updatePriceIfNecessary(){
		Date current = new Date();
		PriceWSClient client = new PriceWSClient();
		if(lastPrice!=null){
			if(current.getTime() -lastPrice.getTime().getTime()	> PRICE_UPDATE_INTERVAL_MS){
				//need to get new price 
				lastPrice =client.getPrice(new Date(), location, maxDelay);
			}
		}
		else{
			lastPrice =client.getPrice(new Date(), location,maxDelay);
		}
	}
	
	@Override
	public void createTimers() {
		cancelTimers();
		timerService.createTimer(new Date(), 
					TIMER_INTERVAL
					,TIMER);
		log.debug(TIMER +" created successfully");
	}

	@Override
	public void cancelTimers() {
		Collection<?> timersList = timerService.getTimers();
	    super.cancelTimers(timersList);
	}
	
	@Override
	@Timeout
	public void timeoutHandler(Timer timer) {
		updatePriceIfNecessary();
	}

	@Override
	public String getTimersInfo() {
		Collection<?> timersList = timerService.getTimers();
		return super.getTimersInfo(timersList);
	}
}
