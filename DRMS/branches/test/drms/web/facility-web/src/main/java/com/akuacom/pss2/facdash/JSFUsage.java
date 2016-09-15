
package com.akuacom.pss2.facdash;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.data.usage.UsageDataManager;
import com.akuacom.utils.DateUtil;


public class JSFUsage {

    private boolean showRawData;
    private double dayAvgBase = new Double(00.00);
    private double dayTotalBase= new Double(00.00);
    private double dayAvgActual= new Double(00.00);
    private double dayTotalActual = new Double(00.00);
    private double dayAvgShed= new Double(00.00);
    private double dayTotalShed= new Double(00.00);


    private double eventAvgBase= new Double(00.00);
    private double eventTotalBase= new Double(00.00);
    private double eventAvgActual= new Double(00.00);
    private double eventTotalActual= new Double(00.0);
    private double eventAvgShed= new Double(00.00);
    private double eventTotalShed= new Double(00.00);

    private String date;
    private String participantName;
    
    private boolean individualparticipant;

    public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}


    /** The Constant log. */
    private static final Logger log =
            Logger.getLogger(JSFUsage.class.getName());


    public JSFUsage(){
        this.getUsage(null);
    }

     public void getUsage(Date date){
            UsageDataManager um = (UsageDataManager)  EJBFactory.getBean(UsageDataManager.class);

            participantName =  FDUtils.getParticipantName();
            individualparticipant = um.isIndividualparticipant(participantName);
                //String eventName = (String)FDUtils.getEventTable().getEvents().get(0).getName();
                //UsageSummary UsageSummaryForEvent =  um.getUsageSummaryForEvent(eventName, participantName);
                //GregorianCalendar gc1 = new GregorianCalendar(1995, 11, 1, 3, 2, 1);
                //Date d1 = gc1.getTime();

         /*  Calendar calEnd = new GregorianCalendar();
           if(date==null){
        	   date = new Date();
           }
    	   calEnd.setTime(date);

           Date today = new Date();
       	   if(DateUtils.isSameDay(today, date)){
       		   calEnd.setTime(today);
       		   Date lastTime = um.getLastActualTime(participantName, DateUtil.stripTime(date));
       		   if((null!=lastTime)&&today.after(lastTime)&&DateUtils.isSameDay(today, lastTime)){
       			 calEnd.setTime(lastTime);
       		   }
    	   }else{
    		   calEnd = DateUtil.endOfDay(calEnd);
    	   }

            // Get Entire day usage data Start time is 00:00:00:00 of related date, end time is 23:59:59:999 of related date or the current time
           List<UsageSummary> UsageSummaryForEntireDayList =  (List<UsageSummary>)  um.getSummary(participantName, DateUtil.stripTime(date), calEnd.getTime(), showRawData);

           for(UsageSummary usageEntire: UsageSummaryForEntireDayList){

               if (usageEntire!=null && usageEntire.getName().equalsIgnoreCase("shed")){
                   this.setDayAvgShed(usageEntire.getAverage());
                   this.setDayTotalShed(calculateTotal(date, calEnd, today, usageEntire.getAverage()));
               }else  if (usageEntire!=null && usageEntire.getName().equalsIgnoreCase("baseline")){
                   this.setDayAvgBase(usageEntire.getAverage());
                   this.setDayTotalBase(calculateTotal(date, calEnd, today, usageEntire.getAverage()));
               }else if (usageEntire!=null && usageEntire.getName().equalsIgnoreCase("actual")){
                   this.setDayAvgActual(usageEntire.getAverage());
                   this.setDayTotalActual(calculateTotal(date, calEnd, today, usageEntire.getAverage()));
               }
           }

           List<UsageSummary> UsageSummaryForEventList =  (List<UsageSummary>)  um.getEventTimeSummary(participantName, DateUtil.stripTime(date), calEnd.getTime(), showRawData);
           for(UsageSummary usageEvent: UsageSummaryForEventList){

               if (usageEvent!=null && usageEvent.getName().equalsIgnoreCase("shed")){
                   this.setEventAvgShed(usageEvent.getAverage());
                   this.setEventTotalShed(calculateTotal(date, calEnd, today, usageEvent.getAverage()));
               }else  if (usageEvent!=null && usageEvent.getName().equalsIgnoreCase("baseline")){
                   this.setEventAvgBase(usageEvent.getAverage());
                   this.setEventTotalBase(calculateTotal(date, calEnd, today, usageEvent.getAverage()));
               }else if (usageEvent!=null && usageEvent.getName().equalsIgnoreCase("actual")){
                   this.setEventAvgActual(usageEvent.getAverage());
                   this.setEventTotalActual(calculateTotal(date, calEnd, today, usageEvent.getAverage()));
               }

           }
*/
    }

	private double calculateTotal(Date date, Calendar calEnd, Date today, double avg) {
		if(Double.isNaN(avg)){
			avg = 0;
		}
	   double total = 0;
	   double hours = (calEnd.getTime().getTime()-DateUtil.stripTime(date).getTime())/3600000.0;

	   if(DateUtils.isSameDay(today, date)){
		   total = avg*hours;
	   }else{
		   total = avg*24;
	   }
	   return total;
	}
     public void getUsageByDate(ActionEvent event){
//     	date = (String) event.getNewValue();
     	//event.getComponent().getV
     	DateFormat format = new  SimpleDateFormat("yyyy-MM-dd");
     	Date choosedate = null;
     	try {
 			choosedate = format.parse(date);
 		} catch (ParseException e) {
 			e.printStackTrace();
 		}
 		log.debug("date value "+date);
     	getUsage(choosedate);

    }
     
     public void getUsageByDate(){
//      	date = (String) event.getNewValue();
      	//event.getComponent().getV
      	DateFormat format = new  SimpleDateFormat("yyyy-MM-dd");
      	Date choosedate = null;
      	try {
  			choosedate = format.parse(date);
  		} catch (ParseException e) {
  			e.printStackTrace();
  		}
  		log.debug("date value "+date);
      	getUsage(choosedate);

     }


    public double getDayAvgActual() {
        if(Double.isNaN(dayAvgActual)){
    		return 0.0;
    	}
        return dayAvgActual;
    }

    public void setDayAvgActual(double dayAvgActual) {
        this.dayAvgActual = dayAvgActual;
    }

    public double getDayAvgBase() {
    	if(Double.isNaN(dayAvgBase)){
    		return 0.0;
    	}
        return dayAvgBase;
    }

    public void setDayAvgBase(double dayAvgBase) {
		this.dayAvgBase = dayAvgBase;
    }

    public double getDayAvgShed() {
        if(Double.isNaN(dayAvgShed)){
    		return 0.0;
    	}
        return dayAvgShed;
    }

    public void setDayAvgShed(double dayAvgShed) {
        this.dayAvgShed = dayAvgShed;
    }

    public double getDayTotalActual() {
         if(Double.isNaN(dayTotalActual)){
    		return 0.0;
    	}
        return dayTotalActual;
    }

    public void setDayTotalActual(double dayTotalActual) {
        this.dayTotalActual = dayTotalActual;
    }

    public double getDayTotalBase() {
    	if(Double.isNaN(dayTotalBase)){
    		return 0.0;
    	}
        return dayTotalBase;
    }

    public void setDayTotalBase(double dayTotalBase) {
        this.dayTotalBase = dayTotalBase;
    }

    public double getDayTotalShed() {
        if(Double.isNaN(dayTotalShed)){
    		return 0.0;
    	}
        return dayTotalShed;
    }

    public void setDayTotalShed(double dayTotalShed) {
        this.dayTotalShed = dayTotalShed;
    }

    public double getEventAvgActual() {
         if(Double.isNaN(eventAvgActual)){
    		return 0.0;
    	}
        return eventAvgActual;
    }

    public void setEventAvgActual(double eventAvgActual) {
        this.eventAvgActual = eventAvgActual;
    }

    public double getEventAvgBase() {
        if(Double.isNaN(eventAvgBase)){
    		return 0.0;
    	}
        return eventAvgBase;
    }

    public void setEventAvgBase(double eventAvgBase) {
        this.eventAvgBase = eventAvgBase;
    }

    public double getEventAvgShed() {
         if(Double.isNaN(eventAvgShed)){
    		return 0.0;
    	}
        return eventAvgShed;
    }

    public void setEventAvgShed(double eventAvgShed) {
        this.eventAvgShed = eventAvgShed;
    }

    public double getEventTotalActual() {
        if(Double.isNaN(eventTotalActual)){
    		return 0.0;
    	}
        return eventTotalActual;
    }

    public void setEventTotalActual(double eventTotalActual) {
        this.eventTotalActual = eventTotalActual;
    }

    public double getEventTotalBase() {
         if(Double.isNaN(eventTotalBase)){
    		return 0.0;
    	}
        return eventTotalBase;
    }

    public void setEventTotalBase(double eventTotalBase) {
        this.eventTotalBase = eventTotalBase;
    }

    public double getEventTotalShed() {
        if(Double.isNaN(eventTotalShed)){
    		return 0.0;
    	}
        return eventTotalShed;
    }

    public void setEventTotalShed(double eventTotalShed) {
        this.eventTotalShed = eventTotalShed;
    }

    public boolean getShowRawData() {
        return showRawData;
    }

    public void setShowRawData(boolean showRawData) {
	    this.showRawData = showRawData;
    }

	public String getParticipantName() {
		return participantName;
	}

	public boolean isIndividualparticipant() {
		return individualparticipant;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public void setIndividualparticipant(boolean individualparticipant) {
		this.individualparticipant = individualparticipant;
	}

}
