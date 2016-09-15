/**
 * 
 */
package com.akuacom.pss2.drw;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.drw.core.DRWebsiteProperty;
import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.core.Program;
import com.akuacom.pss2.drw.value.AlertValue;
import com.akuacom.pss2.drw.value.EventValue;
import com.akuacom.pss2.drw.value.WeatherValue;

/**
 * the interface DREventManager
 * 
 */
public interface CFEventManager {
    @Remote
    public interface R extends CFEventManager {

		@Local
		public interface L extends CFEventManager {}}
    @Local
    public interface L extends CFEventManager {}

	List<Program> getAllProgram();
	
	List<EventValue> getActiveEvent(List<String> programClass, boolean commercial);
	List<EventValue> getScheduledEvent(List<String> programClass, boolean commercial);
	
	List<EventValue> getHistoryEvent(String program, String product, 
			Date start, Date end, List<String> zipCodes);

	List<WeatherValue> getHistoryTems(String programName, Date startDate, Date endDate);

	List<WeatherValue> getForcast(Date date);

	List<EventValue> getListView(List<String> products, String county,
			String city, List<String> zipCodes);

	List<String> getCountyName();
	List<String> getCityName();
	List<String> getCity(String countyName);
	Date getWatherForeCastModifidDate();
	
	
	
	List<String> getKML4API();
	
	List<String> getKML4BIP();
	
	List<String> getKML4CBP();
	
	List<String> getKML4SDPC();
	
	List<String> getKML4SDPR();
	
	DRWebsiteProperty getDRWebsitePropertyByPropertyName(String propertyName);
	
	List<EventValue> getKioskEvent(boolean isActive);
	
	List<String> getAllBlock();

	/**
	 * @param products
	 * @param county
	 * @param city
	 * @param zipCodes
	 * @return
	 */
	List<EventValue> getListViewScheduled(List<String> products, String county,
			String city, List<String> zipCodes);

	/**
	 * @return
	 */
	List<Location> getLocationEntity();
	public List<String> getKML4Block(List<String> blocks);

	/**
	 * @param sqltemplate
	 * @param params
	 * @return
	 */
	List<EventValue> getLocationAndBlockEntry(String sqltemplate,
			Map<String, Object> params);

	/**
	 * @param programClass
	 * @return
	 */
	List<EventValue> getPSS2ActiveEvent(List<String> programClass);

	/**
	 * @param programClass
	 * @return
	 */
	List<EventValue> getPSS2ScheduledEvent(List<String> programClass);
	
	 List<String> getKML4EventDetails(String eventDetailIds);
	 List<String> getKML4EventDetails(String eventDetailIds,boolean consolidationSLAP);
	 List<EventValue> getPSS2ScheduledEvent4Mobile(List<String> programClass);
	 List<AlertValue> getAlertHistory(String deviceKey);
}
