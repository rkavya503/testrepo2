package com.akuacom.pss2.task;
 
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "timer_config")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
			@NamedQuery(
				name="TimerConfig.findByName",
				query="select distinct(t) from RoutineStatus t where t.name =:name ",
				hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
		})
public class TimerConfig extends BaseEntity {
	
	private static final long serialVersionUID = -659374934827518205L;
	
	public static String TYPE_NORMAL="NORMAL";
	public static String TYPE_CONTINUE_SCOPE="CONTINUE_SCOPE";
	public static String TYPE_SINGLE_SCOPE="SINGLE_SCOPE";
	
	private String name;
	private String type;
	private boolean timerEnable;
	private int invokeHour;
	private int invokeMin;
	private int invokeSec;
	private double interval;
	private int startYear;
	private int startMonth;
	private int startDay;
	private int endYear;
	private int endMonth;
	private int endDay;
	private int threshold;
	private int day;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the timerEnable
	 */
	public boolean isTimerEnable() {
		return timerEnable;
	}
	/**
	 * @param timerEnable the timerEnable to set
	 */
	public void setTimerEnable(boolean timerEnable) {
		this.timerEnable = timerEnable;
	}
	/**
	 * @return the invokeHour
	 */
	public int getInvokeHour() {
		return invokeHour;
	}
	/**
	 * @param invokeHour the invokeHour to set
	 */
	public void setInvokeHour(int invokeHour) {
		this.invokeHour = invokeHour;
	}
	/**
	 * @return the invokeMin
	 */
	public int getInvokeMin() {
		return invokeMin;
	}
	/**
	 * @param invokeMin the invokeMin to set
	 */
	public void setInvokeMin(int invokeMin) {
		this.invokeMin = invokeMin;
	}
	/**
	 * @return the invokeSec
	 */
	public int getInvokeSec() {
		return invokeSec;
	}
	/**
	 * @param invokeSec the invokeSec to set
	 */
	public void setInvokeSec(int invokeSec) {
		this.invokeSec = invokeSec;
	}
	/**
	 * @return the interval
	 */
	public double getInterval() {
		return interval;
	}
	/**
	 * @param interval the interval to set
	 */
	public void setInterval(double interval) {
		this.interval = interval;
	}
	/**
	 * @return the startYear
	 */
	public int getStartYear() {
		return startYear;
	}
	/**
	 * @param startYear the startYear to set
	 */
	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}
	/**
	 * @return the startMonth
	 */
	public int getStartMonth() {
		return startMonth;
	}
	/**
	 * @param startMonth the startMonth to set
	 */
	public void setStartMonth(int startMonth) {
		this.startMonth = startMonth;
	}
	/**
	 * @return the startDay
	 */
	public int getStartDay() {
		return startDay;
	}
	/**
	 * @param startDay the startDay to set
	 */
	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}
	/**
	 * @return the endYear
	 */
	public int getEndYear() {
		return endYear;
	}
	/**
	 * @param endYear the endYear to set
	 */
	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}
	/**
	 * @return the endMonth
	 */
	public int getEndMonth() {
		return endMonth;
	}
	/**
	 * @param endMonth the endMonth to set
	 */
	public void setEndMonth(int endMonth) {
		this.endMonth = endMonth;
	}
	/**
	 * @return the endDay
	 */
	public int getEndDay() {
		return endDay;
	}
	/**
	 * @param endDay the endDay to set
	 */
	public void setEndDay(int endDay) {
		this.endDay = endDay;
	}
	/**
	 * @return the threshold
	 */
	public int getThreshold() {
		return threshold;
	}
	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	} 
	
	
}
