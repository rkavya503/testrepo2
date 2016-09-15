package com.akuacom.pss2.drw;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.akuacom.utils.DateUtil;

@XmlRootElement(name="eventStore")
public class EventStore {

	private HashMap<String,ArrayList<Event>> events;
	private String updateTime;
	private HashMap<String,Long> lastUpdateTime;
	private RTPWeather todayWeather;
	private List<RTPWeather> forecastWeather;

	@XmlElement(name="lastUpdateTime")
	@XmlJavaTypeAdapter(MapAdapter.class)
	public HashMap<String, Long> getLastUpdateTime() {
		lastUpdateTime = lastUpdateTime==null?new HashMap<String,Long>():lastUpdateTime;
		return lastUpdateTime;
	}

	public void setLastUpdateTime(HashMap<String, Long> lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@XmlElement(name="events")
	@XmlJavaTypeAdapter(StringVsEventListXmlAdapter.class)
	public HashMap<String, ArrayList<Event>> getEvents() {
		events = events==null?new HashMap<String,ArrayList<Event>>():events;
		return events;
	}

	public void setEvents(HashMap<String, ArrayList<Event>> events) {
		this.events = events;
	}

	@XmlElement(name="updateTime")
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = DateUtil.format(updateTime, "MM/dd/yyyy hh:mm a");
	}

	@XmlElement(name="todayWeather")
	public RTPWeather getTodayWeather() {
		return todayWeather;
	}

	public void setTodayWeather(RTPWeather todayWeather) {
		this.todayWeather = todayWeather;
	}

	@XmlElement(name="forecastWeather")
	public List<RTPWeather> getForecastWeather() {
		return forecastWeather;
	}

	public void setForecastWeather(List<RTPWeather> forecastWeather) {
		this.forecastWeather = forecastWeather;
	}
	

}
