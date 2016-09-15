
package com.akuacom.pss2.facdash.web.listener;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.pss2.weather.WeatherManager;


public class DRTimerTask extends java.util.TimerTask{ 
	private static WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
	
	private static final Logger log = Logger.getLogger(DRTimerTask.class);
	
	public DRTimerTask(ServletContext ctx){
		this.ctx = ctx;
	}
	
	private ServletContext ctx;
	@Override
	public void run() {
		try{
			updateStatus();
		}catch(Exception e){
			log.debug(LogUtils.createExceptionLogEntry(null, "Forcast_Weather", e));
		}
	}
	
	private void updateStatus(){
		WeatheRretriever weather  = ImplFactory.instance().getWeatherRetriever(manager.getConfig().getCountry());//config will be always exist, don't need to consider null pointer exception
		weather.retriveWeather();
	}
	

}